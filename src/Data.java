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

    public void add(String filename, Long checksum){
        if(!next.containsKey(filename))
            next.put(filename,checksum);
    }

    public int getReferenceCount(){
        return next.size();
    }

    public int getOffset(){
        return offset;
    }

    public int getLen(){
        return len;
    }
}
