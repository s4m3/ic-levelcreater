package actionlisteners;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import main.LevelCreater;
import model.Level;
import model.LevelParameters;
import view.LevelFrame;

public class CreateButtonListener implements ActionListener, PropertyChangeListener{

	private LevelParameters levelParameters;

	public Level level;
	
	public CreateButtonListener(LevelParameters source) {
		super();
		this.levelParameters = source;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LevelCreater lc = LevelCreater.getInstance();
		lc.createButton.setEnabled(false);
		lc.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		lc.outputTextField.setText("Creating Level...\n");
		level = new Level(levelParameters);
		level.addPropertyChangeListener(this);
		level.execute();
		LevelFrame levelFrame = new LevelFrame(level);
		// level.pack();
		levelFrame.setVisible(true);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            LevelCreater lc = LevelCreater.getInstance();
            lc.progressBar.setValue(progress);
            if(level.getStatusUpdates().size() > 0)
            	lc.outputTextField.append(level.getStatusUpdates().get(level.getStatusUpdates().size()-1) + "\n");
        } 
		
	}

}
