package model;

import helper.Randomizer;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class LOWaypoint extends LOCircle {

	public LOWaypoint() {
		this.objectColor = Color.YELLOW;
	}

	public LOWaypoint(boolean randomPosition, double xSize, double ySize,
			int levelWidth, int levelHeight) {
		this.objectColor = Color.YELLOW;
		this.position = new Point(Math.round(Randomizer.random.nextFloat()
				* levelWidth), Math.round(Randomizer.random.nextFloat()
				* levelHeight));
		this.ellipse = new Ellipse2D.Double(this.position.x, this.position.y,
				xSize, ySize);
	}
}
