package cn.yeegro.nframe.plugin.storage.client;

import cn.yeegro.nframe.common.web.StorageResult;
import cn.yeegro.nframe.components.storage.data.GroupInfo;
import cn.yeegro.nframe.components.storage.data.StorageInfo;
import cn.yeegro.nframe.components.storage.data.UploadStorage;

import java.io.IOException;
import java.util.List;

/**
 * 追踪器客户端接口<br>
 * 1.通过追踪器获取group信息和storage信息。<br>
 * 2.通过追踪器和fileId获取文件存储的storage信息和下载地址等<br>
 * 
 * @author lixia
 *
 */
public interface TrackerClient {
  /**
   * 获取上传的storage信息，包含ip+port ,pathIndex
   * @return
   * @throws IOException
   */
	public StorageResult<UploadStorage> getUploadStorage() throws IOException;
	/**
	 * 根据groupName和fileName获取storageAddr<br>
	 * 
	 * @param group   
	 *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        g1即为groupName<br> 
	 * @param fileName
	 *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
	 *        M00/00/00/aaaabbbbccc.jpg为fileName<br>
	 * @return ip+port
	 * @throws IOException
	 */
	public StorageResult<String> getUpdateStorageAddr(String group, String fileName) throws IOException;
	
	/**
   * 根据groupName和fileName获取下载地址<br>
   * 
   * @param group   
   *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        g1即为groupName<br> 
   * @param fileName
   *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        M00/00/00/aaaabbbbccc.jpg为fileName<br>
   * @return  ip+port
   * @throws IOException
   */
	public StorageResult<String> getDownloadStorageAddr(String group, String fileName) throws IOException;
	/**
	 * 获取group信息
	 * @return group结果列表
	 * @throws IOException
	 */
	public StorageResult<List<GroupInfo>> getGroupInfos() throws IOException;
	/**
	 * 根据groupName获取storage信息
	 * 
	 * @param group  groupName
	 *     如 g1/M00/00/00/aaaabbbbccc.jpg中的g1即为groupName
	 * @return storage结果列表
	 * @throws IOException
	 */
	public StorageResult<List<StorageInfo>> getStorageInfos(String group) throws IOException;
	/**
	 * 关闭socket，发送关闭命令至服务器
	 * @throws IOException
	 */
	public void close() throws IOException;
	
}
