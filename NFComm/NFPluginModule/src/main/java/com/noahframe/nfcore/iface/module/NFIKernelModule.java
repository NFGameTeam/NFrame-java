/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;



import com.noahframe.nfcore.iface.functor.CLASS_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.functor.CLASS_OBJECT_EVENT;
import com.noahframe.nfcore.iface.functor.PROPERTY_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.functor.RECORD_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.math.NFVector2;
import com.noahframe.nfcore.iface.math.NFVector3;
import com.noahframe.nfcore.iface.util.NFDataList;
import com.noahframe.nfcore.iface.util.NFMapEx;

import java.util.ArrayList;
import java.util.List;




public abstract class NFIKernelModule extends NFMapEx<NFGUID, NFIObject> implements NFIModule {

	// /////////////////////////////////////////////////////////////////////

	public abstract boolean ExistScene(int nSceneID);

	public abstract boolean ExistObject(NFGUID ident);

	public abstract boolean ExistObject(NFGUID ident, int nSceneID,
			int nGroupID);

	public abstract NFIObject GetObject(NFGUID ident);

	public abstract NFIObject CreateObject(NFGUID self, int nSceneID,
			int nGroupID, String strClassName, String strConfigIndex,
			NFDataList arg);

	public abstract boolean DestroyAll();

	public abstract boolean DestroySelf(NFGUID self);

	public abstract boolean DestroyObject(NFGUID self);

	// ////////////////////////////////////////////////////////////////////////
	public abstract boolean FindProperty(NFGUID self, String strPropertyName);

	public abstract boolean SetPropertyInt(NFGUID self,
			String strPropertyName, int nValue);

	public abstract boolean SetPropertyFloat(NFGUID self,
			String strPropertyName, double dValue);

	public abstract boolean SetPropertyString(NFGUID self,
			String strPropertyName, String strValue);

	public abstract boolean SetPropertyObject(NFGUID self,
			String strPropertyName, NFGUID objectValue);

	public abstract boolean SetPropertyVector2(NFGUID self,
			String strPropertyName, NFVector2 value);

	public abstract boolean SetPropertyVector3(NFGUID self,
			String strPropertyName, NFVector3 value);

	public abstract int GetPropertyInt(NFGUID self, String strPropertyName);

	public abstract double GetPropertyFloat(NFGUID self, String strPropertyName);

	public abstract String GetPropertyString(NFGUID self,
			String strPropertyName);

	public abstract NFGUID GetPropertyObject(NFGUID self,
			String strPropertyName);

	public abstract NFVector2 GetPropertyVector2(NFGUID self,
												 String strPropertyName);

	public abstract NFVector3 GetPropertyVector3(NFGUID self,
			String strPropertyName);

	// ////////////////////////////////////////////////////////////////////////
	public abstract NFIRecord FindRecord(NFGUID self, String strRecordName);

	public abstract boolean ClearRecord(NFGUID self, String strRecordName);

	public abstract boolean SetRecordInt(NFGUID self, String strRecordName,
			int nRow, int nCol, int nValue);

	public abstract boolean SetRecordFloat(NFGUID self, String strRecordName,
			int nRow, int nCol, double dwValue);

	public abstract boolean SetRecordString(NFGUID self, String strRecordName,
			int nRow, int nCol, String strValue);

	public abstract boolean SetRecordObject(NFGUID self, String strRecordName,
			int nRow, int nCol, NFGUID objectValue);

	public abstract boolean SetRecordVector2(NFGUID self,
			String strRecordName, int nRow, int nCol, NFVector2 value);

	public abstract boolean SetRecordVector3(NFGUID self,
			String strRecordName, int nRow, int nCol, NFVector3 value);

	public abstract boolean SetRecordInt(NFGUID self, String strRecordName,
			int nRow, String strColTag, int value);

	public abstract boolean SetRecordFloat(NFGUID self, String strRecordName,
			int nRow, String strColTag, double value);

	public abstract boolean SetRecordString(NFGUID self, String strRecordName,
			int nRow, String strColTag, String value);

	public abstract boolean SetRecordObject(NFGUID self, String strRecordName,
			int nRow, String strColTag, NFGUID value);

	public abstract boolean SetRecordVector2(NFGUID self,
			String strRecordName, int nRow, String strColTag, NFVector2 value);

	public abstract boolean SetRecordVector3(NFGUID self,
			String strRecordName, int nRow, String strColTag, NFVector3 value);

	public abstract int GetRecordInt(NFGUID self, String strRecordName,
			int nRow, int nCol);

	public abstract double GetRecordFloat(NFGUID self, String strRecordName,
			int nRow, int nCol);

	public abstract String GetRecordString(NFGUID self, String strRecordName,
			int nRow, int nCol);

