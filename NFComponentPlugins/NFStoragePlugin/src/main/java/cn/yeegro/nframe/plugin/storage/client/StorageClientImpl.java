package cn.yeegro.nframe.plugin.storage.client;


import cn.yeegro.nframe.common.web.StorageResult;
import cn.yeegro.nframe.components.storage.command.*;
import cn.yeegro.nframe.plugin.storage.invoke.FastdfsClientConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;


/**
 * StorageClient客户端实现类
 * 
 * @author lixia
 *
 */
public class StorageClientImpl extends AbstractClient implements StorageClient {

  private Socket socket;
  private String host;
  private Integer port;
  private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
  private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;
  
  private Socket getSocket() throws IOException {
    if (socket == null) {
      socket = new Socket();
      socket.setSoTimeout(networkTimeout);
      System.out.println("socket连接地址:"+host+","+port);
      socket.connect(new InetSocketAddress(host, port), connectTimeout);
    }
    return socket;
  }

  public StorageClientImpl(String address) {
    super();
    String[] hostport = address.split(":");
    this.host = hostport[0];
    this.port = Integer.valueOf(hostport[1]);
  }

  public StorageClientImpl(String address, Integer connectTimeout, Integer networkTimeout) {
    this(address);
    this.connectTimeout = connectTimeout;
    this.networkTimeout = networkTimeout;
  }

  public void close() throws IOException {
    Socket socket = getSocket();
    Command<Boolean> command = new CloseCmd();
    command.exec(socket);
    socket.close();
    socket = null;
  }

  @Override
  public StorageResult<String> uploadSlave(File file, String fileid, String slavePrefix, String ext, Map<String, String> meta) throws IOException {
    Socket socket = getSocket();
    UploadSlaveCmd uploadSlaveCmd = new UploadSlaveCmd(file,fileid,slavePrefix,ext);
    StorageResult<String> result = uploadSlaveCmd.exec(socket);

    if( meta != null ) {
      String[] tupple = super.splitFileId(fileid);
      if( tupple != null ) {
        String group = tupple[0];
        String fileName = tupple[1];
        this.setMeta(group, fileName, meta);
      }
    }
    return result;
  }

  @Override
  public StorageResult<String> upload(InputStream is, String fileName,
        byte storePathIndex) throws IOException {
    Socket socket = getSocket();
    UploadStreamCmd uploadCmd = new UploadStreamCmd(is, fileName, storePathIndex);
    return uploadCmd.exec(socket);
  }
  
  public StorageResult<String> upload(File file, String fileName, byte storePathIndex) throws IOException {
    Socket socket = getSocket();
    UploadCmd uploadCmd = new UploadCmd(file, fileName, storePathIndex);
    return uploadCmd.exec(socket);
  }

  public StorageResult<Boolean> delete(String group, String fileName) throws IOException {
    Socket socket = getSocket();
    DeleteCmd deleteCmd = new DeleteCmd(group, fileName);
    return deleteCmd.exec(socket);
  }

  @Override
  public StorageResult<Boolean> setMeta(String group, String fileName,
                                   Map<String, String> meta) throws IOException {
    Socket socket = getSocket();
    SetMetaDataCmd setMetaDataCmd = new SetMetaDataCmd(group, fileName, meta);
    return setMetaDataCmd.exec(socket);
  }

  @Override
  public StorageResult<Map<String, String>> getMeta(String group, String fileName) throws IOException {
    Socket socket = getSocket();
    GetMetaDataCmd getMetaDataCmd = new GetMetaDataCmd(group, fileName);
    return getMetaDataCmd.exec(socket);
  }

  /**
   * check storage client socket is closed
   *
   * @return boolean
   */
  @Override
  public boolean isClosed() {
    if (this.socket == null) {
        return true;
    }

    if( this.socket.isClosed() )
      return true;
    
    //根据fastdfs的Active_Test_Cmd测试连通性
    ActiveTestCmd atcmd = new ActiveTestCmd();
    try {
      StorageResult<Boolean> result = atcmd.exec(getSocket());
      //True,表示连接正常
      return result.getData();
    }
    catch (IOException e) {
      //logger.error("execute ActiveTestCmd exception",e);
    }
    //有异常，直接丢掉这个连接，让连接池回收
    return true;
  }

}
