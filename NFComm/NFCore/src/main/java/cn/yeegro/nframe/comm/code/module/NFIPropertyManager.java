/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.module;


import cn.yeegro.nframe.comm.code.iface.NFIProperty;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.util.NFDATA_TYPE;
import cn.yeegro.nframe.comm.code.util.NFData;
import cn.yeegro.nframe.comm.code.util.NFMapEx;
import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.functor.PROPERTY_EVENT_FUNCTOR;

public abstract class NFIPropertyManager extends NFMapEx<String, NFIProperty> {

	public abstract boolean RegisterCallback(String strProperty, PROPERTY_EVENT_FUNCTOR cb);
	public abstract NFIProperty AddProperty(NFGUID self, NFIProperty pProperty);
	public abstract NFIProperty AddProperty(NFGUID self, String strPropertyName, NFDATA_TYPE varType);

	public abstract NFGUID Self();
	
	public abstract boolean SetProperty(String strPropertyName, NFGUID TData);
	public abstract boolean SetProperty(String strPropertyName, NFData TData);

	public abstract boolean SetPropertyInt(String strPropertyName, int nValue) ;
	public abstract boolean SetPropertyFloat(String strPropertyName, double dwValue) ;
	public abstract boolean SetPropertyString(String strPropertyName, String strValue) ;
	public abstract boolean SetPropertyObject(String strPropertyName, NFGUID obj) ;
	public abstract boolean  SetPropertyVector2(String strPropertyName, NFVector2 value);
	public abstract boolean  SetPropertyVector3(String strPropertyName, NFVector3 value);
    
	public abstract int GetPropertyInt(String strPropertyName);
	public abstract double GetPropertyFloat(String strPropertyName) ;
	public abstract String GetPropertyString(String strPropertyName) ;
	public abstract NFGUID GetPropertyObject(String strPropertyName) ;
	public abstract NFVector2 GetPropertyVector2(String strPropertyName) ;
	public abstract NFVector3 GetPropertyVector3(String strPropertyName) ;

}
