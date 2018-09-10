/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.api.core;


import com.noahframe.nfcore.iface.functor.PROPERTY_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.module.NFIProperty;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.math.NFVector2;
import com.noahframe.nfcore.iface.math.NFVector3;
import com.noahframe.nfcore.iface.module.NFIPropertyManager;
import com.noahframe.nfcore.iface.util.NFDATA_TYPE;
import com.noahframe.nfcore.iface.util.NFData;

import java.util.Map;


public class NFPropertyManager extends NFIPropertyManager {

	private NFGUID mSelf;
	private Map<String, Integer> mxPropertyIndexMap;
	
	public NFPropertyManager(NFGUID self)
	{
		 mSelf = self;
	}
	
	@Override
	public boolean RegisterCallback(String strProperty,
			PROPERTY_EVENT_FUNCTOR cb) {
		 NFIProperty pProperty = this.GetElement(strProperty);
		    if (pProperty != null)
		    {
		        pProperty.RegisterCallback(cb);
		        return true;
		    }

		    return false;
	}

	@Override
	public NFIProperty AddProperty(NFGUID self, NFIProperty pProperty) {
		String strProperty = pProperty.GetKey();
		NFIProperty pOldProperty = this.GetElement(strProperty);
	    if (null==pOldProperty)
	    {
			NFIProperty pNewProperty=new NFProperty(self, strProperty, pProperty.GetType());

	        pNewProperty.SetPublic(pProperty.GetPublic());
	        pNewProperty.SetPrivate(pProperty.GetPrivate());
	        pNewProperty.SetSave(pProperty.GetSave());
	        pNewProperty.SetCache(pProperty.GetCache());
	        pNewProperty.SetRef(pProperty.GetRef());
			pNewProperty.SetUpload(pProperty.GetUpload());

	        this.AddElement(strProperty, pNewProperty);
	    }

	    return pOldProperty;
	}

	@Override
	public NFGUID Self() {
		// TODO Auto-generated method stub
		return mSelf;
	}

	@Override
	public boolean SetProperty(String strPropertyName, NFGUID TData) {
		return false;
	}

	public boolean SetProperty(String strPropertyName, NFData TData) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        pProperty.SetValue(TData);

	        return true;
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyInt(String strPropertyName, int nValue) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        pProperty.SetInt(nValue);

	        return true;
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyFloat(String strPropertyName, double dwValue) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        pProperty.SetFloat(dwValue);

	        return true;
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyString(String strPropertyName, String strValue) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        pProperty.SetString(strValue);

	        return true;
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyObject(String strPropertyName, NFGUID obj) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        pProperty.SetObject(obj);

	        return true;
	    }

	    return false;
	}

	@Override
	public int GetPropertyInt(String strPropertyName) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	    	return pProperty.GetInt();

	    }

	    return 0;
	}

	@Override
	public double GetPropertyFloat(String strPropertyName) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	    	return pProperty.GetFloat();

	    }

	    return 0.0;
	}

	@Override
	public String GetPropertyString(String strPropertyName) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	    	return pProperty.GetString();

	    }

	    return null;
	}

	@Override
	public NFGUID GetPropertyObject(String strPropertyName) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	    	return pProperty.GetObject();

	    }

	    return null;
	}

	@Override
	public NFIProperty AddProperty(NFGUID self, String strPropertyName,
			NFDATA_TYPE varType) {
		NFIProperty pProperty = this.GetElement(strPropertyName);
		    if (null==pProperty)
		    {
		        pProperty = new NFProperty(self, strPropertyName, varType);

		        this.AddElement(strPropertyName, pProperty);
		    }

		    return pProperty;
	}

	@Override
	public boolean SetPropertyVector2(String strPropertyName, NFVector2 value) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        pProperty.SetVector2(value);

	        return true;
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyVector3(String strPropertyName, NFVector3 value) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        pProperty.SetVector3(value);

	        return true;
	    }

	    return false;
	}

	@Override
	public NFVector2 GetPropertyVector2(String strPropertyName) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	    	return pProperty.GetVector2();

	    }

	    return null;
	}

	@Override
	public NFVector3 GetPropertyVector3(String strPropertyName) {
		NFIProperty pProperty = GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	    	return pProperty.GetVector3();

	    }

	    return null;
	}

}
