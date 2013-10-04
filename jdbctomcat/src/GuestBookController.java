import java.io.Closeable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public interface GuestBookController extends Closeable {
    void addRecord(String message) throws SQLException;
    List<Record> getRecords();
}
