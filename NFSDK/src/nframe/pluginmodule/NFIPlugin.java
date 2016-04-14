
package nframe.pluginmodule;

import java.util.Map;

import nframe.core.NFMap;

/**
 * 
 * @author zhiyu.zhao
 * @Description:
 *
 */
public abstract class NFIPlugin extends NFILogicModule
{
	public NFMap<String, NFILogicModule> map = new NFMap<>();

	public int getPluginVersion()
	{
		return 0;
	}

	public abstract String getPluginName();

	public abstract void install();

	public abstract void uninstall();

	public boolean init()
	{
		for (Map.Entry<String, NFILogicModule> entry : this.map.getMap().entrySet())
		{
			entry.getValue().init();
		}
		return true;
	}

	public boolean afterInit()
	{
		for (Map.Entry<String, NFILogicModule> entry : this.map.getMap().entrySet())
		{
			entry.getValue().afterInit();
		}
		return true;
	}

	public boolean checkConfig()
	{
		for (Map.Entry<String, NFILogicModule> entry : this.map.getMap().entrySet())
		{
			entry.getValue().checkConfig();
		}
		return true;
	}

	public boolean execute()
	{
		for (Map.Entry<String, NFILogicModule> entry : this.map.getMap().entrySet())
		{
			entry.getValue().execute();
		}
		return true;
	}

	public boolean beforeShut()
	{
		for (Map.Entry<String, NFILogicModule> entry : this.map.getMap().entrySet())
		{
			entry.getValue().beforeShut();
		}
		return true;
	}

	public boolean shut()
	{
		for (Map.Entry<String, NFILogicModule> entry : this.map.getMap().entrySet())
		{
			entry.getValue().shut();
		}
		return true;
	}

}
