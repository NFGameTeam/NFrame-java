/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.api;


import cn.yeegro.nframe.comm.code.iface.NFIProperty;
import cn.yeegro.nframe.comm.code.iface.NFPlatform;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.util.NFDATA_TYPE;
import cn.yeegro.nframe.comm.code.util.NFData;
import cn.yeegro.nframe.comm.code.util.NFDataList;
import cn.yeegro.nframe.comm.code.util.NFMapEx;
import cn.yeegro.nframe.comm.code.functor.PROPERTY_EVENT_FUNCTOR;

import java.util.ArrayList;
import java.util.List;




public class NFProperty implements NFIProperty {

	private List<PROPERTY_EVENT_FUNCTOR> mtPropertyCallback=new ArrayList<PROPERTY_EVENT_FUNCTOR>();

	private NFGUID mSelf;
	private String msPropertyName;
	private NFDATA_TYPE eType;

	private NFData mxData;
	private NFMapEx<String, String> mxEmbeddedMap=new NFMapEx<String, String>();
	private List<String> mxEmbeddedList=new ArrayList<String>();

	private boolean mbPublic;
	private boolean mbPrivate;
	private boolean mbSave;
	private boolean mbCache;
	private boolean mbRef;
	private boolean mbUpload;

	private NFProperty() {
		mbPublic = false;
		mbPrivate = false;
		mbSave = false;
		mbCache = false;
		mbRef = false;
		mbUpload = false;

		mSelf = new NFGUID();
		eType = NFDATA_TYPE.TDATA_UNKNOWN;

		msPropertyName = "";
	}

	public NFProperty(NFGUID self, String strPropertyName,
			NFDATA_TYPE varType) {
		mbPublic = false;
		mbPrivate = false;
		mbSave = false;
		mbCache = false;
		mbRef = false;
		mbUpload = false;

		mSelf = self;

		msPropertyName = strPropertyName;
		eType = varType;
	}

	@Override
	public void SetValue(NFData xData) {
		// TODO Auto-generated method stub
		if (eType != xData.GetType()) {
			return;
		}

		if (xData.IsNullValue()) {
			return;
		}

		if (null == mxData) {
			mxData = new NFData(xData);
		}

		if (mtPropertyCallback.size() == 0) {
			mxData.variantData = xData.variantData;
		} else {
			NFData oldValue;
			oldValue = mxData;

			mxData.variantData = xData.variantData;

			NFData newValue;
			newValue = mxData;

			OnEventHandler(oldValue, newValue);
		}
	}

	@Override
	public void SetValue(NFIProperty pProperty) {
		SetValue(pProperty.GetValue());
	}

	@Override
	public boolean SetInt(int value) {

		if (eType != NFDATA_TYPE.TDATA_INT) {
			return false;
		}

		if (null == mxData) {

			if (0 == value) {
				return false;
			}

			mxData = new NFData(NFDATA_TYPE.TDATA_INT);
			mxData.SetInt(0);
		}

		if (value == mxData.GetInt()) {
			return false;
		}

		if (mtPropertyCallback.size() == 0) {
			mxData.SetInt(value);
		} else {
			NFData oldValue=new NFData(NFDATA_TYPE.TDATA_INT);
			oldValue.SetInt(mxData.GetInt());
			
			mxData.SetInt(value);

			OnEventHandler(oldValue, mxData);
		}

		return true;
	}

	@Override
	public boolean SetFloat(double value) {
		if (eType != NFDATA_TYPE.TDATA_FLOAT) {
			return false;
		}

		if (null == mxData) {

			if (0 == value) {
				return false;
			}

			mxData = new NFData(NFDATA_TYPE.TDATA_FLOAT);
			mxData.SetFloat(0.0);
		}

		if (NFPlatform.IsZeroDouble(value - mxData.GetFloat())) {
			return false;
		}

		if (mtPropertyCallback.size() == 0) {
			mxData.SetFloat(value);
		} else {
			NFData oldValue=new NFData(NFDATA_TYPE.TDATA_FLOAT);
			oldValue.SetFloat(mxData.GetFloat());
			
			mxData.SetFloat(value);

			OnEventHandler(oldValue, mxData);
		}

		return true;
	}

	@Override
	public boolean SetString(String value) {
		if (eType != NFDATA_TYPE.TDATA_STRING) {
			return false;
		}

		if (null == mxData) {

			if (value.isEmpty()) {
				return false;
			}

			mxData = new NFData(NFDATA_TYPE.TDATA_STRING);
			mxData.SetString(null);
		}

		if (value.equals(mxData.GetString())) {
			return false;
		}

		if (mtPropertyCallback.size() == 0) {
			mxData.SetString(value);
		} else {
			NFData oldValue=new NFData(NFDATA_TYPE.TDATA_STRING);
			oldValue.SetString(mxData.GetString());
			
			mxData.SetString(value);

			OnEventHandler(oldValue, mxData);
		}

		return true;
	}

