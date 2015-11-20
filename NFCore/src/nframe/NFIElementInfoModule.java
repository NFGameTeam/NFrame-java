/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang 框架核心,逻辑类接口
 */

public abstract class NFIElementInfoModule extends NFILogicModule {
	public abstract boolean load();

	public abstract boolean clear();

	// special
	public abstract boolean loadSceneInfo(String strFileName, String strClassName);

	public abstract boolean load(String strClassName);

	public abstract boolean existElement(String strConfigName);

	public abstract NFIPropertyManager getPropertyManager(String strConfigName);
	// public abstract NFIRecordManager GetRecordManager(String strConfigName);

	public abstract long getPropertyInt(String strConfigName, String strPropertyName);

	public abstract double getPropertyFloat(String strConfigName, String strPropertyName);

	public abstract String getPropertyString(String strConfigName, String strPropertyName);
}
