package view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import model.LevelParameters;
import controller.LevelController;

public class LevelsContainer extends JPanel {
	private static final long serialVersionUID = -1856101864226493020L;

	public LevelsContainer(LevelController level) {
		LevelParameters lp = level.getLevelParameters();
		// this.setSize(lp.getLevelWidth(), lp.getLevelHeight() + 40/* + menubar
		// */);

		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension preferredSize = new Dimension(lp.getLevelWidth() < screenSize.getWidth() ? lp.getLevelWidth() : (int) screenSize.getWidth() - 100, lp.getLevelHeight() < screenSize.getHeight() ? lp.getLevelHeight() : (int) screenSize.getHeight() - 50);
			
		this.setPreferredSize(preferredSize);

		
		JPanel levelsPanel = new JPanel(new GridLayout(1, 1));
		LevelPanel levelPanel = new LevelPanel(level);
		
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
		levelsPanel.setPreferredSize(new Dimension(level.getLevelParameters().getLevelWidth() + 10, level.getLevelParameters().getLevelHeight() + 4));
		this.add(scrollPane);
//		levelPanel.repaint();
	}
}
