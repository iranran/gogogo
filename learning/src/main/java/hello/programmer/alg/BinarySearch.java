package hello.programmer.alg;

public class BinarySearch {

    private static int counter = 0;
    public static void main(String[] args) {
        int numb[] = new int[200_000];
        for (int i=0; i<200_000; i++) {
            numb[i] = i;
        }
        long t0 = System.nanoTime();
        for(int i=0;i<1; i++){
            int index = binarySearch(numb, 40000);

        }
        System.out.println(System.nanoTime() - t0);
        //int index = search(numb, 3000);
       // System.out.println("counter="+ counter);


    }

    public static int binarySearch(int nums[], int target){
        int right = nums.length - 1;
        int left = 0;
        int mid = (left + right)/2;
        while (left < mid){
            //++counter;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] > target) {
                right = mid;
            } else {
                left = mid;
            }
            mid = (left + right)/2;
        }
        return -1;
    }

    public static int search(int nums[], int target){
        int len = nums.length;
        for (int i = 0; i<len; i++){
            //++counter;
            if (nums[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
