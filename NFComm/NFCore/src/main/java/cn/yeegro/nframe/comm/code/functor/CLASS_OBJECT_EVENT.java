/**   
* @Title: CLASS_OBJECT_EVENT 
* @Package ${package_name} 
* @Description: 对象事件接口 
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.functor;

public enum CLASS_OBJECT_EVENT {

	COE_CREATE_NODATA,
	COE_CREATE_LOADDATA,
	COE_CREATE_BEFORE_EFFECT,
	COE_CREATE_EFFECTDATA,
	COE_CREATE_AFTER_EFFECT,
	COE_CREATE_HASDATA,
	COE_CREATE_FINISH,
	COE_CREATE_CLIENT_FINISH,
	COE_BEFOREDESTROY,
	COE_DESTROY,
}
