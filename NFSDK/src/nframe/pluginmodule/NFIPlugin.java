
package nframe.pluginmodule;

/**
 * 
 * @author zhiyu.zhao
 * @Description:
 *
 */
public abstract class NFIPlugin extends NFILogicModule
{
	public abstract int GetPluginVersion();

	public abstract void install();

	public abstract void uninstall();
}
