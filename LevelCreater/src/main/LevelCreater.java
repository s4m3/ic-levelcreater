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

import layout.SpringUtilities;

public class LevelCreater extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1728955245801240639L;
	private static final Dimension defaultDimension = new Dimension(800, 600);

	public LevelCreater() {
		this.setTitle("Level Creater");
		this.setSize(defaultDimension);
		this.setLocation(200, 100);
	}

	private static void createAndShowGUI() {
		LevelCreater frame = new LevelCreater();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	private static void addComponentsToPane(Container pane) {
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
		for (int i = 0; i < numPairs; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			parameterPanel.add(l);
			JTextField textField = new JTextField(10);
			l.setLabelFor(textField);
			parameterPanel.add(textField);
		}

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(parameterPanel, numPairs, 2, // rows,//
																		// cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		Border paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		parameterPanel.setBorder(paneEdge);
		pane.add(parameterPanel, BorderLayout.CENTER);

		JButton createButton = new JButton("CREATE");
		pane.add(createButton, BorderLayout.SOUTH);

		// REFERENCE TODO: DELETE
		//
		//
		// JButton button = new JButton("Button 1 (PAGE_START)");
		// pane.add(button, BorderLayout.PAGE_START);
		//
		// // Make the center component big, since that's the
		// // typical usage of BorderLayout.
		// button = new JButton("Button 2 (CENTER)");
		// button.setPreferredSize(new Dimension(200, 100));
		// pane.add(button, BorderLayout.CENTER);
		//
		// button = new JButton("Button 3 (LINE_START)");
		// pane.add(button, BorderLayout.LINE_START);
		//
		// button = new JButton("Long-Named Button 4 (PAGE_END)");
		// pane.add(button, BorderLayout.PAGE_END);
		//
		// button = new JButton("5 (LINE_END)");
		// pane.add(button, BorderLayout.LINE_END);
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
