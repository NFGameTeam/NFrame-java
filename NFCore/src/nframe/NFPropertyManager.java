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
	private NFGUID guid;
	
	public NFPropertyManager(NFGUID guid){
		this.guid = guid;
	}

	@Override
	public NFIProperty addProperty(String name, long var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(guid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, double var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(guid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, String var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(guid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, NFGUID var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(guid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, NFIData var) {
		NFIProperty rt = null;
		if (!properties.containsKey(name)){
			rt = new NFProperty(guid, name, var);
			properties.put(name, rt);
		}
		return rt;
	}


	@Override
	public long getPropertyInt(String name){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			return prop.getInt();
		}
		return NFIData.INT_NIL;
	}

	@Override
	public double getPropertyFloat(String name){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			return prop.getFloat();
		}
		return NFIData.FLOAT_NIL;
	}

	@Override
	public String getPropertyString(String name){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			return prop.getString();
		}
		return NFIData.STRING_NIL;
	}

	@Override
	public NFGUID getPropertyObject(String name){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			return prop.getObject();
		}
		return NFIData.OBJECT_NIL;
	}

	@Override
	public boolean setProperty(String name, long var){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			prop.set(var);
			return true;
		}
		return false;
	}

	@Override
	public boolean setProperty(String name, double var){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			prop.set(var);
			return true;
		}
		return false;
	}

	@Override
	public boolean setProperty(String name, String var){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			prop.set(var);
			return true;
		}
		return false;
	}

	@Override
	public boolean setProperty(String name, NFGUID var){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			prop.set(var);
			return true;
		}
		return false;
	}

	@Override
	public boolean setProperty(String name, NFIData var){
		NFIProperty prop = properties.get(name);
		if (prop != null){
			prop.set(var);
			return true;
		}
		return false;
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
