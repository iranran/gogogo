package hello.programmer.common.basic;

import org.junit.Test;

/**
 * Created by lenovo on 2017-7-8.
 */
public class TestPinyinUtil {

    @Test
    public void pinyin(){
        Combo2<String,String> combo2 = PinyinUtil.dealHzPy("怕其求乐");
        System.out.println(combo2.getV1()+"=="+combo2.getV2());

        System.out.println(PinyinUtil.findpy2("怕其求乐"));
    }
}
