package hello.programmer.alg;

/**
 * @Description: 十大经典排序算法（动图演示） https://www.cnblogs.com/onepixel/p/7674659.html
 * @Date: 2019/2/3 下午8:54
 * @Author: liwei
 * @Copyright（C）: 2018 BlueHelix Inc. All rights reserved.
 */
public class SortedAlgra {

    public static void main(String[] args) {
        int[] arr = {6,3,8,2,9,1};

        //排序前；
        System.out.println("原数组：");
        printArray(arr);

//        //排序
//        insertionSort(arr);
//        System.out.println("升序排序后：");
//
//        //排序后：
//        printArray(arr);

        selectionSort(arr);
        System.out.println("selectionSort排序后：");

        //bubbleSort(arr);
        //System.out.println("bubbleSort排序后：");
        printArray(arr);
    }

    public static int[] selectionSort(int[] arr) {

        for (int i=0; i < arr.length-1; i++) { //外层循环控制排序趟数
            int min = arr[i];
            for (int j = i+1; j < arr.length; j++) { //内层循环控制每一趟排序多少次
                if(min > arr[j]){
                    swap(arr, i, j);
                }
            }
        }
        return arr;
    }

    public static int[] bubbleSort(int[] arr) {
        for (int i=0; i < arr.length-1; i++) { //外层循环控制排序趟数
            for (int j=0; j < arr.length-1-i; j++) { //内层循环控制每一趟排序多少次
                if(arr[j] > arr[j+1]){
                    swap(arr, j++,j);
                }
            }
        }
        return arr;
    }
    /**
     * 插入排序:  升序为例
     * 原理：每次将最后一个元素作为插入元素，  与有序数列比较后 插入正确位置
     *
     */
    private static int[]  insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {  //fast
            for (int j = i; j > 0; j--) {       //before
                if (arr[j] < arr[j - 1]) {//符合条件，插入元素（交换位置）
                    swap(arr, j,j-1);
                }
            }
        }
        return arr;
    }
    /*
    发现无论什么排序。都需要对满足条件的元素进行位置置换。
    所以可以把这部分相同的代码提取出来，单独封装成一个函数。
    */
    public static void swap(int[] arr,int a,int b)
    {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void printArray(int[] arr)
    {
        System.out.print("[");
        for(int x=0; x<arr.length; x++)
        {
            if(x!=arr.length-1)
                System.out.print(arr[x]+", ");
            else
                System.out.println(arr[x]+"]");

        }
    }
}
