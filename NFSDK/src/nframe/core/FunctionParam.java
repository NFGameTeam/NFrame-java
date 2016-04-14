package nframe.core;

import nframe.core.NFDefine.CLASS_OBJECT_EVENT;
import nframe.pluginmodule.NFGUID;

/**
 * @author zhiyu.zhao
 * @Description:
 * 
 */
public class FunctionParam
{
	public NFGUID mSelf = null;
	public String msName = null;
	
	
	public CLASS_OBJECT_EVENT event;
	public NFDataList var = null;
	
	//Property
	public NFData oldVar;
	public NFData newVar;
	
	

	public FunctionParam(NFGUID guid, String className, CLASS_OBJECT_EVENT event, NFDataList var)
	{
		super();
		this.mSelf = guid;
		this.msName = className;
		this.event = event;
		this.var = var;
	}


	public FunctionParam(NFGUID guid, String propertyName, NFData oldVar, NFData newVar)
	{
		super();
		this.mSelf = guid;
		this.msName = propertyName;
		this.oldVar = oldVar;
		this.newVar = newVar;
	}
	
	

}
