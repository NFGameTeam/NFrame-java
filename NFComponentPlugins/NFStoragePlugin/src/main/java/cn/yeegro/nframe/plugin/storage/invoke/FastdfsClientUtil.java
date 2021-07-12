package cn.yeegro.nframe.plugin.storage.invoke;

import cn.yeegro.nframe.components.storage.utils.IOCloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * fastdfs客户端工具类
 * 
 * 可以通过文件流和文件名执行上传操作，通过fileId执行删除操作
 * 执行完上传/更新/删除等操作后，请勿close，FastdfsClientFactory中缓存了FastdfsClient，关闭对象池后将无法调用客户端实例对象
 * 
 * @author lixia
 *
 */
public class FastdfsClientUtil {
  private static Logger logger = LoggerFactory.getLogger(FastdfsClientUtil.class);
  
  /**
   * 通过文件输入流的方式进行文件上传
   * 
   * @param fileName  原始文件名
   * @param is        文件输入流
   * @return
   */
  public static String upload(String fileName,InputStream is) {
    //通过fastdfs客户端工厂创建fastdfs客户端对象
    FastdfsClient fastdfsClient = FastdfsClientFactory.getFastdfsClient();
    String ex = fileName.substring(fileName.lastIndexOf(".")+1);
    String fileId = "";
    try {
      fileId = fastdfsClient.upload(is, ex, null);
      logger.info("upload fileId is:"+fileId);
    }
    catch (Exception e) {
      e.printStackTrace();
      logger.info("upload fileId fail");
      return "";
    }
    finally{
      IOCloseUtil.close(is);
    }
    
    /**
     * 目前的访问地址是：
     * http://10.162.62.230:9004/resource/00/00/CqI-5leEpL2AdsgJAAKu13Ay66Q406_big.jpg
     * 截取获取到的fileId
     * group1/M00/00/01/CqI-5leMgBeAWqrAAACHpYDeJ8s653.jpg
     * 将fileId转换为可以访问的地址连接
     * 00/01/CqI-5leMgBeAWqrAAACHpYDeJ8s653.jpg
     */
    if(!fileId.equals("")&&fileId.length()>10&&fileId.indexOf("group1/M00/")!=-1){
      fileId = fileId.substring(10);
    }
    return fileId;
  }
  
  
  /**
   * 根据fileId删除fastdfs文件服务器文件
   * fileId格式：
   * group1/M00/00/01/CqI-5leMgBeAWqrAAACHpYDeJ8s653.jpg
   * 
   * @param fileId 
   * @return
   */
  public static boolean delete(String fileId){
    FastdfsClient fastdfsClient = FastdfsClientFactory.getFastdfsClient();
    boolean flag = true;
    try {
      flag = fastdfsClient.delete(fileId);
      logger.info("delete fileId success");
    }
    catch (Exception e) {
      logger.info("delete fileId fail",e);
      return false;
    }
    finally{
    }
    return flag;
  }
}
