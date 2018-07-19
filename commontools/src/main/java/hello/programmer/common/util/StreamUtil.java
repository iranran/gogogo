/**
 * @title StreamUtil
 * @package com.jianlc.asset.util
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/5/22 10:10
 */
package hello.programmer.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * lambda stream util
 * @author liwei
 * @create 2018/5/22 10:10
 */
public class StreamUtil {

//    /**
//     * 对原始数据类型的list进行group
//     * 对于 1,1,2,2,2进行group运算，得到 map 1-2,2-3
//     * @param data
//     * @param <T>
//     * @return
//     */
//    public static <T> Map<T,Long> group(List<T> data){
//        return data.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//    }


    /**
     * 将List中 T 类型数据转换成 R 类型
     * @param data
     * @param mapFunc
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T,R> List<R> map(List<T> data, Function<T, R> mapFunc) {
        return data.stream().map(mapFunc).collect(Collectors.toList());
    }

    /**
     * 过滤掉不合规则的数据
     * @param data
     * @param filterFunc
     * @param <T>
     * @return
     */
    public static <T> List<T> filter(List<T> data, Predicate<T> filterFunc) {
        return data.stream().filter(filterFunc).collect(Collectors.toList());
    }

    /**
     * 先对list进行filter，再对filter剩下的数据进行map
     * @param data
     * @param mapFunc
     * @param filterFunc
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T,R> List<R> filterAndMap(List<T> data, Predicate<T> filterFunc, Function<T, R> mapFunc) {
        return data.stream().filter(filterFunc).map(mapFunc).collect(Collectors.toList());
    }

    /**
     * 先对list进行map操作,再对map的结果进行filter
     * @param data
     * @param mapFunc
     * @param filterFunc
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T,R> List<R> mapAndFilter(List<T> data, Function<T, R> mapFunc, Predicate<R> filterFunc) {
        return data.stream().map(mapFunc).filter(filterFunc).collect(Collectors.toList());
    }



    public static <T,R> Map<R,List<T>> group(List<T> data, Function<T,R> groupKey){
        return data.stream().collect(Collectors.groupingBy(groupKey));
    }




}