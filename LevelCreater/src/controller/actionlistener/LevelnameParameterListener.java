package controller.actionlistener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import model.level.LevelParameters;

public class LevelnameParameterListener extends ParameterListenerBase implements
		DocumentListener {

	public LevelnameParameterListener(LevelParameters levelParameters) {
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
		levelParameters.setLevelName(text);
		// java.awt.EventQueue.invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		// levelParameters.setLevelName(e.toString());
		// }
		// });
	}
}
