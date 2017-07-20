package hello.programmer.common.database.query;


import static  org.junit.Assert.*;

import hello.programmer.common.basic.Combo2;
import org.junit.Test;

import java.util.Date;


/**
 * @author liwei
 * @Description
 * @date 2017/6/29 16:01
 */
public class TestCriteria {

    @Test
    public void testAppend(){
        String sql = Criteria.create()
                .append("id in (select id from b where status = 2)")
                .build();
        System.out.println(sql);
    }

    @Test
    public void testNull(){
        Criteria criteria = Criteria.create()
                .preNull("name").andEqualTo("name","name")
                .preNull("code").andEqualTo("code",null)
                .preNull("nick").andFullLike("nick",null)
                .andEqualTo("haha","sdfdfs")
                .andDateEqualTo("startDate",new Date())
                .andDateBetween("endDate",new Date(),new Date())
                .append("id in (select id from b where status = 2)");
        Combo2<String,Object[]> b = criteria.build2();
        System.out.println(b.getV1());
        for(Object v : b.getV2()){
            System.out.println(v);
        }

//        assertTrue(b.indexOf("name") > -1);
//        assertTrue(b.indexOf("code") == -1);
//        assertTrue(b.indexOf("nick") == -1);
    }

    @Test
    public void testEmpty(){

        Criteria criteria = Criteria.create();
        criteria.preEmpty("name").andEqualTo("name","name");
        criteria.preEmpty("code").andEqualTo("code",null);
        criteria.preEmpty("province").andEqualTo("province","");
        criteria.preEmpty("nick").andFullLike("nick","");
        criteria.preEmpty("city").andFullLike("city",null);

        String b = criteria.build("select * from loan",false);
        System.out.println(b);
        assertTrue(b.indexOf("name") > -1);
        assertTrue(b.indexOf("code") == -1);
        assertTrue(b.indexOf("province") == -1);
        assertTrue(b.indexOf("nick") == -1);
        assertTrue(b.indexOf("city") == -1);

    }

}
