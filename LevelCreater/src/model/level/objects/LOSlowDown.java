package model.level.objects;

import java.awt.Color;
import java.awt.Polygon;

public class LOSlowDown extends LOPolygon {

	public LOSlowDown() {
		this.objectColor = new Color(139, 69, 19);
	}

	public LOSlowDown(Polygon poly) {
		this();
		this.polygon = poly;
	}

}
