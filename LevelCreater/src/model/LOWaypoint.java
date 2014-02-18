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
				ySplit = totalNumOfWayPoints;
				xStart = 0;
				xEnd = levelWidth;
				yStart = (currentWayPointNum - 1) * (levelHeight / ySplit);
				yEnd = currentWayPointNum * (levelHeight / ySplit);
			} else {
				xSplit = totalNumOfWayPoints;
				xStart = (currentWayPointNum - 1) * (levelWidth / xSplit);
				xEnd = currentWayPointNum * (levelWidth / xSplit);
				yStart = 0;
				yEnd = levelWidth;
			}
		} else {
			if(squareLevel) {
				xSplit = totalNumOfWayPoints / 2;
				ySplit = totalNumOfWayPoints / 2;
			} else {
				if(verticalSplittingFavored) {
					xSplit = totalNumOfWayPoints / 2;
					ySplit = totalNumOfWayPoints / 2 + (totalNumOfWayPoints % 2);
				} else {
					xSplit = totalNumOfWayPoints / 2 + (totalNumOfWayPoints % 2);
					ySplit = totalNumOfWayPoints / 2;
				}
			}
			//TODO: this is wrong DEBUG!!!! i.e. 4 waypoints, ab wp 2> x=50 y=100
			xStart = (currentWayPointNum / xSplit) * (levelWidth / xSplit);
			xEnd = (currentWayPointNum / xSplit + 1) * (levelWidth / xSplit);
			yStart = (currentWayPointNum / ySplit) * (levelHeight / ySplit);
			yEnd = (currentWayPointNum / ySplit + 1) * (levelHeight / ySplit);
		}
		
		//use a random position in the selected sector
		int xPosition = xStart + Randomizer.random.nextInt(xEnd - xStart);
		int yPosition = yStart + Randomizer.random.nextInt(yEnd - yStart);
		Point p = new Point(xPosition, yPosition);

		return p;
	}
}
