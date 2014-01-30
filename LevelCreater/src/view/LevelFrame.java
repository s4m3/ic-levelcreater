package view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import model.Level;
import model.LevelParameters;
import actionlisteners.SaveLevelMenuItemListener;

public class LevelFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6379564719134837001L;

	public LevelFrame(Level level) {
		LevelParameters lp = level.getLevelParameters();
		this.setTitle(lp.getLevelName());
		// this.setSize(lp.getLevelWidth(), lp.getLevelHeight() + 40/* + menubar
		// */);
		this.setSize(new Dimension(800, 600));
		this.setPreferredSize(new Dimension(800, 600));
		this.setLocation(200, 200);

		// MENU
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Level");
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new SaveLevelMenuItemListener());
		menu.add(save);
		menuBar.add(menu);

		// Container pane = this.getContentPane();
		this.setJMenuBar(menuBar);
		JScrollPane scrollPane = new JScrollPane(new LevelPanel(level));
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		// pane.add(new LevelPanel(level));
		this.add(scrollPane);
	}
}
