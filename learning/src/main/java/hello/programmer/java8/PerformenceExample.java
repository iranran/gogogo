/**
 * @title PerformenceExample
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/9 11:04
 */
package hello.programmer.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 区别不大，但lambda第一次使用初始化时要进行加载速度很慢，大概几十ms，后序和原生代码差别不大
 * @author liwei
 * @create 2018/3/9 11:04
 */
public class PerformenceExample {

    @Test
    public void adf(){
        List<Person> persons = new ArrayList<>();

        for(int i=0; i<10000000; i++){
            Person person = new Person();
            person.setFirstName("hahahha" + i);
            persons.add(person);
        }

        for(int i = 0 ; i<100; i++){
            long t0 = System.currentTimeMillis();
            long c = persons.stream().filter(p -> p.getFirstName().startsWith("h")).count();
            long t1 = System.currentTimeMillis();
            System.out.println("lambda=" + (t1 - t0));



            long t10 = System.currentTimeMillis();
            List<Person> ps = new ArrayList<>();
            for(Person p : persons){
                if(p.getFirstName().startsWith("df")){
                    ps.add(p);
                }
            }
            long t11 = System.currentTimeMillis();
            System.out.println("loop="+(t11 - t10));
        }

        long t0 = System.currentTimeMillis();
        long c = persons.stream().map(p -> p.getFirstName().startsWith("h")).count();
        long t1 = System.currentTimeMillis();
        System.out.println(t1 - t0);



    }
}