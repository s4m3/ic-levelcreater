package view;

import helper.LevelIO;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import model.LevelParameters;
import controller.LevelController;
import controller.actionlistener.SaveLevelMenuItemListener;

public class LevelsContainer extends JFrame implements ItemListener,
		ActionListener {
	private static final long serialVersionUID = -1856101864226493020L;
	private LevelPanel levelPanel;

	private JLabel scaleLabel;

	private JCheckBox showVerticesCheckbox;
	private JCheckBox showFilledPolygonsCheckbox;
	private JCheckBox showPathsCheckbox;

	private static int MIN_WIDTH = 800;

	@SuppressWarnings("serial")
	public LevelsContainer(final LevelController levelController) {
		LevelParameters lp = levelController.getLevelParameters();

		setTitle(levelController.getLevelParameters().getLevelName());
		setLocation(400, 100);
		Container contentPane = getContentPane();
		// MENU
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Level");
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new SaveLevelMenuItemListener(levelController));
		menu.add(save);
		menuBar.add(menu);

		setJMenuBar(menuBar);

		contentPane.setLayout(new BorderLayout());

		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		Dimension preferredSize = new Dimension(
				lp.getLevelWidth() < screenSize.getWidth() ? lp.getLevelWidth()
						: (int) screenSize.getWidth() - 100,
				lp.getLevelHeight() < screenSize.getHeight() ? lp
						.getLevelHeight() : (int) screenSize.getHeight() - 50);

		if (preferredSize.width < MIN_WIDTH)
			preferredSize.width = MIN_WIDTH;

		preferredSize.height += 150;
		setSize(preferredSize);
		// JPanel levelsPanel = new JPanel(new BorderLayout());
		// levelsPanel.setAlignmentX(LEFT_ALIGNMENT);
		levelPanel = new LevelPanel(levelController);

		// THIS Should not be here! TODO or ok??
		JPanel optionsPanel = new JPanel(new FlowLayout());
		optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		optionsPanel.setMaximumSize(new Dimension(screenSize.width, 60));
		optionsPanel.setAlignmentX(LEFT_ALIGNMENT);

		JLabel verticesCount = new JLabel(String.format(
				"Number of Vertices:%s", levelController.getLevel()
						.getNumOfVertices()));
		optionsPanel.add(verticesCount);

		showVerticesCheckbox = new JCheckBox("Show Vertices",
				levelPanel.isShowPolyPoints());
		showVerticesCheckbox.addItemListener(this);
		optionsPanel.add(showVerticesCheckbox);

		showFilledPolygonsCheckbox = new JCheckBox("Show Filled Polygons",
				levelPanel.isShowFilledPolygons());
		showFilledPolygonsCheckbox.addItemListener(this);
		optionsPanel.add(showFilledPolygonsCheckbox);

		showPathsCheckbox = new JCheckBox("Show Paths",
				levelPanel.isShowPaths());
		showPathsCheckbox.addItemListener(this);
		optionsPanel.add(showPathsCheckbox);

		scaleLabel = new JLabel(getScaleLabelText());
		optionsPanel.add(scaleLabel);

		JButton scaleUpButton = new JButton("+");
		scaleUpButton.addActionListener(this);
		optionsPanel.add(scaleUpButton);

		JButton scaleDownButton = new JButton("-");
		scaleDownButton.addActionListener(this);
		optionsPanel.add(scaleDownButton);

		contentPane.add(optionsPanel, BorderLayout.NORTH);

		levelPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		levelPanel.setPreferredSize(new Dimension(levelController
				.getLevelParameters().getLevelWidth(), levelController
				.getLevelParameters().getLevelHeight()));
		JScrollPane scrollPane = new JScrollPane(levelPanel);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		// pane.add(new LevelPanel(level));
		// levelsPanel.add(levelPanel);

		contentPane.add(scrollPane, BorderLayout.CENTER);

		this.getRootPane().addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				levelPanel.repaintAfterMilliseconds(100);
			}
		});

		//
		// HOTKEYS //
		//

		// include hotkey for saving
		Action saveAction = new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				LevelIO levelIO = new LevelIO();
				levelIO.saveLevelToFile(levelController.getLevel()
						.getLevelObjects(), levelController
						.getLevelParameters().getLevelName());
			}
		};

		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK);
		levelPanel.getActionMap().put("Save", saveAction);
		levelPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				keyStroke, "Save");

		// include hotkey for changing poly
		Action switchAction = new AbstractAction("Change") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				levelPanel.setShowFilledPolygons(!levelPanel
						.isShowFilledPolygons());
				levelPanel.repaintLevel();
				updateGUI();
			}
		};

		KeyStroke keyStroke2 = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
		levelPanel.getActionMap().put("Change", switchAction);
		levelPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				keyStroke2, "Change");

		// include hotkey for showing points of polygons
		Action pointsAction = new AbstractAction("Show Points") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				levelPanel.setShowPolyPoints(!levelPanel.isShowPolyPoints());
				levelPanel.repaintLevel();
				updateGUI();
			}
		};

		KeyStroke keyStroke3 = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0);
		levelPanel.getActionMap().put("Show Points", pointsAction);
		levelPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				keyStroke3, "Show Points");

		// include hotkey for zooming in
		Action zoomInAction = new AbstractAction("Zoom In") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				levelPanel.setScale(levelPanel.getScale() + 0.1);
				levelPanel.repaintLevel();
				updateGUI();
			}
		};

		KeyStroke keyStroke4 = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
		levelPanel.getActionMap().put("Zoom In", zoomInAction);
		levelPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				keyStroke4, "Zoom In");

		// include hotkey for zooming out
		Action zoomOutAction = new AbstractAction("Zoom Out") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				levelPanel.setScale(levelPanel.getScale() - 0.1);
				levelPanel.repaintLevel();
				updateGUI();
			}
		};

		KeyStroke keyStroke5 = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
		levelPanel.getActionMap().put("Zoom Out", zoomOutAction);
		levelPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				keyStroke5, "Zoom Out");

		// include hotkey for showing paths
		Action showPathAction = new AbstractAction("Show Path") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				levelPanel.setShowPaths(!levelPanel.isShowPaths());
				levelPanel.repaintLevel();
				updateGUI();
			}
		};

		KeyStroke keyStroke6 = KeyStroke.getKeyStroke(KeyEvent.VK_B, 0);
		levelPanel.getActionMap().put("Show Path", showPathAction);
		levelPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				keyStroke6, "Show Path");

	}

	private String getScaleLabelText() {
		return String.format("Scale:%.1f", levelPanel.getScale());
	}

	private void updateGUI() {
		this.showFilledPolygonsCheckbox.setSelected(levelPanel
				.isShowFilledPolygons());
		this.showPathsCheckbox.setSelected(levelPanel.isShowPaths());
		this.showVerticesCheckbox.setSelected(levelPanel.isShowPolyPoints());
		this.scaleLabel.setText(String.format("Scale:%.1f",
				levelPanel.getScale()));
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (levelPanel == null)
			return;

		String checkBoxLabel = ((JCheckBox) e.getSource()).getText();
		boolean state = ((JCheckBox) e.getSource()).isSelected();
		if (checkBoxLabel == "Show Filled Polygons") {
			levelPanel.setShowFilledPolygons(state);
		} else if (checkBoxLabel == "Show Vertices") {
			levelPanel.setShowPolyPoints(state);
		} else if (checkBoxLabel == "Show Paths") {
			levelPanel.setShowPaths(state);
		}
		levelPanel.repaintLevel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (levelPanel == null)
			return;

		String text = ((JButton) e.getSource()).getText();
		double scale = levelPanel.getScale();
		if (text == "+")
			levelPanel.setScale(scale + 0.1);
		else if (text == "-")
			levelPanel.setScale(scale - 0.1);

		levelPanel.repaintLevel();
		scaleLabel.setText(getScaleLabelText());

	}

}
