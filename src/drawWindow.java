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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	int m_iCurrentTrial;
	
	int m_iStartErrorTime;
	int m_iEndErrorTime;
	int m_iErrorTimer;
	int m_iTrialTimer;

	Vector<Trial> m_vTrials = new Vector<Trial>();

	Button startButton;
	Button restartButton;
	Button quitButton;
	Button saveButton;

	drawWindow() {
		addMouseListener(this);
		addMouseMotionListener(this);

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
		screenWidth = (int) getWidth();
		screenHeight = (int) getHeight();
		
		m_State = State.IDLE;
		m_DirectionType = DirectionType.first();
		m_HandType = HandType.first();
		m_VisionType = VisionType.first();
		m_TypeCount = 0;
		
		m_iCurrentTrial = 0;
		
		m_iStartErrorTime = 0;
		m_iEndErrorTime = 0;
		m_iErrorTimer = 0;
		m_iTrialTimer = 0;

		m_vTrials.clear();

		for (int i = 0; i < 35; i++) {
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
	
	private void RestartTrial() {
		m_vTrials.remove(m_iCurrentTrial);
		m_vTrials.add(m_iCurrentTrial, new Trial(DIFFICULTY));
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
			break;
		case COMPLETE:
			break;
		case IN_TRIAL:
			break;
		default:
			break;
		}

		g2d.drawImage(startButton.getImage(), startButton.getX(),
				startButton.getY(), null);
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
		g2d.drawString(m_State == State.FAIL ? "TRIAL FAILED" : "", 5, 135);
		g2d.drawString(m_State == State.IN_TRIAL ? "TRIAL PROGRESS" : "", 5, 155);
		g2d.drawString(m_State == State.IDLE ? "TRIAL IDLE" : "", 5, 175);

		g2d.setStroke(new BasicStroke(STROKE_WIDTH));

		Point2D[] m_aPointsArray = m_vTrials.get(m_iCurrentTrial).getPointsArray();

		for (int j = 0; j < m_aPointsArray.length - 1; j++)
			g2d.drawLine(m_aPointsArray[j].getX() * screenWidth / 100,
					m_aPointsArray[j].getY() * screenHeight / 100,
					m_aPointsArray[j + 1].getX() * screenWidth / 100,
					m_aPointsArray[j + 1].getY() * screenHeight / 100);
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
			Reset();
			UpdateGraphics();
		} else if (quitButton.isPressed(x, y)) {
			System.exit(0);
		} else if (restartButton.isPressed(x, y)) {
			m_State = State.IDLE;
			RestartTrial();
			UpdateGraphics();
		} else if (saveButton.isPressed(x, y)) {
			ExportFile();
		}
	}

	private void ExportFile() {
		JTextField fileNameInput = new JTextField();
		String CompletionString = "Please enter File Name";

		if (m_State != State.COMPLETE)
			CompletionString += " (Trial is Unfinished)";

		final JComponent[] inputs = new JComponent[] {
				new JLabel(CompletionString), fileNameInput };
		int dialogResult = JOptionPane.showConfirmDialog(null, inputs,
				"Save File", JOptionPane.OK_CANCEL_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION) {
			try {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy_MM_dd HH_mm_ss");
				Date date = new Date();
				String fileName = "";

				if (fileNameInput.getText().equals(""))
					fileName = dateFormat.format(date) + "_NON_NAMED_TRIAL"
							+ ".xls";
				else
					fileName = dateFormat.format(date) + "_"
							+ fileNameInput.getText() + ".xls";

				PrintWriter writer = new PrintWriter(fileName, "US-ASCII");
				String exportString = "";
				
				for (int i = 0; i < m_vTrials.size(); i++) {
					exportString += "TRIAL #" + (i + 1) + "\r\n";
					exportString += m_vTrials.get(i).ExportTrial();
				}

				writer.println(exportString);
				writer.close();

				JOptionPane.showMessageDialog(null, "Save Successful!",
						"Save Success", JOptionPane.PLAIN_MESSAGE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		int xStart = m_vTrials.get(m_iCurrentTrial).getStartX()*screenWidth / 100;
		int yStart = m_vTrials.get(m_iCurrentTrial).getStartY()*screenHeight / 100;
		
		if (Math.sqrt(Math.pow((x-xStart), 2) + Math.pow((y-yStart), 2)) < STROKE_WIDTH) {
			if (m_State == State.IDLE) {
				m_State = State.IN_TRIAL;
			}
		}
		
		UpdateGraphics();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (m_State == State.IN_TRIAL) {
			m_State = State.FAIL;
			System.out.println("FAIL");
			UpdateGraphics();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		int xEnd = m_vTrials.get(m_iCurrentTrial).getEndX()*screenWidth / 100;
		int yEnd = m_vTrials.get(m_iCurrentTrial).getEndY()*screenHeight / 100;
		
		if (Math.sqrt(Math.pow((x-xEnd), 2) + Math.pow((y-yEnd), 2)) < STROKE_WIDTH) {
			if (m_State == State.IN_TRIAL) {
				if (m_iCurrentTrial == 35) {
					m_State = State.COMPLETE;
				} else {
					m_State = State.IDLE;
					m_iCurrentTrial++;
				}
			}
		}
		
		UpdateGraphics();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
}
