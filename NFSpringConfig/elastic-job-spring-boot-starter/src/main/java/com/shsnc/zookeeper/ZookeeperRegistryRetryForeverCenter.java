package com.shsnc.zookeeper;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.ExecutorServiceHandlerRegistry;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.exception.RegExceptionHandler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.shsnc.annotation.ElasticJobConf;
import com.shsnc.schedul.service.LiteJobService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 基于Zookeeper的注册中心.
 *  根据 ZookeeperRegistryCenter 修改
 *      只修改连接zk的策略
 */
@Slf4j
public final class ZookeeperRegistryRetryForeverCenter implements CoordinatorRegistryCenter {

    @Getter(AccessLevel.PROTECTED)
    private ZookeeperConfiguration zkConfig;

    private final Map<String, TreeCache> caches = new HashMap<>();

    @Getter
    private CuratorFramework client;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Lazy
    private LiteJobService liteJobService;

    // TODO: 2020/9/10 定义三种类型作业
    private final List<String> jobTypeNameList = Arrays.asList("SimpleJob", "DataflowJob", "ScriptJob");


    public static final String PREFIX = "PARSER-";


    public ZookeeperRegistryRetryForeverCenter(final ZookeeperConfiguration zkConfig) {
        this.zkConfig = zkConfig;
    }

    @Override
    public void init() {
        log.info("Elastic job: zookeeper registry center init, server lists is: {}.", zkConfig.getServerLists());
        // retryIntervalMs:每次重试间隔的时间
        RetryForever retryForever = new RetryForever(zkConfig.getBaseSleepTimeMilliseconds());

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConfig.getServerLists())
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMilliseconds(), zkConfig.getMaxRetries(), zkConfig.getMaxSleepTimeMilliseconds()))
//                .retryPolicy( retryForever )
                .namespace(zkConfig.getNamespace());

        if (0 != zkConfig.getSessionTimeoutMilliseconds()) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeoutMilliseconds());
        }
        if (0 != zkConfig.getConnectionTimeoutMilliseconds()) {
            builder.connectionTimeoutMs(zkConfig.getConnectionTimeoutMilliseconds());
        }
        if (!Strings.isNullOrEmpty(zkConfig.getDigest())) {
            builder.authorization("digest", zkConfig.getDigest().getBytes(Charsets.UTF_8))
                    .aclProvider(new ACLProvider() {
                        @Override
                        public List<ACL> getDefaultAcl() {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }
                        @Override
                        public List<ACL> getAclForPath(final String path) {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }
                    });
        }
        client = builder.build();

        // TODO: 2020/9/10 注册一个 Listener 事件
        final ConnectionStateListener listener = new ConnectionStateListener()  {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if ( ConnectionState.CONNECTED == newState || ConnectionState.RECONNECTED == newState ) {
                    log.info("monitor start : {}",System.currentTimeMillis());
                    try {
                        List<String> strings = client.getChildren().forPath("/");
                        strings.forEach(s->{
                            ExecutorServiceHandlerRegistry.remove(s);
                        });
                    } catch (Exception e) {
                    }
                    if (liteJobService!=null)  liteJobService.monitorJobRegister();
                    initialAnnotation();
                }



            }
        };
        client.getConnectionStateListenable().addListener(listener);

        // TODO: 2020/9/10 表示一直重连
        client.start();

        // TODO: 2020/9/10 如果超时也不管
