/**   
* @Title: OBJECT_EVENT_FUNCTOR 
* @Package ${package_name} 
* @Description: 对象事件接口
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.functor;


import com.noahframe.nfcore.iface.NFEventDefine;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.util.NFDataList;

public interface OBJECT_EVENT_FUNCTOR {
	int operator(NFGUID guid, NFEventDefine zed, NFDataList zdl);
}
