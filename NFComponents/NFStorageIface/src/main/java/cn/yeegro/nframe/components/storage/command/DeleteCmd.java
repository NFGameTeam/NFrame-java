package cn.yeegro.nframe.components.storage.command;

import cn.yeegro.nframe.common.web.StorageResult;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * 删除服务器上文件信息
 * 
 * @author lixia
 *
 */
public class DeleteCmd extends AbstractCmd<Boolean> {

	@Override
	public StorageResult<Boolean> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		Response response = response(socket.getInputStream());
		if(response.isSuccess()){
			return new StorageResult<Boolean>(response.getCode(),true);
		}else{
			return new StorageResult<Boolean>(response.getCode(),"Delete Error");
		}
	}

	/**
	 * 在初始化当前实例时，会创建封装删除过程中需要的socket传输的基本字节码信息：命令，group名称，文件名称等。
	 * body1（16+文件名字节码长度）：<br>
	 *                 0至16：group字节码<br>
	 *                 16至最后：文件名字节码<br>
	 *                 <br>
	 * group1/M00/00/01/CqI-5leN7jyAW56TAACHpYDeJ8s858.jpg信息的切割<br>
	 * 
	 * @param group      文件的   group信息 group1<br>
	 * @param fileName   文件名   M00/00/01/CqI-5leN7jyAW56TAACHpYDeJ8s858.jpg<br>
	 */
	public DeleteCmd(String group,String fileName) {
		super();
		byte[] groupByte = group.getBytes(charset);
		int group_len = groupByte.length;
		if (group_len > FDFS_GROUP_NAME_MAX_LEN) {
			group_len = FDFS_GROUP_NAME_MAX_LEN;
		}
		byte[] fileNameByte = fileName.getBytes(charset);
		body1 = new byte[FDFS_GROUP_NAME_MAX_LEN + fileNameByte.length];
		Arrays.fill(body1, (byte) 0);
		System.arraycopy(groupByte, 0, body1, 0, group_len);
		System.arraycopy(fileNameByte, 0, body1, FDFS_GROUP_NAME_MAX_LEN, fileNameByte.length);
		this.requestCmd = STORAGE_PROTO_CMD_DELETE_FILE;
		this.responseCmd = STORAGE_PROTO_CMD_RESP;
		this.responseSize = 0;
	}

}
