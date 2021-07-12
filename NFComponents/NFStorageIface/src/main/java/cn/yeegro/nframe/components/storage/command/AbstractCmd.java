package cn.yeegro.nframe.components.storage.command;


import cn.yeegro.nframe.components.storage.utils.IOCloseUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;


public abstract class AbstractCmd<T> implements Command<T> {
	/**
	 * 请求命令参数，在AbstractCmd子类初始化时进行设定
	 */
	protected byte requestCmd;
	/**
	 * 响应命令参数（请求后应获取的响应参数，在获取socket响应流后进行检查）
	 */
	protected byte responseCmd;
	/*
  STORAGE_PROTO_CMD_UPLOAD_FILE = 11; 上传
  STORAGE_PROTO_CMD_DELETE_FILE = 12; 删除
  STORAGE_PROTO_CMD_SET_METADATA = 13;  设置元数据
  STORAGE_PROTO_CMD_DOWNLOAD_FILE = 14; 下载文件
  STORAGE_PROTO_CMD_GET_METADATA = 15;  获取元数据
  STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE = 21; 上传从文件
  STORAGE_PROTO_CMD_QUERY_FILE_INFO = 22;   
  STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE = 23;  追加文件
  STORAGE_PROTO_CMD_APPEND_FILE = 24; 
  STORAGE_PROTO_CMD_MODIFY_FILE = 34; 
  STORAGE_PROTO_CMD_TRUNCATE_FILE = 36; 
  STORAGE_PROTO_CMD_RESP = TRACKER_PROTO_CMD_RESP;  返回结果命令与读取的socket字节码中的命令匹配
  */
	
	protected long responseSize;
	/**
	 * 初始化command时的命令封装（如上传长度15：storePathIndex字节码+文件长度字节码+后缀名字节码）
	 * 子类的header为长度为10的字节码信息，body信息可以为空
	 */
	protected byte[] body1;
	/**
	 * 上传文件的长度
	 */
	protected long body2Len = 0l;
	
	/**
	 * 请求输出流
	 * 将封装的基本命令和信息发送（header字节码）
	 * 
	 * @param socketOut  输出流对象
	 * @throws IOException
	 */
	protected void request(OutputStream socketOut)throws IOException {
		socketOut.write(getRequestHeaderAndBody1());		
	}
	
	/**
   * 请求输出流
   * 1.将封装的基本命令和信息发送（header字节码）
   * 2.将文件输入流写出发送（InputStream is）
   * 
   * @param socketOut  socket输出流对象
   * @param is          文件输入流
   * @throws IOException
   */
	protected void request(OutputStream socketOut,InputStream is)throws IOException {
	  //发送命令和基本信息
		request(socketOut);
		//将文件字节码写入socket请求
		int readBytes;
		byte[] buff = new byte[256 * 1024];
		while ((readBytes = is.read(buff)) >= 0) {
			if (readBytes == 0) {
				continue;
			}
			socketOut.write(buff, 0, readBytes);
		}
		//关闭字节流
		IOCloseUtil.close(is);
	}
	/**
	 * 拼接发送请求的字节码信息header
	 * header字节码：0-7：存储body1（字节码长度）+body2Len（上传文件的长度）的字节码信息（在上传时body2Len才会有值）
	 *             8：请求命令字节码（如上传11，删除12）
	 *             9：存储状态信息0（0为成功）
	 *             10-结尾：body1的字节码信息
	 *             
	 * @return 请求的字节码信息
	 */
	protected byte[] getRequestHeaderAndBody1() {
		if(body1==null){
			body1 = new byte[0];
		}
		//设定请求的参数
		byte[] header = new byte[FDFS_PROTO_PKG_LEN_SIZE + 2 + body1.length];
		Arrays.fill(header, (byte) 0);
		byte[] hex_len = long2buff(body1.length+body2Len);
		System.arraycopy(hex_len, 0, header, 0, hex_len.length);
		System.arraycopy(body1, 0, header, FDFS_PROTO_PKG_LEN_SIZE + 2, body1.length);
		//设定请求命令
		header[PROTO_HEADER_CMD_INDEX] = requestCmd;
		//设定请求状态
		header[PROTO_HEADER_STATUS_INDEX] = (byte) 0;
		
		//StringBuffer sb = new StringBuffer(128);
		//buff2long(header,0);
		//sb.append(new String(header,charset).toString());
		Logger.getLogger(new StringBuilder(64).append("The FastDfs command is ---- ").append(requestCmd).toString());
		
		return header;
	}
	
