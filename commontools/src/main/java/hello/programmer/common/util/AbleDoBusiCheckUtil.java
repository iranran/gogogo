package hello.programmer.common.util;

import com.jianlc.asset.configCenter.JianlcConfigCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Slf4j
public class AbleDoBusiCheckUtil {

    private AbleDoBusiCheckUtil(){}


    public static AbleDoBusiCheckUtil getInstance(){
        return AbleDoBusiCheckEnum.INSTANCE.getInstance();
    }


    public enum AbleDoBusiCheckEnum {
        INSTANCE;

        private AbleDoBusiCheckUtil ableDoBusiCheckUtil;

        private AbleDoBusiCheckEnum() {
            ableDoBusiCheckUtil = new AbleDoBusiCheckUtil();
        }
        public AbleDoBusiCheckUtil getInstance() {
            return ableDoBusiCheckUtil;
        }
    }



    /**
     * @description: 同用小时业务校验
     * @author: RunFa.Zhou
     * @create: 2018/6/6
     **/
    private boolean commonHourCheck(String ableDoHour, String curDateStr, String busiName) {

        if (StringUtils.isEmpty(ableDoHour)) {
            log.error("{}:{}:时间点校验:参数配置非法！", Thread.currentThread(), busiName);
            return false;
        }

        if (ableDoHour.indexOf("-") == -1) {
            log.error("{}:{}:时间点校验:参数配置不正确！", Thread.currentThread(), busiName);
            return false;
        }

        String[] timeArrays = ableDoHour.split(",");


        boolean ableDo = false;

        if (timeArrays.length == 0) {
            log.error("{}:{}:时间点校验:无配置！校验通过", Thread.currentThread(), busiName);
            return true;
        } else {

            String timePrefix = DateUtils.formatDate(new Date(), null) + " ";

            for (String timeItem : timeArrays) {

                if (StringUtils.isEmpty(timeItem)) {
                    log.error("{}:{}:时间点校验:时间点配置空！", Thread.currentThread(), busiName);
                    return false;
                }
                String[] timeArray = timeItem.split("-");

                if (timeArray == null || timeArray.length > 2) {
                    log.error("{}:{}:时间点校验:时间点配置空异常或者非法！", Thread.currentThread(), busiName);
                    return false;
                }

                String startStr = timeArray[0];
                String endStr = timeArray[1];

                if (StringUtils.isEmpty(startStr)) {
                    log.error("{}:{}:时间点校验:时间点配置空异常或者非法！", Thread.currentThread(), busiName);
                    return false;
                }

                if (StringUtils.isEmpty(endStr)) {
                    log.error("{}:{}:时间点校验:时间点配置空异常或者非法！", Thread.currentThread(), busiName);
                    return false;
                }
                try {
                    Date startDate = DateUtils.parseDateTimeFmt(timePrefix + startStr);
                    Date endDate = DateUtils.parseDate(timePrefix + endStr);
                    Date curDate = DateUtils.parseDate(curDateStr);

                    if (startDate.compareTo(curDate) <= 0 && endDate.compareTo(curDate) >= 0) {
                        log.info("{}:{}:时间点校验:当前时间{}点,在匹配时间{}至{}中...触发{}",
                                Thread.currentThread(),busiName, curDateStr, startStr, endDate,busiName);
                        ableDo = true;
                        break;
                    }else {
                        log.info("{}:{}:时间点校验,当前时间{},不再在时间{}至{}中...,不触发{}",
                                Thread.currentThread(),busiName, DateUtils.formatDateTime(curDate),
                                DateUtils.formatDateTime(startDate),DateUtils.formatDateTime(endDate),busiName);
                    }
                } catch (Exception e) {
                    log.error("{}:{}:时间点校验:校验时间解析异常！startStr={},endStr={},curDateStr={}",
                            Thread.currentThread(), busiName, startStr, endStr, curDateStr);
                    throw e;
                }
            }
        }
        return ableDo;
    }



