import javafx.util.Pair;

import java.io.*;
import java.util.*;

class WordCounter implements Comparable {
    @Override
    public int compareTo(Object o) {
        return Integer.compare(val, ((WordCounter)o).val);
    }

    public WordCounter(int val) {
        this.val = val;
    }

    public void inc() {
        ++val;
    }

    public int getVal() {
        return val;
    }

    private int val;
}


public class Main {
    public static void main(String args[]) {
        HashMap<String, WordCounter> words = new HashMap<String, WordCounter>();
        StringBuilder sb = new StringBuilder();
        try {
            Reader r = new InputStreamReader(new BufferedInputStream(new FileInputStream(args[0])));
            for (int ch = r.read(); ch != -1; ch = r.read()) {
                if (Character.isLetterOrDigit(ch)) {
                    sb.append(ch);
                }
                else {
                    String s = sb.toString();

                    if (!s.isEmpty()) {
                        WordCounter count = words.get(s);
                        if (count == null) {
                            count = new WordCounter(0);
                        }
                        count.inc();
                    }
                }
            }
            r.close();
            processResults(words);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void processResults(HashMap<String, WordCounter> words) {
        ArrayList<Map.Entry<String, WordCounter>> list = new ArrayList<Map.Entry<String, WordCounter>>();
        for (Iterator<Map.Entry<String, WordCounter>> it = words.entrySet().iterator(); it.hasNext();) {
            list.add(it.next());
        }

        Collections.sort();

    }
}
