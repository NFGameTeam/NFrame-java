package cn.yeegro.nframe.plugin.storage.invoke;


import cn.yeegro.nframe.common.web.StorageResult;
import cn.yeegro.nframe.components.storage.data.GroupInfo;
import cn.yeegro.nframe.components.storage.data.StorageInfo;
import cn.yeegro.nframe.components.storage.data.UploadStorage;
import cn.yeegro.nframe.plugin.storage.client.*;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FastDfs客户端调用 实现设定属性和上传关闭池对象功能
 * 
 * @author lixia
 * 
 */

public class FastdfsClientImpl extends AbstractClient implements FastdfsClient {

  private static Logger logger = LoggerFactory
      .getLogger(FastdfsClientImpl.class);
  /**
   * apache tracker和storage对象池
   */
  private GenericKeyedObjectPool<String, TrackerClient> trackerClientPool;
  private GenericKeyedObjectPool<String, StorageClient> storageClientPool;
  /**
   * tracker地址列表
   */
  private List<String> trackerAddrs = new ArrayList<String>();
  /**
   * storage map列表
   */
  private Map<String, String> storageIpMap = new ConcurrentHashMap<String, String>();

  /**
   * 根据追踪器地址list初始化fastdfs客户端实例
   * 
   * @param trackerAddrs
   *          tracker地址
   * @throws Exception
   */
  public FastdfsClientImpl(List<String> trackerAddrs) throws Exception {
    super();
    this.trackerAddrs = trackerAddrs;
    trackerClientPool = new GenericKeyedObjectPool<String, TrackerClient>(
        new TrackerClientFactory());
    storageClientPool = new GenericKeyedObjectPool<String, StorageClient>(
        new StorageClientFactory());
    updateStorageIpMap();
  }

  /**
   * 根据追踪器地址list，tracker对象池，storage对象池 初始化fastdfs客户端实例
   * 
   * @param trackerAddrs
   * @param trackerClientPool
   * @param storageClientPool
   */
  public FastdfsClientImpl(List<String> trackerAddrs,
                           GenericKeyedObjectPool<String, TrackerClient> trackerClientPool,
                           GenericKeyedObjectPool<String, StorageClient> storageClientPool) {
    super();
    this.trackerAddrs = trackerAddrs;
    this.trackerClientPool = trackerClientPool;
    this.storageClientPool = storageClientPool;
  }

  /**
   * 关闭对象池 关闭对象池后，实例必须被重新初始化才可以进行功能调用
   */
  @Override
  public void close() {
    this.trackerClientPool.close();
    this.storageClientPool.close();
  }

