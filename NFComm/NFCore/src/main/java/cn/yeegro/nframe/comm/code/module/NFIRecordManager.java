/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.module;


import cn.yeegro.nframe.comm.code.iface.NFIRecord;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.util.NFDataList;
import cn.yeegro.nframe.comm.code.util.NFMapEx;
import cn.yeegro.nframe.comm.code.api.NFGUID;

public abstract class NFIRecordManager extends NFMapEx<String, NFIRecord> {

	public abstract NFIRecord AddRecord(NFGUID self, String strRecordName,
                                        NFDataList TData, NFDataList tagData, int nRows);

	public abstract NFGUID Self();

	// ////////////////////////////////////////////////////////////////////////

	public abstract boolean SetRecordInt(String strRecordName, int nRow,
			int nCol, int nValue);

	public abstract boolean SetRecordFloat(String strRecordName, int nRow,
			int nCol, double dwValue);

	public abstract boolean SetRecordString(String strRecordName, int nRow,
			int nCol, String strValue);

	public abstract boolean SetRecordObject(String strRecordName, int nRow,
			int nCol, NFGUID obj);

	public abstract boolean SetRecordVector2(String strRecordName, int nRow,
			int nCol, NFVector2 obj);

	public abstract boolean SetRecordVector3(String strRecordName, int nRow,
			int nCol, NFVector3 obj);

	public abstract boolean SetRecordInt(String strRecordName, int nRow,
			String strColTag, int value);

	public abstract boolean SetRecordFloat(String strRecordName, int nRow,
			String strColTag, double value);

	public abstract boolean SetRecordString(String strRecordName, int nRow,
			String strColTag, String value);

	public abstract boolean SetRecordObject(String strRecordName, int nRow,
			String strColTag, NFGUID value);

	public abstract boolean SetRecordVector2(String strRecordName, int nRow,
			String strColTag, NFVector2 value);

	public abstract boolean SetRecordVector3(String strRecordName, int nRow,
			String strColTag, NFVector3 value);

	public abstract int GetRecordInt(String strRecordName, int nRow, int nCol);

	public abstract double GetRecordFloat(String strRecordName, int nRow,
			int nCol);

	public abstract String GetRecordString(String strRecordName, int nRow,
			int nCol);

	public abstract NFGUID GetRecordObject(String strRecordName, int nRow,
			int nCol);

	public abstract NFVector2 GetRecordVector2(String strRecordName, int nRow,
			int nCol);

	public abstract NFVector3 GetRecordVector3(String strRecordName, int nRow,
			int nCol);

	public abstract int GetRecordInt(String strRecordName, int nRow,
			String strColTag);

	public abstract double GetRecordFloat(String strRecordName, int nRow,
			String strColTag);

	public abstract String GetRecordString(String strRecordName, int nRow,
			String strColTag);

	public abstract NFGUID GetRecordObject(String strRecordName, int nRow,
			String strColTag);

	public abstract NFVector2 GetRecordVector2(String strRecordName, int nRow,
			String strColTag);

	public abstract NFVector3 GetRecordVector3(String strRecordName, int nRow,
			String strColTag);

}
