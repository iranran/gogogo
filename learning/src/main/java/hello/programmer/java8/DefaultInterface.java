/**
 * @title DefaultInterface
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 11:14
 */
package hello.programmer.java8;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/8 11:14
 */
public class DefaultInterface {

    public interface Formula {
        double calculate(int a);


        default double sqrt(int a) {
            return Math.sqrt(a);
        }
    }

    public static void main(String[] args) {
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };

        System.out.println(formula.calculate(100));     // 100.0
        System.out.println(formula.sqrt(16));           // 4.0

    }
}