/**
 * 
 */
package nframe;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xiong
 * 属性管理器
 */
public class NFPropertyManager implements NFIPropertyManager {
	
	private Map<String, NFIProperty> propertyList = new HashMap<String, NFIProperty>();
	private NFIdent oid;
	
	public NFPropertyManager(NFIdent oid){
		this.oid = oid;
	}

	@Override
	public NFIProperty addProperty(String name, long var) {
		NFIProperty rt = null;
		if (!propertyList.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			propertyList.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, double var) {
		NFIProperty rt = null;
		if (!propertyList.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			propertyList.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, String var) {
		NFIProperty rt = null;
		if (!propertyList.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			propertyList.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, NFIdent var) {
		NFIProperty rt = null;
		if (!propertyList.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			propertyList.put(name, rt);
		}
		return rt;
	}

	@Override
	public NFIProperty addProperty(String name, NFIData var) {
		NFIProperty rt = null;
		if (!propertyList.containsKey(name)){
			rt = new NFProperty(oid, name, var);
			propertyList.put(name, rt);
		}
		return rt;
	}

	@Override
	public boolean setProperty(String name, long var) {
		boolean rt = false;
		NFIProperty prop = propertyList.get(name);
		if (null != prop){
			rt = true;
			prop.set(var);
		}
		return rt;
	}

	@Override
	public boolean setProperty(String name, double var) {
		boolean rt = false;
		NFIProperty prop = propertyList.get(name);
		if (null != prop){
			rt = true;
			prop.set(var);
		}
		return rt;
	}

	@Override
	public boolean setProperty(String name, String var) {
		boolean rt = false;
		NFIProperty prop = propertyList.get(name);
		if (null != prop){
			rt = true;
			prop.set(var);
		}
		return rt;
	}

	@Override
	public boolean setProperty(String name, NFIdent var) {
		boolean rt = false;
		NFIProperty prop = propertyList.get(name);
		if (null != prop){
			rt = true;
			prop.set(var);
		}
		return rt;
	}

	@Override
	public boolean setProperty(String name, NFIData var) {
		boolean rt = false;
		NFIProperty prop = propertyList.get(name);
		if (null != prop){
			rt = true;
			prop.set(var);
		}
		return rt;
	}

	@Override
	public NFIProperty getProperty(String name) {
		return propertyList.get(name);
	}

	@Override
	public NFIProperty[] getPropertyList() {
		if (propertyList.isEmpty()){
			return null;
		}
		
		NFIProperty[] rt = new NFIProperty[propertyList.size()];
		int i = 0;
		for (NFIProperty prop : propertyList.values()){
			rt[i] = prop;
			++i;
		}
		return rt;
	}

	@Override
	public boolean addCallback(String name, NFIPropertyHandler cb) {
		NFIProperty prop = getProperty(name);
		if (null != prop){
			prop.addCallback(cb);
			return true;
		}
		return false;
	}

}
