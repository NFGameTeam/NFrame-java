package cn.yeegro.nframe.components.storage.command;

import cn.yeegro.nframe.common.web.StorageResult;

import java.io.IOException;
import java.net.Socket;

/**
 * 测试socket通信是否正常，服务器是否正常
 * 
 * @author lixia
 *
 */
public class ActiveTestCmd extends AbstractCmd<Boolean> {

  public ActiveTestCmd() {
    super();
    this.requestCmd = FDFS_PROTO_CMD_ACTIVE_TEST;
  }

  /**
   * 测试服务器是否正常
   */
  @Override
  public StorageResult<Boolean> exec(Socket socket) throws IOException {
    request(socket.getOutputStream());
    Response response = response(socket.getInputStream());
    if (response.isSuccess()) {
      return new StorageResult<Boolean>(SUCCESS_CODE, true);
    } else {
      return new StorageResult<Boolean>(response.getCode(), false);
    }
  }
}
