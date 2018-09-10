/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.api.core;



import com.noahframe.nfcore.iface.functor.RECORD_EVENT_DATA;
import com.noahframe.nfcore.iface.functor.RECORD_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.module.NFIRecord;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.math.NFVector2;
import com.noahframe.nfcore.iface.math.NFVector3;
import com.noahframe.nfcore.iface.util.NFDATA_TYPE;
import com.noahframe.nfcore.iface.util.NFData;
import com.noahframe.nfcore.iface.util.NFDataList;

import java.util.List;
import java.util.Map;



public class NFRecord extends NFIRecord {

	NFDataList mVarRecordType;
	NFDataList mVarRecordTag;

	Map<String, Integer> mmTag;

	// //////////////////////////

	List<NFData> mtRecordVec;
	List<Integer> mVecUsedState;
	int mnMaxRow;

	NFGUID mSelf;
	boolean mbSave;
	boolean mbPublic;
	boolean mbPrivate;
	boolean mbCache;
	boolean mbUpload;
	String mstrRecordName;

	List<RECORD_EVENT_FUNCTOR> mtRecordCallback;

	public NFRecord() {

		mSelf = new NFGUID();

		mbSave = false;
		mbPublic = false;
		mbPrivate = false;
		mbCache = false;
		mbUpload = false;

		mstrRecordName = "";
		mnMaxRow = 0;
	}

	public NFRecord(NFGUID self, String strRecordName, NFDataList valueList,
			NFDataList tagList, int nMaxRow) {

		mVarRecordType = valueList;
		mVarRecordTag = tagList;

		mSelf = self;

		mbSave = false;
		mbPublic = false;
		mbPrivate = false;
		mbCache = false;
		mbUpload = false;

		mstrRecordName = strRecordName;

		mnMaxRow = nMaxRow;

		for (int i = 0; i < mnMaxRow; i++) {
			mVecUsedState.add(i, 0);
		}

		// init share_pointer for all data
		for (int i = 0; i < GetRows() * GetCols(); i++) {
			mtRecordVec.add(new NFData());
		}

		// optimize would be better, it should be applied memory space only once
		for (int i = 0; i < mVarRecordTag.GetCount(); ++i) {
			if (!mVarRecordTag.String(i).isEmpty()) {
				mmTag.put(mVarRecordTag.String(i), i);
			}
		}
	}

	@Override
	public boolean IsUsed(int nRow) {
		if (ValidRow(nRow)) {
			return (mVecUsedState.get(nRow) > 0);
		}
		return false;
	}

	@Override
	public boolean SetUsed(int nRow, int bUse) {
		if (ValidRow(nRow)) {
			mVecUsedState.add(nRow, bUse);
			return true;
		}
		return false;
	}

	@Override
	public int GetCols() {
		// TODO Auto-generated method stub
		return mVarRecordType.GetCount();
	}

	@Override
	public int GetRows() {
		// TODO Auto-generated method stub
		return mnMaxRow;
	}

	@Override
	public NFDATA_TYPE GetColType(int nCol) {
		// TODO Auto-generated method stub
		return mVarRecordType.Type(nCol);
	}

	@Override
	public String GetColTag(int nCol) {
		// TODO Auto-generated method stub
		return mVarRecordType.String(nCol);
	}

	@Override
	public int AddRow(int nRow) {
		// TODO Auto-generated method stub
		return AddRow(nRow, mVarRecordType);
	}

	@Override
	public int AddRow(int nRow, NFDataList var) {
		boolean bCover = false;
		int nFindRow = nRow;
		if (nFindRow >= mnMaxRow) {
			return -1;
		}

		if (var.GetCount() != GetCols()) {
			return -1;
		}

		if (nFindRow < 0) {
			for (int i = 0; i < mnMaxRow; i++) {
				if (!IsUsed(i)) {
					nFindRow = i;
					break;
				}
			}
		} else {
			if (IsUsed(nFindRow)) {
				bCover = true;
			}
		}

		if (nFindRow < 0) {
			return -1;
		}

		for (int i = 0; i < GetCols(); ++i) {
			if (var.Type(i) != GetColType(i)) {
				return -1;
			}
		}

		SetUsed(nFindRow, 1);

		for (int i = 0; i < GetCols(); ++i) {
			NFData pVar = mtRecordVec.get(GetPos(nFindRow, i));
			if (null == pVar) {
				pVar = new NFData(var.Type(i));
			}

			pVar.variantData = var.GetStack(i).variantData;
		}

		RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
		xEventData.nOpType = bCover ? RECORD_EVENT_DATA.RecordOptype.Cover.value()
				: RECORD_EVENT_DATA.RecordOptype.Add.value();
		xEventData.nRow = nFindRow;
		xEventData.nCol = 0;
		xEventData.strRecordName = mstrRecordName;

		NFData tData = new NFData();
		OnEventHandler(mSelf, xEventData, tData, tData); // FIXME:RECORD

		return nFindRow;
	}

