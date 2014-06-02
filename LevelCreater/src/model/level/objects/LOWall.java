package model.level.objects;

import java.awt.Color;
import java.awt.Polygon;

public class LOWall extends LOPolygon {

	public LOWall() {
		this.objectColor = Color.DARK_GRAY;
	}

	public LOWall(Polygon shape) {
		this();
		this.polygon = shape;
	}

}
