package hello.programmer.common.utils.project;

import com.asset.enums.BourseTypeEnums;
import com.asset.framework.utils.date.DateUtils;

import java.util.Date;

/**
 * 生成项目名称
 * @author 张伯文
 * createTime 2018/4/10-16:41
 */
public class NameGeneter {
    
    
    /**
     * 天津所活期产品名称生成
     * “银信通”2018年第x期收益权分享合约产品， 2018是年份，x是从00000001开始递增
     * @author 张伯文
     * createTime 2018/4/10-16:42
     */
    public static String tianjinCurrentProduceName(Date dealDate, int number){
        String year= DateUtils.getYear(dealDate);
        String num=String.format("%08d", number);
        return "“银信通”"+year+"年第"+num+"期收益权分享合约产品";
    }

    /**
     * 天津所活期产品编号生成
     * SFH-YXT-201800000001-JLC-QHRS ，2018是年份，年份后面是从00000001开始递增。
     * @author 张伯文
     * createTime 2018/4/10-16:42
     */
    public static String tianjinCurrentProduceCode(Date dealDate, int number){
        String year= DateUtils.getYear(dealDate);
        String num=String.format("%08d", number);
        return "SFH-YXT-"+year+num+"-JLC-QHRS";
    }

    /**
     * 小v服务器托管产品编号生成
     * “银信通”2018年第x期收益权分享合约产品， 2018是年份，x是从00000001开始递增
     * @author 张伯文
     * createTime 2018/4/10-16:42
     */
    public static String xiaoVProduceName(int number){
//        String year= DateUtils.getYear(dealDate);
        String num=String.format("%08d", number);
        return "中金尊享"+num+"号";
    }

    /**
     * 小v服务器托管产品编号生成
     * XXX-XXXX-201800000001-JLC-ZJHK ，2018是年份，年份后面是从00000001开始递增。
     * @author 张伯文
     * createTime 2018/4/10-16:42
     */
    public static String xiaoVProduceCode(Date dealDate, int number,String bourseName,String spvBorrowerId){
        String year= DateUtils.getYear(dealDate);
        String num=String.format("%08d", number);
        //如果是天津交易所并且spv是中金昊坤则产品编号规则要不一样
        if(bourseName.equals(BourseTypeEnums.TJ_BOURSE.getDesc())&&spvBorrowerId.equals("135")){
            return "SFH-WLZX-JLC-ZJHK-" + year + num;
        }else{
            return "XXX-XXXX-"+year+num+"-JLC-ZJHK";  
        }
    }
}
