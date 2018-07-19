/**
 * @title: EnumsQuery.java
 * @package com.jianlc.asset.util
 * @description: 简理财
 * @copyright: Copyright (c) 2016
 * @company:北京简变快乐信息技术有限公司
 * 
 * @author zhourf
 * @date 2016年7月29日 下午7:26:19
 */
package hello.programmer.common.util;


import com.jianlc.asset.enums.AssetClassificationEnum;
import com.jianlc.asset.enums.AssetRepayTypeEnum;

import java.util.Map;

/**
 * 枚举查询器
 * @author zhourf
 * @create 2016年7月29日 下午7:26:19
 */
public class EnumsQuery {
      
      
      private Map<Integer, String> assetClassificationEnumMap = AssetClassificationEnum.convert2Map();
      
      private Map<Integer, String> assetRepayTypeEnumMap = AssetRepayTypeEnum.convert2Map();

      public Map<Integer, String> getAssetClassificationEnumMap() {
            return assetClassificationEnumMap;
      }

      public void setAssetClassificationEnumMap(Map<Integer, String> assetClassificationEnumMap) {
            this.assetClassificationEnumMap = assetClassificationEnumMap;
      }

      public Map<Integer, String> getAssetRepayTypeEnumMap() {
            return assetRepayTypeEnumMap;
      }

      public void setAssetRepayTypeEnumMap(Map<Integer, String> assetRepayTypeEnumMap) {
            this.assetRepayTypeEnumMap = assetRepayTypeEnumMap;
      }
      
      
      
}
