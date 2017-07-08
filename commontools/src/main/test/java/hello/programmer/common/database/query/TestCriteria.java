package hello.programmer.common.database.query;


import static  org.junit.Assert.*;
import org.junit.Test;


/**
 * @author liwei
 * @Description
 * @date 2017/6/29 16:01
 */
public class TestCriteria {

    @Test
    public void testNull(){
        Criteria criteria = new Criteria();
        criteria.preNull("name").andEqualTo("name","name");
        criteria.preNull("code").andEqualTo("code",null);
        criteria.preNull("nick").andFullLike("nick",null);
        String b = criteria.build("select * from loan",false);
        System.out.println(b);

        assertTrue(b.indexOf("name") > -1);
        assertTrue(b.indexOf("code") == -1);
        assertTrue(b.indexOf("nick") == -1);
    }

    @Test
    public void testEmpty(){

        Criteria criteria = new Criteria();
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
