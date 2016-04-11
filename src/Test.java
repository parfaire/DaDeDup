
import java.util.HashMap;
import java.util.Map;

public class Test {
    private static String PATH = "/Users/parfaire/IdeaProjects/DataDeduplication/";
    private static String READPATH = "/Users/parfaire/IdeaProjects/DataDeduplication/READ/";
    private static String out = READPATH+"input.txt";
    private static String in = PATH+"input.txt";
    private static String storage = PATH+"storage.txt";
    private static String ddtfile = PATH+"ddt.txt";
    private static String initialfile = PATH+"init.txt";
    private static String hashFunc = "SHA-256";
    private static int blockSize = 32; //32 bytes = 256bits

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        HashMap<Long,Data> ddt;
        HashMap<String,Long> init;
        try{
            ddt =  (HashMap<Long,Data>) Interface.loadObj(ddtfile);
        }catch (Exception e){
            System.out.println(" - ddt is empty.");
            ddt = new HashMap<>();
        }
        try{
            init = (HashMap<String,Long>) Interface.loadObj(initialfile);
        }catch (Exception e){
            System.out.println(" - init is empty.");
            init = new HashMap<>();
        }

        //PRINT HASHMAP
//        System.out.println(showHM(ddt));
//        System.out.println(showHM(init));
//
//        Interface.read(in,storage,ddt,init,hashFunc,blockSize);
        Interface.write(out,storage,ddt,init);
//
//        Interface.saveObj(ddtfile,ddt);
//        Interface.saveObj(initialfile,init);







        /* TEST CASE OF getBytes() THAT INDICATE THE INCONSISTENCY
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

    static String showHM(HashMap<?,?> hm){
        String s;
        s="\n##### PRINTING HASHMAP #####\n";
        for(Map.Entry<?, ?> entry : hm.entrySet()) {
            s+=entry.getKey()+"____"+entry.getValue()+"\n";
        }
        s+="##### ================ #####\n";
        return s;
    }
}
