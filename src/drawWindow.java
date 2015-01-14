import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Button;
import model.Point2D;
import model.Trial;
import enums.DirectionType;
import enums.HandType;
import enums.State;
import enums.VisionType;

public class drawWindow extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private static final int DIFFICULTY = 5;
	private static final int STROKE_WIDTH = 40;

	int screenWidth;
	int screenHeight;
	RenderingHints rh;

	State m_State;
	DirectionType m_DirectionType;
	HandType m_HandType;
	VisionType m_VisionType;
	int m_TypeCount;
	
	boolean m_bIsPressed;
	
	long m_lStartTime;
	int m_iCurrentTrial;
	int m_iGlobalTimer;

	Vector<Trial> m_vTrials = new Vector<Trial>();

	Button startButton;
	Button restartButton;
	Button quitButton;
	Button saveButton;

	drawWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
		setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
		addMouseListener(this);

		try {
			startButton = new Button(ImageIO.read(getClass().getResource(
					"images/startButton.png")), 5, 5);
			restartButton = new Button(ImageIO.read(getClass().getResource(
					"images/restartButton.png")), 100, 5);
			quitButton = new Button(ImageIO.read(getClass().getResource(
					"images/quitButton.png")), 195, 5);
			saveButton = new Button(ImageIO.read(getClass().getResource(
					"images/saveButton.png")), 290, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Reset();
	}

	void Reset() {
		m_State = State.IDLE;
		m_DirectionType = DirectionType.first();
		m_HandType = HandType.first();
		m_VisionType = VisionType.first();
		m_TypeCount = 0;
		
		m_bIsPressed = false;
		m_iGlobalTimer = 0;
		m_iCurrentTrial = 0;

		m_vTrials.clear();

		for (int i = 0; i < 1; i++) {
			Trial myTrial = new Trial(DIFFICULTY);
			m_vTrials.add(myTrial);
		}	
		
		for (DirectionType direction : DirectionType.values()) {
			for (HandType hand : HandType.values()) {
				for (VisionType vision : VisionType.values()) {
					System.out.println("Trial: " + direction.name() + ", "
												+ hand.name() + ", " 
												+ vision.name());
				}
			}
		}
	}

	private void UpdateGraphics() {
		Graphics g;
		g = getGraphics();
		paint(g);
	}

	void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);

		switch (m_State) {
		case IDLE:
			g2d.drawImage(startButton.getImage(), startButton.getX(),
					startButton.getY(), null);
			break;
		case COMPLETED:
			break;
		case IN_TRIAL:
			break;
		default:
			break;
		}

		g2d.drawImage(restartButton.getImage(), restartButton.getX(),
				restartButton.getY(), null);
		g2d.drawImage(quitButton.getImage(), quitButton.getX(),
				quitButton.getY(), null);
		g2d.drawImage(saveButton.getImage(), saveButton.getX(),
				saveButton.getY(), null);
		
		Font helvetica = new Font ("Helvetica", Font.BOLD, 18);
		g2d.setFont(helvetica);
		g2d.drawString(m_DirectionType.name(), 5, 75);
		g2d.drawString(m_VisionType.name(), 5, 95);
		g2d.drawString(m_HandType.name(), 5, 115);

		g2d.setStroke(new BasicStroke(STROKE_WIDTH));

		for (int i = 0; i < m_vTrials.size(); i++) {
			Trial myTrial = m_vTrials.get(i);
			Point2D[] m_aPointsArray = myTrial.getPointsArray();

			for (int j = 0; j < m_aPointsArray.length - 1; j++)
				g2d.drawLine(m_aPointsArray[j].getX() * screenWidth / 100,
						m_aPointsArray[j].getY() * screenHeight / 100,
						m_aPointsArray[j + 1].getX() * screenWidth / 100,
						m_aPointsArray[j + 1].getY() * screenHeight / 100);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (startButton.isPressed(x, y)) {
			StartSimulation();
		} else if (quitButton.isPressed(x, y)) {
			System.exit(0);
		} else if (restartButton.isPressed(x, y)) {
			Reset();
			UpdateGraphics();
		} else if (saveButton.isPressed(x, y)) {
			ExportFile();
		}
		
		int xStart = m_vTrials.get(m_iCurrentTrial).getStartX()*screenWidth / 100;
		int yStart = m_vTrials.get(m_iCurrentTrial).getStartY()*screenHeight / 100;
		
		if (Math.sqrt(Math.pow((x-xStart), 2) + Math.pow((y-yStart), 2)) < STROKE_WIDTH) {
			if (m_State == State.IDLE) {
				m_State = State.IN_TRIAL;
			}
		}
	}

	private void StartSimulation() {
	}

	private void ExportFile() {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		m_bIsPressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		m_bIsPressed = false; 
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (m_bIsPressed) {
			int x = e.getX();
			int y = e.getY();
			
			// Do Math check
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
}
