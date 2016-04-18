import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainWindow extends JFrame implements WindowListener{

	private final Dimension MIN_FRAME_SIZE = new Dimension(1280, 640);
	
	private InterfacePanel interfacePanel;
	private ListPanel listPanel;
	private StatusPanel statusPanel;
	
	Controller controller;

	public ListPanel getListPanel() {
		return listPanel;
	}

	public StatusPanel getStatusPanel() {
		return statusPanel;
	}

	public Controller getController() {
		return controller;
	}
	
	/**
	 * Constructor
	 */
	public MainWindow() {
		// controller
		this.setupComponents();
		controller = new Controller(this);
		setVisible(true);
		addWindowListener(this);
	}
	
	public void setupComponents() {
		this.setMinimumSize(MIN_FRAME_SIZE);
		
		// Panels
		this.interfacePanel = new InterfacePanel(this);
		this.listPanel = new ListPanel(this);
		this.statusPanel = new StatusPanel(this);
		
		// Layout
		this.setLayout(new BorderLayout());
		this.getContentPane().add(this.interfacePanel, BorderLayout.PAGE_START);
		this.interfacePanel.setPreferredSize(new Dimension(this.getWidth(), (int)(this.getHeight()*0.4)));
		this.getContentPane().add(this.listPanel, BorderLayout.WEST);
		this.listPanel.setPreferredSize(new Dimension((int)(this.getWidth()*0.4), this.getHeight()));
		this.getContentPane().add(this.statusPanel, BorderLayout.CENTER);
		this.statusPanel.setPreferredSize(new Dimension((int)(this.getWidth()*0.6), this.getHeight()));
		
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
