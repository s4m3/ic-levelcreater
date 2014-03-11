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
		
		
		int xSum = 0;
		int ySum = 0;
		int points = wall.npoints;
		for (int i = 0; i < points; i++) {
			xSum += wall.xpoints[i];
			ySum += wall.ypoints[i];
		}
		MapPoint center = new MapPoint(xSum / points, ySum / points);
		Rectangle bounds = wall.getBounds();
		ellipse.setFrame(bounds);
		return ellipse;
	}
	
}
