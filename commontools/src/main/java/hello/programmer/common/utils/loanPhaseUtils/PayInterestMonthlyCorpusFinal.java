package hello.programmer.common.utils.loanPhaseUtils;

import com.asset.enums.IsEndDateInterestEnum;
import com.asset.enums.LoanPhaseIsRepaidEnum;
import com.asset.enums.LoanPhaseMoneyTypeEnum;
import com.asset.exception.BizException;
import com.asset.framework.utils.date.DateUtil;
import com.asset.modules.asset.model.AstLoanPhase;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description 生成还款计划：按天计息，按月付息，到期还本
 * <br>支持最后一天计息，固定或不固定还款日期
 * @author 张伯文
 * @date 2016年11月21日下午9:02:46
 */
public class PayInterestMonthlyCorpusFinal {


	private final static int YEARDAYS = 365; //每年天数
	
	
	

	/**
	 * @Description 生成还款计划：按天计息，按月付息，到期还本
	 * <br>支持最后一天计息，固定或不固定还款日期
	 * @author 张伯文
	 * @date 2016年11月21日下午9:04:27
	 * 
	 * @param amount 本金
	 * @param aunualInterestRate 年利率
	 * @param interestCountDays 计息天数，适用于按天计息的项目
	 * @param loanEndTime 贷款结束日期
	 * @param assetId 资产id
	 * @param manageRate 管理费率
	 * @param addRate 加息费率
	 * @param firstTermNum 生成第一个计划的期数号码，一般应为1（如为2，第一个还款计划为第二期）
	 * @param payDay 固定还款日期（1-28，不填以结束日期为还款日期）
	 * @param isLastDayPayInterest 最后一天是否计息（0不计息1计息）
	 * @return
	 * @throws ParseException
	 * @throws BizException
	 */
	public static List<AstLoanPhase> getLoanPhaseByDays(
			BigDecimal amount,
			BigDecimal aunualInterestRate,
			Integer interestCountDays,
			Date loanEndTime,
			Integer assetId,
			BigDecimal manageRate,
			BigDecimal addRate,
			Integer firstTermNum,
			Integer payDay,
			Integer isLastDayPayInterest
			) throws ParseException, BizException
	{
		
		Date releaseTime;//起息日期根据计息天数和贷款截止日期倒推
		if(isLastDayPayInterest==IsEndDateInterestEnum.YES.getValue()){
			//如果最后一天计息，由于计息天数增加了，起息日期要+1才正确
			releaseTime = getMonthlyRepayDate(loanEndTime, -interestCountDays+1);
		}else{
			releaseTime = getMonthlyRepayDate(loanEndTime, -interestCountDays);
		}
		List<AstLoanPhase> loanPhaseList = new ArrayList<AstLoanPhase>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		BigDecimal yearDays = new BigDecimal(YEARDAYS);

		// 起息日期
		
		Date startTime = DateUtil.getDateByDay(releaseTime, 0, DateUtil.BEGIN_SECOND_OF_DAY);
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startTime);
		int sYear = startCal.get(Calendar.YEAR);
		int sMonth = startCal.get(Calendar.MONTH);
		int sday = startCal.get(Calendar.DATE);
		// 结束日期
		Date endTime = DateUtil.getDateByDay(loanEndTime, 0, DateUtil.END_SECOND_OF_DAY);
		
		// 判断日期是否合法
		if (startTime.after(endTime)) {
			throw new BizException("assetId=" + assetId + " 开始时间不能晚于结束时间");
		}
		// 如果固定还款日未设置，默认为结束日期
		if (payDay == null) {
			Calendar cl = Calendar.getInstance();
			cl.setTime(endTime);
			payDay = cl.get(Calendar.DATE);
		}
		// 计算期数
		int mouth = getMonth(startTime, endTime, payDay);

