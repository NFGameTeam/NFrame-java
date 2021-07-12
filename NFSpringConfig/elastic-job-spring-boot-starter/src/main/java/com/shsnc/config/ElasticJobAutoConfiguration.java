package com.shsnc.config;


import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.shsnc.schedul.service.LiteJobService;
import com.shsnc.zookeeper.ZookeeperRegistryRetryForeverCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;

@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
@Slf4j
public class ElasticJobAutoConfiguration {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    /**
     * 初始化Zookeeper注册中心
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryRetryForeverCenter zookeeperRegistryCenter() {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(zookeeperProperties.getServerLists(),
                zookeeperProperties.getNamespace());
        zkConfig.setBaseSleepTimeMilliseconds(zookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfig.setConnectionTimeoutMilliseconds(zookeeperProperties.getConnectionTimeoutMilliseconds());
        zkConfig.setDigest(zookeeperProperties.getDigest());
        zkConfig.setMaxRetries(zookeeperProperties.getMaxRetries());
        zkConfig.setMaxSleepTimeMilliseconds(zookeeperProperties.getMaxSleepTimeMilliseconds());
        zkConfig.setSessionTimeoutMilliseconds(zookeeperProperties.getSessionTimeoutMilliseconds());
        return new ZookeeperRegistryRetryForeverCenter(zkConfig);
    }

    @Bean
    @Lazy
    public LiteJobService liteJobService(ZookeeperRegistryRetryForeverCenter registryCenter){
        return new LiteJobService(registryCenter);
    }
}
