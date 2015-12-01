/**
 * 
 */
package nframe.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import nframe.NFBehaviour;
import nframe.NFIPlugin;
import nframe.NFIPluginManager;

/**
 * @author Xiong
 * 动态库类
 */
public class NFDynLib extends NFBehaviour {
	private NFIPlugin plugin;
	private String libPath;
	private String pluginName;
	private NFIPluginManager pluginManager;
	
	/**
	 * 创建指定路径的jar
	 * @param libPath
	 * @param pluginManager
	 */
	public NFDynLib(String libPath, NFIPluginManager pluginManager){
		this.libPath = libPath;
		this.pluginManager = pluginManager;
	}

	public void install(String pluginName) throws Exception{
		// 加载jar
		URL url = new URL("file:"+this.libPath);
		@SuppressWarnings("resource")
		URLClassLoader loader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
		Class<?> classPlugin = loader.loadClass(pluginName);
		this.plugin = (NFIPlugin) classPlugin.newInstance();
		this.plugin.setPluginManager(this.pluginManager);
		this.pluginName = pluginName;
		this.plugin.install();
	}
	
	public NFIPlugin getPlugin(){
		return plugin;
	}
	
	public void uninstall(){
		plugin.uninstall();
	}
	
	@Override
	public void afterInit() {
		plugin.afterInit();
	}

	@Override
	public void beforeShut() {
		plugin.beforeShut();
	}

	@Override
	public void execute() {
		plugin.execute();
	}

	@Override
	public void init() {
		plugin.init();
	}

	@Override
	public void shut() {
		plugin.shut();
	}
	
}
