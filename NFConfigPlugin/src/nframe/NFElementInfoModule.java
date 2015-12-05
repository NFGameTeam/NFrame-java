/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,数据类
 */

public class NFElementInfoModule extends NFIElementInfoModule{
	
	private NFILogicModule logicClassModule;
	
	@Override
	public void init(){
		logicClassModule = (NFILogicModule)(this.getPluginManager().getModule("NFCLogicClassModule"));
	}
	
	@Override
	public void afterInit(){
	}
	
	@Override
	public void beforeShut(){
		clear();
	}
	
	@Override
	public void shut(){
	}
	
	@Override
	public void execute(){
	}
	
	@Override
	public boolean load(){
/*		NF_SHARED_PTR<NFILogicClass> pLogicClass = m_pLogicClassModule->First();
	    while (pLogicClass.get())
	    {
			Load(pLogicClass);

	        mbLoaded = true;

	        pLogicClass = m_pLogicClassModule->Next();
	    }*/
	    
		return true;
	}
	
	@Override
	public boolean clear(){
		return false;
	}

	@Override
	public boolean loadSceneInfo(String strFileName, String strClassName){
		return false;
	}
	
	@Override
	public boolean load(String strClassName){
		return false;
	}
	
	@Override
	public boolean existElement(String strConfigName){
		return false;
	}
	
	@Override
	public NFIPropertyManager getPropertyManager(String strConfigName){
		return null;
	}
	
	// public abstract NFIRecordManager GetRecordManager(String strConfigName);
	@Override
	public long getPropertyInt(String strConfigName, String strPropertyName){
		return NFIData.INT_NIL;
	}
	
	@Override
	public double getPropertyFloat(String strConfigName, String strPropertyName){
		return NFIData.FLOAT_NIL;
	}
	
	@Override
	public String getPropertyString(String strConfigName, String strPropertyName){
		return NFIData.STRING_NIL;
	}
}
