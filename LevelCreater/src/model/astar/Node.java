package model.astar;


//for A* Algorithm
public class Node {
		private Node parent;
		private int x, y;
		private float f, g, h;

		public Node(Node parent, float g, float h, int x, int y) {
			this.parent = parent;
			this.x = x; 
			this.y = y; 
			this.g = g; // Kosten Start->Node
			this.h = h; // gesch?tzte Kosten Node->Ziel
			this.f = g + h; // gesch?tzte Kosten Node
		}

		public Node(Node parent, int x, int y) {
			this.parent = parent;
			this.x = x;
			this.y = y;
		}
		public Node getParent() {
			return parent;
		}

		
		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public float getF() {
			return f;
		}

		public float getG() {
			return g;
		}

		public void setG(float g) {
			this.g = g;
		}

		public float getH() {
			return h;
		}

		public void setH(float h) {
			this.h = h;
		}

		public void setF(float f) {
			this.f = f;
		}
		
		


}
