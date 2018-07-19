package hello.programmer.common.util;


import com.jianlc.asset.api.bean.CapitalAssetPriority;
import com.jianlc.asset.api.bean.base.MqTask;
import com.jianlc.asset.api.enums.MoneyAccountTypeEnum;
import com.jianlc.asset.api.enums.TaskTypeEnum;
import com.jianlc.asset.conf.SysConfigUtil;
import com.jianlc.asset.configCenter.JianlcConfigCenter;
import com.jianlc.asset.enums.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 通用定制工具
 * @author tangang
 * @create 2016年6月24日 下午3:47:29
 */
public class CommonCustomUtil {
	/**
	 * 获取资金权重
	 * @author tangang
	 * @create 2016年6月24日 下午3:47:29
	 */
	public static Integer getMoneyWeight(SysConfigUtil sysConfig, String fundType) throws Exception {
		List<CapitalAssetPriority> moneyList = sysConfig.getListByKey(SysConfigEnum.CAP.getValue(), CapitalAssetPriority.class);
		String weight = "";
		for (CapitalAssetPriority cap : moneyList) {
			if (cap.getFundType().equals(fundType)) {
				weight = cap.getPriority();
				break;
			}
		}
		return new Integer(weight);
	}
	/**
	 * 获取资产权重
	 * @author tangang
	 * @create 2016年6月24日 下午3:47:29
	 */
	public static Integer getAssetWeight(SysConfigUtil sysConfig, String fundType) throws Exception {
		List<CapitalAssetPriority> assetList = sysConfig.getListByKey(SysConfigEnum.ASS.getValue(), CapitalAssetPriority.class);
		String weight = "";
		for (CapitalAssetPriority cap : assetList) {
			if (cap.getFundType().equals(fundType)) {
				weight = cap.getPriority();
				break;
			}
		}
		return new Integer(weight);
	}
    public static Integer getAccountType(MqTask mqTask) {
        if(mqTask.getTaskType() == TaskTypeEnum.USER_CURRENT_TRANSFER_REGULAR.getValue()){
            return MoneyAccountTypeEnum.REGULAR_ACCOUNT.getValue();
        }
        if(mqTask.getTaskType() == TaskTypeEnum.USER_REGULAR_TRANSFER_CURRENT.getValue()){
            return MoneyAccountTypeEnum.CURRENT_ACCOUNT.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_CURRENT_TRANSFER_TN.getValue()) {
            return MoneyAccountTypeEnum.TN_ACCOUNT.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_TN_TRANSFER_CURRENT.getValue()) {
            return MoneyAccountTypeEnum.CURRENT_ACCOUNT.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_MATCHED_CURRENT_RELEASE.getValue()) {
            return MoneyAccountTypeEnum.CURRENT_ACCOUNT.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_MATCHED_REGULAR_RELEASE.getValue()) {
            return MoneyAccountTypeEnum.REGULAR_ACCOUNT.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_MATCHED_TN_RELEASE.getValue()) {
            return MoneyAccountTypeEnum.TN_ACCOUNT.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.RELEASE_USER_CURRENT_ASSET.getValue()) {
            return MoneyAccountTypeEnum.CURRENT_ACCOUNT.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.RELEASE_USER_REGULAR_ASSET.getValue()) {
            return MoneyAccountTypeEnum.REGULAR_ACCOUNT.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.RELEASE_USER_TN_ASSET.getValue()) {
            return MoneyAccountTypeEnum.TN_ACCOUNT.getValue();
        }
        return null;
    }
    
    
    /**
     * 
     * @Description TODO(用一句话描述这个变量表示什么)
     * @author ZhouRunFa
     * @date 2017年5月5日下午5:45:06
     * 
     * @param userScore
     * @return
     */
    public static int calculateLevel(BigDecimal userScore, JianlcConfigCenter jianlcConfigCenter) {
        // 高级标准
        int advancedStandard = Integer.parseInt(jianlcConfigCenter.getMatchingConfig().get("advancedStandard"));
        // 中级标准
        int mediumStandard = Integer.parseInt(jianlcConfigCenter.getMatchingConfig().get("mediumStandard"));

        if (userScore.compareTo(new BigDecimal(Integer.valueOf(advancedStandard).toString())) >= 0) {
            return UserLevelEnum.ADVANCED.getValue();
        } else if (userScore.compareTo(new BigDecimal(Integer.valueOf(advancedStandard).toString())) < 0
                && userScore.compareTo(new BigDecimal(Integer.valueOf(mediumStandard).toString())) >= 0) {
            return UserLevelEnum.MEDIUM.getValue();
        } else {
            return UserLevelEnum.JUNIOR.getValue();
        }
    }
    
    
    /**
     * 
     * @Description 根据任务类型返回资金队列的类型
     * @author ZhouRunFa
     * @date 2017年5月10日下午7:14:24
     * 
     * @param mqTask
     * @return
     */
    public static Integer getMoneyType(MqTask mqTask) {
        if(mqTask.getTaskType() == TaskTypeEnum.USER_CURRENT_TRANSFER_REGULAR.getValue()){
            return MatchingMoneyTypeEnum.USER_CAPITAL_CHANGE.getValue();
        }
        if(mqTask.getTaskType() == TaskTypeEnum.USER_REGULAR_TRANSFER_CURRENT.getValue()){
            return MatchingMoneyTypeEnum.USER_CAPITAL_CHANGE.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_CURRENT_TRANSFER_TN.getValue()) {
            return MatchingMoneyTypeEnum.USER_CAPITAL_CHANGE.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_TN_TRANSFER_CURRENT.getValue()) {
            return MatchingMoneyTypeEnum.USER_CAPITAL_CHANGE.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_MATCHED_CURRENT_RELEASE.getValue()) {
            return MatchingMoneyTypeEnum.ASSET_RELEASE.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_MATCHED_REGULAR_RELEASE.getValue()) {
            return MatchingMoneyTypeEnum.ASSET_RELEASE.getValue();
        }
        if (mqTask.getTaskType() == TaskTypeEnum.USER_MATCHED_TN_RELEASE.getValue()) {
            return MatchingMoneyTypeEnum.ASSET_RELEASE.getValue();
        }
        return null;
    }
    
    
    /**
     * 
     * @Description 通过资金队列的类型获取资金队列的权重值
     * @author ZhouRunFa
     * @date 2017年5月10日下午7:25:44
     * 
     * @param moneyType
     * @param sysConfig
     * @return
     * @throws Exception
     */
    public static Integer getMatchinMoneyWeight(Integer moneyType,SysConfigUtil sysConfig) throws Exception {
        if(MatchingMoneyTypeEnum.ASSET_RELEASE.getValue() == moneyType){
            return getMoneyWeight(sysConfig, FundKeyTypeEnum.ASSET_OUT.getValue());
        }
        
        if(MatchingMoneyTypeEnum.USER_CAPITAL_CHANGE.getValue() == moneyType){
            return getMoneyWeight(sysConfig,FundKeyTypeEnum.BUY.getValue());
        }
        return null;
    }
    
    
    
