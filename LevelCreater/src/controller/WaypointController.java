package controller;

import helper.Randomizer;

import java.awt.Point;
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
		List<Node> path = aStar.calculatePath(map, startPoint.getPosition(),
				endPoint.getPosition());
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

}
