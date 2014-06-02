package controller.actionlistener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.level.LevelParameters;

public class ParameterListenerBase implements DocumentListener {

	protected LevelParameters levelParameters;

	public ParameterListenerBase(LevelParameters levelParameters) {
		this.levelParameters = levelParameters;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {

	}

	@Override
	public void removeUpdate(DocumentEvent e) {

	}

	@Override
	public void changedUpdate(DocumentEvent e) {

	}

}
