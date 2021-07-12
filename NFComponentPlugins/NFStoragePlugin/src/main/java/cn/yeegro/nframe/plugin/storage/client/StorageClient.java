package cn.yeegro.nframe.plugin.storage.client;


import cn.yeegro.nframe.common.web.StorageResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public interface StorageClient {
	
  /**
   * 通过file和文件名上传
   * 
   * @param file
   * @param fileName
   * @param storePathIndex
   * @return
   * @throws IOException
   */
  public StorageResult<String> upload(File file, String fileName, byte storePathIndex) throws IOException;
  
  /**
   * 通过文件流和文件名上传
   * @param file
   * @param fileName
   * @param storePathIndex
   * @return
   * @throws IOException
   */
  public StorageResult<String> upload(InputStream file, String fileName, byte storePathIndex) throws IOException;
	
	/**
	 * 通过group和文件名删除服务器文件
	 * 
	 * @param group   
   *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        g1即为groupName<br> 
   * @param fileName
   *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        M00/00/00/aaaabbbbccc.jpg为fileName<br>
	 * @return
	 * @throws IOException
	 */
	public StorageResult<Boolean> delete(String group, String fileName) throws IOException;
	
	/**
	 * 设置服务器文件的元数据信息
	 * @param group   
   *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        g1即为groupName<br> 
   * @param fileName
   *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        M00/00/00/aaaabbbbccc.jpg为fileName<br>
	 * @param meta
	 *        map键值对属性信息
	 * @return
	 * @throws IOException
	 */
	public StorageResult<Boolean> setMeta(String group, String fileName, Map<String, String> meta) throws IOException;
	/**
	 * 获取服务器文件的元数据信息
	 * @param group   
   *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        g1即为groupName<br> 
   * @param fileName
   *        如 g1/M00/00/00/aaaabbbbccc.jpg中的<br>
   *        M00/00/00/aaaabbbbccc.jpg为fileName<br>
	 * @return
	 * @throws IOException
	 */
	public StorageResult<Map<String,String>> getMeta(String group, String fileName) throws IOException;
	/**
	 * 发送关闭命令至服务器
	 * @throws IOException
	 */
	public void close() throws IOException;

    /**
     * 指定主文件id,存为slave
     * @param file 文件
     * @param fileid 主文件id,带group,如 g1/M00/00/00/aaaabbbbccc.jpg
     * @param slavePrefix slave的后缀名 如200x200,最终的文件名将为g1/M00/00/00/aaaabbbbccc_200x200.jpg
     * @param ext 扩展文件名，可以为null,如果为null，则从fileid里取
     * @param meta 文件元数据，可以为null
     * @return fileid 带group的文件fileid
     * @throws IOException
     */
    public StorageResult<String> uploadSlave(File file, String fileid, String slavePrefix, String ext, Map<String, String> meta) throws IOException;

    /**
     * 判断storage socket是否关闭
     * @return boolean  
     */
    public boolean isClosed();
}