	@Override
	public boolean SetObject(NFGUID value) {
		if (eType != NFDATA_TYPE.TDATA_OBJECT) {
			return false;
		}

		if (null == mxData) {

			if (value.IsNull()) {
				return false;
			}

			mxData = new NFData(NFDATA_TYPE.TDATA_OBJECT);
			mxData.SetObject(null);
		}

		if (value.equals(mxData.GetObject())) {
			return false;
		}

		if (mtPropertyCallback.size() == 0) {
			mxData.SetObject(value);
		} else {
			NFData oldValue=new NFData(NFDATA_TYPE.TDATA_OBJECT);
			oldValue.SetObject(mxData.GetObject());
			
			mxData.SetObject(value);

			OnEventHandler(oldValue, mxData);
		}

		return true;
	}

	@Override
	public boolean SetVector2(NFVector2 value) {
		if (eType != NFDATA_TYPE.TDATA_VECTOR2) {
			return false;
		}

		if (null == mxData) {

			if (value == null) {
				return false;
			}

			mxData = new NFData(NFDATA_TYPE.TDATA_VECTOR2);
			mxData.SetVector2(null);
		}

		if (value.oper_heq(mxData.GetVector2())) {
			return false;
		}

		if (mtPropertyCallback.size() == 0) {
			mxData.SetVector2(value);
		} else {
			NFData oldValue=new NFData(NFDATA_TYPE.TDATA_VECTOR2);
			oldValue.SetVector2(mxData.GetVector2());
			
			mxData.SetVector2(value);

			OnEventHandler(oldValue, mxData);
		}

		return true;
	}

	@Override
	public boolean SetVector3(NFVector3 value) {
		if (eType != NFDATA_TYPE.TDATA_VECTOR3) {
			return false;
		}

		if (null == mxData) {

			if (value == null) {
				return false;
			}

			mxData = new NFData(NFDATA_TYPE.TDATA_VECTOR3);
			mxData.SetVector3(null);
		}

		if (value.oper_heq(mxData.GetVector3())) {
			return false;
		}

		if (mtPropertyCallback.size() == 0) {
			mxData.SetVector3(value);
		} else {
			NFData oldValue=new NFData(NFDATA_TYPE.TDATA_VECTOR3);
			oldValue.SetVector3(mxData.GetVector3());
			
			mxData.SetVector3(value);

			OnEventHandler(oldValue, mxData);
		}

		return true;
	}

	@Override
	public NFDATA_TYPE GetType() {
		// TODO Auto-generated method stub
		return eType;
	}

	@Override
	public boolean GeUsed() {
		if (mxData != null) {
			return true;
		}

		return false;
	}

	@Override
	public String GetKey() {
		// TODO Auto-generated method stub
		return msPropertyName;
	}

	@Override
	public boolean GetSave() {
		// TODO Auto-generated method stub
		return mbSave;
	}

	@Override
	public boolean GetPublic() {
		// TODO Auto-generated method stub
		return mbPrivate;
	}

	@Override
	public boolean GetPrivate() {
		// TODO Auto-generated method stub
		return mbPrivate;
	}

	@Override
	public boolean GetCache() {
		// TODO Auto-generated method stub
		return mbCache;
	}

	@Override
	public boolean GetRef() {
		// TODO Auto-generated method stub
		return mbRef;
	}

	@Override
	public boolean GetUpload() {
		// TODO Auto-generated method stub
		return mbUpload;
	}

	@Override
	public void SetSave(boolean bSave) {
		// TODO Auto-generated method stub
		mbSave = bSave;
	}

	@Override
	public void SetPublic(boolean bPublic) {
		// TODO Auto-generated method stub
		mbPublic = bPublic;
	}

	@Override
	public void SetPrivate(boolean bPrivate) {
		// TODO Auto-generated method stub
		mbPrivate = bPrivate;
	}

	@Override
	public void SetCache(boolean bCache) {
		// TODO Auto-generated method stub
		mbCache = bCache;
	}

	@Override
	public void SetRef(boolean bRef) {
		// TODO Auto-generated method stub
		mbRef = bRef;
	}

	@Override
	public void SetUpload(boolean bUpload) {
		// TODO Auto-generated method stub
		mbUpload = bUpload;
	}

	@Override
	public int GetInt() {
		if (null == mxData) {
			return 0;
		}

		return mxData.GetInt();
	}

	@Override
	public double GetFloat() {
		if (null == mxData) {
			return 0.0;
		}

		return mxData.GetFloat();
	}

	@Override
	public String GetString() {
		if (null == mxData) {
			return null;
		}

		return mxData.GetString();
	}

	@Override
	public NFGUID GetObject() {
		if (null == mxData) {
			return null;
		}

		return mxData.GetObject();
	}

	@Override
	public NFVector2 GetVector2() {
		if (null == mxData) {
			return null;
		}

		return mxData.GetVector2();
	}

	@Override
	public NFVector3 GetVector3() {
		if (null == mxData) {
			return null;
		}

		return mxData.GetVector3();
	}

	@Override
	public NFData GetValue() {
		if (null == mxData) {
			return null;
		}

		return mxData;
	}

	@Override
	public List<String> GetEmbeddedList() {
		// TODO Auto-generated method stub
		return this.mxEmbeddedList;
	}

