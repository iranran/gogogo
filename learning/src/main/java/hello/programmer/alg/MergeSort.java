package hello.programmer.alg;

public class MergeSort{
    public static void main(String[] args) {
        int A[] = {
                1,6,4,5,2,9,7,23,56,43,98,56
        };
        int[] temp = new int[A.length];
        mergeSort(A,temp,0,A.length-1);
        for (int i:A){
            System.out.print(i+" ");
        }
    }

    public static void mergeSort(int[] A,int[] temp,int start,int end){
        if (start < end){
            int mid = (start + end)/2;
            //把数组分解为两个子列
            mergeSort(A, temp, start, mid);
            mergeSort(A, temp,mid+1, end);
            //逐级合并两个子列
            merge(A,temp,start,mid,end);
        }
    }

    public static void merge(int[] A, int[] temp, int start, int mid, int end){
        int i = start;
        int j = mid + 1;
        int k = 0;
        while(i<=mid && j<=end){
            if (A[i] <= A[j]) {
                temp[k++] = A[i++];
            }else {
                temp[k++] = A[j++];
            }
        }
        while(i <= mid){
            temp[k++] = A[i++];
        }
        while(j <= end){
            temp[k++] = A[j++];
        }
        for (int m = 0; m<k; m++) {
            A[start + m] = temp[m];
        }
    }
}