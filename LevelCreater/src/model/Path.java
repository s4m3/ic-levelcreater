package model;

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

}
