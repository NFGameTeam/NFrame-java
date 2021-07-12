package cn.yeegro.nframe.components.storage.command;

import cn.yeegro.nframe.common.web.StorageResult;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 全局常量设置<br>
 * 命令的字节码长度，各种限制length定义，如：命令长度，ip长度，port长度等<br>
 * 命令参数定义：如上传，下载，查询storage，group等<br>
 * 
 * @author lixia
 *
 * @param <T>
 */
public interface Command<T> {

  /**
   * 执行命令方法
   * 1.根据对文件或group storage基本信息的操作查看，通过tracker与服务器通信获取storage信息，子类：
   * QueryUploadCmd
   * QueryUpdateCmd
   * QueryDownloadCmd
   * GroupInfoCmd
   * StorageInfoCmd
   * 
   * 2.storage对文件和属性的操作与服务器通信，子类：
   * UploadSlaveCmd
   * UploadStreamCmd
   * UploadCmd
   * DeleteCmd
   * SetMetaDataCmd
   * GetMetaDataCmd
   * 
   * @param socket
   * @return
   * @throws IOException
   */
	public StorageResult<T> exec(Socket socket) throws IOException;
	
	
	public static final byte FDFS_PROTO_CMD_QUIT = 82;
	public static final byte TRACKER_PROTO_CMD_SERVER_LIST_GROUP = 91;
	public static final byte TRACKER_PROTO_CMD_SERVER_LIST_STORAGE = 92;
	public static final byte TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE = 93;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE = 101;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE = 102;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE = 103;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE = 104;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL = 105;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL = 106;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL = 107;
	public static final byte TRACKER_PROTO_CMD_RESP = 100;
	public static final byte FDFS_PROTO_CMD_ACTIVE_TEST = 111;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_FILE = 11;
	public static final byte STORAGE_PROTO_CMD_DELETE_FILE = 12;
	public static final byte STORAGE_PROTO_CMD_SET_METADATA = 13;
	public static final byte STORAGE_PROTO_CMD_DOWNLOAD_FILE = 14;
	public static final byte STORAGE_PROTO_CMD_GET_METADATA = 15;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE = 21;
	public static final byte STORAGE_PROTO_CMD_QUERY_FILE_INFO = 22;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE = 23; // create appender file
	public static final byte STORAGE_PROTO_CMD_APPEND_FILE = 24; // append file
	public static final byte STORAGE_PROTO_CMD_MODIFY_FILE = 34; // modify appender file
	public static final byte STORAGE_PROTO_CMD_TRUNCATE_FILE = 36; // truncate appender file
	public static final byte STORAGE_PROTO_CMD_RESP = TRACKER_PROTO_CMD_RESP;
	
	public static final byte FDFS_STORAGE_STATUS_INIT = 0;
	public static final byte FDFS_STORAGE_STATUS_WAIT_SYNC = 1;
	public static final byte FDFS_STORAGE_STATUS_SYNCING = 2;
	public static final byte FDFS_STORAGE_STATUS_IP_CHANGED = 3;
	public static final byte FDFS_STORAGE_STATUS_DELETED = 4;
	public static final byte FDFS_STORAGE_STATUS_OFFLINE = 5;
	public static final byte FDFS_STORAGE_STATUS_ONLINE = 6;
	public static final byte FDFS_STORAGE_STATUS_ACTIVE = 7;
	public static final byte FDFS_STORAGE_STATUS_NONE = 99;

	/**
	 * for overwrite all old metadata
	 */
	public static final byte STORAGE_SET_METADATA_FLAG_OVERWRITE = 'O';

	/**
	 * for replace, insert when the meta item not exist, otherwise update it
	 */
	public static final byte STORAGE_SET_METADATA_FLAG_MERGE = 'M';

	public static final int FDFS_PROTO_PKG_LEN_SIZE = 8;
	public static final int FDFS_PROTO_CMD_SIZE = 1;
	public static final int FDFS_GROUP_NAME_MAX_LEN = 16;
	public static final int FDFS_IPADDR_SIZE = 16;
	public static final int FDFS_DOMAIN_NAME_MAX_SIZE = 128;
	public static final int FDFS_VERSION_SIZE = 6;
	public static final int FDFS_STORAGE_ID_MAX_SIZE = 16;

	public static final String FDFS_RECORD_SEPERATOR = "\u0001";
	public static final String FDFS_FIELD_SEPERATOR = "\u0002";

	public static final int TRACKER_QUERY_STORAGE_FETCH_BODY_LEN = FDFS_GROUP_NAME_MAX_LEN + FDFS_IPADDR_SIZE - 1 + FDFS_PROTO_PKG_LEN_SIZE;
	public static final int TRACKER_QUERY_STORAGE_STORE_BODY_LEN = FDFS_GROUP_NAME_MAX_LEN + FDFS_IPADDR_SIZE + FDFS_PROTO_PKG_LEN_SIZE;

	public static final int PROTO_HEADER_CMD_INDEX = FDFS_PROTO_PKG_LEN_SIZE;
	public static final int PROTO_HEADER_STATUS_INDEX = FDFS_PROTO_PKG_LEN_SIZE + 1;

	public static final byte FDFS_FILE_EXT_NAME_MAX_LEN = 6;
	public static final byte FDFS_FILE_PREFIX_MAX_LEN = 16;
	public static final byte FDFS_FILE_PATH_LEN = 10;
	public static final byte FDFS_FILENAME_BASE64_LENGTH = 27;
	public static final byte FDFS_TRUNK_FILE_INFO_LEN = 16;

	public static final byte ERR_NO_ENOENT = 2;
	public static final byte ERR_NO_EIO = 5;
	public static final byte ERR_NO_EBUSY = 16;
	public static final byte ERR_NO_EINVAL = 22;
	public static final byte ERR_NO_ENOSPC = 28;
	public static final byte ECONNREFUSED = 61;
	public static final byte ERR_NO_EALREADY = 114;

	public static final long INFINITE_FILE_SIZE = 256 * 1024L * 1024 * 1024	* 1024 * 1024L;
	public static final long APPENDER_FILE_SIZE = INFINITE_FILE_SIZE;
	public static final long TRUNK_FILE_MARK_SIZE = 512 * 1024L * 1024 * 1024 * 1024 * 1024L;
	public static final long NORMAL_LOGIC_FILENAME_LENGTH = FDFS_FILE_PATH_LEN	+ FDFS_FILENAME_BASE64_LENGTH + FDFS_FILE_EXT_NAME_MAX_LEN + 1;
	public static final long TRUNK_LOGIC_FILENAME_LENGTH = NORMAL_LOGIC_FILENAME_LENGTH	+ FDFS_TRUNK_FILE_INFO_LEN;
	
	public static final int SUCCESS_CODE = 0;
	public Charset charset = Charset.forName("UTF-8");
	
}
