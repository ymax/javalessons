import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;


public class Main {
    public static void main(String args[]) {
        Stack<Double> dataStk = new Stack<Double>();
        HashMap<String, Double> defs = new HashMap<String, Double>();


        try {
            Scanner sc = new Scanner(new File(args[0]));
            try {
                while (sc.hasNextLine()) {
                    String s = sc.nextLine();
                    String []cmdArgs = s.split(" ");
                    if (cmdArgs.length > 0) {
                        CalcCommand cmd = Commands.getCmd(cmdArgs[0]);
                        if (cmd != null) {
                            for (Field field: cmd.getClass().getDeclaredFields()) {
                                Annotation ann = field.getAnnotation(Resource.class);
                                if (ann != null) {
                                    try {
                                        if (ResourceType.STACK.equals(((Resource)ann).value())) {
                                            field.set(cmd, dataStk);
                                        }
                                        else if (ResourceType.DEFINES.equals(((Resource)ann).value())) {
                                            field.set(cmd, defs);
                                        }
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                    }
                                }
                            }
                            cmd.execute(cmdArgs);
                        }
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

}
