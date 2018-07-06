package hello.programmer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.math.BigDecimal;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        BigDecimal a = getAmoutCount(133,new BigDecimal(5.0),new BigDecimal(9876543.00),new BigDecimal(365));
        System.out.println(a);

        System.out.println(a.subtract(new BigDecimal(171824.78)));

        System.out.println(a.subtract(new BigDecimal(10).divide(new BigDecimal(2))));
    }

    private  BigDecimal getAmoutCount(int days,BigDecimal aunualInterestRate,BigDecimal amount,BigDecimal yearDays ){
        return amount.multiply(aunualInterestRate.divide(new BigDecimal(100))).multiply(new BigDecimal(days)).divide(yearDays, 2, BigDecimal.ROUND_HALF_UP);
    }
}
