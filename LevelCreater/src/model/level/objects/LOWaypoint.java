package model.level.objects;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class LOWaypoint extends LOCircle {

	private LOWaypoint connectedWaypoint;

	public LOWaypoint() {
		this.objectColor = Color.YELLOW;
	}

	public LOWaypoint(int xPos, int yPos, int xSize, int ySize) {
		this.objectColor = Color.YELLOW;
		this.position = new Point(xPos, yPos);
		this.ellipse = new Ellipse2D.Double(xPos - xSize / 2, yPos - ySize / 2, xSize, ySize);
	}

	public LOWaypoint getConnectedWaypoint() {
		return connectedWaypoint;
	}

	public void setConnectedWaypoint(LOWaypoint connectedWaypoint) {
		this.connectedWaypoint = connectedWaypoint;
	}

}