	@Override
	public boolean SetInt(int nRow, int nCol, int value) {
		if (!ValidPos(nRow, nCol)) {
			return false;
		}

		if (NFDATA_TYPE.TDATA_INT != GetColType(nCol)) {
			return false;
		}

		if (!IsUsed(nRow)) {
			return false;
		}

		NFData var = new NFData();
		var.SetInt(value);

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		// must have memory
		if (null == pVar) {
			return false;
		}

		if (var.oper_heq(pVar)) {
			return false;
		}

		if (mtRecordCallback.size() == 0) {
			pVar.variantData = value;
		} else {
			NFData oldValue = new NFData();
			oldValue.SetInt(pVar.GetInt());

			pVar.variantData = value;

			RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
			xEventData.nOpType = RECORD_EVENT_DATA.RecordOptype.Update.value();
			xEventData.nRow = nRow;
			xEventData.nCol = nCol;
			xEventData.strRecordName = mstrRecordName;

			OnEventHandler(mSelf, xEventData, oldValue, pVar);
		}

		return true;
	}

	@Override
	public boolean SetFloat(int nRow, int nCol, double value) {
		if (!ValidPos(nRow, nCol)) {
			return false;
		}

		if (NFDATA_TYPE.TDATA_FLOAT != GetColType(nCol)) {
			return false;
		}

		if (!IsUsed(nRow)) {
			return false;
		}

		NFData var = new NFData();
		var.SetFloat(value);

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		// must have memory
		if (null == pVar) {
			return false;
		}

		if (var.oper_heq(pVar)) {
			return false;
		}

		if (mtRecordCallback.size() == 0) {
			pVar.variantData = value;
		} else {
			NFData oldValue = new NFData();
			oldValue.SetFloat(pVar.GetFloat());

			pVar.variantData = value;

			RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
			xEventData.nOpType = RECORD_EVENT_DATA.RecordOptype.Update.value();
			xEventData.nRow = nRow;
			xEventData.nCol = nCol;
			xEventData.strRecordName = mstrRecordName;

			OnEventHandler(mSelf, xEventData, oldValue, pVar);
		}

		return true;
	}

	@Override
	public boolean SetString(int nRow, int nCol, String value) {
		if (!ValidPos(nRow, nCol)) {
			return false;
		}

		if (NFDATA_TYPE.TDATA_STRING != GetColType(nCol)) {
			return false;
		}

		if (!IsUsed(nRow)) {
			return false;
		}

		NFData var = new NFData();
		var.SetString(value);

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		// must have memory
		if (null == pVar) {
			return false;
		}

		if (var.oper_heq(pVar)) {
			return false;
		}

		if (mtRecordCallback.size() == 0) {
			pVar.variantData = value;
		} else {
			NFData oldValue = new NFData();
			oldValue.SetString(pVar.GetString());

			pVar.variantData = value;

			RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
			xEventData.nOpType = RECORD_EVENT_DATA.RecordOptype.Update.value();
			xEventData.nRow = nRow;
			xEventData.nCol = nCol;
			xEventData.strRecordName = mstrRecordName;

			OnEventHandler(mSelf, xEventData, oldValue, pVar);
		}

		return true;
	}

	@Override
	public boolean SetObject(int nRow, int nCol, NFGUID value) {
		if (!ValidPos(nRow, nCol)) {
			return false;
		}

		if (NFDATA_TYPE.TDATA_OBJECT != GetColType(nCol)) {
			return false;
		}

		if (!IsUsed(nRow)) {
			return false;
		}

		NFData var = new NFData();
		var.SetObject(value);

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		// must have memory
		if (null == pVar) {
			return false;
		}

		if (var.oper_heq(pVar)) {
			return false;
		}

		if (mtRecordCallback.size() == 0) {
			pVar.variantData = value;
		} else {
			NFData oldValue = new NFData();
			oldValue.SetObject(pVar.GetObject());

			pVar.variantData = value;

			RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
			xEventData.nOpType = RECORD_EVENT_DATA.RecordOptype.Update.value();
			xEventData.nRow = nRow;
			xEventData.nCol = nCol;
			xEventData.strRecordName = mstrRecordName;

			OnEventHandler(mSelf, xEventData, oldValue, pVar);
		}

		return true;
	}

