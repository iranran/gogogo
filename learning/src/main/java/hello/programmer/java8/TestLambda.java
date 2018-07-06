/**
 * @title TestLambda
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 11:17
 */
package hello.programmer.java8;


import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/8 11:17
 */
public class TestLambda {

    @Test
    public void arraySort(){
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });

        Collections.sort(names, (String a, String b) -> b.compareTo(a));

        Collections.sort(names, (a, b) -> b.compareTo(a));


        System.out.println(names);
    }



}