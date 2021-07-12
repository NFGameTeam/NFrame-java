package cn.yeegro.nframe.components.storage.command;



import cn.yeegro.nframe.common.web.StorageResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * 上传从文件信息
 * 
 * @author lixia
 *
 */
public class UploadSlaveCmd extends AbstractCmd<String> {

	private File file;

    /**
     *
     * @param file 从文件
     * @param masterfilename 主文件名，fileId
     * @param prefix 前缀  如200x300 :最终文件名****200x300.jpg
     * @param ext 后缀 .之后的信息，如jpg
     */
    public UploadSlaveCmd(File file, String masterfilename, String prefix, String ext){
        super();
        this.file = file;
        this.requestCmd = STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE;
        this.body2Len = file.length();
        this.responseCmd = STORAGE_PROTO_CMD_RESP;
        this.responseSize = -1;

        byte[] masterfileNameLenByte = long2buff(masterfilename.length());
        byte[] fileSizeLenByte = long2buff(file.length());
        byte[] prefixByte = prefix.getBytes(charset);
        byte[] fileExtNameByte = getFileExtNameByte(ext);
        int fileExtNameByteLen = fileExtNameByte.length;
        if(fileExtNameByteLen>FDFS_FILE_EXT_NAME_MAX_LEN){
            fileExtNameByteLen = FDFS_FILE_EXT_NAME_MAX_LEN;
        }

        byte[] masterfilenameBytes = masterfilename.getBytes(charset);

        // 2 * FDFS_PROTO_PKG_LEN_SIZE + FDFS_FILE_PREFIX_MAX_LEN + FDFS_FILE_EXT_NAME_MAX_LEN + master_filename_len
        this.body1 = new byte[2 * FDFS_PROTO_PKG_LEN_SIZE + FDFS_FILE_PREFIX_MAX_LEN + FDFS_FILE_EXT_NAME_MAX_LEN
                + masterfilenameBytes.length ];

        Arrays.fill(body1, (byte) 0);

        System.arraycopy(masterfileNameLenByte, 0, body1, 0, masterfileNameLenByte.length);
        System.arraycopy(fileSizeLenByte, 0, body1, FDFS_PROTO_PKG_LEN_SIZE , fileSizeLenByte.length);
        System.arraycopy(prefixByte, 0, body1, 2*FDFS_PROTO_PKG_LEN_SIZE , prefixByte.length);
        System.arraycopy(fileExtNameByte, 0, body1,2 * FDFS_PROTO_PKG_LEN_SIZE + FDFS_FILE_PREFIX_MAX_LEN , fileExtNameByteLen);
        System.arraycopy(masterfilenameBytes, 0, body1,2 * FDFS_PROTO_PKG_LEN_SIZE + FDFS_FILE_PREFIX_MAX_LEN + FDFS_FILE_EXT_NAME_MAX_LEN ,
                masterfilenameBytes.length);
    }

	@Override
	public StorageResult<String> exec(Socket socket) throws IOException {
		InputStream is = new FileInputStream(file);
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
	 * 文件后缀名获取字节码信息
	 * 
	 * @param extName  文件后缀名
	 * @return         文件后缀名的字节码信息
	 */
	private byte[] getFileExtNameByte(String extName) {

		if (extName != null && extName.length() > 0) {
			return extName.getBytes(charset);
		}else{
			return new byte[0];
		}
	}
}
