package controller;

import helper.Randomizer;
import helper.TimerThread;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import main.LevelCreator;
import model.astar.Path;
import model.contour.Contour;
import model.level.Level;
import model.level.LevelParameters;
import model.level.MapPoint;
import model.level.objects.LOCircle;
import model.level.objects.LOCircledWall;
import model.level.objects.LOFloor;
import model.level.objects.LOPolygon;
import model.level.objects.LOSlowDown;
import model.level.objects.LOSpeedUp;
import model.level.objects.LOWall;
import model.level.objects.LevelObject;

public class LevelController extends SwingWorker<Void, Void> {

	private LevelParameters levelParameters;
	private ArrayList<String> statusUpdates;
	private TimerThread timerThread;
	private Level level;
	private WaypointController wpController;

	public static final int CAVERN_ITERATIONS = 6;
	public static final int POLYGON_INFLATION_MIN = 11;
	public static final int POLYGON_INFLATION_MAX = 17;

	public int scale = 1;
	private static int xTranslate = 0;
	private static int yTranslate = 0;

	public LevelController(LevelParameters levelParameters) {
		scale = levelParameters.getScale() != 0 ? levelParameters.getScale() : 1;
		level = new Level();
		this.levelParameters = levelParameters;
		statusUpdates = new ArrayList<String>();
	}

	@Override
	protected Void doInBackground() throws Exception {
		try {
			runLevelCreation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private Void runLevelCreation() {

		// setup timer
		long start = System.currentTimeMillis();
		timerThread = new TimerThread();
		timerThread.run();

		setProgress(0);

		createFloor();

		setProgress(5);

		// setup cellular automaton
		CellularAutomaton cellAutomat = new CellularAutomaton(levelParameters.getLevelWidth(), levelParameters.getLevelHeight(),
				levelParameters.getObstacles());

		// create caverns
		for (int i = 0; i < CAVERN_ITERATIONS; i++) {
			cellAutomat.makeCaverns();
		}
		setProgress(10);
		statusUpdates.add("cavern creation done");

		// label regions and get copy, to keep original created map
		int[][] createdMap = cellAutomat.getMapWithLabeledRegions();
		setProgress(40);
		statusUpdates.add("region labeling done");

		// validate created map
		if (!cellAutomat.isValidMap(createdMap))
			return null;

		// to have a closed outside polygon, close the polygon in the middle
		ArrayList<MapPoint> entrance = cellAutomat.makeEntrance(createdMap);

		// cellAutomat.printMap(createdMap);
		setProgress(65);

		// after opening the polygon, make a entrance polygon that closes that
		// spot
		createEntranceClosingPolygon(entrance);
		setProgress(70);

		// delete regions that are to small
		cleanupMap(createdMap, cellAutomat.getRegionSizeByLabel());
		setProgress(75);
		statusUpdates.add("map cleanup done");
		// init contour tracer that creates polygons out of the labeled regions
		// map
		ContourTracer contourTracer = new ContourTracer(createdMap);

		// trace contours
		contourTracer.findAllContours();

		setProgress(80);
		statusUpdates.add("contours found");
		// TODO: maybe fix this? watch out for the hack in contour tracer for
		// corner points!!
		// contour tracer changes x and y, therefore x and y need to be switched again
		contourTracer.switchContourPointsXandY();

		// get traced contours
		ArrayList<Contour> contourList = contourTracer.getContours();

		// clean up contours by reducing polygon points that are unnecessary
		ArrayList<Contour> updatedContours = new ArrayList<Contour>();
		PolygonPointReducer polyPointReducer = new PolygonPointReducer();
		for (Contour contour : contourList) {
			ArrayList<MapPoint> updatedPoints = polyPointReducer
					.reduceWithTolerance((ArrayList<MapPoint>) contour.getPoints(), 1);

			contour.setPoints(updatedPoints);
			updatedContours.add(contour);
		}
		statusUpdates.add("polygon points reduced");
		setProgress(85);

		// finally create polygons out of the cleaned contours
		Shape[] shapes = Contour.makePolygons(updatedContours);
		for (int i = 0; i < shapes.length; i++) {
			LevelObject lo = null;
			if (shapes[i] instanceof Polygon)
				lo = new LOWall((Polygon) shapes[i]);
			else if (shapes[i] instanceof Ellipse2D.Double)
				lo = new LOCircledWall((Ellipse2D.Double) shapes[i]);

			level.addLevelObject(lo);
		}

		// init polygon hull controller and create inflated polygons of walls as
		// slow down objects surrounding the walls
		PolygonHullController polygonHullController = new PolygonHullController();
		int fractionOfLevelSize = this.getLevelParameters().getLevelWidth() * this.getLevelParameters().getLevelHeight() < 1000000 ? 1000
				: 10000;
		for (int j = 1; j < shapes.length; j++) {
			if (shapes[j] instanceof Polygon) {
				LOPolygon p = new LOSlowDown((Polygon) shapes[j]);
				Rectangle bounds = p.getPolygon().getBounds();

				// only add for large polygons
				if ((bounds.width * bounds.height) > ((this.getLevelParameters().getLevelWidth() * this.getLevelParameters()
						.getLevelHeight()) / fractionOfLevelSize)) {
					LOSlowDown slowDownObj = new LOSlowDown(polygonHullController.getPolygonHullOfPoints(p.getPolyPointList()));
					inflatePolygon(slowDownObj.getPolygon());
					level.addLevelObject(slowDownObj);
				}

			}
		}
		statusUpdates.add("wall polygon- and slow downers creation done");
		setProgress(89);

		statusUpdates.add("creating waypoints...");
		setProgress(90);

		// init waypoint controller
		wpController = new WaypointController(createdMap, level.getLevelObjects());
		// create waypoints
		List<LevelObject> waypoints = wpController.createWaypointsWithSections(levelParameters.getNumOfWaypoints());
		level.addLevelObjects(waypoints);

		setProgress(95);
		statusUpdates.add("waypoint creation done");

		Iterator<Path> pathIter = wpController.getPaths().iterator();
		Path path;
		while (pathIter.hasNext()) {
			path = pathIter.next();
			ArrayList<MapPoint> updatedPath = polyPointReducer.reduceWithTolerance(path.getNodesAsArrayList(), 1);
			Polygon speedUpPoly = new Polygon();
			for (MapPoint mapPoint : updatedPath) {
				speedUpPoly.addPoint(mapPoint.x, mapPoint.y);
			}
			LOSpeedUp speedUp = new LOSpeedUp(speedUpPoly);
			speedUp.setLineWidth(wpController.getWayPointSize());
			level.addLevelObject(speedUp);

		}

		// level can be scaled (see scale, xTransform and yTransform)
		translateAndScaleLevelObjects();

		setProgress(99);

		// print final progress time and num of vertices
		long diff = System.currentTimeMillis() - start;
		String duration = String.format("%02d:%02d:%02d:%03d", TimeUnit.MILLISECONDS.toHours(diff),
				TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
				TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)),
				diff % 1000);
		statusUpdates.add("creation took " + duration + " to finish (hh:mm:ss:millis)");
		statusUpdates.add(String.format("Num of vertices: %d", level.getNumOfVertices()));

		setProgress(100);
		return null;
	}

