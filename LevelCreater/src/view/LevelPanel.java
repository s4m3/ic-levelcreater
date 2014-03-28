package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import model.LOCircle;
import model.LOCircledWall;
import model.LOPolygon;
import model.LevelObject;
import model.MapPoint;
import model.Path;
import controller.LevelController;

public class LevelPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3171735915272793867L;

	private LevelController levelController;

	private boolean showFilledPolygons = true;

	private boolean showPolyPoints = false;

	private boolean showPaths = false;

	private int polyPointSize = 2;
	private double scale = 1;

	public LevelPanel(LevelController level) {
		super();
		this.levelController = level;
		setBackground(Color.WHITE);

		this.setLayout(new BorderLayout());
		repaintLevel();
		repaintAfterMilliseconds(100);
	}

	public void repaintAfterMilliseconds(int ms) {
		Timer timer = new Timer();
		timer.schedule(scheduleRepaint(), ms);
	}

	private TimerTask scheduleRepaint() {
		return new TimerTask() {
			@Override
			public void run() {
				repaintLevel();
			}
		};
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		paintLevel(g2);

	}

	public void repaintLevel() {
		repaint();

	}

	public void paintLevel(Graphics2D g2) {
		ArrayList<LevelObject> levelObjs = (ArrayList<LevelObject>) levelController
				.getLevel().getLevelObjects();
		AffineTransform scaleMatrix = new AffineTransform();
		scaleMatrix.scale(scale, scale);
		g2.setTransform(scaleMatrix);
		paintLevelObjects(g2, levelObjs);
		if (showPaths)
			paintPaths(g2);
		revalidate();
		super.revalidate();
	}

	private void paintLevelObjects(Graphics2D g2,
			ArrayList<LevelObject> levelObjs) {
		for (LevelObject levelObject : levelObjs) {
			g2.setColor(levelObject.getObjectColor());
			if (levelObject instanceof LOPolygon) {
				Polygon poly = ((LOPolygon) levelObject).getPolygon();
				if (showFilledPolygons)
					g2.fillPolygon(poly);
				else
					g2.drawPolygon(poly);

				if (showPolyPoints) {
					g2.setColor(Color.MAGENTA);
					for (int i = 0; i < poly.npoints; i++) {
						Ellipse2D.Double pointEllipse = new Ellipse2D.Double(
								poly.xpoints[i] - polyPointSize / 2,
								poly.ypoints[i] - polyPointSize / 2,
								polyPointSize, polyPointSize);
						g2.fill(pointEllipse);
					}
				}
			} else if (levelObject instanceof LOCircle) {
				Ellipse2D.Double ellipse = ((LOCircle) levelObject)
						.getEllipse();
				if (showFilledPolygons)
					g2.fill(ellipse);
				else
					g2.draw(ellipse);

				if (showPolyPoints) {
					if (levelObject instanceof LOCircledWall) {
						g2.setColor(Color.MAGENTA);
						Ellipse2D.Double pointEllipse = new Ellipse2D.Double(
								levelObject.getPosition().x - polyPointSize / 2,
								levelObject.getPosition().y - polyPointSize / 2,
								polyPointSize, polyPointSize);
						g2.fill(pointEllipse);
					}
				}
			}
		}
	}

	private void paintPaths(Graphics2D g2) {
		g2.setColor(Color.MAGENTA);
		List<Path> paths = levelController.wpController.getPaths();
		int size, i;
		List<MapPoint> pointList;
		MapPoint aP;
		MapPoint bP;
		for (Path path : paths) {
			pointList = path.getNodes();
			size = pointList.size();
			for (i = 0; i < size - 1; i++) {
				aP = pointList.get(i);
				bP = pointList.get(i + 1);
				g2.drawLine(aP.x, aP.y, bP.x, bP.y);
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (levelController.getLevelParameters()
				.getLevelWidth() * scale), (int) (levelController
				.getLevelParameters().getLevelHeight() * scale));
	}

	public boolean isShowFilledPolygons() {
		return showFilledPolygons;
	}

	public void setShowFilledPolygons(boolean showFilledPolygons) {
		this.showFilledPolygons = showFilledPolygons;
	}

	public boolean isShowPolyPoints() {
		return showPolyPoints;
	}

	public void setShowPolyPoints(boolean showPolyPoints) {
		this.showPolyPoints = showPolyPoints;
	}

	public boolean isShowPaths() {
		return showPaths;
	}

	public void setShowPaths(boolean showPaths) {
		this.showPaths = showPaths;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

}
