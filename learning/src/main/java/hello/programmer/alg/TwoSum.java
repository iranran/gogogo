package hello.programmer.alg;

import java.util.ArrayList;
import java.util.List;

public class TwoSum {

    public static boolean sorted(List<Integer> list, int target) {
        int len = list.size();
        for(int i=0,j=len-1; i<j ;){
            if(list.get(i) + list.get(j) == target){
                return true;
            }
            if(list.get(i) + list.get(j) > target){
                j--;
            }
            else{
                i++;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(6);
        list.add(60);
        boolean contained = sorted(list, 61);
        System.out.println(contained);
    }
}