	private void cleanupMap(int[][] map, Map<Integer, Integer> regionSizeByLabel) {
		int width = map.length;
		int height = map[0].length;
		int amountOfRegions = regionSizeByLabel.size();

		// save region numbers that must be deleted to iterate only once over
		// map.
		ArrayList<Integer> regionsToDelete = new ArrayList<Integer>(amountOfRegions);
		// mapsize * minsize percentage = min size of region
		int minSize = (int) ((width * height) * levelParameters.getMinSizeRegionInMapSizePercentage());

		// labels start with 2. -1,0,1 are reserved for waypoints, empty space
		// and walls
		for (int i = 2; i < amountOfRegions; i++) {

			if (regionSizeByLabel.get(i) < minSize) {
				regionsToDelete.add(i);
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (regionsToDelete.contains(map[i][j])) {
					map[i][j] = 0;
				}
			}
		}

	}

	@Override
	public void done() {
		// Toolkit.getDefaultToolkit().beep();
		if (getProgress() < 100) {
			JOptionPane.showMessageDialog(null, "Error occured during Level Creation");
			setProgress(0);
			statusUpdates.add("Error: invalid level");
		}
		LevelCreator.getInstance().createButton.setEnabled(true);
		LevelCreator.getInstance().setCursor(null); // turn off the wait cursor
		timerThread.stopTimer();
	}

