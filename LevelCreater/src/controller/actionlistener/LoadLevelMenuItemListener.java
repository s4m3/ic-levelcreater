package controller.actionlistener;

import helper.LevelIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.level.objects.LevelObject;
import controller.LevelController;

public class LoadLevelMenuItemListener implements ActionListener {

	private LevelController levelController;

	public LoadLevelMenuItemListener(LevelController levelController) {
		this.levelController = levelController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.levelController == null) {
			return;
		}
		LevelIO levelIO = new LevelIO();
		List<LevelObject> loadedLevelObjs = levelIO.loadLevelFromFile();
		// TODO: populate level
	}

}
