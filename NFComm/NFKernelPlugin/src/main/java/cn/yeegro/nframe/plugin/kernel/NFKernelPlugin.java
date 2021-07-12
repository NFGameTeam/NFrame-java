package cn.yeegro.nframe.plugin.kernel;


import org.pf4j.Plugin;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import cn.yeegro.nframe.comm.code.iface.NFIPlugin;

public class NFKernelPlugin extends Plugin {

	public NFKernelPlugin(PluginWrapper wrapper) {
		super(wrapper);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method is called by the application when the plugin is started.
	 * See {@link PluginManager#startPlugin(String)}.
	 */
	@Override
	public void start() {
		System.out.println("NFKernelPlugin.start()");
	}

	/**
	 * This method is called by the application when the plugin is stopped.
	 * See {@link PluginManager#stopPlugin(String)}.
	 */
	@Override
	public void stop() {
		System.out.println("NFKernelPlugin.stop()");
	}

	/**
	 * This method is called by the application when the plugin is deleted.
	 * See {@link PluginManager#deletePlugin(String)}.
	 */
	@Override
	public void delete() {
		System.out.println("NFKernelPlugin.delete()");
	}

}
