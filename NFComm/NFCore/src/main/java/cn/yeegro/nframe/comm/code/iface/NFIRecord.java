/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.iface;


import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.util.NFDATA_TYPE;
import cn.yeegro.nframe.comm.code.util.NFData;
import cn.yeegro.nframe.comm.code.util.NFDataList;
import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.functor.RECORD_EVENT_FUNCTOR;

import java.util.List;


public abstract class NFIRecord {
	

	public abstract boolean IsUsed(int nRow);

	public abstract boolean SetUsed(int nRow, int bUse);

	public abstract int GetCols();

	public abstract int GetRows();

	public abstract NFDATA_TYPE GetColType(int nCol);

	public abstract String GetColTag(int nCol);

	public abstract int AddRow(int nRow);

	public abstract int AddRow(int nRow, NFDataList var);

	public abstract boolean SetInt(int nRow, int nCol, int value);

	public abstract boolean SetFloat(int nRow, int nCol, double value);

	public abstract boolean SetString(int nRow, int nCol, String value);

	public abstract boolean SetObject(int nRow, int nCol, NFGUID value);

	public abstract boolean SetVector2(int nRow, int nCol, NFVector2 value);

	public abstract boolean SetVector3(int nRow, int nCol, NFVector3 value);

	public abstract boolean SetInt(int nRow, String strColTag, int value);

	public abstract boolean SetFloat(int nRow, String strColTag, double value);

	public abstract boolean SetString(int nRow, String strColTag, String value);

	public abstract boolean SetObject(int nRow, String strColTag, NFGUID value);

	public abstract boolean SetVector2(int nRow, String strColTag, NFVector2 value);

	public abstract boolean SetVector3(int nRow, String strColTag, NFVector3 value);

	public abstract boolean QueryRow(int nRow, NFDataList varList);

	public abstract boolean SwapRowInfo(int nOriginRow, int nTargetRow);

	public abstract int GetInt(int nRow, int nCol);

	public abstract double GetFloat(int nRow, int nCol);

	public abstract String GetString(int nRow, int nCol);

	public abstract NFGUID GetObject(int nRow, int nCol);

	public abstract NFVector2 GetVector2(int nRow, int nCol);

	public abstract NFVector3 GetVector3(int nRow, int nCol);

	public abstract int GetInt(int nRow, String strColTag);

	public abstract double GetFloat(int nRow, String strColTag);

	public abstract String GetString(int nRow, String strColTag);

	public abstract NFGUID GetObject(int nRow, String strColTag);

	public abstract NFVector2 GetVector2(int nRow, String strColTag);

	public abstract NFVector3 GetVector3(int nRow, String strColTag);

	public abstract int FindRowByColValue(int nCol, NFDataList var,
										  NFDataList varResult);

	public abstract int FindInt(int nCol, int value, NFDataList varResult);

	public abstract int FindFloat(int nCol, double value, NFDataList varResult);

	public abstract int FindString(int nCol, String value, NFDataList varResult);

	public abstract int FindObject(int nCol, NFGUID value, NFDataList varResult);

	public abstract int FindVector2(int nCol, NFVector2 value, NFDataList varResult);

	public abstract int FindVector3(int nCol, NFVector3 value, NFDataList varResult);

	public int SortByCol(int nCol, boolean bOrder, NFDataList varResult) {
		return 0;
	};

	public abstract int FindRowByColValue(String strColTag, NFDataList var,
										  NFDataList varResult);

	public abstract int FindInt(String strColTag, int value, NFDataList varResult);

	public abstract int FindFloat(String strColTag, double value, NFDataList varResult);

	public abstract int FindString(String strColTag, String value,
								   NFDataList varResult);

	public abstract int FindObject(String strColTag, NFGUID value,
								   NFDataList varResult);

	public abstract int FindVector2(String strColTag, NFVector2 value,
									NFDataList varResult);

	public abstract int FindVector3(String strColTag, NFVector3 value,
									NFDataList varResult);

	public int SortByTag(String strColTag, boolean bOrder, NFDataList varResult) {
		return 0;
	};

	public abstract boolean Remove(int nRow);

	public boolean Remove(NFDataList varRows) // need to optimize
	{
		for (int i = 0; i < varRows.GetCount(); ++i) {
			Remove((int) varRows.Int(i));
		}

		return true;
	}

	public abstract boolean Clear();

	public abstract void AddRecordHook(RECORD_EVENT_FUNCTOR cb);

	public abstract boolean GetSave();

	public abstract boolean GetPublic();

	public abstract boolean GetPrivate();

	public abstract boolean GetCache();

	public abstract boolean GetUpload();

	public abstract String GetName();

	public abstract NFDataList GetInitData();

	public abstract NFDataList GetTag();

	public abstract void SetSave(boolean bSave);

	public abstract void SetCache(boolean bCache);

	public abstract void SetUpload(boolean bUpload);

	public abstract void SetPublic(boolean bPublic);

	public abstract void SetPrivate(boolean bPrivate);

	public abstract void SetName(String strName);

	public abstract List<NFData> GetRecordVec();

}
