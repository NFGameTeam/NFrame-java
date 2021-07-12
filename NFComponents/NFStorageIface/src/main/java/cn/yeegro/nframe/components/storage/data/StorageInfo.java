package cn.yeegro.nframe.components.storage.data;


import cn.yeegro.nframe.components.storage.command.AbstractCmd;

import java.util.Date;


/**
 * 存储节点详细信息
 * 
 * @author lixia
 *
 */
public class StorageInfo {
	
	protected byte status;
	protected String id;
	protected String ipAddr;
	protected String domainName; // http domain name
	protected String srcIpAddr;
	protected String version;
	protected Date joinTime; // storage join timestamp (create timestamp)
	protected Date upTime; // storage service started timestamp
	protected long totalMB; // total disk storage in MB
	protected long freeMB; // free disk storage in MB
	
	protected int uploadPriority; // upload priority
	protected int storePathCount; // store base path count of each storage
	protected int subdirCountPerPath;
	protected int currentWritePath; // current write path index
	protected int storagePort;
	protected int storageHttpPort; // storage http server port
	
	protected long totalUploadCount;
	protected long successUploadCount;
	protected long totalAppendCount;
	protected long successAppendCount;
	protected long totalModifyCount;
	protected long successModifyCount;
	protected long totalTruncateCount;
	protected long successTruncateCount;
	protected long totalSetMetaCount;
	protected long successSetMetaCount;
	protected long totalDeleteCount;
	protected long successDeleteCount;
	protected long totalDownloadCount;
	protected long successDownloadCount;
	protected long totalGetMetaCount;
	protected long successGetMetaCount;
	protected long totalCreateLinkCount;
	protected long successCreateLinkCount;
	protected long totalDeleteLinkCount;
	protected long successDeleteLinkCount;
	protected long totalUploadBytes;
	protected long successUploadBytes;
	protected long totalAppendBytes;
	protected long successAppendBytes;
	protected long totalModifyBytes;
	protected long successModifyBytes;
	protected long totalDownloadloadBytes;
	protected long successDownloadloadBytes;
	protected long totalSyncInBytes;
	protected long successSyncInBytes;
	protected long totalSyncOutBytes;
	protected long successSyncOutBytes;
	protected long totalFileOpenCount;
	protected long successFileOpenCount;
	protected long totalFileReadCount;
	protected long successFileReadCount;
	protected long totalFileWriteCount;
	protected long successFileWriteCount;
	
	protected Date lastSourceUpdate;
	protected Date lastSyncUpdate;
	protected Date lastSyncedTimestamp;
	protected Date lastHeartBeatTime;
	protected boolean ifTrunkServer;
	//public final static int BYTE_SIZE = 600;
	public final static int BYTE_SIZE = 612;
	

