import java.io.*;
import java.util.HashMap;

public class Data implements Serializable {
    private HashMap<String,ByteWrapper> next;
    private long offset;
    private int len;

    public Data(String filename, ByteWrapper checksum, long offset,int len){
        this.offset=offset;
        this.len=len;
        next = new HashMap<>();
        next.put(filename,checksum);
    }

    void add(String filename, ByteWrapper checksum){
        if(!next.containsKey(filename))
            next.put(filename,checksum);
    }

    int getReferenceCount(){
        return next.size();
    }

    long getOffset(){
        return offset;
    }

    int getLen(){
        return len;
    }

    ByteWrapper getNext(String filename){
        return next.get(filename);
    }

    @Override
    public String toString() {
        return "offset:"+offset+"\t len:"+len+"\t next:"+ Controller.showHM(next);
    }
/*
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(next);
        out.writeLong(offset);
        out.writeInt(len);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        next = (HashMap<String,ByteWrapper>)in.readObject();
        offset = in.readLong();
        len = in.readInt();
    }*/
}
