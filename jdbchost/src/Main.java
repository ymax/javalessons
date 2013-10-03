import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Макс
 * Date: 03.10.13
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        GuestBookController gbc = new MyGuestBookController();
        while (true) {
            String cmd = new BufferedReader(new InputStreamReader(System.in)).readLine();
            String[] cmdp = cmd.split(" ", 2);
            if (cmdp[0].equals("x"))
                break;
            if (cmdp[0].equals("a") && cmdp.length == 2) {
                gbc.addRecord(cmdp[1]);
                continue;
            }
            if (cmdp[0].equals("l")) {
                for (Record r: gbc.getRecords()) {
                    System.out.println("[" + r.getId() + " " + r.getDate() +"] " + r.getMessage());
                }
            }
        }

    }
}
