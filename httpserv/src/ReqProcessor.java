import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import org.apache.commons.io.IOUtils;

public class ReqProcessor extends Thread {

    ReqProcessor(Socket sock) {
        super();
        socket = sock;
        start();
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
                respond(os, 500, "Internal Server Error", "text/html",
                        ("<html><title>Error</title><body>" + e.getLocalizedMessage() + "</body></html>").getBytes());
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
        String url = "";
        for (int i = 1; i < args.length - 1; ++i) {
            url = url + " " + args[i];
        }
        File f = new File("C:" + url);
        if (f.isDirectory()) {
            ArrayList<String[]> cont = contents(f);
            StringBuilder sb = new StringBuilder();
            sb.append("<html>" +
                    "<title>Directory</title>" +
                    "<body>" +
                    "<table rules=\"cols\" cellpadding=\"10\">" +
                    "<tr><td><b>Name</b></td><td><b>Modified</b></td><td><b>Size</b></td></tr>");
            for (String[] finfo: cont) {
                sb.append("<tr><td><a href=\"" + url + finfo[0] + "\">" + finfo[0] + "</a></td><td>" + finfo[1] + "</td><td>" + finfo[2] + "</td></tr>");
            }
            sb.append("</table>" +
                    "</body>" +
                    "</html>");
            respond(os, 200, "OK", "text/html", sb.toString().getBytes());
        }
        else {
            respond(os, f);
        }
        respond(os, 501, "Not Implemented", null, null);
    }

    private void processHead(OutputStream os, String[] args) throws IOException {
        respond(os, 501, "Not Implemented", null, null);
    }

    private void respond(OutputStream os, int code, String cause, String cont_type, byte[] cont) throws IOException {
        os.write(("HTTP/1.0 " + code + " " + cause + "\r\n").getBytes());
        if (cont_type != null && cont.length != 0) {
            os.write(("Content-Type: " + cont_type + "\r\n").getBytes());
            os.write(("Content-Length: " + cont.length + "\r\n").getBytes());
            os.write(("Connection: close\r\n").getBytes());
            os.write(("Cache-Control: no-cache,no-store\r\n").getBytes());
            os.write(("\r\n").getBytes());
            os.write(cont);
            os.flush();
        }
    }

    private void respond(OutputStream os, File f) throws IOException {
        os.write(("HTTP/1.0 200 OK\r\n").getBytes());
        if (f != null) {
            os.write(("Content-Type: " + new MimetypesFileTypeMap().getContentType(f) + "\r\n").getBytes());
            os.write(("Content-Length: " + f.length() + "\r\n").getBytes());
            os.write(("Connection: close\r\n").getBytes());
            os.write(("Cache-Control: no-cache,no-store").getBytes());
            os.write(("\r\n").getBytes());
            IOUtils.copy(new FileInputStream(f), os);
            os.flush();
        }
    }

    private ArrayList<String[]> contents(File path) {
        ArrayList<String[]> files = new ArrayList<String[]>();
        ArrayList<String[]> dirs = new ArrayList<String[]>();
        for (File f: path.listFiles()) {
            if (f.isDirectory()) {
                dirs.add(new String[] {f.getName(), "", ""});
            }
            else {
                files.add(new String[] {f.getName(), new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(
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
            dirs.add(0, new String[] {"..", "", ""});
        }
        dirs.addAll(files);
        return dirs;
    }

    private Socket socket;
}
