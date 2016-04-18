import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InterfacePanel extends JPanel {
	private final String INTERFACE_LABEL = "Interface";
	private final String READ_GROUP_LABEL = "Read";
    private final String READ_LABEL1 = "Path";
    private final String READ_LABEL2 = "Filename";
	private final String WRITE_GROUP_LABEL = "Write";
    private final String WRITE_LABEL1 = "Input File";
	private final String READ_BUTTON_LABEL = ".....READ";
	private final String WRITE_BUTTON_LABEL = ".....WRITE";
    private final String READ_SUBMIT_LABEL = "Get the file!";
    private final String WRITE_SUBMIT_LABEL = "Store the file!";
	private final int TEXT_FIELD_WTDTH = 95;
	
	private MainWindow mainWindow;
    private JPanel interfacePanel;

    private JPanel readPanel;
	private JTextField readPathText;
    private JTextField readFilename;
	private JButton readFileButton;
    private JLabel readLbl;
    private JLabel readLbl2;
    private JButton readSubmit;

    private JPanel writePanel;
	private	JTextField writePathText;
	private JButton writePathButton;
    private JLabel writeLbl;
    private JButton writeSubmit;
	
	public InterfacePanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		setComponents();
	}
	
	public void setComponents() {
        readFilename = new JTextField("", TEXT_FIELD_WTDTH);
		readPathText = new JTextField("", TEXT_FIELD_WTDTH);
		readPathText.setEditable(false);
        readSubmit = new JButton(READ_SUBMIT_LABEL);
        readSubmit.addActionListener(new EventListener());
		readFileButton = new JButton(READ_BUTTON_LABEL);
		readFileButton.setPreferredSize(new Dimension(20, 20));
        readFileButton.addActionListener(new EventListener());
        readLbl = new JLabel(READ_LABEL1);
        readLbl2 = new JLabel(READ_LABEL2);
		readPanel = new JPanel();
        readPanel.add(readLbl);
		readPanel.add(readPathText);
		readPanel.add(readFileButton);
        readPanel.add(readLbl2);
        readPanel.add(readFilename);
        readPanel.add(readSubmit);
		TitledBorder inputTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), READ_GROUP_LABEL);
		readPanel.setBorder(inputTitle);
		
		writePathText = new JTextField("", TEXT_FIELD_WTDTH);
		writePathText.setEditable(false);
        writeSubmit = new JButton(WRITE_SUBMIT_LABEL);
        writeSubmit.addActionListener(new EventListener());
		writePathButton = new JButton(WRITE_BUTTON_LABEL);
		writePathButton.setPreferredSize(new Dimension(20, 20));
        writePathButton.addActionListener(new EventListener());
        writeLbl = new JLabel(WRITE_LABEL1);
		writePanel = new JPanel();
        writePanel.add(writeLbl);
		writePanel.add(writePathText);
		writePanel.add(writePathButton);
        writePanel.add(writeSubmit);
		TitledBorder outputTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), WRITE_GROUP_LABEL);
		writePanel.setBorder(outputTitle);
		
		interfacePanel = new JPanel();
		interfacePanel.setLayout(new GridLayout(2,1));
		interfacePanel.add(readPanel);
		interfacePanel.add(writePanel);
		TitledBorder interfaceTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), INTERFACE_LABEL);
		interfacePanel.setBorder(interfaceTitle);

		
		setLayout(new BorderLayout());
		add(interfacePanel, BorderLayout.CENTER);
		
		setVisible(true);
	}

    private class EventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean flag=true;
            // TODO Auto-generated method stub
            if(e.getActionCommand().equals(READ_BUTTON_LABEL)) {
                JFileChooser opener = new JFileChooser();
                opener.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                opener.setAcceptAllFileFilterUsed(false);
                if(opener.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
                    readPathText.setText(opener.getSelectedFile().toString());
                }
            } else if (e.getActionCommand().equals(WRITE_BUTTON_LABEL)) {
                JFileChooser opener = new JFileChooser();
                opener.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if(opener.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
                    writePathText.setText(opener.getSelectedFile().toString());
                }
            }else if (e.getActionCommand().equals(READ_SUBMIT_LABEL)) {
                if(readPathText.getText().equals("") || readFilename.getText().equals("") ){
                    JOptionPane.showMessageDialog(null, "Please input the file.");
                }else {
                    JOptionPane.showMessageDialog(null, "File is created..");
                    mainWindow.getController().read(readPathText.getText() +"/"+ readFilename.getText());
                    readFilename.setText("");
                    readPathText.setText("");

                }
            }else if (e.getActionCommand().equals(WRITE_SUBMIT_LABEL)) {
                if(writePathText.getText().equals("") ){
                    JOptionPane.showMessageDialog(null, "Please input the file.");
                }else{
                    JOptionPane.showMessageDialog(null, "File is stored..");
                    mainWindow.getController().write(writePathText.getText());
                    writePathText.setText("");
                }
            }
            updateUI();
        }
    }
}
