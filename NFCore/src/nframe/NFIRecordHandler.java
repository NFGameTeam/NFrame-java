/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 记录回调接口
 */
public interface NFIRecordHandler {
	public void handle(NFGUID guid, String recordName, NFIRecord.Optype optype, int row, int column, NFIDataList oldVar, NFIDataList newVar);
}
