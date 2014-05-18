package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Path {

	private List<MapPoint> nodes;

	public Path(List<Node> nodeList) {
		nodes = new ArrayList<MapPoint>();
		for (Node node : nodeList) {
			nodes.add(new MapPoint(node.getX(), node.getY()));
		}
	}

	public List<MapPoint> getNodes() {
		return nodes;
	}

	public ArrayList<MapPoint> getNodesAsArrayList() {
		ArrayList<MapPoint> nodesAsAL = new ArrayList<>(nodes.size());
		for (MapPoint mapPoint : nodes) {
			nodesAsAL.add(mapPoint);
		}
		return nodesAsAL;
	}

	public ArrayList<Point> getPathAsArrayListOfPoints() {
		ArrayList<Point> pointList = new ArrayList<Point>(nodes.size());
		for (MapPoint mapPoint : nodes) {
			Point p = new Point(mapPoint.x, mapPoint.y);
			pointList.add(p);
		}
		return pointList;
	}

}