  /**
   * 上传文件方法，根据输入文件流的方式进行文件上传
   * 
   * @param fileIs
   *          文件流
   * @param ext
   *          上传文件的后缀名
   * @param meta
   *          上传文件的元数据
   */
  @Override
  public String upload(InputStream fileIs, String ext, Map<String, String> meta)
      throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    StorageClient storageClient = null;
    String storageAddr = null;
    String fileId = null;
    try {
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<UploadStorage> result = trackerClient.getUploadStorage();
      if (result.getCode() == 0) {
        storageAddr = result.getData().getAddress();
        storageClient = storageClientPool.borrowObject(storageAddr);

        String extname = ext;
        StorageResult<String> result2 = storageClient.upload(fileIs, extname, result
            .getData().getPathIndex());
        if (result2.getCode() == 0) {
          fileId = result2.getData();
          // if meta key value
          if (meta != null) {
            this.setMeta(fileId, meta);
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (storageClient != null) {
        storageClientPool.returnObject(storageAddr, storageClient);
      }
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
    return fileId;
  }

  /**
   * 上传文件方法，通过文件的方式进行上传
   * 
   * @param file
   *          上传的文件
   * @param ext
   *          上传文件的后缀名
   * @param meta
   *          上传文件的元数据
   */
  @Override
  public String upload(File file, String ext, Map<String, String> meta)
      throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    StorageClient storageClient = null;
    String storageAddr = null;
    String fileId = null;
    try {
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<UploadStorage> result = trackerClient.getUploadStorage();
      if (result.getCode() == 0) {
        storageAddr = result.getData().getAddress();
        storageClient = storageClientPool.borrowObject(storageAddr);

        String extname = ext;
        if (ext == null) {
          extname = getFileExtName(file);
        }
        StorageResult<String> result2 = storageClient.upload(file, extname, result
            .getData().getPathIndex());
        if (result2.getCode() == 0) {
          fileId = result2.getData();
          // if meta key value
          if (meta != null) {
            this.setMeta(fileId, meta);
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (storageClient != null) {
        storageClientPool.returnObject(storageAddr, storageClient);
      }
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
    return fileId;
  }

  @Override
  public String uploadSlave(File file, String fileid, String prefix, String ext)
      throws Exception {

    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    StorageClient storageClient = null;
    String storageAddr = null;
    String fileId = null;
    try {
      trackerClient = trackerClientPool.borrowObject(trackerAddr);

      if (fileid != null) {
        String[] tupple = splitFileId(fileid);
        String groupname = tupple[0];
        String filename = tupple[1];

        StorageResult<String> result = trackerClient.getUpdateStorageAddr(groupname,
            filename);
        if (result.getCode() == 0) {
          storageAddr = result.getData();
          storageClient = storageClientPool.borrowObject(storageAddr);
          StorageResult<String> result2 = storageClient.uploadSlave(file, filename,
              prefix, ext, null);
          if (result2.getCode() == 0) {
            fileId = result2.getData();
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (storageClient != null) {
        storageClientPool.returnObject(storageAddr, storageClient);
      }
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
    return fileId;
  }

  /**
   * 根据tracker获取storage的ip和端口信息并放到map中
   * 
   * （group和storage中的详细参数相见GroupInfo，StorageInfo） 1.根据tracker获取group信息
   * 2.根据tracker通过group名称获取storage信息
   * 
   * @throws Exception
   */
  private void updateStorageIpMap() throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    try {
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<List<GroupInfo>> result = trackerClient.getGroupInfos();
      if (result.getCode() == 0) {
        List<GroupInfo> groupInfos = result.getData();
        for (GroupInfo groupInfo : groupInfos) {
          StorageResult<List<StorageInfo>> result2 = trackerClient
              .getStorageInfos(groupInfo.getGroupName());
          if (result2.getCode() == 0) {
            List<StorageInfo> storageInfos = result2.getData();
            for (StorageInfo storageInfo : storageInfos) {
              String hostPort = storageInfo.getDomainName();
              if (storageInfo.getStorageHttpPort() != 80) {
                hostPort = hostPort + ":" + storageInfo.getStorageHttpPort();
              }
              storageIpMap.put(
                  storageInfo.getIpAddr() + ":" + storageInfo.getStoragePort(),hostPort);
            }
          }
        }
      } else {
        throw new Exception("Get getGroupInfos Error");
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
  }

  /**
   * 通过storage地址（ip：端口）获取hostport
   * 
   * @param storageAddr
   * @return
   * @throws Exception
   */
  private String getDownloadHostPort(String storageAddr) throws Exception {
    String downloadHostPort = storageIpMap.get(storageAddr);
    if (downloadHostPort == null) {
      updateStorageIpMap();
      downloadHostPort = storageIpMap.get(storageAddr);
    }
    return downloadHostPort;
  }

  /**
   * 设置上传文件的元数据
   * 
   * @param fileId
   *          文件id 格式：group1/M00/00/01/CqI-5leMgBeAWqrAAACHpYDeJ8s653.jpg
   * @param meta
   *          上传文件的元数据信息（属性信息）
   * 
   */
  @Override
  public Boolean setMeta(String fileId, Map<String, String> meta)
      throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    StorageClient storageClient = null;
    Boolean result = null;
    String storageAddr = null;
    try {
      FastDfsFile fastDfsFile = new FastDfsFile(fileId);
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<String> result2 = trackerClient.getUpdateStorageAddr(
          fastDfsFile.group, fastDfsFile.fileName);
      if (result2.getCode() == 0) {
        storageAddr = result2.getData();
        storageClient = storageClientPool.borrowObject(storageAddr);
        StorageResult<Boolean> result3 = storageClient.setMeta(fastDfsFile.group,
            fastDfsFile.fileName, meta);
        if (result3.getCode() == 0 || result3.getCode() == 0) {
          result = result3.getData();
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (storageClient != null) {
        storageClientPool.returnObject(storageAddr, storageClient);
      }
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
    return result;
  }

  /**
   * 根据fileId获取文件对应的元数据（metadata）
   * 
   * @param fileId
   *          文件的ID 格式：group1/M00/00/01/CqI-5leMgBeAWqrAAACHpYDeJ8s653.jpg
   */
  @Override
  public Map<String, String> getMeta(String fileId) throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    StorageClient storageClient = null;
    Map<String, String> meta = null;
    String storageAddr = null;
    try {
      FastDfsFile fastDfsFile = new FastDfsFile(fileId);
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<String> result2 = trackerClient.getUpdateStorageAddr(
          fastDfsFile.group, fastDfsFile.fileName);
      if (result2.getCode() == 0) {
        storageAddr = result2.getData();
        storageClient = storageClientPool.borrowObject(storageAddr);
        StorageResult<Map<String, String>> result3 = storageClient.getMeta(
            fastDfsFile.group, fastDfsFile.fileName);
        if (result3.getCode() == 0 || result3.getCode() == 0) {
          meta = result3.getData();
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (storageClient != null) {
        storageClientPool.returnObject(storageAddr, storageClient);
      }
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
    return meta;
  }

  /**
   * 根据fileId获取文件访问地址
   * 
   * @fileId 文件id 格式：group1/M00/00/01/CqI-5leMgBeAWqrAAACHpYDeJ8s653.jpg
   */
  public String getUrl(String fileId) throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    String url = null;
    try {
      FastDfsFile fastDfsFile = new FastDfsFile(fileId);
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<String> result = trackerClient.getDownloadStorageAddr(
          fastDfsFile.group, fastDfsFile.fileName);
      if (result.getCode() == 0) {
        String hostPort = getDownloadHostPort(result.getData());
        url = "http://" + hostPort + "/" + fastDfsFile.fileName;
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
    return url;
  }

  public String upload(File file) throws Exception {
    String fileName = file.getName();
    return upload(file, fileName);
  }

  public String upload(File file, String fileName) throws Exception {
    return this.upload(file, fileName, null);
  }

  /**
   * 通过fileId删除文件
   * 
   * @param fileId
   *          文件的id 格式：group1/M00/00/01/CqI-5leMgBeAWqrAAACHpYDeJ8s653.jpg
   * 
   */
  @Override
  public Boolean delete(String fileId) throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    StorageClient storageClient = null;
    Boolean result = false;
    String storageAddr = null;
    try {
      FastDfsFile fastDfsFile = new FastDfsFile(fileId);
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<String> result2 = trackerClient.getUpdateStorageAddr(
          fastDfsFile.group, fastDfsFile.fileName);
      if (result2.getCode() == 0) {
        storageAddr = result2.getData();
        storageClient = storageClientPool.borrowObject(storageAddr);
        StorageResult<Boolean> result3 = storageClient.delete(fastDfsFile.group,
            fastDfsFile.fileName);
        if (result3.getCode() == 0 || result3.getCode() == 0) {
          result = true;
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (storageClient != null) {
        storageClientPool.returnObject(storageAddr, storageClient);
      }
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
    return result;
  }

  /**
   * 获取全部的group信息
   */
  @Override
  public void getGroupInfos() throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    try {
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<List<GroupInfo>> result = trackerClient.getGroupInfos();
      if (result.getCode() == 0) {
        List<GroupInfo> groupInfos = result.getData();
        for (GroupInfo groupInfo : groupInfos) {
          System.out.println(groupInfo.toString());
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
  }
  
  /**
   * 获取全部的storage信息
   * 
   */
  @Override
  public void getStorageInfos() throws Exception {
    String trackerAddr = getTrackerAddr();
    TrackerClient trackerClient = null;
    try {
      trackerClient = trackerClientPool.borrowObject(trackerAddr);
      StorageResult<List<GroupInfo>> result = trackerClient.getGroupInfos();
      if (result.getCode() == 0) {
        List<GroupInfo> groupInfos = result.getData();
        for (GroupInfo groupInfo : groupInfos) {
          System.out.println(groupInfo.toString());
          
          StorageResult<List<StorageInfo>> resultStorage = trackerClient.getStorageInfos(groupInfo.getGroupName());
          List<StorageInfo> storageInfos = resultStorage.getData();
          for (StorageInfo storageInfo : storageInfos) {
            System.out.println(storageInfo.toString());
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    } finally {
      if (trackerClient != null) {
        trackerClientPool.returnObject(trackerAddr, trackerClient);
      }
    }
  }

  /**
   * 通过随机的方式选取追踪器地址
   * 
   * @return tracker的ip+port
   */
  public String getTrackerAddr() {
    Random r = new Random();
    int i = r.nextInt(trackerAddrs.size());
    return trackerAddrs.get(i);
  }

  /**
   * 根据文件名获取文件后缀名
   * 
   * @param file
   * @return 文件后缀名
   */
  private String getFileExtName(File file) {
    String name = file.getName();
    if (name != null) {
      int i = name.lastIndexOf('.');
      if (i > -1) {
        return name.substring(i + 1);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * group和fileName的bean 用于将fieldId分解
   */
  private class FastDfsFile {
    private String group;
    private String fileName;

    public FastDfsFile(String fileId) {
      super();
      int pos = fileId.indexOf("/");
      group = fileId.substring(0, pos);
      fileName = fileId.substring(pos + 1);
    }

  }
}
