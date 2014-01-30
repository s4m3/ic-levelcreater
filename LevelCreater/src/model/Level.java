package model;

import java.awt.Polygon;
import java.util.ArrayList;

import enums.StandardWall;

public class Level {

	private ArrayList<LevelObject> levelObjectList;
	private LevelParameters levelParameters;

	public Level(LevelParameters levelParameters) {
		levelObjectList = new ArrayList<LevelObject>();
		this.levelParameters = levelParameters;
	}

	public void createLevel() {
		createFloor();
		createOutsideWalls();
		createRandomWaypoints();

		// createTestObject();
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

}