	@Override
	public boolean SetVector2(int nRow, int nCol, NFVector2 value) {
		if (!ValidPos(nRow, nCol)) {
			return false;
		}

		if (NFDATA_TYPE.TDATA_VECTOR2 != GetColType(nCol)) {
			return false;
		}

		if (!IsUsed(nRow)) {
			return false;
		}

		NFData var = new NFData();
		var.SetVector2(value);

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		// must have memory
		if (null == pVar) {
			return false;
		}

		if (var.oper_heq(pVar)) {
			return false;
		}

		if (mtRecordCallback.size() == 0) {
			pVar.variantData = value;
		} else {
			NFData oldValue = new NFData();
			oldValue.SetVector2(pVar.GetVector2());

			pVar.variantData = value;

			RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
			xEventData.nOpType = RECORD_EVENT_DATA.RecordOptype.Update.value();
			xEventData.nRow = nRow;
			xEventData.nCol = nCol;
			xEventData.strRecordName = mstrRecordName;

			OnEventHandler(mSelf, xEventData, oldValue, pVar);
		}

		return true;
	}

	@Override
	public boolean SetVector3(int nRow, int nCol, NFVector3 value) {
		if (!ValidPos(nRow, nCol)) {
			return false;
		}

		if (NFDATA_TYPE.TDATA_VECTOR3 != GetColType(nCol)) {
			return false;
		}

		if (!IsUsed(nRow)) {
			return false;
		}

		NFData var = new NFData();
		var.SetVector3(value);

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		// must have memory
		if (null == pVar) {
			return false;
		}

		if (var.oper_heq(pVar)) {
			return false;
		}

		if (mtRecordCallback.size() == 0) {
			pVar.variantData = value;
		} else {
			NFData oldValue = new NFData();
			oldValue.SetVector3(pVar.GetVector3());

			pVar.variantData = value;

			RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
			xEventData.nOpType = RECORD_EVENT_DATA.RecordOptype.Update.value();
			xEventData.nRow = nRow;
			xEventData.nCol = nCol;
			xEventData.strRecordName = mstrRecordName;

			OnEventHandler(mSelf, xEventData, oldValue, pVar);
		}

		return true;
	}

	@Override
	public boolean SetInt(int nRow, String strColTag, int value) {
		int nCol = GetCol(strColTag);
		return SetInt(nRow, nCol, value);
	}

	@Override
	public boolean SetFloat(int nRow, String strColTag, double value) {
		int nCol = GetCol(strColTag);
		return SetFloat(nRow, nCol, value);
	}

	@Override
	public boolean SetString(int nRow, String strColTag, String value) {
		int nCol = GetCol(strColTag);
		return SetString(nRow, nCol, value);
	}

	@Override
	public boolean SetObject(int nRow, String strColTag, NFGUID value) {
		int nCol = GetCol(strColTag);
		return SetObject(nRow, nCol, value);
	}

	@Override
	public boolean SetVector2(int nRow, String strColTag, NFVector2 value) {
		int nCol = GetCol(strColTag);
		return SetVector2(nRow, nCol, value);
	}

	@Override
	public boolean SetVector3(int nRow, String strColTag, NFVector3 value) {
		int nCol = GetCol(strColTag);
		return SetVector3(nRow, nCol, value);
	}

	@Override
	public boolean QueryRow(int nRow, NFDataList varList) {
		if (!ValidRow(nRow)) {
			return false;
		}

		if (!IsUsed(nRow)) {
			return false;
		}

		varList.Clear();
		for (int i = 0; i < GetCols(); ++i) {
			NFData pVar = mtRecordVec.get(GetPos(nRow, i));
			if (pVar != null) {
				varList.Append(pVar);
			} else {
				switch (GetColType(i)) {
				case TDATA_INT:
					varList.Add(0);
					break;

				case TDATA_FLOAT:
					varList.Add(0.0f);
					break;

				case TDATA_STRING:
					varList.Add("");
					break;

				case TDATA_OBJECT:
					varList.Add(new NFGUID());
					break;

				case TDATA_VECTOR2:
					varList.Add(new NFVector2());
					break;

				case TDATA_VECTOR3:
					varList.Add(new NFVector3());
					break;
				default:
					return false;
				}
			}
		}

		if (varList.GetCount() != GetCols()) {
			return false;
		}

		return true;
	}

