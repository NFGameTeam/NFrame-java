/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类模块
 */

public abstract class NFILogicClassModule extends NFBehaviour {
	
	public abstract  boolean load();
	public abstract  boolean clear();

	public abstract  boolean reload(String className);

	public abstract  NFIPropertyManager getPropertyManager(String className);
	public abstract  NFIRecordManager getRecordManager(String className);
}
