package cn.yeegro.nframe.plugin.storage.client;

import cn.yeegro.nframe.plugin.storage.invoke.FastdfsClientConfig;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;


/**
 * tracker客户端对象池工厂
 * @author lixia
 *
 */
public class TrackerClientFactory implements KeyedPooledObjectFactory<String,TrackerClient> {
	
  /**
   * 设置默认socket连接失效时间
   */
	private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
	private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;

	public TrackerClientFactory() {
		super();
	}
	
	/**
	 * socket连接失效时间
	 * @param connectTimeout
	 * @param networkTimeout
	 */
	public TrackerClientFactory(Integer connectTimeout, Integer networkTimeout) {
		super();
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
	}

	@Override
	public PooledObject<TrackerClient> makeObject(String key){
		TrackerClient trackerClient = new TrackerClientImpl(key,connectTimeout,networkTimeout);
		PooledObject<TrackerClient> pooledTrackerClient = new DefaultPooledObject<TrackerClient>(trackerClient);
		return pooledTrackerClient;
	}

	@Override
	public void destroyObject(String key, PooledObject<TrackerClient> pooledTrackerClient) throws IOException{
		TrackerClient trackerClient = pooledTrackerClient.getObject();
		trackerClient.close();
	}

	@Override
	public boolean validateObject(String key, PooledObject<TrackerClient> p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void activateObject(String key, PooledObject<TrackerClient> p)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivateObject(String key, PooledObject<TrackerClient> p)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	

}
