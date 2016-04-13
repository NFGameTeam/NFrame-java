package nframe.pluginmodule;

/**
 * 
 * @author zhiyu.zhao
 * @Description:
 *
 */
public abstract class NFILogicModule extends NFBehaviour
{
	public String name;
	public boolean bCanReload = true;

	public void OnReload()
	{
		beforeShut();
		shut();
		init();
		afterInit();
	}

	private NFIPluginManager pluginManager;

	public NFIPluginManager getPluginManager()
	{
		return pluginManager;
	}

	public void setPluginManager(NFIPluginManager pluginManager)
	{
		this.pluginManager = pluginManager;
	}
}
