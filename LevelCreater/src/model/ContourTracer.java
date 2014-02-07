package model;

import java.awt.Point;
import java.util.ArrayList;

public class ContourTracer {
	// TODO: proper constructor, isoutofbounds, usage

	private int[][] map;

	public ContourTracer(int[][] map) {
		this.map = map;
	}

	// trace one contour starting at (xS,yS) in direction dS
	ArrayList<Point> traceContour(int xS, int yS, int label, int dS) {
		ArrayList<Point> cont = new ArrayList<Point>();
		int xT, yT; // T = successor of starting point (xS,yS)
		int xP, yP; // P = previous contour point
		int xC, yC; // C = current contour point
		Point pt = new Point(xS, yS);
		int dNext = findNextPoint(pt, dS);
		cont.add(pt);
		xP = xS;
		yP = yS;
		xC = xT = pt.x;
		yC = yT = pt.y;

		boolean done = (xS == xT && yS == yT); // true if isolated pixel

		while (!done) {
			// labelArray[yC][xC] = label;
			pt = new Point(xC, yC);
			int dSearch = (dNext + 6) % 8;
			dNext = findNextPoint(pt, dSearch);
			xP = xC;
			yP = yC;
			xC = pt.x;
			yC = pt.y;
			// are we back at the starting position?
			done = (xP == xS && yP == yS && xC == xT && yC == yT);
			if (!done) {
				cont.add(pt);
			}
		}
		return cont;
	}

	int findNextPoint(Point pt, int dir) {
		// starts at Point pt in direction dir, returns the
		// ﬁnal tracing direction, and modiﬁes pt
		final int[][] delta = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 },
				{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };
		for (int i = 0; i < 7; i++) {
			int x = pt.x + delta[dir][0];
			int y = pt.y + delta[dir][1];
			if (map[y][x] == 0) { // TODO: is this correct x and y???
				dir = (dir + 1) % 8;
			} else {
				pt.x = x;
				pt.y = y;
				break;
			}
			// if (pixelArray[y][x] == BACKGROUND) {
			// // mark surrounding background pixels
			// labelArray[y][x] = -1;
			// dir = (dir + 1) % 8;
			// } else { // found a nonbackground pixel
			// pt.x = x;
			// pt.y = y;
			// break;
			// }
		}
		return dir;
	}

}
