package cn.yeegro.nframe.components.storage.command;

import cn.yeegro.nframe.common.web.StorageResult;
import cn.yeegro.nframe.components.storage.data.GroupInfo;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取group信息
 * 
 * @author lixia
 *
 */
public class GroupInfoCmd extends AbstractCmd<List<GroupInfo>> {

	@Override
	public StorageResult<List<GroupInfo>> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		Response response = response(socket.getInputStream());
		if(response.isSuccess()){
			byte[] data = response.getData();
			int dataLength = data.length;
			if(dataLength%GroupInfo.BYTE_SIZE!=0){
				throw new IOException("recv body length: " + data.length + " is not correct");
			}
			List<GroupInfo> groupInfos = new ArrayList<GroupInfo>();
			int offset = 0;
			while(offset<dataLength) {
				GroupInfo groupInfo = new GroupInfo(data,offset);
				groupInfos.add(groupInfo);
				offset += GroupInfo.BYTE_SIZE;
			}
			return new StorageResult<List<GroupInfo>>(response.getCode(),groupInfos);
		}
		else {
			return new StorageResult<List<GroupInfo>>(response.getCode(),"Error");
		}
	}

	public GroupInfoCmd() {
		super();
		this.requestCmd = TRACKER_PROTO_CMD_SERVER_LIST_GROUP;
		this.responseCmd = TRACKER_PROTO_CMD_RESP;
		this.responseSize = -1;
	}

}
