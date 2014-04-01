package model;

public class LevelParameters {

	private String levelName;
	private int levelWidth;
	private int levelHeight;
	private int numOfWaypoints;
	private int scale;
	private int obstacles;
	private double minSizeRegionInMapSizePercentage;

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getLevelWidth() {
		return levelWidth;
	}

	public void setLevelWidth(int levelWidth) {
		this.levelWidth = levelWidth;
	}

	public int getLevelHeight() {
		return levelHeight;
	}

	public void setLevelHeight(int levelHeight) {
		this.levelHeight = levelHeight;
	}

	public int getNumOfWaypoints() {
		return numOfWaypoints;
	}

	public void setNumOfWaypoints(int numOfWaypoints) {
		this.numOfWaypoints = numOfWaypoints;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getObstacles() {
		return obstacles;
	}

	public void setObstacles(int obstacles) {
		this.obstacles = obstacles;
	}

	public double getMinSizeRegionInMapSizePercentage() {
		return minSizeRegionInMapSizePercentage;
	}

	public void setMinSizeRegionInMapSizePercentage(
			double minSizeRegionInMapSizePercentage) {
		this.minSizeRegionInMapSizePercentage = minSizeRegionInMapSizePercentage;
	}

}
