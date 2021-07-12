/**   
* @Title: MODULE_EVENT_FUNCTOR 
* @Package ${package_name} 
* @Description: 模块事件接口 
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.functor;


import cn.yeegro.nframe.comm.code.NFEventDefine;
import cn.yeegro.nframe.comm.code.util.NFDataList;

public interface MODULE_EVENT_FUNCTOR {

	int operator(NFEventDefine zed, NFDataList zdl);
	
}
