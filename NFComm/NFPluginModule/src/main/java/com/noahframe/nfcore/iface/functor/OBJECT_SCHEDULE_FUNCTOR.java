/**   
* @Title: OBJECT_SCHEDULE_FUNCTOR 
* @Package ${package_name} 
* @Description: 对象策划接口
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.functor;


import com.noahframe.nfcore.iface.module.NFGUID;

public interface OBJECT_SCHEDULE_FUNCTOR<T> {

	int operator(NFGUID guid, String str, float f, int n);
}
