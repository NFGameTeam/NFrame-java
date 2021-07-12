package com.shsnc.strategy;

import com.dangdang.ddframe.job.lite.api.strategy.JobInstance;
import com.dangdang.ddframe.job.lite.api.strategy.JobShardingStrategy;
import com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.shsnc.config.ZookeeperProperties;
import com.shsnc.zookeeper.ZookeeperRegistryRetryForeverCenter;

import java.util.*;

/**
 *      根据任务列表下标 负载分配节点
 */
public class RotateServerByJobIndexShardingStrategy implements JobShardingStrategy {

    /**
     *  Zookeeper重连客户端
     *
     */
    ZookeeperRegistryRetryForeverCenter registryCenter;

    public RotateServerByJobIndexShardingStrategy() {
        // TODO: 2020/9/3  通过Spring静态变量方式注入 registryCenter
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration( ZookeeperProperties.serverLists,
                ZookeeperProperties.namespace);
        zkConfig.setBaseSleepTimeMilliseconds(ZookeeperProperties.baseSleepTimeMilliseconds);
        zkConfig.setConnectionTimeoutMilliseconds(ZookeeperProperties.connectionTimeoutMilliseconds);
        zkConfig.setDigest(ZookeeperProperties.digest);
        // TODO: 2020/9/10 最大次数与最大等待重试的间隔时间已经失效
        zkConfig.setMaxRetries(ZookeeperProperties.maxRetries);
        zkConfig.setMaxSleepTimeMilliseconds(ZookeeperProperties.maxSleepTimeMilliseconds);
        zkConfig.setSessionTimeoutMilliseconds(ZookeeperProperties.sessionTimeoutMilliseconds);
        this.registryCenter = new ZookeeperRegistryRetryForeverCenter(zkConfig);
        registryCenter.init();
    }

    private AverageAllocationJobShardingStrategy averageAllocationJobShardingStrategy = new AverageAllocationJobShardingStrategy();

    /**
     * 作业分片.
     *
     * @param jobInstances       所有参与分片的单元列表
     * @param jobName            作业名称
     * @param shardingTotalCount 分片总数
     * @return 分片结果
     */
    @Override
    public Map<JobInstance, List<Integer>> sharding(List<JobInstance> jobInstances, String jobName, int shardingTotalCount) {
        return averageAllocationJobShardingStrategy.sharding(rotateServerList(jobInstances, jobName), jobName, shardingTotalCount);
    }

    private List<JobInstance> rotateServerList(final List<JobInstance> shardingUnits, final String jobName) {
        // TODO: 2020/9/7 获取 namespace 下全部的作业目录
        List<String> keys = registryCenter.getChildrenKeys("/");

        // TODO: 2020/9/7 按照顺序排列
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return o1.compareTo(o2);
            }
        });

        // TODO: 2020/9/7 获取作业下标
        int inx = keys.indexOf(jobName);
        int shardingUnitsSize = shardingUnits.size();
        int offset = Math.abs(inx) % shardingUnitsSize;
        // TODO: 2020/9/7 取模如果为0 直接返回list
        if (0 == offset) {
            return shardingUnits;
        }
        // TODO: 2020/9/7 否则按照 offset 取模放入数组中
        List<JobInstance> result = new ArrayList<>(shardingUnitsSize);
        for (int i = 0; i < shardingUnitsSize; i++) {
            int index = (i + offset) % shardingUnitsSize;
            result.add(shardingUnits.get(index));
        }
        return result;
    }


}
