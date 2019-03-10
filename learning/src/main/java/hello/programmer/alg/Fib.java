package hello.programmer.alg;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Fib {

    static long counter = 0;

    static Map<Integer, BigInteger> store = new HashMap<>();

//    public static long fib(int n){
//        System.err.println(n);
//        counter++;
//        if (n == 0) return 0;
//        if (n == 1) return 1;
//        return fib(n-1) + fib(n-2);
//    }

    public static BigInteger fib2(Integer n){
        counter++;
        if (store.containsKey(n)) {
            return store.get(n);
        }
        if (n == 0) {
            store.put(0, new BigInteger("0"));
            return new BigInteger("0");
        }
        if (n == 1){
            store.put(1, new BigInteger("1"));
            return new BigInteger("1");
        }
        BigInteger result = fib2(n-1).add(fib2(n-2));
        store.put(n, result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new BigInteger("1000").add(new BigInteger("1000")));
        long t0 = System.currentTimeMillis();
        for (int i = 0; i<100; i++) {
            long t00 = System.currentTimeMillis();
            long temp = counter;
            if (System.currentTimeMillis() - t0 > 60000) {
                System.out.println("game over!"+i+" counter="+counter + " consumer="+(System.currentTimeMillis() - t0));
                break;
            }
            BigInteger v = fib2(i);
            System.out.println(i+" v="+ v +" counter="+(counter - temp) + " consumer="+(System.currentTimeMillis() - t00));
        }
    }
}
