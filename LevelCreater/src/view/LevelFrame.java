package view;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
		this.setSize(lp.getLevelWidth(), lp.getLevelHeight() + 40/* + menubar */);
		this.setLocation(50, 50);

		// MENU
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Level");
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new SaveLevelMenuItemListener());
		menu.add(save);
		menuBar.add(menu);

		Container pane = this.getContentPane();
		this.setJMenuBar(menuBar);
		pane.add(new LevelPanel(level));
	}
}
