package controller;

import helper.Randomizer;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import model.LOWall;
import model.LOWaypoint;
import model.LevelObject;
import model.Node;
import model.Path;

public class WaypointController {

	private int[][] map;
	private List<LevelObject> levelObjects;
	private AStar aStar;
	private static int WAYPOINT_SIZE = 5;
	private List<Path> paths;

	public WaypointController(int[][] map, List<LevelObject> levelObjects) {
		this.map = map;
		this.levelObjects = levelObjects;
		this.aStar = new AStar();
		paths = new ArrayList<Path>();
	}

	public List<LevelObject> createWaypoints(int numOfWayPoints) {
		if (this.map.equals(null))
			return null;
		if (this.levelObjects.size() == 0)
			return null;

		List<LevelObject> waypoints = new ArrayList<LevelObject>();
		int mapWidth = this.map.length;
		int mapHeight = this.map[0].length;

		while (waypoints.size() < numOfWayPoints) {
			LOWaypoint currentWp = new LOWaypoint(true, numOfWayPoints,
					waypoints.size(), 1, 1, mapWidth, mapHeight);
			// LOWaypoint currentWp = new LOWaypoint(true, 1, 1, mapWidth,
			// mapHeight);
			if (!wpIntersectsWithWalls(currentWp)) {
				if (waypoints.size() >= 1) {
					if (areInterconnected((LOWaypoint) waypoints.get(0),
							currentWp)) {
						waypoints.add(currentWp);
					}
				} else {
					waypoints.add(currentWp);
				}
			}
		}
		return waypoints;
	}

	public List<LevelObject> createWaypointsALT(int numOfWaypoints) {
		if (this.map.equals(null))
			return null;
		if (this.levelObjects.size() == 0)
			return null;

		List<LevelObject> waypoints = new ArrayList<LevelObject>();
		int mapWidth = this.map.length;
		int mapHeight = this.map[0].length;

		int xPos, yPos;

		while (waypoints.size() < numOfWaypoints) {
			xPos = Randomizer.random.nextInt(mapWidth);
			yPos = Randomizer.random.nextInt(mapHeight);
			if (map[xPos][yPos] == 0) {
				LOWaypoint newWp = new LOWaypoint(xPos, yPos, WAYPOINT_SIZE,
						WAYPOINT_SIZE);
				if (waypoints.size() >= 1) {
					if (areInterconnected((LOWaypoint) waypoints.get(0), newWp)) {
						waypoints.add(newWp);
						map[xPos][yPos] = -1;
					}
				} else {
					waypoints.add(newWp);
					map[xPos][yPos] = -1;
				}
			}
		}
		return waypoints;
	}

	public List<LevelObject> createWaypointsWithSections(int numOfWaypoints) {
		if (this.map.equals(null))
			return null;
		if (this.levelObjects.size() == 0)
			return null;

		List<LevelObject> waypoints = new ArrayList<LevelObject>();
		int mapWidth = this.map.length;
		int mapHeight = this.map[0].length;

		Rectangle[] sections = calculateSections(mapWidth, mapHeight,
				numOfWaypoints);
		Rectangle currentSection;
		int xPos, yPos;
		for (int i = 0; i < numOfWaypoints; i++) {
			currentSection = sections[i];

			do {
				xPos = Randomizer.random
						.nextInt((currentSection.width - currentSection.x) + 1)
						+ currentSection.x;
				yPos = Randomizer.random
						.nextInt((currentSection.height - currentSection.y) + 1)
						+ currentSection.y;
			} while (map[xPos][yPos] != 0);

			LOWaypoint newWp = new LOWaypoint(xPos, yPos, WAYPOINT_SIZE,
					WAYPOINT_SIZE);

			// TODO: are interconnected?

			map[xPos][yPos] = -1;
			waypoints.add(newWp);

		}

		return waypoints;
	}

	// Returns sections where width and height are end coordinates (instead of
	// Coordinates like: width - x, and height - y)
	public Rectangle[] calculateSections(int levelWidth, int levelHeight,
			int numWayPoints) {
		int levelSize = levelWidth * levelHeight;
		int sectionSize = levelSize / numWayPoints;
		double sectionEdgeSize = Math.floor(Math.sqrt(sectionSize));
		int numOfWidthSections = (int) Math.floor(levelWidth / sectionEdgeSize);
		int numOfHeightSections = (int) Math
				.ceil(levelHeight / sectionEdgeSize);
		//
		// System.out.println(numOfWidthSections);
		// System.out.println(numOfHeightSections);
		int sectionWidth = levelWidth / numOfWidthSections;
		int sectionHeight = levelHeight / numOfHeightSections;

		Rectangle[] sections = new Rectangle[numOfWidthSections
				* numOfHeightSections];

		int x, y, width, height;
		for (int j = 0; j < numOfHeightSections; j++) {

			y = (j % numOfHeightSections) * sectionHeight;
			height = y + sectionHeight;

			for (int i = 0; i < numOfWidthSections; i++) {

				x = (i % numOfWidthSections) * sectionWidth;
				width = x + sectionWidth;
				// System.out.println(String.format(
				// "for wp:%d, x=%d, y=%d, width=%d, height=%d", i, x, y,
				// width, height));
				Rectangle currentSection = new Rectangle(x, y, width, height);
				System.out.println((j * numOfWidthSections) + i);
				sections[(j * numOfWidthSections) + i] = currentSection;
			}
		}

		return sections;
	}

	private boolean wpIntersectsWithWalls(LOWaypoint wayPoint) {
		Point p = wayPoint.getPosition();
		for (LevelObject lo : levelObjects) {
			if (lo instanceof LOWall) {
				if (((LOWall) lo).getPolygon().contains(p)) {
					return true;
				}
			}
		}
		return false;
	}

	// A* Pathfinding to check if the waypoints are interconnected
	private boolean areInterconnected(LOWaypoint startPoint, LOWaypoint endPoint) {
		// TODO: delete
		return true;
		// List<Node> path = aStar.calculatePath(map, startPoint.getPosition(),
		// endPoint.getPosition());
		// if (path != null && path.size() > 0) {
		// addPathToList(path);
		// return true;
		// } else
		// return false;
	}

	private void addPathToList(List<Node> nodeList) {
		paths.add(new Path(nodeList));
	}

	public List<Path> getPaths() {
		return paths;
	}

}
