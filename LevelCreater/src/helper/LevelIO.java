package helper;

import java.awt.Polygon;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import model.LOFloor;
import model.LOWall;
import model.LOWaypoint;
import model.LevelObject;

public class LevelIO {

	public static final String pathToLevelFiles = "levels\\";
	public static final String fileEnding = ".dat";
	public static final String xAndYSeparator = ":";
	public static final String pointsSeparator = " ";
	
	public void saveLevelToFile(List<LevelObject> levelObjects, String levelName) {
		String line = levelName;
		try {
			String adjustedLevelName = getAdjustedFileName(levelName);
			FileWriter fwriter = new FileWriter(adjustedLevelName);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write(line);
			out.write("\n");
			
			for (LevelObject levelObject : levelObjects) {
				out.write(getOutputStringByLevelObject(levelObject));
			}
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getOutputStringByLevelObject(LevelObject lo) {
		String output = "";
		if(lo instanceof LOWall) {
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
			output += poly.xpoints[i] + xAndYSeparator + poly.ypoints[i] + pointsSeparator;
		}
		output += "\n";
		return output;
	}

	private String getWaypointOutputString(LOWaypoint waypointObject) {
		String output = "[Waypoint]";
		output += waypointObject.getPosition().x + xAndYSeparator + waypointObject.getPosition().y;
		output += "\n";
		return output;
	}

	private String getWallOutputString(LOWall wallObject) {
		String output = "[Wall]";
		Polygon poly = wallObject.getPolygon();
		for (int i = 0; i < poly.npoints; i++) {
			output += poly.xpoints[i] + xAndYSeparator + poly.ypoints[i] + pointsSeparator;
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
		while(!foundNonExistingFile) {
			adjustedFileName = getFileNameWithPathAndEnding(fileName);
			file = new File(adjustedFileName);
			if(!file.exists() && !file.isFile()) {
				foundNonExistingFile = true;
			} else {
				fileName = fileName.substring(0, fileNameLength)+ "_" + fileExtension;
				fileExtension++;
			}
		}
		return adjustedFileName;
	}

	private String getFileNameWithPathAndEnding(String fileName) {
		return (pathToLevelFiles + fileName + fileEnding);
	}
}
