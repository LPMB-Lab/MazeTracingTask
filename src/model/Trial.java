package model;
import java.util.Random;

import enums.DirectionType;
import enums.HandType;
import enums.VisionType;

public class Trial {
	private Point2D[] m_aPointsArray = new Point2D[10];
	private long m_lTimer;
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

		GenerateTrial();
	}

	private void GenerateTrial() {
		for (int i = 0; i < 10; i++) {
			int yLowerRange = 50 - 5 * m_iDifficulty;

			int yCoord = (new Random().nextInt(m_iDifficulty * 10))
					+ yLowerRange;

			m_aPointsArray[i].setCoords(m_iStepX, yCoord);
			m_iStepX += 10;
		}
	}

	public Point2D[] getPointsArray() {
		return m_aPointsArray;
	}
	
	public void setTimer(long timer) {
		m_lTimer = timer;
	}
	
	public long getTimer() {
		return m_lTimer;
	}

	public HandType getM_HandType() {
		return m_HandType;
	}

	public void setM_HandType(HandType m_HandType) {
		this.m_HandType = m_HandType;
	}

	public DirectionType getM_DirectionType() {
		return m_DirectionType;
	}

	public void setM_DirectionType(DirectionType m_DirectionType) {
		this.m_DirectionType = m_DirectionType;
	}

	public VisionType getM_VisionType() {
		return m_VisionType;
	}

	public void setM_VisionType(VisionType m_VisionType) {
		this.m_VisionType = m_VisionType;
	}
}