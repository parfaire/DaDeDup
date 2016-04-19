package customclass;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Interface {
    public static void read(String out, String storage, Book book, int blocksize){
        try {
            byte[] block;
            //create clean output file
            File fout = new File(out);
            PrintWriter pw = new PrintWriter(out);
            pw.close();
            String filename = fout.getName();

            if(!book.containsRecord(filename)){
                System.err.println(filename+" does not exist.");
            }else {
                RandomAccessFile raf = new RandomAccessFile(storage, "r");
                FileOutputStream fos = new FileOutputStream(fout, true);
                Data data = book.getRecord(filename);
                long size = data.getSize();
                List<Long> offsets = data.getOffsets();
                for (Long offset : offsets){
                    if(size<blocksize)
                        blocksize= (int) size;
                    block = new byte[blocksize];
                    raf.seek(offset);
                    raf.read(block);
                    fos.write(block,0,blocksize);
                    size = size-blocksize;
                }
                fos.close();
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void write(String input, String storage, Ddt ddt, Book book, String hashFunc, int blockSize){
        try {
            MessageDigest digest = MessageDigest.getInstance(hashFunc);
            File fin = new File(input);
            File storageFile = new File(storage);
            FileInputStream fis = new FileInputStream(fin);
            RandomAccessFile raf = new RandomAccessFile(storage, "rw");
            ByteWrapper hashedBlock;
            List<Long> offsets = new ArrayList<>();
            byte[] block = new byte[blockSize];
            int i;
            long offset;

            String filename = fin.getName();
            raf.seek(storageFile.length()); //append mode

            while((i = fis.read(block)) != -1){
                digest.update(block, 0, i);
                hashedBlock = new ByteWrapper(digest.digest());
                //duplicate block?
                if (ddt.containsKey(hashedBlock)) {
                    offsets.add(ddt.get(hashedBlock));
                } else {
                    offset = raf.getFilePointer();
                    raf.write(block, 0, i);
                    offsets.add(offset);
                    ddt.put(hashedBlock,offset);
                }
            }
            raf.close();
            fis.close();
            Data data = new Data(offsets,fin.length());
            book.addRecord(filename,data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
