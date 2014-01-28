package actionlisteners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import model.LevelParameterDefaults;
import model.LevelParameters;

public class LevelNumOfWaypointsParameterListener extends ParameterListenerBase
		implements DocumentListener {

	public LevelNumOfWaypointsParameterListener(LevelParameters levelParameters) {
		super(levelParameters);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateLabel(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateLabel(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		updateLabel(e);
	}

	private void updateLabel(DocumentEvent e) {
		String text = "";
		try {
			text = e.getDocument().getText(0, e.getDocument().getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		System.out.println(text);
		try {
			levelParameters.setNumOfWaypoints(Integer.parseInt(text));
		} catch (NumberFormatException e1) {
			levelParameters
					.setNumOfWaypoints(LevelParameterDefaults.numOfWaypoints);
		}
	}
}
