package hello.programmer.common.utils.lock;

/**
 * @author hanxu
 * @package com.jianlc.asset.conf
 * @description: 简理财
 * @copyright: Copyright (c) 2016
 * @company:北京金未来金融信息服务有限公司
 * @date 2016/6/1 20:18
 */
public interface ZooKeeperConfig {

    public String getZooKeeperHost();

    /**
     * 获取zk锁环境配置
     * @return
     */
    public String getZkLockEnv();
}
