package com.noahframe.plugins.kernel;


import com.noahframe.nfcore.api.plugin.PluginWrapper;
import com.noahframe.nfcore.iface.NFIPlugin;

public class NFKernelPlugin extends NFIPlugin {

	public NFKernelPlugin(PluginWrapper wrapper) {
		super(wrapper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Install() {
		System.out.println("NFKernelPlugin.Install()");
	}

	@Override
	public void Uninstall() {
		System.out.println("NFKernelPlugin.Uninstall()");
	}

}
