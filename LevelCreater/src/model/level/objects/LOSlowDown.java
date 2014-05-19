package model.level.objects;

import helper.Randomizer;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;

import model.level.MapPoint;

public class LOSlowDown extends LOPolygon {

	public LOSlowDown() {
		this.objectColor = new Color(139, 69, 19);
	}

	public LOSlowDown(Polygon poly) {
		this();
		this.polygon = poly;
	}

	public void inflatePolygon() {
		Rectangle bounds = this.polygon.getBounds();
		int points = this.polygon.npoints;
		MapPoint center = new MapPoint(bounds.x + (bounds.width / 2), bounds.y
				+ (bounds.height / 2));

		// move polygon to origin, ...
		this.polygon.translate(-center.x, -center.y);
		// ...modify, ...
		double inflation;
		for (int i = 0; i < points; i++) {
			inflation = Randomizer.randomIntFromInterval(12, 16) / 10.0;
			this.polygon.xpoints[i] *= inflation;
			this.polygon.ypoints[i] *= inflation;
		}
		// ...and translate back to original position
		this.polygon.translate(center.x, center.y);
	}
}
