package hello.programmer.common.utils.loanPhaseUtils;

import com.asset.modules.asset.model.AstLoanPhase;

import java.math.BigDecimal;

public class LoanPhaseUtil {


    /**
     * 计算每日平均利息
     * @author 张伯文
     * createTime 2018/6/26-20:29
     */
    public static BigDecimal calAvgDayInterest(AstLoanPhase loanPhase){
        return loanPhase.getPlannedTermInterest()
                .divide(new BigDecimal(loanPhase.getTermDays()), 2,BigDecimal.ROUND_DOWN);
    }

    /**
     * 计算最后一天剩余利息（用减法）
     * @author 张伯文
     * createTime 2018/6/26-20:30
     */
    public static BigDecimal calLastDayInterest(AstLoanPhase astLoanPhase){
        BigDecimal normalDayInterest=calAvgDayInterest(astLoanPhase);
        BigDecimal beforeAmount=normalDayInterest.multiply(new BigDecimal(astLoanPhase.getTermDays()-1));
        return astLoanPhase.getPlannedTermInterest().subtract(beforeAmount);
    }

    /**
     * 计算最后一天剩余利息（用数据库数据减法）
     * @author 张伯文
     * createTime 2018/6/26-20:30
     */
    public static BigDecimal calLastDayInterestByRealDdatabaseData(AstLoanPhase astLoanPhase,BigDecimal realBeforeAmount){
        return astLoanPhase.getPlannedTermInterest().subtract(realBeforeAmount);
    }
}
