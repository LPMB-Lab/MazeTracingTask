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

public class drawWindow extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	private static final int STATE_POSITION = 105;
	private static final int DIFFICULTY = 5;
	
	Dimension screenSize;
	RenderingHints rh;
		
	State m_State;
	State m_RecoveryState;
	boolean m_bIsPressed;
	boolean m_bIsPaused;
	
	Vector<Trial> m_vTrials = new Vector<Trial>();
	
	Timer m_Timer;
	Button startButton;
	Button pauseButton;
	Button restartButton;
	Button quitButton;
	Button saveButton;
	
	long m_lStartTime;
	int m_iGlobalTimer;
	
	drawWindow()
	{
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
		addMouseListener(this);
		
		try {
			startButton = new Button(ImageIO.read(getClass().getResource("images/startButton.png")), 5, 5);
			pauseButton = new Button(ImageIO.read(getClass().getResource("images/pauseButton.png")), 100, 5);
			restartButton = new Button(ImageIO.read(getClass().getResource("images/restartButton.png")), 195, 5);
			quitButton = new Button(ImageIO.read(getClass().getResource("images/quitButton.png")), 290, 5);
			saveButton = new Button(ImageIO.read(getClass().getResource("images/saveButton.png")), 385, 5);
		} catch (IOException e) {e.printStackTrace();}

		Reset();
	}
	
	void Reset()
	{
		if (m_Timer != null)
			m_Timer.cancel();
			
		m_State = State.IDLE;
		m_Timer = new Timer();
		m_bIsPressed = false;
		m_bIsPaused = false;
		m_iGlobalTimer = 0;
		
		m_vTrials.clear();
		
		for (int i = 0; i < 1; i++)
		{
			Trial myTrial = new Trial(DIFFICULTY);
			m_vTrials.add(myTrial);
		}
	}
	class updateTask extends TimerTask
	{
    	State state;
    	
    	updateTask(State state) {this.state = state;}
		public void run()
		{
			if (m_State != State.PAUSE && m_State != State.COUNTDOWN)
				m_State = state;
			
			if (m_State == State.COUNTDOWN)
			{
				if (m_iGlobalTimer == 0)
				{
					m_State = state;
				}
				else
				{
					m_Timer.schedule(new updateTask(State.IN_TRIAL), 1000);
					m_iGlobalTimer--;
				}
			}
			
			if (m_State == State.IN_TRIAL)
					UpdateGraphics();
		}
	}
	private void UpdateGraphics()
	{
		Graphics g;
		g = getGraphics();
		paint(g);
	}
    private void countDownToState(int timer, State state)
    {
    	m_iGlobalTimer = timer;
    	m_State = State.COUNTDOWN;
    	m_Timer.schedule(new updateTask(state), 1000);
    }
	void doDrawing(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        
        switch (m_State)
        {
        	case IDLE:
        		g2d.drawImage(startButton.getImage(), startButton.getX(),  startButton.getY(),  null);
        		break;
			case COMPLETED:
				break;
			case COUNTDOWN:
				g2d.drawString("Countdown to begin in " + m_iGlobalTimer + " seconds", 5, STATE_POSITION);
				break;
			case IN_TRIAL:				
				break;
			case PAUSE:
				break;
			default:
				break;
        }
        
        g2d.drawImage(pauseButton.getImage(), pauseButton.getX(), pauseButton.getY(), null);
        g2d.drawImage(restartButton.getImage(), restartButton.getX(), restartButton.getY(), null);
        g2d.drawImage(quitButton.getImage(), quitButton.getX(), quitButton.getY(), null);
        g2d.drawImage(saveButton.getImage(), saveButton.getX(), saveButton.getY(), null);
        
        
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        
        g2d.setStroke(new BasicStroke(10));
        
        for (int i = 0; i < m_vTrials.size(); i++)
        {
        	Trial myTrial = m_vTrials.get(i);
        	Point2D[] m_aPointsArray = myTrial.getPointsArray();
        	
        	for (int j = 0; j < m_aPointsArray.length-1; j++)
        		g2d.drawLine(m_aPointsArray[j].getX()*screenWidth/100, m_aPointsArray[j].getY()*screenHeight/100, m_aPointsArray[j+1].getX()*screenWidth/100, m_aPointsArray[j+1].getY()*screenHeight/100 );
        }
	}
	@Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }
	@Override
	public void mouseClicked(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		
		if (startButton.isPressed(x, y))		{StartSimulation();}
		else if (pauseButton.isPressed(x, y))	{PauseSimulation();}
		else if (quitButton.isPressed(x, y))	{System.exit(0);}
		else if (restartButton.isPressed(x, y))	{Reset();}
		else if (saveButton.isPressed(x, y))	{ExportFile();}
	}
	private void StartSimulation()
	{
		countDownToState(5, State.IN_TRIAL);
	}
	private void ExportFile()
	{
	}
	private void PauseSimulation()
	{
		if (m_bIsPaused)
		{
			m_bIsPaused = false;
			if (m_RecoveryState == State.COUNTDOWN)
				countDownToState(5, State.IN_TRIAL);
			else
				m_State = m_RecoveryState;
		}
		else
		{
			m_bIsPaused = true;
			m_RecoveryState = m_State;
			m_State = State.PAUSE;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {m_bIsPressed = true;}
	@Override
	public void mouseReleased(MouseEvent e) {m_bIsPressed = false;}
}