    /**
     * @description: 同用分钟业务校验
     * @author: RunFa.Zhou
     * @create: 2018/6/3
     **/
    private boolean commonMinuteCheck(String  ableDoMinute, Integer minute,String busiName) {

        if(StringUtils.isEmpty(ableDoMinute)){
            log.error("{}:{}:匹配时间点校验参数配置非法！",Thread.currentThread(),busiName);
            return false;
        }

        if(ableDoMinute.indexOf("-") == -1){
            log.error("{}:{}:分钟校验,参数配置非法！",Thread.currentThread(),busiName);
            return false;
        }

        String [] timeArrays = ableDoMinute.split(",");


        boolean ableDo = false;

        if(timeArrays.length == 0 ){
            log.error("{}:{}:分钟校验,参数配置非法！",Thread.currentThread(),busiName);
            return false;
        }else {
            for(String timeItem : timeArrays){

                if(StringUtils.isEmpty(timeItem)){
                    log.error("{}:{}:分钟校验,时间点配置空异常或者非法！",Thread.currentThread(),busiName);
                    return false;
                }
                String [] timeArray = timeItem.split("-");

                if(timeArray == null || timeArray.length>2){
                    log.error("{}:{}:分钟校验,匹配时间校验:时间点配置空异常或者非法！",Thread.currentThread(),busiName);
                    return false;
                }

                String startStr = timeArray [0];
                String endStr = timeArray[1];

                if(StringUtils.isEmpty(startStr)){
                    log.error("{}:{}:分钟校验,时间点配置空异常或者非法！",Thread.currentThread(),busiName);
                    return false;
                }

                if(StringUtils.isEmpty(endStr)){
                    log.error("{}:{}:分钟校验,时间点配置空异常或者非法！",Thread.currentThread(),busiName);
                    return false;
                }

                Integer start = Integer.parseInt(startStr);
                Integer end = Integer.parseInt(endStr);

                if(start >= end){
                    log.error("{}:{}:分钟校验,时间点配置不正确！",Thread.currentThread(),busiName);
                    return false;
                }

                if (start <= minute && minute < end) {
                    log.info("{}:{}:分钟校验,当前时间{}分,在时间{}至{}中...",
                            Thread.currentThread(),busiName, minute, start, end);
                    ableDo = true;
                    break;
                }else {
                    log.info("{}:{}:分钟校验,当前时间{}分,不再在时间{}至{}中...",
                            Thread.currentThread(),busiName, minute, start, end);
                }
            }
        }
        return ableDo;
    }

    /**
     * @description: 匹配时间点校验
     * @author: RunFa.Zhou
     * @create: 2018/6/5
     **/
    public boolean ableMatch(JianlcConfigCenter jianlcConfigCenter){

        String timeType =jianlcConfigCenter.getNoGlobalLockMatch().get("ableMatchTimeType");
        if(StringUtils.isEmpty(timeType)){
            log.error("{}:匹配时间校验:匹配时间校验类型空异常",Thread.currentThread());
            return false;
        }
        if("hour".equals(timeType)){
            String curDateStr = DateUtils.formatDateTime(new Date());
            String  ableMatchHour = jianlcConfigCenter.getNoGlobalLockMatch().get("ableMatchHour");
            return commonHourCheck(ableMatchHour,curDateStr,"资产匹配");

        } else if("minute".equals(timeType)){
            String minuteStr = DateUtils.getMinute();
            Integer minute = Integer.parseInt(minuteStr);
            String  ableMatchMinute = jianlcConfigCenter.getNoGlobalLockMatch().get("ableMatchMinute");
            return commonMinuteCheck(ableMatchMinute,minute,"资产匹配分钟");
        }else {
            log.error("{}:匹配时间校验:未知匹配时间校验类型",Thread.currentThread());
            return false;
        }
    }

    /**
     * @description: 匹配时间点校验
     * @author: RunFa.Zhou
     * @create: 2018/6/5
     **/
    public boolean ableForceMatch(JianlcConfigCenter jianlcConfigCenter){
        String curDateStr = DateUtils.formatDateTime(new Date());
        String ableForceHour = jianlcConfigCenter.getNoGlobalLockMatch().get("ableForceHour");
        return commonHourCheck(ableForceHour, curDateStr,"强制匹配");
    }

    /**
     * @description: 预匹配记录状态更新时间点校验
     * @author: RunFa.Zhou
     * @create: 2018/6/5
     **/
    public boolean ableUpdatePreMatchedRecord(JianlcConfigCenter jianlcConfigCenter){
        String curDateStr = DateUtils.formatDateTime(new Date());
        String ableForceHour = jianlcConfigCenter.getNoGlobalLockMatch().get("ableUpdatePreMatchedRecordHour");
        return commonHourCheck(ableForceHour, curDateStr,"预匹配记录状态更新");
    }

