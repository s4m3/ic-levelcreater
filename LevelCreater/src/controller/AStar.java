package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.Node;

/**
 * Adaptiert von http://www.java-forum.org/codeschnipsel-u-projekte/5046-codeschnipsel.html
 */
public class AStar {

	public boolean pathExists(int[][] map, Point start, Point goal) {
		int mapWidth = map.length;
		int mapHeight = map[0].length;
		int startX, startY, endX, endY, iX, iY; //for iteration through successors
		int cX;
		int cY;
		List<Node> openList = new ArrayList<Node>(); 
		List<Node> closedList = new ArrayList<Node>(); 
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
			
			for(iX = startX; iX < endX; iX++) {
				for(iY = startY; iY < endY; iY++) {
					if(!isOutOfBounds(iX, iY, mapWidth, mapHeight) && !(iX == cX && iY == cY)) { //check: out of bounds and ignore same node
						if(map[iX][iY] == 0) {
							Node n = new Node(currentNode, iX, iY);
							n.setG(calcG(n));
							n.setH(calcH(n, goal));
							n.setF(n.getG() + n.getH());
							successors.add(n);
						}
					}
				}
			}
			
			for (Node n : successors) { // für jede Gehmöglichkeit
				if (n.getX() == (int) goal.getX() && n.getY() == (int) goal.getY()) // wenn Ziel
					return true; // FERTIG!!!:)
				boolean add = true; // wenn es keinen besseren Knoten in der
				if (besserIn(n, openList)) // openList und der
					add = false;
				if (besserIn(n, closedList)) // closedList
					add = false;
				if (add)
					openList.add(n); // gibt, successor zur openList hinzufügen
			}
			closedList.add(currentNode); // Schleife beendet, q zur closedList tun
		}
		// Schleife beendet->kein Weg gefunden
		return false;
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
		//if x and y are different to the parent node, it is a diagonal node ~(+ 0.4)
		if(node.getX() != parent.getX() && node.getY() != parent.getY())
			newG += 0.4;
		
		return newG;
	}

	private float calcH(Node act, Point goal) // Heuristik
	{
		int distX = Math.abs(act.getX() - goal.x); // Differenz a
		int distY = Math.abs(act.getY() - goal.y); // Differenz b
		float ret = (float) Math.sqrt(distX * distX + distY * distY); // Luftlinie,
																		// frei
																		// nach
																		// Pythagoras
																		// (c =
																		// Wurzel(a²+b²))
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
}
