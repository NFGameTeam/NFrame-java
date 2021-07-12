package cn.yeegro.nframe.plugin.storage.invoke;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface FastdfsClient {

  /**
   * 通过文件上传
   * 
   * @param file
   *          文件对象
   * @return fileId
   * @throws Exception
   */
  public String upload(File file) throws Exception;

  /**
   * 通过文件和文件名上传
   * 
   * @param file
   *          文件对象
   * @param fileName
   *          文件名
   * @return fileID
   * @throws Exception
   */
  public String upload(File file, String fileName) throws Exception;

  /**
   * 通过文件流方式进行上传，文件后缀名，元数据（属性信息，键值对得方式，如：width:200等）
   * 
   * @param fileIs
   *          文件输入流
   * @param ext
   *          后缀名
   * @param meta
   *          元数据（可以为空）
   * @return fileId
   * @throws Exception
   */
  public String upload(InputStream fileIs, String ext, Map<String, String> meta)
      throws Exception;

  /**
   * 获取下载url地址<br>
   * 目前得到的地址是：http://10.162.62.230:8888/M00/00/01/CqI-5lePFXKAZDvZAACHpYDeJ8s898.jpg<br>
   * 有bug
   * @param fileId
   * @return
   * @throws Exception
   */
  public String getUrl(String fileId) throws Exception;

  /**
   * 通过fileId设置文件的元数据（属性信息，键值对得方式，如：width:200等）
   * 
   * @param fileId
   *          文件的ID
   * @param meta
   *          元数据
   * @return 设置成功返回true
   * @throws Exception
   */
  public Boolean setMeta(String fileId, Map<String, String> meta)
      throws Exception;

  /**
   * 通过fileId获取文件的元数据（属性信息，键值对得方式，如：width:200等）
   * 
   * @param fileId
   *          文件的ID
   * @return 返回元数据的map
   * @throws Exception
   */
  public Map<String, String> getMeta(String fileId) throws Exception;

  /**
   * 通过fileId删除服务器上的file信息
   * 
   * @param fileId
   *          文件的ID
   * @return 成功返回true
   * @throws Exception
   */
  public Boolean delete(String fileId) throws Exception;

  /**
   * 关闭对象池信息
   * 
   */
  public void close();

  /**
   * 上传一个文件
   * 
   * @param file
   *          要上传的文件
   * @param ext
   *          文件扩展名
   * @param meta
   *          meta key/value的meta data，可为null
   * @return fileid 带group的fileid
   * @throws Exception
   */
  public String upload(File file, String ext, Map<String, String> meta)
      throws Exception;

  /**
   * upload slave
   * 
   * @param file
   * @param fileid
   *          带group的fileid,like group1/M00/00/01/abc.jpg
   * @param prefix
   *          slave的扩展名，如200x200
   * @param ext
   *          文件扩展名，like jpg，不带.
   * @return 上传后的fileid group1/M00/00/01/abc_200x200.jpg
   * @throws Exception
   */
  public String uploadSlave(File file, String fileid, String prefix, String ext)
      throws Exception;
  
  /**
   * 获取全部的group信息
   * @throws Exception
   */
  public void getGroupInfos() throws Exception;
  /**
   * 获取全部的group信息
   * @throws Exception
   */
  public void getStorageInfos() throws Exception;

}
