import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ReqProcessor {

    public void getBytes(String path) {
        ArrayList<String[]> files = new ArrayList<String[]>();
        ArrayList<String> dirs = new ArrayList<String>();
        if (new File(path).getParent() != null) {
            dirs.add("..");
        }
        for (File f: new File(path).listFiles()) {
            if (f.isDirectory()) {
                dirs.add(f.getName());
            }
            else {
                String[] finfo = {f.getName(), new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(
                        new Date(f.lastModified())).toString(), new Long(f.length()).toString()};
                files.add(finfo);
            }
        }
        Collections.sort(files, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });
        Collections.sort(dirs);
    }
}
