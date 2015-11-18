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
	public abstract NFGUID getPropertyObject(NFGUID self, String recordName, int row, String colTag);
	
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
	public abstract long getRecordInt(NFGUID self, String recordName, int row, int col);
	public abstract double getRecordFloat(NFGUID self, String recordName, int row, int col);
	public abstract String getRecordString(NFGUID self, String recordName, int row, int col);
	public abstract NFGUID getPropertyObject(NFGUID self, String recordName, int row, int col);
	
	/**
	 * 设置表内容
	 * @param name
	 * @param var
	 * @return 如果属性不存在，则返回false
	 */
	public abstract boolean setRecord(NFGUID self, String recordName, int row, int col, long var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, int col, double var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, int col, String var);
	public abstract boolean setRecord(NFGUID self, String recordName, int row, int col, NFGUID var);
	
	/**
	 * 创建场景
	 */
	public abstract boolean CreateContainer(int nContainerIndex, String strSceneConfigID);
	public abstract boolean DestroyContainer(int nContainerIndex);
	public abstract boolean SwitchScene(NFGUID self, int nTargetSceneID, int nTargetGroupID, float fX, float fY, float fZ, float fOrient);

	/**
	 * 对象控制
	 */
	public abstract NFIObject GetObject(NFGUID self);
	public abstract NFIObject CreateObject(NFGUID self, int nContainerID, int nGroupID, String strClassName, String strConfigIndex, NFIDataList arg);
	public abstract  boolean DestroyObject(NFGUID self);
	public abstract  boolean DestroyAll();
	
	
	
	
    
}
