package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.text.PlainDocument;

import layout.SpringUtilities;
import model.LevelParameterDefaults;
import model.LevelParameters;
import actionlisteners.CreateButtonListener;
import actionlisteners.LevelNumOfWaypointsParameterListener;
import actionlisteners.LevelheightParameterListener;
import actionlisteners.LevelnameParameterListener;
import actionlisteners.LevelwidthParameterListener;
import actionlisteners.ParameterListenerBase;
import filters.IntegerFilter;

public class LevelCreater extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1728955245801240639L;
	private static final Dimension defaultDimension = new Dimension(800, 600);

	private LevelParameters levelParameters;

	public LevelCreater() {
		this.setTitle("Level Creater");
		this.setSize(defaultDimension);
		this.setLocation(100, 100);
		levelParameters = new LevelParameters();
	}

	private static void createAndShowGUI() {
		LevelCreater frame = new LevelCreater();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	private void addComponentsToPane(Container pane) {
		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}
		JLabel topLabel = new JLabel("Insert parameters and press \"CREATE\"!");
		pane.add(topLabel, BorderLayout.NORTH);
		String[] labels = { "Levelname: ", "Width: ", "Height: ",
				"Number of Waypoints: " };
		int numPairs = labels.length;

		// Create and populate the panel.
		JPanel parameterPanel = new JPanel(new SpringLayout());

		addParameter("Levelname", LevelParameterDefaults.levelName,
				parameterPanel,
				new LevelnameParameterListener(levelParameters), false);
		addParameter("Width", LevelParameterDefaults.levelWidth,
				parameterPanel,
				new LevelwidthParameterListener(levelParameters), true);
		addParameter("Height", LevelParameterDefaults.levelHeight,
				parameterPanel, new LevelheightParameterListener(
						levelParameters), true);
		addParameter("Number of Waypoints",
				LevelParameterDefaults.numOfWaypoints, parameterPanel,
				new LevelNumOfWaypointsParameterListener(levelParameters), true);

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(parameterPanel, numPairs, 2, // rows,//
																		// cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		Border paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		parameterPanel.setBorder(paneEdge);
		pane.add(parameterPanel, BorderLayout.CENTER);

		JButton createButton = new JButton("CREATE");
		createButton.addActionListener(new CreateButtonListener(this
				.getLevelParameters()));
		pane.add(createButton, BorderLayout.SOUTH);

	}

	private void addParameter(String parameterName, Object defaultValue,
			JPanel parameterPanel, ParameterListenerBase listener,
			boolean isIntegerFiltered) {
		JLabel l = new JLabel(parameterName, JLabel.TRAILING);
		parameterPanel.add(l);
		JTextField textField = new JTextField(10);
		if (isIntegerFiltered) {
			PlainDocument doc = (PlainDocument) textField.getDocument();
			doc.setDocumentFilter(new IntegerFilter());
		}
		textField.getDocument().addDocumentListener(listener);
		l.setLabelFor(textField);
		textField.setText(defaultValue.toString());
		parameterPanel.add(textField);
	}

	public LevelParameters getLevelParameters() {
		return levelParameters;
	}

	public void setLevelParameters(LevelParameters levelParameters) {
		this.levelParameters = levelParameters;
	}

	public static void main(String[] args) {
		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

}
