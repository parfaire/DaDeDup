import java.io.*;
import java.util.Arrays;

public final class ByteWrapper implements Serializable
{
    private final byte[] data;

    public ByteWrapper(byte[] data)
    {
        if (data == null)
        {
            throw new NullPointerException();
        }
        this.data = data;
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof ByteWrapper))
        {
            return false;
        }
        return Arrays.equals(data, ((ByteWrapper)other).data);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(data);
    }
/*
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.write(data);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.read(data);
    }*/
}