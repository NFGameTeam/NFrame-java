package cn.yeegro.nframe.components.storage.data;

/**
 * 存储storage的基本信息
 * 
 * @author lixia
 *
 */
public class UploadStorage {
	
	private String address;
	private byte pathIndex;
	public UploadStorage(String address, byte pathIndex) {
		super();
		this.address = address;
		this.pathIndex = pathIndex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public byte getPathIndex() {
		return pathIndex;
	}
	public void setPathIndex(byte pathIndex) {
		this.pathIndex = pathIndex;
	}
	@Override
	public String toString(){
	  StringBuffer sb = new StringBuffer(126);
	  sb.append("address:"+address).append("\n")
	  .append("pathIndex:").append(String.valueOf(pathIndex));
	  return sb.toString();
	}
}
