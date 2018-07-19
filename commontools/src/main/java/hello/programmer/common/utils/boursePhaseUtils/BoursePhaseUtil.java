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
package hello.programmer.common.utils.boursePhaseUtils;

import com.alibaba.fastjson.JSON;
import com.asset.bean.BoursePhaseList;
import com.asset.enums.IsEndDateInterestEnum;
import com.asset.exception.BizException;
import com.asset.framework.utils.date.DateUtil;
import com.jianlc.asset.api.enums.AssetRepayTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 资产交易所资产还款计划生成工具
 * 
 * @author zhourf
 * @create 2016年8月13日 下午1:46:59
 */
public class BoursePhaseUtil {

	private static Logger logger = LoggerFactory.getLogger(BoursePhaseUtil.class);

	private static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 生产还款计划
	 * @param assetId
	 * @param principal
	 * @param interestRate
	 * @param manageFeeRate
	 * @param interestStartDate
	 * @param interestEndDate
	 * @param endDate
	 * @param repayType
	 * @param fixedRepayDay
	 * @param firstTermNum
	 * @param isEndDayInterest
	 * @return
	 * @throws Exception
	 */
	public static BoursePhaseList createAstPhase(Integer assetId, BigDecimal principal, BigDecimal interestRate,
			BigDecimal manageFeeRate, Date interestStartDate, Date interestEndDate, Date endDate, Integer repayType,
			Integer fixedRepayDay, Integer firstTermNum, Integer isEndDayInterest) throws Exception {

		interestStartDate = sdfTime.parse(sdfDate.format(interestStartDate) + " 00:00:00");
		interestEndDate = sdfTime.parse(sdfDate.format(interestEndDate) + " 23:59:59");
		endDate = sdfTime.parse(sdfDate.format(endDate) + " 23:59:59");
		
		// 参数校验
		paramCheck(assetId, principal, interestRate, manageFeeRate, interestStartDate, interestEndDate, endDate,
				repayType, fixedRepayDay, firstTermNum, isEndDayInterest);

		// 计息天数 = 结息日期 - 起息日期
		int interestDays = 0;
		try {
			interestDays = DateUtil.daysBetweenDate(interestStartDate, interestEndDate) + 1;
		} catch (ParseException e) {
			logger.error("日期解析错误，assetId=" + assetId);
			throw new BizException("日期解析错误", e);
		}

		// 生成还款计划
		BoursePhaseList boursePhaseList = null;
		if (repayType == AssetRepayTypeEnum.DAYOFMONTH.getValue()
				|| repayType == AssetRepayTypeEnum.DAYOFMONTHENDDATENOINTEREST.getValue()) {
			boursePhaseList = DayOfMonth.dayInterestMonthRepayEndPricipal(assetId, principal, interestRate, manageFeeRate,
					interestStartDate, interestDays, endDate, fixedRepayDay, firstTermNum, isEndDayInterest);
		} else if (repayType == AssetRepayTypeEnum.DAYOFONE.getValue()
				|| repayType == AssetRepayTypeEnum.DAYOFONE_ENDDAYINTEREST.getValue()) {
			boursePhaseList = DayOfOne.dayOfOne(assetId, principal, interestRate, manageFeeRate, interestDays, endDate, firstTermNum);
		}

		return boursePhaseList;
	}

	/**
	 * 生成本金还款计划
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param principal
	 * @param interestRate
	 * @param manageFeeRate
	 * @param isEndDayInterest
	 * @param interestDate
	 * @param endDate
	 * @author liunian
	 * @create 2016年10月8日 上午11:58:34
	 */
	// private static List<BoursePhase> genPrincipalPhaseList(Integer assetId,
	// BigDecimal principal, boolean isEndDayInterest, Date interestDate, Date
	// endDate) {
	// List<BoursePhase> principalPhaseList = new ArrayList<BoursePhase>();
	//
	//
	//
	// return principalPhaseList;
	// }

