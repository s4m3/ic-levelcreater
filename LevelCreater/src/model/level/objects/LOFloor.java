package model.level.objects;

import java.awt.Color;
import java.awt.Polygon;

public class LOFloor extends LOPolygon {

	public LOFloor() {
		this.objectColor = new Color(51, 153, 51);
	}
	public LOFloor(int levelWidth, int levelHeight) {
		this();
		int[] xs = { 0, levelWidth, levelWidth, 0 };
		int[] ys = { 0, 0, levelHeight, levelHeight };
		this.polygon = new Polygon(xs, ys, xs.length);
	}
	
	public LOFloor(Polygon polygon) {
		this();
		this.polygon = polygon;
	}

}
