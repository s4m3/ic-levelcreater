package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		LevelFrame level = new LevelFrame(levelParameters);
		// level.pack();
		level.setVisible(true);

	}

}
