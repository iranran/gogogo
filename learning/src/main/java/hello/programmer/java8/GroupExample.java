/**
 * @title GroupExample
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 18:01
 */
package hello.programmer.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/8 18:01
 */
public class GroupExample {

    @Test
    public void sdf(){
        Employee employee = new Employee();
        employee.setName("opop");
        employee.setCity("London");
        employee.setNumberSales(3);

        Employee employee2 = new Employee();
        employee2.setName("2");
        employee2.setCity("BJ");
        employee2.setNumberSales(3);

        Employee employee3 = new Employee();
        employee3.setName("3");
        employee3.setCity("BJ");
        employee3.setNumberSales(3);

        List<Employee> employees = Arrays.asList(employee,employee2,employee3);

        Map<String, List<Employee>> employeesByCity =
                employees.stream().collect(groupingBy(Employee::getCity));

        System.out.println(employeesByCity);

        Map<String, Long> numEmployeesByCity =
                employees.stream().collect(groupingBy(Employee::getCity, counting()));
        System.out.println(numEmployeesByCity);
    }
}