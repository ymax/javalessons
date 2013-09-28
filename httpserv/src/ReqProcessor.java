import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.*;
import java.security.URIParameter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Pattern;

public class ReqProcessor implements Runnable {

    ReqProcessor(Socket sock) {
        super();
        socket = sock;
    }

    @Override
    public void run() {
        try {
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            try {
                BufferedReader br = new BufferedReader(isr);
                String []args = br.readLine().split(" ");
                String cmd = args[0].trim().toUpperCase();

                if (cmd.equals("GET")) {
                    processGet(os, args);
                }
                else {
                    respondData(os, 501, "Not Implemented", null, null);
                }
            }
            finally {
                isr.close();
                os.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processGet(OutputStream os, String[] args) throws IOException {
        String[] req = args[1].substring(1).split(Pattern.quote("/"));
        if (args[1].substring(args[1].length() - 1).equals("/")) {
            String[] req2 = new String[req.length + 1];
            int i;
            for (i = 0; i < req.length; ++i) {
                req2[i] = req[i];
            }
            req2[i] = "";
            req = req2;
        }
        String s = URLDecoder.decode(req[0], "UTF-8");
        for (int i = 1; i < req.length; ++i) {
            if (i == (req.length - 1) || !req[i].equals("")) {
                s = s + File.separator + URLDecoder.decode(req[i], "UTF-8");
            }
        }
        String path = URLDecoder.decode(args[1], "ASCII");
        if (s.equals("")) {
            s = new File(".").getCanonicalPath();
        }
        path = s;//path.replace("/", File.separator);
        File f = new File(path);
        if (f.exists()) {
            if (f.isDirectory()) {
                ArrayList<String[]> cont = dirContents(f);
                StringBuilder sb = new StringBuilder();
                sb.append("<html><meta http-equiv=Content-Type content=\"text/html;charset=UTF-8\">" +
                        "<title>Directory</title>" +
                        "<body>" +
                        "<table rules=\"cols\" cellpadding=\"10\">" +
                        "<tr><td><b>Name</b></td><td><b>Modified</b></td><td><b>Size</b></td></tr>");
                for (String[] finfo: cont) {
//                    String s2 = "";
                    String[] sx = finfo[1].split(Pattern.quote(File.separator));
                    if (finfo[1].substring(finfo[1].length() - 1).equals(File.separator)) {
                        String[] sx2 = new String[sx.length + 1];
                        int i;
                        for (i = 0; i < sx.length; ++i) {
                            sx2[i] = sx[i];
                        }
                        sx2[i] = "";
                        sx = sx2;
                    }
                    String s2 = "/" + URLEncoder.encode(sx[0], "UTF-8");
                    for (int i = 1; i < sx.length; ++i) {
                        if (i == (sx.length - 1) || !sx[i].equals("")) {
                            s2 = s2 + "/" + URLEncoder.encode(sx[i], "UTF-8");
                        }
                    }
//                    for (String s1: finfo[1].split(File.separator)) {
//                        s2 = s2 + "/" + URLEncoder.encode(s1, "ASCII");
//                    }
                    sb.append("<tr><td><a href=\"" + s2 + "\">" + finfo[0] + "</a></td><td>" + finfo[2] + "</td><td>" + finfo[3] + "</td></tr>");
                    System.out.println(finfo[0]);
                }
                sb.append("</table>" +
                        "</body>" +
                        "</html>");
                respondData(os, 200, "OK", "text/html; charset=utf-8", sb.toString().getBytes("UTF-8"));
            }
            else {
                respondFile(os, f);
            }
        }
        else
        {
            respondData(os, 404, "Not Found", null, null);
        }
    }

    private void respondData(OutputStream os, int code, String cause, String cont_type, byte[] cont) throws IOException {
        String resp = "HTTP/1.0 " + code + " " + cause + "\r\n";
        if (cont_type != null && cont.length != 0) {
            resp +=
                    "Content-Type: " + cont_type + "\r\n" +
                    "Content-Length: " + cont.length + "\r\n" +
                    "Connection: close\r\n" +
                    "Cache-Control: no-cache,no-store\r\n" +
                    "\r\n";
        }
        os.write(resp.getBytes("UTF-8"));
        os.write(cont);
        os.flush();
    }

    private void respondFile(OutputStream os, File f) throws IOException {
        String resp =
                "HTTP/1.0 200 OK\r\n" +
                "Content-Type: " + new MimetypesFileTypeMap().getContentType(f) + "\r\n" +
                "Content-Length: " + f.length() + "\r\n" +
//                "Content-Disposition: attachment; filename=\"" + f.getName() + "\"\r\n" +
                "Connection: close\r\n" +
                "Cache-Control: no-cache,no-store\r\n" +
                "\r\n";
        os.write(resp.getBytes("UTF-8"));
        byte[] buf = new byte[1000000];
        FileInputStream fis = new FileInputStream(f);
        int len;
        while((len = fis.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
        os.flush();
    }

    private ArrayList<String[]> dirContents(File path) throws IOException {
        ArrayList<String[]> files = new ArrayList<String[]>();
        ArrayList<String[]> dirs = new ArrayList<String[]>();
        for (File f: path.listFiles()) {
            if (f.isDirectory()) {
                dirs.add(new String[] {f.getName(), f.getCanonicalPath(), "", ""});
            }
            else {
                files.add(new String[] {f.getName(), f.getCanonicalPath(), new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(
                        new Date(f.lastModified())), Long.toString(f.length())});
            }
        }
        Collections.sort(files, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });
        Collections.sort(dirs, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });
        if (path.getParent() != null) {
            dirs.add(0, new String[] {"..", new File(path.getParent()).getCanonicalPath(), "", ""});
        }
        dirs.addAll(files);
        return dirs;
    }

    private Socket socket;
}
