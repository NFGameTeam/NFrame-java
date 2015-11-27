/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 记录回调接口
 */
public interface NFIRecordHandler {
	public void handle(NFGUID guid, String recordName, NFIRecord.Optype optype, int arg1, int arg2, NFIDataList oldVar, NFIDataList newVar);
}
