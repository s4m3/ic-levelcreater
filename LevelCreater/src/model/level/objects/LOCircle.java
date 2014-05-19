package model.level.objects;

import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class LOCircle extends LevelObject {

	protected Ellipse2D.Double ellipse;

	public LOCircle() {
		super();
	}

	public Ellipse2D.Double getEllipse() {
		return ellipse;
	}

	protected void setPosition(int xPos, int yPos, int xSize, int ySize) {
		this.position = new Point(xPos + xSize / 2, yPos + ySize / 2);
	}

	public void scale(int scale) {
		ellipse.height *= scale;
		ellipse.width *= scale;
		ellipse.x *= scale;
		ellipse.y *= scale;
		position.x *= scale;
		position.y *= scale;

	}

	public void translate(int xTranslate, int yTranslate) {
		ellipse.x += xTranslate;
		ellipse.y += yTranslate;
		position.x += xTranslate;
		position.y += yTranslate;
	}

}
