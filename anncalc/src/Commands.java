import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
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

    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
    @Resource(ResourceType.DEFINES)
    HashMap<String, Double> defines;
}

class PopCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for POP");
        dataStack.pop();
    }

    @Resource(ResourceType.STACK)
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

    @Resource(ResourceType.DEFINES)
    HashMap<String, Double> defines;
}

class PrintCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for PRINT");
        System.out.println(dataStack.firstElement());
    }

    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
}

class AddCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for ADD");
        dataStack.push(dataStack.pop() + dataStack.pop());
    }

    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
}

class SubCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for SUB");
        dataStack.push(-dataStack.pop() + dataStack.pop());
    }

    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
}

class MulCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for MUL");
        dataStack.push(dataStack.pop() * dataStack.pop());
    }

    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
}

class DivCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for DIV");
        dataStack.push(1 / dataStack.pop() * dataStack.pop());
    }

    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
}

class SqrtCommand implements CalcCommand {
    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for SQRT");
        dataStack.push(Math.sqrt(dataStack.pop()));
    }

    @Resource(ResourceType.STACK)
    Stack<Double> dataStack;
}

class Commands {

    private static HashMap<String, CalcCommand> cmds;

    static {
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

        /** read additional commands */
        Properties props = new Properties();
        InputStream f = Commands.class.getResourceAsStream("cmd_add.properties");
        try {
            props.load(f);
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Object key: props.keySet()) {
            String newName = props.getProperty((String)key);
            try {
                cmds.put((String)key, (CalcCommand)Class.forName(newName).newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static CalcCommand getCmd(String arg) {
        return cmds.get(arg);
    }
}

