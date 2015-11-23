/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 记录管理器
 */
public interface NFIRecordManager {
	/**
	 * 添加一个新记录
	 * @param name 
	 * @param maxRow 最大行索引
	 * @param types 列类型
	 * @return 返回新的记录，如果名字已经存在，则返回null
	 */
	public NFIRecord addRecord(String recordName, int maxRow, NFIDataList types);

	/**
	 * 获取指定名字的记录
	 * @param name
	 * @return 如果名字不存在，返回null
	 */
	public NFIRecord getRecord(String recordName);
	
	/**
	 * 设置记录的字段值
	 * @param recordName
	 * @param row
	 * @param column
	 * @param var
	 * @return 是否设置成功，如果指定的名字不存在，返回false
	 */
	public boolean setRecord(String recordName, int row, int column, long var);
	public boolean setRecord(String recordName, int row, int column, double var);
	public boolean setRecord(String recordName, int row, int column, String var);
	public boolean setRecord(String recordName, int row, int column, NFGUID var);

	/**
	 * 设置记录的字段值
	 * @param recordName
	 * @param row
	 * @param colTag
	 * @param var
	 * @return 是否设置成功，如果指定的名字不存在，返回false
	 */
	public boolean setRecord(String recordName, int row, String colTag, long var);
	public boolean setRecord(String recordName, int row, String colTag, double var);
	public boolean setRecord(String recordName, int row, String colTag, String var);
	public boolean setRecord(String recordName, int row, String colTag, NFGUID var);
	
	/**
	 * 获取指定行列字段的值
	 * @param recordName
	 * @param row
	 * @param column
	 * @return 如果不存在返回NFIData.XXX_NIL
	 */
	public long getRecordInt(String recordName, int row, int column);
	public double getRecordFloat(String recordName, int row, int column);
	public String getRecordString(String recordName, int row, int column);
	public NFGUID getRecordObject(String recordName, int row, int column);
	
	/**
	 * 获取指定行列字段的值
	 * @param recordName
	 * @param row
	 * @param colTag
	 * @return 如果不存在返回NFIData.XXX_NIL
	 */
	public long getRecordInt(String recordName, int row, String colTag);
	public double getRecordFloat(String recordName, int row, String colTag);
	public String getRecordString(String recordName, int row, String colTag);
	public NFGUID getRecordObject(String recordName, int row, String colTag);
	
	/**
	 * 添加记录回调
	 * @param recordName
	 * @param cb
	 * @return
	 */
	public boolean addCallback(String recordName, NFIRecordHandler cb);
}