	private void createEntranceClosingPolygon(ArrayList<MapPoint> points) {

		int m = points.size();
		int[] xPoints = new int[4];
		int[] yPoints = new int[4];

		xPoints[0] = points.get(0).x;
		yPoints[0] = points.get(0).y - 2;

		xPoints[1] = points.get(m - 1).x;
		yPoints[1] = points.get(m - 1).y - 2;

		xPoints[2] = points.get(m - 1).x;
		yPoints[2] = points.get(m - 1).y + 2;

		xPoints[3] = points.get(0).x;
		yPoints[3] = points.get(0).y + 2;

		LOWall wall = new LOWall(new Polygon(xPoints, yPoints, xPoints.length));
		level.addLevelObject(wall);
	}

	private void createFloor() {
		LOFloor levelFloor = new LOFloor(levelParameters.getLevelWidth(), levelParameters.getLevelHeight());
		level.addLevelObject(levelFloor);

	}

	private void translateAndScaleLevelObjects() {
		if (scale != 1)
			scaleLevelObjects();
		if (xTranslate != 0 || yTranslate != 0)
			translateLevelObjects();
	}

	private void translateLevelObjects() {
		for (LevelObject levelObject : level.getLevelObjects()) {
			if (levelObject instanceof LOPolygon) {
				LOPolygon poly = ((LOPolygon) levelObject);
				if (poly != null)
					poly.translate(xTranslate, yTranslate);
			} else if (levelObject instanceof LOCircle) {
				LOCircle circle = (LOCircle) levelObject;
				if (circle != null)
					circle.translate(xTranslate, yTranslate);
			}
		}
	}

	private void scaleLevelObjects() {
		for (LevelObject levelObject : level.getLevelObjects()) {
			if (levelObject instanceof LOPolygon) {
				LOPolygon poly = ((LOPolygon) levelObject);
				if (poly != null)
					poly.scale(scale);
			} else if (levelObject instanceof LOCircle) {
				LOCircle circle = (LOCircle) levelObject;
				if (circle != null)
					circle.scale(scale);
			}
		}

		Iterator<Path> pathIter = wpController.getPaths().iterator();
		Path path;
		while (pathIter.hasNext()) {
			path = pathIter.next();
			path.scale(scale);
		}
	}

	// private void inflatePolygon2(Polygon polygon) {
	// AffineTransform test = new AffineTransform();
	// test.scale(10, 10);
	// double[] coords = new double[6];
	// Shape temp = new Polygon();
	// Polygon returnPoly = new Polygon();
	// for (PathIterator pathIter = temp.getPathIterator(test); !pathIter.isDone(); pathIter.next()) {
	// int type = pathIter.currentSegment(coords);
	// returnPoly.addPoint((int) coords[0], (int) coords[1]);
	// }
	// polygon = returnPoly;
	// }

	private void inflatePolygon(Polygon polygon) {
		Rectangle bounds = polygon.getBounds();

		// only inflate large polygons
		if ((bounds.width * bounds.height) < ((this.levelParameters.getLevelWidth() * this.levelParameters.getLevelHeight()) / 1000))
			return;

		int points = polygon.npoints;
		MapPoint center = new MapPoint((int) bounds.getCenterX(), (int) bounds.getCenterY());

		// move polygon to origin, ...
		polygon.translate(-center.x, -center.y);
		// ...modify, ...
		double inflation = Randomizer.randomIntFromInterval(POLYGON_INFLATION_MIN, POLYGON_INFLATION_MAX) / 10.0;
		int xBoundsMax = this.levelParameters.getLevelWidth() - center.x;
		int yBoundsMax = this.levelParameters.getLevelHeight() - center.y;
		int xBoundsMin = 0 - center.x;
		int yBoundsMin = 0 - center.y;
		int newX, newY;
		for (int i = 0; i < points; i++) {
			newX = (int) (polygon.xpoints[i] * inflation);
			polygon.xpoints[i] = clampValue(newX, xBoundsMin, xBoundsMax);

			newY = (int) (polygon.ypoints[i] * inflation);
			polygon.ypoints[i] = clampValue(newY, yBoundsMin, yBoundsMax);

		}
		// ...and translate back to original position
		polygon.translate(center.x, center.y);
	}

	private int clampValue(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public LevelParameters getLevelParameters() {
		return levelParameters;
	}

	public ArrayList<String> getStatusUpdates() {
		return statusUpdates;
	}

	public WaypointController getWpController() {
		return wpController;
	}

}
