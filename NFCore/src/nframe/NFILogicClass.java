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
	
	public abstract NFIPropertyManager getPropertyManager();
	//public NFIRecordManager GetRecordManager();

	public abstract void setParentClass(NFILogicClass parentClass);
	public abstract NFILogicClass getParentClass();
	public abstract void setTypeName(String typeName);
	public abstract String getTypeName();
	public abstract String getClassName();
	public abstract boolean addConfigName(String configName);
	public abstract List<String> getConfigNameList();
	public abstract void clearConfigNameList();
	public abstract void setInstancePath(String instancePath);
	public abstract String getInstancePath();
	
	public abstract boolean addFile(String file);
	public abstract List<String> getFileList();

}
