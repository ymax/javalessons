import java.io.IOException;
import java.net.ServerSocket;

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
                new ReqProcessor(serverSocket.accept());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ServerSocket serverSocket;
}
