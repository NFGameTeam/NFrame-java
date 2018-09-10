/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;


import com.noahframe.nfcore.iface.NFEventDefine;
import com.noahframe.nfcore.iface.functor.MODULE_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.functor.OBJECT_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.util.NFDataList;

public interface NFIEventModule extends NFIModule {


	public boolean DoEvent(NFEventDefine nEventID, NFDataList valueList);
	public boolean ExistEventCallBack(NFEventDefine nEventID);
	public boolean RemoveEventCallBack(NFEventDefine nEventID);
	
	public boolean DoEvent(NFGUID self, NFEventDefine nEventID, NFDataList valueList);
	public boolean ExistEventCallBack(NFGUID self, NFEventDefine nEventID);
	public boolean RemoveEventCallBack(NFGUID self, NFEventDefine nEventID);
	public boolean RemoveEventCallBack(NFGUID self);
	
	public boolean AddEventCallBack(NFEventDefine nEventID, MODULE_EVENT_FUNCTOR cb);
	public boolean AddEventCallBack(NFGUID self, NFEventDefine nEventID, OBJECT_EVENT_FUNCTOR cb);
}
