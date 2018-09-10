/**   
* @Title: NFComponentManager
* @Package ${package_name} 
* @Description: 组件管理对象
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.api.core;


import com.noahframe.nfcore.iface.module.NFIComponent;
import com.noahframe.nfcore.iface.module.NFIComponentManager;
import com.noahframe.nfcore.iface.module.NFGUID;

public class NFComponentManager extends NFIComponentManager {

	NFGUID mSelf;
	
	private NFComponentManager()
	{
		
	}
	public NFComponentManager(NFGUID self)
	{
		mSelf=self;
	}
	
	
	@Override
	public boolean Awake() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Init() {
		NFIComponent pComponent = First();
		    while (null != pComponent)
		    {
		        pComponent.Init();

		        pComponent = Next();
		    }

		    return true;
	}

	@Override
	public boolean AfterInit() {
		NFIComponent pComponent = First();
	    while (null != pComponent)
	    {
	        pComponent.AfterInit();

	        pComponent = Next();
	    }

	    return true;
	}

	@Override
	public boolean CheckConfig() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ReadyExecute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Execute() {
		NFIComponent pComponent = First();
	    while (null != pComponent)
	    {
	        pComponent.Execute();

	        pComponent = Next();
	    }

	    return true;
	}

	@Override
	public boolean BeforeShut() {
		NFIComponent pComponent = First();
	    while (null != pComponent)
	    {
	        pComponent.BeforeShut();

	        pComponent = Next();
	    }

	    return true;
	}

	@Override
	public boolean Shut() {
		NFIComponent pComponent = First();
	    while (null != pComponent)
	    {
	        pComponent.Shut();

	        pComponent = Next();
	    }

	    return true;
	}

	@Override
	public boolean Finalize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean OnReloadPlugin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NFGUID Self() {
		// TODO Auto-generated method stub
		 return mSelf;
	}

	@Override
	public boolean AddComponent(String strComponentName,
			NFIComponent pNewComponent) {
		return AddElement(strComponentName, pNewComponent);
	}

}
