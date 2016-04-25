package customclass;

import java.io.*;
import java.util.*;

public class Book{
    private HashMap<String,Data> records;
    private HashMap<String,List<String>> folderDetails;

    public Book(){
        records = new HashMap<>();
        folderDetails = new HashMap<>();
    }

    void addRecord(String filename, Data data){
        records.put(filename,data);
    }

    boolean containsRecord(String filename){
        return records.containsKey(filename);
    }

    public void addFolderDetails(String s, List<String> files){
        folderDetails.put(s,files);
    }

    public List<String> getFolderDetailsOf(String s){
        return folderDetails.get(s);
    }

    Data getRecord(String filename){
        return records.get(filename);
    }

    public void writeRecords(String output) throws IOException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File(output),true));
        for(Map.Entry<String, Data> entry : records.entrySet()) {
            String filename = entry.getKey();
            Data data = entry.getValue();
            pw.println(filename);
            pw.println(data.getSize());
            for (Long l : data.getOffsets()) {
                pw.print(l+" ");
            }
            pw.println();
        }
        pw.close();
    }

    public void readRecords(String input, String target, boolean all) throws IOException {
        long size;
        String filename;
        List<Long> offsets;
        Data data;
        try{
            Scanner sc = new Scanner(new FileInputStream(input));
            while(sc.hasNextLine()) {
                filename = sc.nextLine();
                if(filename.equals(target) || all){
                    size = sc.nextLong();sc.nextLine();
                    offsets = new ArrayList<>();
                    while (sc.hasNextLong()) {
                        Long l = sc.nextLong();
                        offsets.add(l);
                    }
                    if(sc.hasNextLine())
                        sc.nextLine();
                    data = new Data(offsets,size);
                    records.put(filename,data);
                }else{//skip it
                    sc.nextLine();
                    if(sc.hasNextLine())
                        sc.nextLine();
                }
            }
            sc.close();
        }
        catch (Exception e){e.printStackTrace();}
    }

    public void writeFolderDetails(String fileFolderDetails) throws IOException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File(fileFolderDetails),true));
        for(Map.Entry<String, List<String>> entry : folderDetails.entrySet()) {
            String folderName = entry.getKey();
            List<String> files = entry.getValue();
            pw.println(folderName);
            pw.println(files.size());
            files.forEach(pw::println);
        }
        pw.close();
    }

    public void readFolderDetails(String input, String target, boolean all) throws IOException {
        int numberOfFiles;
        String folderName;
        List<String> files;
        try{
            Scanner sc = new Scanner(new FileInputStream(input));
            while(sc.hasNextLine()) {
                folderName = sc.nextLine();
                if(folderName.equals(target) || all){
                    numberOfFiles = sc.nextInt(); sc.nextLine();
                    files = new ArrayList<>();
                    for(int i=0;i<numberOfFiles;i++){
                        files.add(sc.nextLine());
                    }
                    folderDetails.put(folderName,files);
                }else{ //skip it
                    numberOfFiles = sc.nextInt(); sc.nextLine();
                    for(int i=0;i<numberOfFiles;i++){
                        sc.nextLine();
                    }
                }
            }
            sc.close();
        }
        catch (Exception e){e.printStackTrace();}
    }
}
