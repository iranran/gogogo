package hello.programmer.common.utils;

import com.asset.enums.AssetClassificationTypeEnums;
import com.asset.enums.BourseTypeEnums;
import com.asset.framework.configCenter.JianlcConfigCenter;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SPVUtils {

    private JianlcConfigCenter jianlcConfigCenter;

    private static volatile List<Map<String,Object>> list=null;

    private static String ZTYB_ZTSY="天津金融资产交易所“直融尊宝”2018年第%s期收益权分享合约产品";
    private static String HZF="天津金融资产交易所“直融优享”2018年第%s期收益权分享合约产品";
//    private static String YinChuanBourse = "中投尊享理财计划%s号";
    private static String JXCM = "天津金融资产交易所“快乐尊享”2018年第%s期收益权分享合约产品";
    private static String ZTYB_ZTSY_3="天津金融资产交易所“直融尊宝三号”2018年第%s期收益权分享合约产品";
    private static String TJ_ZJHK_WLZX="“未来尊享”%s年第%s期收益权分享合约产品";//天津交易所

    private static String ZTSYproductTemplateId = "8a7d80e75f0fa1c9015f32dddd02108d";
    private static String ZTSY_HOUSE_MORTGAGEproductTemplateId ="8a7d80e75f75e5c40162d278728b389e";
    private static String ZTYBproductTemplateId = "8a7d80e75f75e5c401614b8a8ea424dd";
    private static String ZTYB_HOUSE_MORTGAGEproductTemplateId = "8a7d80e75f75e5c40162d25d86d9372d";
    private static String HZFproductTemplateId =  "8a7d80e75f75e5c4016222aaf08b0d3a";
    private static String JXCMproductTemplateId = "8a7d80e75f75e5c40162b3c1a001155e";
    private static String ZJHKproductTemplateId = "8a7d80e7633f298701638bc2fb710e04";//中金昊坤

    private Logger logger = LoggerFactory.getLogger(SPVUtils.class);

   public SPVUtils(){}

    public SPVUtils(JianlcConfigCenter jianlcConfigCenter){
        this.jianlcConfigCenter = jianlcConfigCenter;
    }

    public List<Map<String,Object>> getSPVList(){
        if(list==null){
            initSPVList();
        }
        return  list;
    }
    public void initSPVList(){

        if(list!=null){
            return;
        }

        List<Map<String,Object>> tmpList = new ArrayList<>();
        Map<String, String> commonConfig = jianlcConfigCenter.getCommonConfig();
        //47:ZTSY:中投晟元:G1011010509334510Y:招商银行首体支行:110924455910806
        Map<String, Object> all = new HashMap<>();
        all.put("id",null);
        all.put("name","全部");
        tmpList.add(all);
        int i=1;
        while(StringUtils.isNotBlank(commonConfig.get("asset-spv-acount"+i))){
            String config = commonConfig.get("asset-spv-acount"+i);
            Map<String,Object> map = new HashMap<>();
            String bankCard = config.split(":")[5];
            String bankName = config.split(":")[2];
            String id = config.split(":")[0];
//            String showName = bankName+"("+bankCard.substring(0,3)+"***"+bankCard.substring(12,15)+")";
            String showName = bankName+"("+safeBankCard(bankCard)+")";

            map.put("id",id);
            map.put("name",showName);
            map.put("bankName",bankName);
            tmpList.add(map);
            i++;
        }
        if(list!=null){
            return;
        }else{
            list=tmpList;
        }

    }
    public static String safeBankCard(String cardNo){

        if(!LogicUtil.hasVal(cardNo)){
            return "";
        }
        if(cardNo.length()==15){
            return cardNo.substring(0,3)+"***"+cardNo.substring(12,15);
        }
        int sep=cardNo.length()/3;
        String first=cardNo.substring(0,sep);
        StringBuilder sb=new StringBuilder();
        for(int i=0;(i<cardNo.length()-sep*2)&&(i<3);i++){
            sb.append("*");

        }
        String middle=sb.toString();
        String last=cardNo.substring(sep+middle.length());
        return first+middle+last;

    }
    public Map<String,String> getSpvBorrowerMap(){
        Map<String, String> all = new HashMap<>();
        Map<String, String> commonConfig = jianlcConfigCenter.getCommonConfig();
        int i=1;
        while(StringUtils.isNotBlank(commonConfig.get("asset-spv-acount"+i))){
            String msg = commonConfig.get("asset-spv-acount"+i);
            String spvBorrowerId = msg.split(":")[0];
            all.put(spvBorrowerId,msg);
            i++;
        }
        return all;
    }
    public String  getSPVNameById(Object id) {

        initSPVList();
        if(id == null){
            return "";
        }
        Object name="";
        for(Map<String,Object> map :list){
             if(map.get("id") !=null && Integer.parseInt(map.get("id").toString()) == (Integer)id){
                 name= map.get("name");
             }
        }
        return name.toString();
    }

    public Map<String,Object>  getSPVMapById(Object id) {
        initSPVList();
        if(id == null){
            return null;
        }
        for(Map<String,Object> map :list){
            if(map.get("id") !=null && Integer.parseInt(map.get("id").toString()) == (Integer)id){
                return map;
            }
        }
        return null;
    }

    /**
     * 生成loanInfo.product_external_code字段值
     * 添加资产分类判断逻辑，资产分类是"房抵类"的项目，特殊处理
     * */
    public static String generateProductExternalCode(String bourseNameNo, String spvBorrowerId, Integer num, Map<String,String> spvBorrowerMap,Integer classification){

        String result = "";
        String strNum ="";

        if(spvBorrowerId.equals("114") && num<57){  //会找房从57号编码开始
            num = 57;
        }
        //计算编号
        if(num < 10){
            strNum = "0000000"+num;
        }else if(num<100){
            strNum = "000000"+num;
        }else if(num<1000){
            strNum = "00000"+num;
        }else if(num<10000){
            strNum = "0000"+num;
        }else if(num<100000){
            strNum = "000"+num;
        }else if(num<1000000){
            strNum = "00"+num;
        }else if(num<10000000){
            strNum = "0"+num;
        }else if(num<100000000){
            strNum = ""+num;
        }
        if(Integer.valueOf(bourseNameNo) == BourseTypeEnums.TJ_BOURSE.getValue()){
            //47:ZTSY:中投晟元:G1011010509334510Y:110924455910806:招商银行首体支行
            //60:ZTYB:中通银邦:G1011010811157910C:110923879610304:招商银行首体支行
            String spvBorrower = spvBorrowerMap.get(spvBorrowerId);
            if(StringUtils.isNotBlank(spvBorrower)){
                String prefix = "";
                if(spvBorrowerId.equals("47")|| spvBorrowerId.equals("60")){ //47中投晟元,60中通银邦
                	if(classification!=null&& classification.equals(AssetClassificationTypeEnums.HOUSE_MORTGAGE.getValue())) {
                		//资产类型是房抵类的项目
                		prefix="SFH-ZRZB3-";
                	}else {
                		prefix="SFH-ZRZB-";
                	}
                    
                }else if(spvBorrowerId.equals("114")){ //114 会找房
                    prefix="SFH-ZRYX-";
                }else if(spvBorrowerId.equals("123")){ //123 江西春眠
                    prefix="SFH-KLZX-";
                } 
                result = prefix + new DateTime().getYear() + strNum + "-JLC-"+spvBorrower.split(":")[1];
                if(spvBorrowerId.equals("135")){ //135 中金昊坤
                    result = "SFH-WLZX-JLC-"+spvBorrower.split(":")[1]+"-" + new DateTime().getYear() + strNum;
                }
            }else{
                return null;
            }
        }else if(Integer.valueOf(bourseNameNo) == BourseTypeEnums.YC_BOURSE.getValue()){
            result = "XXX-XXXX-"+new DateTime().getYear() + strNum+"-JLC-XXXX";
        }

        return result;
    }
    /**\
     * 获取实际上的产品外部编码，数据库中存储的只是格式确定的编码，不一定是真的编码，
     * */
    public static String getRealProjectExtendCode(String bourseName,Object projectExtendCode){

        if(projectExtendCode == null){
            return null;
        }
        if (bourseName.equals(BourseTypeEnums.TJ_BOURSE.getDesc())) {
            return projectExtendCode.toString();
        } else if (bourseName.equals(BourseTypeEnums.YC_BOURSE.getDesc())) {
            return projectExtendCode.toString().split("-")[2].substring(4,12); //只截取数字部分，00000001；
        }
        return null;
    }

    /**
     * 分割产品外部编码的数字部分，格式可能各个部分不一样
     * */
    public static String getNumFromProjectExtendCode(Object productExternalCode,String bourseName,String spvBorrowerId) throws Exception{
        String num="";
        try {
            String productExternalCodes = productExternalCode.toString();
            num = productExternalCodes.split("-")[2];
            if(bourseName.equals(BourseTypeEnums.TJ_BOURSE.getDesc())&&spvBorrowerId.equals("135")){
                num = productExternalCodes.split("-")[4];
            }
            num = num.substring(4, num.length());
        }catch (Exception e){
            throw new Exception("productExternalCode格式异常，productExternalCode="+productExternalCode);
        }
            return num;
    }


    public String getSPVIdByName(String name) {
        initSPVList();
        if(name.isEmpty()){
            return "";
        }
        String id="";
        for(Map<String,Object> map :list){
            if(map.get("bankName") !=null && map.get("bankName").toString().equals(name)){
                id= map.get("id").toString();
                return id;
            }
        }
        return id;
    }
    
    /**
     * 导出服务器托管模式下项目信息，获取在天金所处的产品名称
     * 添加资产类型是"房抵类"的不良资产 信息 add by 2018-04-25
     * */
    public static String getBourseProductName(String bourseName,String spvBorrowerId,String proudctNum,Integer classification){
        if(bourseName.equals(BourseTypeEnums.TJ_BOURSE.getDesc())) {
            if (spvBorrowerId.equals("47") || spvBorrowerId.equals("60")) {
            	if(classification!=null&& classification.equals(AssetClassificationTypeEnums.HOUSE_MORTGAGE.getValue())) {
            		return String.format(ZTYB_ZTSY_3, proudctNum);
            	}else {
            		return String.format(ZTYB_ZTSY, proudctNum);
            	}
                
            } else if (spvBorrowerId.equals("114")) {
                return String.format(HZF, proudctNum);
            } else if (spvBorrowerId.equals("123")) {
                return String.format(JXCM, proudctNum);
            }else if(spvBorrowerId.equals("135")){
                return String.format(TJ_ZJHK_WLZX, new DateTime().getYear(),proudctNum);
            }
        }else if(bourseName.equals(BourseTypeEnums.YC_BOURSE.getDesc())){
            if (spvBorrowerId.equals("47")) {//47:ZTSY:中投晟元:
                return "中投尊享理财计划"+proudctNum+"号";
            }
            if (spvBorrowerId.equals("135") || spvBorrowerId.equals("76")) {//135:ZJHK:中金昊坤
                return "中金尊享"+proudctNum+"号";
            }

        }
        return null;
    }
    
    /**
     * 导出服务器托管模式下项目信息，获取在天金所处的产品模版id
     * */
    public static String getBourseProductTemplateId(String bourseName,String spvBorrowerId,Integer classification){
        if(bourseName.equals(BourseTypeEnums.TJ_BOURSE.getDesc())){
            if(spvBorrowerId.equals("47")){
            	if(classification!=null&& classification.equals(AssetClassificationTypeEnums.HOUSE_MORTGAGE.getValue())) {
            		return ZTSY_HOUSE_MORTGAGEproductTemplateId;
            	}else {
            		return ZTSYproductTemplateId;
            	}
                
            } else if(spvBorrowerId.equals("60")){
            	if(classification!=null&& classification.equals(AssetClassificationTypeEnums.HOUSE_MORTGAGE.getValue())) {
            		return ZTYB_HOUSE_MORTGAGEproductTemplateId;
            	}else {
            		return ZTYBproductTemplateId;
            	}
                
            }else if(spvBorrowerId.equals("114")){
                return HZFproductTemplateId;
            }else if(spvBorrowerId.equals("123")){
                return JXCMproductTemplateId;
            }else if(spvBorrowerId.equals("135")){
                return ZJHKproductTemplateId;
            }
        }else if(bourseName.equals(BourseTypeEnums.YC_BOURSE.getDesc())){
            return null;             //银川交易所目前没有模版id
        }
        return null;
    }

    public static void main(String[] args) {
//        System.out.println(safeBankCard("123456789012345"));
//        System.out.println(safeBankCard("123456789"));
//        System.out.println(safeBankCard(""));
//        System.out.println(safeBankCard(null));
//        System.out.println(safeBankCard("1"));
//        System.out.println(safeBankCard("12"));
//        System.out.println(safeBankCard("123"));
//        System.out.println(safeBankCard("1234"));
//        System.out.println(safeBankCard("12345"));
//        System.out.println(safeBankCard("123456"));
//        System.out.println(safeBankCard("1234567"));
//        System.out.println(safeBankCard("12345678"));
//        System.out.println(safeBankCard("123456789"));
//        System.out.println(safeBankCard("123456789012345123456789012345"));
    }

}
