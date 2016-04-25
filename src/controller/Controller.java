package controller;

import customclass.*;
import org.apache.commons.codec.digest.DigestUtils;
import ui.MainWindow;

import javax.swing.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;

public class Controller {
    private String PATH = "/Users/parfaire/IdeaProjects/DataDeduplication/dd/";
    private String storage = PATH+"storage.txt";
    private String ddtfile = PATH+"ddt.txt";
    private String records = PATH+"records.txt";
    private String folderdetails = PATH+"folderdetails.txt";
    private String listfiles = PATH+"list.txt";
    private String hashFunc = "SHA-256";
    private String conf = PATH+"conf.txt";
    private int blockSize;
	private MainWindow mainWindow;
    private Ddt ddt = new Ddt();
    private Book book = new Book();
	
	public Controller(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
        try{
            readConf();
            book.readRecords(records,"",true);
            book.readFolderDetails(folderdetails,"",true);
            ddt.read(ddtfile);
        } catch(Exception e){
            System.err.println("There is no ddt and book to be loaded.");
        }
        refreshStatus();
	}
	
	public void refreshStatus() {
        setList();
        monitorMemory();
        Long d = new File(ddtfile).length();
        Long r = new File(records).length();
        Long f = new File(folderdetails).length();
        Long s = new File(storage).length();
        Long tot = d+r+f+s;
		mainWindow.getStatusPanel().setLblDdt(d);
        mainWindow.getStatusPanel().setLblRecord(r);
        mainWindow.getStatusPanel().setLblFolder(f);
        mainWindow.getStatusPanel().setLblStorage(s);
        mainWindow.getStatusPanel().setLblTotal(tot);
        mainWindow.getStatusPanel().setTfDdt(ddtfile);
        mainWindow.getStatusPanel().setTfRecord(records);
        mainWindow.getStatusPanel().setTfFolder(folderdetails);
        mainWindow.getStatusPanel().setTfStorage(storage);
        mainWindow.getStatusPanel().setLblDedup();
        mainWindow.getStatusPanel().updateUI();
	}

    public void updateStatus() {
        try {
            ddt.write(ddtfile);
            book.writeRecords(records);
            book.writeFolderDetails(folderdetails);
//            book = new Book();
        }catch (Exception e){
            e.printStackTrace();
        }
        refreshStatus();
    }

    public void read(String target, String folderAndFileName){
//        try {
//            book.readRecords(records,folderAndFileName,false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Interface.read(target,folderAndFileName,storage,book,blockSize);
    }
    public void readDirectory(String target, String folderName){
//        try {
//            book.readFolderDetails(folderdetails,folderName,false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        List<String> al = book.getFolderDetailsOf(folderName);
        for( String folderAndFileName : al ){
            read(target,folderAndFileName);
        }
        JOptionPane.showMessageDialog(mainWindow, "Read Folder is finished..");
        //book = new Book();
    }

