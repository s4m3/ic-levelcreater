package model;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class LOCircledSlowDown extends LOCircle {
	public LOCircledSlowDown() {
		this.objectColor = new Color(139, 69, 19);
	}

	public LOCircledSlowDown(Polygon wall) {
		this();
		this.ellipse = calculateEllipseWithWall(wall);
	}

	public Ellipse2D.Double calculateEllipseWithWall(Polygon wall) {
		Ellipse2D.Double ellipse = new Ellipse2D.Double();

		Rectangle bounds = wall.getBounds();
		ellipse.setFrame(bounds);
		return ellipse;
	}

}
