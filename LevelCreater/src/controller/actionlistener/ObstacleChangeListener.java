package controller.actionlistener;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.LevelParameterDefaults;
import model.LevelParameters;

public class ObstacleChangeListener implements ChangeListener {

	LevelParameters levelParameters;
	JLabel label;
	public ObstacleChangeListener(LevelParameters levelParameters, JLabel label) {
		this.levelParameters = levelParameters;
		this.label = label;
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
        int obstacleValue = (int)source.getValue();
        levelParameters.setObstacles(obstacleValue);
        label.setText(obstacleValue < LevelParameterDefaults.OBSTACLES - 1 ? "low" : obstacleValue < LevelParameterDefaults.OBSTACLES + 1 ? "medium" : "high");

	}

}
