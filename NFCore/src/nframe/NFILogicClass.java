/**
 * 
 */
package nframe;

import java.util.List;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类接口
 */
public abstract class NFILogicClass extends NFBehaviour {
	
	public abstract NFIPropertyManager GetPropertyManager();
	//public NFIRecordManager GetRecordManager();

	public abstract void SetParent(NFILogicClass pClass);
	public abstract NFILogicClass GetParent();
	public abstract void SetTypeName(String strType);
	public abstract String GetTypeName();
	public abstract String GetClassName();
	public abstract boolean AddConfigName(String strConfigName);
	public abstract List<String> GetConfigNameList();
	public abstract void ClearConfigNameList();
	public abstract void SetInstancePath(String strPath);
	public abstract String GetInstancePath();

}
