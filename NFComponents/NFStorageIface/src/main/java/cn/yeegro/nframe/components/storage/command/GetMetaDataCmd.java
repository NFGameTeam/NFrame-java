package cn.yeegro.nframe.components.storage.command;

import cn.yeegro.nframe.common.web.StorageResult;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取文件的元数据信息
 * 
 * @author lixia
 *
 */

public class GetMetaDataCmd extends AbstractCmd<Map<String, String>> {

	@Override
	public StorageResult<Map<String, String>> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		Response response = response(socket.getInputStream());
		if(response.isSuccess()){
			String metaStr = new String(response.getData(),charset);
			Map<String, String> metaData = new HashMap<String, String>();
			String[] rows = metaStr.split(FDFS_RECORD_SEPERATOR);
			for(String row:rows){
				String[] cols = row.split(FDFS_FIELD_SEPERATOR);
				metaData.put(cols[0], cols[1]);
			}
			return new StorageResult<Map<String, String>>(response.getCode(),metaData);
		}else{
			return new StorageResult<Map<String, String>>(response.getCode(),"GetMetaData Error");
		}
	}
	
	/**
   * 在初始化当前实例时，会创建封装获取元数据过程中需要的socket传输的基本字节码信息：命令，group名称，文件名称等。
   * body1（16+文件名字节码长度）：<br>
   *                 0至16：group字节码<br>
   *                 16至最后：文件名字节码<br>
   *                 <br>
   * group1/M00/00/01/CqI-5leN7jyAW56TAACHpYDeJ8s858.jpg信息的切割<br>
   * 
   * @param group      文件的   group信息 group1<br>
   * @param fileName   文件名   M00/00/01/CqI-5leN7jyAW56TAACHpYDeJ8s858.jpg<br>
   */
	public GetMetaDataCmd(String group, String fileName) {
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
		this.requestCmd = STORAGE_PROTO_CMD_GET_METADATA;
		this.responseCmd = STORAGE_PROTO_CMD_RESP;
		this.responseSize = -1;
	}

}