	@Override
	public NFMapEx<String, String> GetEmbeddedMap() {
		// TODO Auto-generated method stub
		return this.mxEmbeddedMap;
	}

	@Override
	public boolean Changed() {
		// TODO Auto-generated method stub
		return !(GetValue().IsNullValue());
	}

	@Override
	public String ToString() {
		String strData;
		NFDATA_TYPE eType = GetType();
		switch (eType) {
		case TDATA_INT:
			strData = String.valueOf(GetInt());
			break;
		case TDATA_FLOAT:
			strData = String.valueOf(GetFloat());
			break;
		case TDATA_STRING:
			strData = GetString();
			break;
		case TDATA_OBJECT:
			strData = GetObject().ToString();
			break;
		case TDATA_VECTOR2:
			strData = GetVector2().ToString();
			break;
		case TDATA_VECTOR3:
			strData = GetVector3().ToString();
			break;
		default:
			strData = null;
			break;
		}

		return strData;
	}

	@Override
	public boolean FromString(String strData) {
		NFDATA_TYPE eType = GetType();
		boolean bRet = false;
		switch (eType) {
		case TDATA_INT: {
			int nValue = 0;
			bRet = NFPlatform.NF_StrTo(strData, nValue);
			SetInt(nValue);
		}
			break;

		case TDATA_FLOAT: {
			double dValue = 0;
			bRet = NFPlatform.NF_StrTo(strData, dValue);
			SetFloat(dValue);
		}
			break;

		case TDATA_STRING: {
			SetString(strData);
			bRet = true;
		}
			break;
		case TDATA_OBJECT: {
			NFGUID xID = new NFGUID();
			bRet = xID.FromString(strData);
			SetObject(xID);
		}
			break;
		case TDATA_VECTOR2: {
			NFVector2 xVector2 = new NFVector2();

			bRet = xVector2.FromString(strData);
			SetVector2(xVector2);
		}
			break;
		case TDATA_VECTOR3: {
			NFVector3 xVector3 = new NFVector3();

			bRet = xVector3.FromString(strData);
			SetVector3(xVector3);
		}
			break;
		default:
			break;
		}

		return bRet;
	}

	@Override
	public boolean DeSerialization() {
		boolean bRet = false;

		NFDATA_TYPE eType = GetType();
		if (eType == NFDATA_TYPE.TDATA_STRING && null != mxData
				&& !mxData.IsNullValue()) {
			NFDataList xDataList=new NFDataList();
			String strData = mxData.GetString();

			xDataList.Split(strData, ";");
			if (xDataList.GetCount() <= 0) {
				return bRet;
			}

			NFDataList xTemDataList = new NFDataList();
			xTemDataList.Split(xDataList.String(0), ",");
			int nSubDataLength = xTemDataList.GetCount();

			if (xDataList.GetCount() == 1 && nSubDataLength == 1) {
				// most of property value only one value
				return bRet;
			}

			if (null == mxEmbeddedList) {
				mxEmbeddedList = new ArrayList<String>();
			} else {
				mxEmbeddedList.clear();
			}

			for (int i = 0; i < xDataList.GetCount(); ++i) {
				if (xDataList.String(i).isEmpty()) {
					// NFASSERT(0, strData, "NFProperty", "DeSerialization");
				}

				mxEmbeddedList.add(xDataList.String(i));
			}

			// //////////////////////////////////////

			if (nSubDataLength < 2 || nSubDataLength > 2) {
				return bRet;
			}

			if (null == mxEmbeddedMap) {
				mxEmbeddedMap = new NFMapEx<String, String>() {
				};
			} else {
				mxEmbeddedMap.ClearAll();
			}

			for (int i = 0; i < xDataList.GetCount(); ++i) {
				xTemDataList = new NFDataList();
				String strTemData = xDataList.String(i);
				xTemDataList.Split(strTemData, ",");
				{
					if (xTemDataList.GetCount() != nSubDataLength) {
						// NFASSERT(0, strTemData, __FILE__, __FUNCTION__);
					}

					String strKey = xTemDataList.String(0);
					String strValue = xTemDataList.String(1);

					if (strKey.isEmpty() || strValue.isEmpty()) {
						// NFASSERT(0, strTemData, __FILE__, __FUNCTION__);
					}

					mxEmbeddedMap.AddElement(strKey, strValue);
				}
			}

			bRet = true;
		}

		return bRet;
	}

	@Override
	public void RegisterCallback(PROPERTY_EVENT_FUNCTOR cb) {
		// TODO Auto-generated method stub
		mtPropertyCallback.add(cb);
	}

	private int OnEventHandler(NFData oldVar, NFData newVar) {
		if (mtPropertyCallback.size() <= 0) {
			return 0;
		}

		for (int i = 0; i < mtPropertyCallback.size(); i++) {
			PROPERTY_EVENT_FUNCTOR pFunc = mtPropertyCallback.get(i);

			int nTemRet = pFunc.operator(mSelf, msPropertyName,
					oldVar, newVar);
		}

		return 0;
	}

}
