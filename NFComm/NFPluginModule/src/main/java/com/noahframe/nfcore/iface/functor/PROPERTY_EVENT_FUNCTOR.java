/**   
* @Title: PROPERTY_EVENT_FUNCTOR 
* @Package ${package_name} 
* @Description: PROPERTY事件接口
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.functor;


import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.util.NFData;

public interface PROPERTY_EVENT_FUNCTOR<T> {

	 int operator(NFGUID guid, String str, NFData pData, NFData zData);
	
}
