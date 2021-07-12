package cn.yeegro.nframe.components.storage.command;

import cn.yeegro.nframe.common.web.StorageResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * 处理文件上传的类<br>
 * 负责组织检查封装socket传递的数据信息，并对服务器返回的数据进行解析封装。<br>
 * 在初始化当前实例时，会创建上传过程中需要的socket传输的基本字节码信息：命令，文件长度，文件后缀名等。<br>
 * 
 * @author lixia
 *
 */
public class UploadCmd extends AbstractCmd<String> {

  /**
   * 上传的文件信息
   */
  private File file;
  /**
   * 执行上传操作：
   * 1.socket发送请求信息
   * 2.socket发送文件信息
   * 3.socket获取响应信息
   * 4.解析并封装返回结果
   * 
   * @param socket  通信socket
   * @return        返回结果封装对象，详见Result
   */
  @Override
  public StorageResult<String> exec(Socket socket) throws IOException {
    //获取文件流
    InputStream is = new FileInputStream(file);
    //发送上传参数和文件字节码
    request(socket.getOutputStream(), is);
    
    //读取响应的结果信息，并做检查和数据封装
    Response response = response(socket.getInputStream());
    if (response.isSuccess()) {
      byte[] data = response.getData();
      //group信息
      String group = new String(data, 0, FDFS_GROUP_NAME_MAX_LEN).trim();
      //文件fileId（去掉group的fileId）
      String remoteFileName = new String(data, FDFS_GROUP_NAME_MAX_LEN,
          data.length - FDFS_GROUP_NAME_MAX_LEN);
      StorageResult<String> result = new StorageResult<String>(response.getCode());
      //文件fileId
      result.setData(group + "/" + remoteFileName);
      return result;
    } else {
      StorageResult<String> result = new StorageResult<String>(response.getCode());
      result.setMessage("Error");
      return result;
    }
  }

  /**
   * 在初始化当前实例时，会创建封装上传过程中需要的socket传输的基本字节码信息：命令，文件长度，文件后缀名等。
   * <br>
   * body1 （长度15）字节码：<br>
   *            0：storePathIndex字节码<br>
   *            1到fileSizeByte.length：文件长度字节码<br>
   *            fileSizeByte.length + 1到fileExtNameByteLen：后缀名字节码<br>
   *            
   * @param file
   *          需要上传的文件
   * @param extName
   *          文件扩展名，如果传入一个完整文件名，将截取扩展名
   * @param storePathIndex  
   *          storage的indexPath
   * 
   */
  public UploadCmd(File file, String extName, byte storePathIndex) {
    super();
    this.file = file;
    //设定命令，上传为11
    this.requestCmd = STORAGE_PROTO_CMD_UPLOAD_FILE;
    //文件大小
    this.body2Len = file.length();
    //响应命令是100
    this.responseCmd = STORAGE_PROTO_CMD_RESP;
    this.responseSize = -1;
    //初始化15个0的body1数组
    this.body1 = new byte[15];
    Arrays.fill(body1, (byte) 0);
    //body1中设定storePathIndex的字节码
    this.body1[0] = storePathIndex;
    byte[] fileSizeByte = long2buff(file.length());
    byte[] fileExtNameByte = getFileExtNameByte(extName);
    //文件后缀名长度检查，最大不能超过6位
    int fileExtNameByteLen = fileExtNameByte.length;
    if (fileExtNameByteLen > FDFS_FILE_EXT_NAME_MAX_LEN) {
      fileExtNameByteLen = FDFS_FILE_EXT_NAME_MAX_LEN;
    }
    System.arraycopy(fileSizeByte, 0, body1, 1, fileSizeByte.length);
    System.arraycopy(fileExtNameByte, 0, body1, fileSizeByte.length + 1,fileExtNameByteLen);
  }

  /**
   * 获取文件的后缀名，并转字节码
   * 
   * @param fileName  文件名称
   * @return          文件后缀名的字节码
   */
  private byte[] getFileExtNameByte(String fileName) {
    String fileExtName = null;
    int nPos = fileName.lastIndexOf('.');
    if (nPos > 0 && fileName.length() - nPos <= FDFS_FILE_EXT_NAME_MAX_LEN + 1) {
      fileExtName = fileName.substring(nPos + 1);
      if (fileExtName != null && fileExtName.length() > 0) {
        return fileExtName.getBytes(charset);
      } else {
        return new byte[0];
      }
    } else {
      // 传入的即为扩展名
      return fileName.getBytes(charset);
    }
  }
}
