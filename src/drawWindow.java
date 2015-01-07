import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class drawWindow extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static final int DIFFICULTY = 5;

	Dimension screenSize;
	RenderingHints rh;

	State m_State;
	boolean m_bIsPressed;

	Vector<Trial> m_vTrials = new Vector<Trial>();

	Button startButton;
	Button restartButton;
	Button quitButton;
	Button saveButton;

	int m_iCurrentTrial;

	long m_lStartTime;
	int m_iGlobalTimer;

	drawWindow() {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
		m_bIsPressed = false;
		m_iGlobalTimer = 0;
		m_iCurrentTrial = 0;

		m_vTrials.clear();

		for (int i = 0; i < 35; i++) {
			Trial myTrial = new Trial(DIFFICULTY);
			m_vTrials.add(myTrial);
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

		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();

		g2d.setStroke(new BasicStroke(10));

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
}