		// 还款日初始值
		Date time = sdf1.parse(sYear + "-" + (sMonth + 1) + "-" + payDay + " " + "23:59:59");
		// 1期以上的
		GregorianCalendar gc = new GregorianCalendar();
		int termNumber = firstTermNum - 1;//还款期数偏移，一般为0（无偏移）
		if (mouth > 1) {
			for (int i = 1; i <= mouth; i++) {
				AstLoanPhase loanPhase = new AstLoanPhase();
				loanPhase.setIsLastDayCount(isLastDayPayInterest==IsEndDateInterestEnum.YES.getValue());
				loanPhase.setAssetId(assetId);// 标ID
				loanPhase.setIsRepaid(LoanPhaseIsRepaidEnum.NO.getValue());// 是否已还，因为刚生成还款计划表，所以此处都是false
				gc.setTime(time);

				// 仅有第一期付款日在起始日后的情况，还款日为当月，其他情况都为下个月
				if (!(i == 1 && sday < payDay)) {
					gc.add(Calendar.MONTH, +1);
				}

				Date nextDate = sdf1.parse(sdf1.format(gc.getTime())); // 倒推一个月

				// 最后一期为结束日期
				if (i == mouth) {
					nextDate = endTime;
				}

				loanPhase.setDueDate(nextDate); // 本期应还款的时间

				int dayNum = 0;// 本期计息天数
				if (i == 1) {// 第一期时间去计息开始日期
					dayNum = daysBetween(startTime, nextDate);
					//填写本还款计划的起息日期和结息日期
					loanPhase.setStartInterestDate(startTime);
					loanPhase.setEndInterestDate(DateUtil.getDateByDay(nextDate, -1, DateUtil.END_SECOND_OF_DAY));
				} else {
					dayNum = daysBetween(time, nextDate);
					//填写本还款计划的起息日期和结息日期
					loanPhase.setStartInterestDate(DateUtil.getDateByDay(time, 0, DateUtil.BEGIN_SECOND_OF_DAY));
					loanPhase.setEndInterestDate(DateUtil.getDateByDay(nextDate, -1, DateUtil.END_SECOND_OF_DAY));
				}
				

				
				// 如果最后一天计息，最后一期多算一天
				if (i == mouth && IsEndDateInterestEnum.YES.getValue() == isLastDayPayInterest) {
					dayNum++;
					loanPhase.setEndInterestDate(nextDate);
				}

				// 计算管理费
				BigDecimal fee = getAmoutCount(Math.abs(dayNum), manageRate, amount, yearDays);

				loanPhase.setPhaseNumber((short) (termNumber + i));// 本期还款计划期数
				loanPhase.setTermDays(Math.abs(dayNum));
				if (i == mouth) {
					loanPhase.setIsLastTerm(true);
					// 最后一期还款计划
					//BigDecimal termInterest = getAmoutCountLast(Math.abs(dayNum), aunualInterestRate, amount, yearDays,interestCountDays);

					BigDecimal termInterest = getInterestAmoutCountLast(Math.abs(dayNum), aunualInterestRate, amount, yearDays,interestCountDays,loanPhaseList);
					BigDecimal thisTermAmount = termInterest.add(amount); // 本息

					//fee = getAmoutCountLast(Math.abs(dayNum), manageRate, amount, yearDays,interestCountDays);
					fee = getManageAmoutCountLast(Math.abs(dayNum), manageRate, amount, yearDays,interestCountDays,loanPhaseList);

					loanPhase.setPlannedTermAmount(thisTermAmount); // 本息
					loanPhase.setPlannedTermInterest(termInterest);// 利息
					loanPhase.setPlannedTermPrincipal(amount);// 本金
					loanPhase.setTermRemainingPrincipal(BigDecimal.ZERO);// 本期还款后，剩余本金
				} else {
					BigDecimal termInterest = getAmoutCount(Math.abs(dayNum), aunualInterestRate, amount, yearDays);
					loanPhase.setPlannedTermAmount(termInterest); // 本息
					loanPhase.setPlannedTermInterest(termInterest);// 利息
					loanPhase.setPlannedTermPrincipal(new BigDecimal("0"));// 本金
					loanPhase.setTermRemainingPrincipal(amount);// 本期还款后，剩余本金
				}
				// 加息利息
				if(addRate==null){
					addRate=BigDecimal.ZERO;
				}
				BigDecimal increaseInterest = loanPhase.getPlannedTermInterest().multiply(addRate)
						.divide(aunualInterestRate, 2, BigDecimal.ROUND_DOWN);
				loanPhase.setIncreaseInterest(increaseInterest);
				
				if (fee.compareTo(BigDecimal.ZERO) > 0) {
					loanPhase.setManageFee(fee);
				} else {
					loanPhase.setManageFee(BigDecimal.ZERO);
				}
				loanPhase.setMoneyType(LoanPhaseMoneyTypeEnum.ALL.getValue());
				loanPhase.setFee(BigDecimal.ZERO);
				loanPhaseList.add(loanPhase);
				time = nextDate; // 保存这个月，为下次倒推一个月用
			}
		} else {
			// 只有一期还款的情况
			AstLoanPhase loanPhase = new AstLoanPhase();
			loanPhase.setIsLastDayCount(isLastDayPayInterest==IsEndDateInterestEnum.YES.getValue());
			loanPhase.setIsLastTerm(true);
			loanPhase.setAssetId(assetId);// 标ID
			loanPhase.setIsRepaid(LoanPhaseIsRepaidEnum.NO.getValue());// 是否已还，因为刚生成还款计划表，所以此处都是false
			// 只有 一期还款计划
			loanPhase.setPhaseNumber((short) (termNumber + 1));// 本期还款计划期数
			loanPhase.setDueDate(endTime); // 本期应还款的时间
			//填写本还款计划的起息日期和结息日期
			loanPhase.setStartInterestDate(startTime);
			loanPhase.setEndInterestDate(endTime);
			
			int dayNum = daysBetween(startTime, endTime);
			// 如果最后一天计息，最后一期多算一天
			if (IsEndDateInterestEnum.YES.getValue() == isLastDayPayInterest) {
				dayNum++;
			}else{
				loanPhase.setEndInterestDate(DateUtil.getDateByDay(endTime, -1, DateUtil.END_SECOND_OF_DAY));
			}

			
			//BigDecimal termInterest = getAmoutCountLast(Math.abs(dayNum), aunualInterestRate, amount, yearDays,interestCountDays);
			BigDecimal termInterest = getInterestAmoutCountLast(Math.abs(dayNum), aunualInterestRate, amount, yearDays,interestCountDays,loanPhaseList);

			BigDecimal thisTermAmount = termInterest.add(amount);
			loanPhase.setPlannedTermAmount(thisTermAmount); // 本息
			loanPhase.setPlannedTermInterest(termInterest);// 利息
			loanPhase.setPlannedTermPrincipal(amount);// 本金
			loanPhase.setTermDays(Math.abs(dayNum));
			// 计算管理费
			
			//BigDecimal fee = getAmoutCountLast(Math.abs(dayNum), manageRate, amount, yearDays,interestCountDays);
			BigDecimal fee = getManageAmoutCountLast(Math.abs(dayNum), manageRate, amount, yearDays,interestCountDays,loanPhaseList);

			if (fee.compareTo(BigDecimal.ZERO) > 0) {
				loanPhase.setManageFee(fee);
			} else {
				loanPhase.setManageFee(BigDecimal.ZERO);
			}
			loanPhase.setTermRemainingPrincipal(BigDecimal.ZERO);// 本期还款后，剩余本金
			// 加息利息
			if(addRate==null){
				addRate=BigDecimal.ZERO;
			}
			BigDecimal increaseInterest = loanPhase.getPlannedTermInterest().multiply(addRate)
					.divide(aunualInterestRate, 2, BigDecimal.ROUND_DOWN);
			loanPhase.setIncreaseInterest(increaseInterest);
			loanPhase.setMoneyType(LoanPhaseMoneyTypeEnum.ALL.getValue());
			loanPhase.setFee(BigDecimal.ZERO);
			loanPhaseList.add(loanPhase);
		}

