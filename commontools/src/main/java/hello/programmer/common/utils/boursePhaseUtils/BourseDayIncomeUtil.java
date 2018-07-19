/**
 * @title: BourseDayIncomeUtil.java
 * @package com.asset.utils
 * @description: 简理财
 * @copyright: Copyright (c) 2016
 * @company:北京简变快乐信息技术有限公司
 * 
 * @author zhourf
 * @date 2016年8月13日 下午4:25:26
 */
package hello.programmer.common.utils.boursePhaseUtils;

import com.asset.bean.BourseDayIncomeInfo;
import com.asset.bean.BoursePhase;
import com.asset.enums.IsEndDateInterestEnum;
import com.asset.framework.utils.date.DateUtil;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资产交易所资产日息表生成工具
 * 
 * @author zhourf
 * @create 2016年8月13日 下午4:25:26
 */
public class BourseDayIncomeUtil {

	public static List<BourseDayIncomeInfo> createBourseDayIncomeInfo(List<BoursePhase> BoursePhaseList, Integer isEndDateInterest) throws Exception {
		if (!CollectionUtils.isEmpty(BoursePhaseList)) {
			List<BourseDayIncomeInfo> list = new ArrayList<BourseDayIncomeInfo>();
			for (BoursePhase boursePhase : BoursePhaseList) {
				List<BourseDayIncomeInfo> bourseDayIncomeInfoList = createBourseDayIncomeList(boursePhase.getPlannedTermAmount(), 
						boursePhase.getTermDays(), boursePhase.getId(), boursePhase.getDueDate(), boursePhase.getAssetId(), isEndDateInterest);
				list.addAll(bourseDayIncomeInfoList);
			}
			return list;
		}
		return null;
	}

	/**
	 * 生成某一期还款计划的日息记录列表
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param income 利息 
	 * @param phaseTermCount 当期还款计划计息天数
	 * @param boursePhaseId 还款计划id
	 * @param boursePhaseDueDate 还款计划应还日期
	 * @param assetId 资产id
	 * @return
	 * @throws Exception
	 * @author liunian
	 * @create 2016年10月8日 下午9:08:15
	 */
	private static List<BourseDayIncomeInfo> createBourseDayIncomeList(BigDecimal income,
			Integer phaseTermCount, Long boursePhaseId, Date boursePhaseDueDate, int assetId, Integer isEndDateInterest) throws Exception {
		// 累计日息
		BigDecimal totalIncome = income;
		// 日息
		BigDecimal curIncome = totalIncome.divide(new BigDecimal(phaseTermCount), 16, BigDecimal.ROUND_DOWN);

		List<BourseDayIncomeInfo> list = new ArrayList<BourseDayIncomeInfo>();

		for (int i = 0; i < phaseTermCount; i++) {
			BourseDayIncomeInfo bourseDayIncomeInfo = new BourseDayIncomeInfo();
			if (i == 1) {
				BigDecimal lIncome = totalIncome.subtract(curIncome.multiply(new BigDecimal(phaseTermCount - 1)))
						.setScale(4, BigDecimal.ROUND_DOWN);
				bourseDayIncomeInfo.setPreIncome(lIncome);
			} else {
				bourseDayIncomeInfo.setPreIncome(curIncome.setScale(4, BigDecimal.ROUND_DOWN));
			}
			bourseDayIncomeInfo.setAssetId(assetId);
			bourseDayIncomeInfo.setPhaseId(boursePhaseId);
			if (isEndDateInterest == IsEndDateInterestEnum.YES.getValue()) {
				bourseDayIncomeInfo.setBuinessDate(DateUtil.getReceiveRepayDate(boursePhaseDueDate, -phaseTermCount+i+1));
			} else {
				bourseDayIncomeInfo.setBuinessDate(DateUtil.getReceiveRepayDate(boursePhaseDueDate, -phaseTermCount+i));
			}
			
			list.add(bourseDayIncomeInfo);
		}
		return list;
	}

}
