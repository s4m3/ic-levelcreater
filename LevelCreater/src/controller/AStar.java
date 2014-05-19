package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.astar.Node;

/**
 * Adaptiert von
 * http://www.java-forum.org/codeschnipsel-u-projekte/5046-codeschnipsel.html
 */
public class AStar {

	public ArrayList<Node> calculatePath(int[][] map, Point start, Point goal) {
		// printMapWithSymbols(map, start, goal, null);
		int mapWidth = map.length;
		int mapHeight = map[0].length;
		int startX, startY, endX, endY, iX, iY; // for iteration through
												// successors
		int cX;
		int cY;
		List<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		Node startNode = new Node(null, 0, 0, start.x, start.y);

		openList.add(startNode);
		while (!openList.isEmpty()) {
			Node currentNode = getLeastF(openList);
			openList.remove(currentNode);

			ArrayList<Node> successors = new ArrayList<Node>();

			cX = currentNode.getX();
			cY = currentNode.getY();

			startX = cX - 1;
			startY = cY - 1;
			endX = cX + 1;
			endY = cY + 1;

			for (iX = startX; iX <= endX; iX++) {
				for (iY = startY; iY <= endY; iY++) {
					if (!isOutOfBounds(iX, iY, mapWidth, mapHeight)
							&& !(iX == cX && iY == cY)) { // check: out of
															// bounds and ignore
															// same node
						if (map[iX][iY] == 0) {

							Node n = new Node(currentNode, iX, iY);
							// printMapWithSymbols(map, start, goal,
							// closedList);
							n.setG(calcG(n));
							n.setH(calcH(n, goal));
							n.setF(n.getG() + n.getH());
							// System.out.println(String.format("%f %f %f",
							// n.getG(), n.getH(), n.getF()));
							successors.add(n);
						}
					}
				}
			}

			for (Node n : successors) { // für jede Gehmöglichkeit
				if (n.getX() == (int) goal.getX()
						&& n.getY() == (int) goal.getY()) {
					ArrayList<Node> path = generatePath(n);
					return path;
				}
				boolean add = true; // wenn es keinen besseren Knoten in der
				if (besserIn(n, openList)) // openList und der
					add = false;
				if (besserIn(n, closedList)) // closedList
					add = false;
				if (add)
					openList.add(n); // gibt, successor zur openList hinzufügen
			}
			closedList.add(currentNode); // Schleife beendet, q zur closedList
											// tun
		}
		// Schleife beendet->kein Weg gefunden
		return null;
	}

	private ArrayList<Node> generatePath(Node n) {
		ArrayList<Node> path = new ArrayList<Node>();
		Node node = n;
		while (node.getParent() != null) {
			node = node.getParent();
			path.add(node);
		}
		return path;
	}

	private Node getLeastF(List<Node> l) // Node aus open-/closedList mit
											// niedrigstem f suchen
	{
		Node least = null;
		for (Node n : l) {
			if ((least == null) || (n.getF() < least.getF())) {
				least = n;
			}
		}
		return least;
	}

	private boolean besserIn(Node n, List<Node> l) // Umweg gegangen?
	{
		for (Node no : l) {
			if (no.getX() == n.getX() && no.getY() == n.getY()
					&& no.getF() <= n.getF())
				return true;
		}
		return false;
	}

	private float calcG(Node node) {
		Node parent = node.getParent();
		float newG = parent.getG() + 1;
		// if x and y are different to the parent node, it is a diagonal node
		// ~(+ 0.4)
		if (node.getX() != parent.getX() && node.getY() != parent.getY())
			newG += 0.4;

		return newG;
	}

	private float calcH(Node act, Point goal) // Heuristik
	{
		int distX = Math.abs(act.getX() - goal.x); // Differenz a
		int distY = Math.abs(act.getY() - goal.y); // Differenz b
		float ret = (float) Math.sqrt(distX * distX + distY * distY);
		return ret;
	}

	private boolean isOutOfBounds(int x, int y, int mapWidth, int mapHeight) {
		if (x < 0 || y < 0) {
			return true;
		} else if (x > mapWidth - 1 || y > mapHeight - 1) {
			return true;
		}
		return false;
	}

	public void printMapWithSymbols(int[][] map, Point start, Point goal,
			List<Node> openList) {
		System.out.println(mapToStringWithSymbols(map, start, goal, openList));
	}

	private String mapToStringWithSymbols(int[][] map, Point start, Point goal,
			List<Node> openList) {
		String returnString = "";
		ArrayList<String> mapSymbols = new ArrayList<String>();
		mapSymbols.add(".");
		mapSymbols.add("#");
		mapSymbols.add("W");
		mapSymbols.add("S");
		mapSymbols.add("G");
		mapSymbols.add("N");

		int startX = start.x;
		int startY = start.y;
		int goalX = goal.x;
		int goalY = goal.y;
		int mapVal;
		int val;
		int mapHeight = map[0].length;
		int mapWidth = map.length;

		for (int column = 0, row = 0; row < mapHeight; row++) {
			for (column = 0; column < mapWidth; column++) {
				mapVal = map[column][row];
				val = mapVal == 0 ? 0 : mapVal > 0 ? 1 : 2;
				if (column == startX && row == startY)
					val = 3;
				else if (column == goalX && row == goalY)
					val = 4;
				else if (listContainsNode(openList, column, row))
					val = 5;

				returnString += mapSymbols.get(val);
			}
			returnString += "\n";
		}
		return returnString;
	}

	private boolean listContainsNode(List<Node> openList, int x, int y) {
		if (openList == null)
			return false;

		for (Node node : openList) {
			if (node.getX() == x && node.getY() == y)
				return true;
		}
		return false;
	}
}
