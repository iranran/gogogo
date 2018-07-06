/**
 * @title LambdaStream
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 15:43
 */
package hello.programmer.java8;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/8 15:43
 */
public class LambdaStream {

    static List<String> stringCollection = new ArrayList<>();
    static {
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb3");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");
    }


    @Test
    public void filter(){
        stringCollection.stream().filter(s -> s.startsWith("a")).forEach(System.out::println);
    }

    @Test
    public void map(){
        stringCollection.stream().map(s -> s + "<~~>").forEach(System.out::println);
    }

    @Test
    public void sort(){
        stringCollection.stream().sorted(String::compareTo).forEach(System.out::println);
    }

    @Test
    public void sortShort(){
        Short a = Arrays.asList(new Short[]{3,1,5,8,4}).stream().max(Short::compareTo).get();
        System.out.println(a);
    }

    @Test
    public void match(){
        boolean anyStartsWithA =
                stringCollection
                        .stream()
                        .anyMatch(s -> s.startsWith("a"));
        System.out.println(anyStartsWithA);      // true


        boolean allStartsWithA =
                stringCollection
                        .stream()
                        .allMatch(s -> s.startsWith("a"));
        System.out.println(allStartsWithA);      // false

        boolean noneStartsWithZ =
                stringCollection
                        .stream()
                        .noneMatch(s -> s.startsWith("z"));
        System.out.println(noneStartsWithZ);      // true
    }

    @Test
    public void group(){
        Map<String,Long> result =
                stringCollection.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );
        System.out.println(result);
    }
}