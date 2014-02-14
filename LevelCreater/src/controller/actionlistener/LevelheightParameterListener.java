package controller.actionlistener;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import model.LevelParameterDefaults;
import model.LevelParameters;

public class LevelheightParameterListener extends ParameterListenerBase
		implements DocumentListener {

	public LevelheightParameterListener(LevelParameters levelParameters) {
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
		try {
			if(text.length() > 0)
				levelParameters.setLevelHeight(Integer.parseInt(text));
			else 
				levelParameters.setLevelHeight(0);
		} catch (NumberFormatException e1) {
			levelParameters.setLevelHeight(LevelParameterDefaults.LEVEL_HEIGHT);
			JOptionPane.showMessageDialog(null, "Invalid input. Default is set ("+LevelParameterDefaults.LEVEL_HEIGHT+")");

		}
	}
}
