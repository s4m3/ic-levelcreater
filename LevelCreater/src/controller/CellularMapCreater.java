package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import model.MapPoint;

public class CellularMapCreater {

	Random rand = new Random();

	public int[][] map;

	public int mapWidth;
	public int mapHeight;
	public int percentAreWalls;

	// private enum neighbors {
	// ALL_BACKGROUND, ONE, MANY
	// }

	public CellularMapCreater() {
		mapWidth = 40;
		mapHeight = 21;
		percentAreWalls = 40;

		randomFillMap();
	}

	public CellularMapCreater(int width, int height, int percentAreWalls) {
		this.mapWidth = width;
		this.mapHeight = height;
		this.percentAreWalls = percentAreWalls;

		randomFillMap();
	}

	public CellularMapCreater(int mapWidth, int mapHeight, int[][] map,
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
		int mapHeight = map.length;
		int mapWidth = map[0].length;
		int m = 2;
		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				if (map[column][row] == 1) {
					floodFill(map, column, row, m, mapWidth, mapHeight);
					m++;
				}
			}
		}
		return map;
	}



	private void floodFill(int[][] map, int column, int row, int label,
			int mapWidth, int mapHeight) {
		LinkedList<MapPoint> q = new LinkedList<MapPoint>(); // queue
		q.addFirst(new MapPoint(column, row));
		while (!q.isEmpty()) {
			MapPoint n = q.removeLast();
			if ((n.x >= 0) && (n.x < mapWidth) && (n.y >= 0)
					&& (n.y < mapHeight) && map[n.x][n.y] == 1) {
				map[n.x][n.y] = label;
				q.addFirst(new MapPoint(n.x + 1, n.y));
				q.addFirst(new MapPoint(n.x, n.y + 1));
				q.addFirst(new MapPoint(n.x, n.y - 1));
				q.addFirst(new MapPoint(n.x - 1, n.y));
			}
		}
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

	public int[][] convertRegionsToContour(int[][] input, int initialLabel) {
		int map[][] = cloneArray(input);
		int mapHeight = map.length;
		int mapWidth = map[0].length;
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

		return map;
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
}
