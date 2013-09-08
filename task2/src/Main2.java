import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

class PushCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        try {
            dataStack.push(new Double(args[1]));
        }
        catch (NumberFormatException e) {
            Double v = defines.get(args[1]);
            if (v != null) {
                dataStack.push(v);
            }
        }
    }
}

class PopCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        dataStack.pop();
    }
}

class CommentCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        // do nothing
    }
}

class DefineCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        defines.put(args[1], new Double(args[2]));
    }
}

class PrintCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        System.out.println(dataStack.firstElement());
    }
}

class AddCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        dataStack.push(dataStack.pop() + dataStack.pop());
    }
}

class SubCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        dataStack.push(-dataStack.pop() + dataStack.pop());
    }
}

class MulCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        dataStack.push(dataStack.pop() * dataStack.pop());
    }
}

class DivCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
        dataStack.push(1 / dataStack.pop() * dataStack.pop());
    }
}

class SqrtCommand implements CalcCommand {
    @Override
    public void Execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) {
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
            sc.useDelimiter("\n");
            while (sc.hasNext()) {
                String s = sc.next();
                String []cmdArgs = s.split(" ");
                if (cmdArgs.length > 0) {
                    /** разбивка по \n поэтому нужно этот \n отрезать
                     * у последнего элемента */
                    cmdArgs[cmdArgs.length - 1] = cmdArgs[cmdArgs.length - 1].trim();
                    cmds.get(cmdArgs[0]).Execute(cmdArgs, dataStack, defines);
                }
            }
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
