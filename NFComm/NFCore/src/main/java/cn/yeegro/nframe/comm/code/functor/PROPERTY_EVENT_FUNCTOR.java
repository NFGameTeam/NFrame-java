/**   
* @Title: PROPERTY_EVENT_FUNCTOR 
* @Package ${package_name} 
* @Description: PROPERTY事件接口
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.functor;


import cn.yeegro.nframe.comm.code.util.NFData;
import cn.yeegro.nframe.comm.code.api.NFGUID;

public interface PROPERTY_EVENT_FUNCTOR<T> {

	 int operator(NFGUID guid, String str, NFData pData, NFData zData);
	
}
