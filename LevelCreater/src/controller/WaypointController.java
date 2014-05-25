package controller;

import helper.Randomizer;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import model.astar.Node;
import model.astar.Path;
import model.level.objects.LOWaypoint;
import model.level.objects.LevelObject;

public class WaypointController {

	private int[][] map;
	private List<LevelObject> levelObjects;
	private AStar aStar;
	private int wayPointSize;
	private List<Path> paths;

	private static int DIMENSION_FRACTION = 130;

	public WaypointController(int[][] map, List<LevelObject> levelObjects) {
		this.map = map;
		this.levelObjects = levelObjects;
		this.aStar = new AStar();
		paths = new ArrayList<Path>();

		// set waypoint size according to width and height of map
		int minDimension = map.length < map[0].length ? map.length : map[0].length;
		wayPointSize = 1 + (minDimension / DIMENSION_FRACTION);
	}

	public List<LevelObject> createWaypointsWithSections(int numOfWaypoints) {
		if (this.map.equals(null))
			return null;
		if (this.levelObjects.size() == 0)
			return null;

		List<LevelObject> waypoints = new ArrayList<LevelObject>();
		int mapWidth = this.map.length;
		int mapHeight = this.map[0].length;

		Rectangle[] sections = calculateSections(mapWidth, mapHeight, numOfWaypoints);
		List<Rectangle> sectionList = new ArrayList<>(sections.length);
		// convert to list to be able to easily pick random section
		Collections.addAll(sectionList, sections);
		Rectangle currentSection;
		int xPos, yPos, numOfFieldsInSection, numOfTrials;
		boolean emptyField = false;
		boolean isReachable = false;
		boolean tooManyTrials = false;
		for (int i = 0; i < numOfWaypoints; i++) {
			// pick random section

			currentSection = getNextSection(sectionList);
			numOfFieldsInSection = (currentSection.height - currentSection.y) * (currentSection.width - currentSection.x);
			numOfTrials = 0;

			LOWaypoint newWp = null;
			do {
				emptyField = false;
				isReachable = false;
				tooManyTrials = false;
				// System.out.println("before:" + i);
				xPos = Randomizer.randomIntFromInterval(currentSection.x, currentSection.width - 1);
				yPos = Randomizer.randomIntFromInterval(currentSection.y, currentSection.height - 1);
				emptyField = map[xPos][yPos] == 0;
				numOfTrials++;
				if (emptyField) {
					newWp = new LOWaypoint(xPos, yPos, wayPointSize, wayPointSize);
					if (waypoints.size() < 1) {
						isReachable = true;

					} else {
						LOWaypoint otherWp = (LOWaypoint) getClosestWaypoint(waypoints, newWp);
						isReachable = areInterconnected(otherWp, newWp);
					}
				}
				tooManyTrials = numOfTrials >= numOfFieldsInSection;
			} while ((!emptyField || !isReachable) && !tooManyTrials);
			map[xPos][yPos] = -1;
			waypoints.add(newWp);
			sectionList.remove(currentSection);
		}

		return waypoints;
	}

	// totally random waypoint creation(@Deprecated)
	public List<LevelObject> createWaypoints(int numOfWaypoints) {
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
				LOWaypoint newWp = new LOWaypoint(xPos, yPos, wayPointSize, wayPointSize);
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

	// Returns sections where width and height are end coordinates (instead of
	// Coordinates like: width - x, and height - y)
	// split level into sectors depending on total number of waypoints as
	// follows (1 - 6 beeing waypoint sections)
	// -----
	// |1|2|
	// -----
	// |3|4|
	// -----
	// |5|6|
	// -----
	public Rectangle[] calculateSections(int levelWidth, int levelHeight, int numWayPoints) {
		int levelSize = levelWidth * levelHeight;
		int sectionSize = levelSize / numWayPoints;
		double sectionEdgeSize = Math.ceil(Math.sqrt(sectionSize));
		int numOfWidthSections = (int) Math.ceil(levelWidth / sectionEdgeSize);
		int numOfHeightSections = (int) Math.ceil(levelHeight / sectionEdgeSize);
		int sectionWidth = levelWidth / numOfWidthSections;
		int sectionHeight = levelHeight / numOfHeightSections;

		Rectangle[] sections = new Rectangle[numOfWidthSections * numOfHeightSections];

		int x, y, width, height;
		for (int j = 0; j < numOfHeightSections; j++) {

			y = (j % numOfHeightSections) * sectionHeight;
			height = y + sectionHeight;

			for (int i = 0; i < numOfWidthSections; i++) {

				x = (i % numOfWidthSections) * sectionWidth;
				width = x + sectionWidth;
				Rectangle currentSection = new Rectangle(x, y, width, height);
				sections[(j * numOfWidthSections) + i] = currentSection;
			}
		}

		return sections;
	}

	private Rectangle getNextSection(List<Rectangle> sectionList) {
		int sectionListSize = sectionList.size();
		if (sectionListSize > 0) {
			return sectionList.get(Randomizer.random.nextInt(sectionListSize));
		} else {
			return new Rectangle(0, 0, map.length - 1, map[0].length);
		}
	}

	private LevelObject getClosestWaypoint(List<LevelObject> waypoints, LOWaypoint waypoint) {
		int closestDistance = Integer.MAX_VALUE;
		LevelObject ret = null;
		int currentDistance;
		Iterator<LevelObject> iter = waypoints.iterator();
		while (iter.hasNext()) {
			LevelObject wp = iter.next();
			if (wp != null && !waypoint.equals(wp)) {
				currentDistance = getDistance(wp, waypoint);
				if (currentDistance < closestDistance) {
					ret = wp;
					closestDistance = currentDistance;
				}
			}
		}
		return ret;
	}

	private int getDistance(LevelObject a, LevelObject b) {
		int distX = a.getPosition().x - b.getPosition().x;
		int distY = b.getPosition().y - b.getPosition().y;
		return (distX * distX + distY * distY);
	}

	// A* Pathfinding to check if the waypoints are interconnected
	private boolean areInterconnected(LOWaypoint startPoint, LOWaypoint endPoint) {
		List<Node> path = aStar.calculatePath(map, startPoint.getPosition(), endPoint.getPosition());
		if (path != null && path.size() > 0) {
			addPathToList(path);
			return true;
		} else
			return false;
	}

	private void addPathToList(List<Node> nodeList) {
		paths.add(new Path(nodeList));
	}

	public List<Path> getPaths() {
		return paths;
	}

	public int getWayPointSize() {
		return wayPointSize;
	}

}
