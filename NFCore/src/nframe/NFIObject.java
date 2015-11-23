/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 基础对象抽象类
 */
public abstract class NFIObject extends NFBehaviour {
	/**
	 * 查看name的属性是否存在
	 * @param name
	 * @return 如果name的属性存在返回true；反之返回false
	 */
	public abstract boolean hasProperty(String name);
	
	/**
	 * 返回属性值
	 * @param name
	 * @return 如果不存在，则返回默认值
	 */
	public abstract long getPropertyInt(String name);
	public abstract double getPropertyFloat(String name);
	public abstract String getPropertyString(String name);
	public abstract NFGUID getPropertyObject(String name);
	
	/**
	 * 设置指定name的属性值，如果旧的值和新值不同，则触发属性回调
	 * @param name
	 * @param var
	 * @return 如果name的属性存在，返回true；反之返回false
	 */
	public abstract boolean setProperty(String name, long var);
	public abstract boolean setProperty(String name, double var);
	public abstract boolean setProperty(String name, String var);
	public abstract boolean setProperty(String name, NFGUID var);
	
	/**
	 * 设置指定name的属性值，不会触发属性回调
	 * @param name
	 * @param var
	 * @return 如果name的属性存在，返回true；反之返回false
	 */
	public abstract boolean setProperty(String name, NFIData var);
	
	/**
	 * 获取属性
	 * @param name
	 * @return 如果name不存在返回null；否则返回获取的属性
	 */
	public abstract NFIProperty getProperty(String name);
	
	/**
	 * 获取属性管理器
	 * @return
	 */
	public abstract NFIPropertyManager getPropertyManager();
	
	/**
	 * 是否存在指定名字的记录
	 * @param recordName
	 * @return
	 */
	public abstract boolean hasRecord(String recordName);
	
	/**
	 * 返回记录字段值
	 * @param recordName
	 * @param row
	 * @param column
	 * @return 如果不存在，则返回默认值
	 */
	public abstract long getRecordInt(String recordName, int row, int column);
	public abstract double getRecordFloat(String recordName, int row, int column);
	public abstract String getRecordString(String recordName, int row, int column);
	public abstract NFGUID getRecordObject(String recordName, int row, int column);
	
	/**
	 * 返回记录字段值
	 * @param recordName
	 * @param row
	 * @param colTag
	 * @return 如果不存在，则返回默认值
	 */
	public abstract long getRecordInt(String recordName, int row, String colTag);
	public abstract double getRecordFloat(String recordName, int row, String colTag);
	public abstract String getRecordString(String recordName, int row, String colTag);
	public abstract NFGUID getRecordObject(String recordName, int row, String colTag);
	
	/**
	 * 设置记录字段值
	 * @param recordName
	 * @param row
	 * @param column
	 * @param var
	 * @return 如果不存在，返回false
	 */
	public abstract boolean setRecord(String recordName, int row, int column, long var);
	public abstract boolean setRecord(String recordName, int row, int column, double var);
	public abstract boolean setRecord(String recordName, int row, int column, String var);
	public abstract boolean setRecord(String recordName, int row, int column, NFGUID var);

	/**
	 * 设置记录字段值
	 * @param recordName
	 * @param row
	 * @param colTag
	 * @param var
	 * @return 如果不存在，返回false
	 */
	public abstract boolean setRecord(String recordName, int row, String colTag, long var);
	public abstract boolean setRecord(String recordName, int row, String colTag, double var);
	public abstract boolean setRecord(String recordName, int row, String colTag, String var);
	public abstract boolean setRecord(String recordName, int row, String colTag, NFGUID var);
	
	/**
	 * 返回记录管理器
	 * @return
	 */
	public abstract NFIRecordManager getRecordManager();
}