		return loanPhaseList;
	}
	
	/**
	 * 计算本期应还利息
	 * @param days 计息天数
	 * @param aunualInterestRate 年化利率
	 * @param amount 本金
	 * @param yearDays 常量：一年天数，目前是365
	 * @return 应还的利息
	 */
	private static BigDecimal getAmoutCount(int days,BigDecimal aunualInterestRate,BigDecimal amount,BigDecimal yearDays ){
			return amount.multiply(aunualInterestRate.divide(new BigDecimal(100))).multiply(new BigDecimal(days)).divide(yearDays, 2, BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 计算最后一期期应还利息
	 * @param days 计息天数
	 * @param aunualInterestRate 年化利率
	 * @param amount 本金
	 * @param yearDays 常量：一年天数，目前是365
	 * @param totalDays 总计息天数
	 * @return 应还的利息
	 */
//	private static BigDecimal getAmoutCountLast(int days,BigDecimal aunualInterestRate,BigDecimal amount,BigDecimal yearDays,int totalDays ){
//		if(totalDays==days){
//			return getAmoutCount(days, aunualInterestRate, amount, yearDays);
//		}else{
//			BigDecimal total=getAmoutCount(totalDays, aunualInterestRate, amount, yearDays);
//			BigDecimal totalDivideOne=getAmoutCount(totalDays-days, aunualInterestRate, amount, yearDays);
//			return total.subtract(totalDivideOne);
//		}
//	}


	private static BigDecimal getInterestAmoutCountLast(int days,BigDecimal aunualInterestRate,BigDecimal amount,
			BigDecimal yearDays,int totalDays, List<AstLoanPhase> loanPhaseList){
		if(totalDays==days){
			return getAmoutCount(days, aunualInterestRate, amount, yearDays);
		}

		BigDecimal total = getAmoutCount(totalDays, aunualInterestRate, amount, yearDays);
		BigDecimal _totalDivideOne = BigDecimal.ZERO;
		for(AstLoanPhase phase : loanPhaseList){
			if(phase.getPlannedTermInterest().compareTo(BigDecimal.ZERO) > 0){
				_totalDivideOne = _totalDivideOne.add(phase.getPlannedTermInterest());
			}
		}

		return total.subtract(_totalDivideOne);

	}

	private static BigDecimal getManageAmoutCountLast(int days,BigDecimal aunualInterestRate,BigDecimal amount,
			BigDecimal yearDays,int totalDays, List<AstLoanPhase> loanPhaseList){
		if(totalDays==days){
			return getAmoutCount(days, aunualInterestRate, amount, yearDays);
		}

		BigDecimal total = getAmoutCount(totalDays, aunualInterestRate, amount, yearDays);
		BigDecimal _totalDivideOne = BigDecimal.ZERO;
		for(AstLoanPhase phase : loanPhaseList){
			if(phase.getManageFee().compareTo(BigDecimal.ZERO) > 0){
				_totalDivideOne = _totalDivideOne.add(phase.getManageFee());
			}
		}
		return total.subtract(_totalDivideOne);
	}

	 /**
     * 返回某个按月还款的Loan在第n期的还款时间。
     * <br>
     * 某标在6月3日投满，并经确认放款给借款者（都在6月3日
     * 23：59之前完成）。则此标第一个还款日应是7月2日。在7月2日晚23：59分或之前还款，都算是正常。（一个更典型的例子：6月1日放款的标，第一个还款日应是6月30日，而不是7月1日。） <br>
     * <br>
     * 如果在7月3日0：00之后还款，则视为逾期，如果还款人在7月4日凌晨1：00还款，逾期了25个小时，则视为逾期2天。
     * 
     * @param releaseTime 计息起始日期
     * @param phaseNumber 期数（第n期，从1开始）
     * @return 在第n期的还款时间
     */
	private static Date getMonthlyRepayDate(Date releaseTime, int phaseNumber)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(releaseTime);

        //目标时间为加1月减1秒，根据期数平移
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.SECOND, -1);
        calendar.add(Calendar.DATE, phaseNumber);
        return calendar.getTime();
    }
	
	
	
	/**
	 * 两日期相差天数
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	private static int daysBetween(Date smdate,Date bdate) throws ParseException     
    {    
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis(); 
        long between_days=(time2-time1)/(1000*3600*24);  
       return Integer.parseInt(String.valueOf(between_days));           
    }
	/**
	 * 计算需要还款期数（月份数）
	 * @param startDate
	 * @param endDate
	 * @param day 固定还款日
	 * @return 需要还款期数
	 * @throws ParseException 
	 */
	private static int getMonth(Date startDate, Date endDate,Integer day) throws ParseException {
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

	public static void main(String[] args)throws Exception 
	{
//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date endTime = sdf1.parse("2010-05-31 12:22:22");
//		GregorianCalendar gc=new GregorianCalendar(); 
//		gc.setTime(endTime); 
//		gc.add(2, 1);
//		System.out.println(sdf1.format(gc.getTime()));
//		gc.setTime(endTime); 
//		gc.add(2, 2);
//		
//		AssetRepayTypeEnum aa=AssetRepayTypeEnum.AstRepayTypeEnum_10;
//		int ss=aa.getValue();
//		String tt=aa.getDesc();
//		int payDay = gc.get(Calendar.DATE);
//		payDay=payDay;
		
	}
	
}//class end
