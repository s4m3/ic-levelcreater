package model;

import java.util.ArrayList;
import java.util.Random;

public class MapHandlerTest {

	Random rand = new Random();

	public int[][] map;

	public int mapWidth;
	public int mapHeight;
	public int percentAreWalls;

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
		;
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
				returnString += mapSymbols.get(map[column][row]);
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

}
