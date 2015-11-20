/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类
 */

public abstract class NFILogicModule extends NFBehaviour {

	private NFIPluginManager pluginManager;
	
	public NFIPluginManager getPluginManager(){
		return pluginManager;
	}
	
	public void setPluginManager(NFIPluginManager pluginManager){
		this.pluginManager = pluginManager;
	}
}
