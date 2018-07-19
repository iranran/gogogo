package hello.programmer.common.util.lock;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanxu
 * @package com.jianlc.asset.util.lock
 * @description: 简理财
 * @copyright: Copyright (c) 2016
 * @company:北京金未来金融信息服务有限公司
 * @date 2016/6/20 16:14
 */
public class LockMonitor {

    private static final Logger logger = LoggerFactory.getLogger(LockMonitor.class);

    private static final int DURATION_THRESHOLD_LOCK_COMMON = 300000;
    private static final int DURATION_THRESHOLD_LOCK_GLOBAL = 3600000;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void checkLockDuration() throws Exception {
        logger.info("run LockMonitor to check lock duration ...");

        DistributedLockUtil distributedLockUtil = DistributedLockUtil.getInstance();

        // 获得各个锁的父路径
        String globalLockPath = distributedLockUtil.getLockPathGlobal();
        String[] lockPathArray = distributedLockUtil.getLockPathArray();

        List<String> lockPathList = new ArrayList<String>();
        for(String lockPath : lockPathArray){
            if(lockPath != null && !lockPath.equals(globalLockPath)) {
                lockPathList.add(lockPath);
            }
        }

        // 获取具体的锁信息并判断是否超时
        checkOnePathDurantion(distributedLockUtil, globalLockPath, DURATION_THRESHOLD_LOCK_GLOBAL);

        for(String lockPath : lockPathList){
            checkOnePathDurantion(distributedLockUtil, lockPath, DURATION_THRESHOLD_LOCK_COMMON);
        }
    }

    private void checkOnePathDurantion(DistributedLockUtil distributedLockUtil, String lockPath, long durationThreshold){
        List<String> lockNodeNameList = distributedLockUtil.getChildrenLockNodeName(lockPath);
        for(String lockName : lockNodeNameList){
            String lockNodePath = lockPath + "/" + lockName;
            byte[] value = distributedLockUtil.getLockNodeValue(lockNodePath);
            if(value == null){
                continue;
            }

            LockInfo lockInfo = JSON.parseObject(new String(value), LockInfo.class);
            if(lockInfo != null){
                long duration = System.currentTimeMillis() - lockInfo.getLockTimeMills();
                if(duration >= DURATION_THRESHOLD_LOCK_GLOBAL){
                    logger.error("lock node is danger, " + lockNodePath + ", duration is " + duration);
                }
                else{
                    logger.info("lock node is normal, " + lockNodePath + ", duration is " + duration);
                }
            }
        }
    }

}
