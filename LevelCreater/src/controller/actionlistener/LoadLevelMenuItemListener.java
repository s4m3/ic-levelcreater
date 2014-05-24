package controller.actionlistener;

import helper.LevelIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.level.Level;
import model.level.objects.LevelObject;
import view.LevelsContainer;
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
		if (loadedLevelObjs != null && loadedLevelObjs.size() > 0) {
			Level level = new Level(loadedLevelObjs);
			levelController.setLevel(level);
			levelController.getLevelParameters().setLevelName(levelIO.levelName);
			levelController.getLevelParameters().setLevelWidth(levelIO.levelWidth);
			levelController.getLevelParameters().setLevelHeight(levelIO.levelHeight);
			LevelsContainer levelContainer = new LevelsContainer(levelController);
			levelContainer.setVisible(true);
		}

	}
}
