/**
 * @title Employee
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 17:58
 */
package hello.programmer.java8;

import lombok.Data;

import java.io.Serializable;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/8 17:58
 */
@Data
public class Employee implements Serializable{

    private String name;

    private String city;

    private int numberSales;

}