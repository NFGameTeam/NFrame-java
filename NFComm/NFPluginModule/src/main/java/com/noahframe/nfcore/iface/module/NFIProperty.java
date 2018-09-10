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

import java.util.List;


public interface NFIProperty {

	public void SetValue(NFData TData);
	public void SetValue(NFIProperty pProperty);
	
	public boolean SetInt(int value);
	public boolean SetFloat(double value);
	public boolean SetString(String value);
	public boolean SetObject(NFGUID value);
	public boolean SetVector2(NFVector2 value);
	public boolean SetVector3(NFVector3 value);

	public NFDATA_TYPE GetType();
	public boolean GeUsed();
	public String GetKey();
	public boolean GetSave();
	public boolean GetPublic();
	public boolean GetPrivate();
	public boolean GetCache();
	public boolean GetRef();
	public boolean GetUpload();

	public void SetSave(boolean bSave);
	public void SetPublic(boolean bPublic);
	public void SetPrivate(boolean bPrivate);
	public void SetCache(boolean bCache);
	public void SetRef(boolean bRef);
	public void SetUpload(boolean bUpload);

	public int GetInt();
	public double GetFloat();
	public String GetString();
	public NFGUID GetObject();
	public NFVector2 GetVector2();
	public NFVector3 GetVector3();

	public NFData GetValue();
	public List<String> GetEmbeddedList();
	public NFMapEx<String, String> GetEmbeddedMap();

	public boolean Changed();

	public String ToString();
	public boolean FromString(String strData);
	public boolean DeSerialization();

	public void RegisterCallback(PROPERTY_EVENT_FUNCTOR cb);
	
}
