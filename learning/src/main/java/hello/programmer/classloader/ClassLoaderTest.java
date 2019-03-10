package hello.programmer.classloader;


import hello.work.CommissionMail;

public class ClassLoaderTest extends  SuperClass{

    static {
        System.out.println("static area");
    }
    public static void main(String[] args) throws Exception{

        System.out.println(ClassLoaderTest.m);
        //可以通过这种方式打印加载路径
        System.out.println("boot:"+System.getProperty("sun.boot.class.path"));
        System.out.println("ext:"+System.getProperty("java.ext.dirs"));
        System.out.println("app:"+System.getProperty("java.class.path"));

        System.out.println("class loader for sun.misc.Unsafe: " + sun.misc.Launcher.class.getClassLoader());
        System.out.println("class loader for HashMap: " + java.util.HashMap.class.getClassLoader());
        System.out.println("class loader for DNSNameService: "  + sun.net.spi.nameservice.dns.DNSNameService.class.getClassLoader());
        System.out.println("class loader for this class: " + CommissionMail.class.getClassLoader());

        String s = "sdf";
        System.out.println(s.getClass().getClassLoader());
        //System.out.println(ClassLoader.getSystemClassLoader());

        System.out.println(ClassLoaderTest.class.getClassLoader());

        System.out.println(ClassLoaderTest.class.getClassLoader());
    }

}
