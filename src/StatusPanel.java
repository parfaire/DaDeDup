import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class StatusPanel extends JPanel {

	private final String LABEL = "Status";
	private final int TEXT_FIELD_WTDTH = 45;
    private MainWindow mainWindow;
	private JPanel panelDdt;
	private JPanel panelInit;
	private JPanel panelStorage;
	private JPanel panelAction;
	private JButton btnUpdate;
	private JButton btnRefresh;
	private JTextField tfDdt;
	private JTextField tfInit;
	private JTextField tfStorage;
    private JLabel lblDdt;
    private JLabel lblInit;
    private JLabel lblStorage;
    private JLabel lblTotal;

    public void setLblTotal(long l) {
        this.lblTotal.setText("Total : " + l +" bytes");
    }

    public void setLblStorage(long l) {
        this.lblStorage.setText(l +" bytes");
    }

    public void setLblDdt(long l) {
        this.lblDdt.setText(l +" bytes");
    }

    public void setLblInit(long l) {
        this.lblInit.setText(l +" bytes");
    }

    public void setTfDdt(String s) {
        this.tfDdt.setText(s);
    }

    public void setTfInit(String s) {
        this.tfInit.setText(s);
    }

    public void setTfStorage(String s) {
        this.tfStorage.setText(s);
    }

	
	public StatusPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
		this.setComponents();
	}
	
	public void setComponents() {
		//title
		TitledBorder joblistTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), this.LABEL);
		this.setBorder(joblistTitle);

		//components
		tfDdt = new JTextField("", TEXT_FIELD_WTDTH);
        tfDdt.setEditable(false);
		tfInit = new JTextField("", TEXT_FIELD_WTDTH);
        tfInit.setEditable(false);
		tfStorage = new JTextField("", TEXT_FIELD_WTDTH);
        tfStorage.setEditable(false);
        lblDdt = new JLabel("");
        lblInit = new JLabel("");
        lblStorage = new JLabel("");
        lblTotal = new JLabel("");
		btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            mainWindow.getController().updateStatus();
            JOptionPane.showMessageDialog(null, "Updated");
        });
		btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> {
            mainWindow.getController().refreshStatus();
            JOptionPane.showMessageDialog(null, "Refreshed");
        });

		panelDdt = new JPanel();
		panelDdt.setLayout(new FlowLayout());
		panelDdt.add(tfDdt);
        panelDdt.add(lblDdt);
        TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "DDT");
        panelDdt.setBorder(title);

		panelInit = new JPanel();
		panelInit.setLayout(new FlowLayout());
		panelInit.add(tfInit);
        panelInit.add(lblInit);
        title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Initial Table");
        panelInit.setBorder(title);

		panelStorage = new JPanel();
		panelStorage.setLayout(new FlowLayout());
        panelStorage.add(tfStorage);
        panelStorage.add(lblStorage);
        title= BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Storage");
        panelStorage.setBorder(title);

		panelAction = new JPanel();
		panelAction.setLayout(new FlowLayout());
		panelAction.add(btnRefresh);
		panelAction.add(btnUpdate);
		
		// Layout
		setLayout(new GridLayout(5,1));
        add(panelDdt);
        add(panelInit);
        add(panelStorage);
        add(lblTotal);
        add(panelAction);
		setVisible(true);
	}
}
