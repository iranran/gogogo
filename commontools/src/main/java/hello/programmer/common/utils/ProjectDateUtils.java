/**
 * @title: ProjectDateUtils.java
 * @package com.asset.utils
 * @description: 简理财
 * @copyright: Copyright (c) 2016
 * @company:北京简变快乐信息技术有限公司
 * 
 * @author zhouRunFa
 * @date 2016年9月22日 下午2:43:43
 */
package hello.programmer.common.utils;

import com.asset.enums.IsEndDateInterestEnum;
import com.asset.framework.utils.date.DateUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * 项目日期工具
 * @author zhouRunFa
 * @create 2016年9月22日 下午2:43:43
 */
public class ProjectDateUtils {
	
	/**
	 * 计算项目的计息天数
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param isEndDateInterest
	 * @return
	 * @return int
	 * @throws ParseException 
	 * @throws
	 * @author zhouRunFa
	 * @create 2016年9月22日 下午2:47:22
	 */
	public static int getProjectInterestDays(int isEndDateInterest, Date interestDate, Date endInterestDate)
			throws ParseException {
		int projectInterestDays = 0;
		// 最后一天计息
		if (Integer.valueOf(isEndDateInterest) == IsEndDateInterestEnum.YES.getValue()) {
			// 计息天数 = 结息日期 - 起息日期 + 1
			projectInterestDays = DateUtil.daysBetweenDate(interestDate, endInterestDate) + 1;
		} else if (Integer.valueOf(isEndDateInterest) == IsEndDateInterestEnum.NO.getValue()) {
			// 结束日不计息
			endInterestDate = DateUtil.parseDate(DateUtil.addDate(endInterestDate, "D", -1), "yyyy-MM-dd");
			// 计息天数 = 结息日期 - 起息日期 + 1
			projectInterestDays = DateUtil.daysBetweenDate(interestDate, endInterestDate) + 1;
		}
		return projectInterestDays;

	}

    /**
     * 根据查询页数和查询条数,计算查询范围
     *
     * @author wulinsong
     */
	public static int[] getLimitParam(int pageNo, int pageSize) {
		int[] limitParam = new int[2];
		if (pageNo > 1) {
			limitParam[0] = (pageNo - 1) * pageSize;
			limitParam[1] = pageNo * pageSize;
		} else {
			limitParam[1] = pageSize;
		}
		return limitParam;
	}

}
