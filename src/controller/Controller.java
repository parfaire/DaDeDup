package controller;

import customclass.Book;
import customclass.Data;
import customclass.Ddt;
import customclass.Interface;
import ui.MainWindow;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private String PATH = "/Users/parfaire/IdeaProjects/DataDeduplication/dd/";
    private String storage = PATH+"storage.txt";
    private String ddtfile = PATH+"ddt.txt";
    private String bookfile = PATH+"book.txt";
    private String hashFunc = "SHA-256";
    private int blockSize = 4096;
	private MainWindow mainWindow;
    private Ddt ddt = new Ddt();
    private Book book = new Book();
	
	public Controller(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
        try{
            ddt.read(ddtfile);
            book.read(bookfile);
        } catch(Exception e){
            System.err.println("There is no ddt and book to be loaded.");
        }

        refreshStatus();
	}
	
	public void refreshStatus() {
        Long d = new File(ddtfile).length();
        Long b = new File(bookfile).length();
        Long s = new File(storage).length();
        Long tot = d+b+s;
		mainWindow.getStatusPanel().setLblDdt(d);
        mainWindow.getStatusPanel().setLblBook(b);
        mainWindow.getStatusPanel().setLblStorage(s);
        mainWindow.getStatusPanel().setLblTotal(tot);
        mainWindow.getStatusPanel().setTfDdt(ddtfile);
        mainWindow.getStatusPanel().setTfBook(bookfile);
        mainWindow.getStatusPanel().setTfStorage(storage);
        mainWindow.getStatusPanel().updateUI();
        setList();
	}

    public void updateStatus() {
        try {
            ddt.write(ddtfile);
            book.write(bookfile);
        }catch (Exception e){
            e.printStackTrace();
        }
        refreshStatus();
    }

    public void read(String output){
        Interface.read(output,storage,book,blockSize);
    }

    public void write(String input){
        Interface.write(input,storage,ddt,book,hashFunc,blockSize);
        updateStatus();
    }

    private void setList(){
        HashMap<String,Data> bookRecords= book.getBookRecords();
        String[] filenames = new String[bookRecords.size()];
        int i=0;
        for(String name : bookRecords.keySet()) {
            filenames[i++] = name;
        }
        mainWindow.getListPanel().getList().setListData(filenames);
    }

    public static String showHM(HashMap<?,?> hm) {
        String s;
        s = "\n##### PRINTING HASHMAP #####\n";
        for (Map.Entry<?, ?> entry : hm.entrySet()) {
            s += entry.getKey() + "____" + entry.getValue() + "\n";
        }
        s += "##### ================ #####\n";
        return s;
    }

    public String getddt(){
        return showHM(ddt);
    }

    public String getbook(){
        return showHM(book.getBookRecords());
    }

    public void clearData(){
        System.out.println("Data is cleared");
        try{
            PrintWriter pw = new PrintWriter(bookfile);
            pw.close();
            pw = new PrintWriter(ddtfile);
            pw.close();
            pw = new PrintWriter(storage);
            pw.close();
            ddt = new Ddt();
            book = new Book();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
