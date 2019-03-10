package hello.programmer.alg;

public class ArrayPerform {

    public static void m1(){
        int[][] numbs = new int[10000][10000];
        for (int i = 0; i < numbs.length; i++) {
            for (int j = 0; j < numbs[i].length; j++) {
                numbs[i][j] = i;
            }
        }
    }

    public static void m2(){
        int[][] numbs = new int[10000][10000];
        for (int i = 0; i < numbs.length; i++) {
            for (int j = 0; j < numbs[i].length; j++) {
                numbs[j][i] = j;
            }
        }
    }
    public static void main(String[] args) {
        for (int i=0; i< 10; i++){
            long t0 = System.currentTimeMillis();
            m2();
            System.out.println(System.currentTimeMillis() - t0);
        }

    }
}
