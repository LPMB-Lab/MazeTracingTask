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
		GenerateTrial();
	}
	
	private void GenerateTrial()
	{
		for (int i = 0; i < 10; i++)
		{
			int yCoord = new Random().nextInt(100) + 1;
			
			m_aPointsArray[i].setCoords(m_iStepX, yCoord);
			m_iStepX += 10;
		}
	}
}
