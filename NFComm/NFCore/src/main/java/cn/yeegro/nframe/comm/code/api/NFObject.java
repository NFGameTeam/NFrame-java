/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.api;


import cn.yeegro.nframe.comm.code.functor.CLASS_OBJECT_EVENT;
import cn.yeegro.nframe.comm.code.iface.NFIProperty;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.module.NFIComponentManager;
import cn.yeegro.nframe.comm.code.module.NFIPropertyManager;
import cn.yeegro.nframe.comm.code.functor.PROPERTY_EVENT_FUNCTOR;
import cn.yeegro.nframe.comm.code.functor.RECORD_EVENT_FUNCTOR;
import cn.yeegro.nframe.comm.code.iface.NFIRecord;
import cn.yeegro.nframe.comm.code.module.NFIObject;
import cn.yeegro.nframe.comm.code.module.NFIRecordManager;

public class NFObject extends NFIObject {
	
    private NFGUID mSelf;
    private CLASS_OBJECT_EVENT mObjectEventState;
    private NFIRecordManager m_pRecordManager;
    private NFIPropertyManager m_pPropertyManager;
    private NFIComponentManager m_pComponentManager;
	

	public NFObject(NFGUID self)
	{
		mObjectEventState= CLASS_OBJECT_EVENT.COE_CREATE_NODATA;
		mSelf=self;


	    m_pRecordManager = new NFRecordManager(mSelf);
	    m_pPropertyManager = new NFPropertyManager(mSelf);
	    m_pComponentManager = new NFComponentManager(mSelf);
	}
	
	public boolean Init()
	{
	    return true;
	}
	
	public boolean Shut()
	{
	    return true;
	}

	
	@Override
	public boolean Execute() {
	    GetComponentManager().Execute();

	    return true;
	}

	@Override
	public NFGUID Self() {
		
		return mSelf;
	}

	@Override
	public CLASS_OBJECT_EVENT GetState() {
		// TODO Auto-generated method stub
		return mObjectEventState;
	}

	@Override
	public boolean SetState(CLASS_OBJECT_EVENT eState) {
		mObjectEventState = eState;
		return true;
	}

	@Override
	public boolean FindProperty(String strPropertyName) {
		 if (GetPropertyManager().GetElement(strPropertyName) != null)
		    {
		        return true;
		    }

		    return false;
	}

	@Override
	public boolean SetPropertyInt(String strPropertyName, int nValue) {
	    NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.SetInt(nValue);
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyFloat(String strPropertyName, double dwValue) {
		 NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
		    if (pProperty != null)
		    {
		        return pProperty.SetFloat(dwValue);
		    }

		    return false;
	}

	@Override
	public boolean SetPropertyString(String strPropertyName, String strValue) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.SetString(strValue);
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyObject(String strPropertyName, NFGUID obj) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.SetObject(obj);
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyVector2(String strPropertyName, NFVector2 value) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.SetVector2(value);
	    }

