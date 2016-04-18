
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private String PATH = "/Users/parfaire/IdeaProjects/DataDeduplication/dd/";
    private String storage = PATH+"storage.txt";
    private String ddtfile = PATH+"ddt.txt";
    private String initialfile = PATH+"init.txt";
    private String hashFunc = "SHA-256";
    private int blockSize = 4096;
	private MainWindow mainWindow;
    private HashMap<ByteWrapper, Data> ddt;
    private HashMap<String, ByteWrapper> init;
	
	public Controller(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
        try{ddt = (HashMap<ByteWrapper,Data>) Interface.loadObj(ddtfile);} catch(Exception e){ ddt = new HashMap<>();}
        try{init = (HashMap<String,ByteWrapper>) Interface.loadObj(initialfile);} catch(Exception e){init = new HashMap<>();}
        refreshStatus();
	}
	
	public void refreshStatus() {
        Long d = new File(ddtfile).length();
        Long i = new File(initialfile).length();
        Long s = new File(storage).length();
        Long tot = d+i+s;
		mainWindow.getStatusPanel().setLblDdt(d);
        mainWindow.getStatusPanel().setLblInit(i);
        mainWindow.getStatusPanel().setLblStorage(s);
        mainWindow.getStatusPanel().setLblTotal(tot);
        mainWindow.getStatusPanel().setTfDdt(ddtfile);
        mainWindow.getStatusPanel().setTfInit(initialfile);
        mainWindow.getStatusPanel().setTfStorage(storage);
        mainWindow.getStatusPanel().updateUI();
        setList();
	}

    public void updateStatus() {
        Interface.saveObj(ddtfile,ddt);
        Interface.saveObj(initialfile,init);
        refreshStatus();
    }

    public void read(String input){
        Interface.read(input,storage,ddt,init);
    }

    public void write(String output){
        Interface.write(output,storage,ddt,init,hashFunc,blockSize);
        updateStatus();
    }

    public void setList(){
        String[] filenames = new String[init.size()];
        int i=0;
        for(String name : init.keySet()) {
            filenames[i++] = name;
        }
        mainWindow.getListPanel().getList().setListData(filenames);
    }

    public static String showHM(HashMap<?,?> hm) {
        String s;
        s = "\n##### PRINTING HASHMAP #####\n";
        for (Map.Entry<?, ?> entry : hm.entrySet()) {
            s += entry.getKey() + "____" + entry.getValue() + "\n";
        }
        s += "##### ================ #####\n";
        return s;
    }

    public String getddt(){
        return showHM(ddt);
    }

    public String getinit(){
        return showHM(init);
    }

    public void clearData(){
        System.out.println("Data is cleared");
        try{
            PrintWriter pw = new PrintWriter(initialfile);
            pw.close();
            pw = new PrintWriter(ddtfile);
            pw.close();
            pw = new PrintWriter(storage);
            pw.close();
            ddt = new HashMap<>();
            init = new HashMap<>();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
