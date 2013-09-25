import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServ {
    HttpServ() {
        try {
            serverSocket = new ServerSocket(8888);
            serverSocket.setReuseAddress(true);
        }
        catch (IOException e) {
            System.out.println("Could not listen on port: 8888");
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (true) {
            try {
                Socket s = serverSocket.accept();
                Thread t = new Thread(new ReqProcessor(s));
                t.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ServerSocket serverSocket;
}
