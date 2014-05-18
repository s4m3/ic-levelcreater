package model;

import java.awt.Color;
import java.awt.Polygon;

public class LOSpeedUp extends LOPolygon {

	public LOSpeedUp() {
		this.objectColor = new Color(127, 255, 212);
	}

	public LOSpeedUp(Polygon poly) {
		this();
		this.polygon = poly;
	}

}