    public boolean ableUpdatePreMatchedRecordMonitor(JianlcConfigCenter jianlcConfigCenter){
        String curDateStr = DateUtils.formatDateTime(new Date());
        String ableForceHour = jianlcConfigCenter.getNoGlobalLockMatch().get("ableUpdatePreMatchedRecordMonitorHour");
        return commonHourCheck(ableForceHour, curDateStr,"预匹配记录状态更新监控");
    }

    public boolean ableOutMatchedRecordUpdate(JianlcConfigCenter jianlcConfigCenter){
        String curDateStr = DateUtils.formatDateTime(new Date());
        String ableForceHour = jianlcConfigCenter.getNoGlobalLockMatch().get("ableOutMatchedRecordUpdateHour");
        return commonHourCheck(ableForceHour, curDateStr,"D+1日对前一天出库记录进行更改");
    }

    public boolean ableAssetOut(JianlcConfigCenter jianlcConfigCenter){
        String curDateStr = DateUtils.formatDateTime(new Date());
        String ableForceHour = jianlcConfigCenter.getNoGlobalLockMatch().get("ableAssetOutHour");
        return commonHourCheck(ableForceHour, curDateStr,"资产出库");
    }

    public boolean ableAssetOutMonitor(JianlcConfigCenter jianlcConfigCenter){
        String curDateStr = DateUtils.formatDateTime(new Date());
        String ableForceHour = jianlcConfigCenter.getNoGlobalLockMatch().get("ableAssetOutMonitorHour");
        return commonHourCheck(ableForceHour, curDateStr,"资产出库监控");
    }


    /**
     * @description: 可资金变动时间点校验
     * @author: RunFa.Zhou
     * @create: 2018/6/4
     **/
    public boolean ableCapitalChange(JianlcConfigCenter jianlcConfigCenter){

        String timeType =jianlcConfigCenter.getPrematchConfig().get("ableCapitalChangeTimeType");
        if(StringUtils.isEmpty(timeType)){
            log.error("{}:资金变动时间点校验:资金变动时间校验类型空异常",Thread.currentThread());
            return false;
        }

        if("hour".equals(timeType)){
            String curDateStr = DateUtils.formatDateTime(new Date());
            String  ablcapitalChangeHour = jianlcConfigCenter.getPrematchConfig().get("ableCapitalChangeHour");
            return commonHourCheck(ablcapitalChangeHour,curDateStr,"资金变动");
        } else if("minute".equals(timeType)){
            String minuteStr = DateUtils.getMinute();
            Integer minute = Integer.parseInt(minuteStr);
            String  ableCapitalChangeMinute = jianlcConfigCenter.getPrematchConfig().get("ableCapitalChangeMinute");
            return commonMinuteCheck(ableCapitalChangeMinute,minute,"资金变动分钟校验");
        }else {
            log.error("{}:资金变动时间点校验:未知资金变动时间校验类型",Thread.currentThread());
            return false;
        }
    }

    /**
     * @description: 待匹配资产监控
     * @author: RunFa.Zhou
     * @create: 2018/6/5
     **/
    public boolean ableMatchingAssetMonitoring(JianlcConfigCenter jianlcConfigCenter){
        String curDateStr = DateUtils.formatDateTime(new Date());
        String ableMatchHour = jianlcConfigCenter.getNoGlobalLockMatch().get("ableMatchingAssetMonitoringHour");
        return commonHourCheck(ableMatchHour, curDateStr,"待匹配资产监控");
    }


    public static void main(String[] args) {


        String timePrefix = DateUtils.formatDate(new Date(), null) + " ";
        String startStr = "08:00:00";
        String endStr = "22:00:00";
        Date startDate = DateUtils.parseDateTimeFmt(timePrefix + startStr);
        Date endDate = DateUtils.parseDate(timePrefix + endStr);

        String curDateStr = DateUtils.formatDateTime(new Date());
        Date curDate = DateUtils.parseDate(curDateStr);

        if (startDate.compareTo(curDate) <= 0 && endDate.compareTo(curDate) >= 0) {
            log.info("{}:{}:时间点校验:当前时间{}点,在匹配时间{}至{}中...",
                    Thread.currentThread(), curDateStr, startStr, curDateStr,"Test");

        }
    }
}
