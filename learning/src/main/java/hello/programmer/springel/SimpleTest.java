/**
 * @title SimpleTest
 * @package hello.programmer.springel
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/5/31 15:13
 */
package hello.programmer.springel;

import hello.programmer.java8.Employee;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/5/31 15:13
 */
public class SimpleTest {
    private static ExpressionParser parser = new SpelExpressionParser();

    @Test
    public void testHelloWorld() {
        Expression expression = parser.parseExpression("'你好世界！'");
        String result = (String) expression.getValue();
        System.out.println(result);

    }


    @Test
    public void testStringOperation() {
        Expression expression = parser.parseExpression("'你好'.concat('世界!')");
        String result = (String) expression.getValue();
        System.out.println(result);
        expression = parser.parseExpression("'Hello world!'.toUpperCase()");
        result = expression.getValue(String.class);
        System.out.println(result);
    }

    @Test
    public void adfs(){
        Map<String,String> item = new HashMap<>();
        item.put("a","a");
        item.put("b","b");
        item.put("c","c");


        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("item",item);

        Expression expression = parser.parseExpression("#item['a']");


        String aleks = expression.getValue(context,String.class);
        System.out.println(aleks);

    }



}