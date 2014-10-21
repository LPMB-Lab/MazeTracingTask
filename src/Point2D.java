public class Point2D
{
	private int x;
	private int y;
	
	Point2D()
	{
		this.x = 0;
		this.y = 0;
	}

	public int getX() {return x;}
	public int getY() {return y;}
	public void setCoords(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