	public abstract NFGUID GetRecordObject(NFGUID self, String strRecordName,
			int nRow, int nCol);

	public abstract NFVector2 GetRecordVector2(NFGUID self,
			String strRecordName, int nRow, int nCol);

	public abstract NFVector3 GetRecordVector3(NFGUID self,
			String strRecordName, int nRow, int nCol);

	public abstract int GetRecordInt(NFGUID self, String strRecordName,
			int nRow, String strColTag);

	public abstract double GetRecordFloat(NFGUID self, String strRecordName,
			int nRow, String strColTag);

	public abstract String GetRecordString(NFGUID self, String strRecordName,
			int nRow, String strColTag);

	public abstract NFGUID GetRecordObject(NFGUID self, String strRecordName,
			int nRow, String strColTag);

	public abstract NFVector2 GetRecordVector2(NFGUID self,
			String strRecordName, int nRow, String strColTag);

	public abstract NFVector3 GetRecordVector3(NFGUID self,
			String strRecordName, int nRow, String strColTag);

	// //////////////////////////////////////////////////////////////
	public abstract NFGUID CreateGUID();

	public abstract boolean CreateScene(int nSceneID);

	public abstract boolean DestroyScene(int nSceneID);

	public abstract int GetOnLineCount();

	public abstract int GetMaxOnLineCount();

	public abstract int RequestGroupScene(int nSceneID);

	public abstract boolean ReleaseGroupScene(int nSceneID, int nGroupID);

	public abstract boolean ExitGroupScene(int nSceneID, int nGroupID);

	public abstract boolean GetGroupObjectList(int nSceneID, int nGroupID,
			NFDataList list);

	public abstract boolean GetGroupObjectList(int nSceneID, int nGroupID,
			NFDataList list, NFGUID noSelf);

	public abstract boolean GetGroupObjectList(int nSceneID, int nGroupID,
			NFDataList list, boolean bPlayer);

	public abstract boolean GetGroupObjectList(int nSceneID, int nGroupID,
			NFDataList list, boolean bPlayer, NFGUID noSelf);

	public abstract boolean GetGroupObjectList(int nSceneID, int nGroupID,
			String strClassName, NFDataList list);

	public abstract boolean GetGroupObjectList(int nSceneID, int nGroupID,
			String strClassName, NFGUID noSelf, NFDataList list);

	public abstract int GetObjectByProperty(int nSceneID, int nGroupID,
			String strPropertyName, NFDataList valueArgArg, NFDataList list);

	public abstract void Random(int nStart, int nEnd, int nCount,
			NFDataList valueList);

	public abstract int Random(int nStart, int nEnd);

	// ////////////////////////////////////////////////////////////////////////
	public abstract boolean LogStack();

	public abstract boolean LogInfo(NFGUID ident);

	public abstract boolean LogSelfInfo(NFGUID ident);

	// ////////////////////////////////////////////////////////////////////////

	public abstract boolean DoEvent(NFGUID self, String strClassName,
									CLASS_OBJECT_EVENT eEvent, NFDataList valueList);

	// protected:

	public abstract boolean RegisterCommonClassEvent(CLASS_EVENT_FUNCTOR cb);

	public abstract boolean RegisterCommonPropertyEvent(
			PROPERTY_EVENT_FUNCTOR cb);

	public abstract boolean RegisterCommonRecordEvent(
			RECORD_EVENT_FUNCTOR cb);

	// protected:

	public abstract boolean AddClassCallBack(String strClassName,
			CLASS_EVENT_FUNCTOR cb);

	public abstract void InitRandom();

	public abstract int OnClassCommonEvent(NFGUID self, String strClassName,
										   CLASS_OBJECT_EVENT eClassEvent, NFDataList var);

	public abstract void ProcessMemFree();

	protected List<NFGUID> mtDeleteSelfList=new ArrayList();

	// ////////////////////////////////////////////////////////////////////////

	protected List<CLASS_EVENT_FUNCTOR> mtCommonClassCallBackList=new ArrayList();;

	protected List<PROPERTY_EVENT_FUNCTOR> mtCommonPropertyCallBackList=new ArrayList();;

	protected List<RECORD_EVENT_FUNCTOR> mtCommonRecordCallBackList=new ArrayList();;

	protected List<Float> mvRandom=new ArrayList<Float>();
	protected int nGUIDIndex;
	protected int mnRandomPos;

	protected NFGUID mnCurExeObject;
	protected long nLastTime;

	protected NFISceneAOIModule m_pSceneModule;
	protected NFILogModule m_pLogModule;
	protected NFIClassModule m_pClassModule;
	protected NFIElementModule m_pElementModule;
	protected NFIScheduleModule m_pScheduleModule;
	protected NFIEventModule m_pEventModule;
}
