package model;

import java.awt.Color;
import java.awt.Polygon;

public class LOPolygon extends LevelObject {
	protected Polygon polygon;

	public LOPolygon() {
		this.objectColor = Color.WHITE;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

}
