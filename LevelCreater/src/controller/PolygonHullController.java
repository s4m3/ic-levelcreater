package controller;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PolygonHullController {

	public Polygon getPolygonHullOfPoints(ArrayList<Point> points) {
		ArrayList<Point> xSorted = (ArrayList<Point>) points.clone();
		Collections.sort(xSorted, new XCompare());

		int n = xSorted.size();

		Point[] lUpper = new Point[n];

		lUpper[0] = xSorted.get(0);
		lUpper[1] = xSorted.get(1);

		int lUpperSize = 2;

		for (int i = 2; i < n; i++) {
			lUpper[lUpperSize] = xSorted.get(i);
			lUpperSize++;

			while (lUpperSize > 2
					&& !rightTurn(lUpper[lUpperSize - 3],
							lUpper[lUpperSize - 2], lUpper[lUpperSize - 1])) {
				// Remove the middle point of the three last
				lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
				lUpperSize--;
			}
		}

		Point[] lLower = new Point[n];

		lLower[0] = xSorted.get(n - 1);
		lLower[1] = xSorted.get(n - 2);

		int lLowerSize = 2;

		for (int i = n - 3; i >= 0; i--) {
			lLower[lLowerSize] = xSorted.get(i);
			lLowerSize++;

			while (lLowerSize > 2
					&& !rightTurn(lLower[lLowerSize - 3],
							lLower[lLowerSize - 2], lLower[lLowerSize - 1])) {
				// Remove the middle point of the three last
				lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
				lLowerSize--;
			}
		}

		Polygon result = new Polygon();

		for (int i = 0; i < lUpperSize; i++) {
			result.addPoint(lUpper[i].x, lUpper[i].y - 2);
		}

		for (int i = 1; i < lLowerSize - 1; i++) {
			result.addPoint(lLower[i].x, lLower[i].y + 2);
		}

		return result;
	}

	private boolean rightTurn(Point a, Point b, Point c) {
		return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) > 0;
	}

	private class XCompare implements Comparator<Point> {
		@Override
		public int compare(Point o1, Point o2) {
			return (new Integer(o1.x)).compareTo(new Integer(o2.x));
		}
	}
}
