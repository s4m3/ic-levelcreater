package view;

import helper.LevelIO;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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

		// include hotkey for saving
		Action saveAction = new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (levelController == null) {
					return;
				}
				LevelIO levelIO = new LevelIO();
				levelIO.saveLevelToFile(levelController.getLevel()
						.getLevelObjects(), levelController
						.getLevelParameters().getLevelName());
			}
		};

		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK);
		this.getActionMap().put("Save", saveAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke,
				"Save");

		// include hotkey for changing poly
		Action switchAction = new AbstractAction("Change") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showFilledPolygons = !showFilledPolygons;
				repaint();
			}
		};

		KeyStroke keyStroke2 = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
		this.getActionMap().put("Change", switchAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke2,
				"Change");

		// include hotkey for showing points of polygons
		Action pointsAction = new AbstractAction("Show Points") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPolyPoints = !showPolyPoints;
				repaint();
			}
		};

		KeyStroke keyStroke3 = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0);
		this.getActionMap().put("Show Points", pointsAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke3,
				"Show Points");

		// include hotkey for zooming in
		Action zoomInAction = new AbstractAction("Zoom In") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scale += 0.1;
				repaint();
			}
		};

		KeyStroke keyStroke4 = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
		this.getActionMap().put("Zoom In", zoomInAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke4,
				"Zoom In");

		// include hotkey for zooming out
		Action zoomOutAction = new AbstractAction("Zoom Out") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scale -= 0.1;
				repaint();
			}
		};

		KeyStroke keyStroke5 = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
		this.getActionMap().put("Zoom Out", zoomOutAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke5,
				"Zoom Out");

		// include hotkey for showing paths
		Action showPathAction = new AbstractAction("Show Path") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPaths = !showPaths;
				repaint();
			}
		};

		KeyStroke keyStroke6 = KeyStroke.getKeyStroke(KeyEvent.VK_B, 0);
		this.getActionMap().put("Show Path", showPathAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke6,
				"Show Path");

		this.setLayout(null);
		this.revalidate();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		paintLevel(g2);

	}

	public void repaintLevel() {
		super.repaint();
		repaint();

	}

	public void paintLevel(Graphics2D g2) {
		ArrayList<LevelObject> levelObjs = (ArrayList<LevelObject>) levelController
				.getLevel().getLevelObjects();
		AffineTransform scaleMatrix = new AffineTransform();
		scaleMatrix.scale(scale, scale);
		this.setPreferredSize(new Dimension(levelController
				.getLevelParameters().getLevelWidth() * levelController.scale,
				levelController.getLevelParameters().getLevelHeight()
						* levelController.scale));
		g2.setTransform(scaleMatrix);
		paintLevelObjects(g2, levelObjs);
		if (showPaths)
			paintPaths(g2);
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
