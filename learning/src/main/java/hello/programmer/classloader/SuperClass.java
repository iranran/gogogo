package hello.programmer.classloader;

public class SuperClass extends SuperSuperClass{
    public static int m = 33;
    static {
        System.out.println("super static area");
    }

    public static void main(String[] args) throws Exception{

    }
}
