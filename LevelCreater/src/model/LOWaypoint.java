package model;

import helper.Randomizer;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class LOWaypoint extends LOCircle {

	public LOWaypoint() {
		this.objectColor = Color.YELLOW;
	}

	public LOWaypoint(boolean randomPosition, int totalNumOfWayPoints, int currentWayPointNum, double xSize, double ySize,
			int levelWidth, int levelHeight) {
		this.objectColor = Color.YELLOW;
//		this.position = new Point((int) (Math.random() * (levelWidth - 1)),
//				(int) (Math.random() * (levelHeight - 1)));
		this.position = getEvenlyDistributedWayPoint(totalNumOfWayPoints, currentWayPointNum, levelWidth, levelHeight);
		this.ellipse = new Ellipse2D.Double(this.position.x, this.position.y,
				xSize, ySize);
	}
	
	private Point getEvenlyDistributedWayPoint(int totalNumOfWayPoints, int currentWayPointNum,
			int levelWidth, int levelHeight) {
		int xStart, xEnd, yStart, yEnd;
		int xSplit; 
		int ySplit;
		int faktor = 0;
		//split level into sectors depending on total number of waypoints as follows
		//-----
		//|1|2|
		//-----
		//|3|4|
		//-----
		//|5|6|
		//-----
		//depending on width and length of level, favor vertical or horizontal splitting
		boolean verticalSplittingFavored = levelHeight > levelWidth;
		boolean squareLevel = levelHeight == levelWidth;
		//if num of waypoints less then 4, only split in one direction
		boolean oneDirectionSplitting = totalNumOfWayPoints < 4;

		//select sector by current waypoint number
		if(oneDirectionSplitting) {
			if(verticalSplittingFavored) {
				xStart = 0;
				xEnd = levelWidth;
				yStart = currentWayPointNum * (levelHeight / totalNumOfWayPoints);
				yEnd = (currentWayPointNum + 1) * (levelHeight / totalNumOfWayPoints);
			} else {
				xStart = currentWayPointNum * (levelWidth / totalNumOfWayPoints);
				xEnd = (currentWayPointNum + 1) * (levelWidth / totalNumOfWayPoints);
				yStart = 0;
				yEnd = levelWidth;
			}
		} else {
			if(squareLevel) {
				xSplit = totalNumOfWayPoints / 2;
				ySplit = totalNumOfWayPoints / 2;
			} else {
				if(verticalSplittingFavored) {
					ySplit = totalNumOfWayPoints / 2;
					xSplit = totalNumOfWayPoints / ySplit;
					ySplit += totalNumOfWayPoints % 2 == 1 ? 1 : 0;
					faktor = xSplit - ySplit;
				} else {
					xSplit = totalNumOfWayPoints / 2;
					ySplit = totalNumOfWayPoints / xSplit;
					xSplit += totalNumOfWayPoints % 2 == 1 ? 1 : 0;
					faktor = xSplit - ySplit;
				}
			}
			
			xStart = (currentWayPointNum % xSplit) * (levelWidth / xSplit);
			xEnd = xStart + (levelWidth / xSplit);
			yStart = (currentWayPointNum / (ySplit + faktor)) * (levelHeight / ySplit);
			yEnd = yStart + (levelHeight / ySplit);

		}
		
		//use a random position in the selected sector
		int xPosition = xStart + Randomizer.random.nextInt(xEnd - xStart);
		int yPosition = yStart + Randomizer.random.nextInt(yEnd - yStart);
		Point p = new Point(xPosition, yPosition);

		return p;
	}
}
