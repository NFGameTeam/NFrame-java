/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;


import com.noahframe.nfcore.iface.util.NFMapEx;

public abstract class NFIComponentManager extends NFMapEx<String, NFIComponent> implements NFIModule {

	public <T> boolean AddComponent()
	{
		
		return false;
	}
	
	public <T> T FindComponent(String strName)
	{
		
		return null;
	}
	

	public abstract NFGUID Self();

	public abstract boolean AddComponent(String strComponentName, NFIComponent pNewComponent);
}
