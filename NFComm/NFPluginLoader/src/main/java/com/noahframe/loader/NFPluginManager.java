package com.noahframe.loader;

import com.noahframe.api.file.SysPath;
import com.noahframe.api.utils.DateUtil;
import com.noahframe.api.utils.XPathUtil;
import com.noahframe.nfcore.api.plugin.DefaultPluginManager;
import com.noahframe.nfcore.api.plugin.PluginManager;
import com.noahframe.nfcore.api.plugin.PluginWrapper;
import com.noahframe.nfcore.iface.module.NFIModule;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.noahframe.nfcore.iface.NFIPlugin;
import com.noahframe.nfcore.iface.NFIPluginManager;
import org.jdom.Element;



public class NFPluginManager implements NFIPluginManager {

	// public
	public boolean Awake() {

		plugin_manager = new DefaultPluginManager();
		LoadPluginConfig();

		plugin_manager.loadPlugins();
		for (Entry<String, Boolean> entry : mPluginNameMap.entrySet()) {

			LoadPluginLibrary(entry.getKey());

		}

		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			entry.getValue().Awake();
		}

		return true;
	}

	public boolean Init() {

		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			boolean isOK = entry.getValue().Init();
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).Init();
			}

		}

		return true;
	}

	public boolean AfterInit() {
		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			boolean isOK = entry.getValue().AfterInit();

			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).AfterInit();
			}

		}

		return true;
	}

	public boolean CheckConfig() {
		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			entry.getValue().CheckConfig();
			

			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).CheckConfig();
			}
			
		}

		return true;
	}

	public boolean Execute() {
		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			entry.getValue().Execute();

//			String pluginid = entry.getKey();
//			List<ZOEIModule> modules = plugin_manager.getExtensions(pluginid);
//
//			for (int i = 0; i < modules.size(); i++) {
//				modules.get(i).Execute();
//			}

		}

		return true;
	}

	public boolean ReadyExecute() {
		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			entry.getValue().ReadyExecute();
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).ReadyExecute();
			}
		}

		return true;
	}

	public boolean BeforeShut() {
		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			entry.getValue().BeforeShut();
			
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).BeforeShut();
			}
			
		}

		return true;
	}

	public boolean Shut() {
		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			entry.getValue().Shut();
			
			String pluginid = entry.getKey();
			List<NFIModule> modules = plugin_manager.getExtensions(pluginid);

			for (int i = 0; i < modules.size(); i++) {
				modules.get(i).Shut();
			}
			
		}

		return true;
	}

	public boolean Finalize() {

		for (Entry<String, NFIPlugin> entry : mPluginInstanceMap.entrySet()) {

			entry.getValue().Finalize();
			
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

	public NFIPlugin FindPlugin(String strPluginName) {
		// TODO Auto-generated method stub
		return null;
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
		return SysPath.getResRoot()+File.separator;
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
		strContent = SysPath.root + mstrConfigName;

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

		NFIPlugin plugin = pluginWrapper.getPlugin();

		// 加载实体
		mPluginInstanceMap.put(strPluginDLLName, plugin);
		// 加载模块
		List<?> modules = plugin_manager
				.getExtensions(strPluginDLLName);
		
		for (int i = 0; i < modules.size(); i++) {

			AddModule(modules.get(i).getClass().getName(), modules.get(i));
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

	private Map<String, Boolean> mPluginNameMap = new HashMap<String, Boolean>();
	private Map<String, NFIPlugin> mPluginInstanceMap = new HashMap<String, NFIPlugin>();
	private Map<String, Object> mModuleInstanceMap = new HashMap<String, Object>();

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





}
