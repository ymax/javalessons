package jdbchost;

import java.io.Closeable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

class Record {
    Record(BigDecimal id, Date date, String message) {
        this.id = id;
        this.date = date;
        this.message = message;
    }

    BigDecimal getId() {
        return id;
    }

    void setId(BigDecimal id) {
        this.id = id;
    }

    Date getDate() {
        return date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    private BigDecimal id;
    private Date date;
    private String message;
}


public interface GuestBookController extends Closeable {
    void addRecord(String message);
    List<Record> getRecords();
}
