/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类
 */

import java.util.List;
import java.util.ArrayList;

public class NFLogicClass extends NFILogicClass{
	
	private NFIPropertyManager mxPropertyManager;
	//private NFIRecordManager mXRecordManager;

	private NFILogicClass mxParentClass;
	private String mstrType;
	private String mstrClassName;
	private String mstrClassInstancePath;

	private List<String> mlConfigList = new ArrayList<String>();
    
	@Override
	public NFIPropertyManager GetPropertyManager()
	{
		return mxPropertyManager;
	}
	//public NFIRecordManager GetRecordManager();
	@Override
	public void SetParent(NFILogicClass pClass)
	{
		this.mxParentClass = pClass;
	}
	@Override
	public NFILogicClass GetParent()
	{
		return this.mxParentClass;
	}
	@Override
	public void SetTypeName(String strType)
	{
		this.mstrType = strType;
	}
	@Override
	public String GetTypeName()
	{
		return this.mstrType;
	}
	@Override
	public String GetClassName()
	{
		return this.mstrClassName;
	}
	@Override
	public boolean AddConfigName(String strConfigName)
	{
		this.mlConfigList.add(strConfigName);
		
		return true;
	}
	@Override
	public List<String> GetConfigNameList()
	{
		return this.mlConfigList;
	}
	@Override
	public void ClearConfigNameList()
	{
		this.mlConfigList.clear();
	}
	@Override
	public void SetInstancePath(String strPath)
	{
		this.mstrClassInstancePath = strPath;
	}
	@Override
	public String GetInstancePath()
	{
		return this.mstrClassInstancePath;
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
