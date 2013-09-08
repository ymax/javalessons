import java.util.HashMap;
import java.util.Stack;

public interface CalcCommand {
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines);
}
