package view;

import helper.LevelIO;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import model.LOCircle;
import model.LOPolygon;
import model.LevelObject;
import controller.LevelController;

public class LevelPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3171735915272793867L;
	private static final int scale = 10;
	private LevelController levelController;

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
		ArrayList<LevelObject> levelObjs = this.levelController.getLevelObjectList();
		AffineTransform scaleMatrix = new AffineTransform();
        scaleMatrix.scale(scale, scale);
		this.setPreferredSize(new Dimension(levelController.getLevelParameters()
				.getLevelWidth() * scale, levelController.getLevelParameters().getLevelHeight() * scale));
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
