package hello.programmer.common.utils.project;

import com.asset.enums.IsEndDateInterestEnum;
import com.asset.enums.ProjectPhaseEnum;
import com.asset.framework.utils.date.DateUtils;
import com.asset.modules.enums.ProjectStatusEnum;
import com.jianlc.asset.api.enums.AssetRepayTypeEnum;

import java.text.ParseException;
import java.util.Date;

/**
 * 项目信息相关常用静态逻辑
 * @author 张伯文
 * @date 2017年11月15日下午8:03:45
 */
public class ProjectUtil {

	public static Date calEndDateByEndInterestDay(Date endInterestDate,Integer isEndDateInterest){
		if(isEndDateInterest.equals(IsEndDateInterestEnum.YES.getValue())){
			return endInterestDate;
		}else{
			return DateUtils.addDays(endInterestDate, 1);
		}
	}
	
	public static boolean isInProjectRange(Date date,Date actualEndDate,Date interestDate,Date endInterestDate) throws ParseException{
		if(actualEndDate!=null){
			return DateUtils.isBetween(date, interestDate, actualEndDate);
		}else{
			return DateUtils.isBetween(date, interestDate, endInterestDate);
		}
	}

	public static String transSql_PageToStatus(ProjectPhaseEnum projectPhase){
		if(projectPhase==null)return "";
		StringBuilder sbwhere=new StringBuilder();
		switch(projectPhase){
			case START:
				sbwhere.append(" and  ali.projectStatus in(" + ProjectStatusEnum.NEW.getKey() + ","
						+ ProjectStatusEnum.RISK_AUDIT_REFUSE.getKey() + "," + ProjectStatusEnum.ASSET_IN_REFUSE.getKey()+ ")  ");
				break;
			case RISK:
				sbwhere.append(" and  ali.projectStatus in (" + ProjectStatusEnum.RISK_AUDIT_READY.getKey()+","+
						ProjectStatusEnum.AUDIT_REFUSE.getKey()+")");
				break;
			case AUDIT:
				sbwhere.append(" and  ali.projectStatus = " + ProjectStatusEnum.WAIT_BUSINESS_DEPARTMENT_AUDIT.getKey());
				break;
			case ASSETIN:
				sbwhere.append(" and  ali.projectStatus = " + ProjectStatusEnum.AUDIT_PASS.getKey());
				break;
			case INVESTCONFIRM:
				sbwhere.append(" and  ali.projectStatus = " + ProjectStatusEnum.INVESTED.getKey());
				break;
			case REPAY:
				sbwhere.append(" and  ali.projectStatus = " + ProjectStatusEnum.REPAYING.getKey());
				break;
			case PREPAY:
				sbwhere.append(" and  ali.projectStatus = " + ProjectStatusEnum.REPAYING.getKey());
				break;
			default:
				break;
		}
		return sbwhere.toString();
	}


	/**
	 * 是否按月计息项目
	 * @author 张伯文
	 * createTime 2018/6/29-16:02
	 */
	public static boolean isMonthRepayType(int repayType){
		if(repayType== AssetRepayTypeEnum.EPILOAN.getValue()
				||repayType== AssetRepayTypeEnum.AVGPRINCIPAL.getValue()
				||repayType== AssetRepayTypeEnum.AVG_CAPITAL.getValue()
				||repayType== AssetRepayTypeEnum.BALLOON_LOAN.getValue() ){
			return true;
		}else{
			return false;
		}
	}
}
