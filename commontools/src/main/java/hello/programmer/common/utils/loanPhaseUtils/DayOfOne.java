/**
 * @title: BourseRepayPhaseUtil.java
 * @package com.asset.modules.bourse.utils
 * @description: 简理财
 * @copyright: Copyright (c) 2016
 * @company:北京简变快乐信息技术有限公司
 * 
 * @author zhourf
 * @date 2016年8月13日 下午1:46:59
 */
package hello.programmer.common.utils.loanPhaseUtils;

import com.asset.consts.CommonDef;
import com.asset.enums.IsEndDateInterestEnum;
import com.asset.enums.IsPrechargeInterestEnum;
import com.asset.enums.LoanPhaseIsRepaidEnum;
import com.asset.enums.LoanPhaseMoneyTypeEnum;
import com.asset.framework.utils.date.DateUtil;
import com.asset.modules.asset.model.AstLoanPhase;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一次性还本付息还款计划生成类
 * 
 * @author liunian
 * @create 2016年9月18日 上午11:14:33
 */
public class DayOfOne {

	/**
	 * 生成一次性还本付息还款计划
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param amount  本金
	 * @param annualInterestRate  年化利率
	 * @param leastInterestDays  最小计息天数
	 * @param termCount  项目周期(天)
	 * @param endDate  结束日期
	 * @param assetId  资产id
	 * @param manageRate  管理费率
	 * @param firstTermNum  生成第一个计划的期数号码（一般为1，如为2，则生成的第一期期数为2）
	 * @param isLastDayPayInterest 最后一天是否计息（0不计息，1计息）
	 * @return
	 * @throws Exception
	 * @return List<AstLoanPhase>
	 * @throws
	 * @author liunian
	 * @create 2016年9月18日 上午11:14:23
	 */
	public static List<AstLoanPhase> createAstPhase(
			BigDecimal amount, 
			BigDecimal annualInterestRate,
			Integer leastInterestDays,
			Integer termCount,
			Date endDate, 
			Integer assetId, 
			BigDecimal manageRate, 
			Integer firstTermNum,
			Integer isLastDayPayInterest,
			Integer isPrechargeInterest
			) throws Exception {
		// 还款计划
		List<AstLoanPhase> list = createReceivePlanByDays(amount, annualInterestRate, leastInterestDays,
				termCount, endDate, manageRate, firstTermNum, assetId,isLastDayPayInterest,isPrechargeInterest);
		return list;
	}

