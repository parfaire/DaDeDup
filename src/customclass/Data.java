package customclass;

import java.util.List;

public class Data {
    private List<Long> offsets;
    private long size;

    public Data(List<Long> offsets, long size){
        this.offsets=offsets;
        this.size=size;
    }

    List<Long> getOffsets(){
        return offsets;
    }

    public long getSize(){
        return size;
    }
}
