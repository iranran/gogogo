package hello.programmer.common.utils.lock;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author hanxu
 * @package com.jianlc.asset.util.lock
 * @description: 简理财
 * @copyright: Copyright (c) 2016
 * @company:北京金未来金融信息服务有限公司
 * @date 2016/5/31 17:47
 */
public class DistributedLockUtil {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockUtil.class);

    private static final int SESSION_TIMEOUT = 20000;
//    private String LOCK_PATH_PARENT = "/lock";
//    private  final String ASSET_LOCK_PATH_GLOBAL = LOCK_PATH_PARENT + "/asset_global";
//    private  final String ASSET_LOCK_PATH_USER = LOCK_PATH_PARENT + "/asset_user";
//    private  final String ASSET_LOCK_PATH_ASSET = LOCK_PATH_PARENT + "/asset_asset";
//    private  final String JIANLC_LOCK_PATH_USER = LOCK_PATH_PARENT + "/jianlc_user";
//    private  final String JIANLC_LOCK_INTEREST_ASSET = LOCK_PATH_PARENT + "/jianlc_interest_asset";
//    private  final String JIANLC_LOCK_INTEREST_USER = LOCK_PATH_PARENT + "/jianlc_interest_user";
//    private  final String ASSET_LOCK_PATH_MATCHINGASSET = LOCK_PATH_PARENT + "/asset_matchingAsset";
    private String LOCK_ROOT;
    private String LOCK_PATH_PARENT;
    private String ASSET_LOCK_PATH_GLOBAL;
    private String ASSET_LOCK_PATH_USER ;
    private String ASSET_LOCK_PATH_ASSET;
    private String JIANLC_LOCK_PATH_USER;
    private String JIANLC_LOCK_INTEREST_ASSET;
    private String JIANLC_LOCK_INTEREST_USER;
    private String ASSET_LOCK_PATH_MATCHINGASSET;
	
    private String[] LOCK_PATH_ARRAY;

    private static final String GLOBAL_LOCK_FLAG = "global_lock_flag";

    private static DistributedLockUtil instance = new DistributedLockUtil();
    private ZooKeeper zooKeeper;
    private CountDownLatch latch = new CountDownLatch(1);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DistributedLockUtil() {
        try {
            connectZookeeper();
            createLockNode();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void connectZookeeper() throws IOException, InterruptedException {
        if (zooKeeper != null) {
            return;
        }

        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

        if (wac == null) {
            logger.warn("wac is null, waiting for WebApplicationContext initialization completed!");
            long sleepMills = 3000;

            int i = 0;
            while (wac == null) {
                Thread.sleep(sleepMills);
                wac = ContextLoader.getCurrentWebApplicationContext();
                i++;
            }
            logger.info("sleep Mills for " + i + " * " + sleepMills);
        }

        ZooKeeperConfig zooKeeperConfig = (ZooKeeperConfig) wac.getBean("zooKeeperConfig");

        //使用环境配置作为lock的跟节点，区分每个环境的锁
        String lockEnv = zooKeeperConfig.getZkLockEnv();
        LOCK_ROOT = "/lock";
        LOCK_PATH_PARENT = StringUtils.isEmpty(lockEnv)? LOCK_ROOT : LOCK_ROOT + lockEnv;
        ASSET_LOCK_PATH_GLOBAL = LOCK_PATH_PARENT + "/asset_global";
        ASSET_LOCK_PATH_USER = LOCK_PATH_PARENT + "/asset_user";
        ASSET_LOCK_PATH_ASSET = LOCK_PATH_PARENT + "/asset_asset";
        JIANLC_LOCK_PATH_USER = LOCK_PATH_PARENT + "/jianlc_user";
        JIANLC_LOCK_INTEREST_ASSET = LOCK_PATH_PARENT + "/jianlc_interest_asset";
        JIANLC_LOCK_INTEREST_USER = LOCK_PATH_PARENT + "/jianlc_interest_user";
        ASSET_LOCK_PATH_MATCHINGASSET = LOCK_PATH_PARENT + "/asset_matchingAsset";
        LOCK_PATH_ARRAY = new String[] {ASSET_LOCK_PATH_GLOBAL, ASSET_LOCK_PATH_USER,
                ASSET_LOCK_PATH_ASSET, JIANLC_LOCK_PATH_USER, ASSET_LOCK_PATH_MATCHINGASSET, JIANLC_LOCK_INTEREST_ASSET, JIANLC_LOCK_INTEREST_USER};
        logger.info("1zk锁的跟节点是" + ASSET_LOCK_PATH_MATCHINGASSET);
        logger.info("2zk锁的跟节点是" + LOCK_PATH_ARRAY);

        zooKeeper = new ZooKeeper(zooKeeperConfig.getZooKeeperHost(), SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                logger.info("#### WatchedEvent is " + event.toString());
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            }
        });

        // latch.await();
    }

    private void validZooKeeper() {
        if (zooKeeper == null) {
            logger.warn("zooKeeper is null, re-init");
            synchronized (DistributedLockUtil.class) {
                new DistributedLockUtil();
            }
        }
    }

    private void createLockNode() throws KeeperException, InterruptedException {

        List<String> lockPathList = new ArrayList<String>();
        lockPathList.add(LOCK_ROOT);
        lockPathList.add(LOCK_PATH_PARENT);

        for (String lockPath : LOCK_PATH_ARRAY) {
            lockPathList.add(lockPath);
        }

        Stat stat = null;
        for (String nodePath : lockPathList) {
            try {
                stat = zooKeeper.exists(nodePath, false);
                if (stat == null) {
                    String nodeStr = zooKeeper.create(nodePath, nodePath.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT);
                    logger.info("create zookeeper node " + nodePath);
                }
            } catch (Exception e) {
                logger.error("create zookeeper node fail, nodePath is " + nodePath, e);
            }
        }
    }

    public static DistributedLockUtil getInstance() {
        return instance;
    }

    public LockResultEnum lock(LockTypeEnum lockTypeEnum, String lockId, String mqTaskId) {
        validZooKeeper();

        // 全局锁只能加一个，加锁 GLOBAL_LOCK_FLAG
        if (LockTypeEnum.LOCK_GLOBAL.equals(lockTypeEnum)) {
            logger.info("lock GLOBAL_LOCK_FLAG");
            LockResultEnum flagResultEnum = createZooKeeperNode(getLockPath(lockTypeEnum), GLOBAL_LOCK_FLAG, "");
            if (!flagResultEnum.equals(LockResultEnum.LOCK_SUCCESS)) {
                logger.warn("lock GLOBAL_LOCK_FLAG fail - " + flagResultEnum);
                return flagResultEnum;
            }
        }

        // 正常加锁
        LockResultEnum lockResultEnum = createZooKeeperNode(getLockPath(lockTypeEnum), lockId, mqTaskId);

        // 如果加全局锁失败，需要解锁 GLOBAL_LOCK_FLAG
        if (!lockResultEnum.equals(LockResultEnum.LOCK_SUCCESS) && LockTypeEnum.LOCK_GLOBAL.equals(lockTypeEnum)) {
            logger.warn("lock LOCK_GLOBAL fail, to unlock GLOBAL_LOCK_FLAG");
            LockResultEnum flagResultEnum = deleteZooKeeperNode(getLockPath(lockTypeEnum), GLOBAL_LOCK_FLAG);
            if (!flagResultEnum.equals(LockResultEnum.UNLOCK_SUCCESS)) {
                logger.error("unlock GLOBAL_LOCK_FLAG fail - " + flagResultEnum);
            }
        }
        return lockResultEnum;
    }

    public LockResultEnum unlock(LockTypeEnum lockTypeEnum, String lockId) {
        validZooKeeper();

        LockResultEnum unlockResultEnum = deleteZooKeeperNode(getLockPath(lockTypeEnum), lockId);

        // 全局锁特殊处理
        if (unlockResultEnum.equals(LockResultEnum.UNLOCK_SUCCESS) && LockTypeEnum.LOCK_GLOBAL.equals(lockTypeEnum)) {
            logger.info("unlock GLOBAL_LOCK_FLAG");
            LockResultEnum flagResultEnum = deleteZooKeeperNode(getLockPath(lockTypeEnum), GLOBAL_LOCK_FLAG);
            if (!flagResultEnum.equals(LockResultEnum.UNLOCK_SUCCESS)) {
                logger.error("unlock GLOBAL_LOCK_FLAG fail - " + flagResultEnum);
                return flagResultEnum;
            }
        }

        return unlockResultEnum;
    }

    public LockResultEnum checkLock(LockTypeEnum lockTypeEnum, String lockId) {
        validZooKeeper();

        if (LockTypeEnum.LOCK_GLOBAL.equals(lockTypeEnum)) {
            return checkZooKeeperChildren(getLockPath(lockTypeEnum));
        }

        return checkZooKeeperNode(getLockPath(lockTypeEnum), lockId);
    }

    public boolean hasUserLock() {
        validZooKeeper();
        LockResultEnum lockResultEnum = checkZooKeeperChildren(getLockPath(LockTypeEnum.LOCK_USER));
        return !LockResultEnum.LOCK_NOT_EXIST.equals(lockResultEnum);
    }

    public boolean hasAssetLock() {
        validZooKeeper();
        LockResultEnum lockResultEnum = checkZooKeeperChildren(getLockPath(LockTypeEnum.LOCK_ASSET));
        return !LockResultEnum.LOCK_NOT_EXIST.equals(lockResultEnum);
    }
	
    public boolean hasMatchingAssetLock() {
        validZooKeeper();
        LockResultEnum lockResultEnum = checkZooKeeperChildren(getLockPath(LockTypeEnum.LOCK_MATCHINGASSET));
        return !LockResultEnum.LOCK_NOT_EXIST.equals(lockResultEnum);
    }

    public List<String> getChildrenLockNodeName(String lockParentPath) {
        validZooKeeper();
        Assert.notNull(lockParentPath, "锁路径不能为空");

        try {
            return zooKeeper.getChildren(lockParentPath, false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return new ArrayList<String>();
    }

    public byte[] getLockNodeValue(String lockNodePath) {
        validZooKeeper();
        Assert.notNull(lockNodePath, "锁节点路径不能为空");

        try {
            return zooKeeper.getData(lockNodePath, null, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    private String getLockPath(LockTypeEnum lockTypeEnum) {
        String lockPath = null;

        if (LockTypeEnum.LOCK_USER.equals(lockTypeEnum)) {
            lockPath = ASSET_LOCK_PATH_USER;
        } else if (LockTypeEnum.LOCK_ASSET.equals(lockTypeEnum)) {
            lockPath = ASSET_LOCK_PATH_ASSET;
        } else if (LockTypeEnum.LOCK_GLOBAL.equals(lockTypeEnum)) {
            lockPath = ASSET_LOCK_PATH_GLOBAL;
        } else if (LockTypeEnum.LOCK_USER_ACCOUNT.equals(lockTypeEnum)) {
            lockPath = JIANLC_LOCK_PATH_USER;
        } else if (LockTypeEnum.LOCK_MATCHINGASSET.equals(lockTypeEnum)) {
            lockPath = ASSET_LOCK_PATH_MATCHINGASSET;
        } else if (LockTypeEnum.LOCK_INTEREST_ASSET.equals(lockTypeEnum)) {
            lockPath = JIANLC_LOCK_INTEREST_ASSET;
        } else if (LockTypeEnum.LOCK_INTEREST_USER.equals(lockTypeEnum)) {
            lockPath = JIANLC_LOCK_INTEREST_USER;
        }

        if (lockPath == null) {
            logger.error("lockPath is null, LockTypeEnum is " + lockTypeEnum.name());
        }
        return lockPath;
    }

    private LockResultEnum createZooKeeperNode(String parentPath, String lockId, String mqTaskId) {
        Assert.notNull(parentPath, "锁路径不能为空");
        Assert.notNull(lockId, "用户id或资产id不能为空");

        String lockPath = new StringBuilder(parentPath).append("/").append(lockId).toString();
        try {
            Stat stat = zooKeeper.exists(lockPath, false);
            if (stat != null) {
                return LockResultEnum.LOCK_FAIL;
            }

            String valueStr = generateLockInfo(lockId, mqTaskId);
            String nodeStr =
                    zooKeeper.create(lockPath, valueStr.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("create zookeeper node " + lockPath);
            return LockResultEnum.LOCK_SUCCESS;

        } catch (Exception e) {
            if (e instanceof KeeperException.NodeExistsException) {
                // KeeperException keeperException = (KeeperException) e;
                // if (keeperException.code().equals(Code.NODEEXISTS)) {
                logger.warn(e.getMessage());
                // }
            } else {
                logger.error(e.getMessage(), e);
            }
            // logger.error(e.getMessage(), e);
            return LockResultEnum.EXCEPTION;
        }
    }

    private String generateLockInfo(String lockId, String businessId) {
        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockId(lockId);
        lockInfo.setBusinessId(businessId);

        Date date = new Date();
        lockInfo.setLockTimeStr(dateFormat.format(date));
        lockInfo.setLockTimeMills(date.getTime());

        lockInfo.setIp(getLocalHostIP());
        lockInfo.setThreadId(Thread.currentThread().getId());

        return JSON.toJSONString(lockInfo);
    }

    private String getLocalHostIP() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return ip;
    }

    private LockResultEnum deleteZooKeeperNode(String parentPath, String lockId) {
        Assert.notNull(parentPath, "锁路径不能为空");
        Assert.notNull(lockId, "用户id或资产id不能为空");

        String lockPath = new StringBuilder(parentPath).append("/").append(lockId).toString();
        try {
            Stat stat = zooKeeper.exists(lockPath, false);
            if (stat == null) {
                return LockResultEnum.UNLOCK_FAIL;
            }

            zooKeeper.delete(lockPath, -1);
            logger.info("delete zookeeper node " + lockPath);
            return LockResultEnum.UNLOCK_SUCCESS;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return LockResultEnum.EXCEPTION;
        }
    }

    private LockResultEnum checkZooKeeperNode(String parentPath, String lockId) {
        Assert.notNull(parentPath, "锁路径不能为空");
        Assert.notNull(lockId, "用户id或资产id不能为空");

        String lockPath = new StringBuilder(parentPath).append("/").append(lockId).toString();
        try {
            Stat stat = zooKeeper.exists(lockPath, false);
            if (stat == null) {
                return LockResultEnum.LOCK_NOT_EXIST;
            }

            return LockResultEnum.LOCK_EXIST;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return LockResultEnum.EXCEPTION;
        }
    }

    private LockResultEnum checkZooKeeperChildren(String parentPath) {
        Assert.notNull(parentPath, "锁路径不能为空");

        try {
            List<String> childrenList = zooKeeper.getChildren(parentPath, false);
            if (CollectionUtils.isEmpty(childrenList)) {
                return LockResultEnum.LOCK_NOT_EXIST;
            }

            return LockResultEnum.LOCK_EXIST;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return LockResultEnum.EXCEPTION;
        }
    }

    public String[] getLockPathArray() {
        return LOCK_PATH_ARRAY.clone();
    }

    public String getLockPathGlobal() {
        return ASSET_LOCK_PATH_GLOBAL;
    }

    public enum LockTypeEnum {
        LOCK_GLOBAL(0, "资产模块全局锁"), 
        LOCK_USER(1, "资产模块用户锁"), 
        LOCK_ASSET(2, "资产模块资产锁"), 
        LOCK_USER_ACCOUNT(3,"用户模块用户账户锁（操作简理财账户专用）"), 
        LOCK_MATCHINGASSET(4, "资产模块待匹配资产锁"),
        LOCK_INTEREST_ASSET(5,"计息模版资产锁"),
        LOCK_INTEREST_USER(6,"计息模版用户锁");

        /**
         * 枚举值
         */
        private int value;

        /**
         * 枚举描述
         */
        private String desc;

        LockTypeEnum(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    public enum LockResultEnum {
        LOCK_SUCCESS(0, "加锁成功"), LOCK_FAIL(1, "加锁失败"), UNLOCK_SUCCESS(2, "解锁成功"), UNLOCK_FAIL(3, "解锁失败"), LOCK_EXIST(4,
                "锁存在"), LOCK_NOT_EXIST(5, "锁不存在"), EXCEPTION(99, "操作异常");

        /**
         * 枚举值
         */
        private int value;

        /**
         * 枚举描述
         */
        private String desc;

        LockResultEnum(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

}


class LockInfo {
    private String lockId;
    private String businessId;
    private long lockTimeMills;
    private String lockTimeStr;
    private String ip;
    private long threadId;

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public long getLockTimeMills() {
        return lockTimeMills;
    }

    public void setLockTimeMills(long lockTimeMills) {
        this.lockTimeMills = lockTimeMills;
    }

    public String getLockTimeStr() {
        return lockTimeStr;
    }

    public void setLockTimeStr(String lockTimeStr) {
        this.lockTimeStr = lockTimeStr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }
}
