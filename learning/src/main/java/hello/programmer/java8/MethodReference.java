/**
 * @title MethodReference
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 14:47
 */
package hello.programmer.java8;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  https://www.cnblogs.com/JohnTsai/p/5806194.html
 * 类型	示例
 * 引用静态方法	ContainingClass::staticMethodName
 * 引用某个对象的实例方法	containingObject::instanceMethodName
 * 引用某个类型的任意对象的实例方法	ContainingType::methodName
 * 引用构造方法	ClassName::new
 *
 *
 * @author liwei
 * @create 2018/3/8 14:47
 */
public class MethodReference {



    interface PersonFactory<P extends Person> {
        P create(String firstName, String lastName);
    }

    @Test
    public void constructor(){
        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Peter", "Parker");
        System.out.println(person);
    }

    @Test
    public void staticString(){
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(6);
        list.add(9);

        List<String> stringList = list.stream().map(String::valueOf).collect(Collectors.toList());
        System.out.println(stringList);
    }

    @Test
    public void classStaticMethod(){
        Person person1 = new Person("Peterc", "Parker");
        Person person2 = new Person("Peters", "Parker");
        Person person3 = new Person("Petera", "Parker");
        List<Person> persons = Arrays.asList(person1,person2,person3);

        Collections.sort(persons, Comparator.comparing(Person::getFirstName));
        System.out.println(persons);

        //使用方法引用 引用的是类的静态方法,参数自动推导，简化代码书写
        Collections.sort(persons, Person::compare);
        System.out.println(persons);
    }

    class ComparisonProvider{
        public int compare(Person a,Person b){
            return a.getFirstName().compareTo(b.getFirstName());
        }
    }


    @Test
    public void instanceMethod(){

        Person person1 = new Person("Peterc", "Parker");
        Person person2 = new Person("Peters", "Parker");
        Person person3 = new Person("Petera", "Parker");
        List<Person> persons = Arrays.asList(person1,person2,person3);

        ComparisonProvider provider = new ComparisonProvider();
        //使用lambda表达式
        //对象的实例方法
        //Collections.sort(persons,(a,b)->provider.compare(a,b));
        //System.out.println(persons);
        //使用方法引用
        //引用的是对象的实例方法
        Collections.sort(persons, provider::compare);
        System.out.println(persons);
    }

    @Test
    public void ssss(){
        String[] stringsArray = {"Hello","World"};

        //使用lambda表达式和类型对象的实例方法
        Arrays.sort(stringsArray,(s1,s2)->s1.compareToIgnoreCase(s2));

        //使用方法引用
        //引用的是类型对象的实例方法
        Arrays.sort(stringsArray, String::compareToIgnoreCase);
    }
}