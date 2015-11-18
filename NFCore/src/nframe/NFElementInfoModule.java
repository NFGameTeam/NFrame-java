/**
 * 
 */
package nframe;

import java.util.List;

/**
 * @author lvsheng.huang
 * 框架核心,数据类
 */

public class NFElementInfoModule extends NFIElementInfoModule{
	
	private NFILogicModule mxLogicClassModule;
	
	@Override
	public void init()
	{
		mxLogicClassModule = (NFILogicModule)(this.getMng().getModule("NFCLogicClassModule"));

	}
	@Override
	public void afterInit()
	{
		
	}
	@Override
	public void beforeShut()
	{
		Clear();
	}
	@Override
	public void shut()
	{
		
	}
	@Override
	public void execute()
	{
		
	}
	@Override
	public boolean Load()
	{
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
	public boolean Clear()
	{
		return false;
	}

	@Override
	public boolean LoadSceneInfo(String strFileName, String strClassName)
	{
		return false;
	}
	@Override
	public boolean Load(String strClassName)
	{
		return false;
	}
	@Override
	public boolean ExistElement(String strConfigName)
	{
		return false;
	}
	@Override
	public NFIPropertyManager GetPropertyManager(String strConfigName)
	{
		return null;
	}
	// public abstract NFIRecordManager GetRecordManager(String strConfigName);
	@Override
	public long GetPropertyInt(String strConfigName, String strPropertyName)
	{
		return NFIData.INT_NIL;
	}
	@Override
	public double GetPropertyFloat(String strConfigName, String strPropertyName)
	{
		return NFIData.FLOAT_NIL;
	}
	@Override
	public String GetPropertyString(String strConfigName, String strPropertyName)
	{
		return NFIData.STRING_NIL;
	}
}
