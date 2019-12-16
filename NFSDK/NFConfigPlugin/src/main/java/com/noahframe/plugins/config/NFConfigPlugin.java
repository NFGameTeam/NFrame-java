package com.noahframe.plugins.config;


import com.noahframe.nfcore.api.plugin.PluginWrapper;
import com.noahframe.nfcore.iface.NFIPlugin;

public class NFConfigPlugin extends NFIPlugin {

	public NFConfigPlugin(PluginWrapper wrapper) {
		super(wrapper);
		// TODO Auto-generated constructor stub
	}

	@Override
    public String GetPluginName(){
    	return null;
    }
	
	@Override
	public void Install() {
		
		NFClassModule.GetSingletonPtr();
		NFElementModule.GetSingletonPtr();
		
		System.out.println("NFConfigPlugin.Install()");
	}

	@Override
	public void Uninstall() {
		System.out.println("NFConfigPlugin.Uninstall()");
	}
}
