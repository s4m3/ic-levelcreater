package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import model.level.MapPoint;

public class CellularAutomaton {

	Random rand = new Random();

	public int[][] map;

	public int mapWidth;
	public int mapHeight;
	public int percentAreWalls;

	private Map<Integer, Integer> regionSizeByLabel;

	public CellularAutomaton(int width, int height) {
		this.mapWidth = width;
		this.mapHeight = height;
	}

	public CellularAutomaton(int width, int height, int percentAreWalls) {
		this(width, height);
		this.percentAreWalls = percentAreWalls;
		regionSizeByLabel = new HashMap<Integer, Integer>();
		randomFillMap();
	}

	public void makeCaverns() {
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
		if (isWallOutOfBounds(x, y)) {
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

	private boolean isWallOutOfBounds(int x, int y) {
		if (x < 3 || y < 3) {
			return true;
		} else if (x > mapWidth - 4 || y > mapHeight - 4) {
			return true;
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

	private int RandomPercent(int percent) {
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

	public int[][] getMapWithLabeledRegions() {
		int[][] map = cloneArray(this.map);
		int m = 2;
		try {
			for (int column = 0, row = 0; row < mapHeight; row++) {
				for (column = 0; column < mapWidth; column++) {
					if (map[column][row] == 1) {
						floodFill(map, column, row, m, mapWidth, mapHeight);
						m++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	private void floodFill(int[][] map, int column, int row, int label,
			int mapWidth, int mapHeight) {
		LinkedList<MapPoint> q = new LinkedList<MapPoint>(); // queue
		q.addFirst(new MapPoint(column, row));
		int size = 0;
		while (!q.isEmpty()) {
			MapPoint n = q.removeLast();
			if ((n.x >= 0) && (n.x < mapWidth) && (n.y >= 0)
					&& (n.y < mapHeight) && map[n.x][n.y] == 1) {
				map[n.x][n.y] = label;
				size++;
				q.addFirst(new MapPoint(n.x + 1, n.y));
				q.addFirst(new MapPoint(n.x, n.y + 1));
				q.addFirst(new MapPoint(n.x, n.y - 1));
				q.addFirst(new MapPoint(n.x - 1, n.y));
			}
		}
		if (size > 0)
			regionSizeByLabel.put(label, size);
	}

	public ArrayList<MapPoint> makeEntrance(int[][] input) {
		ArrayList<MapPoint> pointList = new ArrayList<MapPoint>();
		int middleY = input[0].length / 2;
		int width = input.length;
		int i = 0;
		boolean done = false;
		while (!done) {
			if (input[i][middleY] != 0 && i < width) {
				input[i][middleY] = 0;
				MapPoint p = new MapPoint(i, middleY);
				pointList.add(p);
				i++;
			} else {
				done = true;
			}
		}
		return pointList;
	}

	// //////////////////////////////////////////////////
	// MAP EVALUATION
	// //////////////////////////////////////////////////

	public boolean isValidMap(int[][] map) {
		boolean isEntrancePossible = isEntrancePossible(map);
		boolean mapHasMinFreeSpace = mapHasMinFreeSpace(map, 0.2);

		return (isEntrancePossible && mapHasMinFreeSpace);
	}

	private boolean isEntrancePossible(int[][] map) {
		int middleY = map[0].length / 2;
		for (int i = 0; i < map.length; i++) {
			if (map[i][middleY] == 0)
				return true;
		}
		return false;
	}

	private boolean mapHasMinFreeSpace(int[][] map, double percentage) {
		int height = map[0].length;
		int width = map.length;
		double totalSpace = width * height;
		double emptySpace = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (map[i][j] == 0)
					emptySpace++;
			}
		}
		return emptySpace / totalSpace > percentage;
	}

	public void convertRegionsToContour(int[][] map, int initialLabel) {
		int mapHeight = map[0].length;
		int mapWidth = map.length;
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				// if not a background pixel and has top bottom left and right
				// neighbor, it is a inner pixel
				if (map[column][row] != 0) {
					if (isInnerPixel(column, row, map))
						map[column][row] = -1;
				}
			}
		}

		for (int column2 = 0, row2 = 0; row2 < mapHeight; row2++) {
			for (column2 = 0; column2 < mapWidth; column2++) {
				if (map[column2][row2] == -1) {
					map[column2][row2] = 0;
				}
			}
		}
	}

	private boolean isInnerPixel(int column, int row, int[][] map) {
		boolean innerPixel = true;
		// Out of bounds? then no inner pixel
		if (isOutOfBounds(column, row - 1) || isOutOfBounds(column, row + 1)
				|| isOutOfBounds(column - 1, row)
				|| isOutOfBounds(column + 1, row))
			return false;

		// TOP
		if (map[column][row - 1] == 0)
			innerPixel = false;
		// BOTTOM
		if (map[column][row + 1] == 0)
			innerPixel = false;
		// LEFT
		if (map[column - 1][row] == 0)
			innerPixel = false;
		// RIGHT
		if (map[column + 1][row] == 0)
			innerPixel = false;
		return innerPixel;
	}

	// //////////////////////////////////////////////////
	// SETTERS AND GETTERS
	// //////////////////////////////////////////////////

	public Map<Integer, Integer> getRegionSizeByLabel() {
		return regionSizeByLabel;
	}

	// //////////////////////////////////////////////////
	// LOGGING AND DEBUGGING
	// //////////////////////////////////////////////////

	public void printMap(int[][] map) {
		System.out.println(mapToString(map));
	}

	public void printMapWithSymbols(int[][] map) {
		System.out.println(mapToStringWithSymbols(map));
	}

	private String mapToStringWithSymbols(int[][] map) {
		String returnString = "";
		ArrayList<String> mapSymbols = new ArrayList<String>();
		mapSymbols.add(".");
		mapSymbols.add("#");
		mapSymbols.add("W");

		int mapVal;
		int val;
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				mapVal = map[column][row];
				val = mapVal == 0 ? 0 : mapVal > 0 ? 1 : 2;
				returnString += mapSymbols.get(val);
			}
			returnString += "\n";
		}
		return returnString;
	}

	private String mapToString(int[][] map) {
		String returnString = "Width: " + mapWidth + "\tHeight: " + mapHeight
				+ "\t" + percentAreWalls + "% Walls \n";

		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				returnString += map[column][row];// mapSymbols.get(map[column][row]);
			}
			returnString += "\n";
		}
		return returnString;
	}

}
