package cn.yeegro.nframe.plugin.storage.invoke;

import cn.yeegro.nframe.common.utils.PropertiesLoader;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * FastdfsClient配置类
 * 获取网络连接超时时间
 * 和跟踪器（tracker）IP，tracker和storage的池对象
 * @author lixia
 *
 */
public class FastdfsClientConfig {
	//秒
	public static final int DEFAULT_CONNECT_TIMEOUT = 5; // second
	public static final int DEFAULT_NETWORK_TIMEOUT = 30; // second
	
	private int connectTimeout = DEFAULT_CONNECT_TIMEOUT * 1000;
	private int networkTimeout = DEFAULT_NETWORK_TIMEOUT * 1000;
	//tracker的IP地址和端口信息
	private List<String> trackerAddrs = new ArrayList<String>();
	
	/**
	 * 默认读取properties文件中的配置信息
	 * 通过写入system.properties文件的方式
	 * （仅能配置一个tracker，在PropertiesLoader中重复属性将被覆盖）
	 */
	public FastdfsClientConfig() {
		super();
		this.connectTimeout = PropertiesLoader.getInteger("connect_timeout",5)*1000;
    this.networkTimeout = PropertiesLoader.getInteger("network_timeout",30)*1000;
    trackerAddrs.add((String)PropertiesLoader.getProperty("tracker_server","10.162.62.230:22122"));
	}
	/**
	 * 根据配置文件读取配置信息
	 * （可以配置多个tracker）
	 * @param configFile
	 * @throws ConfigurationException
	 */
	public FastdfsClientConfig(String configFile) throws ConfigurationException {
		super();
		//String conf = FastdfsClientConfig.class.getClassLoader().getResource(configFile).getPath();
		Configuration config = new PropertiesConfiguration(configFile);
		this.connectTimeout = config.getInt("connect_timeout", DEFAULT_CONNECT_TIMEOUT)*1000;
		this.networkTimeout = config.getInt("network_timeout", DEFAULT_NETWORK_TIMEOUT)*1000;
		List<Object> trackerServers = config.getList("tracker_server");
		for(Object trackerServer:trackerServers){
			trackerAddrs.add((String)trackerServer);
		}
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getNetworkTimeout() {
		return networkTimeout;
	}

	public void setNetworkTimeout(int networkTimeout) {
		this.networkTimeout = networkTimeout;
	}

	public List<String> getTrackerAddrs() {
		return trackerAddrs;
	}

	public void setTrackerAddrs(List<String> trackerAddrs) {
		this.trackerAddrs = trackerAddrs;
	}

	public GenericKeyedObjectPoolConfig getTrackerClientPoolConfig(){
		GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
//		poolConfig.setMaxIdlePerKey(maxIdlePerKey);
//		poolConfig.setMaxTotal(maxTotal);
//		poolConfig.setMaxTotalPerKey(maxTotalPerKey);
//		poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//		poolConfig.setma
		
		return poolConfig;
	}
	
	public GenericKeyedObjectPoolConfig getStorageClientPoolConfig(){
		GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
		return poolConfig;
	}
}
