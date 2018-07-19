package hello.programmer.common.utils.loanPhaseUtils;

import com.asset.consts.CommonDef;
import com.asset.enums.IsEndDateInterestEnum;
import com.asset.enums.LoanPhaseIsAutoEnum;
import com.asset.enums.LoanPhaseIsRepaidEnum;
import com.asset.enums.LoanPhaseMoneyTypeEnum;
import com.asset.framework.utils.date.DateUtil;
import com.asset.modules.asset.model.AstLoanPhase;
import com.asset.modules.asset.utils.product.EPILoan;
import com.jianlc.asset.api.bean.base.LoanInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EpiLoan {

	/**
	 * 获取等额本息总利息
	 * @param loanInfo
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getEpAllInterest(LoanInfo loanInfo) throws Exception {
		BigDecimal allInterest = new BigDecimal(0);
        // 起息日期、结息日间隔的月数
        int totalPhaseNum = DateUtil.getMonthSpace(loanInfo.getInterestDate(), loanInfo.getEndInterestDate());
        // 借款金额
        BigDecimal amount = loanInfo.getAmount();
        // 项目利率
        BigDecimal projectRate = loanInfo.getProjectRate();
        // 项目利率每月应还本息
        BigDecimal projectRateRepay = EPILoan.averageCapitalPlusInterest(totalPhaseNum, amount,
                projectRate.divide(new BigDecimal(100)));
        // 项目利率月利率
        BigDecimal projectMonthRate = projectRate.divide(CommonDef.N_100).divide(CommonDef.N_12,
                8, BigDecimal.ROUND_HALF_UP);
        BigDecimal lastPhasePrincipal = amount;
        for(int i = 1; i < (totalPhaseNum + 1); i++){
        	// 项目利率算出的月息
            BigDecimal projectRateInterest = lastPhasePrincipal.multiply(projectMonthRate).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            // 项目利率应还款本金
            BigDecimal projectRateRepayCapital = projectRateRepay.subtract(projectRateInterest);
            // 剩余还款本金
            BigDecimal termRemainingPrincipal = lastPhasePrincipal.subtract(projectRateRepayCapital);
            if(i == totalPhaseNum){// 最后一期
                lastPhasePrincipal = lastPhasePrincipal.subtract(projectRateRepayCapital);
                projectRateRepayCapital = projectRateRepayCapital.add(lastPhasePrincipal);
                lastPhasePrincipal = BigDecimal.ZERO;
                termRemainingPrincipal = BigDecimal.ZERO;
            }
            allInterest = allInterest.add(projectRateInterest);
            lastPhasePrincipal = termRemainingPrincipal;
        }
		
		return allInterest;
	}
	
    public static List<AstLoanPhase> createEpLoanPhase(LoanInfo loanInfo,Integer opId) throws Exception {
        // 起息日期
        Date interestDate = loanInfo.getInterestDate();
        // 起息日期、结息日间隔的月数
        int totalPhaseNum = DateUtil.getMonthSpace(loanInfo.getInterestDate(), loanInfo.getEndInterestDate());
        // 还款计划列表
        List<AstLoanPhase> loanPhaseList = new ArrayList<AstLoanPhase>(totalPhaseNum);
        // 借款金额
        BigDecimal amount = loanInfo.getAmount();
        // 项目利率
        BigDecimal projectRate = loanInfo.getProjectRate();
        // 借款人利率
        BigDecimal borrowerRate = loanInfo.getRealBorrowerRate();
        // 最后一天是否计息标志
        Integer isEndDateInterest = loanInfo.getIsEndDateInterest();
        
        
        // 项目利率每月应还本息
        BigDecimal projectRateRepay = EPILoan.averageCapitalPlusInterest(totalPhaseNum, amount,
                projectRate.divide(new BigDecimal(100)));
        // 借款人利率每月应还本息
        BigDecimal borrowerRateRepay = EPILoan.averageCapitalPlusInterest(totalPhaseNum, amount,
                borrowerRate.divide(new BigDecimal(100)));
        
        
        // 项目利率月利率
        BigDecimal projectMonthRate = projectRate.divide(CommonDef.N_100).divide(CommonDef.N_12,
                8, BigDecimal.ROUND_HALF_UP);
        // 借款人利率月利率
        BigDecimal borrowerMonthRate = borrowerRate.divide(CommonDef.N_100).divide(CommonDef.N_12,
                8, BigDecimal.ROUND_HALF_UP);
        
        BigDecimal lastPhasePrincipal = amount;
        for(int i = 1; i < (totalPhaseNum + 1); i++){
            // 利息还款计划
            AstLoanPhase interestPhase = new AstLoanPhase();
            // 本金还款计划
            AstLoanPhase capitalPhase = new AstLoanPhase();
            // 管理费还款计划
            AstLoanPhase manageFeePhase = new AstLoanPhase();
            // 项目利率算出的月息
            BigDecimal projectRateInterest = lastPhasePrincipal.multiply(projectMonthRate).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            // 项目利率应还款本金
            BigDecimal projectRateRepayCapital = projectRateRepay.subtract(projectRateInterest);
            // 借款人利率算出的月息 
            BigDecimal borrowerRateInterest = lastPhasePrincipal.multiply(borrowerMonthRate).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            // 借款人利率应还款本金
            BigDecimal borrowerRateCapital = borrowerRateRepay.subtract(borrowerRateInterest);
            
            // 管理费
            BigDecimal manageFee = (borrowerRateInterest.add(borrowerRateCapital)).subtract(projectRateInterest).subtract(projectRateRepayCapital);
            
            // 还款计划结束日
            Date dueDate = DateUtil.getMonthlyRepayDate(interestDate, i);
            // 还款计划起息日期
            Date startInterestDate = DateUtil.getMonthlyRepayDate(interestDate, (i-1));
            // 还款计划周期
            int termDays = DateUtil.daysBetweenDate(startInterestDate,dueDate);
            // 还款计划结息日期
            Date endInterestDate = DateUtil.getDateByDay(startInterestDate, termDays-1, DateUtil.END_SECOND_OF_DAY);
                    

            
            // 剩余还款本金
            BigDecimal termRemainingPrincipal = lastPhasePrincipal.subtract(projectRateRepayCapital);
            

            
            if(i == totalPhaseNum){// 最后一期
                lastPhasePrincipal = lastPhasePrincipal.subtract(projectRateRepayCapital);
                projectRateRepayCapital = projectRateRepayCapital.add(lastPhasePrincipal);
                lastPhasePrincipal = BigDecimal.ZERO;
                termRemainingPrincipal = BigDecimal.ZERO;
                
                interestPhase.setIsLastTerm(true);
                capitalPhase.setIsLastTerm(true);
                manageFeePhase.setIsLastTerm(true);
                
                if(IsEndDateInterestEnum.YES.getValue() == isEndDateInterest){// 不计息
                    endInterestDate = DateUtil.getDateByDay(startInterestDate, termDays, DateUtil.END_SECOND_OF_DAY);
                    termDays = termDays+1;
                }
            }
            
            // 生成利息的还款计划
            interestPhase.setAssetId(loanInfo.getAssetId());// 资产Id
            interestPhase.setDueDate(DateUtil.getMonthlyRepayDate(interestDate,(i-1)));// 预计还款日
            interestPhase.setPlannedTermAmount(projectRateInterest);// 本息
            interestPhase.setPlannedTermInterest(projectRateInterest);// 利息
            interestPhase.setPlannedTermPrincipal(BigDecimal.ZERO);
            interestPhase.setPhaseNumber((short) i);// 期号
            interestPhase.setIsRepaid(LoanPhaseIsRepaidEnum.NO.getValue());// 还款状态
            interestPhase.setTermDays(termDays);// 计息天数
            interestPhase.setIsAuto(LoanPhaseIsAutoEnum.NO.getValue());// 自动还款标志
            interestPhase.setManageFee(BigDecimal.ZERO);// 管理费
            interestPhase.setUpdateId(opId);// 修改用户
            interestPhase.setMoneyType(LoanPhaseMoneyTypeEnum.INTEREST.getValue());// 利息类型
            interestPhase.setStartInterestDate(DateUtil.getDateByDay(startInterestDate,DateUtil.BEGIN_SECOND_OF_DAY));// 起息日期
            interestPhase.setEndInterestDate(endInterestDate);// 结息日期
            interestPhase.setTermRemainingPrincipal(termRemainingPrincipal);
            interestPhase.setFee(BigDecimal.ZERO);
            interestPhase.setIncreaseInterest(BigDecimal.ZERO);
            interestPhase.setIsLastDayCount(false);
            // 生成本金的还款计划
            capitalPhase.setAssetId(loanInfo.getAssetId());// 资产Id
            capitalPhase.setDueDate(dueDate);// 预计还款日
            capitalPhase.setPlannedTermAmount(projectRateRepayCapital);// 本息
            capitalPhase.setPlannedTermInterest(BigDecimal.ZERO);// 利息
            capitalPhase.setPlannedTermPrincipal(projectRateRepayCapital);
            capitalPhase.setPhaseNumber((short) i);// 期号
            capitalPhase.setIsRepaid(LoanPhaseIsRepaidEnum.NO.getValue());// 还款状态
            capitalPhase.setTermDays(termDays);// 计息天数
            capitalPhase.setIsAuto(LoanPhaseIsAutoEnum.NO.getValue());// 自动还款标志
            capitalPhase.setManageFee(BigDecimal.ZERO);// 管理费
            capitalPhase.setUpdateId(opId);// 修改用户
            capitalPhase.setMoneyType(LoanPhaseMoneyTypeEnum.PRICIPLE.getValue());// 利息类型
            capitalPhase.setStartInterestDate(DateUtil.getDateByDay(startInterestDate,DateUtil.BEGIN_SECOND_OF_DAY));// 起息日期
            capitalPhase.setEndInterestDate(endInterestDate);// 结息日期
            capitalPhase.setTermRemainingPrincipal(termRemainingPrincipal);
            capitalPhase.setFee(BigDecimal.ZERO);
            capitalPhase.setIncreaseInterest(BigDecimal.ZERO);
            capitalPhase.setIsLastDayCount(false);
            // 生成管理费的还款计划
            manageFeePhase.setAssetId(loanInfo.getAssetId());// 资产Id
            manageFeePhase.setDueDate(dueDate);// 预计还款日
            manageFeePhase.setPlannedTermAmount(BigDecimal.ZERO);// 本息
            manageFeePhase.setPlannedTermInterest(BigDecimal.ZERO);// 利息
            manageFeePhase.setPlannedTermPrincipal(BigDecimal.ZERO);
            manageFeePhase.setPhaseNumber((short) i);// 期号
            manageFeePhase.setIsRepaid(LoanPhaseIsRepaidEnum.NO.getValue());// 还款状态
            manageFeePhase.setTermDays(termDays);// 计息天数
            manageFeePhase.setIsAuto(LoanPhaseIsAutoEnum.NO.getValue());// 自动还款标志
            manageFeePhase.setManageFee(manageFee);// 管理费
            manageFeePhase.setUpdateId(opId);// 修改用户
            manageFeePhase.setMoneyType(LoanPhaseMoneyTypeEnum.MANAGE_FEE.getValue());// 利息类型
            manageFeePhase.setStartInterestDate(DateUtil.getDateByDay(startInterestDate,DateUtil.BEGIN_SECOND_OF_DAY));// 起息日期
            manageFeePhase.setEndInterestDate(endInterestDate);// 结息日期
            manageFeePhase.setTermRemainingPrincipal(termRemainingPrincipal);
            manageFeePhase.setFee(BigDecimal.ZERO);
            manageFeePhase.setIncreaseInterest(BigDecimal.ZERO);
            manageFeePhase.setIsLastDayCount(false);
            
            lastPhasePrincipal = capitalPhase.getTermRemainingPrincipal();
            
            loanPhaseList.add(interestPhase);
            loanPhaseList.add(capitalPhase);
            loanPhaseList.add(manageFeePhase);
        }
        return loanPhaseList;
    }
}
