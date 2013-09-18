import java.util.Stack;

public class ExpCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for EXP");
        dataStack.push(Math.exp(dataStack.pop()));
    }
    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
}
