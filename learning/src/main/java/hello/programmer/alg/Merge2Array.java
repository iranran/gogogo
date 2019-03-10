package hello.programmer.alg;

public class Merge2Array {

    public static int[] merge(int[] arr1,int[] arr2){
        if(arr1==null || arr2==null){
            throw new java.lang.NullPointerException();
        }
        if(arr1.length == 0){return arr2;}//arr1={}
        if(arr2.length == 0){return arr1;}

        int[] arr3 = new int[ arr1.length + arr2.length ];
        int i,j,k; i =j = k=0;//index of arr1，2，3
        while ( i<arr1.length && j<arr2.length ){
            if( arr1[i] <= arr2[j] ){
                arr3[k++] = arr1[i++];
            }else{
                arr3[k++] = arr2[j++];
            }
        }
        while( i<arr1.length )
            arr3[k++] = arr1[i++];
        while( j<arr2.length )
            arr3[k++] = arr2[j++];
        return arr3;
    }


}
