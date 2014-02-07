package model;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import main.LevelCreater;
import enums.StandardWall;

public class Level extends SwingWorker<Void, Void>{

	private ArrayList<LevelObject> levelObjectList;
	private LevelParameters levelParameters;
	private ArrayList<String> statusUpdates;
	private MapHandlerTest mht;
	
	public Level(LevelParameters levelParameters) {
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
		mht = new MapHandlerTest(levelParameters.getLevelWidth(), levelParameters.getLevelHeight(), 44);
		mht.makeCaverns();
		statusUpdates.add("cavern creation done");
		setProgress(40);
		mht.makeCaverns();
		statusUpdates.add("cavern creation done");
		setProgress(50);
		mht.makeCaverns();
		statusUpdates.add("cavern creation done");
		setProgress(60);
		mht.makeCaverns();
		statusUpdates.add("cavern creation done");
		setProgress(70);
		mht.makeCaverns();
		statusUpdates.add("cavern creation done");
		setProgress(80);
		mht.makeCaverns();
		statusUpdates.add("cavern creation done");
		setProgress(90);
		int test[][] = mht.regionLabeling(mht.map);
		//mht.sequentialLabeling(/*mht.map*/);
		mht.printMap(test);
		//createTestMap(mht.map);
		statusUpdates.add("map printing done");
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
        LevelCreater.getInstance().setCursor(null); //turn off the wait cursor
    }

	public void createLevel() {
		createFloor();
		createOutsideWalls();
		createRandomWaypoints();
		//createTestMap(mht.map);

		// createTestObject();
	}
	
	public void createTestMap(int[][] map) {
		int mapHeight = levelParameters.getLevelHeight();
		int mapWidth = levelParameters.getLevelWidth();
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				if(map[column][row] == 1) {
					System.out.println(column + " " + row);
					LOWall wall = new LOWall(new Point(column, row), 10, 10);
					this.addLevelObject(wall);
				}
			}
		}
	}

	private void createTestObject() {
		LOPolygon test = new LOPolygon();
		int[] xpoints = { -20, -10, -50, 100 };
		int[] ypoints = { -100, -23, 200, 400 };

		test.polygon = new Polygon(xpoints, ypoints, xpoints.length);
		this.addLevelObject(test);
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

	// testing purpose TODO: delete?
	public void createRandomWaypoints() {
		for (int i = 0; i < levelParameters.getNumOfWaypoints(); i++) {
			LOWaypoint wp = new LOWaypoint(true, 10, 10,
					levelParameters.getLevelWidth(),
					levelParameters.getLevelHeight());
			this.addLevelObject(wp);
		}
	}

	public void addLevelObject(LevelObject lo) {
		this.levelObjectList.add(lo);
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
