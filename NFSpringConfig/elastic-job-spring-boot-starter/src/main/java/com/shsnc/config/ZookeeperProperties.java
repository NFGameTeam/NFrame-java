package com.shsnc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "elastic.job.zk")
public class ZookeeperProperties {

    /**
     * 连接Zookeeper服务器的列表. 包括IP地址和端口号. 多个地址用逗号分隔. 如: host1:2181,host2:2181
     */
    public static String serverLists;

    /**
     * 命名空间.
     */
    public static String namespace;

    /**
     * 等待重试的间隔时间的初始值. 单位毫秒.
     */
    public static int baseSleepTimeMilliseconds = 1000;

    /**
     * 等待重试的间隔时间的最大值. 单位毫秒.
     */
    @Deprecated
    public static int maxSleepTimeMilliseconds = 3000;

    /**
     * 最大重试次数.
     */
    @Deprecated
    public static int maxRetries = 3;

    /**
     * 会话超时时间. 单位毫秒.
     */
    public static int sessionTimeoutMilliseconds = 3000;

    /**
     * 连接超时时间. 单位毫秒.
     */
    public static int connectionTimeoutMilliseconds = 3000;

    /**
     * 连接Zookeeper的权限令牌. 缺省为不需要权限验证.
     */
    public static String digest = "ivory:ivory";

    public String getServerLists() {
        return serverLists;
    }

    @Value("${elastic.job.zk.server-lists}")
    public void setServerLists(String serverLists) {
        this.serverLists = serverLists;
    }

    public String getNamespace() {
        return namespace;
    }

    @Value("${elastic.job.zk.namespace}")
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getBaseSleepTimeMilliseconds() {
        return baseSleepTimeMilliseconds;
    }

    @Value("${elastic.job.zk.base-sleep-time-milliseconds:1000}")
    public void setBaseSleepTimeMilliseconds(int baseSleepTimeMilliseconds) {
        this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
    }

    public int getMaxSleepTimeMilliseconds() {
        return maxSleepTimeMilliseconds;
    }

    @Value("${elastic.job.zk.max-sleep-time-milliseconds:3000}")
    public void setMaxSleepTimeMilliseconds(int maxSleepTimeMilliseconds) {
        this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    @Value("${elastic.job.zk.max-retries:3}")
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getSessionTimeoutMilliseconds() {
        return sessionTimeoutMilliseconds;
    }

    @Value("${elastic.job.zk.session-timeout-milliseconds:3000}")
    public void setSessionTimeoutMilliseconds(int sessionTimeoutMilliseconds) {
        this.sessionTimeoutMilliseconds = sessionTimeoutMilliseconds;
    }

    public int getConnectionTimeoutMilliseconds() {
        return connectionTimeoutMilliseconds;
    }

    @Value("${elastic.job.zk.connection-timeout-milliseconds:3000}")
    public void setConnectionTimeoutMilliseconds(int connectionTimeoutMilliseconds) {
        this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
    }

    public String getDigest() {
        return digest;
    }

    @Value("${elastic.job.zk.digest:#{'ivory:ivory'}}")
    public void setDigest(String digest) {
        this.digest = digest;
    }
}
