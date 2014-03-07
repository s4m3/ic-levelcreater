package model;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class LOPolygon extends LevelObject {
	protected Polygon polygon;

	public LOPolygon() {
		this.objectColor = Color.BLACK;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}
	
	public Polygon getScaledPolygon(int scale) {
		int length = this.polygon.npoints;
		int[] xPoints = new int[length];
		int[] yPoints = new int[length];

		for (int i = 0; i < length; i++) {
			xPoints[i] = this.polygon.xpoints[i] * scale;
			yPoints[i] = this.polygon.ypoints[i] * scale;
		}
		
		return new Polygon(xPoints, yPoints, length);
		
		//AffineTransform at = new Affine
			//	this.polygon.ge
		//int[] xPoints = this.polygon;
	}

	public void scale(int scale) {
		int length = this.polygon.npoints;

		for (int i = 0; i < length; i++) {
			this.polygon.xpoints[i] *= scale;
			this.polygon.ypoints[i] *= scale;
		}
	}

	public void translate(int xtranslate, int ytranslate) {
		this.getPolygon().translate(xtranslate, ytranslate);
	}

}
