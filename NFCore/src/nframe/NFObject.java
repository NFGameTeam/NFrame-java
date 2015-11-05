/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 框架基础对象类
 */
public class NFObject extends NFIObject {
	
	private NFIPropertyManager propMgr;
	
	public NFObject(NFIdent oid){
		super.setId(oid);
		this.propMgr = new NFPropertyManager(oid);
	}

	@Override
	public boolean hasProperty(String name){
		return propMgr.hasProperty(name);
	}

	@Override
	public boolean setProperty(String name, long var){
		NFIProperty prop = propMgr.getProperty(name);
		if (prop != null){
			prop.set(var);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean setProperty(String name, double var){
		NFIProperty prop = propMgr.getProperty(name);
		if (prop != null){
			prop.set(var);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean setProperty(String name, String var){
		NFIProperty prop = propMgr.getProperty(name);
		if (prop != null){
			prop.set(var);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean setProperty(String name, NFIdent var){
		NFIProperty prop = propMgr.getProperty(name);
		if (prop != null){
			prop.set(var);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean setProperty(String name){
		NFIProperty prop = propMgr.getProperty(name);
		if (prop != null){
			prop.set();
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean setProperty(String name, NFIData var){
		NFIProperty prop = propMgr.getProperty(name);
		if (prop != null){
			prop.set(var);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public NFIProperty getProperty(String name){
		return propMgr.getProperty(name);
	}

	@Override
	public NFIPropertyManager getPropertyManager(){
		return propMgr;
	}

	@Override
	public void init(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterInit(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeShut(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shut(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(){
		// TODO Auto-generated method stub
		
	}

}
