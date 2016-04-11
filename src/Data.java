import java.io.Serializable;
import java.util.HashMap;

public class Data implements Serializable {
    private HashMap<String,Long> next;
    private int offset;
    private int len;

    public Data(String filename, Long checksum, int offset,int len){
        this.offset=offset;
        this.len=len;
        next = new HashMap<>();
        next.put(filename,checksum);
    }

    void add(String filename, Long checksum){
        if(!next.containsKey(filename))
            next.put(filename,checksum);
    }

    int getReferenceCount(){
        return next.size();
    }

    int getOffset(){
        return offset;
    }

    int getLen(){
        return len;
    }

    Long getNext(String filename){
        return next.get(filename);
    }

    @Override
    public String toString() {
        return "offset:"+offset+"\t len:"+len+"\t next:"+Test.showHM(next);
    }
}
