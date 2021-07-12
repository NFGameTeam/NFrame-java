package cn.yeegro.nframe.components.storage.data;


import cn.yeegro.nframe.components.storage.command.AbstractCmd;

/**
 * group基本信息
 * 
 * @author lixia
 *
 */

public class GroupInfo {

	private String groupName; 		// name of this group
	private long totalMB;			// total disk storage in MB
	private long freeMB; 			// free disk space in MB
	private long trunkFreeMB; 		// trunk free space in MB
	private int storageCount; 		// storage server count
	private int storagePort; 		// storage server port
	private int storageHttpPort; 	// storage server HTTP port
	private int activeCount; 		// active storage server count
	private int currentWriteServer; // current storage server index to upload file
	private int storePathCount; 	// store base path count of each storage server
	private int subdirCountPerPath; // sub dir count per store path
	private int currentTrunkFileId; // current trunk file id
	public final static int BYTE_SIZE = 105;
	

	public GroupInfo(byte[] data,int offset) {
		super();
		groupName = new String(data, offset, AbstractCmd.FDFS_GROUP_NAME_MAX_LEN+1).trim();
		offset +=  AbstractCmd.FDFS_GROUP_NAME_MAX_LEN+1;
		totalMB = AbstractCmd.buff2long(data, offset);
		offset += 8;
		freeMB = AbstractCmd.buff2long(data, offset);
		offset += 8;
		trunkFreeMB = AbstractCmd.buff2long(data, offset);
		offset += 8;
		storageCount = (int) AbstractCmd.buff2long(data, offset);
		offset += 8;
		storagePort = (int) AbstractCmd.buff2long(data, offset);
		offset += 8;
		storageHttpPort = (int) AbstractCmd.buff2long(data, offset);
		offset += 8;
		activeCount = (int) AbstractCmd.buff2long(data, offset);
		offset += 8;
		currentWriteServer = (int) AbstractCmd.buff2long(data, offset);
		offset += 8;
		storePathCount = (int) AbstractCmd.buff2long(data, offset);
		offset += 8;
		subdirCountPerPath = (int) AbstractCmd.buff2long(data, offset);
		offset += 8;
		currentTrunkFileId = (int) AbstractCmd.buff2long(data, offset);
	}

	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public long getTotalMB() {
		return totalMB;
	}
	public void setTotalMB(long totalMB) {
		this.totalMB = totalMB;
	}
	public long getFreeMB() {
		return freeMB;
	}
	public void setFreeMB(long freeMB) {
		this.freeMB = freeMB;
	}
	public long getTrunkFreeMB() {
		return trunkFreeMB;
	}
	public void setTrunkFreeMB(long trunkFreeMB) {
		this.trunkFreeMB = trunkFreeMB;
	}
	public int getStorageCount() {
		return storageCount;
	}
	public void setStorageCount(int storageCount) {
		this.storageCount = storageCount;
	}
	public int getStoragePort() {
		return storagePort;
	}
	public void setStoragePort(int storagePort) {
		this.storagePort = storagePort;
	}
	public int getStorageHttpPort() {
		return storageHttpPort;
	}
	public void setStorageHttpPort(int storageHttpPort) {
		this.storageHttpPort = storageHttpPort;
	}
	public int getActiveCount() {
		return activeCount;
	}
	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}
	public int getCurrentWriteServer() {
		return currentWriteServer;
	}
	public void setCurrentWriteServer(int currentWriteServer) {
		this.currentWriteServer = currentWriteServer;
	}
	public int getStorePathCount() {
		return storePathCount;
	}
	public void setStorePathCount(int storePathCount) {
		this.storePathCount = storePathCount;
	}
	public int getSubdirCountPerPath() {
		return subdirCountPerPath;
	}
	public void setSubdirCountPerPath(int subdirCountPerPath) {
		this.subdirCountPerPath = subdirCountPerPath;
	}
	public int getCurrentTrunkFileId() {
		return currentTrunkFileId;
	}
	public void setCurrentTrunkFileId(int currentTrunkFileId) {
		this.currentTrunkFileId = currentTrunkFileId;
	}
	
	@Override
	public String toString(){
	  StringBuffer sb = new StringBuffer(128);
	  sb.append("groupName:").append(this.groupName).append("\n")
	  .append("totalMB:").append(this.totalMB).append("\n")
	  .append("freeMB:").append(this.freeMB).append("\n")
	  .append("trunkFreeMB:").append(this.trunkFreeMB).append("\n")
	  .append("storageCount:").append(this.storageCount).append("\n")
	  .append("storagePort:").append(this.storagePort).append("\n")
	  .append("storageHttpPort:").append(this.storageHttpPort).append("\n")
	  .append("activeCount:").append(this.activeCount).append("\n")
	  .append("currentWriteServer:").append(this.currentWriteServer).append("\n")
	  .append("storePathCount:").append(this.storePathCount).append("\n")
	  .append("subdirCountPerPath:").append(this.subdirCountPerPath).append("\n")
	  .append("currentTrunkFileId:").append(this.currentTrunkFileId).append("\n");
	  return sb.toString();
	}
	
}
