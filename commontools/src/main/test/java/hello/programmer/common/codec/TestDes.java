package hello.programmer.common.codec;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by lenovo on 2017-7-8.
 */
public class TestDes {

    @Test
    public void testEncodeAndDecode() throws Exception{
        String initcode = "hello world!";
        String descode = Base64Util.encode(Des.encode(initcode.getBytes(), "show2013show2013show2013", "show2013".getBytes()));
        Assert.assertNotNull(descode);
        String decode = (new String(Des.decode(Base64Util.decode(descode), "show2013show2013show2013", "show2013".substring(0,8).getBytes())));
        Assert.assertEquals(initcode,decode);
    }
}
