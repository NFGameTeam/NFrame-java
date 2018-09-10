/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;


import com.noahframe.nfcore.iface.functor.PROPERTY_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.math.NFVector2;
import com.noahframe.nfcore.iface.math.NFVector3;
import com.noahframe.nfcore.iface.util.NFDATA_TYPE;
import com.noahframe.nfcore.iface.util.NFData;
import com.noahframe.nfcore.iface.util.NFMapEx;

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
