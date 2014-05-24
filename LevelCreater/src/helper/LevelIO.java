package helper;

import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.level.LevelParameters;
import model.level.objects.LOFloor;
import model.level.objects.LOSlowDown;
import model.level.objects.LOSpeedUp;
import model.level.objects.LOWall;
import model.level.objects.LOWaypoint;
import model.level.objects.LevelObject;
import controller.LevelController;

public class LevelIO {

	public static final String LEVEL_FILE_DIRECTORY = "levels";
	public static final String FILE_ENDING = ".dat";
	public static final String X_AND_Y_SEPARATOR = ":";
	public static final String POINTS_SEPARATOR = " ";
	public static final String POINT_AND_SIZE_SEPARATOR = "SIZE";
	public static final String SIZE_SEPARATOR = ";";
	public static final String LINEWIDTH_SEPARATOR = "LINEWIDTH";

	public static final String LEVEL_WIDTH_MARKER = "[WIDTH]";
	public static final String LEVEL_HEIGHT_MARKER = "[HEIGHT]";

	public static final String FLOOR_MARKER = "[Floor]";
	public static final String WALL_MARKER = "[Wall]";
	public static final String WP_MARKER = "[Waypoint]";
	public static final String SD_MARKER = "[SlowDown]";
	public static final String SU_MARKER = "[SpeedUp]";

	public String levelName = "Default";
	public int levelWidth = 0;
	public int levelHeight = 0;