//        try {
//            if (!client.blockUntilConnected(zkConfig.getMaxSleepTimeMilliseconds() * zkConfig.getMaxRetries(), TimeUnit.MILLISECONDS)) {
//                client.close();
//                throw new KeeperException.OperationTimeoutException();
//            }
//            //CHECKSTYLE:OFF
//        } catch (final Exception ex) {
//            //CHECKSTYLE:ON
//            RegExceptionHandler.handleException(ex);
//        }


    }


    // TODO: 2020/9/10  初始化 ElasticJobConf 的作业实例
    public void initialAnnotation(){
        if (Objects.nonNull(applicationContext)){
            // TODO: 2020/9/1 自动注册 带有 ElasticJobConf 作业 并实例化
            Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(ElasticJobConf.class);
            for (Object confBean : beanMap.values()) {
                Class<?> clz = confBean.getClass();
                // TODO: 2020/9/1 解决CGLIB代理问题
                String jobTypeName = clz.getInterfaces()[0].getSimpleName();
                if (!jobTypeNameList.contains(jobTypeName)) {
                    jobTypeName = clz.getSuperclass().getInterfaces()[0].getSimpleName();
                    clz = clz.getSuperclass();
                }
                ElasticJobConf conf = AnnotationUtils.findAnnotation(clz, ElasticJobConf.class);

                String jobClass = clz.getName();
                String jobName = conf.jobName();
                String cron = conf.cron();
                String shardingItemParameters =  conf.shardingItemParameters();
                String description = conf.description();
                String jobParameter = conf.jobParameter();
                String jobExceptionHandler =  conf.jobExceptionHandler();
                String executorServiceHandler =  conf.executorServiceHandler();

                String jobShardingStrategyClass =  conf.jobShardingStrategyClass();
                String eventTraceRdbDataSource =  conf.eventTraceRdbDataSource();
                String scriptCommandLine =  conf.scriptCommandLine();

                boolean failover =  conf.failover();
                boolean misfire =  conf.misfire();
                boolean overwrite =  conf.overwrite();
                boolean disabled = conf.disabled();
                boolean monitorExecution = conf.monitorExecution();
                boolean streamingProcess =conf.streamingProcess();

                int shardingTotalCount =  conf.shardingTotalCount();
                int monitorPort =  conf.monitorPort();
                int maxTimeDiffSeconds =  conf.maxTimeDiffSeconds();
                int reconcileIntervalMinutes =  conf.reconcileIntervalMinutes();

                // TODO: 2020/9/1  作业核心配置
                JobCoreConfiguration coreConfig =
                        JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount)
                                .shardingItemParameters(shardingItemParameters)
                                .description(description)
                                .failover(failover)
                                .jobParameter(jobParameter)
                                .misfire(misfire)
                                .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
                                .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler)
                                .build();

                // TODO: 2020/9/1 不同类型的任务配置处理
                LiteJobConfiguration jobConfig = null;
                JobTypeConfiguration typeConfig = null;

                // TODO: 2020/9/1 根据作业类型创建对应作业 默认 SimpleJob
                switch (jobTypeName) {
                    case "SimpleJob" :
                        typeConfig = new SimpleJobConfiguration(coreConfig,jobClass);
                        break;
                    case "DataflowJob" :
                        typeConfig = new DataflowJobConfiguration(coreConfig,jobClass, streamingProcess);
                        break;
                    case "ScriptJob" :
                        typeConfig = new ScriptJobConfiguration(coreConfig, scriptCommandLine);
                        break;
                    default:
                        typeConfig = new SimpleJobConfiguration(coreConfig,jobClass);
                        break;
                }

                jobConfig = LiteJobConfiguration.newBuilder(typeConfig)
                        .overwrite(overwrite)
                        .disabled(disabled)
                        .monitorPort(monitorPort)
                        .monitorExecution(monitorExecution)
                        .maxTimeDiffSeconds(maxTimeDiffSeconds)
                        .jobShardingStrategyClass(jobShardingStrategyClass)
                        .reconcileIntervalMinutes(reconcileIntervalMinutes)
                        .build();

                List<BeanDefinition> elasticJobListeners = getTargetElasticJobListeners(conf);

                // 构建SpringJobScheduler对象来初始化任务
                BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
                factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
                if ("ScriptJob".equals(jobTypeName)) {
                    factory.addConstructorArgValue(null);
                } else {
                    factory.addConstructorArgValue(confBean);
                }
                factory.addConstructorArgValue(this);
                factory.addConstructorArgValue(jobConfig);

                // 任务执行日志数据源，以名称获取
                if (StringUtils.hasText(eventTraceRdbDataSource)) {
                    BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                    rdbFactory.addConstructorArgReference(eventTraceRdbDataSource);
                    factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
                }

                factory.addConstructorArgValue(elasticJobListeners);
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
                defaultListableBeanFactory.registerBeanDefinition( PREFIX + jobName , factory.getBeanDefinition());
                SpringJobScheduler springJobScheduler = (SpringJobScheduler) applicationContext.getBean(PREFIX + jobName );
                springJobScheduler.init();
                log.info(" 【" + jobName + "】  " + jobClass + " INIT SUCCESSED   ");
            }
        }
    }

    // TODO: 2020/9/1 封装ElasticJob前后监听器
    private List<BeanDefinition> getTargetElasticJobListeners(ElasticJobConf conf) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        String listeners = conf.listener();
        if (StringUtils.hasText(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners =  conf.distributedListener();
        long startedTimeoutMilliseconds =  conf.startedTimeoutMilliseconds();
        long completedTimeoutMilliseconds = conf.completedTimeoutMilliseconds();

        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            factory.addConstructorArgValue(startedTimeoutMilliseconds);
            factory.addConstructorArgValue(completedTimeoutMilliseconds);
            result.add(factory.getBeanDefinition());
        }
        return result;
    }

    @Override
    public void close() {
        for (Map.Entry<String, TreeCache> each : caches.entrySet()) {
            each.getValue().close();
        }
        waitForCacheClose();
        CloseableUtils.closeQuietly(client);
    }

    /* TODO 等待500ms, cache先关闭再关闭client, 否则会抛异常
     * 因为异步处理, 可能会导致client先关闭而cache还未关闭结束.
     * 等待Curator新版本解决这个bug.
     * BUG地址：https://issues.apache.org/jira/browse/CURATOR-157
     */
    private void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String get(final String key) {
        TreeCache cache = findTreeCache(key);
        if (null == cache) {
            return getDirectly(key);
        }
        ChildData resultInCache = cache.getCurrentData(key);
        if (null != resultInCache) {
            return null == resultInCache.getData() ? null : new String(resultInCache.getData(), Charsets.UTF_8);
        }
        return getDirectly(key);
    }

    private TreeCache findTreeCache(final String key) {
        for (Map.Entry<String, TreeCache> entry : caches.entrySet()) {
            if (key.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public String getDirectly(final String key) {
        try {
            return new String(client.getData().forPath(key), Charsets.UTF_8);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
            return null;
        }
    }

    @Override
    public List<String> getChildrenKeys(final String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            Collections.sort(result, new Comparator<String>() {

                @Override
                public int compare(final String o1, final String o2) {
                    return o2.compareTo(o1);
                }
            });
            return result;
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
            return Collections.emptyList();
        }
    }

    @Override
    public int getNumChildren(final String key) {
        try {
            Stat stat = client.checkExists().forPath(key);
            if (null != stat) {
                return stat.getNumChildren();
            }
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
        return 0;
    }

    @Override
    public boolean isExisted(final String key) {
        try {
            return null != client.checkExists().forPath(key);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
            return false;
        }
    }

    @Override
    public void persist(final String key, final String value) {
        try {
            if (!isExisted(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes(Charsets.UTF_8));
            } else {
                update(key, value);
            }
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void update(final String key, final String value) {
        try {
            client.inTransaction().check().forPath(key).and().setData().forPath(key, value.getBytes(Charsets.UTF_8)).and().commit();
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void persistEphemeral(final String key, final String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charsets.UTF_8));
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public String persistSequential(final String key, final String value) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(key, value.getBytes(Charsets.UTF_8));
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
        return null;
    }

    @Override
    public void persistEphemeralSequential(final String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void remove(final String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public long getRegistryCenterTime(final String key) {
        long result = 0L;
        try {
            persist(key, "");
            result = client.checkExists().forPath(key).getMtime();
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
        Preconditions.checkState(0L != result, "Cannot get registry center time.");
        return result;
    }

    @Override
    public Object getRawClient() {
        return client;
    }

    @Override
    public void addCacheData(final String cachePath) {
        TreeCache cache = new TreeCache(client, cachePath);
        try {
            cache.start();
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
        caches.put(cachePath + "/", cache);
    }

    @Override
    public void evictCacheData(final String cachePath) {
        TreeCache cache = caches.remove(cachePath + "/");
        if (null != cache) {
            cache.close();
        }
    }

    @Override
    public Object getRawCache(final String cachePath) {
        return caches.get(cachePath + "/");
    }

}
