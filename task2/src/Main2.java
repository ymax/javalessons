import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

class PushCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
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
}

class PopCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for POP");
        dataStack.pop();
    }
}

class CommentCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        // do nothing
    }
}

class DefineCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        if (args.length < 3)
            throw new IllegalArgumentException("No argument for DEFINE");
        defines.put(args[1], new Double(args[2]));
    }
}

class PrintCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for PRINT");
        System.out.println(dataStack.firstElement());
    }
}

class AddCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for ADD");
        dataStack.push(dataStack.pop() + dataStack.pop());
    }
}

class SubCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for SUB");
        dataStack.push(-dataStack.pop() + dataStack.pop());
    }
}

class MulCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for MUL");
        dataStack.push(dataStack.pop() * dataStack.pop());
    }
}

class DivCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        if (dataStack.size() < 2)
            throw new IllegalArgumentException("No argument for DIV");
        dataStack.push(1 / dataStack.pop() * dataStack.pop());
    }
}

class SqrtCommand implements CalcCommand {
    @Override
    public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
        if (dataStack.size() < 1)
            throw new IllegalArgumentException("No argument for SQRT");
        dataStack.push(Math.sqrt(dataStack.pop()));
    }
}


public class Main2 {
    public static void main(String args[]) {
        Stack<Double> dataStack = new Stack<Double>();
        HashMap<String, Double> defines = new HashMap<String, Double>();

        HashMap<String, CalcCommand> cmds = new HashMap<String, CalcCommand>();

        initCommands(cmds);

        try {
            Scanner sc = new Scanner(new File(args[0]));
            try {
                while (sc.hasNextLine()) {
                    String s = sc.nextLine();
                    String []cmdArgs = s.split(" ");
                    if (cmdArgs.length > 0) {
                        /** разбивка по \n поэтому нужно этот \n отрезать
                         * у последнего элемента */
                        cmdArgs[cmdArgs.length - 1] = cmdArgs[cmdArgs.length - 1].trim();
                        cmds.get(cmdArgs[0]).execute(cmdArgs, dataStack, defines);
                    }
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            sc.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void initCommands(HashMap<String, CalcCommand> cmds) {
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
}
