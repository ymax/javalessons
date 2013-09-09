import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;


public class Main3 {
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
        cmds.put("PUSH", new CalcCommand() {
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
        });
        cmds.put("POP", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                if (dataStack.size() < 1)
                    throw new IllegalArgumentException("No argument for POP");
                dataStack.pop();
            }
        });
        cmds.put("#", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                // do nothing
            }
        });
        cmds.put("DEFINE", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                if (args.length < 3)
                    throw new IllegalArgumentException("No argument for DEFINE");
                defines.put(args[1], new Double(args[2]));
            }
        });
        cmds.put("PRINT", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                if (dataStack.size() < 1)
                    throw new IllegalArgumentException("No argument for PRINT");
                System.out.println(dataStack.firstElement());
            }
        });
        cmds.put("ADD", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                if (dataStack.size() < 2)
                    throw new IllegalArgumentException("No argument for ADD");
                dataStack.push(dataStack.pop() + dataStack.pop());
            }
        });
        cmds.put("SUB", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                if (dataStack.size() < 2)
                    throw new IllegalArgumentException("No argument for SUB");
                dataStack.push(-dataStack.pop() + dataStack.pop());
            }
        });
        cmds.put("MUL", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                if (dataStack.size() < 2)
                    throw new IllegalArgumentException("No argument for MUL");
                dataStack.push(dataStack.pop() * dataStack.pop());
            }
        });
        cmds.put("DIV", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                if (dataStack.size() < 2)
                    throw new IllegalArgumentException("No argument for DIV");
                dataStack.push(1 / dataStack.pop() * dataStack.pop());
            }
        });
        cmds.put("SQRT", new CalcCommand() {
            @Override
            public void execute(String[] args, Stack<Double> dataStack, HashMap<String, Double> defines) throws IllegalArgumentException {
                if (dataStack.size() < 1)
                    throw new IllegalArgumentException("No argument for SQRT");
                dataStack.push(Math.sqrt(dataStack.pop()));
            }
        });
    }
}