    /**
     * 
     * @Description 用户活期产品提现，新增待匹配资产的资产的权重
     * @author ZhouRunFa
     * @date 2017年4月28日下午5:29:32
     * 
     * @param mqTask
     * @param assetMq
     * @param transOutCount
     * @param topTransOutCnt
     * @param SysConfigUtil
     * @return
     * @throws Exception
     */
    public static Integer getAssetWeight(MqTask mqTask,Integer transOutCount,Integer topTransOutCnt,SysConfigUtil sysConfig) throws Exception {
        if (mqTask.getTaskType() == TaskTypeEnum.CONSUME.getValue()) {
            return  CommonCustomUtil.getAssetWeight(sysConfig, AssetKeyTypeEnum.CONSUME.getValue());
        } else {
            if (transOutCount > topTransOutCnt) {
                return CommonCustomUtil.getAssetWeight(sysConfig, AssetKeyTypeEnum.SELL.getValue());
            } else {
                return CommonCustomUtil.getAssetWeight(sysConfig, AssetKeyTypeEnum.NEW_SELL.getValue());
            }
        }
    }
    
    
    
    
    /**
     * 
     * @Description 用户活期产品提现，新增待匹配资产的资产类型
     * @author ZhouRunFa
     * @date 2017年4月28日下午5:29:38
     * 
     * @param mqTask
     * @param assetMq
     * @param transOutCount
     * @param topTransOutCnt
     * @return
     * @throws Exception
     */
    public static Integer getAssetType(MqTask mqTask,Integer transOutCount,Integer topTransOutCnt) throws Exception {
        if (mqTask.getTaskType() == TaskTypeEnum.CONSUME.getValue()) {
            return  MatchingAssetTypeEnum.CONSUME.getValue();
        } else {
            if (transOutCount > topTransOutCnt) {
                return MatchingAssetTypeEnum.SELL.getValue();
            } else {
                return MatchingAssetTypeEnum.NEW_SELL.getValue();
            }
        }
    }
}
