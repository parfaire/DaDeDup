import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.HashMap;

public class Interface {
    static public void read(String out, String storage, HashMap<Long,Data> ddt){
        try {
            RandomAccessFile read = new RandomAccessFile(storage,"r");
            FileOutputStream fos = new FileOutputStream(new File(out), true);


            fos.close();
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static public void write(String in, String storage, HashMap<Long,Data> ddt, String hashFunc, int blockSize){
        try {
            MessageDigest digest = MessageDigest.getInstance(hashFunc);
            FileInputStream fis = new FileInputStream(new File(in));
            FileOutputStream fos = new FileOutputStream(new File(storage), true);
            byte[] block = new byte[blockSize];
            byte[] hashedBytes,prevHashedBytes;
            long prevHB=0,HB;
            int i, offset = 0;

            //FIRST RECORD
            if ((i = fis.read(block)) != -1) {
                //convert block to hash
                digest.update(block,0,i);
                prevHashedBytes = digest.digest();
                prevHB = ByteBuffer.wrap(prevHashedBytes).getLong();
                //write the block to storage
                if(!ddt.containsKey(prevHB)){
                    fos.write(block,0,i);
                    offset += i;
                }
            }
            while ((i = fis.read(block)) != -1) {
                //convert block to hash
                digest.update(block,0,i);
                hashedBytes = digest.digest();
                HB = ByteBuffer.wrap(hashedBytes).getLong();
                //check duplicate

                if(ddt.containsKey(prevHB)){
                    System.out.println("DUPLICATE BLOCK - DETECTED");
                    ddt.get(prevHB).add(in,HB);
                }else{
                    //update ddt record & write the block to storage
                    ddt.put(prevHB, new Data(in, HB, offset, i));
                    fos.write(block,0,i);
                    offset += i;
                }
                prevHB = HB;
            }
            //LAST RECORD
            if (offset>0) //anticipate of no record
                ddt.put(prevHB, new Data(in, null, offset, i));
            fos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static public void saveddt(String file, HashMap<Long,Data> ddt){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ddt);
            oos.close();
            fos.close();
        } catch (Exception ex) {
            System.out.println("There is an issue on saving ddt.");
            //ex.printStackTrace();
        }
    }

    static public HashMap<Long,Data> loadddt(String file){
        HashMap<Long,Data> ddt = new HashMap<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ddt = (HashMap<Long,Data>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception ex) {
            System.out.println("There is no existing ddt.");
            //ex.printStackTrace();
        }
        return ddt;
    }

}
