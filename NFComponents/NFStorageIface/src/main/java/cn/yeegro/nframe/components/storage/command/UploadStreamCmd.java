package cn.yeegro.nframe.components.storage.command;

import cn.yeegro.nframe.common.web.StorageResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * 通过文件流的方式上传文件信息
 * 
 * @author lixia
 *
 */
public class UploadStreamCmd extends AbstractCmd<String> {
	
	private InputStream is;

	@Override
	public StorageResult<String> exec(Socket socket) throws IOException {
		InputStream is = this.is;
		request(socket.getOutputStream(), is);
		Response response = response(socket.getInputStream());
		if(response.isSuccess()){
			byte[] data = response.getData();
			String group = new String(data, 0,	FDFS_GROUP_NAME_MAX_LEN).trim();
			String remoteFileName = new String(data,FDFS_GROUP_NAME_MAX_LEN, data.length - FDFS_GROUP_NAME_MAX_LEN);
			StorageResult<String> result = new StorageResult<String>(response.getCode());
			result.setData(group + "/" + remoteFileName);
			return result;
		}else{
			StorageResult<String> result = new StorageResult<String>(response.getCode());
			result.setMessage("Error");
			return result;
		}
	}

    /**
     *
     * @param file
     * @param extName 文件扩展名，如果传入一个完整文件名，将截取扩展名
     * @param storePathIndex
     */
	public UploadStreamCmd(InputStream is,String extName,byte storePathIndex){
		super();
		this.is = is;
		long size = 0L;
		try {
      size = is.available();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
		this.requestCmd = STORAGE_PROTO_CMD_UPLOAD_FILE;
		this.body2Len = size;
		this.responseCmd = STORAGE_PROTO_CMD_RESP;
		this.responseSize = -1;
		this.body1 = new byte[15];
		Arrays.fill(body1, (byte) 0);
		this.body1[0] = storePathIndex;
		byte[] fileSizeByte = long2buff(size);
		byte[] fileExtNameByte = getFileExtNameByte(extName);
		int fileExtNameByteLen = fileExtNameByte.length;
		if(fileExtNameByteLen>FDFS_FILE_EXT_NAME_MAX_LEN){
			fileExtNameByteLen = FDFS_FILE_EXT_NAME_MAX_LEN;
		}
		System.arraycopy(fileSizeByte, 0, body1, 1, fileSizeByte.length);
		System.arraycopy(fileExtNameByte, 0, body1, fileSizeByte.length + 1, fileExtNameByteLen);
	}

	/**
	 * 截取文件后缀名，并转换字节码
	 * 
	 * @param fileName 文件名或后缀名
	 * @return         文件后缀名的字节码信息   
	 */
	private byte[] getFileExtNameByte(String fileName) {
		String fileExtName = null;
		int nPos = fileName.lastIndexOf('.');
		if (nPos > 0 && fileName.length() - nPos <= FDFS_FILE_EXT_NAME_MAX_LEN + 1) {
			fileExtName = fileName.substring(nPos + 1);
            if (fileExtName != null && fileExtName.length() > 0) {
                return fileExtName.getBytes(charset);
            }else{
                return new byte[0];
            }
		} else {
            //传入的即为扩展名
            return fileName.getBytes(charset);
        }

	}
}
