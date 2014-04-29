package helper;

import java.awt.Polygon;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.LOFloor;
import model.LOWall;
import model.LOWaypoint;
import model.LevelObject;

public class LevelIO {

	public static final String LEVEL_FILE_DIRECTORY = "levels";
	public static final String FILE_ENDING = ".dat";
	public static final String X_AND_Y_SEPARATOR = ":";
	public static final String POINTS_SEPARATOR = " ";

	public void saveLevelToFile(List<LevelObject> levelObjects, String levelName) {
		String line = levelName;
		FileWriter fwriter = null;
		String adjustedLevelName = getAdjustedFileName(levelName);
		try {
			fwriter = new FileWriter(adjustedLevelName);
		} catch (IOException fnfe) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.showOpenDialog(null);
			try {
				fwriter = new FileWriter(chooser.getSelectedFile());
				adjustedLevelName = chooser.getSelectedFile().getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {

			BufferedWriter out = new BufferedWriter(fwriter);

			out.write(line);
			out.write("\n");

			for (LevelObject levelObject : levelObjects) {
				out.write(getOutputStringByLevelObject(levelObject));
			}
			out.close();

			JOptionPane.showMessageDialog(null, String.format(
					"%s saved at %s.", levelName, adjustedLevelName));

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private String getOutputStringByLevelObject(LevelObject lo) {
		String output = "";
		if (lo instanceof LOWall) {
			output = getWallOutputString((LOWall) lo);
		} else if (lo instanceof LOWaypoint) {
			output = getWaypointOutputString((LOWaypoint) lo);
		} else if (lo instanceof LOFloor) {
			output = getFloorOutputString((LOFloor) lo);
		}
		return output;
	}

	private String getFloorOutputString(LOFloor floorObject) {
		String output = "[Floor]";
		Polygon poly = floorObject.getPolygon();
		for (int i = 0; i < poly.npoints; i++) {
			output += poly.xpoints[i] + X_AND_Y_SEPARATOR + poly.ypoints[i]
					+ POINTS_SEPARATOR;
		}
		output += "\n";
		return output;
	}

	private String getWaypointOutputString(LOWaypoint waypointObject) {
		String output = "[Waypoint]";
		output += waypointObject.getPosition().x + X_AND_Y_SEPARATOR
				+ waypointObject.getPosition().y;
		output += "\n";
		return output;
	}

	private String getWallOutputString(LOWall wallObject) {
		String output = "[Wall]";
		Polygon poly = wallObject.getPolygon();
		for (int i = 0; i < poly.npoints; i++) {
			output += poly.xpoints[i] + X_AND_Y_SEPARATOR + poly.ypoints[i]
					+ POINTS_SEPARATOR;
		}
		output += "\n";
		return output;
	}

	private String getAdjustedFileName(String fileName) {
		int fileNameLength = fileName.length();
		String adjustedFileName = "";
		int fileExtension = 0;
		boolean foundNonExistingFile = false;
		File file;
		while (!foundNonExistingFile) {
			adjustedFileName = getFileNameWithPathAndEnding(fileName);
			file = new File(adjustedFileName);
			if (!file.exists() && !file.isFile()) {
				foundNonExistingFile = true;
			} else {
				fileName = fileName.substring(0, fileNameLength) + "_"
						+ fileExtension;
				fileExtension++;
			}
		}
		return adjustedFileName;
	}

	private String getFileNameWithPathAndEnding(String fileName) {
		return (LEVEL_FILE_DIRECTORY + File.separator + fileName + FILE_ENDING);
	}
}
