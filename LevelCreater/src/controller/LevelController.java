package controller;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingWorker;

import main.LevelCreater;
import model.Contour;
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
	private int scale = 1;

	public LevelController(LevelParameters levelParameters) {
		levelObjectList = new ArrayList<LevelObject>();
		this.levelParameters = levelParameters;
		statusUpdates = new ArrayList<String>();
	}

	@Override
	protected Void doInBackground() throws Exception {
		setProgress(0);
		createLevel();
		setProgress(30);
		// TODO: update... testing purpose
		cmc = new CellularMapCreater(levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight(), 44);
		cmc.makeCaverns();
		setProgress(40);
		cmc.makeCaverns();
		setProgress(45);
		cmc.makeCaverns();
		setProgress(50);
		cmc.makeCaverns();
		setProgress(55);
		cmc.makeCaverns();
		setProgress(60);
		cmc.makeCaverns();
		statusUpdates.add("cavern creation done");
		cmc.printMap();
		int createdMap[][] = cmc.regionLabeling(cmc.map);
		setProgress(60);
		statusUpdates.add("region labeling done");
		//cmc.printMap(test);
//		ContourTracer ct = new ContourTracer(test);
//		ct.findAllContours();
//		createTestObject();
		//to have a closed outside polygon, close the polygon in the middle
		ArrayList<MapPoint> entrance = cmc.makeEntrance(createdMap);
		cmc.printMap(createdMap);
		setProgress(65);
		//after opening the polygon, make a entrance polygon that closes that spot
		createEntranceClosingPolygon(entrance);
		setProgress(70);
		//cmc.printMap(test);
		cmc.convertRegionsToContour(createdMap, 2);
		setProgress(75);
		//cmc.printMap(test);
		
//		cmc.printMap(test2);
		// createTestMap(mht.map);
		
		//TEST CONTOUR TRACER
//		int[][] multi = new int[][]{
//				  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				  { 0, 0, 0, 0, 0, 2, 2, 2, 2, 0, 3, 3, 3, 3, 3, 0, 0, 0, 0 },
//				  { 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0 },
//				  { 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0 },
//				  { 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 3, 3, 3, 0, 0, 0, 0, 0, 0 }
//				};
//		
//		System.out.println("value: " + multi[1][2]);
//		ContourTracer ct2 = new ContourTracer(multi);
//		ct2.findAllContours();
//		ArrayList<Contour> contourList2 = ct2.getContours();
//		Contour testC = contourList2.get(0);
//		List<MapPoint> points2 = testC.getPoints();
//		for (MapPoint mapPoint : points2) {
//			System.out.println(mapPoint.x +":"+ mapPoint.y);
//		}
//		LOWall wallp = new LOWall((Polygon) testC.makePolygon());
//		this.addLevelObject(wallp);
		//////////////
		
		ContourTracer ct = new ContourTracer(createdMap);
		ct.findAllContours();
		setProgress(80);
		
		//contour tracer changes x and y, therefore x and y need to be switched again
		ct.switchContourPointsXandY();
		ArrayList<Contour> contourList = ct.getContours();
		ArrayList<Contour> updatedContours = new ArrayList<Contour>();
		PolygonPointReducer ppr = new PolygonPointReducer();
		setProgress(85);
//		for (Contour contour : contourList) {
//			LOWall wallPoly = new LOWall((Polygon) contour.makePolygon());
//			this.addLevelObject(wallPoly);
//		}
		setProgress(90);
		for (Contour contour : contourList) {
			ArrayList<MapPoint> points = (ArrayList<MapPoint>) contour.getPoints();
			ArrayList<MapPoint> updatedPoints = ppr.reduceWithTolerance(points, 0);
			contour.setPoints(updatedPoints);
			updatedContours.add(contour);
		}
		
		setProgress(91);
		
//		System.out.println(updatedContours.size());
		for (Contour contour : updatedContours) {
			//System.out.println("label: " + contour.getLabel());
			LOPolygon poly = new LOWall((Polygon) contour.makePolygon());
			this.addLevelObject(poly);
		}
		
		WaypointController wpController = new WaypointController(createdMap, this.getLevelObjectList());
		this.addLevelObjects(wpController.createWaypoints(levelParameters.getNumOfWaypoints()));
		
//		for (ArrayList<MapPoint> pointList : pointLists) {
//			System.out.println("new pointList:");
//			for (MapPoint mapPoint : pointList) {
//				System.out.println("Point " +mapPoint.x+":"+ mapPoint.y);
//			}
//		}
		
		
		//TEST PPR

//		ArrayList<MapPoint> shape = new ArrayList<MapPoint>();
//		shape.add(new MapPoint(0, 100));
//		shape.add(new MapPoint(20, 0));
//		shape.add(new MapPoint(40, 300));
//		shape.add(new MapPoint(60, 0));
//		shape.add(new MapPoint(80, 20));
//		shape.add(new MapPoint(100, 30));
//		shape.add(new MapPoint(120, 50));
//		shape.add(new MapPoint(140, 200));
//		shape.add(new MapPoint(200, 0));
//		for (MapPoint mapPoint : shape) {
//			System.out.println(mapPoint.x +":"+ mapPoint.y);
//		}
//		ArrayList<MapPoint> reduced = ppr.reduceWithTolerance(shape, 50);
//		System.out.println("after");
//		for (MapPoint mapPoint : reduced) {
//			System.out.println(mapPoint.x +":"+ mapPoint.y);
//		}
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

	public void createLevel() {
		createFloor();
		//createOutsideWalls();
		//createRandomWaypoints();
		// createTestMap(mht.map);

		// createTestObject();
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
		LOFloor levelFloor = new LOFloor(levelParameters.getLevelWidth() * scale,
				levelParameters.getLevelHeight() * scale);
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
