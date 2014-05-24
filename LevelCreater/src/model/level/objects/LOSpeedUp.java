package model.level.objects;

import java.awt.Color;
import java.awt.Polygon;

public class LOSpeedUp extends LOPolygon {

	private int lineWidth;

	public LOSpeedUp() {
		this.objectColor = new Color(127, 255, 212);
	}

	public LOSpeedUp(Polygon poly) {
		this();
		this.polygon = poly;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

}
