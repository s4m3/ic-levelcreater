package controller.actionlistener;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import model.level.LevelParameterDefaults;
import model.level.LevelParameters;

public class LevelwidthParameterListener extends ParameterListenerBase
		implements DocumentListener {

	public LevelwidthParameterListener(LevelParameters levelParameters) {
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
				levelParameters.setLevelWidth(Integer.parseInt(text));
			else
				levelParameters.setLevelWidth(0);
		} catch (NumberFormatException e1) {
			levelParameters.setLevelWidth(LevelParameterDefaults.LEVEL_WIDTH);
			JOptionPane.showMessageDialog(null, "Invalid input. Default is set ("+LevelParameterDefaults.LEVEL_WIDTH+")");
		}
	}
}
