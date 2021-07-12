/**   
* @Title: NFIClassModule
* @Package ${package_name} 
* @Description: 类模块
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.module;


import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.functor.CLASS_EVENT_FUNCTOR;
import cn.yeegro.nframe.comm.code.functor.CLASS_OBJECT_EVENT;
import cn.yeegro.nframe.comm.code.util.NFDataList;
import cn.yeegro.nframe.comm.code.util.NFMapEx;

public abstract class NFIClassModule extends NFMapEx<String, NFIClass> implements NFIModule {

	public abstract boolean Load();
	public abstract boolean Save();
	public abstract boolean Clear();
	
	
	public <BaseType>  boolean AddClassCallBack(String strClassName, BaseType pBase, CLASS_EVENT_FUNCTOR functor)
    {
		return AddClassCallBack(strClassName,functor);
    }
	
	
    public abstract boolean DoEvent(NFGUID objectID, String strClassName, CLASS_OBJECT_EVENT eClassEvent, NFDataList valueList);

    public abstract boolean AddClassCallBack(String strClassName, CLASS_EVENT_FUNCTOR cb);

    public abstract NFIPropertyManager GetClassPropertyManager( String strClassName);

    public abstract NFIRecordManager GetClassRecordManager( String strClassName);

    public abstract NFIComponentManager GetClassComponentManager( String strClassName);
}
