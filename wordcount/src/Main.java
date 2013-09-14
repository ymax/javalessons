import java.io.*;
import java.util.*;

class WordCounter implements Comparable {
    @Override
    public int compareTo(Object o) {
        return Integer.compare(val, ((WordCounter)o).val);
    }

    public WordCounter(String key, int val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public int getVal() {
        return val;
    }

    private int val;
    private String key;
}


public class Main {
    public static void main(String args[]) {
        HashMap<String, Integer> words = new HashMap<String, Integer>();
        StringBuilder sb = new StringBuilder();
        try {
            Reader r = new InputStreamReader(new BufferedInputStream(new FileInputStream(args[0])));
            for (int ch = r.read(); ch != -1; ch = r.read()) {
                if (Character.isLetterOrDigit(ch)) {
                    sb.append((char)ch);
                }
                else {
                    String s = sb.toString();

                    if (!s.isEmpty()) {
                        Integer count = words.get(s);
                        if (count == null) {
                            count = new Integer(0);
                        }
                        ++count;
                        words.put(s, count);
                    }
                    sb = new StringBuilder();
                }
            }
            r.close();
            processResults(words);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void processResults(HashMap<String, Integer> words) {
        ArrayList<WordCounter> list = new ArrayList<WordCounter>();
        int total = 0;
        for (Iterator<Map.Entry<String, Integer>> it = words.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Integer> e = it.next();
            list.add(new WordCounter(e.getKey(), e.getValue()));
            total += e.getValue();
        }

        Collections.sort(list);
        Collections.reverse(list);

        try {
            Writer fw = new FileWriter("res.csv");
            for (WordCounter wcount: list) {
                fw.write(wcount.getKey() + "," + wcount.getVal() + "," + (wcount.getVal() / total * 100)+"\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
