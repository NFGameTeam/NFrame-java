package cn.yeegro.nframe.plugin.storage.client;


import cn.yeegro.nframe.components.storage.command.AbstractCmd;

/**
 * 提供一些公用的的方法
 * 
 * @author lixia
 */
public abstract class AbstractClient {
  /**
   * 传入带group的fileid,返回group和filename的二元数组
   * 
   * @param fileid  fileid like g1/M00/00/00/abc.jpg
   * @return        string[0] = g1, string[1]=M00/00/00/abc.jpg
   */
  protected String[] splitFileId(String fileid) {
    return AbstractCmd.splitFileId(fileid);
  }
}
