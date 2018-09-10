package com.noahframe.plugins.log;


import com.noahframe.nfcore.api.plugin.PluginWrapper;
import com.noahframe.nfcore.iface.NFIPlugin;

public class NFLogPlugin extends NFIPlugin {

	public NFLogPlugin(PluginWrapper wrapper) {
		super(wrapper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Install() {
		System.out.println("NFLogPlugin.Install()");
	}

	@Override
	public void Uninstall() {
		System.out.println("NFLogPlugin.Uninstall()");
	}
}
