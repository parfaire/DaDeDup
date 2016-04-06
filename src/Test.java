
import java.util.HashMap;

public class Test {
    private static String PATH = "/Users/parfaire/IdeaProjects/DataDeduplication/";
    private static String READPATH = "/Users/parfaire/IdeaProjects/DataDeduplication/READ/";
    private static String out = READPATH+"input.txt";
    private static String in = PATH+"input2.txt";
    private static String storage = PATH+"output.txt";
    private static String ddtfile = PATH+"ddt.txt";
    private static String hashFunc = "SHA-256";
    private static int blockSize = 32; //32 bytes = 256bits
    private static HashMap<Long,Data> ddt;

    public static void main(String[] args) {
        ddt = Interface.loadddt(ddtfile);

        Interface.read(out,storage,ddt);

        //Interface.write(in,storage,ddt,hashFunc,blockSize);

        //Interface.saveddt(ddtfile,ddt);

        /*
        try{
            System.out.println(new String("ASD".getBytes()));
            System.out.println(new String("ASD".getBytes()));
            System.out.println(new String("ASD".getBytes()));
            System.out.println(new String("ASD".getBytes()));
            System.out.println("------------------");

            MessageDigest digest = MessageDigest.getInstance(hashFunc);
            digest.update("ASD".getBytes("UTF-8"));
            byte[] prevHashedBytes = digest.digest();
            System.out.println(ByteBuffer.wrap(("ASD".getBytes("UTF-8"))).getLong());
            digest.update("ASD".getBytes("UTF-8"));
            byte[] x = digest.digest();
            System.out.println(ByteBuffer.wrap(("ASD".getBytes("UTF-8"))).getLong());
            System.out.println(Arrays.equals(prevHashedBytes,x));
        }catch (Exception e){
        */

    }
}
