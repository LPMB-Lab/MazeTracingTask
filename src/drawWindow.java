import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class drawWindow extends JPanel implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Dimension screenSize;
	RenderingHints rh;
	
	Timer m_Timer;
	State m_State;
	int m_iGlobalTimer;
	
	drawWindow()
	{
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
		addMouseListener(this);

		Reset();
	}
	
	void Reset()
	{
		m_iGlobalTimer = 0;
		m_Timer = new Timer();
		m_State = State.IDLE;
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
		}
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
		
		g2d.drawString("TEST",  50,  50);
	}
	@Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
