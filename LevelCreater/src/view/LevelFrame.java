package view;

import java.awt.Container;

import javax.swing.JFrame;

import model.LevelParameters;

public class LevelFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6379564719134837001L;

	public LevelFrame(LevelParameters levelParameters) {
		this.setTitle(levelParameters.getLevelName());
		this.setSize(levelParameters.getLevelWidth(),
				levelParameters.getLevelHeight());
		this.setLocation(300, 200);
		Container pane = this.getContentPane();
		pane.add(new LevelPanel());
	}

}
