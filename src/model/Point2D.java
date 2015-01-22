package model;
public class Point2D {
	private int x;
	private int y;
	private boolean isValid;

	public Point2D() {
		this.x = 0;
		this.y = 0;
		this.isValid = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setValid(boolean flag) {
		this.isValid = flag;
	}
	
	public boolean isValid() {
		return this.isValid;
	}
	
	public void clearPoint() {
		setValid(false);
		this.x = 0;
		this.y = 0;
	}
}