    public void write(String folderAndFileName, File f){
        Interface.write(folderAndFileName,f,storage,ddt,book,hashFunc,blockSize);
    }
    public long writeDirectoryRec(String folderName, File directory, List<String> listOfFiles, long s){
        long size = s;
        for( File f : directory.listFiles() ){
            if(f.isDirectory()) {
                size += writeDirectoryRec(folderName,f, listOfFiles,0);
            }else{
                int startIdx = f.getAbsolutePath().indexOf(folderName);
                String folderAndFileName = f.getAbsolutePath().substring(startIdx);
                listOfFiles.add(folderAndFileName);
                write(folderAndFileName,f);
                size+= f.length();
            }
        }
        return size;
    }
    public void writeDirectory(String folderName, File directory){
        long size;
        List<String> listOfFiles = new ArrayList<>();
        size = writeDirectoryRec(folderName,directory,listOfFiles,0);
        book.addFolderDetails(folderName,listOfFiles);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(new File(listfiles),true));
            pw.println(folderName);
            pw.println(size);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setList(){
        long total = 0;
        NumberFormat format = NumberFormat.getInstance();
        List<String> filenames = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new FileInputStream(listfiles));
            while(sc.hasNextLine()){
                String name = sc.nextLine();
                long size = sc.nextLong(); sc.nextLine();
                total += size;
                filenames.add(name + " (" + format.format(size/1024)+ " KB)");
            }
            String[] fnArray = new String[filenames.size()];
            filenames.toArray(fnArray);
            mainWindow.getInterfacePanel().getList().setListData(fnArray);
            mainWindow.getStatusPanel().setLblExpectedTotal(total);
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clearData(){
        System.gc();
        try{
            PrintWriter pw = new PrintWriter(records);
            pw.close();
            pw = new PrintWriter(folderdetails);
            pw.close();
            pw = new PrintWriter(ddtfile);
            pw.close();
            pw = new PrintWriter(storage);
            pw.close();
            pw = new PrintWriter(listfiles);
            pw.close();
            ddt = new Ddt();
            book = new Book();
            refreshStatus();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void readConf(){
        int x=4096; //default
        try{
            Scanner sc = new Scanner(new FileInputStream(conf));
            x = sc.nextInt();
            sc.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        blockSize = x;
        mainWindow.getStatusPanel().setTfBlokcSize(x);
    }

    public void saveConf(String bSize){
        blockSize = Integer.parseInt(bSize);
        try{
            PrintWriter pw = new PrintWriter(conf);
            pw.print(bSize);
            pw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"Configuration is saved.");
    }
    public void compareTwoDir(String dir1, String dir2){
        String x = calcMD5HashForDir(new File(dir1),true);
        String y = calcMD5HashForDir(new File(dir2),true);
        if(x.equals(y)){
            JOptionPane.showMessageDialog(null,"Two directories contain the same files\n"+dir1+" = "+x+"\n"+dir2+" = "+y);
        }else{
            JOptionPane.showMessageDialog(null,"Two directories are different\n"+dir1+" = "+x+"\n"+dir2+" = "+y);
        }
    }

    public String calcMD5HashForDir(File dirToHash, boolean includeHiddenFiles) {

        assert (dirToHash.isDirectory());
        Vector<FileInputStream> fileStreams = new Vector<>();

        collectInputStreams(dirToHash, fileStreams, includeHiddenFiles);

        SequenceInputStream seqStream =
                new SequenceInputStream(fileStreams.elements());

        try {
            String md5Hash = DigestUtils.md5Hex(seqStream);
            seqStream.close();
            return md5Hash;
        }
        catch (IOException e) {
            throw new RuntimeException("Error reading files to hash in "
                    + dirToHash.getAbsolutePath(), e);
        }

    }

    private void collectInputStreams(File dir,
                                     List<FileInputStream> foundStreams,
                                     boolean includeHiddenFiles) {

        File[] fileList = dir.listFiles();
        Arrays.sort(fileList,               // Need in reproducible order
                new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return f1.getName().compareTo(f2.getName());
                    }
                });

        for (File f : fileList) {
            if (!includeHiddenFiles && f.getName().startsWith(".")) {
                // Skip it
            } else if (f.isDirectory()) {
                collectInputStreams(f, foundStreams, includeHiddenFiles);
            } else {
                try {
                    //System.out.println("\t" + f.getAbsolutePath());
                    foundStreams.add(new FileInputStream(f));
                } catch (FileNotFoundException e) {
                    throw new AssertionError(e.getMessage()
                            + ": file should never not be found!");
                }
            }
        }
    }

    public void monitorMemory(){
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long total = runtime.totalMemory();
        long used  = runtime.totalMemory() - runtime.freeMemory();
        sb.append("Total memory: " + format.format(total / 1024) + "KB | ");
        sb.append("Used memory: " + format.format(used / 1024) + "KB | ");
        mainWindow.getStatusPanel().setLblMemory(sb.toString());
    }
}
