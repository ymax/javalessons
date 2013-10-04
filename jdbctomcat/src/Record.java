import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 04.10.13
 * Time: 20:30
 * To change this template use File | Settings | File Templates.
 */
public class Record {
    public Record(BigDecimal id, Date date, String message) {
        this.id = id;
        this.date = date;
        this.message = message;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private BigDecimal id;
    private Date date;
    private String message;
}
