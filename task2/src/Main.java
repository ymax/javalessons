import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String args[]) {
        Stack<Double> dataStack = new Stack<Double>();
        HashMap<String, Double> defines = new HashMap<String, Double>();

        try {
            Scanner sc = new Scanner(new File(args[0]));
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String []cmdArgs = s.split(" ");
                if (cmdArgs.length > 0) {
                    /** разбивка по \n поэтому нужно этот \n отрезать
                     * у последнего элемента */
                    cmdArgs[cmdArgs.length - 1] = cmdArgs[cmdArgs.length - 1].trim();
                    if (cmdArgs[0].equals("PUSH")) {
                        if (cmdArgs.length < 2)
                            throw new IllegalArgumentException("No argument for PUSH");
                        try {
                            dataStack.push(new Double(cmdArgs[1]));
                        }
                        catch (NumberFormatException e) {
                            Double v = defines.get(cmdArgs[1]);
                            if (v != null) {
                                dataStack.push(v);
                            }
                        }
                    }
                    else if (cmdArgs[0].equals("POP")) {
                        if (dataStack.size() < 1)
                            throw new IllegalArgumentException("No argument for POP");
                        dataStack.pop();
                    }
                    else if (cmdArgs[0].equals("#")) {
                        // do nothing
                    }
                    else if (cmdArgs[0].equals("DEFINE")) {
                        if (cmdArgs.length < 3)
                            throw new IllegalArgumentException("No argument for DEFINE");
                        defines.put(cmdArgs[1], new Double(cmdArgs[2]));
                    }
                    else if (cmdArgs[0].equals("PRINT")) {
                        if (dataStack.size() < 1)
                            throw new IllegalArgumentException("No argument for PRINT");
                        System.out.println(dataStack.firstElement());
                    }
                    else if (cmdArgs[0].equals("ADD")) {
                        if (dataStack.size() < 2)
                            throw new IllegalArgumentException("No argument for ADD");
                        dataStack.push(dataStack.pop() + dataStack.pop());
                    }
                    else if (cmdArgs[0].equals("SUB")) {
                        if (dataStack.size() < 2)
                            throw new IllegalArgumentException("No argument for SUB");
                        dataStack.push(-dataStack.pop() + dataStack.pop());
                    }
                    else if (cmdArgs[0].equals("MUL")) {
                        if (dataStack.size() < 2)
                            throw new IllegalArgumentException("No argument for MUL");
                        dataStack.push(dataStack.pop() * dataStack.pop());
                    }
                    else if (cmdArgs[0].equals("DIV")) {
                        if (dataStack.size() < 2)
                            throw new IllegalArgumentException("No argument for DIV");
                        dataStack.push(1 / dataStack.pop() * dataStack.pop());
                    }
                    else if (cmdArgs[0].equals("SQRT")) {
                        if (dataStack.size() < 1)
                            throw new IllegalArgumentException("No argument for SQRT");
                        dataStack.push(Math.sqrt(dataStack.pop()));
                    }
                }
            }
            sc.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
