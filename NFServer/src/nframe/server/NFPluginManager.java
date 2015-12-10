/**
 * 
 */
package nframe.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import nframe.NFGUID;
import nframe.NFILogicModule;
import nframe.NFIPluginManager;

/**
 * @author Xiong
 * 插件管理器实现
 */
public class NFPluginManager extends NFIPluginManager {
	private static NFIPluginManager instance = new NFPluginManager();
	private ConcurrentHashMap<String, NFDynLib> dynLibs = new ConcurrentHashMap<String, NFDynLib>();
	private ConcurrentHashMap<String, NFILogicModule> modules = new ConcurrentHashMap<String, NFILogicModule>();
	private List<String> libraryPathList = new ArrayList<String>(1);
	private String classPath = "";

	public static NFIPluginManager getInstance(){
		return instance;
	}
	
	/**
	 * 设置插件库的路径前缀，非线程安全，必须在install调用之前调用
	 * @param path
	 */
	public void addLibraryPath(String path){
		libraryPathList.add(path);
	}
	
	@Override
	public boolean addModule(String name, NFILogicModule module) {
		if (modules.contains(name)){
			return false;
		}
		modules.put(name, module);
		return true;
	}

	@Override
	public int getAPPID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAPPType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getClassPath() {
		return classPath;
	}

	@Override
	public NFILogicModule getModule(String name) {
		return modules.get(name);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void install() {
		SAXReader saxReader = new SAXReader();

		if (libraryPathList.isEmpty()){
			libraryPathList.add("");
		}
		
		Document document = null;
		try {
//			document = saxReader.read(new File("H:/work/git/NFGameTeam/NFrame-java/NFrame-java/NFServer/bin/nframe/server/test/Plugin.xml"));
			document = saxReader.read(new File("Plugin.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Element root = document.getRootElement();
		for ( Iterator i = root.elementIterator("Plugin"); i.hasNext();) {
			Element e = (Element) i.next();
			String dynLibName = e.attributeValue("LibName");
			String pluginName = e.attributeValue("Name");
			assert dynLibName != null;
			assert pluginName != null;
			
			NFDynLib dynLib = null;
			for (String path : libraryPathList){
				try{
					dynLib = new NFDynLib(path + dynLibName + ".jar", this);
					break;
				}catch (Exception ex){
					dynLib = null;
					ex.printStackTrace();
				}
			}
			assert dynLib != null;
			
			if (!dynLibs.containsKey(dynLibName)){
				try{
					dynLib.install(pluginName);
					dynLibs.put(dynLibName, dynLib);
				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
		
		Element appIdNode = root.element("APPID");
		String appId = appIdNode.attributeValue("Name");
		long id = Long.parseLong(appId);
		this.setId(new NFGUID(id, 0));
		
		Element classPathNode = root.element("ClassPath");
		classPath = classPathNode.attributeValue("Name");
	}

	@Override
	public void uninstall() {
		for (NFDynLib dynLib : dynLibs.values()){
			dynLib.uninstall();
		}
		dynLibs.clear();
	}

	@Override
	public void afterInit() {
		for (NFDynLib dynLib : dynLibs.values()){
			dynLib.afterInit();
		}
	}

	@Override
	public void beforeShut() {
		for (NFDynLib dynLib : dynLibs.values()){
			dynLib.beforeShut();
		}
	}

	@Override
	public void execute() {
		for (NFDynLib dynLib : dynLibs.values()){
			dynLib.execute();
		}
	}

	@Override
	public void init() {
		for (NFDynLib dynLib : dynLibs.values()){
			dynLib.init();
		}
	}

	@Override
	public void shut() {
		for (NFDynLib dynLib : dynLibs.values()){
			dynLib.shut();
		}
	}

}
