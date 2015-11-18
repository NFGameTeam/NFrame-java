/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类模块
 */

public abstract class NFILogicClassModule extends NFBehaviour {
	
	public abstract  boolean Load();
	public abstract  boolean Clear();

	public abstract  boolean ReLoad(String strClassName);

	public abstract  NFIPropertyManager GetClassPropertyManager(String strClassName);

	//public abstract  NFIRecordManager GetClassRecordManager(String strClassName);
}
