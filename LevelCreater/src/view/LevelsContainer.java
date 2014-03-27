package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import model.LevelParameters;
import controller.LevelController;

public class LevelsContainer extends JPanel implements ItemListener,
		ActionListener {
	private static final long serialVersionUID = -1856101864226493020L;
	private LevelPanel levelPanel;
	private JLabel scaleLabel;
	private static int MIN_WIDTH = 800;

	public LevelsContainer(LevelController levelController) {
		LevelParameters lp = levelController.getLevelParameters();
		// this.setSize(lp.getLevelWidth(), lp.getLevelHeight() + 40/* + menubar
		// */);

		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		Dimension preferredSize = new Dimension(
				lp.getLevelWidth() < screenSize.getWidth() ? lp.getLevelWidth()
						: (int) screenSize.getWidth() - 100,
				lp.getLevelHeight() < screenSize.getHeight() ? lp
						.getLevelHeight() : (int) screenSize.getHeight() - 50);

		if (preferredSize.width < MIN_WIDTH)
			preferredSize.width = MIN_WIDTH;
		this.setPreferredSize(preferredSize);

		JPanel levelsPanel = new JPanel(new GridLayout(1, 1));
		levelsPanel.setAlignmentX(LEFT_ALIGNMENT);
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

		JCheckBox showVerticesCheckbox = new JCheckBox("Show Vertices",
				levelPanel.isShowPolyPoints());
		showVerticesCheckbox.addItemListener(this);
		optionsPanel.add(showVerticesCheckbox);

		JCheckBox showFilledPolygonsCheckbox = new JCheckBox(
				"Show Filled Polygons", levelPanel.isShowFilledPolygons());
		showFilledPolygonsCheckbox.addItemListener(this);
		optionsPanel.add(showFilledPolygonsCheckbox);

		JCheckBox showPathsCheckbox = new JCheckBox("Show Paths",
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

		this.add(optionsPanel);

		JScrollPane scrollPane = new JScrollPane(levelsPanel);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		// pane.add(new LevelPanel(level));
		levelsPanel.add(levelPanel);
		levelsPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		levelsPanel.setPreferredSize(new Dimension(levelController
				.getLevelParameters().getLevelWidth() + 10, levelController
				.getLevelParameters().getLevelHeight() + 4));
		this.add(scrollPane);
		// levelPanel.repaint();
	}

	private String getScaleLabelText() {
		return String.format("Scale:%.1f", levelPanel.getScale());
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
