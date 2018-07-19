package hello.programmer.common.utils.boursePhaseUtils;

import com.asset.bean.BoursePhase;
import com.asset.bean.BoursePhaseList;
import com.asset.consts.CommonDef;
import com.asset.enums.BoursePhaseMoneyTypeEnum;
import com.asset.exception.BizException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一次性还本付息生成还款计划
 * 
 * @author liunian
 */
public class DayOfOne {
	
	/**
	 * 一次性还本付息生成还款计划
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
	 * @create 2016年10月9日 下午4:38:32
	 */
	public static BoursePhaseList dayOfOne(Integer assetId, BigDecimal principal, BigDecimal interestRate,
			BigDecimal manageFeeRate, Integer interestDays, Date endDate, Integer firstTermNum) throws BizException, ParseException {
		BoursePhaseList boursePhaseList = new BoursePhaseList();
		List<BoursePhase> principalPhaseList = new ArrayList<BoursePhase>();
		List<BoursePhase> interestPhaseList = new ArrayList<BoursePhase>();
		List<BoursePhase> manageFeePhaseList = new ArrayList<BoursePhase>();

		// 本金还款计划
		BoursePhase principalPhase = new BoursePhase();
		principalPhase.setAssetId(assetId);
		principalPhase.setPlannedTermAmount(principal);
		principalPhase.setDueDate(endDate);
		principalPhase.setPhaseNumber(firstTermNum);
		principalPhase.setTermDays(interestDays);
		principalPhase.setMoneyType(BoursePhaseMoneyTypeEnum.PRINCIPAL.getValue());
		principalPhaseList.add(principalPhase);

		// 利息还款计划
		if (interestRate.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal interest = principal.multiply(interestRate).multiply(new BigDecimal(interestDays))
					.divide(CommonDef.N_100).divide(CommonDef.N_365, 2, BigDecimal.ROUND_HALF_UP);
			BoursePhase interestPhase = new BoursePhase();
			interestPhase.setAssetId(assetId);
			interestPhase.setPlannedTermAmount(interest);
			interestPhase.setDueDate(endDate);
			interestPhase.setPhaseNumber(firstTermNum);
			interestPhase.setTermDays(interestDays);
			interestPhase.setMoneyType(BoursePhaseMoneyTypeEnum.INTEREST.getValue());
			interestPhaseList.add(interestPhase);
		}

		// 管理费还款计划
		if (manageFeeRate.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal manageFee = principal.multiply(manageFeeRate).multiply(new BigDecimal(interestDays))
					.divide(CommonDef.N_100).divide(CommonDef.N_365, 2, BigDecimal.ROUND_HALF_UP);
			BoursePhase manageFeePhase = new BoursePhase();
			manageFeePhase.setAssetId(assetId);
			manageFeePhase.setPlannedTermAmount(manageFee);
			manageFeePhase.setDueDate(endDate);
			manageFeePhase.setPhaseNumber(firstTermNum);
			manageFeePhase.setTermDays(interestDays);
			manageFeePhase.setMoneyType(BoursePhaseMoneyTypeEnum.MANAGEFEE.getValue());
			manageFeePhaseList.add(manageFeePhase);
		}

		boursePhaseList.setPrincipalPhaseList(principalPhaseList);
		boursePhaseList.setInterestPhaseList(interestPhaseList);
		boursePhaseList.setManageFeePhaseList(manageFeePhaseList);
		return boursePhaseList;
	}
}
