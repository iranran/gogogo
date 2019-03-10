package hello.work;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReplaceBroker {
    private static String tmpl = "INSERT INTO `tb_common_ini`( `org_id` , `ini_name` , `ini_desc` , `ini_value` , `language` , `created` , `updated`) VALUES( '6001' , 'invite_share_url' , '邀请返佣注册链接' , 'https://www.bhex.com/m/invite/register/step1?invite_code=%s&broker=%s&account=%s' , 'zh_CN' , '0' , '0') ,( '6001' , 'invite_share_url' , '邀请返佣注册链接' , 'https://www.bhex.com/m/invite/register/step1?invite_code=%s&broker=%s&account=%s' , 'en_US' , '0' , '0') ,( '6001' , 'invite_share_wx_title' , '邀请返佣分享标题' , 'BHEX' , 'zh_CN' , '0' , '0') ,( '6001' , 'invite_share_wx_title' , '邀请返佣分享标题' , 'BHEX' , 'en_US' , '0' , '0') ,( '6001' , 'invite_share_wx_content' , '邀请返佣分享内容' , '您的朋友在BHEX等您，快来注册吧！' , 'zh_CN' , '0' , '0') ,( '6001' , 'invite_share_wx_content' , '邀请返佣分享内容' , 'Your friend is waiting for you at BHEX, come and register!' , 'en_US' , '0' , '0');";

    public static void df() {
        String brokers =
                "|      7026 | BHOPB            | bhopb.cloud                |\n" +
                        "|      7025 | 360EXX            | 360exx.com                 |\n" +
                        "|      7024 | AFABIT            | afabit.com                 |\n" +
                        "|      7023 | 亿财              | yiyuncoin.com              |\n" +
                        "|      7022 | FOFAgent          | fofbex.com                 |\n" +
                        "|      7021 | SCEX              | scex.co                    |\n" +
                        "|      7020 | BBE               | bigblock.pro               |\n" +
                        "|      7019 | CoinFox           | coinfox.io                 |\n" +
                        "|      7018 | 币牛              | beenew.io                  |\n" +
                        "|      7017 | 币in              | bink.pro                      |\n" +
                        "|      7015 | Bitrabbit         | bitdad.com                 |\n" +
                        "|      7013 | FBEx              | fortunebus.io              |\n" +
                        "|      7012 | 溯源链            | tac.vip                    |\n" +
                        "|      7011 | Bitfinity         | coinfinity.io              |\n" +
                        "|      7010 | MtToken           | mttoken.io                 |\n" +
                        "|      7009 | MtBlock           | www.broker-7009.bhop.cloud |\n" +
                        "|      7008 | BITREET           | bitreet.com                |\n" +
                        "|      7007 | FChain            | fchain.one                 |\n" +
                        "|      7002 | BHOP-大步券商     | www.broker-7002.bhop.cloud |\n" ;
        String[] arr = brokers.split("\n");
        Arrays.asList(arr).forEach(s -> {
            String[] ss = s.split("\\|");
            System.out.println(tmpl
                    .replaceAll("BHEX", ss[2].trim())
                    .replaceAll("6001", ss[1].trim())
                    .replaceAll("www.bhex.com", "www." + ss[3].trim())
            );
        });
    }



    public static void tbtokens() {
        String brokers =
                        "|      7002 |         602 |\n" +
                        "|      7003 |         603 |\n" +
                        "|      7007 |         607 |\n" +
                        "|      7008 |         608 |\n" +
                        "|      7008 |         602 |\n" +
                        "|      7008 |         618 |\n" +
                        "|      7010 |         609 |\n" +
                        "|      7011 |         610 |\n" +
                        "|      7012 |         611 |\n" +
                        "|      7012 |         611 |\n" +
                        "|      7013 |         612 |\n" +
                        "|      7017 |         614 |\n" +
                        "|      7018 |         615 |\n" +
                        "|      7019 |         607 |\n" +
                        "|      7020 |         615 |\n" +
                        "|      7021 |         617 |\n" +
                        "|      7022 |         618 |\n" +
                        "|      7022 |         608 |\n" +
                        "|      7023 |         619 |\n" +
                        "|      7025 |         607 |\n" +
                        "|      7026 |         604 |" ;
        String[] arr = brokers.split("\n");
        Arrays.asList(arr).forEach(s -> {
            String[] ss = s.split("\\|");
            System.out.print(String.format("update tb_token set exchange_id=%s where org_id=%s ;", ss[2].trim(), ss[1].trim()));

        });
    }

    public static void fd() {
        String s1 = "182004,182006,182012,182014,182069,182082,182083,182086,182089,182090,182095,182096,218358,218359,218360,218361,218362,218363,218364,218365,218366,218368";
        String s2 = "218358,218359,218360,218361,218366,218368,218362,218363,218364,218365,182086,182089,182012,182014,182069,182096,182004,182006,181959,181961,218373,218374,248008,248010";
        String[] arr1 = s1.split(",");

        String[] arr2 = s2.split(",");

        List<String> stringList = Arrays.asList(arr2).stream().filter(
                str2 ->!s1.contains(str2))
        .collect(Collectors.toList());
        System.out.println(String.join(",",stringList));
    }

    public static void main(String[] args) {
        //tbtokens();
        MessageFormat form = new MessageFormat("{0}xxxxx{1}xx{2}");
        String[] testArgs=new String[]{"","hello","4545"};
        System.out.println(form.format(testArgs));
        fd();

        int int1 = 12;
        int int2 = 12;

        Integer integer1 = new Integer(12);
        Integer integer2 = new Integer(12);
        Integer integer3 = new Integer(127);

        Integer a1 = 127;
        Integer a2 = 127;

        Integer a = 128;
        Integer b = 128;

        System.out.println("int1 == int2 -> " + (int1 == int2));
        System.out.println("int1 == integer1 -> " + (int1 == integer1));
        System.out.println("integer1 == integer2 -> " + (integer1 == integer2));
        System.out.println("integer3 == a1 -> " + (integer3 == a1));
        System.out.println("a1 == a2 -> " + (a1 == a2));
        System.out.println("a == b -> " + (a == b));

        Integer i=1; Integer j = new Integer(1);
        System.out.println(i==j);

        Map<String, Integer> counter = new HashMap<>();
        counter.putIfAbsent("a", 0);
        Integer integer = counter.computeIfPresent("b", (k,v)-> v +1);
        System.out.println(counter);
        System.out.println(integer);
    }
}