	public StorageInfo(byte[] data,int offset) {
		status = data[offset];
		offset += 1;
		id = new String(data, offset, AbstractCmd.FDFS_STORAGE_ID_MAX_SIZE).trim();
		offset += AbstractCmd.FDFS_STORAGE_ID_MAX_SIZE;
		ipAddr = new String(data, offset, AbstractCmd.FDFS_IPADDR_SIZE).trim();
		offset +=  AbstractCmd.FDFS_IPADDR_SIZE;
		domainName = new String(data, offset, AbstractCmd.FDFS_DOMAIN_NAME_MAX_SIZE).trim();
		offset +=  AbstractCmd.FDFS_DOMAIN_NAME_MAX_SIZE;
		srcIpAddr = new String(data, offset, AbstractCmd.FDFS_IPADDR_SIZE).trim();
		offset +=  AbstractCmd.FDFS_IPADDR_SIZE;
		version = new String(data, offset, AbstractCmd.FDFS_VERSION_SIZE).trim();
		offset += AbstractCmd.FDFS_VERSION_SIZE;
		joinTime = new Date(AbstractCmd.buff2long(data, offset) * 1000);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		upTime  = new Date(AbstractCmd.buff2long(data, offset) * 1000);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalMB = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		freeMB = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		uploadPriority = (int) AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		storePathCount = (int) AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		subdirCountPerPath = (int) AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		currentWritePath = (int) AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		storagePort = (int) AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		storageHttpPort = (int) AbstractCmd.buff2long(data, offset);
		
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalUploadCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successUploadCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalAppendCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successAppendCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalModifyCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successModifyCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalTruncateCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successTruncateCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalSetMetaCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successSetMetaCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalDeleteCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successDeleteCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalDownloadCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successDownloadCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalGetMetaCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successGetMetaCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalCreateLinkCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successCreateLinkCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalDeleteLinkCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successDeleteLinkCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalUploadBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successUploadBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalAppendBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successAppendBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalModifyBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successModifyBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalDownloadloadBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successDownloadloadBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalSyncInBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successSyncInBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalSyncOutBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successSyncOutBytes = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalFileOpenCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successFileOpenCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalFileReadCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successFileReadCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		totalFileWriteCount = AbstractCmd.buff2long(data, offset);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		successFileWriteCount = AbstractCmd.buff2long(data, offset);
		
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		lastSourceUpdate  = new Date(AbstractCmd.buff2long(data, offset) * 1000);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		lastSyncUpdate  = new Date(AbstractCmd.buff2long(data, offset) * 1000);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		lastSyncedTimestamp  = new Date(AbstractCmd.buff2long(data, offset) * 1000);
		offset += AbstractCmd.FDFS_PROTO_PKG_LEN_SIZE;
		lastHeartBeatTime  = new Date(AbstractCmd.buff2long(data, offset) * 1000);
		
		ifTrunkServer = (data[offset] != 0);
	}
	
	
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getSrcIpAddr() {
		return srcIpAddr;
	}
	public void setSrcIpAddr(String srcIpAddr) {
		this.srcIpAddr = srcIpAddr;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public int getUploadPriority() {
		return uploadPriority;
	}
	public void setUploadPriority(int uploadPriority) {
		this.uploadPriority = uploadPriority;
	}
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
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
	public int getCurrentWritePath() {
		return currentWritePath;
	}
	public void setCurrentWritePath(int currentWritePath) {
		this.currentWritePath = currentWritePath;
	}
	public long getTotalUploadCount() {
		return totalUploadCount;
	}
	public void setTotalUploadCount(long totalUploadCount) {
		this.totalUploadCount = totalUploadCount;
	}
	public long getSuccessUploadCount() {
		return successUploadCount;
	}
	public void setSuccessUploadCount(long successUploadCount) {
		this.successUploadCount = successUploadCount;
	}
	public long getTotalAppendCount() {
		return totalAppendCount;
	}
	public void setTotalAppendCount(long totalAppendCount) {
		this.totalAppendCount = totalAppendCount;
	}
	public long getSuccessAppendCount() {
		return successAppendCount;
	}
	public void setSuccessAppendCount(long successAppendCount) {
		this.successAppendCount = successAppendCount;
	}
	public long getTotalModifyCount() {
		return totalModifyCount;
	}
	public void setTotalModifyCount(long totalModifyCount) {
		this.totalModifyCount = totalModifyCount;
	}
	public long getSuccessModifyCount() {
		return successModifyCount;
	}
	public void setSuccessModifyCount(long successModifyCount) {
		this.successModifyCount = successModifyCount;
	}
	public long getTotalTruncateCount() {
		return totalTruncateCount;
	}
	public void setTotalTruncateCount(long totalTruncateCount) {
		this.totalTruncateCount = totalTruncateCount;
	}
	public long getSuccessTruncateCount() {
		return successTruncateCount;
	}
	public void setSuccessTruncateCount(long successTruncateCount) {
		this.successTruncateCount = successTruncateCount;
	}
	public long getTotalSetMetaCount() {
		return totalSetMetaCount;
	}
	public void setTotalSetMetaCount(long totalSetMetaCount) {
		this.totalSetMetaCount = totalSetMetaCount;
	}
	public long getSuccessSetMetaCount() {
		return successSetMetaCount;
	}
	public void setSuccessSetMetaCount(long successSetMetaCount) {
		this.successSetMetaCount = successSetMetaCount;
	}
	public long getTotalDeleteCount() {
		return totalDeleteCount;
	}
	public void setTotalDeleteCount(long totalDeleteCount) {
		this.totalDeleteCount = totalDeleteCount;
	}
	public long getSuccessDeleteCount() {
		return successDeleteCount;
	}
	public void setSuccessDeleteCount(long successDeleteCount) {
		this.successDeleteCount = successDeleteCount;
	}
	public long getTotalDownloadCount() {
		return totalDownloadCount;
	}
	public void setTotalDownloadCount(long totalDownloadCount) {
		this.totalDownloadCount = totalDownloadCount;
	}
	public long getSuccessDownloadCount() {
		return successDownloadCount;
	}
	public void setSuccessDownloadCount(long successDownloadCount) {
		this.successDownloadCount = successDownloadCount;
	}
	public long getTotalGetMetaCount() {
		return totalGetMetaCount;
	}
	public void setTotalGetMetaCount(long totalGetMetaCount) {
		this.totalGetMetaCount = totalGetMetaCount;
	}
	public long getSuccessGetMetaCount() {
		return successGetMetaCount;
	}
	public void setSuccessGetMetaCount(long successGetMetaCount) {
		this.successGetMetaCount = successGetMetaCount;
	}
	public long getTotalCreateLinkCount() {
		return totalCreateLinkCount;
	}
	public void setTotalCreateLinkCount(long totalCreateLinkCount) {
		this.totalCreateLinkCount = totalCreateLinkCount;
	}
	public long getSuccessCreateLinkCount() {
		return successCreateLinkCount;
	}
	public void setSuccessCreateLinkCount(long successCreateLinkCount) {
		this.successCreateLinkCount = successCreateLinkCount;
	}
	public long getTotalDeleteLinkCount() {
		return totalDeleteLinkCount;
	}
	public void setTotalDeleteLinkCount(long totalDeleteLinkCount) {
		this.totalDeleteLinkCount = totalDeleteLinkCount;
	}
	public long getSuccessDeleteLinkCount() {
		return successDeleteLinkCount;
	}
	public void setSuccessDeleteLinkCount(long successDeleteLinkCount) {
		this.successDeleteLinkCount = successDeleteLinkCount;
	}
	public long getTotalUploadBytes() {
		return totalUploadBytes;
	}
	public void setTotalUploadBytes(long totalUploadBytes) {
		this.totalUploadBytes = totalUploadBytes;
	}
	public long getSuccessUploadBytes() {
		return successUploadBytes;
	}
	public void setSuccessUploadBytes(long successUploadBytes) {
		this.successUploadBytes = successUploadBytes;
	}
	public long getTotalAppendBytes() {
		return totalAppendBytes;
	}
	public void setTotalAppendBytes(long totalAppendBytes) {
		this.totalAppendBytes = totalAppendBytes;
	}
	public long getSuccessAppendBytes() {
		return successAppendBytes;
	}
	public void setSuccessAppendBytes(long successAppendBytes) {
		this.successAppendBytes = successAppendBytes;
	}
	public long getTotalModifyBytes() {
		return totalModifyBytes;
	}
	public void setTotalModifyBytes(long totalModifyBytes) {
		this.totalModifyBytes = totalModifyBytes;
	}
	public long getSuccessModifyBytes() {
		return successModifyBytes;
	}
	public void setSuccessModifyBytes(long successModifyBytes) {
		this.successModifyBytes = successModifyBytes;
	}
	public long getTotalDownloadloadBytes() {
		return totalDownloadloadBytes;
	}
	public void setTotalDownloadloadBytes(long totalDownloadloadBytes) {
		this.totalDownloadloadBytes = totalDownloadloadBytes;
	}
	public long getSuccessDownloadloadBytes() {
		return successDownloadloadBytes;
	}
	public void setSuccessDownloadloadBytes(long successDownloadloadBytes) {
		this.successDownloadloadBytes = successDownloadloadBytes;
	}
	public long getTotalSyncInBytes() {
		return totalSyncInBytes;
	}
	public void setTotalSyncInBytes(long totalSyncInBytes) {
		this.totalSyncInBytes = totalSyncInBytes;
	}
	public long getSuccessSyncInBytes() {
		return successSyncInBytes;
	}
	public void setSuccessSyncInBytes(long successSyncInBytes) {
		this.successSyncInBytes = successSyncInBytes;
	}
	public long getTotalSyncOutBytes() {
		return totalSyncOutBytes;
	}
	public void setTotalSyncOutBytes(long totalSyncOutBytes) {
		this.totalSyncOutBytes = totalSyncOutBytes;
	}
	public long getSuccessSyncOutBytes() {
		return successSyncOutBytes;
	}
	public void setSuccessSyncOutBytes(long successSyncOutBytes) {
		this.successSyncOutBytes = successSyncOutBytes;
	}
	public long getTotalFileOpenCount() {
		return totalFileOpenCount;
	}
	public void setTotalFileOpenCount(long totalFileOpenCount) {
		this.totalFileOpenCount = totalFileOpenCount;
	}
	public long getSuccessFileOpenCount() {
		return successFileOpenCount;
	}
	public void setSuccessFileOpenCount(long successFileOpenCount) {
		this.successFileOpenCount = successFileOpenCount;
	}
	public long getTotalFileReadCount() {
		return totalFileReadCount;
	}
	public void setTotalFileReadCount(long totalFileReadCount) {
		this.totalFileReadCount = totalFileReadCount;
	}
	public long getSuccessFileReadCount() {
		return successFileReadCount;
	}
	public void setSuccessFileReadCount(long successFileReadCount) {
		this.successFileReadCount = successFileReadCount;
	}
	public long getTotalFileWriteCount() {
		return totalFileWriteCount;
	}
	public void setTotalFileWriteCount(long totalFileWriteCount) {
		this.totalFileWriteCount = totalFileWriteCount;
	}
	public long getSuccessFileWriteCount() {
		return successFileWriteCount;
	}
	public void setSuccessFileWriteCount(long successFileWriteCount) {
		this.successFileWriteCount = successFileWriteCount;
	}
	public Date getLastSourceUpdate() {
		return lastSourceUpdate;
	}
	public void setLastSourceUpdate(Date lastSourceUpdate) {
		this.lastSourceUpdate = lastSourceUpdate;
	}
	public Date getLastSyncUpdate() {
		return lastSyncUpdate;
	}
	public void setLastSyncUpdate(Date lastSyncUpdate) {
		this.lastSyncUpdate = lastSyncUpdate;
	}
	public Date getLastSyncedTimestamp() {
		return lastSyncedTimestamp;
	}
	public void setLastSyncedTimestamp(Date lastSyncedTimestamp) {
		this.lastSyncedTimestamp = lastSyncedTimestamp;
	}
	public Date getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}
	public void setLastHeartBeatTime(Date lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}
	public boolean isIfTrunkServer() {
		return ifTrunkServer;
	}
	public void setIfTrunkServer(boolean ifTrunkServer) {
		this.ifTrunkServer = ifTrunkServer;
	}
	
