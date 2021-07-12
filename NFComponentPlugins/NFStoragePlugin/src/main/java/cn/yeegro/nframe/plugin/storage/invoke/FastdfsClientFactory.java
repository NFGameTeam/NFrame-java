package cn.yeegro.nframe.plugin.storage.invoke;

import cn.yeegro.nframe.plugin.storage.client.TrackerClientFactory;
import cn.yeegro.nframe.plugin.storage.client.StorageClientFactory;
import cn.yeegro.nframe.plugin.storage.client.StorageClient;
import cn.yeegro.nframe.plugin.storage.client.TrackerClient;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * fastdfs客户端工厂类
 * 
 * @author lixia
 *
 */
public class FastdfsClientFactory {

    private static Logger logger = LoggerFactory.getLogger(FastdfsClientFactory.class);
	
    private static volatile FastdfsClient fastdfsClient;
    
    private static FastdfsClientConfig config = null;

    private final static String configFile = "FastdfsClient.properties";

    public FastdfsClientFactory() {
    }

    /**
     * 根据配置信息创建fastdfs客户端对象
     * @return
     */
    public static FastdfsClient getFastdfsClient(){
        if (fastdfsClient == null) {
            synchronized (FastdfsClient.class) {
                if (fastdfsClient == null) {
                    try {
                        config = new FastdfsClientConfig(configFile);
                    } catch (Exception e) {
                        logger.warn("Load fastdfs config failed.",e);
                    }
                    int connectTimeout = config.getConnectTimeout();
                    int networkTimeout = config.getNetworkTimeout();
                    TrackerClientFactory trackerClientFactory = new TrackerClientFactory(connectTimeout, networkTimeout);
                    StorageClientFactory storageClientFactory = new StorageClientFactory(connectTimeout, networkTimeout);
                    GenericKeyedObjectPoolConfig trackerClientPoolConfig = config.getTrackerClientPoolConfig();
                    GenericKeyedObjectPoolConfig storageClientPoolConfig = config.getStorageClientPoolConfig();
                    GenericKeyedObjectPool<String,TrackerClient> trackerClientPool = new GenericKeyedObjectPool<String, TrackerClient>(trackerClientFactory, trackerClientPoolConfig);
                    GenericKeyedObjectPool<String,StorageClient> storageClientPool = new GenericKeyedObjectPool<String, StorageClient>(storageClientFactory, storageClientPoolConfig);
                    List<String> trackerAddrs = config.getTrackerAddrs();
                    fastdfsClient = new FastdfsClientImpl(trackerAddrs,trackerClientPool,storageClientPool);
                }
            }
        }
        return fastdfsClient;
    }
}
