package controller;

import java.util.ArrayList;

import model.MapPoint;

public class ContourTracer {
	// TODO: proper constructor, isoutofbounds, usage

	private int[][] map;
	private ArrayList<ArrayList<MapPoint>> contours;

	public ContourTracer(int[][] map) {
		this.map = map;
		contours = new ArrayList<ArrayList<MapPoint>>();
	}
	
	public void traceContours() {
		int label = 2;
		int mapHeight = map.length;
		int mapWidth = map[0].length;
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				if(map[column][row] == label) {
					ArrayList<MapPoint> pointList = traceContour(column, row, label, 1);
					this.contours.add(pointList);
					label++;
				}
			}
		}
	}

	// trace one contour starting at (xS,yS) in direction dS
	private ArrayList<MapPoint> traceContour(int xS, int yS, int label, int dS) {
		ArrayList<MapPoint> cont = new ArrayList<MapPoint>();
		int xT, yT; // T = successor of starting point (xS,yS)
		int xP, yP; // P = previous contour point
		int xC, yC; // C = current contour point
		MapPoint pt = new MapPoint(xS, yS);
		int dNext = findNextPoint(pt, dS);
		cont.add(pt);
		xP = xS;
		yP = yS;
		xC = xT = pt.x;
		yC = yT = pt.y;

		boolean done = (xS == xT && yS == yT); // true if isolated pixel

		while (!done) {
			// labelArray[yC][xC] = label;
			pt = new MapPoint(xC, yC);
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

	int findNextPoint(MapPoint pt, int dir) {
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

	public ArrayList<ArrayList<MapPoint>> getContours() {
		return contours;
	}
	
	

}
