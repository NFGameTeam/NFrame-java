/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.iface;



import org.pf4j.Plugin;
import org.pf4j.PluginClassLoader;

import java.util.List;


public interface NFIPluginManager {

	boolean ReLoadPlugin(String strPinginName);
	void Registered(NFIPlugin plugin);
	void UnRegistered(NFIPlugin plugin);
	Plugin FindPlugin(String strPluginName);
	<T> Plugin FindPlugin(Class<T> pModule);
	<T> void AddModule(String strModuleName, T module);
	void RemoveModule(String strModuleName);
	<T> T FindModule(String strModuleName);
	<T> List<T> FindModules(String pluginid, Class<T> clas);
	<T> T FindModule(Class<T> clas);
	
	
	 int GetAppID();
	 void SetAppID(int nAppID);
	 
	 long GetInitTime();
	 long GetNowTime();
	 
	 String GetConfigPath();
	 void SetConfigName(String strFileName);
	 
	 String GetAppName();
	 void SetAppName(String strAppName);
	 
	 String GetLogConfigName();
	 void SetLogConfigName(String strName);

	Object getWebContext();

	void setWebContext(Object WebContext);

	ClassLoader getPluginClassLoader(String pluginId);
	 
	 
	
}
