package hello.programmer.work;

import java.util.Arrays;

public class ReplaceBroker {
    private static String tmpl = "INSERT INTO `tb_common_ini`( `org_id` , `ini_name` , `ini_desc` , `ini_value` , `language` , `created` , `updated`) VALUES( '6001' , 'invite_share_url' , '邀请返佣注册链接' , 'https://www.bhex.com/m/invite/register/step1?invite_code=%s&broker=%s&account=%s' , 'zh_CN' , '0' , '0') ,( '6001' , 'invite_share_url' , '邀请返佣注册链接' , 'https://www.bhex.com/m/invite/register/step1?invite_code=%s&broker=%s&account=%s' , 'en_US' , '0' , '0') ,( '6001' , 'invite_share_wx_title' , '邀请返佣分享标题' , 'BHEX' , 'zh_CN' , '0' , '0') ,( '6001' , 'invite_share_wx_title' , '邀请返佣分享标题' , 'BHEX' , 'en_US' , '0' , '0') ,( '6001' , 'invite_share_wx_content' , '邀请返佣分享内容' , '您的朋友在BHEX等您，快来注册吧！' , 'zh_CN' , '0' , '0') ,( '6001' , 'invite_share_wx_content' , '邀请返佣分享内容' , 'Your friend is waiting for you at BHEX, come and register!' , 'en_US' , '0' , '0');";
    public static void main(String[] args) {
        String brokers = "|      7025 | 360EXX            | 360exx.com                 |\n" +
                "|      7024 | AFABIT            | afabit.com                 |\n" +
                "|      7023 | 亿财              | yiyuncoin.com              |\n" +
                "|      7022 | FOFAgent          | fofbex.com                 |\n" +
                "|      7021 | SCEX              | scex.co                    |\n" +
                "|      7020 | BBE               | bigblock.pro               |\n" +
                "|      7019 | CoinFox           | coinfox.io                 |\n" +
                "|      7018 | 币牛              | beenew.io                  |\n" +
                "|      7017 | 币in              | b.ink                      |\n" +
                "|      7016 | Bitdad            | www.broker-7016.bhop.cloud |\n" +
                "|      7015 | Bitrabbit         | bitdad.com                 |\n" +
                "|      7014 | Bitdad            | www.broker-7014.bhop.cloud |\n" +
                "|      7013 | FBEx              | fortunebus.io              |\n" +
                "|      7012 | 溯源链            | tac.vip                    |\n" +
                "|      7011 | Bitfinity         | coinfinity.io              |\n" +
                "|      7010 | MtToken           | mttoken.io                 |\n" +
                "|      7009 | MtBlock           | www.broker-7009.bhop.cloud |\n" +
                "|      7008 | BITREET           | bitreet.com                |\n" +
                "|      7007 | FChain            | fchain.one                 |\n" +
                "|      7006 | SaaS-broker-004   | www.broker-7006.bhop.cloud |\n" +
                "|      7005 | SaaS-broker-003   | www.broker-7005.bhop.cloud |\n" +
                "|      7004 | SaaS-broker-002   | www.broker-7004.bhop.cloud |\n" +
                "|      7003 | SaaS-broker-001   | www.broker-7003.bhop.cloud |\n" +
                "|      7002 | BHOP-大步券商     | www.broker-7002.bhop.cloud |\n" +
                "|      6002 | BHEX              | www.bhex.com               |";
        String[] arr = brokers.split("\n");
        Arrays.asList(arr).forEach(s-> System.out.println(s));
    }
}