	/**
	 * 读取socket的返回的结果信息，并对结果信息进行检查，封装结果信息至Response对象
	 * 
	 * @param socketIn socket返回结果
	 * @return         返回结果的Response对象
	 * @throws IOException
	 */
	protected Response response(InputStream socketIn)throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		//读取返回信息，并返回状态code
		int code = response(socketIn,os);
		//将code和返回信息封装Response对象
		return new Response(code, os.toByteArray());
		
	}
	
	/**
	 * 
	 * 读取socket的返回的结果信息，并对结果信息进行检查，将结果信息写入到OutputStream os
	 * 
	 * bytes（socket返回的结果字节码10位）：
	 *         8：responseCmd响应命令参数（应与发送请求时赋值的responseCmd一致）
	 *         9：返回结果状态（0为成功）
	 * 
	 * @param socketIn     socket返回结果InputStream对象
	 * @param os           ByteArrayOutputStream对象
	 * @return             返回结果code（成功返回0）
	 * @throws IOException
	 */
  protected int response(InputStream socketIn,OutputStream os) throws IOException {
		byte[] header = new byte[FDFS_PROTO_PKG_LEN_SIZE + 2];
		
		int bytes = socketIn.read(header);
		
		//判定返回的字节码是否是10位
		if (bytes != header.length) {
			throw new IOException("recv package size " + bytes + " != "	+ header.length);
		}
		
		//判断响应回复的命令是否与发送时设定的命令一致
		if (header[PROTO_HEADER_CMD_INDEX] != responseCmd) {
			throw new IOException("recv cmd: " + header[PROTO_HEADER_CMD_INDEX]	+ " is not correct, expect cmd: " + responseCmd);
		}

		//判定返回状态是否成功，不成功则返回状态信息
		if (header[PROTO_HEADER_STATUS_INDEX] != SUCCESS_CODE) {
			return header[PROTO_HEADER_STATUS_INDEX];
		}

		//获取响应的long
		long respSize = buff2long(header, 0);
		if (respSize < 0) {
			throw new IOException("recv body length: " + respSize + " < 0!");
		}
		//并判定是否与请求时设定的响应size一致（如果请求时设定的参数为正整数时）
		if (responseSize >= 0 && respSize != responseSize) {
			throw new IOException("recv body length: " + respSize + " is not correct, expect length: " + responseSize);
		}
		
		//读取信息并写入OutputStream
		byte[] buff = new byte[2 * 1024];
		int totalBytes = 0;
		int remainBytes = (int) respSize;

		while (totalBytes < respSize) {
			int len = remainBytes;
			if(len>buff.length){
				len = buff.length;
			}
			
			if ((bytes = socketIn.read(buff, 0, len)) < 0) {
				break;
			}
			os.write(buff, 0, bytes);
			totalBytes += bytes;
			remainBytes -= bytes;
		}

		if (totalBytes != respSize) {
			throw new IOException("recv package size " + totalBytes + " != "+ respSize);
		}
		os.close();
		return SUCCESS_CODE;
	}

  /**
   * 元数据map转字符串
   * @param metaData
   * @return
   */
  protected String metaDataToStr(Map<String,String> metaData){
    StringBuffer sb = new StringBuffer();
    for(String key:metaData.keySet()){
        sb.append(FDFS_RECORD_SEPERATOR);
        sb.append(key);
        sb.append(FDFS_FIELD_SEPERATOR);
        sb.append(metaData.get(key));
    }
    
    return sb.toString().substring(FDFS_RECORD_SEPERATOR.length());
  }
	/**
	 * long转字节码
	 * @param n
	 * @return
	 */
	public static byte[] long2buff(long n) {
		byte[] bs;

		bs = new byte[8];
		bs[0] = (byte) ((n >> 56) & 0xFF);
		bs[1] = (byte) ((n >> 48) & 0xFF);
		bs[2] = (byte) ((n >> 40) & 0xFF);
		bs[3] = (byte) ((n >> 32) & 0xFF);
		bs[4] = (byte) ((n >> 24) & 0xFF);
		bs[5] = (byte) ((n >> 16) & 0xFF);
		bs[6] = (byte) ((n >> 8) & 0xFF);
		bs[7] = (byte) (n & 0xFF);

		return bs;
	}
	/**
	 * 字节码转long
	 * @param bs     字节码
	 * @param offset 偏移量（从当前下表进行转码）
	 * @return
	 */
	public static long buff2long(byte[] bs, int offset) {
		return (((long) (bs[offset] >= 0 ? bs[offset] : 256 + bs[offset])) << 56)
				| (((long) (bs[offset + 1] >= 0 ? bs[offset + 1]
						: 256 + bs[offset + 1])) << 48)
				| (((long) (bs[offset + 2] >= 0 ? bs[offset + 2]
						: 256 + bs[offset + 2])) << 40)
				| (((long) (bs[offset + 3] >= 0 ? bs[offset + 3]
						: 256 + bs[offset + 3])) << 32)
				| (((long) (bs[offset + 4] >= 0 ? bs[offset + 4]
						: 256 + bs[offset + 4])) << 24)
				| (((long) (bs[offset + 5] >= 0 ? bs[offset + 5]
						: 256 + bs[offset + 5])) << 16)
				| (((long) (bs[offset + 6] >= 0 ? bs[offset + 6]
						: 256 + bs[offset + 6])) << 8)
				| ((long) (bs[offset + 7] >= 0 ? bs[offset + 7]
						: 256 + bs[offset + 7]));
	}

  /**
   * 传入带group的fileid,返回group和filename的二元数组
   * @param file_id fileid like g1/M00/00/00/abc.jpg
   * @return string[2] string[0] = g1, string[1]=M00/00/00/abc.jpg
   */
  public static String[] splitFileId(String file_id) {
    int pos = file_id.indexOf("/");
    if ((pos <= 0) || (pos == file_id.length() - 1)) {
        return null;
    }

    String[] results = new String[2];
    results[0] = file_id.substring(0, pos); //group name
    results[1] = file_id.substring(pos + 1); //file name
    return results;
  }
	/**
	 * 响应的数据封装对象
	 * @author lixia
	 */
	protected class Response {
		/**
		 * 返回code值 0为OK
		 */
		private int code;
		/**
		 * 数据字节码
		 */
		private byte[] data;
		
		public Response(int code) {
			super();
			this.code = code;
		}
		public Response(int code, byte[] data) {
			super();
			this.code = code;
			this.data = data;
		}
		public boolean isSuccess(){
			return code == SUCCESS_CODE;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public byte[] getData() {
			return data;
		}
		public void setData(byte[] data) {
			this.data = data;
		}
	}
}
