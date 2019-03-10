package hello.programmer.java8;

import java.util.StringJoiner;

public class StringJoinerTesgt {

    public static void main(String[] args) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");

        joiner.add("haha");
        joiner.add("calcal");

        System.out.println(joiner.toString());
    }
}
