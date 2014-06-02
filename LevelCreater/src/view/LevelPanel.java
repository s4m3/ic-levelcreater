package view;

import java.awt.BasicStroke;
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

import model.astar.Path;
import model.level.Level;
import model.level.MapPoint;
import model.level.objects.LOCircle;
import model.level.objects.LOCircledWall;
import model.level.objects.LOPolygon;
import model.level.objects.LOSpeedUp;
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
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintLevel(g2);

	}

	public void repaintLevel() {
		repaint();

	}

	public void paintLevel(Graphics2D g2) {
		Level level = levelController.getLevel();
		AffineTransform scaleMatrix = new AffineTransform();
		scaleMatrix.scale(scale, scale);
		g2.setTransform(scaleMatrix);
		paintLevelObjects(g2, level);
		if (showPaths)
			paintPaths(g2);
		revalidate();
		super.revalidate();
	}

	private void paintLevelObjects(Graphics2D g2, Level level) {
		paintPolys(g2, level.getFloor());
		paintPolys(g2, level.getSlowDowners());
		paintLines(g2, level.getSpeedUps());
		paintPolys(g2, level.getWalls());
		paintCircles(g2, level.getWaypoints());
	}

	private void paintCircles(Graphics2D g2, ArrayList<LOCircle> circles) {
		for (LOCircle loCircle : circles) {
			g2.setColor(loCircle.getObjectColor());
			Ellipse2D.Double ellipse = loCircle.getEllipse();
			if (showFilledPolygons)
				g2.fill(ellipse);
			else
				g2.draw(ellipse);

			if (showPolyPoints) {
				if (loCircle instanceof LOCircledWall) {
					g2.setColor(Color.MAGENTA);
					Ellipse2D.Double pointEllipse = new Ellipse2D.Double(loCircle.getPosition().x - polyPointSize / 2,
							loCircle.getPosition().y - polyPointSize / 2, polyPointSize, polyPointSize);
					g2.fill(pointEllipse);
				}
			}
		}

	}

	private void paintLines(Graphics2D g2, ArrayList<LOPolygon> polys) {
		for (LOPolygon loPolygon : polys) {
			paintSpeedUpAsLine(g2, (LOSpeedUp) loPolygon);
		}

	}

	private void paintPolys(Graphics2D g2, ArrayList<LOPolygon> polys) {
		for (LOPolygon loPoly : polys) {
			Polygon poly = loPoly.getPolygon();
			g2.setColor(loPoly.getObjectColor());
			if (showFilledPolygons) {
				g2.fillPolygon(poly);
			} else {
				g2.setStroke(new BasicStroke(1));
				g2.drawPolygon(poly);
			}

			if (showPolyPoints) {
				g2.setColor(Color.MAGENTA);
				for (int i = 0; i < poly.npoints; i++) {
					Ellipse2D.Double pointEllipse = new Ellipse2D.Double(poly.xpoints[i] - polyPointSize / 2, poly.ypoints[i]
							- polyPointSize / 2, polyPointSize, polyPointSize);
					g2.fill(pointEllipse);
				}
			}
		}

	}

	private void paintSpeedUpAsLine(Graphics2D g2, LOSpeedUp speedUpObj) {
		g2.setColor(speedUpObj.getObjectColor());
		if (showFilledPolygons) {
			g2.setStroke(new BasicStroke(levelController.getLevelParameters().getScale() + speedUpObj.getLineWidth(),
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		} else {
			g2.setStroke(new BasicStroke(1));
		}
		Polygon poly = speedUpObj.getPolygon();
		for (int i = 0; i < poly.npoints - 1; i++) {
			g2.drawLine(poly.xpoints[i], poly.ypoints[i], poly.xpoints[i + 1], poly.ypoints[i + 1]);
		}
		if (showPolyPoints) {
			g2.setColor(Color.MAGENTA);
			for (int i = 0; i < poly.npoints; i++) {
				Ellipse2D.Double pointEllipse = new Ellipse2D.Double(poly.xpoints[i] - polyPointSize / 2, poly.ypoints[i]
						- polyPointSize / 2, polyPointSize, polyPointSize);
				g2.fill(pointEllipse);
			}
		}
	}

	private void paintPaths(Graphics2D g2) {
		g2.setColor(Color.MAGENTA);
		g2.setStroke(new BasicStroke(1));
		List<Path> paths = levelController.getWpController().getPaths();
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
		return new Dimension((int) (levelController.getLevelParameters().getLevelWidth() * scale), (int) (levelController
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
