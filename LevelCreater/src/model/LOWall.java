package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;

import enums.StandardWall;

public class LOWall extends LOPolygon {

	public LOWall() {
		this.objectColor = Color.DARK_GRAY;
	}
	
	public LOWall(Point position, int width, int height) {
		this.objectColor = Color.DARK_GRAY;
		this.position = position;
		int[] xs = { position.x, position.x + width, position.x + width, position.x };
		int[] ys = { position.y, position.y, position.y + height, position.y + height };
		this.polygon = new Polygon(xs, ys, xs.length);
	}
	
	public LOWall(Polygon shape) {
		this.objectColor = Color.DARK_GRAY;
		this.polygon = shape;
	}

	public LOWall(StandardWall wallType, int levelWidth, int levelHeigth,
			int wallDepth) {
		this.objectColor = Color.DARK_GRAY;
		switch (wallType) {
		case TOP:
			createTopWall(levelWidth, wallDepth);
			break;
		case BOTTOM:
			createBottomWall(levelWidth, levelHeigth, wallDepth);
			break;
		case LEFT:
			createLeftWall(levelHeigth, wallDepth);
			break;
		case RIGHT:
			createRightWall(levelWidth, levelHeigth, wallDepth);
			break;
		default:
			break;
		}
	}

	private void createTopWall(int levelWidth, int wallDepth) {
		int[] xs = { 0, levelWidth, levelWidth, 0 };
		int[] ys = { 0, 0, wallDepth, wallDepth };
		this.polygon = new Polygon(xs, ys, xs.length);

	}

	private void createBottomWall(int levelWidth, int levelHeight, int wallDepth) {
		int[] xs = { 0, levelWidth, levelWidth, 0 };
		int[] ys = { levelHeight - wallDepth, levelHeight - wallDepth,
				levelHeight, levelHeight };

		this.polygon = new Polygon(xs, ys, xs.length);

	}

	private void createLeftWall(int levelHeight, int wallDepth) {
		int[] xs = { 0, wallDepth, wallDepth, 0 };
		int[] ys = { 0, 0, levelHeight, levelHeight };
		this.polygon = new Polygon(xs, ys, xs.length);
	}

	private void createRightWall(int levelWidth, int levelHeight, int wallDepth) {
		int[] xs = { levelWidth - wallDepth, levelWidth, levelWidth,
				levelWidth - wallDepth };
		int[] ys = { 0, 0, levelHeight, levelHeight };
		this.polygon = new Polygon(xs, ys, xs.length);
	}

	public void setShape(Polygon polygon) {

	}

}
