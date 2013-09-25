import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ReqProcessor implements Runnable {

    ReqProcessor(Socket sock) {
        super();
        socket = sock;
    }

    @Override
    public void run() {
        try {
            OutputStream os = socket.getOutputStream();
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            try {
                BufferedReader br = new BufferedReader(isr);
                String []args = br.readLine().split(" ");
                String cmd = args[0].trim().toUpperCase();

                if (cmd.equals("GET")) {
                    processGet(os, args);
                }
                else if (cmd.equals("HEAD")) {
                    processHead(os, args);
                }
                else {
                    respond(os, 501, "Not Implemented", null, null);
                }
            }
            catch (IOException e) {
                respond(os, 500, "Internal Server Error", "text/html;encoding=UTF-8",
                        ("<html><title>Error</title><body>" + e.getLocalizedMessage() + "</body></html>").getBytes("UTF-8"));
            }
            finally {
                os.close();
                isr.close();
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
        String path = URLDecoder.decode(args[1], "ASCII");
        path = path.replace("/", File.separator);
        File f = new File(path);
        if (f.exists()) {
            if (f.isDirectory()) {
                ArrayList<String[]> cont = contents(f);
                StringBuilder sb = new StringBuilder();
                sb.append("<html>" +
                        "<title>Directory</title>" +
                        "<body>" +
                        "<table rules=\"cols\" cellpadding=\"10\">" +
                        "<tr><td><b>Name</b></td><td><b>Modified</b></td><td><b>Size</b></td></tr>");
                for (String[] finfo: cont) {
                    sb.append("<tr><td><a href=\"" + URLEncoder.encode(finfo[1].replace(File.separator, "/"), "UTF-8") + "\">" + finfo[0] + "</a></td><td>" + finfo[2] + "</td><td>" + finfo[3] + "</td></tr>");
                }
                sb.append("</table>" +
                        "</body>" +
                        "</html>");
                respond(os, 200, "OK", "text/html;encoding=UTF-8", sb.toString().getBytes("UTF-8"));
            }
            else {
                respond(os, f);
            }
        }
        else
        {
            respond(os, 404, "Not Found", null, null);
        }
    }

    private void processHead(OutputStream os, String[] args) throws IOException {
        respond(os, 501, "Not Implemented", null, null);
    }

    private void respond(OutputStream os, int code, String cause, String cont_type, byte[] cont) throws IOException {
        os.write(("HTTP/1.0 " + code + " " + cause + "\r\n").getBytes("UTF-8"));
        if (cont_type != null && cont.length != 0) {
            os.write(("Content-Type: " + cont_type + "\r\n").getBytes("UTF-8"));
            os.write(("Content-Length: " + cont.length + "\r\n").getBytes("UTF-8"));
            os.write(("Connection: close\r\n").getBytes("UTF-8"));
            os.write(("Cache-Control: no-cache,no-store\r\n").getBytes("UTF-8"));
            os.write(("\r\n").getBytes("UTF-8"));
            os.write(cont);
            os.flush();
        }
    }

    private void respond(OutputStream os, File f) throws IOException {
        os.write(("HTTP/1.0 200 OK\r\n").getBytes("UTF-8"));
        os.write(("Content-Type: " + new MimetypesFileTypeMap().getContentType(f) + "\r\n").getBytes("UTF-8"));
        os.write(("Content-Length: " + f.length() + "\r\n").getBytes("UTF-8"));
        os.write(("Connection: close\r\n").getBytes("UTF-8"));
        os.write(("Cache-Control: no-cache,no-store").getBytes("UTF-8"));
        os.write(("\r\n").getBytes("UTF-8"));
        byte[] buf = new byte[200];
        FileInputStream fis = new FileInputStream(f);
        int len;
        while((len = fis.read(buf)) != -1) {
             os.write(buf, 0, len);
        }
        os.flush();
    }

    private ArrayList<String[]> contents(File path) throws IOException {
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
