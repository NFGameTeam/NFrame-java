/**
 * 
 */
package nframe;

import java.util.List;

/**
 * @author lvsheng.huang 框架核心,逻辑类接口
 */

public abstract class NFIElementInfoModule extends NFILogicModule {
	public abstract boolean Load();

	public abstract boolean Clear();

	// special
	public abstract boolean LoadSceneInfo(String strFileName, String strClassName);

	public abstract boolean Load(String strClassName);

	public abstract boolean ExistElement(String strConfigName);

	public abstract NFIPropertyManager GetPropertyManager(String strConfigName);
	// public abstract NFIRecordManager GetRecordManager(String strConfigName);

	public abstract long GetPropertyInt(String strConfigName, String strPropertyName);

	public abstract double GetPropertyFloat(String strConfigName, String strPropertyName);

	public abstract String GetPropertyString(String strConfigName, String strPropertyName);
}