	public List<LevelObject> loadLevelFromFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.showOpenDialog(null);
		FileReader freader = null;
		BufferedReader reader = null;
		if (chooser.getSelectedFile() == null) {
			return null;
		}
		try {
			freader = new FileReader(chooser.getSelectedFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (freader != null) {
			reader = new BufferedReader(freader);
			List<LevelObject> levelObjs = new ArrayList<LevelObject>();
			readInLevelObjects(reader, levelObjs);
			try {
				freader.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return levelObjs;
		}

		return null;
	}

	private void readInLevelObjects(BufferedReader reader, List<LevelObject> levelObjs) {

		String line = "";

		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(FLOOR_MARKER)) {
					levelObjs.add(buildFloorFromString(line));
				} else if (line.startsWith(WALL_MARKER)) {
					levelObjs.add(buildWallFromString(line));
				} else if (line.startsWith(WP_MARKER)) {
					levelObjs.add(buildWaypointFromString(line));
				} else if (line.startsWith(SD_MARKER)) {
					levelObjs.add(buildSlowDownFromString(line));
				} else if (line.startsWith(SU_MARKER)) {
					levelObjs.add(buildSpeedUpFromString(line));
				} else if (line.startsWith(LEVEL_WIDTH_MARKER)) {
					this.levelWidth = Integer.parseInt(line.substring(LEVEL_WIDTH_MARKER.length()));
				} else if (line.startsWith(LEVEL_HEIGHT_MARKER)) {
					this.levelHeight = Integer.parseInt(line.substring(LEVEL_HEIGHT_MARKER.length()));
				} else {
					this.levelName = line;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private LevelObject buildSpeedUpFromString(String line) {
		String[] subStringArray = line.split(LINEWIDTH_SEPARATOR);
		if (subStringArray.length != 2)
			return null;

		LOSpeedUp speedUp = new LOSpeedUp(buildPolygonFromString(subStringArray[0].substring(SU_MARKER.length())));
		speedUp.setLineWidth(Integer.parseInt(subStringArray[1]));
		return speedUp;
	}

	private LevelObject buildSlowDownFromString(String line) {
		LOSlowDown slowDown = new LOSlowDown(buildPolygonFromString(line.substring(SD_MARKER.length())));
		return slowDown;
	}

	private LevelObject buildWaypointFromString(String line) {
		String substring = line.substring(WP_MARKER.length());
		String[] wpSubStringArray = substring.split(POINT_AND_SIZE_SEPARATOR);
		if (wpSubStringArray.length != 2)
			return null;

		String[] wpSizeSubStringArray = wpSubStringArray[1].split(SIZE_SEPARATOR);
		if (wpSizeSubStringArray.length != 2)
			return null;

		String[] wpPoints = wpSubStringArray[0].split(X_AND_Y_SEPARATOR);
		LOWaypoint waypoint = new LOWaypoint(Integer.parseInt(wpPoints[0]), Integer.parseInt(wpPoints[1]),
				(int) Double.parseDouble(wpSizeSubStringArray[0]), (int) Double.parseDouble(wpSizeSubStringArray[1]));

		return waypoint;
	}

	private LevelObject buildWallFromString(String line) {
		LOWall wall = new LOWall(buildPolygonFromString(line.substring(WALL_MARKER.length())));
		return wall;
	}

	private LevelObject buildFloorFromString(String line) {
		LOFloor floor = new LOFloor(buildPolygonFromString(line.substring(FLOOR_MARKER.length())));
		return floor;
	}

	private Polygon buildPolygonFromString(String substring) {
		String[] pointsStringArray = substring.split(POINTS_SEPARATOR);
		Polygon poly = new Polygon();
		for (int i = 0; i < pointsStringArray.length; i++) {
			String[] singlePointStringArr = pointsStringArray[i].split(X_AND_Y_SEPARATOR);
			if (singlePointStringArr != null && singlePointStringArr.length == 2) {
				poly.addPoint(Integer.parseInt(singlePointStringArr[0]), Integer.parseInt(singlePointStringArr[1]));
			}
		}
		return poly;
	}

	public void saveLevelToFile(LevelController levelController) {
		LevelParameters parameters = levelController.getLevelParameters();
		List<LevelObject> levelObjects = levelController.getLevel().getLevelObjects();
		String line = parameters.getLevelName();
		String levelWidth = parameters.getLevelWidth() + "";
		String levelHeight = parameters.getLevelHeight() + "";
		FileWriter fwriter = null;
		String adjustedLevelName = getAdjustedFileName(line);
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

			out.write(LEVEL_WIDTH_MARKER + levelWidth);
			out.write("\n");

			out.write(LEVEL_HEIGHT_MARKER + levelHeight);
			out.write("\n");

			for (LevelObject levelObject : levelObjects) {
				out.write(getOutputStringByLevelObject(levelObject));
			}
			out.close();

			JOptionPane.showMessageDialog(null, String.format("%s saved at %s.", levelName, adjustedLevelName));

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
		} else if (lo instanceof LOSlowDown) {
			output = getSlowDownOutputString((LOSlowDown) lo);
		} else if (lo instanceof LOSpeedUp) {
			output = getSpeedUpOutputString((LOSpeedUp) lo);
		}
		return output;
	}

	private String getFloorOutputString(LOFloor floorObject) {
		String output = FLOOR_MARKER;
		Polygon poly = floorObject.getPolygon();
		for (int i = 0; i < poly.npoints; i++) {
			output += poly.xpoints[i] + X_AND_Y_SEPARATOR + poly.ypoints[i] + POINTS_SEPARATOR;
		}
		output += "\n";
		return output;
	}

	private String getWaypointOutputString(LOWaypoint waypointObject) {
		String output = WP_MARKER;
		output += waypointObject.getPosition().x + X_AND_Y_SEPARATOR + waypointObject.getPosition().y + POINT_AND_SIZE_SEPARATOR
				+ waypointObject.getEllipse().width + SIZE_SEPARATOR + waypointObject.getEllipse().height;
		output += "\n";
		return output;
	}

	private String getWallOutputString(LOWall wallObject) {
		String output = WALL_MARKER;
		Polygon poly = wallObject.getPolygon();
		for (int i = 0; i < poly.npoints; i++) {
			output += poly.xpoints[i] + X_AND_Y_SEPARATOR + poly.ypoints[i] + POINTS_SEPARATOR;
		}
		output += "\n";
		return output;
	}

	private String getSpeedUpOutputString(LOSpeedUp speedUpObject) {
		String output = SU_MARKER;
		Polygon poly = speedUpObject.getPolygon();
		for (int i = 0; i < poly.npoints; i++) {
			output += poly.xpoints[i] + X_AND_Y_SEPARATOR + poly.ypoints[i] + POINTS_SEPARATOR;
		}

		output += LINEWIDTH_SEPARATOR + speedUpObject.getLineWidth();
		output += "\n";
		return output;
	}

	private String getSlowDownOutputString(LOSlowDown slowDownObject) {
		String output = SD_MARKER;
		Polygon poly = slowDownObject.getPolygon();
		for (int i = 0; i < poly.npoints; i++) {
			output += poly.xpoints[i] + X_AND_Y_SEPARATOR + poly.ypoints[i] + POINTS_SEPARATOR;
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
				fileName = fileName.substring(0, fileNameLength) + "_" + fileExtension;
				fileExtension++;
			}
		}
		return adjustedFileName;
	}

	private String getFileNameWithPathAndEnding(String fileName) {
		return (LEVEL_FILE_DIRECTORY + File.separator + fileName + FILE_ENDING);
	}
}
