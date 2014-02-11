package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import controller.LevelController;
import model.LOCircle;
import model.LOPolygon;
import model.LevelObject;

public class LevelPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3171735915272793867L;
	private static final int scale = 10;
	private LevelController level;

	public LevelPanel(LevelController level) {
		super();
		this.level = level;
		this.setPreferredSize(new Dimension(level.getLevelParameters()
				.getLevelWidth(), level.getLevelParameters().getLevelHeight()));
		this.setLayout(null);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// g2.setBackground(Color.GREEN);
		// g2.setColor(Color.BLUE);
		// // g2.set
		// g2.fillRect(10, 10, 1510, 510);
		// g2.clearRect(10, 10, 300, 300);
		paintLevel(g2);

	}

	public void paintLevel(Graphics2D g2) {
		ArrayList<LevelObject> levelObjs = this.level.getLevelObjectList();
		AffineTransform scaleMatrix = new AffineTransform();
        scaleMatrix.scale(scale, scale);
		this.setPreferredSize(new Dimension(level.getLevelParameters()
				.getLevelWidth() * scale, level.getLevelParameters().getLevelHeight() * scale));
        g2.setTransform(scaleMatrix);
		for (LevelObject levelObject : levelObjs) {
			g2.setColor(levelObject.getObjectColor());
			if (levelObject instanceof LOPolygon) {
				g2.fillPolygon(((LOPolygon) levelObject).getPolygon());
			} else if (levelObject instanceof LOCircle) {
				g2.fill(((LOCircle) levelObject).getEllipse());
			}
		}
	}
}
