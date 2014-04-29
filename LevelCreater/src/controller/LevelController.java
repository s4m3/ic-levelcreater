package controller;

import helper.TimerThread;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import main.LevelCreater;
import model.Contour;
import model.LOCircle;
import model.LOCircledWall;
import model.LOFloor;
import model.LOPolygon;
import model.LOSlowDown;
import model.LOSpeedUp;
import model.LOWall;
import model.Level;
import model.LevelObject;
import model.LevelParameters;
import model.MapPoint;
import model.Path;
import enums.StandardWall;

public class LevelController extends SwingWorker<Void, Void> {

	private LevelParameters levelParameters;
	private ArrayList<String> statusUpdates;
	private CellularAutomaton cellAutomat;
	private TimerThread timerThread;
	private Level level;
	public WaypointController wpController;

	public static final int CAVERN_ITERATIONS = 6;

	public int scale = 1;
	private static int xTranslate = 0;
	private static int yTranslate = 0;

	public LevelController(LevelParameters levelParameters) {
		scale = levelParameters.getScale();
		level = new Level();
		this.levelParameters = levelParameters;
		statusUpdates = new ArrayList<String>();
	}

	@Override
	protected Void doInBackground() throws Exception {
		return runLevelCreation();

	}

	private Void runLevelCreation() {
		long start = System.currentTimeMillis();
		timerThread = new TimerThread();
		timerThread.run();
		setProgress(0);
		createFloor();
		setProgress(5);

		cellAutomat = new CellularAutomaton(levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight(),
				levelParameters.getObstacles());

		for (int i = 0; i < CAVERN_ITERATIONS; i++) {
			cellAutomat.makeCaverns();
		}
		setProgress(10);
		statusUpdates.add("cavern creation done");

		int[][] createdMap = cellAutomat.getMapWithLabeledRegions();
		setProgress(40);
		statusUpdates.add("region labeling done");

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

		// cmc.convertRegionsToContour(createdMap, 2);
		setProgress(75);

		// DELETE TOO SMALL REGIONS
		cleanupMap(createdMap, cellAutomat.getRegionSizeByLabel());

		ContourTracer contourTracer = new ContourTracer(createdMap);
		contourTracer.findAllContours();
		setProgress(80);
		statusUpdates.add("contours found");
		// TODO: maybe fix this? watch out for the hack in contour tracer for
		// corner points!!
		// contour tracer changes x and y, therefore x and y need to be switched
		// again
		contourTracer.switchContourPointsXandY();
		ArrayList<Contour> contourList = contourTracer.getContours();
		ArrayList<Contour> updatedContours = new ArrayList<Contour>();
		PolygonPointReducer ppr = new PolygonPointReducer();

		setProgress(90);
		// int t=0;
		for (Contour contour : contourList) {
			// System.out.println(t++);
			// System.out.println("before");
			// for (MapPoint mapPoint1 : contour.getPoints()) {
			// System.out.print(mapPoint1.x +":"+mapPoint1.y + "  ");
			// }
			// System.out.println();
			ArrayList<MapPoint> updatedPoints = ppr.reduceWithTolerance(
					(ArrayList<MapPoint>) contour.getPoints(), 1);
			// System.out.println("updated");
			// for (MapPoint mapPoint : updatedPoints) {
			// System.out.print(mapPoint.x +":"+mapPoint.y + "  ");
			// }
			// System.out.println();
			contour.setPoints(updatedPoints);
			updatedContours.add(contour);
		}
		statusUpdates.add("polygon points reduced");
		setProgress(91);

		Shape[] shapes = Contour.makePolygons(updatedContours);
		PolygonHullController polygonHullController = new PolygonHullController();

		for (int j = 1; j < shapes.length; j++) {
			if (shapes[j] instanceof Polygon) {
				LOPolygon p = new LOSlowDown((Polygon) shapes[j]);
				LOSlowDown slowDownObj = new LOSlowDown(
						polygonHullController.getPolygonHullOfPoints(p
								.getPolyPointList()));
				slowDownObj.inflatePolygon();
				// LOCircledSlowDown slowDownObj = new
				// LOCircledSlowDown((Polygon) shapes[j]);
				level.addLevelObject(slowDownObj);
			}
		}

		for (int i = 0; i < shapes.length; i++) {
			LevelObject lo = null;
			if (shapes[i] instanceof Polygon)
				lo = new LOWall((Polygon) shapes[i]);
			else if (shapes[i] instanceof Ellipse2D.Double)
				lo = new LOCircledWall((Ellipse2D.Double) shapes[i]);

			level.addLevelObject(lo);
		}
		// for (Contour contour : updatedContours) {
		// LOPolygon poly = new LOWall((Polygon) contour.makePolygon());
		// this.addLevelObject(poly);
		// }
		statusUpdates.add("wall polygon creation done");
		setProgress(92);

		wpController = new WaypointController(createdMap,
				level.getLevelObjects());

		setProgress(93);
		level.addLevelObjects(wpController.createWaypointsALT(levelParameters
				.getNumOfWaypoints()));

		setProgress(95);
		statusUpdates.add("waypoint creation done");

		// TODO: create Speed Ups
		Iterator<Path> pathIter = wpController.getPaths().iterator();
		Path path;
		while (pathIter.hasNext()) {
			path = (Path) pathIter.next();
			LOSpeedUp speedUpObject = new LOSpeedUp(
					polygonHullController.getPolygonHullOfPoints(path
							.getPathAsArrayListOfPoints()));
			level.addLevelObject(speedUpObject);

		}

		translateAndScaleLevelObjects();
		setProgress(99);

		long diff = System.currentTimeMillis() - start;

		String duration = String.format(
				"%02d:%02d:%02d:%03d",
				TimeUnit.MILLISECONDS.toHours(diff),
				TimeUnit.MILLISECONDS.toMinutes(diff)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(diff)),
				TimeUnit.MILLISECONDS.toSeconds(diff)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(diff)), diff % 1000);
		statusUpdates.add("creation took " + duration
				+ " to finish (hh:mm:ss:millis)");
		statusUpdates.add(String.format("Num of vertices: %d",
				level.getNumOfVertices()));
		setProgress(100);
		return null;
	}

	private void cleanupMap(int[][] map, Map<Integer, Integer> regionSizeByLabel) {
		int width = map.length;
		int height = map[0].length;
		int amountOfRegions = regionSizeByLabel.size();

		// save region numbers that must be deleted to iterate only once over
		// map.
		ArrayList<Integer> regionsToDelete = new ArrayList<Integer>(
				amountOfRegions);
		// mapsize * minsize percentage = min size of region
		int minSize = (int) ((width * height) * levelParameters
				.getMinSizeRegionInMapSizePercentage());

		// labels start with 2. -1,0,1 are reserved for waypoints, empty space
		// and walls
		for (int i = 2; i < amountOfRegions; i++) {
			// System.out.println(String.format("RegionSize. Num:%d, Size: %d",
			// i,
			// regionSizeByLabel.get(i)));

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

	/*
	 * Executed in event dispatching thread
	 */
	@Override
	public void done() {
		// Toolkit.getDefaultToolkit().beep();
		if (getProgress() < 100) {
			JOptionPane.showMessageDialog(null, "Something went wrong");
			setProgress(0);
			statusUpdates.add("Error: invalid level");
		}
		LevelCreater.getInstance().createButton.setEnabled(true);
		LevelCreater.getInstance().setCursor(null); // turn off the wait cursor
		timerThread.stopTimer();
	}

	public void createTestMap(int[][] map) {
		int mapHeight = levelParameters.getLevelHeight();
		int mapWidth = levelParameters.getLevelWidth();
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				if (map[column][row] == 1) {
					System.out.println(column + " " + row);
					LOWall wall = new LOWall(new Point(column, row), 10, 10);
					level.addLevelObject(wall);
				}
			}
		}
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

		// for (int i = 0; i < yPoints.length; i++) {
		// System.out.println(xPoints[i] +":"+yPoints[i]);
		// }
		LOWall wall = new LOWall(new Polygon(xPoints, yPoints, xPoints.length));
		level.addLevelObject(wall);
	}

	private void createFloor() {
		LOFloor levelFloor = new LOFloor(levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight());
		level.addLevelObject(levelFloor);

	}

	public void createOutsideWalls() {

		LOWall topWall = new LOWall(StandardWall.TOP,
				levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight(), 10);
		LOWall bottomWall = new LOWall(StandardWall.BOTTOM,
				levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight(), 10);
		LOWall leftWall = new LOWall(StandardWall.LEFT,
				levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight(), 10);
		LOWall rightWall = new LOWall(StandardWall.RIGHT,
				levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight(), 10);
		level.addLevelObject(topWall);
		level.addLevelObject(bottomWall);
		level.addLevelObject(leftWall);
		level.addLevelObject(rightWall);
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
				poly.translate(xTranslate, yTranslate);
			} else if (levelObject instanceof LOCircle) {
				LOCircle circle = (LOCircle) levelObject;
				circle.translate(xTranslate, yTranslate);
			}
		}
	}

	private void scaleLevelObjects() {
		for (LevelObject levelObject : level.getLevelObjects()) {
			if (levelObject instanceof LOPolygon) {
				LOPolygon poly = ((LOPolygon) levelObject);
				poly.scale(scale);
			} else if (levelObject instanceof LOCircle) {
				LOCircle circle = (LOCircle) levelObject;
				circle.scale(scale);
			}
		}
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

}
