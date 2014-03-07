package model;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Level {
	private List<LevelObject> levelObjects;
	public Level() {
		this.levelObjects = new ArrayList<LevelObject>();
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
			if(lo instanceof LOPolygon) {
				poly = ((LOPolygon) lo).getPolygon();
				result += poly.npoints;
			} else if(lo instanceof LOCircle) {
				result++;
			}
		}
		return result;
	}
}
