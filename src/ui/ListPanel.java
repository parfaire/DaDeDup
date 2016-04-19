package ui;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class ListPanel extends JPanel {

	private MainWindow mainWindow;
	private String SERVER_LIST_LABEL = "List of Files";
	private JScrollPane scrollPane;
	private JList<String> list;
	
	ListPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		setComponents();
	}

    public JList<String> getList(){
        return list;
    }

	void setComponents() {
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
