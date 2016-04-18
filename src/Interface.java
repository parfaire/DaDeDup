import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;

class Interface {
    static void read(String out, String storage, HashMap<ByteWrapper,Data> ddt, HashMap<String,ByteWrapper> init){
        try {
            byte[] block;
            long offset;
            int len;
            ByteWrapper initial,next;
            Data data;
            String str;

            File fout = new File(out);
            String outName = fout.getName();

            //clean file first
            PrintWriter pw = new PrintWriter(out);
            pw.close();
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
                block = new byte[len];
                read.read(block);
                fos.write(block,0,len);

                //next block sequences
                while( (next = data.getNext(outName)) != null){
                    data = ddt.get(next);
                    offset = data.getOffset();
                    len = data.getLen();
                    //READ
                    read.seek(offset);
                    block = new byte[len];
                    read.read(block);
                    fos.write(block,0,len);

                }
                //write last piece
                if(next != null){
                    data = ddt.get(next);
                    offset = data.getOffset();
                    len = data.getLen();
                    //READ
                    read.seek(offset);
                    block = new byte[len];
                    read.read(block);
                    fos.write(block,0,len);
                }


                fos.close();
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static public void write(String input, String storage, HashMap<ByteWrapper,Data> ddt, HashMap<String,ByteWrapper> init, String hashFunc, int blockSize){
        try {
            MessageDigest digest = MessageDigest.getInstance(hashFunc);
            File fin = new File(input);
            File storageFile = new File(storage);
            FileInputStream fis = new FileInputStream(fin);
            RandomAccessFile fos = new RandomAccessFile(storage, "rw");
            fos.seek(storageFile.length());
            byte[] block = new byte[blockSize];
            byte[] hashedBytes,prevHashedBytes=null;
            int prevI, i;
            long offset=0;
            String in = fin.getName();

            //FIRST RECORD
            if ((i = fis.read(block)) != -1) {
                //convert block to hash
                digest.update(block,0,i);
                prevHashedBytes = digest.digest();
                //store into init table if new file
                if(!init.containsKey(in))
                    init.put(in,new ByteWrapper(prevHashedBytes));
                //write the block to storage
                if(!ddt.containsKey(new ByteWrapper(prevHashedBytes))){
                    offset = fos.getFilePointer();
                    fos.write(block,0,i);
                }
            }
            prevI = i;
            while ((i = fis.read(block)) != -1) {
                //convert block to hash
                digest.update(block,0,i);
                hashedBytes = digest.digest();

                //check duplicate
                if(ddt.containsKey(new ByteWrapper(prevHashedBytes))) {
                    ddt.get(new ByteWrapper(prevHashedBytes)).add(in, new ByteWrapper(hashedBytes));
                    //if hashbyte as pointer is different write it down to the storage
                    if (!ddt.containsKey(new ByteWrapper(hashedBytes))) {
                        offset = fos.getFilePointer();
                        fos.write(block, 0, i);
                    }
                }else{
                    //update ddt record & write the block to storage
                    ddt.put(new ByteWrapper(prevHashedBytes), new Data(in, new ByteWrapper(hashedBytes), offset, prevI));
                    offset = fos.getFilePointer();
                    fos.write(block,0,i);
                }
                prevHashedBytes=hashedBytes;
                prevI = i;
            }
            //LAST RECORD
            if (offset>0) { //anticipate of no record
                ddt.put(new ByteWrapper(prevHashedBytes), new Data(in, null, offset, prevI));
            }
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
            System.out.println("There is an issue on saving object.");
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
            System.out.println("There is no existing object to be load.");
            //ex.printStackTrace();
        }
        return o;
    }

}
