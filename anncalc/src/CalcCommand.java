import java.util.HashMap;
import java.util.Stack;


public interface CalcCommand {
    public void execute(String[] args)
            throws IllegalArgumentException;
}
