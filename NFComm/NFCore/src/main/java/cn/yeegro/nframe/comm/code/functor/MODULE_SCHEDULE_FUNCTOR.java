/**   
* @Title: MODULE_SCHEDULE_FUNCTOR 
* @Package ${package_name} 
* @Description: 模块计划接口 
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.functor;


public interface MODULE_SCHEDULE_FUNCTOR<T> {
	
	int operator(String strScheduleName, float fIntervalTime, int nCount);
	
}
