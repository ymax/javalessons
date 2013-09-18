import java.io.File;
import java.util.ArrayList;

public class ReqProcessor {

    public char[] getBytes(String path) {
        ArrayList<String[]> files = new ArrayList<String[]>();
        ArrayList<String> dirs = new ArrayList<String>();
        for (File f: new File(path).listFiles()) {
            if (f.isDirectory()) {
                dirs.add(f.getName());
            }
            else {
                String[] finfo = {f.getName(), f.lastModified(), f.length()};
                files.add(finfo);
            }
        }
    }
}
