package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import model.LevelParameters;
import controller.LevelController;
import controller.actionlistener.SaveLevelMenuItemListener;

public class LevelFrame extends JFrame implements ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6379564719134837001L;
	private LevelPanel levelPanel;

	public LevelFrame(LevelController level) {
		LevelParameters lp = level.getLevelParameters();
		this.setTitle(lp.getLevelName());
		// this.setSize(lp.getLevelWidth(), lp.getLevelHeight() + 40/* + menubar
		// */);
		this.setSize(new Dimension(800, 600));
		// this.setPreferredSize(new Dimension(800, 600));
		this.setLocation(400, 100);

		// MENU
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Level");
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new SaveLevelMenuItemListener(level));
		menu.add(save);
		menuBar.add(menu);

		// Container pane = this.getContentPane();
		this.setJMenuBar(menuBar);

		JPanel levelsPanel = new JPanel(new GridLayout(1, 1));
		levelPanel = new LevelPanel(level);

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
		levelsPanel.setPreferredSize(new Dimension(level.getLevelParameters()
				.getLevelWidth() + 10, level.getLevelParameters()
				.getLevelHeight() + 4));
		this.add(scrollPane);
		this.pack();
		levelPanel.repaint();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println(e.getSource().toString());

	}

}
