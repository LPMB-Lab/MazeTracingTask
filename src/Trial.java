import java.util.Random;

public class Trial
{
	private Point2D[] m_aPointsArray = new Point2D[10];
	private int m_iStepX;
	private int m_iDifficulty; 
	
	Trial(int difficulty)
	{
		m_iDifficulty = difficulty;
		m_iStepX = 5;
		
		for (int i = 0; i < m_aPointsArray.length; i++)
		{
			Point2D myPoint = new Point2D();
			m_aPointsArray[i] = myPoint;
		}
		
		GenerateTrial();
	}
	
	private void GenerateTrial()
	{
		for (int i = 0; i < 10; i++)
		{
			int yLowerRange = 50 - 5*m_iDifficulty;
			
			int yCoord = (new Random().nextInt(m_iDifficulty*10)) + yLowerRange;
			
			m_aPointsArray[i].setCoords(m_iStepX, yCoord);
			m_iStepX += 10;
		}
	}
	
	public Point2D[] getPointsArray() {return m_aPointsArray;}
}