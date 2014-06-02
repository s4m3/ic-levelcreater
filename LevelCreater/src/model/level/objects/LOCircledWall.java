package model.level.objects;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class LOCircledWall extends LOCircle {

	public LOCircledWall() {
		this.objectColor = Color.DARK_GRAY;
	}

	public LOCircledWall(int xPos, int yPos, int xSize, int ySize) {
		this();
		this.position = new Point(xPos, yPos);
		this.ellipse = new Ellipse2D.Double(xPos - xSize / 2, yPos - ySize / 2, xSize, ySize);
	}

	public LOCircledWall(Ellipse2D.Double ellipse) {
		this();
		this.ellipse = new Ellipse2D.Double(ellipse.x - ellipse.width / 2, ellipse.y - ellipse.height / 2, ellipse.width,
				ellipse.height);
		this.position = new Point((int) ellipse.x, (int) ellipse.y);
	}
}
