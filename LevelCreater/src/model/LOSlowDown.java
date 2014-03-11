package model;

import helper.Randomizer;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class LOSlowDown extends LOPolygon {

	public LOSlowDown() {
		this.objectColor = new Color(139, 69, 19);;
	}
	
	public LOSlowDown(Polygon wall) {
		this();
		this.polygon = calculatePolygonWithWall(wall);
	}

	public Polygon calculatePolygonWithWall(Polygon wall) {
		Polygon poly = new Polygon();
		
		
		int xSum = 0;
		int ySum = 0;
		int points = wall.npoints;
		for (int i = 0; i < points; i++) {
			xSum += wall.xpoints[i];
			ySum += wall.ypoints[i];
		}
		MapPoint center = new MapPoint(xSum / points, ySum / points);
		int x, y;
		int xP, yP;
		int length = 10;
		int xRandom, yRandom;
		for (int j = 0; j < points; j++) {
			xP = wall.xpoints[j];
			yP = wall.ypoints[j];
//			System.out.println();
//			System.out.println(center.x);
//			System.out.println(center.y);
//			System.out.println(xP);
//			System.out.println(yP);
//			System.out.println(center.x - (xP - center.x + length));
//			System.out.println(center.y - (yP - center.y + length));
			xRandom = Randomizer.randomIntFromInterval(-1, 1);
			yRandom = Randomizer.randomIntFromInterval(-1, 1);
			x = xP > center.x ? center.x - (xP - center.x) : center.x + (xP - center.x);
			y = yP > center.y ? center.y - (yP - center.y) : center.y + (yP - center.y);
			poly.addPoint(x + (xRandom * length), y + (yRandom * length));
		}
		
//		Rectangle bounds = wall.getBounds();
//		poly.addPoint(bounds.x, bounds.y);
//		poly.addPoint(bounds.x + bounds.width, bounds.y);
//		poly.addPoint(bounds.x + bounds.width, bounds.y + bounds.height);
//		poly.addPoint(bounds.x, bounds.y + bounds.height);
		return poly;
	}
	
}
