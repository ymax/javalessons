import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class MyGuestBookController implements GuestBookController {

    MyGuestBookController() throws ClassNotFoundException, SQLException {
        super();
        idx = new BigDecimal(0);
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "user", "password");
        connection.createStatement().execute(
                "CREATE TABLE testTable (" +
                "ID INTEGER NOT NULL," +
                "postDate DATETIME," +
                "postMessage TEXT," +
                "PRIMARY KEY(ID))");
    }

    @Override
    public void addRecord(String message) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
                    "INSERT INTO testTable " +
                    "(ID, postDate, postMessage) " +
                    "VALUES(?,?,?)");
            ps.setBigDecimal(1, (idx = idx.add(new BigDecimal(1))));
            ps.setTimestamp(2, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
            ps.setString(3, message);
            ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public List<Record> getRecords() {
        List<Record> rl = new ArrayList<Record>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT " +
                    "ID as id1, " +
                    "postDate as date1, " +
                    "postMessage as msg1 " +
                    "FROM testTable");
            while (resultSet.next()) {
                rl.add(new Record(resultSet.getBigDecimal("id1"), resultSet.getTimestamp("date1"), resultSet.getString("msg1")));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return rl;
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Connection connection;
    private BigDecimal idx;
}
