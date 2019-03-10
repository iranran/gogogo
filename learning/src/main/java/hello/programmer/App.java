package hello.programmer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {

        String s3 = "JournalDev";
        int start = 1;
        char end = 5;
        System.out.println(start + end);
        System.out.println(s3.substring(start, end));

        HashSet shortSet = new HashSet();
        for (short i = 0; i < 100; i++) {
            shortSet.add(i);
            shortSet.remove((i - 1));
        }
        System.out.println(shortSet.size());

        String str = null;
        String str1="abc";
        //System.out.println(str1.equals("abc") | str.equals(null));

        String x = "abc";
        String y = "abc";
        x.concat(y);
        System.out.println(x);

        int k = 10*10-10;
        System.out.println(k);


        try {
            throw new IOException("Hello");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String  base = "string";
        List<String> list = new ArrayList<String>();
        for (int i=0;i< Integer.MAX_VALUE;i++){
            String string = base + base;
            base = string;
            list.add(string.intern());
        }

    }
}
