package cn.yeegro.nframe.plugin.storage.client;


import cn.yeegro.nframe.common.web.StorageResult;
import cn.yeegro.nframe.components.storage.command.*;
import cn.yeegro.nframe.components.storage.data.GroupInfo;
import cn.yeegro.nframe.components.storage.data.StorageInfo;
import cn.yeegro.nframe.components.storage.data.UploadStorage;
import cn.yeegro.nframe.plugin.storage.invoke.FastdfsClientConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

/**
 * 追踪器客户端实现
 * 
 * @author lixia
 *
 */
public class TrackerClientImpl implements TrackerClient{
	
	private Socket socket;
	private String host;
	private Integer port;
	private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
	private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;
	
	public TrackerClientImpl(String address){
		super();
		String[] hostport = address.split(":");
		this.host = hostport[0];
		this.port = Integer.valueOf(hostport[1]);
	}
	
	public TrackerClientImpl(String address,Integer connectTimeout, Integer networkTimeout){
		this(address);
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
	}
	
	private Socket getSocket() throws IOException{
		if(socket==null){
			socket = new Socket();
			socket.setSoTimeout(networkTimeout);
			socket.connect(new InetSocketAddress(host, port),connectTimeout);
		}
		return socket;
	}
	

	public void close() throws IOException{
		Socket socket = getSocket();
		Command<Boolean> command = new CloseCmd();
		command.exec(socket);
		socket.close();
		socket = null;
	}

	/**
	 * 获取上传的storage信息，包含ip+port ,pathIndex
	 * @return 返回结果封装UploadStorage
	 */
	public StorageResult<UploadStorage> getUploadStorage() throws IOException{
		Socket socket = getSocket();
		Command<UploadStorage> command = new QueryUploadCmd();
		return command.exec(socket);
	}
	
	/**
	 * 获取修改文件信息的storage地址信息
	 * @return ip+port
	 */
	public StorageResult<String> getUpdateStorageAddr(String group,String fileName) throws IOException{
		Socket socket = getSocket();
		Command<String> cmd = new QueryUpdateCmd(group,fileName);
		return cmd.exec(socket);
	}
	
	/**
	 * 获取下载地址信息
	 * @param  group 组信息
	 * @param  fileName fileId
	 *
	 */
	public StorageResult<String> getDownloadStorageAddr(String group,String fileName) throws IOException{
		Socket socket = getSocket();
		Command<String> cmd = new QueryDownloadCmd(group,fileName);
		return cmd.exec(socket);
	}
	
	public StorageResult<List<GroupInfo>> getGroupInfos() throws IOException{
		Socket socket = getSocket();
		Command<List<GroupInfo>> cmd = new GroupInfoCmd();
		return cmd.exec(socket);
	}
	
	public StorageResult<List<StorageInfo>> getStorageInfos(String group) throws IOException{
		Socket socket = getSocket();
		Command<List<StorageInfo>> cmd = new StorageInfoCmd(group);
		return cmd.exec(socket);
	}
	
}
