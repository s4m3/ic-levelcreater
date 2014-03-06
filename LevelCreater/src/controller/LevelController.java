package controller;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingWorker;

import main.LevelCreater;
import model.Contour;
import model.LOCircle;
import model.LOFloor;
import model.LOPolygon;
import model.LOWall;
import model.LOWaypoint;
import model.LevelObject;
import model.LevelParameters;
import model.MapPoint;
import enums.StandardWall;

public class LevelController extends SwingWorker<Void, Void> {

	private ArrayList<LevelObject> levelObjectList;
	private LevelParameters levelParameters;
	private ArrayList<String> statusUpdates;
	private CellularMapCreater cmc;
	
	public static final int CAVERN_ITERATIONS = 6;
	
	public static final int scale = 6;
	private static final int xTranslate = 3;
	private static final int yTranslate = 5;

	public LevelController(LevelParameters levelParameters) {
		levelObjectList = new ArrayList<LevelObject>();
		this.levelParameters = levelParameters;
		statusUpdates = new ArrayList<String>();
	}

	@Override
	protected Void doInBackground() throws Exception {
		setProgress(0);
		createFloor();
		setProgress(5);
		
		// TODO: update... testing purpose
		cmc = new CellularMapCreater(levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight(), 44);
		
		for (int i = 0; i < CAVERN_ITERATIONS; i++) {
			cmc.makeCaverns();
		}
		setProgress(10);
		statusUpdates.add("cavern creation done");
		
		//TODO: delete, just testing purpose...
		//cmc.printMap();
		
		int[][] createdMap = cmc.getMapWithLabeledRegions();
		setProgress(40);
		statusUpdates.add("region labeling done");
		
		//to have a closed outside polygon, close the polygon in the middle
		ArrayList<MapPoint> entrance = cmc.makeEntrance(createdMap);
		
		//cmc.printMap(createdMap);
		setProgress(65);

		//after opening the polygon, make a entrance polygon that closes that spot
		createEntranceClosingPolygon(entrance);
		setProgress(70);
		
		//cmc.convertRegionsToContour(createdMap, 2);
		setProgress(75);

		
		ContourTracer ct = new ContourTracer(createdMap);
		ct.findAllContours();
		setProgress(80);
		
		//TODO: maybe fix this? watch out for the hack in contour tracer for corner points!!
		//contour tracer changes x and y, therefore x and y need to be switched again
		ct.switchContourPointsXandY();
		ArrayList<Contour> contourList = ct.getContours();
		ArrayList<Contour> updatedContours = new ArrayList<Contour>();
		PolygonPointReducer ppr = new PolygonPointReducer();

		setProgress(90);
//		int t=0;
		for (Contour contour : contourList) {
//			System.out.println(t++);
//			System.out.println("before");
//			for (MapPoint mapPoint1 : contour.getPoints()) {
//				System.out.print(mapPoint1.x +":"+mapPoint1.y + "  ");
//			}
//			System.out.println();
			ArrayList<MapPoint> updatedPoints = 
					ppr.reduceWithTolerance((ArrayList<MapPoint>) contour.getPoints(), 1);
//			System.out.println("updated");
//			for (MapPoint mapPoint : updatedPoints) {
//				System.out.print(mapPoint.x +":"+mapPoint.y + "  ");
//			}
//			System.out.println();
			contour.setPoints(updatedPoints);
			updatedContours.add(contour);
		}
		
		setProgress(91);
		
		for (Contour contour : updatedContours) {
			LOPolygon poly = new LOWall((Polygon) contour.makePolygon());
			this.addLevelObject(poly);
		}
		
		setProgress(92);
		
		WaypointController wpController = new WaypointController(createdMap, this.getLevelObjectList());
		this.addLevelObjects(wpController.createWaypoints(levelParameters.getNumOfWaypoints()));

		setProgress(95);

		translateAndScaleLevelObjects();
		setProgress(100);
		return null;
	}



	/*
	 * Executed in event dispatching thread
	 */
	@Override
	public void done() {
		Toolkit.getDefaultToolkit().beep();
		LevelCreater.getInstance().createButton.setEnabled(true);
		LevelCreater.getInstance().setCursor(null); // turn off the wait cursor
	}

	public void createTestMap(int[][] map) {
		int mapHeight = levelParameters.getLevelHeight();
		int mapWidth = levelParameters.getLevelWidth();
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				if (map[column][row] == 1) {
					System.out.println(column + " " + row);
					LOWall wall = new LOWall(new Point(column, row), 10, 10);
					this.addLevelObject(wall);
				}
			}
		}
	}

	private void createTestObject() {
		LOPolygon test = new LOPolygon();
		int[] xpoints = { 0, 20, 20, 0 };
		int[] ypoints = { 0, 0, 20, 20 };

		test.setPolygon(new Polygon(xpoints, ypoints, xpoints.length));
		this.addLevelObject(test);
	}
	
	private void createEntranceClosingPolygon(ArrayList<MapPoint> points) {
		
		int m = points.size();
		int[] xPoints = new int[4];
		int[] yPoints = new int[4];
		
		xPoints[0] = points.get(0).x;
		yPoints[0] = points.get(0).y-2;
		
		xPoints[1] = points.get(m-1).x;
		yPoints[1] = points.get(m-1).y - 2;
		
		xPoints[2] = points.get(m-1).x;
		yPoints[2] = points.get(m-1).y + 2;
		
		xPoints[3] = points.get(0).x;
		yPoints[3] = points.get(0).y+2;
		
//		for (int i = 0; i < yPoints.length; i++) {
//			System.out.println(xPoints[i] +":"+yPoints[i]);
//		}
		LOWall wall = new LOWall(new Polygon(xPoints, yPoints, xPoints.length));
		this.addLevelObject(wall);
	}

	private void createFloor() {
		LOFloor levelFloor = new LOFloor(levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight());
		this.addLevelObject(levelFloor);

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
		this.addLevelObject(topWall);
		this.addLevelObject(bottomWall);
		this.addLevelObject(leftWall);
		this.addLevelObject(rightWall);
	}
	
	private void translateAndScaleLevelObjects() {
		List<LevelObject> levelObjs = this.getLevelObjectList();
		for (LevelObject levelObject : levelObjs) {
			if (levelObject instanceof LOPolygon) {
				LOPolygon poly = ((LOPolygon) levelObject);
				poly.scalePolygon(scale);
				poly.translatePolygon(xTranslate, yTranslate);
			} else if (levelObject instanceof LOCircle) {
				Ellipse2D.Double ellipse = ((LOCircle) levelObject).getEllipse();
				ellipse.height *= scale;
				ellipse.width *= scale;
				ellipse.x *= scale;
				ellipse.x += xTranslate;
				ellipse.y *= scale;
				ellipse.y += yTranslate;
			}
		}		
	}


	public void addLevelObject(LevelObject lo) {
		this.levelObjectList.add(lo);
	}
	
	public void addLevelObjects(List<LevelObject> loList) {
		this.levelObjectList.addAll(loList);
	}

	public ArrayList<LevelObject> getLevelObjectList() {
		return levelObjectList;
	}

	public LevelParameters getLevelParameters() {
		return levelParameters;
	}

	public ArrayList<String> getStatusUpdates() {
		return statusUpdates;
	}

}
