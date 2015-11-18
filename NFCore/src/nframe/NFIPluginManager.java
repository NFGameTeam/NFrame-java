/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,插件模块管理类
 */

public abstract class NFIPluginManager extends NFBehaviour {
	
	public abstract void install();
    public abstract void unInstall();

    public abstract String getClassPath();
    public abstract int getAPPID();
    public abstract int getAPPType();

    public abstract NFILogicModule getModule(String strClassName);
    public abstract boolean addModule(String strClassName, NFILogicModule xModule);

    //////////////////////////////////////////////////////////////////////
}
