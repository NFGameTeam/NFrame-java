package com.noahframe.plugins.net;


import com.noahframe.nfcore.api.plugin.PluginWrapper;
import com.noahframe.nfcore.iface.NFIPlugin;

public class NFNettyPlugin extends NFIPlugin {

	public NFNettyPlugin(PluginWrapper wrapper) {
		super(wrapper);
		
	}
	
	@Override
	public void Install() {
		System.out.println("NFNettyPlugin.Install()");
	}

	@Override
	public void Uninstall() {
		System.out.println("NFNettyPlugin.Uninstall()");
	}

}
