/**
 * @title MapNewMethod
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 16:23
 */
package hello.programmer.java8;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/8 16:23
 */
public class MapNewMethod {

    @Test
    public void getDefault(){
        Map<String,String> map = new HashMap<>();
        String va = map.getOrDefault("er","not found");
        System.out.println(va);

        map.putIfAbsent("1","1");
        map.putIfAbsent("1","2");
        map.putIfAbsent("2","2");
        map.forEach((k,v) -> System.out.println(k+"="+v+","));

        System.out.println("-----------------");
        map.remove("1","2");
        map.forEach((k,v) -> System.out.println(k+"="+v+","));

        System.out.println("-----------------");
        map.remove("1","1");
        map.forEach((k,v) -> System.out.println(k+"="+v+","));

        map.computeIfAbsent("3",key-> key + "3");
        System.out.println("-----------------");
        map.forEach((k,v) -> System.out.println(k+"="+v+","));

        map.computeIfPresent("2", (key, val) -> val + key);
        System.out.println("-----------------");
        map.forEach((k,v) -> System.out.println(k+"="+v+","));
    }
}