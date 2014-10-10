import javax.swing.JFrame;

public class mainWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {new mainWindow();}
	
	public mainWindow()
	{	
		setTitle("Project Nahgem");
        setSize(1280, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
}