	@Override
	public boolean SwapRowInfo(int nOriginRow, int nTargetRow) {
		if (!IsUsed(nOriginRow)) {
			return false;
		}

		if (ValidRow(nOriginRow) && ValidRow(nTargetRow)) {
			for (int i = 0; i < GetCols(); ++i) {
				NFData pOrigin = mtRecordVec.get(GetPos(nOriginRow, i));
				mtRecordVec.add(GetPos(nOriginRow, i),
						mtRecordVec.get(GetPos(nTargetRow, i)));
				mtRecordVec.add(GetPos(nTargetRow, i), pOrigin);
			}

			int nOriginUse = mVecUsedState.get(nOriginRow);
			mVecUsedState.add(nOriginRow, mVecUsedState.get(nTargetRow));
			mVecUsedState.add(nTargetRow, nOriginUse);

			RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
			xEventData.nOpType = RECORD_EVENT_DATA.RecordOptype.Swap.value();
			xEventData.nRow = nOriginRow;
			xEventData.nCol = nTargetRow;
			xEventData.strRecordName = mstrRecordName;

			NFData xData = new NFData();
			OnEventHandler(mSelf, xEventData, xData, xData);

			return true;
		}

		return false;
	}

	@Override
	public int GetInt(int nRow, int nCol) {
		if (!ValidPos(nRow, nCol)) {
			return 0;
		}

		if (!IsUsed(nRow)) {
			return 0;
		}

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		if (null == pVar) {
			return 0;
		}

		return pVar.GetInt();
	}

	@Override
	public double GetFloat(int nRow, int nCol) {
		if (!ValidPos(nRow, nCol)) {
			return 0.0f;
		}

		if (!IsUsed(nRow)) {
			return 0.0f;
		}

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		if (null == pVar) {
			return 0.0f;
		}

		return pVar.GetFloat();
	}

	@Override
	public String GetString(int nRow, int nCol) {
		if (!ValidPos(nRow, nCol)) {
			return null;
		}

		if (!IsUsed(nRow)) {
			return null;
		}

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		if (null == pVar) {
			return null;
		}

		return pVar.GetString();
	}

	@Override
	public NFGUID GetObject(int nRow, int nCol) {
		if (!ValidPos(nRow, nCol)) {
			return null;
		}

		if (!IsUsed(nRow)) {
			return null;
		}

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		if (null == pVar) {
			return null;
		}

		return pVar.GetObject();
	}

	@Override
	public NFVector2 GetVector2(int nRow, int nCol) {
		if (!ValidPos(nRow, nCol)) {
			return null;
		}

		if (!IsUsed(nRow)) {
			return null;
		}

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		if (null == pVar) {
			return null;
		}

		return pVar.GetVector2();
	}

	@Override
	public NFVector3 GetVector3(int nRow, int nCol) {
		if (!ValidPos(nRow, nCol)) {
			return null;
		}

		if (!IsUsed(nRow)) {
			return null;
		}

		NFData pVar = mtRecordVec.get(GetPos(nRow, nCol));
		if (null == pVar) {
			return null;
		}

		return pVar.GetVector3();
	}

	@Override
	public int GetInt(int nRow, String strColTag) {
		int nCol = GetCol(strColTag);
		return GetInt(nRow, nCol);
	}

	@Override
	public double GetFloat(int nRow, String strColTag) {
		int nCol = GetCol(strColTag);
		return GetFloat(nRow, nCol);
	}

	@Override
	public String GetString(int nRow, String strColTag) {
		int nCol = GetCol(strColTag);
		return GetString(nRow, nCol);
	}

	@Override
	public NFGUID GetObject(int nRow, String strColTag) {
		int nCol = GetCol(strColTag);
		return GetObject(nRow, nCol);
	}

	@Override
	public NFVector2 GetVector2(int nRow, String strColTag) {
		int nCol = GetCol(strColTag);
		return GetVector2(nRow, nCol);
	}

	@Override
	public NFVector3 GetVector3(int nRow, String strColTag) {
		int nCol = GetCol(strColTag);
		return GetVector3(nRow, nCol);
	}

