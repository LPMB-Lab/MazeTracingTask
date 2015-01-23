package model;
import java.util.Random;

import enums.DirectionType;
import enums.HandType;
import enums.VisionType;

public class Trial {
	private final int ARRAY_SIZE = 10;
	private Point2D[] m_aPointsArray = new Point2D[ARRAY_SIZE];
	private long m_lTimer = 0;
	private long m_lErrorTimer = 0;
	private int m_iStepX;
	private int m_iDifficulty;
	private HandType m_HandType;
	private DirectionType m_DirectionType;
	private VisionType m_VisionType;
	

	public Trial(int difficulty) {
		m_iDifficulty = difficulty;
		m_iStepX = 5;

		for (int i = 0; i < m_aPointsArray.length; i++) {
			Point2D myPoint = new Point2D();
			m_aPointsArray[i] = myPoint;
		}
		
		for (int i = 0; i < ARRAY_SIZE; i++) {
			int yLowerRange = 50 - 5 * m_iDifficulty;

			int yCoord = (new Random().nextInt(m_iDifficulty * 10))
					+ yLowerRange;

			m_aPointsArray[i].setCoords(m_iStepX, yCoord);
			m_iStepX += 10;
		}
	}
	
	public String ExportTrial() {
		
		String exportString = "";
		String endl = "\r\n";
		if (m_VisionType != null &&
				m_DirectionType != null &&
				m_HandType != null) {
			exportString += "VisionType:\t" + m_VisionType.name() + endl;
			exportString += "DirectionType:\t" + m_DirectionType.name() + endl;
			exportString += "HandType:\t" + m_HandType.name() + endl;
			exportString += "Timing (ns):\t" + m_lTimer + endl;
			exportString += "Error (ns):\t" + m_lErrorTimer + endl;
		} else {
			exportString += "INCOMPLETE TRIAL";
			
			for (int i = 0; i < 4; i++) {
				exportString += endl;
			}
		}
		
		return exportString;
	}
	
	public int getSize() {
		return ARRAY_SIZE;
	}

	public Point2D[] getPointsArray() {
		return m_aPointsArray;
	}
	
	public Point2D getPoint(int index) {
		return m_aPointsArray[index];
	}
	
	public int getStartX() {
		return m_aPointsArray[0].getX();
	}
	
	public int getStartY() {
		return m_aPointsArray[0].getY();
	}
	
	public int getEndX() {
		return m_aPointsArray[ARRAY_SIZE-1].getX();
	}
	
	public int getEndY() {
		return m_aPointsArray[ARRAY_SIZE-1].getY();
	}
	
	public void setTimer(long timer) {
		m_lTimer = timer;
	}
	
	public long getTimer() {
		return m_lTimer;
	}
	
	public long getErrorTimer() {
		return m_lErrorTimer;
	}

	public void addErrorTimer(long timer) {
		m_lErrorTimer = timer;
	}

	public HandType getHandType() {
		return m_HandType;
	}

	public void setHandType(HandType type) {
		m_HandType = type;
	}

	public DirectionType getDirectionType() {
		return m_DirectionType;
	}

	public void setDirectionType(DirectionType type) {
		m_DirectionType = type;
	}

	public VisionType getVisionType() {
		return m_VisionType;
	}

	public void setVisionType(VisionType type) {
		m_VisionType = type;
	}
}