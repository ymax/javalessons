import java.util.HashMap;
import java.util.Stack;

class PushCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (args.length < 2)
            throw new IllegalArgumentException("No argument for PUSH");
        try {
            dataStack.push(new Double(args[1]));
        }
        catch (NumberFormatException e) {
            Double v = defines.get(args[1]);
            if (v == null) {
                throw new IllegalArgumentException("Argument for PUSH is not defined");
            }
            dataStack.push(v);
        }
    }

    @Resource(type = "Stack")
    Stack<Double> dataStack;
    @Resource(type = "Defines")
    HashMap<String, Double> defines;
}

class PopCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for POP");
        dataStack.pop();
    }

    @Resource(type = "Stack")
    Stack<Double> dataStack;
}

class CommentCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        // do nothing
    }
}

class DefineCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (args.length < 3)
            throw new IllegalArgumentException("No argument for DEFINE");
        defines.put(args[1], new Double(args[2]));
    }

    @Resource(type = "Defines")
    HashMap<String, Double> defines;
}

class PrintCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for PRINT");
        System.out.println(dataStack.firstElement());
    }

    @Resource(type = "Stack")
    Stack<Double> dataStack;
}

class AddCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for ADD");
        dataStack.push(dataStack.pop() + dataStack.pop());
    }

    @Resource(type = "Stack")
    Stack<Double> dataStack;
}

class SubCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for SUB");
        dataStack.push(-dataStack.pop() + dataStack.pop());
    }

    @Resource(type = "Stack")
    Stack<Double> dataStack;
}

class MulCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for MUL");
        dataStack.push(dataStack.pop() * dataStack.pop());
    }

    @Resource(type = "Stack")
    Stack<Double> dataStack;
}

class DivCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for DIV");
        dataStack.push(1 / dataStack.pop() * dataStack.pop());
    }

    @Resource(type = "Stack")
    Stack<Double> dataStack;
}

class SqrtCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for SQRT");
        dataStack.push(Math.sqrt(dataStack.pop()));
    }

    @Resource(type = "Stack")
    Stack<Double> dataStack;
}

class Commands {
    {
        cmds = new HashMap<String, CalcCommand>();

        cmds.put("PUSH", new PushCommand());
        cmds.put("POP", new PopCommand());
        cmds.put("#", new CommentCommand());
        cmds.put("DEFINE", new DefineCommand());
        cmds.put("PRINT", new PrintCommand());
        cmds.put("ADD", new AddCommand());
        cmds.put("SUB", new SubCommand());
        cmds.put("MUL", new MulCommand());
        cmds.put("DIV", new DivCommand());
        cmds.put("SQRT", new SqrtCommand());
    }

    public static CalcCommand getCmd(String arg) {
        return cmds.get(arg);
    }

    private static HashMap<String, CalcCommand> cmds;

}

