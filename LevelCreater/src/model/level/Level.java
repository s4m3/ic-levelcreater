package model.level;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import model.level.objects.LOCircle;
import model.level.objects.LOFloor;
import model.level.objects.LOPolygon;
import model.level.objects.LOSlowDown;
import model.level.objects.LOSpeedUp;
import model.level.objects.LOWall;
import model.level.objects.LOWaypoint;
import model.level.objects.LevelObject;

public class Level {
	private List<LevelObject> levelObjects;

	public Level() {
		this.levelObjects = new ArrayList<LevelObject>();
	}

	public Level(List<LevelObject> levelObjs) {
		this.levelObjects = levelObjs;
	}

	public void addLevelObject(LevelObject lo) {
		this.levelObjects.add(lo);
	}

	public void addLevelObjects(List<LevelObject> loList) {
		this.levelObjects.addAll(loList);
	}

	public List<LevelObject> getLevelObjects() {
		return levelObjects;
	}

	public int getNumOfVertices() {
		int result = 0;
		Polygon poly;
		for (LevelObject lo : levelObjects) {
			if (lo instanceof LOPolygon) {
				poly = ((LOPolygon) lo).getPolygon();
				if (poly != null)
					result += poly.npoints;
			} else if (lo instanceof LOCircle) {
				result++;
			}
		}
		return result;
	}

	public ArrayList<LOPolygon> getWalls() {
		ArrayList<LOPolygon> ret = new ArrayList<LOPolygon>();
		for (LevelObject levelObj : levelObjects) {
			if (levelObj instanceof LOWall) {
				ret.add((LOWall) levelObj);
			}
		}
		return ret;
	}

	public ArrayList<LOCircle> getWaypoints() {
		ArrayList<LOCircle> ret = new ArrayList<LOCircle>();
		for (LevelObject levelObj : levelObjects) {
			if (levelObj instanceof LOWaypoint) {
				ret.add((LOWaypoint) levelObj);
			}
		}
		return ret;
	}

	public ArrayList<LOWaypoint> TESTgetAllWaypoints() {
		ArrayList<LOWaypoint> ret = new ArrayList<LOWaypoint>();
		for (LevelObject levelObj : levelObjects) {
			if (levelObj instanceof LOWaypoint) {
				ret.add((LOWaypoint) levelObj);
			}
		}
		return ret;
	}

	public ArrayList<LOPolygon> getSlowDowners() {
		ArrayList<LOPolygon> ret = new ArrayList<LOPolygon>();
		for (LevelObject levelObj : levelObjects) {
			if (levelObj instanceof LOSlowDown) {
				ret.add((LOSlowDown) levelObj);
			}
		}
		return ret;
	}

	public ArrayList<LOPolygon> getSpeedUps() {
		ArrayList<LOPolygon> ret = new ArrayList<LOPolygon>();
		for (LevelObject levelObj : levelObjects) {
			if (levelObj instanceof LOSpeedUp) {
				ret.add((LOSpeedUp) levelObj);
			}
		}
		return ret;
	}

	public ArrayList<LOPolygon> getFloor() {
		ArrayList<LOPolygon> ret = new ArrayList<LOPolygon>();
		for (LevelObject levelObj : levelObjects) {
			if (levelObj instanceof LOFloor) {
				ret.add((LOFloor) levelObj);
			}
		}
		return ret;
	}
}
