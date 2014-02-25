package helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import model.LevelObject;

public class LevelIO {

	public static final String pathToLevelFiles = "levels\\";
	public static final String fileEnding = ".dat";
	
	public void saveLevelToFile(List<LevelObject> levelObjects, String levelName) {
		String line = levelName;
		try {
			FileWriter fwriter = new FileWriter(pathToLevelFiles + levelName + fileEnding);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write(line);
			
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
