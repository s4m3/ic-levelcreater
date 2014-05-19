package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.text.PlainDocument;

import layout.SpringUtilities;
import model.level.LevelParameterDefaults;
import model.level.LevelParameters;
import controller.actionlistener.CreateButtonListener;
import controller.actionlistener.LevelNumOfWaypointsParameterListener;
import controller.actionlistener.LevelheightParameterListener;
import controller.actionlistener.LevelnameParameterListener;
import controller.actionlistener.LevelwidthParameterListener;
import controller.actionlistener.ObstacleChangeListener;
import controller.actionlistener.ParameterListenerBase;
import controller.actionlistener.RegionMinSizeChangeListener;
import controller.actionlistener.ScaleParameterListener;
import filters.IntegerFilter;

public class LevelCreater extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1728955245801240639L;
	private static final Dimension defaultDimension = new Dimension(800, 600);
	private static LevelCreater instance = null;

	private LevelParameters levelParameters;

	public JProgressBar progressBar;
	public JButton createButton;
	public JTextArea outputTextField;
	public JLabel timerLabel;

	private LevelCreater() {
		this.setTitle("Level Creater");
		this.setSize(defaultDimension);
		this.setLocation(100, 100);
		levelParameters = new LevelParameters();
	}

	public static LevelCreater getInstance() {
		if (instance == null) {
			instance = new LevelCreater();
		}
		return instance;
	}

	private static void createAndShowGUI() {
		LevelCreater frame = LevelCreater.getInstance();
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
				"Number of Waypoints: ", "Scale: " };
		int numPairs = labels.length;

		// Create and populate the panel.\
		JPanel allParametersPanel = new JPanel();
		allParametersPanel.setLayout(new BoxLayout(allParametersPanel,
				BoxLayout.Y_AXIS));
		JPanel parameterPanel = new JPanel(new SpringLayout());

		addParameter("Levelname", LevelParameterDefaults.levelName,
				parameterPanel,
				new LevelnameParameterListener(levelParameters), false);
		addParameter("Width", LevelParameterDefaults.LEVEL_WIDTH,
				parameterPanel,
				new LevelwidthParameterListener(levelParameters), true);
		addParameter("Height", LevelParameterDefaults.LEVEL_HEIGHT,
				parameterPanel, new LevelheightParameterListener(
						levelParameters), true);
		addParameter("Number of Waypoints",
				LevelParameterDefaults.NUM_WAYPOINTS, parameterPanel,
				new LevelNumOfWaypointsParameterListener(levelParameters), true);
		addParameter("Scale", LevelParameterDefaults.SCALE, parameterPanel,
				new ScaleParameterListener(levelParameters), true);

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(parameterPanel, numPairs, 2, // rows,//
																		// cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		Border paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		parameterPanel.setBorder(paneEdge);
		allParametersPanel.add(parameterPanel);

		levelParameters.setObstacles(LevelParameterDefaults.OBSTACLES);
		JPanel obstaclePanel = new JPanel();
		obstaclePanel.setBorder(paneEdge);
		JLabel obstacleLabel = new JLabel("Wall Percentage");
		JLabel amountOfObstaclesLabel = new JLabel("medium");
		Dimension d = amountOfObstaclesLabel.getPreferredSize();
		amountOfObstaclesLabel
				.setPreferredSize(new Dimension(d.width, d.height));
		obstaclePanel.add(obstacleLabel);

		JSlider amoutOfObstacleSlider = new JSlider(JSlider.HORIZONTAL,
				LevelParameterDefaults.OBSTACLE_MIN,
				LevelParameterDefaults.OBSTACLE_MAX,
				LevelParameterDefaults.OBSTACLES);
		amoutOfObstacleSlider.addChangeListener(new ObstacleChangeListener(
				levelParameters, amountOfObstaclesLabel));
		// Turn on labels at major tick marks.
		amoutOfObstacleSlider.setMajorTickSpacing(10);
		amoutOfObstacleSlider.setMinorTickSpacing(5);
		amoutOfObstacleSlider.setPaintTicks(true);
		// amoutOfObstacleSlider.setPaintLabels(true);
		obstaclePanel.add(amoutOfObstacleSlider);

		obstaclePanel.add(amountOfObstaclesLabel);

		allParametersPanel.add(obstaclePanel);

		// MIN SIZE OF REGION PARAMETER
		levelParameters
				.setMinSizeRegionInMapSizePercentage(LevelParameterDefaults.MIN_SIZE_REGION_IN_MAP_SIZE_PERCENTAGE);
		JPanel minSizeRegionPanel = new JPanel();
		minSizeRegionPanel.setBorder(paneEdge);
		JLabel minSizeRegionLabel = new JLabel("Delete Small Walls");
		minSizeRegionPanel.add(minSizeRegionLabel);
		JLabel minSizeRegionPercentageLabel = new JLabel("normal");
		Dimension dim = minSizeRegionPercentageLabel.getPreferredSize();
		minSizeRegionPercentageLabel.setPreferredSize(new Dimension(dim.width,
				dim.height));
		minSizeRegionPanel.add(minSizeRegionPercentageLabel);

		JSlider minSizeRegionSlider = new JSlider(
				JSlider.HORIZONTAL,
				(int) (LevelParameterDefaults.MIN_SIZE_REGION_IN_MAP_SIZE_PERCENTAGE_MIN * 10000),
				(int) (LevelParameterDefaults.MIN_SIZE_REGION_IN_MAP_SIZE_PERCENTAGE_MAX * 10000),
				(int) (LevelParameterDefaults.MIN_SIZE_REGION_IN_MAP_SIZE_PERCENTAGE * 10000));
		minSizeRegionSlider.addChangeListener(new RegionMinSizeChangeListener(
				levelParameters, minSizeRegionPercentageLabel));

		minSizeRegionSlider.setMajorTickSpacing(1);
		minSizeRegionSlider.setMinorTickSpacing(1);
		minSizeRegionSlider.setPaintTicks(true);
		minSizeRegionPanel.add(minSizeRegionSlider);

		minSizeRegionPanel.add(minSizeRegionPercentageLabel);

		allParametersPanel.add(minSizeRegionPanel);

		pane.add(allParametersPanel, BorderLayout.CENTER);

		JPanel southPanel = new JPanel(new BorderLayout());
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.X_AXIS));
		JLabel progressLabel = new JLabel("Progress: ");
		progressPanel.add(progressLabel);
		timerLabel = new JLabel("--:--:--");
		progressPanel.add(timerLabel);

		southPanel.add(progressPanel, BorderLayout.NORTH);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		southPanel.add(progressBar, BorderLayout.WEST);

		createButton = new JButton("CREATE");
		createButton.addActionListener(new CreateButtonListener());

		outputTextField = new JTextArea();
		outputTextField
				.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		outputTextField.setText("Logger...");
		outputTextField.setEditable(false);
		JScrollPane outputTextScrollPane = new JScrollPane(outputTextField);
		outputTextScrollPane.setPreferredSize(new Dimension(100, 100));
		outputTextScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0,
				0, 0));
		southPanel.add(outputTextScrollPane, BorderLayout.SOUTH);

		southPanel.add(createButton, BorderLayout.EAST);

		// include hotkey for creating level
		Action createAction = new AbstractAction("Create") {

			private static final long serialVersionUID = 2091583767957058534L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createLevel();
			}
		};

		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		southPanel.getActionMap().put("Create", createAction);
		southPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				keyStroke, "Create");

		pane.add(southPanel, BorderLayout.SOUTH);

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

	public void createLevel() {
		JOptionPane.showMessageDialog(null, "create LEvel ");
		// TODO: logic here???
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
