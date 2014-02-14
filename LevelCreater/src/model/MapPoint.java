package model;

public class MapPoint {
	public int x, y;

	public MapPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public void switchXandY() {
		int oldX = this.x;
		this.x = this.y;
		this.y = oldX;
	}
	
}
