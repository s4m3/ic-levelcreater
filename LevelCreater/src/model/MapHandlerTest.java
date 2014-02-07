package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class MapHandlerTest {

	Random rand = new Random();

	public int[][] map;

	public int mapWidth;
	public int mapHeight;
	public int percentAreWalls;

	// private enum neighbors {
	// ALL_BACKGROUND, ONE, MANY
	// }

	public MapHandlerTest() {
		mapWidth = 40;
		mapHeight = 21;
		percentAreWalls = 40;

		randomFillMap();
	}

	public MapHandlerTest(int width, int height, int percentAreWalls) {
		this.mapWidth = width;
		this.mapHeight = height;
		this.percentAreWalls = percentAreWalls;

		randomFillMap();
	}

	public MapHandlerTest(int mapWidth, int mapHeight, int[][] map,
			int percentWalls) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.percentAreWalls = percentWalls == 0 ? 40 : percentWalls;
		this.map = new int[this.mapWidth][this.mapHeight];
		this.map = map;
	}

	public void makeCaverns() {
		// By initilizing column in the outter loop, its only created ONCE
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				map[column][row] = placeWallLogic(column, row);
			}
		}
	}

	public int placeWallLogic(int x, int y) {
		int numWalls = getAdjacentWalls(x, y, 1, 1);

		if (map[x][y] == 1) {
			if (numWalls >= 4) {
				return 1;
			}
			if (numWalls < 2) {
				return 0;
			}
		} else {
			if (numWalls >= 5) {
				return 1;
			}
		}
		return 0;
	}

	public int getAdjacentWalls(int x, int y, int scopeX, int scopeY) {
		int startX = x - scopeX;
		int startY = y - scopeY;
		int endX = x + scopeX;
		int endY = y + scopeY;

		int iX = startX;
		int iY = startY;

		int wallCounter = 0;

		for (iY = startY; iY <= endY; iY++) {
			for (iX = startX; iX <= endX; iX++) {
				if (!(iX == x && iY == y)) {
					if (isWall(iX, iY)) {
						wallCounter += 1;
					}
				}
			}
		}
		return wallCounter;
	}

	private boolean isWall(int x, int y) {
		// Consider out-of-bound a wall
		if (isOutOfBounds(x, y)) {
			return true;
		}

		if (map[x][y] == 1) {
			return true;
		}

		if (map[x][y] == 0) {
			return false;
		}
		return false;
	}

	private boolean isOutOfBounds(int x, int y) {
		if (x < 0 || y < 0) {
			return true;
		} else if (x > mapWidth - 1 || y > mapHeight - 1) {
			return true;
		}
		return false;
	}

	public void printMap() {
		System.out.println(mapToString());
	}

	public void printMap(int[][] map) {
		System.out.println(mapToString(map));
	}

	private String mapToString() {
		String returnString = "Width: " + mapWidth + "\tHeight: " + mapHeight
				+ "\t" + percentAreWalls + "% Walls \n";

		ArrayList<String> mapSymbols = new ArrayList<String>();
		mapSymbols.add(".");
		mapSymbols.add("#");
		mapSymbols.add("+");

		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				returnString += map[column][row];// mapSymbols.get(map[column][row]);
			}
			returnString += "\n";
		}
		return returnString;
	}

	private String mapToString(int[][] map) {
		String returnString = "Width: " + mapWidth + "\tHeight: " + mapHeight
				+ "\t" + percentAreWalls + "% Walls \n";

		ArrayList<String> mapSymbols = new ArrayList<String>();
		mapSymbols.add(".");
		mapSymbols.add("#");
		mapSymbols.add("+");

		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				returnString += map[column][row];// mapSymbols.get(map[column][row]);
			}
			returnString += "\n";
		}
		return returnString;
	}

	public void BlankMap() {
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				map[column][row] = 0;
			}
		}
	}

	public void randomFillMap() {
		// New, empty map
		map = new int[mapWidth][mapHeight];

		int mapMiddle = 0; // Temp variable
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				// If coordinants lie on the the edge of the map (creates a
				// border)
				if (column == 0) {
					map[column][row] = 1;
				} else if (row == 0) {
					map[column][row] = 1;
				} else if (column == mapWidth - 1) {
					map[column][row] = 1;
				} else if (row == mapHeight - 1) {
					map[column][row] = 1;
				}
				// Else, fill with a wall a random percent of the time
				else {
					mapMiddle = (mapHeight / 2);

					if (row == mapMiddle) {
						map[column][row] = 0;
					} else {
						map[column][row] = RandomPercent(percentAreWalls);
					}
				}
			}
		}
	}

	int RandomPercent(int percent) {
		if (percent >= (rand.nextInt(100) + 1)) {
			return 1;
		}
		return 0;
	}

	public static int[][] cloneArray(int[][] src) {
		int length = src.length;
		int[][] target = new int[length][src[0].length];
		for (int i = 0; i < length; i++) {
			System.arraycopy(src[i], 0, target[i], 0, src[i].length);
		}
		return target;
	}

	public int[][] regionLabeling(int[][] newMap) {
		int[][] map = cloneArray(newMap);
		int m = 2;
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				if (map[column][row] == 1) {
					floodFill(map, column, row, m);
					m++;
				}
			}
		}
		return map;
	}

	class Node {
		int x, y;

		Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private void floodFill(int[][] map, int column, int row, int label) {
		LinkedList<Node> q = new LinkedList<Node>(); // queue
		q.addFirst(new Node(column, row));
		while (!q.isEmpty()) {
			Node n = q.removeLast();
			if ((n.x >= 0) && (n.x < mapWidth) && (n.y >= 0)
					&& (n.y < mapHeight) && map[n.x][n.y] == 1) {
				map[n.x][n.y] = label;
				q.addFirst(new Node(n.x + 1, n.y));
				q.addFirst(new Node(n.x, n.y + 1));
				q.addFirst(new Node(n.x, n.y - 1));
				q.addFirst(new Node(n.x - 1, n.y));
			}
		}
	}

	// probably too expensive
	public void sequentialLabeling(/* int[][] map */) {
		// value of the next label to be assigned
		int m = 2;
		Map<Integer, Integer> collisions = new HashMap<Integer, Integer>();
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				if (map[column][row] == 1) {
					int[] labels = getNeighborLabels(column, row);
					if (allBackgroundPixels(labels)) {
						map[column][row] = m;
						m++;
					} else if (exactlyOneLargeNeighbor(labels)) {
						int nk = -100; // TODO: better value, this is for
										// debugging (or no value)
						for (int l : labels) {
							if (l > 1)
								nk = l;
						}
						map[column][row] = nk;
					} else if (severalLargeNeighbors(labels)) {
						int nk = -100; // TODO: better value, this is for
										// debugging (or no value)
						for (int l : labels) {
							if (l > 1)
								nk = l;
						}
						map[column][row] = nk;
						for (int otherLabel : labels) {
							if (otherLabel > 1 && otherLabel != nk) {
								collisions.put(otherLabel, nk);
							}
						}
					}
				}
			}
		}
		// resolve label collisions

	}

	private boolean severalLargeNeighbors(int[] labels) {
		int neighborCount = 0;
		for (int i : labels) {
			if (i > 1)
				neighborCount++;
		}
		if (neighborCount > 1) {
			return true;
		} else {
			return false;
		}
	}

	private boolean exactlyOneLargeNeighbor(int[] labels) {
		int neighborCount = 0;
		for (int i : labels) {
			if (i > 1)
				neighborCount++;
		}
		if (neighborCount == 1)
			return true;
		else
			return false;
	}

	private boolean allBackgroundPixels(int[] labels) {
		int pixelLabel = 0;
		for (int label : labels) {
			if (label > pixelLabel)
				return false;
		}
		return true;
	}

	public int[] getNeighborLabels(int x, int y) {
		int[] result = new int[4];
		Arrays.fill(result, -1);
		result[0] = getLabel(x - 1, y - 1);
		result[1] = getLabel(x, y - 1);
		result[2] = getLabel(x + 1, y - 1);
		result[3] = getLabel(x - 1, y);
		return result;
	}

	private int getLabel(int x, int y) {
		if (isOutOfBounds(x, y))
			return 0;
		return map[x][y];
	}

}
