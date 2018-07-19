package hello.programmer.common.utils.boursePhaseUtils;

import com.asset.bean.BoursePhase;
import com.asset.bean.BoursePhaseList;
import com.asset.consts.CommonDef;
import com.asset.enums.BoursePhaseMoneyTypeEnum;
import com.asset.enums.IsEndDateInterestEnum;
import com.asset.exception.BizException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DayOfMonth {

	private static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 按月计息到期还本
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param assetId
	 * @param principal
	 * @param interestRate
	 * @param manageFeeRate
	 * @param interestStartDate
	 * @param interestEndDate
	 * @param endDate
	 * @return
	 * @throws BizException
	 * @author liunian
	 * @throws ParseException
	 * @create 2016年10月9日 下午4:40:30
	 */
	public static BoursePhaseList dayInterestMonthRepayEndPricipal(Integer assetId, BigDecimal principal,
			BigDecimal interestRate, BigDecimal manageFeeRate, Date interestStartDate, Integer interestDays,
			Date endDate, Integer fixedRepayDay, Integer firstTermNum, Integer isEndDayInterest)
					throws BizException, ParseException {
		BoursePhaseList boursePhaseList = new BoursePhaseList();
		List<BoursePhase> principalPhaseList = new ArrayList<BoursePhase>();
		List<BoursePhase> interestPhaseList = new ArrayList<BoursePhase>();
		List<BoursePhase> manageFeePhaseList = new ArrayList<BoursePhase>();

		// 本金还款计划
		BoursePhase principalPhase = new BoursePhase();
		principalPhase.setAssetId(assetId);
		principalPhase.setPlannedTermAmount(principal);
		principalPhase.setDueDate(endDate);
		principalPhase.setPhaseNumber(1);
		principalPhase.setTermDays(interestDays);
		principalPhase.setMoneyType(BoursePhaseMoneyTypeEnum.PRINCIPAL.getValue());
		principalPhaseList.add(principalPhase);

		// 如果固定还款日未设置，默认为结束日期
		if (fixedRepayDay == null) {
			Calendar cl = Calendar.getInstance();
			cl.setTime(endDate);
			fixedRepayDay = cl.get(Calendar.DATE);
		}
		// 计算期数
		int phaseCount = getPhaseCount(interestStartDate, endDate, fixedRepayDay);

		// 还款日初始值
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(interestStartDate);
		int sYear = startCal.get(Calendar.YEAR);
		int sMonth = startCal.get(Calendar.MONTH);
		int sday = startCal.get(Calendar.DATE);
		Date time = sdfTime.parse(sYear + "-" + (sMonth + 1) + "-" + fixedRepayDay + " " + "23:59:59");

		GregorianCalendar gc = new GregorianCalendar();
		int termNumber = firstTermNum - 1;// 还款期数偏移，一般为0（无偏移）
		BigDecimal totalInterest = BigDecimal.ZERO;//总利息，用于计算最后一期利息，最后一期利息=总利息 - 前面所有期数总利息
		BigDecimal totalManageFee = BigDecimal.ZERO;//总管理费，用于计算最后一期管理费，最后一期管理费=总管理费 - 前面所有期数总管理费
		for (int i = 1; i <= phaseCount; i++) {
			gc.setTime(time);

			// 仅有第一期付款日在起始日后的情况，还款日为当月，其他情况都为下个月
			if (!(i == 1 && sday < fixedRepayDay)) {
				gc.add(Calendar.MONTH, +1);
			}
			// 本期应还款日期
			Date nextDate = gc.getTime();

			// 最后一期为结束日期
			if (i == phaseCount) {
				nextDate = endDate;
			}

			// 本期计息天数
			int dayNum = 0;
			if (i == 1) {// 第一期时间去计息开始日期
				dayNum = daysBetween(interestStartDate, nextDate);
				// 如果最后一天计息，则还款日当天计息，第一期多一天
				if (isEndDayInterest == IsEndDateInterestEnum.YES.getValue()) {
					dayNum++;
				}
			} else {
				dayNum = daysBetween(time, nextDate);
			}

			// 计算利息
			BigDecimal interest = BigDecimal.ZERO;
			// 计算管理费
			BigDecimal manageFee = BigDecimal.ZERO;
			// 最后一期还款计划
			if (i == phaseCount) {
				interest = getAmoutCount(Math.abs(interestDays), interestRate, principal).subtract(totalInterest);
				manageFee = getAmoutCount(Math.abs(interestDays), manageFeeRate, principal).subtract(totalManageFee);
			} else {
				interest = getAmoutCount(Math.abs(dayNum), interestRate, principal);
				manageFee = getAmoutCount(Math.abs(dayNum), manageFeeRate, principal);
			}

			// 利息还款计划
			if (manageFeeRate.compareTo(BigDecimal.ZERO) > 0) {
				BoursePhase interestPhase = new BoursePhase();
				interestPhase.setAssetId(assetId);
				interestPhase.setMoneyType(BoursePhaseMoneyTypeEnum.INTEREST.getValue());
				interestPhase.setPlannedTermAmount(interest);
				interestPhase.setDueDate(nextDate); // 本期应还款的时间
				interestPhase.setPhaseNumber(termNumber + i);// 本期还款计划期数
				interestPhase.setTermDays(Math.abs(dayNum));
				interestPhaseList.add(interestPhase);
			}

			// 管理费还款计划
			if (manageFeeRate.compareTo(BigDecimal.ZERO) > 0) {
				BoursePhase manageFeePhase = new BoursePhase();
				manageFeePhase.setAssetId(assetId);
				manageFeePhase.setMoneyType(BoursePhaseMoneyTypeEnum.MANAGEFEE.getValue());
				manageFeePhase.setPlannedTermAmount(manageFee);
				manageFeePhase.setDueDate(nextDate);
				manageFeePhase.setPhaseNumber(termNumber + i);
				manageFeePhase.setTermDays(Math.abs(dayNum));
				manageFeePhaseList.add(manageFeePhase);
			}
			time = nextDate; // 保存这个月，为下次倒推一个月用
			totalInterest = totalInterest.add(interest);
			totalManageFee = totalManageFee.add(manageFee);
		}
		boursePhaseList.setPrincipalPhaseList(principalPhaseList);
		boursePhaseList.setInterestPhaseList(interestPhaseList);
		boursePhaseList.setManageFeePhaseList(manageFeePhaseList);
		return boursePhaseList;
	}
	
	/**
	 * 计算需要还款期数（月份数）
	 * 
	 * @param startDate
	 * @param endDate
	 * @param day
	 *            固定还款日
	 * @return 需要还款期数
	 * @throws ParseException
	 */
	private static int getPhaseCount(Date startDate, Date endDate, Integer day) throws ParseException {
		long monthday;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		startDate = sdf.parse(sdf.format(startDate));
		endDate = sdf.parse(sdf.format(endDate));

		Calendar starCal = Calendar.getInstance();
		starCal.setTime(startDate);

		int sYear = starCal.get(Calendar.YEAR);
		int sMonth = starCal.get(Calendar.MONTH);
		int sDay = starCal.get(Calendar.DATE);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int eYear = endCal.get(Calendar.YEAR);
		int eMonth = endCal.get(Calendar.MONTH);
		int eDay = endCal.get(Calendar.DATE);

		monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));

		if (eDay > day) {
			monthday = monthday + 1;
		}

		if (sDay < day) {
			monthday = monthday + 1;
		}

		return Integer.parseInt(String.valueOf(monthday));
	}

	/**
	 * 两日期相差天数
	 * 
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	private static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算本期应还利息
	 * 
	 * @param days
	 *            计息天数
	 * @param aunualInterestRate
	 *            年化利率
	 * @param amount
	 *            本金
	 * @param yearDays
	 *            常量：一年天数，目前是365
	 * @return 应还的利息
	 */
	private static BigDecimal getAmoutCount(int days, BigDecimal aunualInterestRate, BigDecimal amount) {
		return amount.multiply(aunualInterestRate.divide(new BigDecimal(100))).multiply(new BigDecimal(days))
				.divide(CommonDef.N_365, 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 计算最后一期期应还利息
	 * 
	 * @param days
	 *            计息天数
	 * @param aunualInterestRate
	 *            年化利率
	 * @param amount
	 *            本金
	 * @param yearDays
	 *            常量：一年天数，目前是365
	 * @param totalDays
	 *            总计息天数
	 * @return 应还的利息
	 */
//	private static BigDecimal getAmoutCountLast(int days, BigDecimal aunualInterestRate, BigDecimal amount,
//			int totalDays) {
//		if (totalDays == days) {
//			return getAmoutCount(days, aunualInterestRate, amount);
//		} else {
//			BigDecimal total = getAmoutCount(totalDays, aunualInterestRate, amount);
//			BigDecimal totalDivideOne = getAmoutCount(totalDays - days, aunualInterestRate, amount);
//			return total.subtract(totalDivideOne);
//		}
//	}
}
