package hello.programmer.common.utils.page;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SearchParamUtil {
	
	public static Map<String, Object> getPrepayProjectListParams(HttpServletRequest request){
		Map<String, Object> params = new HashMap<String, Object>();
		//标志
  		String flag = request.getParameter("flag");
  		params.put("flag",flag);
		//项目名称
  		String projectName = request.getParameter("projectName");
  		params.put("projectName",projectName);
  		// 担保公司
		String companyName = request.getParameter("companyName");
		params.put("companyName", companyName);
		//权证人
		String proxyBorrowerName = request.getParameter("proxyBorrowerName");
		params.put("proxyBorrowerName", proxyBorrowerName);
		//借款人名称
		String borrowerName = request.getParameter("borrowerName");
		params.put("borrowerName", borrowerName);
		//挂牌编号
		String putCode = request.getParameter("putCode");
		params.put("putCode", putCode);
		//挂牌spv
		String spvBorrowerId = request.getParameter("spvBorrowerId");
		params.put("spvBorrowerId",spvBorrowerId);
		return params;
	}
	
	public static Map<String, Object> getAssetProjectListParams(final HttpServletRequest request){
		Map<String, Object> params = new HashMap<String, Object>();
		// 名称
		String name = request.getParameter("name");
		params.put("name", name);
		// 线下合同编号
		String contractCode = request.getParameter("contractCode");
		params.put("contractCode", contractCode);
		// 借款人名称
		String borrowerName = request.getParameter("borrowerName");
		params.put("borrowerName", borrowerName);
		// 担保公司
		String companyName = request.getParameter("companyName");
		params.put("companyName", companyName);
		// 结束日期开始
		String startEndDate = request.getParameter("startEndDate");
		params.put("startEndDate", startEndDate);
		// 挂牌spv
		String spvBorrowerId = request.getParameter("spvBorrowerId");
		params.put("spvBorrowerId", spvBorrowerId);
		// 结束日期结束
		String endEndDate = request.getParameter("endEndDate");
		if (StringUtils.isNotBlank(endEndDate)) {
			endEndDate = endEndDate + " 23:59:59 ";
		}
		params.put("endEndDate", endEndDate);
		// 还款方式
		String repayType = request.getParameter("repayType");
		params.put("repayType", repayType);

		// 起息日期开始
		String startInterestDate = request.getParameter("startInterestDate");
		params.put("startInterestDate", startInterestDate);
		// 起息日期结束
		String endInterestDate = request.getParameter("endInterestDate");
		if (StringUtils.isNotBlank(endInterestDate)) {
			endInterestDate = endInterestDate + " 23:59:59 ";
		}
		params.put("endInterestDate", endInterestDate);

		// 投资日期开始
		String startInvestDate = request.getParameter("startInvestDate");
		params.put("startInvestDate", startInvestDate);
		// 投资日期结束
		String endInvestDate = request.getParameter("endInvestDate");
		if (StringUtils.isNotBlank(endInvestDate)) {
			endInvestDate = endInvestDate + " 23:59:59 ";
		}
		params.put("endInvestDate", endInvestDate);
		
		return params;
	}
	

	public static Map<String, Object> getProjectInfoListParams(final HttpServletRequest request){
		Map<String, Object> params = new HashMap<String, Object>();
		// 小V标识
        String xV = request.getParameter("xV");
        params.put("xV", xV);
		// 小V标识
        String xiaoCheng = request.getParameter("xiaoCheng");
        params.put("xiaoCheng", xiaoCheng);
		// 名称
		String name = request.getParameter("name");
		params.put("name", name);
		// 标志
		String flag = request.getParameter("flag");
		params.put("flag", flag);
		// 项目状态
		String projectStatus = request.getParameter("projectStatus");
		params.put("projectStatus", projectStatus);
		// 还款日期开始
		String startRepayDate = request.getParameter("startRepayDate");
		params.put("startRepayDate", startRepayDate);
		// 还款日期结束
		String endRepayDate = request.getParameter("endRepayDate");
		if (StringUtils.isNotBlank(endRepayDate)) {
			endRepayDate = endRepayDate + " 23:59:59 ";
		}
		params.put("endRepayDate", endRepayDate);
		// 线下合同编号
		String contractCode = request.getParameter("contractCode");
		params.put("contractCode", contractCode);
		// 借款人名称
		String borrowerName = request.getParameter("borrowerName");
		params.put("borrowerName", borrowerName);
		// 代理借款人名称
		String proxyBorrowerName = request.getParameter("proxyBorrowerName");
		params.put("proxyBorrowerName", proxyBorrowerName);
		// 担保公司
		String companyName = request.getParameter("companyName");
		params.put("companyName", companyName);
		//挂牌编号
		String putCode = request.getParameter("putCode");
		params.put("putCode", putCode);
		//挂牌spv
		String spvBorrowerId = request.getParameter("spvBorrowerId");
		params.put("spvBorrowerId", spvBorrowerId);
		// 起息日期开始
		String startInterestDate = request.getParameter("startInterestDate");
		params.put("startInterestDate", startInterestDate);
		// 起息日期结束
		String endInterestDate = request.getParameter("endInterestDate");
		if (StringUtils.isNotBlank(endInterestDate)) {
			endInterestDate = endInterestDate + " 23:59:59 ";
		}
		params.put("endInterestDate", endInterestDate);
		// 结束日期开始
		String startEndDate = request.getParameter("startEndDate");
		params.put("startEndDate", startEndDate);
		// 结束日期结束
		String endEndDate = request.getParameter("endEndDate");
		if (StringUtils.isNotBlank(endEndDate)) {
			endEndDate = endEndDate + " 23:59:59 ";
		}
		params.put("endEndDate", endEndDate);
		// 项目期限开始
		String startTermCount = request.getParameter("startTermCount");
		params.put("startTermCount", startTermCount);
		// 项目期限结束
		String endTermCount = request.getParameter("endTermCount");
		params.put("endTermCount", endTermCount);
		// 项目利率开始
		String startProjectRate = request.getParameter("startProjectRate");
		params.put("startProjectRate", startProjectRate);
		// 项目利率结束
		String endProjectRate = request.getParameter("endProjectRate");
		params.put("endProjectRate", endProjectRate);
		// 还款方式
		String repayType = request.getParameter("repayType");
		params.put("repayType", repayType);
		// 固定还款日
		String repayDay = request.getParameter("repayDay");
		params.put("repayDay", repayDay);

		// 投资日期开始
		String startInvestDate = request.getParameter("startInvestDate");
		params.put("startInvestDate", startInvestDate);
		// 投资日期结束
		String endInvestDate = request.getParameter("endInvestDate");
		if (StringUtils.isNotBlank(endInvestDate)) {
			endInvestDate = endInvestDate + " 23:59:59 ";
		}
		params.put("endInvestDate", endInvestDate);

		// 项目金额开始
		String startAmount = request.getParameter("startAmount");
		params.put("startAmount", startAmount);

		// 项目金额结束
		String endAmount = request.getParameter("endAmount");
		params.put("endAmount", endAmount);

		// 拆借日期开始
		String startLendingDate = request.getParameter("startLendingDate");
		params.put("startLendingDate", startLendingDate);
		// 拆借日期结束
		String endLendingDate = request.getParameter("endLendingDate");
		if (StringUtils.isNotBlank(endLendingDate)) {
			endLendingDate = endLendingDate + " 23:59:59 ";
		}
		params.put("endLendingDate", endLendingDate);
        // 挂牌模式
        String putPattern = request.getParameter("putPattern");
        params.put("putPattern", putPattern);
        //结束日是否计息
        String isEndDateInterest = request.getParameter("isEndDateInterest");
        params.put("isEndDateInterest", isEndDateInterest);
        //勾选的id集合
        String ids = request.getParameter("ids");
        params.put("ids", ids);
		//交易所
		String bourseName2 = request.getParameter("bourseName2");
		if(null!=bourseName2 && bourseName2.equals("全部")){
			params.put("bourseName", null);
		}else{
			params.put("bourseName", bourseName2);
		}
		//挂牌起始日期
		String startPutDate = request.getParameter("startPutDate");
		params.put("startPutDate", startPutDate);
		//挂牌结束日期
		String endPutDate = request.getParameter("endPutDate");
		params.put("endPutDate", endPutDate);

		String tonglianProduceNum = request.getParameter("tonglianProduceNum");
		params.put("tonglianProduceNum", tonglianProduceNum);
        return params;
	}
}