	@Override
	public int FindRowByColValue(int nCol, NFDataList var,
			NFDataList varResult) {
		if (!ValidCol(nCol)) {
			return -1;
		}

		NFDATA_TYPE eType = var.Type(0);
		if (eType != mVarRecordType.Type(nCol)) {
			return -1;
		}

		switch (eType) {
		case TDATA_INT:
			return FindInt(nCol, var.Int(nCol), varResult);
		case TDATA_FLOAT:
			return FindFloat(nCol, var.Float(nCol), varResult);
		case TDATA_STRING:
			return FindString(nCol, var.String(nCol), varResult);
		case TDATA_OBJECT:
			return FindObject(nCol, var.Object(nCol), varResult);
		case TDATA_VECTOR2:
			return FindVector2(nCol, var.Vector2(nCol), varResult);
		case TDATA_VECTOR3:
			return FindVector3(nCol, var.Vector3(nCol), varResult);
		default:
			break;
		}

		return -1;
	}

	@Override
	public int FindInt(int nCol, int var, NFDataList varResult) {
		if (!ValidCol(nCol)) {
			return -1;
		}

		if (NFDATA_TYPE.TDATA_INT != mVarRecordType.Type(nCol)) {
			return -1;
		}

		for (int i = 0; i < mnMaxRow; ++i) {
			if (!IsUsed(i)) {
				continue;
			}

			if (GetInt(i, nCol) == var) {
				varResult.oper_push(i);
			}
		}

		return varResult.GetCount();

	}

	@Override
	public int FindFloat(int nCol, double value, NFDataList varResult) {
		if (!ValidCol(nCol)) {
			return -1;
		}

		if (NFDATA_TYPE.TDATA_FLOAT != mVarRecordType.Type(nCol)) {
			return -1;
		}

		for (int i = 0; i < mnMaxRow; ++i) {
			if (!IsUsed(i)) {
				continue;
			}

			if (GetFloat(i, nCol) == value) {
				varResult.oper_push(i);
			}
		}

		return varResult.GetCount();
	}

	@Override
	public int FindString(int nCol, String value, NFDataList varResult) {
		if (!ValidCol(nCol)) {
			return -1;
		}

		if (NFDATA_TYPE.TDATA_STRING != mVarRecordType.Type(nCol)) {
			return -1;
		}

		for (int i = 0; i < mnMaxRow; ++i) {
			if (!IsUsed(i)) {
				continue;
			}

			String strData = GetString(i, nCol);

			if (strData.equals(value)) {
				varResult.oper_push(i);
			}
		}

		return varResult.GetCount();
	}

	@Override
	public int FindObject(int nCol, NFGUID value, NFDataList varResult) {
		if (!ValidCol(nCol)) {
			return -1;
		}

		if (NFDATA_TYPE.TDATA_OBJECT != mVarRecordType.Type(nCol)) {
			return -1;
		}

		for (int i = 0; i < mnMaxRow; ++i) {
			if (!IsUsed(i)) {
				continue;
			}

			if (GetObject(i, nCol) == value) {
				varResult.oper_push(i);
			}
		}

		return varResult.GetCount();
	}

	@Override
	public int FindVector2(int nCol, NFVector2 value, NFDataList varResult) {
		if (!ValidCol(nCol)) {
			return -1;
		}

		if (NFDATA_TYPE.TDATA_VECTOR2 != mVarRecordType.Type(nCol)) {
			return -1;
		}

		for (int i = 0; i < mnMaxRow; ++i) {
			if (!IsUsed(i)) {
				continue;
			}

			if (GetVector2(i, nCol).oper_heq(value)) {
				varResult.oper_push(i);
			}
		}

		return varResult.GetCount();
	}

	@Override
	public int FindVector3(int nCol, NFVector3 value, NFDataList varResult) {
		if (!ValidCol(nCol)) {
			return -1;
		}

		if (NFDATA_TYPE.TDATA_VECTOR3 != mVarRecordType.Type(nCol)) {
			return -1;
		}

		for (int i = 0; i < mnMaxRow; ++i) {
			if (!IsUsed(i)) {
				continue;
			}

			if (GetVector3(i, nCol).oper_heq(value)) {
				varResult.oper_push(i);
			}
		}

		return varResult.GetCount();
	}

	@Override
	public int FindRowByColValue(String strColTag, NFDataList var,
			NFDataList varResult) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int FindInt(String strColTag, int value, NFDataList varResult) {
		if (strColTag.isEmpty()) {
			return -1;
		}

		int nCol = GetCol(strColTag);
		return FindInt(nCol, value, varResult);
	}

	@Override
	public int FindFloat(String strColTag, double value, NFDataList varResult) {
		if (strColTag.isEmpty()) {
			return -1;
		}

		int nCol = GetCol(strColTag);
		return FindFloat(nCol, value, varResult);
	}

