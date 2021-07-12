package cn.yeegro.nframe.comm.loader;

import org.pf4j.*;
import cn.yeegro.nframe.comm.code.iface.NFIPlugin;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.tools.file.DateUtil;
import cn.yeegro.nframe.tools.file.SysPath;
import cn.yeegro.nframe.tools.file.XPathUtil;
import org.jdom2.Element;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class NFPluginManager implements NFIPluginManager {

	// public
	public boolean Awake() {

		plugin_manager = new DefaultPluginManager();
		LoadPluginConfig();

		plugin_manager.loadPlugins();

		for (Entry<String, Boolean> entry : mPluginNameMap.entrySet()) {

			LoadPluginLibrary(entry.getKey());

		}

//		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {
//
//			entry.getValue().Awake();
//		}

		return true;
	}

	public boolean Init() {

		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			boolean isOK = entry.getValue().Init();
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).Init();
			}

		}

		return true;
	}

	public boolean AfterInit() {
		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			boolean isOK = entry.getValue().AfterInit();

			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).AfterInit();
			}

		}

		return true;
	}

	public boolean CheckConfig() {
		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			entry.getValue().CheckConfig();
			

			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).CheckConfig();
			}
			
		}

		return true;
	}

	public boolean Execute() {
		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			entry.getValue().Execute();

			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).Execute();
			}

		}

		return true;
	}

	public boolean ReadyExecute() {
		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			entry.getValue().ReadyExecute();
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).ReadyExecute();
			}
		}

		return true;
	}

	public boolean BeforeShut() {
		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			entry.getValue().BeforeShut();
			
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).BeforeShut();
			}
			
		}

		return true;
	}

	public boolean Shut() {
		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			entry.getValue().Shut();
			
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).Shut();
			}
			
		}

		return true;
	}

	public boolean Finalize() {

		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			entry.getValue().Finalize();
			
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).Finalize();
			}
			
		}

		for (Entry<String, Boolean> entry : mPluginNameMap.entrySet()) {

			UnLoadPluginLibrary(entry.getKey());

		}

		mPluginInstanceMap.clear();
		mPluginNameMap.clear();
		mModuleInstanceMap.clear();

		return true;
	}

	public boolean ReleaseInstance() {
		return false;
	}

	public boolean ReLoadPlugin(String strPinginName) {
		// TODO Auto-generated method stub
		return false;
	}

	public void Registered(NFIPlugin plugin) {
		// TODO Auto-generated method stub

	}

	public void UnRegistered(NFIPlugin plugin) {
		// TODO Auto-generated method stub

	}

	public Plugin FindPlugin(String strPluginName) {
		// TODO Auto-generated method stub
		return null;
	}

	//查找module所属插件
	public <T> Plugin FindPlugin(Class<T> pModule)
	{
		Plugin plugin=null;
		for (Entry<String, Plugin> entry : mPluginInstanceMap.entrySet()) {

//			entry.getValue().BeforeShut();

			String pluginid = entry.getKey();
			List<T> modules= plugin_manager.getExtensions(pModule, pluginid);
			if (modules!=null&&modules.size()>0)
			{
				plugin=entry.getValue();
				return plugin;
			}

		}
		return plugin;
	}

	public <T> void AddModule(String strModuleName, T pModule) {
		// TODO Auto-generated method stub
		if (null == FindModule(strModuleName)) {
			mModuleInstanceMap.put(strModuleName, pModule);
		}

	}

	public void RemoveModule(String strModuleName) {
		// TODO Auto-generated method stub
		if (mModuleInstanceMap.containsKey(strModuleName)) {
			mModuleInstanceMap.remove(strModuleName);
		}
	}

	public <T> T FindModule(String strModuleName) {
		// TODO Auto-generated method stub

		String strSubModuleName = strModuleName;

		strSubModuleName = strSubModuleName.replace(" ", "");

		if (mModuleInstanceMap.containsKey(strSubModuleName)) {

			return (T) mModuleInstanceMap.get(strSubModuleName);
		}

		return null;
	}
	
	public <T> List<T> FindModules(String pluginid, Class<T> clas) {
		// TODO Auto-generated method stub
		List<T> modules=null;
		if (null!=pluginid) {
			modules= plugin_manager.getExtensions(clas, pluginid);
		}
		else {
			modules=plugin_manager.getExtensions(clas);
		}
		return modules;
	}
	
	public <T> T FindModule(Class<T> clas) {
		T module=null;
		List<T> modules=plugin_manager.getExtensions(clas);
		if (modules.size()>0) {
			module=modules.get(0);
		}
		return module;
	}

	public int GetAppID() {
		// TODO Auto-generated method stub
		return mnAppID;
	}

	public void SetAppID(int nAppID) {
		// TODO Auto-generated method stub
		mnAppID=nAppID;
	}

	public long GetInitTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long GetNowTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String GetConfigPath() {
		// TODO Auto-generated method stub
		return SysPath.getResRoot()+ File.separator;
	}

	public void SetConfigName(String strFileName) {
		// TODO Auto-generated method stub

	}

	public String GetAppName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void SetAppName(String strAppName) {
		// TODO Auto-generated method stub

	}

	public String GetLogConfigName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void SetLogConfigName(String strName) {
		// TODO Auto-generated method stub

	}

	// protected
	protected boolean LoadPluginConfig() {

		String strContent = null;

		try {
			strContent = SysPath.getClassRootPath()+ File.separator + mstrConfigName;
		}
		catch (Exception e) {

		}

		XPathUtil rapidxml = null;
		try {
			rapidxml = XPathUtil.getInsance(strContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		Element pRoot = null;
		Element pAppNameNode = null;
		try {
			pRoot = rapidxml.getSingleElement("/XML");
			pAppNameNode = (Element) pRoot.getChildren().get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		List<Element> pPlugins = rapidxml.getElements("Plugin", pAppNameNode);

		for (int i = 0; i < pPlugins.size(); i++) {
			String strPluginName = pPlugins.get(i).getAttributeValue("Name");
			mPluginNameMap.put(strPluginName, true);
		}

		Element pPluginConfigPathNode = rapidxml.getSingleElement("ConfigPath",
				pAppNameNode);
		mstrConfigPath = pPluginConfigPathNode.getAttributeValue("Name");
		return true;
	}

	protected boolean LoadStaticPlugin(String strPluginDLLName) {
		return false;
	}

	protected boolean LoadPluginLibrary(String strPluginDLLName) {

		plugin_manager.startPlugin(strPluginDLLName);

		PluginWrapper pluginWrapper = plugin_manager
				.getPlugin(strPluginDLLName);

		Plugin plugin = pluginWrapper.getPlugin();

		// 加载实体
		mPluginInstanceMap.put(strPluginDLLName, plugin);
		// 加载模块
		List<?> modules = plugin_manager
				.getExtensions(strPluginDLLName);
		
		for (int i = 0; i < modules.size(); i++) {

			Object aa=modules.get(i);
			String strModuleName=modules.get(i).getClass().getName();

			AddModule(strModuleName, modules.get(i));
		}

		return false;
	}

	protected boolean UnLoadPluginLibrary(String strPluginDLLName) {
		return false;
	}

	protected boolean UnLoadStaticPlugin(String strPluginDLLName) {
		return false;
	}

	// private:
	private int mnAppID;
	private long mnInitTime;
	private long mnNowTime;
	private String mstrConfigPath;
	private String mstrConfigName;
	private String mstrAppName;
	private String mstrLogConfigName;

	private PluginManager plugin_manager;

	private Map<String, Boolean> mPluginNameMap = new LinkedHashMap<String, Boolean>();
	private Map<String, Plugin> mPluginInstanceMap = new LinkedHashMap<String, Plugin>();
	private Map<String, Object> mModuleInstanceMap = new LinkedHashMap<String, Object>();

	private Object mWebContext;

	/*
	 * 锁
	 */
	private static Object LOCK = new Object();

	private static NFPluginManager m_PluginManager = null;

	private NFPluginManager() {
		mnAppID = 0;
		mnInitTime = DateUtil.getCurrentTime();
		mnNowTime = mnInitTime;
		// 配置文件路径
		mstrConfigPath = "";
		mstrConfigName = "Plugin.xml";
	}

	public static NFPluginManager GetSingletonPtr() {
		synchronized (LOCK) {
			if (null == m_PluginManager) {
				m_PluginManager = new NFPluginManager();
			}
		}
		return m_PluginManager;
	}

	public String getMstrAppName() {
		return mstrAppName;
	}

	public void setMstrAppName(String mstrAppName) {
		this.mstrAppName = mstrAppName;
	}


	public Object getWebContext() {
		return mWebContext;
	}

	public void setWebContext(Object WebContext) {
		this.mWebContext = WebContext;
	}

	@Override
	public ClassLoader getPluginClassLoader(String pluginId) {
		return plugin_manager.getPluginClassLoader(pluginId);
	}
}
