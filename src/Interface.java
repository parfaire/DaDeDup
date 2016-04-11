import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

class Interface {
    static void write(String out, String storage, HashMap<Long,Data> ddt, HashMap<String,Long> init){
        try {
            byte[] bytes;
            int offset,len;
            Long initial,next;
            Data data;
            String str;

            File fout = new File(out);
            String outName = fout.getName();
            if(!init.containsKey(outName)){
                System.err.println("File requested does not exist.");
            }else {
                RandomAccessFile read = new RandomAccessFile(storage, "r");
                FileOutputStream fos = new FileOutputStream(fout, true);

                //start chaining up and write
                initial = init.get(outName);
                //first block
                data = ddt.get(initial);
                offset = data.getOffset();
                len = data.getLen();
                //READ
                read.seek(offset);
                bytes = new byte[len];
                read.read(bytes);
                str = new String(bytes, StandardCharsets.UTF_8);
                System.out.print(str+"|");

                //next block sequences
                while( (next = data.getNext(outName)) != null){
                    data = ddt.get(next);
                    offset = data.getOffset();
                    len = data.getLen();
                    //READ
                    read.seek(offset);
                    bytes = new byte[len];
                    System.out.print(len);
                    read.read(bytes);
                    str = new String(bytes, StandardCharsets.UTF_8);
                    System.out.print(str+"|");

                }
                //

                fos.close();
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static public void read(String input, String storage, HashMap<Long,Data> ddt, HashMap<String,Long> init, String hashFunc, int blockSize){
        try {
            MessageDigest digest = MessageDigest.getInstance(hashFunc);
            File fin = new File(input);
            FileInputStream fis = new FileInputStream(fin);
            FileOutputStream fos = new FileOutputStream(new File(storage), true);
            byte[] block = new byte[blockSize];
            byte[] hashedBytes,prevHashedBytes;
            long prevHB=0,HB;
            int prevI, i, offset = 0;
            String in = fin.getName();

            //FIRST RECORD
            if ((i = fis.read(block)) != -1) {
                //convert block to hash
                digest.update(block,0,i);
                prevHashedBytes = digest.digest();
                prevHB = ByteBuffer.wrap(prevHashedBytes).getLong();
                //store into init table if new file
                if(!init.containsKey(in))
                    init.put(in,prevHB);
                //write the block to storage
                if(!ddt.containsKey(prevHB))
                    fos.write(block,0,i);
            }
            prevI = i;
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
                    ddt.put(prevHB, new Data(in, HB, offset, prevI));
                    fos.write(block,0,i);
                    offset += i;
                }
                prevHB = HB;
                prevI = i;
            }
            //LAST RECORD
            if (offset>0) //anticipate of no record
                ddt.put(prevHB, new Data(in, null, offset, prevI));
            fos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static public void saveObj(String file, Object o){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
            fos.close();
        } catch (Exception ex) {
            System.out.print("There is an issue on saving object.");
            //ex.printStackTrace();
        }
    }

    static Object loadObj(String file){
        Object o = new Object();
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            o = ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception ex) {
            System.out.print("There is no existing object to be load.");
            //ex.printStackTrace();
        }
        return o;
    }

}
