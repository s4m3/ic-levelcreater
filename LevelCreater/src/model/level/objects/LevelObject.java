package model.level.objects;

import java.awt.Color;
import java.awt.Point;

public class LevelObject {

	protected Point position;
	protected Color objectColor;

	public LevelObject() {
		this(new Point(0, 0));
	}

	public LevelObject(Point position) {
		this(position, Color.BLACK);
	}

	public LevelObject(Point position, Color color) {
		this.position = position;
		this.objectColor = color;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Color getObjectColor() {
		return objectColor;
	}

	public void setObjectColor(Color objectColor) {
		this.objectColor = objectColor;
	}

}
