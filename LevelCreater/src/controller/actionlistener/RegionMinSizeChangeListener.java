package controller.actionlistener;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.level.LevelParameterDefaults;
import model.level.LevelParameters;

public class RegionMinSizeChangeListener implements ChangeListener {

	LevelParameters levelParameters;
	JLabel label;

	public RegionMinSizeChangeListener(LevelParameters levelParameters, JLabel label) {
		this.levelParameters = levelParameters;
		this.label = label;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		double value = source.getValue() / 10000.0;
		// since wall areas always small, only delete very small areas when level size very big
		if ((levelParameters.getLevelHeight() * levelParameters.getLevelWidth()) > 100000)
			value /= 10;

		levelParameters.setMinSizeRegionInMapSizePercentage(value);
		label.setText(value == 0 ? "no" : value < LevelParameterDefaults.MIN_SIZE_REGION_IN_MAP_SIZE_PERCENTAGE - 0.0001 ? "few"
				: value < LevelParameterDefaults.MIN_SIZE_REGION_IN_MAP_SIZE_PERCENTAGE + 0.0001 ? "normal" : "many");

	}

}
