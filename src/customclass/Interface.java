package customclass;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Interface {
    public static void read(String target, String folderAndFileName, String storage, Book book, int blocksize){
        try {
            byte[] block;
            target = target+"/"+folderAndFileName;
            File fout = new File(target);
            File parent = fout.getParentFile();
            if(!parent.exists() && !parent.mkdirs()){
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }
            if(book.containsRecord(folderAndFileName)){
                PrintWriter pw = new PrintWriter(target);
                pw.close();
                RandomAccessFile raf = new RandomAccessFile(storage, "r");
                FileOutputStream fos = new FileOutputStream(fout, true);
                Data data = book.getRecord(folderAndFileName);
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

    public static void write(String folderAndFileName, File fin, String storage, Ddt ddt, Book book, String hashFunc, int blockSize){
        try {
            MessageDigest digest = MessageDigest.getInstance(hashFunc);
            File storageFile = new File(storage);
            FileInputStream fis = new FileInputStream(fin);
            RandomAccessFile raf = new RandomAccessFile(storage, "rw");
            ByteWrapper hashedBlock;
            List<Long> offsets = new ArrayList<>();
            byte[] block = new byte[blockSize];
            int i;
            long offset;

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
            book.addRecord(folderAndFileName,data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