	@Override
	public int FindString(String strColTag, String value, NFDataList varResult) {
		if (strColTag.isEmpty()) {
			return -1;
		}

		int nCol = GetCol(strColTag);
		return FindString(nCol, value, varResult);
	}

	@Override
	public int FindObject(String strColTag, NFGUID value, NFDataList varResult) {
		if (strColTag.isEmpty()) {
			return -1;
		}

		int nCol = GetCol(strColTag);
		return FindObject(nCol, value, varResult);
	}

	@Override
	public int FindVector2(String strColTag, NFVector2 value,
			NFDataList varResult) {
		if (strColTag.isEmpty()) {
			return -1;
		}

		int nCol = GetCol(strColTag);
		return FindVector2(nCol, value, varResult);
	}

	@Override
	public int FindVector3(String strColTag, NFVector3 value,
			NFDataList varResult) {
		if (strColTag.isEmpty()) {
			return -1;
		}

		int nCol = GetCol(strColTag);
		return FindVector3(nCol, value, varResult);
	}

	@Override
	public boolean Remove(int nRow) {
		if (ValidRow(nRow)) {
			if (IsUsed(nRow)) {
				RECORD_EVENT_DATA xEventData = new RECORD_EVENT_DATA();
				xEventData.nOpType = RECORD_EVENT_DATA.RecordOptype.Del.value();
				xEventData.nRow = nRow;
				xEventData.nCol = 0;
				xEventData.strRecordName = mstrRecordName;

				OnEventHandler(mSelf, xEventData, new NFData(), new NFData());

				mVecUsedState.add(nRow, 0);

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean Clear() {

	    for (int i = GetRows() - 1; i >= 0; i--)
	    {
	        Remove(i);
	    }

	    return true;
	}

	@Override
	public void AddRecordHook(RECORD_EVENT_FUNCTOR cb) {
		// TODO Auto-generated method stub
		 mtRecordCallback.add(cb);
	}

	@Override
	public boolean GetSave() {
		// TODO Auto-generated method stub
		return mbSave;
	}

	@Override
	public boolean GetPublic() {
		// TODO Auto-generated method stub
		return mbPublic;
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
	public boolean GetUpload() {
		// TODO Auto-generated method stub
		return mbUpload;
	}

	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return mstrRecordName;
	}

	@Override
	public NFDataList GetInitData() {
	   NFDataList pIniData = new NFDataList();
	    pIniData.Append(mVarRecordType);

	    return pIniData;
	}

	@Override
	public NFDataList GetTag() {
		 NFDataList pIniData = new NFDataList();
		    pIniData.Append(mVarRecordTag);

		    return pIniData;
	}

	@Override
	public void SetSave(boolean bSave) {
		// TODO Auto-generated method stub
		 mbSave = bSave;
	}

	@Override
	public void SetCache(boolean bCache) {
		// TODO Auto-generated method stub
		 mbCache = bCache;
	}

	@Override
	public void SetUpload(boolean bUpload) {
		// TODO Auto-generated method stub
		mbUpload = bUpload;
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
	public void SetName(String strName) {
		// TODO Auto-generated method stub
		 mstrRecordName = strName;
	}

	@Override
	public List<NFData> GetRecordVec() {
		// TODO Auto-generated method stub
		 return mtRecordVec;
	}

	protected boolean ValidPos(int nRow, int nCol) {
		if (ValidCol(nCol)
		        && ValidRow(nRow))
		    {
		        return true;
		    }

		    return false;
	}

	protected boolean ValidRow(int nRow) {
		if (nRow >= GetRows() || nRow < 0) {
			return false;
		}

		return true;

	}

	protected boolean ValidCol(int nCol) {
		if (nCol >= GetCols() || nCol < 0) {
			return false;
		}

		return true;
	}

	protected int GetPos(int nRow, int nCol) {
		return nRow * mVarRecordType.GetCount() + nCol;
	}

	protected int GetCol(String strTag) {
		
		if (mmTag.containsKey(strTag)) {
			return mmTag.get(strTag);
		}
	
		    return -1;
	}

	protected void OnEventHandler(NFGUID self, RECORD_EVENT_DATA xEventData,
			NFData oldVar, NFData newVar) {
		for (int i = 0; i < mtRecordCallback.size(); i++) {
			RECORD_EVENT_FUNCTOR functor = mtRecordCallback.get(i);
			functor.operator(self, xEventData, oldVar, newVar);
		}
	}

}
