package hello.programmer.common.util.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author hanxu
 * @package com.jianlc.asset.conf
 * @description: 简理财
 * @copyright: Copyright (c) 2016
 * @company:北京金未来金融信息服务有限公司
 * @date 2016/6/1 20:18
 */
@Component(value = "zooKeeperConfig")
public class ZooKeeperConfigImpl implements ZooKeeperConfig {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperConfigImpl.class);

    @Value("#{zkConfig['zk.lock.host']}")
    private String zkLockHost;

    @Value("#{zkConfig['zk.lock.env']}")
    private String zkLockEnv;

    public String getZooKeeperHost() {
        return zkLockHost;
    }

    public String getZkLockEnv() {
        return zkLockEnv;
    }
}
