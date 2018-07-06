/**
 * @title Functional
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 17:02
 */
package hello.programmer.java8;

import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/8 17:02
 */
public class Functional {

    public static <T,R> List<R> map(List<T> data, Function<T, R> mapFunc) {
        return data.stream().map(mapFunc).collect(Collectors.toList());  // stream replace foreach
    }

    public static <T> List<T> filter(List<T> data, Predicate<T> filterFunc) {
        return data.stream().filter(filterFunc).collect(Collectors.toList());  // stream replace foreach
    }

    public static <T> Map<T,Long> group(List<T> data){
        return data.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static <T,R> Map<R,List<T>> group(List<T> data, Function<T,R> groupKey){
        return data.stream().collect(Collectors.groupingBy(groupKey));
    }

    public static <T,R,K> Map<R,Set<K>> group(List<T> data, Function<T,R> groupKey, Function<T,K> mappingKey){
        return data.stream().collect(
                Collectors.groupingBy(groupKey,
                        Collectors.mapping(mappingKey, Collectors.toSet())
                )
        );
    }

    @Test
    public void sdf(){
        Person person1 = new Person("Peterc", "Parker");
        Person person2 = new Person("Peters", "Parker");
        Person person3 = new Person("Petera", "Parker");
        List<Person> persons = Arrays.asList(person1,person2,person3);

        //Function<Person,String> function = Person::getFirstName;
        Function<Person,String> function = item -> item.getFirstName() + "hahah";

        System.out.println(function.apply(person2));

        List<String> names = map(persons, item -> item.getFirstName());
        System.out.println(names);

        names = filter(names,item -> item.contains("c"));
        System.out.println(names);

        List<Person> ps = filter(persons, person -> person.getFirstName().contains("P"));
        System.out.println(ps);

        Map<String,List<Person>> map = group(persons, Person::getLastName);
        System.out.println(map);
    }

    @Test
    public void testF(){
        //简单的,只有一行
        Function<String, String> function1 = (x) -> "f1:" + x;
        Function<String, String> function2 = (x) -> "f2:" + x;
        //标准的,有花括号, return, 分号.
        Function<String, Integer> function3 = (x) -> {
            System.out.println(("f3:") +x);
            return ("f3: " +x).length();
        };

        Function<String, String> function4 = (x) -> {
            return "before function2 "+" test result3: " + x;
        };
        System.out.println(function1.apply("98"));
        System.out.println(function1.andThen(function2).andThen(function3).apply("100"));//先执行function1 然后将其结果作为参数传递到function2中
        //System.out.println(function2.andThen(function1).apply("100"));

        //System.out.println(function1.compose(function2).apply("100"));//先执行function2 在执行function1
       // System.out.println(Function.identity());

        Map<String,Object> items = new HashMap<>();
        items.put("a","342345");
        items.put("b","123123");

        Function<String,Integer> parseInt = s -> items.containsKey(s) ? Integer.parseInt(items.get(s).toString()) : null;

        System.out.println(parseInt.apply("a"));
        System.out.println(parseInt.apply("c"));
        System.out.println(parseInt.apply("b"));

        //使用Lambda
        List<Integer> numbers2 = Arrays.asList(11, 21, 31, 41, 51, 61, 71, 81, 91, 110);
        Consumer<List> consumer=(x)-> System.out.println(x+"p");
        consumer.accept(numbers2);
    }

    @Test
    public void consumer(){
        Consumer<String> c = (x) -> System.out.println(x.toLowerCase());
        c.accept("Java2s.com");
    }
}