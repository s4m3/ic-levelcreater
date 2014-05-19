package controller;

import java.util.ArrayList;

import model.contour.Contour;
import model.level.MapPoint;

public class ContourTracer {
	static final byte FOREGROUND = 1;
	static final byte BACKGROUND = 0;

	private int[][] map;
	private int[][] labelArray;
	private int[][] pixelArray;
	private ArrayList<Contour> contours;

	int width;
	int height;
	
	int regionId = 0;

	public ContourTracer(int[][] map) {
		this.map = map;
		this.width = map.length;
		this.height = map[0].length;
		this.makeAuxArrays();
	}

	void makeAuxArrays() {
		int h = this.height;
		int w = this.width;
		pixelArray = new int[w + 2][h + 2];
		labelArray = new int[w + 2][h + 2];
		// initialize auxiliary arrays
		for (int v = 1; v < h + 1; v++) {
			for (int u = 1; u < w + 1; u++) {
				if (map[u - 1][v - 1] == 0)
					pixelArray[u][v] = BACKGROUND;
				else
					pixelArray[u][v] = FOREGROUND;
			}
		}
		
		
		//TODO: delete
//		for (int i = 0; i < pixelArray.length; i++) {
//			for (int j = 0; j < pixelArray[i].length; j++) {
//				System.out.print(pixelArray[i][j]);
//			}
//			System.out.println();
//		}
	}

	void findAllContours() {
		contours = new ArrayList<Contour>();
		int label = 0;
		// current label
		// scan top to bottom, left to right
		for (int v = 1; v < pixelArray.length - 1; v++) {
			label = 0; // no label
			for (int u = 1; u < pixelArray[v].length - 1; u++) {
				if (pixelArray[v][u] == FOREGROUND) {
					if (label != 0) {
						// keep using the same label
						labelArray[v][u] = label;
					} else {
						label = labelArray[v][u];
						if (label == 0) {
							// unlabeled—new outer contour
							regionId = regionId + 1;
							label = regionId;
							Contour oc = traceContour(u, v, label, 0);
							contours.add(oc);
							labelArray[v][u] = label;
						}
					}
				} else {
					// background pixel
					if (label != 0) {
						if (labelArray[v][u] == 0) {
							// unlabeled—new inner contour
							Contour ic = traceContour(u - 1, v, label, 0);
							//TODO: works but weird...
							//contours.add(ic);
						}
						label = 0;
					}
				}
			}
		}
//		//TODO: delete
//		System.out.println();
//		for (int i = 0; i < labelArray.length; i++) {
//			for (int j = 0; j < labelArray[i].length; j++) {
//				System.out.print(labelArray[i][j] >= 0 ? labelArray[i][j] : "#" );
//			}
//			System.out.println();
//		}
		// shift back to original coordinates
		Contour.moveContoursBy(contours, -1, -1);
		
		//first contours corner points are lost due to array index.
		Contour.addCornerPointsForOutsideContour(contours.get(0), pixelArray[0].length - 2, pixelArray.length - 2);
	}

	// trace one contour starting at (xS,yS) in direction dS
	private Contour traceContour(int xS, int yS, int label, int dS) {
		Contour cont = new Contour(label);
		int xT, yT; // T = successor of starting point (xS,yS)
		int xP, yP; // P = previous contour point
		int xC, yC; // C = current contour point
		MapPoint pt = new MapPoint(xS, yS);
		int dNext = findNextPoint(pt, dS);
		cont.addPoint(pt);
		xP = xS;
		yP = yS;
		xC = xT = pt.x;
		yC = yT = pt.y;

		boolean done = (xS == xT && yS == yT); // true if isolated pixel

		while (!done) {
			labelArray[yC][xC] = label;
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
//				if(label == 1) {
//					System.out.print(pt.x + ":" + pt.y + " ");
//				}
				cont.addPoint(pt);
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
			if (pixelArray[y][x] == BACKGROUND) {
				labelArray[y][x] = -1;
				dir = (dir + 1) % 8;
			} else {
				pt.x = x;
				pt.y = y;
				break;
			}
		}
		return dir;
	}

	public ArrayList<Contour> getContours() {
		return contours;
	}

	public void switchContourPointsXandY() {
		for (Contour contour : contours) {
			contour.switchPointsXandY();
		}
	}

}
