import java.util.HashMap;
import java.util.Stack;


public interface CalcCommand {
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines)
            throws IllegalArgumentException;
}
