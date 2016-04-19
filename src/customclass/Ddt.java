package customclass;

import java.io.*;
import java.util.*;

public class Ddt extends HashMap<ByteWrapper,Long>{

    public Ddt(){
        super();
    }
    public void write(String output) throws IOException{
        //DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(output));
        for (Map.Entry <ByteWrapper,Long> entry: this.entrySet()) {
            out.writeObject(entry.getKey());
            out.writeLong(entry.getValue());
        }
        out.close();
    }

    public void read(String input) throws IOException{
        ByteWrapper key;
        Long val;
        //DataInputStream in = new DataInputStream(new FileInputStream(input));
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(input))) {
            for (; ; ) {
                key = (ByteWrapper) in.readObject();
                val = in.readLong();
                put(key, val);
            }


        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
