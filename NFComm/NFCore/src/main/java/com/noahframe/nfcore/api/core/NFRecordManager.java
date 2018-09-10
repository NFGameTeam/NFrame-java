/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.api.core;


import com.noahframe.nfcore.iface.module.NFIRecord;

import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.math.NFVector2;
import com.noahframe.nfcore.iface.math.NFVector3;
import com.noahframe.nfcore.iface.module.NFIRecordManager;
import com.noahframe.nfcore.iface.util.NFDataList;

public class NFRecordManager extends NFIRecordManager {

	private NFGUID mSelf;

	public NFRecordManager(NFGUID self) {
		mSelf = self;
	}

	@Override
	public NFIRecord AddRecord(NFGUID self, String strRecordName,
							   NFDataList ValueList, NFDataList tagList, int nRows) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord == null) {
			pRecord = new NFRecord(self, strRecordName, ValueList, tagList,
					nRows);
			this.AddElement(strRecordName, pRecord);
		}
		return pRecord;
	}

	@Override
	public NFGUID Self() {
		// TODO Auto-generated method stub
		return mSelf;
	}

	@Override
	public boolean SetRecordInt(String strRecordName, int nRow, int nCol,
			int nValue) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetInt(nRow, nCol, nValue);
		}

		return false;
	}

	@Override
	public boolean SetRecordFloat(String strRecordName, int nRow, int nCol,
			double dwValue) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetFloat(nRow, nCol, dwValue);
		}

		return false;
	}

	@Override
	public boolean SetRecordString(String strRecordName, int nRow, int nCol,
			String strValue) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetString(nRow, nCol, strValue);
		}

		return false;
	}

	@Override
	public boolean SetRecordObject(String strRecordName, int nRow, int nCol,
								   NFGUID obj) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetObject(nRow, nCol, obj);
		}

		return false;
	}

	@Override
	public boolean SetRecordVector2(String strRecordName, int nRow, int nCol,
			NFVector2 obj) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetVector2(nRow, nCol, obj);
		}

		return false;
	}

	@Override
	public boolean SetRecordVector3(String strRecordName, int nRow, int nCol,
			NFVector3 obj) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetVector3(nRow, nCol, obj);
		}

		return false;
	}

	@Override
	public boolean SetRecordInt(String strRecordName, int nRow,
			String strColTag, int value) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetInt(nRow, strColTag, value);
		}
		return false;
	}

	@Override
	public boolean SetRecordFloat(String strRecordName, int nRow,
			String strColTag, double value) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetFloat(nRow, strColTag, value);
		}
		return false;
	}

	@Override
	public boolean SetRecordString(String strRecordName, int nRow,
			String strColTag, String value) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetString(nRow, strColTag, value);
		}
		return false;
	}

	@Override
	public boolean SetRecordObject(String strRecordName, int nRow,
			String strColTag, NFGUID value) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetObject(nRow, strColTag, value);
		}
		return false;
	}

	@Override
	public boolean SetRecordVector2(String strRecordName, int nRow,
			String strColTag, NFVector2 value) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetVector2(nRow, strColTag, value);
		}
		return false;
	}

	@Override
	public boolean SetRecordVector3(String strRecordName, int nRow,
			String strColTag, NFVector3 value) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.SetVector3(nRow, strColTag, value);
		}
		return false;
	}

	@Override
	public int GetRecordInt(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetInt(nRow, nCol);
		}

		return 0;
	}

	@Override
	public double GetRecordFloat(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetFloat(nRow, nCol);
		}

		return 0.0;
	}

	@Override
	public String GetRecordString(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetString(nRow, nCol);
		}
		
		return null;
	}

	@Override
	public NFGUID GetRecordObject(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetObject(nRow, nCol);
		}
		
		return null;
	}

	@Override
	public NFVector2 GetRecordVector2(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetVector2(nRow, nCol);
		}
		
		return null;
	}

	@Override
	public NFVector3 GetRecordVector3(String strRecordName, int nRow, int nCol) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetVector3(nRow, nCol);
		}
		
		return null;
	}

	@Override
	public int GetRecordInt(String strRecordName, int nRow, String strColTag) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetInt(nRow, strColTag);
		}
		
		return 0;
	}

	@Override
	public double GetRecordFloat(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetFloat(nRow, strColTag);
		}
		return 0.0;
	}

	@Override
	public String GetRecordString(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetString(nRow, strColTag);
		}
		return null;
	}

	@Override
	public NFGUID GetRecordObject(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetObject(nRow, strColTag);
		}
		return null;
	}

	@Override
	public NFVector2 GetRecordVector2(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetVector2(nRow, strColTag);
		}
		return null;
	}

	@Override
	public NFVector3 GetRecordVector3(String strRecordName, int nRow,
			String strColTag) {
		NFIRecord pRecord = GetElement(strRecordName);
		if (pRecord != null) {
			return pRecord.GetVector3(nRow, strColTag);
		}
		return null;
	}
}
