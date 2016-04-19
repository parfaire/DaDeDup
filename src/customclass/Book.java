package customclass;

import java.io.*;
import java.util.*;

public class Book{
    private HashMap<String,Data> records;

    public Book(){
        records = new HashMap<>();
    }

    void addRecord(String filename, Data data){
        records.put(filename,data);
    }

    boolean containsRecord(String filename){
        return records.containsKey(filename);
    }

    Data getRecord(String filename){
        return records.get(filename);
    }

    public HashMap<String,Data> getBookRecords() { return records; }

    public void write(String output) throws IOException {
        PrintWriter pw = new PrintWriter(output);
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

    public void read(String input) throws IOException {
        records = new HashMap<>();
        long size;
        String filename;
        List<Long> offsets;
        Data data;
        try{
            Scanner sc = new Scanner(new FileInputStream(input));
            while(sc.hasNextLine()) {
                filename = sc.nextLine();
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
            }
            sc.close();
        }
        catch (Exception e){e.printStackTrace();}
    }
}