	    return false;
	}

	@Override
	public boolean SetPropertyVector3(String strPropertyName, NFVector3 value) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.SetVector3(value);
	    }

	    return false;
	}

	@Override
	public int GetPropertyInt(String strPropertyName) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
		    if (pProperty != null)
		    {
		        return pProperty.GetInt();
		    }

		    return 0;
	}

	@Override
	public double GetPropertyFloat(String strPropertyName) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.GetFloat();
	    }

	    return 0.0;
	}

	@Override
	public String GetPropertyString(String strPropertyName) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.GetString();
	    }

	    return null;
	}

	@Override
	public NFGUID GetPropertyObject(String strPropertyName) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.GetObject();
	    }

	    return null;
	}

	@Override
	public NFVector2 GetPropertyVector2(String strPropertyName) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.GetVector2();
	    }

	    return null;
	}

	@Override
	public NFVector3 GetPropertyVector3(String strPropertyName) {
		NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        return pProperty.GetVector3();
	    }

	    return null;
	}

	@Override
	public boolean FindRecord(String strRecordName) {
	    NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return true;
	    }

	    return false;
	}

	@Override
	public boolean SetRecordInt(String strRecordName, int nRow, int nCol,
			int nValue) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetInt(nRow, nCol, nValue);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordFloat(String strRecordName, int nRow, int nCol,
			double dwValue) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetFloat(nRow, nCol, dwValue);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordString(String strRecordName, int nRow, int nCol,
			String strValue) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetString(nRow, nCol, strValue);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordObject(String strRecordName, int nRow, int nCol,
			NFGUID obj) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetObject(nRow, nCol, obj);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordVector2(String strRecordName, int nRow, int nCol,
			NFVector2 value) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetVector2(nRow, nCol, value);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordVector3(String strRecordName, int nRow, int nCol,
			NFVector3 value) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetVector3(nRow, nCol, value);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordInt(String strRecordName, int nRow,
			String strColTag, int value) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetInt(nRow, strColTag, value);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordFloat(String strRecordName, int nRow,
			String strColTag, double value) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetFloat(nRow, strColTag, value);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordString(String strRecordName, int nRow,
			String strColTag, String value) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetString(nRow, strColTag, value);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordObject(String strRecordName, int nRow,
			String strColTag, NFGUID value) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetObject(nRow, strColTag, value);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordVector2(String strRecordName, int nRow,
			String strColTag, NFVector2 value) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetVector2(nRow, strColTag, value);
	    }

	    return false;
	}

	@Override
	public boolean SetRecordVector3(String strRecordName, int nRow,
			String strColTag, NFVector3 value) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        return pRecord.SetVector3(nRow, strColTag, value);
	    }

	    return false;
	}

	@Override
	public int GetRecordInt(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetInt(nRow, nCol);
		    }

		    return 0;
	}

	@Override
	public double GetRecordFloat(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetFloat(nRow, nCol);
		    }

		    return 0.0;
	}

	@Override
	public String GetRecordString(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetString(nRow, nCol);
		    }

		    return null;
	}

	@Override
	public NFGUID GetRecordObject(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetObject(nRow, nCol);
		    }

		    return null;
	}

	@Override
	public NFVector2 GetRecordVector2(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetVector2(nRow, nCol);
		    }

		    return null;
	}

	@Override
	public NFVector3 GetRecordVector3(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetVector3(nRow, nCol);
		    }

		    return null;
	}

	@Override
	public int GetRecordInt(String strRecordName, int nRow, String strColTag) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetInt(nRow, strColTag);
		    }

		    return 0;
	}

	@Override
	public double GetRecordFloat(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetFloat(nRow, strColTag);
		    }

		    return 0.0;
	}

	@Override
	public String GetRecordString(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetString(nRow, strColTag);
		    }

		    return null;
	}

	@Override
	public NFGUID GetRecordObject(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetObject(nRow, strColTag);
		    }

		    return null;
	}

	@Override
	public NFVector2 GetRecordVector2(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetVector2(nRow, strColTag);
		    }

		    return null;
	}

	@Override
	public NFVector3 GetRecordVector3(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
		    {
		        return pRecord.GetVector3(nRow, strColTag);
		    }

		    return null;
	}

	@Override
	public NFIRecordManager GetRecordManager() {
		// TODO Auto-generated method stub
		return m_pRecordManager;
	}

	@Override
	public NFIPropertyManager GetPropertyManager() {
		// TODO Auto-generated method stub
		return m_pPropertyManager;
	}

	@Override
	public NFIComponentManager GetComponentManager() {
		// TODO Auto-generated method stub
		 return m_pComponentManager;
	}

	@Override
	protected boolean AddRecordCallBack(String strRecordName,
			RECORD_EVENT_FUNCTOR cb) {
	    NFIRecord pRecord = GetRecordManager().GetElement(strRecordName);
	    if (pRecord != null)
	    {
	        pRecord.AddRecordHook(cb);

	        return true;
	    }

	    return false;
	}

	@Override
	protected boolean AddPropertyCallBack(String strPropertyName,
			PROPERTY_EVENT_FUNCTOR cb) {
	    NFIProperty pProperty = GetPropertyManager().GetElement(strPropertyName);
	    if (pProperty != null)
	    {
	        pProperty.RegisterCallback(cb);

	        return true;
	    }

	    return false;
	}

	
	
}
