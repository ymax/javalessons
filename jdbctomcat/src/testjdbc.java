import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 04.10.13
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "Testjdbc", urlPatterns = "/test")
public class Testjdbc extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg = request.getParameter("message");
        msg = msg.trim();
        if (!msg.equals(""))
        {
            try {
                gbc.addRecord(msg);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (gbc == null) {
            response.getOutputStream().println("Database connection error");
        }
        else {
            request.setAttribute("posts", gbc.getRecords());
            System.out.println("before forward");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    static {
        try {
            gbc =  new MyGuestBookController();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static GuestBookController gbc;
}
