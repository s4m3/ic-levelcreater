package controller.actionlistener;

import helper.LevelIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.LevelController;

public class SaveLevelMenuItemListener implements ActionListener {

	private LevelController levelController;
	
	public SaveLevelMenuItemListener(LevelController level) {
		this.levelController = level;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//JOptionPane.showMessageDialog(null, "save");
		if(this.levelController == null) {
			return;
		}
		
		LevelIO levelIO = new LevelIO();
		levelIO.saveLevelToFile(levelController.getLevel().getLevelObjects(), levelController.getLevelParameters().getLevelName());
	}

}