	/**
	 * 通过日期的天数创建还款计划
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param amount  本金
	 * @param annualInterestRate  年化利率
	 * @param leastInterestDays  最小计息天数
	 * @param termCount  项目周期（天）
	 * @param endDate  结束日期
	 * @param assetId  资产id
	 * @param manageRate  管理费率
	 * @param firstTermNum  生成第一个计划的期数号码（一般为1，如为2，则生成的第一期期数为2）
	 * @param isLastDayPayInterest 最后一天是否计息（0不计息，1计息）
	 * @param isPrechargeInterest 
	 * @return
	 * @throws ParseException
	 * @return List<AstLoanPhase>
	 * @throws 
	 * @author liunian
	 * @create 2016年9月18日 下午2:15:40
	 */
	private static List<AstLoanPhase> createReceivePlanByDays(
			BigDecimal amount, 
			BigDecimal annualInterestRate, 
			Integer leastInterestDays,
			Integer termCount,
			Date endDate,
			BigDecimal manageRate, 
			Integer firstTermNum,
			Integer assetId,
			Integer isLastDayPayInterest, 
			Integer isPrechargeInterest
			) throws ParseException {
		List<AstLoanPhase> loanPhaseList = new ArrayList<AstLoanPhase>(1);

		AstLoanPhase loanPhase = new AstLoanPhase();
		// 资产ID
		loanPhase.setAssetId(assetId);	
		
		//--利息、管理费、罚息的计算--//
		BigDecimal monthlyInterest;
		BigDecimal manageFee;
		BigDecimal fee=BigDecimal.ZERO;
		if(isPrechargeInterest == IsPrechargeInterestEnum.YES.getValue()){
			//预收息项目：计息天数小于最小计息天数时，差额产生的利息和管理费计入罚息
			// 应还利息-改为根据计息天数计算
			monthlyInterest = amount.multiply(annualInterestRate).multiply(new BigDecimal(termCount))
					.divide(CommonDef.N_100).divide(CommonDef.N_365, 2, BigDecimal.ROUND_HALF_UP);
			//管理费-根据计息天数计算
			manageFee = amount.multiply(manageRate).multiply(new BigDecimal(termCount)).divide(CommonDef.N_100)
					.divide(CommonDef.N_365, 2, BigDecimal.ROUND_HALF_UP);
			if(leastInterestDays>termCount){
				fee = amount.multiply(manageRate.add(annualInterestRate)).multiply(new BigDecimal(leastInterestDays-termCount)).divide(CommonDef.N_100)
						.divide(CommonDef.N_365, 2, BigDecimal.ROUND_HALF_UP);
			}
		}else{
			//非预收息项目：垫资不足7天多收利息计入利息
			Integer actualTermCount=leastInterestDays>termCount?leastInterestDays:termCount;
			monthlyInterest = amount.multiply(annualInterestRate).multiply(new BigDecimal(actualTermCount))
					.divide(CommonDef.N_100).divide(CommonDef.N_365, 2, BigDecimal.ROUND_HALF_UP);
			manageFee = amount.multiply(manageRate).multiply(new BigDecimal(actualTermCount)).divide(CommonDef.N_100)
					.divide(CommonDef.N_365, 2, BigDecimal.ROUND_HALF_UP);
		}

		
		
		loanPhase.setFee(fee);
		loanPhase.setManageFee(manageFee);
		// 期数
		loanPhase.setPhaseNumber(firstTermNum.shortValue());
		loanPhase.setIsRepaid(LoanPhaseIsRepaidEnum.NO.getValue());
		// 本期应还款的时间
		loanPhase.setDueDate(endDate);
		// 一次性还本金和利息
		loanPhase.setPlannedTermAmount(amount.add(monthlyInterest));
		loanPhase.setPlannedTermPrincipal(amount);
		loanPhase.setPlannedTermInterest(monthlyInterest);
		loanPhase.setTermRemainingPrincipal(BigDecimal.ZERO);

		loanPhase.setTermDays(termCount);
		// 加息利息-没有，设置为0
		loanPhase.setIncreaseInterest(BigDecimal.ZERO);
		loanPhaseList.add(loanPhase);
		//是否最后一天计息
		loanPhase.setIsLastDayCount(isLastDayPayInterest==IsEndDateInterestEnum.YES.getValue());
		//填写本还款计划的起息日期和结息日期
		if(loanPhase.getIsLastDayCount()){
			loanPhase.setEndInterestDate(DateUtil.getDateByDay(endDate, 0, DateUtil.END_SECOND_OF_DAY));
			loanPhase.setStartInterestDate(DateUtil.getDateByDay(endDate, -termCount+1, DateUtil.BEGIN_SECOND_OF_DAY));
		}else{
			loanPhase.setEndInterestDate(DateUtil.getDateByDay(endDate, -1, DateUtil.END_SECOND_OF_DAY));
			loanPhase.setStartInterestDate(DateUtil.getDateByDay(endDate, -termCount, DateUtil.BEGIN_SECOND_OF_DAY));
		}
		//填写本还款计划的起息日期和结息日期
		if(loanPhase.getIsLastDayCount()){
			loanPhase.setEndInterestDate(endDate);
			loanPhase.setStartInterestDate(DateUtil.getDateByDay(endDate, -termCount+1, DateUtil.BEGIN_SECOND_OF_DAY));
		}else{
			loanPhase.setEndInterestDate(DateUtil.getDateByDay(endDate, -1, DateUtil.END_SECOND_OF_DAY));
			loanPhase.setStartInterestDate(DateUtil.getDateByDay(endDate, -termCount, DateUtil.BEGIN_SECOND_OF_DAY));
		}

		//是否最后一期
		loanPhase.setIsLastTerm(true);
		//还款计划资金类型为全部
		loanPhase.setMoneyType(LoanPhaseMoneyTypeEnum.ALL.getValue());
		return loanPhaseList;

	}
}
