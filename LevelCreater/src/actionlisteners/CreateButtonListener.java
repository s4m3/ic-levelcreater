package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Level;
import model.LevelParameters;
import view.LevelFrame;

public class CreateButtonListener implements ActionListener {

	private LevelParameters levelParameters;

	public CreateButtonListener(LevelParameters source) {
		super();
		this.levelParameters = source;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Level level = new Level(levelParameters);
		level.createLevel();
		LevelFrame levelFrame = new LevelFrame(level);
		// level.pack();
		levelFrame.setVisible(true);

	}

}