	private static void paramCheck(Integer assetId, BigDecimal principal, BigDecimal interestRate,
			BigDecimal manageFeeRate, Date interestStartDate, Date interestEndDate, Date endDate, Integer repayType,
			Integer fixedRepayDay, Integer firstTermNum, Integer isEndDayInterest) throws BizException {
		if (assetId == null || assetId <= 0) {
			logger.info("资产id不合法，assetId={}", assetId);
			throw new BizException("资产id不合法，assetId=" + assetId);
		}
		if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
			logger.info("资产本金不合法，principal={}", principal);
			throw new BizException("资产本金不合法，principal=" + principal);
		}
		if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) <= 0) {
			logger.info("资产年化利率不合法，interestRate={}", interestRate);
			throw new BizException("资产年化利率不合法，interestRate=" + interestRate);
		}
		if (manageFeeRate == null || manageFeeRate.compareTo(BigDecimal.ZERO) < 0) {
			logger.info("资产管理费率不合法，manageFeeRate={}", manageFeeRate);
			throw new BizException("资产管理费率不合法，manageFeeRate=" + manageFeeRate);
		}
		if (interestStartDate == null) {
			logger.info("资产起息日期不合法，interestStartDate={}", interestStartDate);
			throw new BizException("资产起息日期不合法，interestStartDate=" + interestStartDate);
		}
		if (interestEndDate == null) {
			logger.info("资产结息日期不合法，interestEndDate={}", interestEndDate);
			throw new BizException("资产结息日期不合法，interestEndDate=" + interestEndDate);
		}
		if (endDate == null) {
			logger.info("资产结束日期不合法，endDate={}", endDate);
			throw new BizException("资产结束日期不合法，endDate=" + endDate);
		}
		if (interestStartDate.after(interestEndDate)) {
			logger.info("assetId=" + assetId + " 起息时间不能晚于结息时间");
			throw new BizException("assetId=" + assetId + " 起息时间不能晚于结息时间");
		}
		if (interestEndDate.after(endDate)) {
			logger.info("assetId=" + assetId + "结息时间不能晚于结束时间");
			throw new BizException("assetId=" + assetId + "结息时间不能晚于结束时间");
		}
		if (repayType == null || repayType <= 0) {
			logger.info("资产还款方式不合法，repayType={}", endDate);
			throw new BizException("资产还款方式不合法，repayType=" + endDate);
		}
		if (fixedRepayDay != null && (fixedRepayDay <= 0 || fixedRepayDay > 28)) {
			logger.info("资产固定还款日只能是1~28日，fixedRepayDay={}", fixedRepayDay);
			throw new BizException("资产固定还款日只能是1~28日，fixedRepayDay=" + fixedRepayDay);
		}
		if (firstTermNum == null || firstTermNum <= 0) {
			logger.info("资产起始还款期数不合法，repayType={}", firstTermNum);
			throw new BizException("资产起始还款期数不合法，repayType=" + firstTermNum);
		}
		if (isEndDayInterest == null || !IsEndDateInterestEnum.isValueExist(isEndDayInterest)) {
			logger.info("资产最后一天计息参数不合法，isEndDayInterest={}", isEndDayInterest);
			throw new BizException("资产最后一天计息参数不合法，isEndDayInterest=" + isEndDayInterest);
		}
	}

	

	public static void main(String[] args) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Integer assetId = 1;
			BigDecimal principal = new BigDecimal("10000");
			BigDecimal interestRate = new BigDecimal("10");
			BigDecimal manageFeeRate = new BigDecimal("2");
			Date interestStartDate = df.parse("2016-09-02 10:34:34");
			Date interestEndDate = df.parse("2016-12-26 10:34:34");
			Date endDate = df.parse("2016-12-26 10:34:34");
			Integer repayType = AssetRepayTypeEnum.DAYOFMONTH.getValue();
//			Integer repayType = AssetRepayTypeEnum.AstRepayTypeEnum_15.getValue();
			Integer fixedRepayDay = 15;
			Integer firstTermNum = 2;
			Integer isEndDateInterest = IsEndDateInterestEnum.YES.getValue();
			BoursePhaseList boursePhaseList = createAstPhase(assetId, principal, interestRate, manageFeeRate, interestStartDate, interestEndDate, endDate,
					repayType, fixedRepayDay, firstTermNum, isEndDateInterest);
			System.out.println("boursePhaseList = " + JSON.toJSONString(boursePhaseList));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}