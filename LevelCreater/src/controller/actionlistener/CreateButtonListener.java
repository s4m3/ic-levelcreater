package controller.actionlistener;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.LevelCreater;
import model.LevelParameterDefaults;
import model.LevelParameters;
import view.LevelFrame;
import view.LevelsContainer;
import controller.LevelController;

public class CreateButtonListener implements ActionListener, PropertyChangeListener{

	public LevelController level;

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!allParametersOk()) {
			JOptionPane.showMessageDialog(null, getParameterMessage());
			return;
		}

		LevelCreater lc = LevelCreater.getInstance();
		lc.createButton.setEnabled(false);
		lc.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		lc.outputTextField.setText("Creating Level...\n");
		level = new LevelController(lc.getLevelParameters());
		level.addPropertyChangeListener(this);
		level.execute();

	}
	
	private String getParameterMessage() {
		LevelParameters paras = LevelCreater.getInstance().getLevelParameters();
		String text = "Invalid Parameters! \n";
		text += "Level width must be between" +LevelParameterDefaults.LEVEL_WIDTH_MIN+" and " +LevelParameterDefaults.LEVEL_WIDTH_MAX+ ". (Currently:" +paras.getLevelWidth()+")\n";
		text += "Level height must be between" +LevelParameterDefaults.LEVEL_HEIGHT_MIN+" and " +LevelParameterDefaults.LEVEL_HEIGHT_MAX+ ". (Currently:" +paras.getLevelHeight()+")\n";
		text += "Amount of waypoints must be between" +LevelParameterDefaults.NUM_WAYPOINTS_MIN+" and " +LevelParameterDefaults.NUM_WAYPOINTS_MAX+ ". (Currently:" +paras.getNumOfWaypoints()+")\n";
		return text;
	}

	private boolean allParametersOk() {
		boolean parametersOk = true;
		LevelParameters paras = LevelCreater.getInstance().getLevelParameters();
		
		int parameter = paras.getLevelHeight();
		if(parameter < LevelParameterDefaults.LEVEL_HEIGHT_MIN || parameter > LevelParameterDefaults.LEVEL_HEIGHT_MAX)
			parametersOk = false;
		
		parameter = paras.getLevelWidth();
		if(parameter < LevelParameterDefaults.LEVEL_WIDTH_MIN || parameter > LevelParameterDefaults.LEVEL_WIDTH_MAX)
			parametersOk = false;
		
		parameter = paras.getNumOfWaypoints();
		if(parameter < LevelParameterDefaults.NUM_WAYPOINTS_MIN || parameter > LevelParameterDefaults.NUM_WAYPOINTS_MAX)
			parametersOk = false;
		
		parameter = paras.getScale();
		if(parameter < LevelParameterDefaults.SCALE_MIN || parameter > LevelParameterDefaults.SCALE_MAX)
			parametersOk = false;
		
		return parametersOk;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            LevelCreater lc = LevelCreater.getInstance();
            lc.progressBar.setValue(progress);
            while(level.getStatusUpdates().size() > 0) {
            	lc.outputTextField.append(level.getStatusUpdates().get(0) + "\n");
            	level.getStatusUpdates().remove(0);
            }
            
            if(progress >= 100) {
            	JFrame levelFrame = new JFrame();
            	levelFrame.setTitle(level.getLevelParameters().getLevelName());
            	levelFrame.setSize(new Dimension(800, 600));
//        		this.setPreferredSize(new Dimension(800, 600));
            	levelFrame.setLocation(400, 100);
            	
        		// MENU
        		JMenuBar menuBar = new JMenuBar();
        		JMenu menu = new JMenu("Level");
        		JMenuItem save = new JMenuItem("Save");
        		save.addActionListener(new SaveLevelMenuItemListener(level));
        		menu.add(save);
        		menuBar.add(menu);

        		levelFrame.setJMenuBar(menuBar);

            	levelFrame.add(new LevelsContainer(level), 0);
            	levelFrame.setVisible(true);

            }
        } 
		
	}

}