	@Override
  public String toString(){
    StringBuffer sb = new StringBuffer(128*4);
    sb.append("status:").append(this.status).append("\n")
    .append("id:").append(this.id).append("\n")
    .append("freeMB:").append(this.freeMB).append("\n")
    .append("ipAddr:").append(this.ipAddr).append("\n")
    .append("domainName:").append(this.domainName).append("\n")
    .append("srcIpAddr:").append(this.srcIpAddr).append("\n")
    .append("version:").append(this.version).append("\n")
    .append("joinTime:").append(this.joinTime).append("\n")
    .append("upTime:").append(this.upTime).append("\n")
    .append("totalMB:").append(this.totalMB).append("\n")
    .append("freeMB:").append(this.freeMB).append("\n")
    .append("uploadPriority:").append(this.uploadPriority).append("\n")
    .append("storePathCount:").append(this.storePathCount).append("\n")
    .append("subdirCountPerPath:").append(this.subdirCountPerPath).append("\n")
    .append("currentWritePath:").append(this.currentWritePath).append("\n")
    .append("storagePort:").append(this.storagePort).append("\n")
    .append("storageHttpPort:").append(this.storageHttpPort).append("\n")
    .append("totalUploadCount:").append(this.totalUploadCount).append("\n")
    .append("successUploadCount:").append(this.successUploadCount).append("\n")
    .append("totalAppendCount:").append(this.totalAppendCount).append("\n")
    .append("successAppendCount:").append(this.successAppendCount).append("\n")
    .append("totalModifyCount:").append(this.totalModifyCount).append("\n")
    .append("successModifyCount:").append(this.successModifyCount).append("\n")
    .append("totalTruncateCount:").append(this.totalTruncateCount).append("\n")
    .append("successTruncateCount:").append(this.successTruncateCount).append("\n")
    .append("totalSetMetaCount:").append(this.totalSetMetaCount).append("\n")
    .append("successSetMetaCount:").append(this.successSetMetaCount).append("\n")
    .append("totalDeleteCount:").append(this.totalDeleteCount).append("\n")
    .append("successDeleteCount:").append(this.successDeleteCount).append("\n")
    .append("totalDownloadCount:").append(this.totalDownloadCount).append("\n")
    .append("successDownloadCount:").append(this.successDownloadCount).append("\n")
    .append("totalGetMetaCount:").append(this.successGetMetaCount).append("\n")
    .append("totalCreateLinkCount:").append(this.totalCreateLinkCount).append("\n")
    .append("successCreateLinkCount:").append(this.successCreateLinkCount).append("\n")
    .append("successDeleteLinkCount:").append(this.totalDeleteLinkCount).append("\n")
    .append("totalUploadBytes:").append(this.totalUploadBytes).append("\n")
    .append("successUploadBytes:").append(this.successUploadBytes).append("\n")
    .append("totalAppendBytes:").append(this.totalAppendBytes).append("\n")
    .append("successAppendBytes:").append(this.successAppendBytes).append("\n")
    .append("totalModifyBytes:").append(this.totalModifyBytes).append("\n")
    .append("successModifyBytes:").append(this.successModifyBytes).append("\n")
    .append("totalDownloadloadBytes:").append(this.totalDownloadloadBytes).append("\n")
    .append("successDownloadloadBytes:").append(this.successDownloadloadBytes).append("\n")
    .append("totalSyncInBytes:").append(this.totalSyncInBytes).append("\n")
    .append("successSyncInBytes:").append(this.successSyncInBytes).append("\n")
    .append("totalFileOpenCount:").append(this.totalFileOpenCount).append("\n")
    .append("successFileOpenCount:").append(this.successFileOpenCount).append("\n")
    .append("successFileReadCount:").append(this.successFileReadCount).append("\n")
    .append("totalFileWriteCount:").append(this.totalFileWriteCount).append("\n")
    .append("successFileWriteCount:").append(this.successFileWriteCount).append("\n")
    .append("lastSyncUpdate:").append(this.lastSyncUpdate).append("\n")
    .append("lastSyncedTimestamp:").append(this.lastSyncedTimestamp).append("\n")
    .append("lastHeartBeatTime:").append(this.lastHeartBeatTime).append("\n")
    .append("ifTrunkServer:").append(this.ifTrunkServer).append("\n");
    return sb.toString();
  }
}
