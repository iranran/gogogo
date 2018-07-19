/**
 * @title MathUtil
 * @package com.jianlc.asset.util
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/5/17 14:14
 */
package hello.programmer.common.util;

import java.math.BigDecimal;

/**
 * @author liwei
 * @Description
 * @date 2018/5/17 14:14
 */
public class MathUtil{

    private MathUtil(){    }

    /**
     * Returns the smaller of two {@code BigDecimal} values.
     * @param a an argument.
     * @param b another argument.
     * @return the smaller of {@code a} and {@code b}.
     */
    public static BigDecimal min(BigDecimal a, BigDecimal b){
        return a.compareTo(b) <= 0 ? a : b;
    }

    /**
     * Returns the greater of two {@code BigDecimal} values.
     * @param a an argument.
     * @param b another argument.
     * @return the larger of {@code a} and {@code b}.
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b){
        return a.compareTo(b) >= 0 ? a : b;
    }
}