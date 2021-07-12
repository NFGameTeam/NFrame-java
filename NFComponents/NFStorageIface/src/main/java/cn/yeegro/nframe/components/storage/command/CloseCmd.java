package cn.yeegro.nframe.components.storage.command;

import cn.yeegro.nframe.common.web.StorageResult;

import java.io.IOException;
import java.net.Socket;

/**
 * 关闭连接服务
 * 
 * @author lixia
 *
 */
public class CloseCmd extends AbstractCmd<Boolean> {
	
	public CloseCmd() {
		super();
		this.requestCmd = FDFS_PROTO_CMD_QUIT;
	}

	@Override
	public StorageResult<Boolean> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		return new StorageResult<Boolean>(SUCCESS_CODE,true);
	}
}
