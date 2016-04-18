import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;



public class ListPanel extends JPanel {

	private MainWindow mainWindow;
	private String SERVER_LIST_LABEL = "List of Files";
	private JScrollPane scrollPane;
	private JList<String> list;
	
	public ListPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		setComponents();
	}

    public JList<String> getList(){
        return list;
    }

	public void setComponents() {
		TitledBorder listTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), SERVER_LIST_LABEL);
		setBorder(listTitle);
        list = new JList<>();
		scrollPane = new JScrollPane(list);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Layout
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		setVisible(true);
	}
}
