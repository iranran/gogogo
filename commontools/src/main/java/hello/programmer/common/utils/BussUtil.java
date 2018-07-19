package hello.programmer.common.utils;

import com.asset.consts.AssetCommConstant;
import com.jianlc.asset.api.bean.base.RiskControlComment;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务工具类
 */
public class BussUtil {

    /**
     * 将风控意见封装成list
     * @param res
     * @return
     */
    public static List<RiskControlComment> convertRiskControlCommentToList(String res){
        List<RiskControlComment> list = new ArrayList<>();
        if (!StringUtils.isEmpty(res)){
            if (res.contains(AssetCommConstant.RISK_SPLITER)){
                String[] arr = res.split(AssetCommConstant.RISK_SPLITER);
                for (String s : arr){
                    addRiskControllCommentToList(list,s);
                }
            }else{
                addRiskControllCommentToList(list,res);
            }
        }
        return list;
    }

    /**
     * 将风控意见按照分隔符拼接
     * @param list
     * @return
     */
    public static String convertRiskControlCommentToString(List<RiskControlComment> list){
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<list.size();i++){
            RiskControlComment r = list.get(i);
            if(i != 0){
                sb.append(AssetCommConstant.RISK_SPLITER);
            }
            sb.append(r.getId()).append(AssetCommConstant.RISK_SPLITER_ID).append(r.getRiskType())
                    .append(AssetCommConstant.RISK_SPLITER_ID).append(r.getComment());
        }
        return  sb.toString();
    }

    private static void addRiskControllCommentToList(List<RiskControlComment> list,String res){
        String[] ar = res.split(AssetCommConstant.RISK_SPLITER_ID);
        RiskControlComment r = new RiskControlComment();
        r.setId(ar[0]);
        r.setRiskType(ar[1]);
        r.setComment(ar[2]);
        list.add(r);
    }


}