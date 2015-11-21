/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心接口，提供对外封装
 */
public abstract class NFIKernelModule extends NFBehaviour {	
	
	
	/**
	 * 返回属性值
	 * @param name
	 * @return 如果不存在，则返回默认值
	 */
	public abstract long getPropertyInt(NFGUID self,  String name);
	public abstract double getPropertyFloat(NFGUID self, String name);
	public abstract String getPropertyString(NFGUID self, String name);
	public abstract NFGUID getPropertyObject(NFGUID self, String name);
	
	/**
	 * 设置属性
	 * @param name
	 * @param var
	 * @return 如果属性不存在，则返回false
	 */
	public abstract boolean setProperty(NFGUID self, String name, long var);
	public abstract boolean setProperty(NFGUID self, String name, double var);
	public abstract boolean setProperty(NFGUID self, String name, String var);
	public abstract boolean setProperty(NFGUID self, String name, NFGUID var);
	
	
	/**
	 * 返回表内容
	 * @param name
	 * @return 如果不存在，则返回默认值
	 */
	public abstract long getRecordInt(NFGUID self, String recordName, int row, String colTag);
	public abstract double getRecordFloat(NFGUID self, String recordName, int row, String colTag);
	public abstract String getRecordString(NFGUID self, String recordName, int row, String colTag);
	public abstract NFGUID getRecordObject(NFGUID self, String recordName, int row, String colTag);
	
	/**
	 * 设置表内容
	 * @param name
	 * @param var
	 * @return 如果属性不存在，则返回false
	 */
	public abstract boolean setRecord(NFGUID self, String recordName, int row, String colTag, long var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, String colTag, double var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, String colTag, String var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, String colTag, NFGUID var);
	
	
	
	/**
	 * 返回属性值
	 * @param name
	 * @return 如果不存在，则返回默认值
	 */
	public abstract long getRecordInt(NFGUID self, String recordName, int row, int column);
	public abstract double getRecordFloat(NFGUID self, String recordName, int row, int column);
	public abstract String getRecordString(NFGUID self, String recordName, int row, int column);
	public abstract NFGUID getRecordObject(NFGUID self, String recordName, int row, int column);
	
	/**
	 * 设置表内容
	 * @param name
	 * @param var
	 * @return 如果属性不存在，则返回false
	 */
	public abstract boolean setRecord(NFGUID self, String recordName, int row, int column, long var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, int column, double var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, int column, String var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, int column, NFGUID var);
	
	/**
	 * 创建场景
	 */
	public abstract boolean createContainer(int containerIndex, String sceneConfigID);
	public abstract boolean destroyContainer(int containerIndex);
	public abstract boolean switchScene(NFGUID self, int targetSceneID, int targetGroupID, float x, float y, float z, float orient);

	/**
	 * 对象控制
	 */
	public abstract NFIObject getObject(NFGUID self);
	public abstract NFIObject createObject(NFGUID self, int containerID, int groupID, String className, String configIndex, NFIDataList arg);
	public abstract boolean destroyObject(NFGUID self);
	public abstract boolean destroyAll();
	
	
	/**
	 * 添加属性回掉，当属性值改变的时候，自动调用回掉，可以添加多个（按顺序调用）
	 * @param cb
	 */
	public abstract void addCallback(NFIPropertyHandler cb);
	//public abstract void addCallback(NFIPropertyHandler cb);
    
}
