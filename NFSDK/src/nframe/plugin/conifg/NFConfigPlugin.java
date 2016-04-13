package nframe.plugin.conifg;

import nframe.pluginmodule.NFIPlugin;

public class NFConfigPlugin extends NFIPlugin
{
	// private NFILogicClassModule logicClassModule;
	// private NFIElementInfoModule elementInfoModule;

	@Override
	public void install()
	{
		// logicClassModule = new NFLogicClassModule();
		// elementInfoModule = new NFElementInfoModule();
		// this.getPluginManager().addModule("nframe.NFElementInfoModule", logicClassModule);
		// this.getPluginManager().addModule("nframe.NFLogicClassModule", elementInfoModule);
	}

	@Override
	public void uninstall()
	{
	}

	@Override
	public boolean init()
	{
		// logicClassModule.init();
		// elementInfoModule.init();
		return true;
	}

	@Override
	public boolean afterInit()
	{
		// logicClassModule.afterInit();
		// elementInfoModule.afterInit();
		return true;
	}

	@Override
	public boolean beforeShut()
	{
		// logicClassModule.beforeShut();
		// elementInfoModule.beforeShut();
		return true;
	}

	@Override
	public boolean shut()
	{
		// logicClassModule.shut();
		// elementInfoModule.shut();
		return true;
	}

	@Override
	public boolean execute()
	{
		// logicClassModule.execute();
		// elementInfoModule.execute();
		return true;
	}

	@Override
	public int GetPluginVersion()
	{
		return 0;
	}


}
