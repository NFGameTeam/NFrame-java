/**
 * 
 */
package nframe;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author Xiong
 * 属性管理器
 */
public class NFPropertyManager implements NFIPropertyManager {
	/** 属性表 */
	private Map<String, NFIProperty> properties = new Hashtable<String, NFIProperty>();
	/** 对象id */
	private NFIdent oid;
	
	public NFPropertyManager(NFIdent oid){
		this.oid = oid;
	}

	@Override
	public NFIProperty addProperty(String name, long var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, double var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, String var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, NFIdent var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, NFIData var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(oid, name);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty getProperty(String name) {
		return properties.get(name);
	}
	
	@Override
	public boolean hasProperty(String name){
		return properties.containsKey(name);
	}

	@Override
	public NFIProperty[] getPropertyList() {
		if (properties.isEmpty()){
			return null;
		}
		
		NFIProperty[] rt = new NFIProperty[properties.size()];
		int i = 0;
		for (NFIProperty prop : properties.values()){
			rt[i] = prop;
			++i;
		}
		return rt;
	}

	@Override
	public boolean addCallback(String name, NFIPropertyHandler cb) {
		NFIProperty prop = getProperty(name);
		if (prop != null){
			prop.addCallback(cb);
			return true;
		}
		return false;
	}

}
