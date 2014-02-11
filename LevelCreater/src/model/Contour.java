package model;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Contour {
	static int INITIAL_SIZE = 50;
	int label;
	List<MapPoint> points;
	
	Contour(int label, int size) {
		this.label = label;
		points = new ArrayList<MapPoint>(size);
	}

	public Contour(int label) {
		this.label = label;
		points = new ArrayList<MapPoint>(INITIAL_SIZE);
	}

	public void addPoint(MapPoint n) {
		points.add(n);
	}

	public Shape makePolygon() {
		int m = points.size();
		if (m > 1) {
			int[] xPoints = new int[m];
			int[] yPoints = new int[m];
			int k = 0;
			Iterator<MapPoint> itr = points.iterator();
			while (itr.hasNext() && k < m) {
				MapPoint cpt = itr.next();
				xPoints[k] = cpt.x;
				yPoints[k] = cpt.y;
				k = k + 1;
			}
			return new Polygon(xPoints, yPoints, m);
		} else {
			// use circles for isolated pixels
			MapPoint cpt = points.get(0);
			return new Ellipse2D.Double(cpt.x - 0.1, cpt.y - 0.1, 0.2, 0.2);
		}
	}

	static Shape[] makePolygons(List<Contour> contours) {
		if (contours == null)
			return null;
		else {
			Shape[] pa = new Shape[contours.size()];
			int i = 0;
			for (Contour c : contours) {
				pa[i] = c.makePolygon();
				i = i + 1;
			}
			return pa;
		}
	}

	void moveBy(int dx, int dy) {
		for (MapPoint pt : points) {
			pt.translate(dx, dy);
		}
	}

	public static void moveContoursBy (List<Contour> contours, int dx, int dy) {
		for (Contour c : contours) {
			c.moveBy(dx, dy);
		}
	}

	public List<MapPoint> getPoints() {
		return points;
	}

	public void setPoints(List<MapPoint> points) {
		this.points = points;
	}

	public int getLabel() {
		return label;
	}
	
	
	
}
