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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import model.LOCircle;
import model.LOFloor;
import model.LOPolygon;
import model.LevelObject;
import controller.LevelController;

public class LevelPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3171735915272793867L;

	private LevelController levelController;
	private boolean showFilledPolygons = true;
	
	private boolean showPolyPoints = false;
	private static final int polyPointSize = 4;
	
	public LevelPanel(LevelController level) {
		super();
		this.levelController = level;
		
		//include hotkey for saving
		Action saveAction = new AbstractAction("Save") {
			private static final long serialVersionUID = 8144901302424973712L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(levelController == null) {
					return;
				}
				LevelIO levelIO = new LevelIO();
				levelIO.saveLevelToFile(levelController.getLevelObjectList(), levelController.getLevelParameters().getLevelName());
			}
		};
		
		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
		this.getActionMap().put("Save", saveAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "Save");
		
		//include hotkey for changing poly
		Action switchAction = new AbstractAction("Change") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showFilledPolygons = !showFilledPolygons;
				repaint();
			}
		};
		
		KeyStroke keyStroke2 = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
		this.getActionMap().put("Change", switchAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke2, "Change");
		
		//include hotkey for showing points of polygons
		Action pointsAction = new AbstractAction("Show Points") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPolyPoints = !showPolyPoints;
				repaint();
			}
		};
		
		KeyStroke keyStroke3 = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0);
		this.getActionMap().put("Show Points", pointsAction);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke3, "Show Points");
		
//		this.setPreferredSize(new Dimension(level.getLevelParameters()
//				.getLevelWidth(), level.getLevelParameters().getLevelHeight()));
		this.setLayout(null);
		this.revalidate();
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
		ArrayList<LevelObject> levelObjs = this.levelController.getLevelObjectList();
		//AffineTransform scaleMatrix = new AffineTransform();
        //scaleMatrix.scale(scale, scale);
		this.setPreferredSize(new Dimension(levelController.getLevelParameters()
				.getLevelWidth() * levelController.scale, levelController.getLevelParameters().getLevelHeight() * levelController.scale));
        //g2.setTransform(scaleMatrix);
		for (LevelObject levelObject : levelObjs) {
			g2.setColor(levelObject.getObjectColor());
			if (levelObject instanceof LOPolygon) {
				Polygon poly = ((LOPolygon) levelObject).getPolygon();
//				System.out.println(poly.npoints + " punkte");
//				for (int i = 0; i < poly.npoints; i++) {
//					System.out.println(poly.xpoints[i] + ":" + poly.ypoints[i]);
//				}
				if(showFilledPolygons) 
					g2.fillPolygon(poly);
				else
					g2.drawPolygon(poly);
				
				if(showPolyPoints) {
					g2.setColor(Color.MAGENTA);
					for (int i = 0; i < poly.npoints; i++) {
						Ellipse2D.Double pointEllipse = new Ellipse2D.Double(poly.xpoints[i] - polyPointSize/2, poly.ypoints[i] - polyPointSize/2, polyPointSize, polyPointSize);
						g2.fill(pointEllipse);
					}
				}
			} else if (levelObject instanceof LOCircle) {
				Ellipse2D.Double ellipse = ((LOCircle) levelObject).getEllipse();
				g2.fill(ellipse);
			}
		}
	}
}
