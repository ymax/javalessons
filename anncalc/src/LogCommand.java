import java.util.Stack;

public class LogCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for LOG");
        dataStack.push(Math.log(dataStack.pop()));
    }
    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
}
