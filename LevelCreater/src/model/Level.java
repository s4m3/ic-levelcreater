package model;

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
		createOutsideWalls();
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

	public ArrayList<LevelObject> getLevelObjectList() {
		return levelObjectList;
	}

	public LevelParameters getLevelParameters() {
		return levelParameters;
	}

}
