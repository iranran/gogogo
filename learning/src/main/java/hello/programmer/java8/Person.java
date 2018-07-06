/**
 * @title Person
 * @package hello.programmer.java8
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/8 14:48
 */
package hello.programmer.java8;


/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/8 14:48
 */
public class Person {
    String firstName;
    String lastName;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static int compare(Person a,Person b){

        return a.firstName.compareTo(b.firstName);
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}