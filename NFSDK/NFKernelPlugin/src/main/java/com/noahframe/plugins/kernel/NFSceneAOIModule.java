package com.noahframe.plugins.kernel;



import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.iface.NFFrame;
import com.noahframe.nfcore.iface.NFIPluginManager;
import com.noahframe.nfcore.iface.functor.*;
import com.noahframe.nfcore.iface.math.NFVector3;
import com.noahframe.nfcore.iface.module.*;
import com.noahframe.nfcore.iface.util.NFData;
import com.noahframe.nfcore.iface.util.NFDataList;

import java.util.List;




public class NFSceneAOIModule extends NFISceneAOIModule {

	
	List<NFISceneAOIModule.OBJECT_ENTER_EVENT_FUNCTOR> mtObjectEnterCallback;
	List<OBJECT_LEAVE_EVENT_FUNCTOR> mtObjectLeaveCallback;

	List<PROPERTY_ENTER_EVENT_FUNCTOR> mtPropertyEnterCallback;
	List<RECORD_ENTER_EVENT_FUNCTOR> mtRecordEnterCallback;

	List<PROPERTY_SINGLE_EVENT_FUNCTOR> mtPropertySingleCallback;
	List<RECORD_SINGLE_EVENT_FUNCTOR> mtRecordSingleCallback;

	List<SCENE_EVENT_FUNCTOR> mtEnterSceneConditionCallback;
	List<SCENE_EVENT_FUNCTOR> mtBeforeEnterSceneCallback;
	List<SCENE_EVENT_FUNCTOR> mtAfterEnterSceneCallback;
	List<SCENE_EVENT_FUNCTOR> mtOnSwapSceneCallback;
	List<SCENE_EVENT_FUNCTOR> mtBeforeLeaveSceneCallback;
	List<SCENE_EVENT_FUNCTOR> mtAfterLeaveSceneCallback;
	
	
	private NFIKernelModule m_pKernelModule = null;
	private NFIClassModule m_pClassModule = null;
	private NFILogModule m_pLogModule = null;
	private NFIElementModule m_pElementModule = null;
	private NFIEventModule m_pEventModule = null;
	private NFIPluginManager pPluginManager = null;
	
	
	private static NFSceneAOIModule SingletonPtr=null;
	
	public static NFSceneAOIModule GetSingletonPtr()
	{
		if (null==SingletonPtr) {
			 SingletonPtr=new NFSceneAOIModule();
			 return SingletonPtr;
		}
		else {
			return SingletonPtr;
		}
	}
	
	
	public NFSceneAOIModule()
    {
        pPluginManager =  NFPluginManager.GetSingletonPtr();
    }
	
	@Override
	public boolean Awake() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public CLASS_EVENT_FUNCTOR OnClassCommonEvent=new CLASS_EVENT_FUNCTOR(){
		
		@Override
		public int operator(NFGUID self, String strClassName, CLASS_OBJECT_EVENT eClassEvent, NFDataList var)
		{
			if (CLASS_OBJECT_EVENT.COE_DESTROY == eClassEvent)
			{
				 int nObjectSceneID = m_pKernelModule.GetPropertyInt(self, NFFrame.IObject.SceneID());
				 int nObjectGroupID = m_pKernelModule.GetPropertyInt(self, NFFrame.IObject.GroupID());

				if (nObjectGroupID < 0 || nObjectSceneID <= 0)
				{
					return 0;
				}

				NFDataList valueAllPlayrNoSelfList=new NFDataList();
				m_pKernelModule.GetGroupObjectList(nObjectSceneID, nObjectGroupID, valueAllPlayrNoSelfList, true, self);

				//tell other people that you want to leave from this scene or this group
				//every one want to know you want to leave notmater you are a monster maybe you are a player
				OnObjectListLeave(valueAllPlayrNoSelfList, new NFDataList().oper_push(self));
			}

			else if (CLASS_OBJECT_EVENT.COE_CREATE_NODATA == eClassEvent)
			{

			}
			else if (CLASS_OBJECT_EVENT.COE_CREATE_LOADDATA == eClassEvent)
			{
			}
			else if (CLASS_OBJECT_EVENT.COE_CREATE_HASDATA == eClassEvent)
			{
				if (strClassName == NFFrame.Player.ThisName())
				{
					//tell youself<client>, u want to enter this scene or this group
					OnObjectListEnter(new NFDataList().oper_push(self), new NFDataList().oper_push(self));

					//tell youself<client>, u want to broad your properties and records to youself
					OnPropertyEnter(new NFDataList().oper_push(self), self);
					OnRecordEnter(new NFDataList().oper_push(self), self);
				}
				else
				{
					 int nObjectSceneID = m_pKernelModule.GetPropertyInt(self, NFFrame.IObject.SceneID());
					 int nObjectGroupID = m_pKernelModule.GetPropertyInt(self, NFFrame.IObject.GroupID());

					if (nObjectGroupID < 0 || nObjectSceneID <= 0)
					{
						return 0;
					}

					NFDataList valueAllPlayrObjectList=new NFDataList();
					m_pKernelModule.GetGroupObjectList(nObjectSceneID, nObjectGroupID, valueAllPlayrObjectList, true);

					//monster or others need to tell all player
					OnObjectListEnter(valueAllPlayrObjectList, new NFDataList().oper_push(self));
					OnPropertyEnter(valueAllPlayrObjectList, self);
				}
			}
			else if (CLASS_OBJECT_EVENT.COE_CREATE_FINISH == eClassEvent)
			{

			}

			return 0;
		}
	};
	
	public PROPERTY_EVENT_FUNCTOR OnPropertyCommonEvent=new PROPERTY_EVENT_FUNCTOR()
	{

		@Override
		public int operator(NFGUID self, String strPropertyName, NFData oldVar,
				NFData newVar) {
			String strClassName = m_pKernelModule.GetPropertyString(self, NFFrame.IObject.ClassName());
			if (strClassName == NFFrame.Player.ThisName())
			{
				//only player can change grupid and sceneid
				if (NFFrame.Player.GroupID() == strPropertyName)
				{
					OnPlayerGroupEvent(self, strPropertyName, oldVar, newVar);
					return 0;
				}

				if (NFFrame.Player.SceneID() == strPropertyName)
				{
					OnPlayerSceneEvent(self, strPropertyName, oldVar, newVar);
					return 0;
				}
			}

			NFDataList valueBroadCaseList=new NFDataList();
			if (GetBroadCastObject(self, strPropertyName, false, valueBroadCaseList) <= 0)
			{
				return 0;
			}

			OnPropertyEvent(self, strPropertyName, oldVar, newVar, valueBroadCaseList);

			return 0;
		}
		
	};
	
	public RECORD_EVENT_FUNCTOR OnRecordCommonEvent=new RECORD_EVENT_FUNCTOR()
	{

		@Override
		public int operator(NFGUID self, RECORD_EVENT_DATA xEventData, NFData oldVar,
							NFData newVar) {
			String strRecordName = xEventData.strRecordName;
			int nOpType = xEventData.nOpType;
			int nRow = xEventData.nRow;
			int nCol = xEventData.nCol;

			int nObjectContainerID = m_pKernelModule.GetPropertyInt(self, NFFrame.Player.SceneID());
			int nObjectGroupID = m_pKernelModule.GetPropertyInt(self, NFFrame.Player.GroupID());

			if (nObjectGroupID < 0)
			{
				return 0;
			}
			
			NFDataList valueBroadCaseList=new NFDataList();
			GetBroadCastObject(self, strRecordName, true, valueBroadCaseList);

			OnRecordEvent(self, strRecordName, xEventData, oldVar, newVar, valueBroadCaseList);

			return 0;
		}
		
	};

	@Override
	public boolean Init() {
		m_pKernelModule = pPluginManager.FindModule(NFIKernelModule.class);
		m_pClassModule = pPluginManager.FindModule(NFIClassModule.class);
		m_pElementModule = pPluginManager.FindModule(NFIElementModule.class);
		m_pLogModule = pPluginManager.FindModule(NFILogModule.class);
		m_pEventModule = pPluginManager.FindModule(NFIEventModule.class);

		m_pKernelModule.RegisterCommonClassEvent(OnClassCommonEvent);
		m_pKernelModule.RegisterCommonPropertyEvent(OnPropertyCommonEvent);
		m_pKernelModule.RegisterCommonRecordEvent(OnRecordCommonEvent);

		//init all scene
		NFIClass xLogicClass = m_pClassModule.GetElement(NFFrame.Scene.ThisName());
		if (xLogicClass != null)
		{
			List<String> strIdList = xLogicClass.GetIDList();

			for (int i = 0; i < strIdList.size(); ++i)
			{
				String strId = strIdList.get(i);

				int nSceneID = Integer.valueOf(strId);
				m_pKernelModule.CreateScene(nSceneID);
			}
		}

	    return true;
	}

	@Override
	public boolean AfterInit() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean ReadyExecute() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Execute() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean BeforeShut() {
		mtObjectEnterCallback.clear();
		mtObjectLeaveCallback.clear();
		mtPropertyEnterCallback.clear();
		mtRecordEnterCallback.clear();
		mtPropertySingleCallback.clear();
		mtRecordSingleCallback.clear();

		mtAfterEnterSceneCallback.clear();
		mtBeforeLeaveSceneCallback.clear();

	    return true;
	}

	@Override
	public boolean Shut() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Finalize() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean OnReloadPlugin() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean RemoveSwapSceneEventCallBack() {
		mtOnSwapSceneCallback.clear();

		return true;
	}

	@Override
	public boolean RequestEnterScene(NFGUID self, int nSceneID, int nGrupID,
			int nType, NFDataList argList) {
		if (nGrupID < 0)
		{
			return false;
		}

		 int nNowSceneID = m_pKernelModule.GetPropertyInt(self, NFFrame.Player.SceneID());
		 int nNowGroupID = m_pKernelModule.GetPropertyInt(self, NFFrame.Player.GroupID());
		
		if (nNowSceneID == nSceneID &&
			 nNowGroupID == nGrupID)
		{
			//m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, self, "in same scene and group but it not a clone scene", nSceneID);

			return false;
		}

		NFSceneInfo pSceneInfo = GetElement(nSceneID);
		if (null==pSceneInfo)
		{
			return false;
		}

		/*
		if (!pSceneInfo.ExistElement(nNewGroupID))
		{
			return false;
		}
		*/

		int nEnterConditionCode = EnterSceneCondition(self, nSceneID, nGrupID, nType, argList);
		if (nEnterConditionCode != 0)
		{
			//m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, self, "before enter condition code:", nEnterConditionCode);
			return false;
		}

		
		NFVector3 vRelivePos = GetRelivePosition(nSceneID, 0);
		if (!SwitchScene(self, nSceneID, nGrupID, nType, vRelivePos.X(), vRelivePos.Y(), vRelivePos.Z(), 0.0f, argList))
		{
			//m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, self, "SwitchScene failed", nSceneID);

			return false;
		}

		return true;
	}

	@Override
	public boolean AddSeedData(int nSceneID, String strSeedID,
							   String strConfigID, NFVector3 vPos, int nWeight) {
		NFSceneInfo pSceneInfo = GetElement(nSceneID);
		if (pSceneInfo != null)
		{
			return pSceneInfo.AddSeedObjectInfo(strSeedID, strConfigID, vPos, nWeight);
		}

		return false;
	}

	@Override
	public boolean AddRelivePosition(int nSceneID, int nIndex, NFVector3 vPos) {
		NFSceneInfo pSceneInfo = GetElement(nSceneID);
		if (pSceneInfo != null)
		{
			return pSceneInfo.AddReliveInfo(nIndex, vPos);
		}

		return false;
	}

	@Override
	public NFVector3 GetRelivePosition(int nSceneID, int nIndex) {
		NFSceneInfo pSceneInfo = GetElement(nSceneID);
		if (pSceneInfo != null)
		{
			return pSceneInfo.GetReliveInfo(nIndex);
		}

		return null;
	}

	@Override
	public boolean CreateSceneNPC(int nSceneID, int nGroupID) {
		NFSceneInfo pSceneInfo = GetElement(nSceneID);
		if (null==pSceneInfo)
		{
			return false;
		}

		//prepare monster for player
		//create monster before the player enter the scene, then we can send monster's data by one message pack
		//if you create monster after player enter scene, then send monster's data one by one
		SceneSeedResource pResource = pSceneInfo.mtSceneResourceConfig.First();
		for (; pResource!=null; pResource = pSceneInfo.mtSceneResourceConfig.Next())
		{
			int nWeight = m_pKernelModule.Random(0, 100);
			if (nWeight <= pResource.nWeight)
			{
				String strClassName = m_pElementModule.GetPropertyString(pResource.strConfigID, NFFrame.IObject.ClassName());

				NFDataList arg=new NFDataList();
				arg.oper_push(NFFrame.IObject.X()).oper_push(pResource.vSeedPos.X());
				arg.oper_push(NFFrame.IObject.Y()).oper_push(pResource.vSeedPos.Y());
				arg.oper_push(NFFrame.IObject.Z()).oper_push(pResource.vSeedPos.Z());
				arg.oper_push(NFFrame.NPC.SeedID()).oper_push(pResource.strSeedID);

				m_pKernelModule.CreateObject(new NFGUID(), nSceneID, nGroupID, strClassName, pResource.strConfigID, arg);
			}
		}

		return false;
	}

	@Override
	public boolean DestroySceneNPC(int nSceneID, int nGroupID) {
		NFSceneInfo pSceneInfo = GetElement(nSceneID);
		if (pSceneInfo != null)
		{
			if (pSceneInfo.GetElement(nGroupID) != null)
			{
				NFDataList xMonsterlistObject = new NFDataList();
				if (m_pKernelModule.GetGroupObjectList(nSceneID, nGroupID, xMonsterlistObject, false))
				{
					for (int i = 0; i < xMonsterlistObject.GetCount(); ++i)
					{
						NFGUID ident = xMonsterlistObject.Object(i);
						m_pKernelModule.DestroyObject(ident);
					}
				}

				pSceneInfo.RemoveElement(nGroupID);

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean AddObjectEnterCallback(OBJECT_ENTER_EVENT_FUNCTOR cb) {
		mtObjectEnterCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddObjectLeaveCallBack(OBJECT_LEAVE_EVENT_FUNCTOR cb) {
		mtObjectLeaveCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddPropertyEnterCallBack(PROPERTY_ENTER_EVENT_FUNCTOR cb) {
		mtPropertyEnterCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddRecordEnterCallBack(RECORD_ENTER_EVENT_FUNCTOR cb) {
		mtRecordEnterCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddPropertyEventCallBack(PROPERTY_SINGLE_EVENT_FUNCTOR cb) {
		mtPropertySingleCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddRecordEventCallBack(RECORD_SINGLE_EVENT_FUNCTOR cb) {
		mtRecordSingleCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddEnterSceneConditionCallBack(SCENE_EVENT_FUNCTOR cb) {
		mtEnterSceneConditionCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddBeforeEnterSceneGroupCallBack(SCENE_EVENT_FUNCTOR cb) {
		mtBeforeEnterSceneCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddAfterEnterSceneGroupCallBack(SCENE_EVENT_FUNCTOR cb) {
		mtAfterEnterSceneCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddSwapSceneEventCallBack(SCENE_EVENT_FUNCTOR cb) {
		mtOnSwapSceneCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddBeforeLeaveSceneGroupCallBack(SCENE_EVENT_FUNCTOR cb) {
		mtBeforeLeaveSceneCallback.add(cb);
		return true;
	}

	@Override
	public boolean AddAfterLeaveSceneGroupCallBack(SCENE_EVENT_FUNCTOR cb) {
		mtAfterLeaveSceneCallback.add(cb);
		return true;
	}

	@Override
	public boolean CheckConfig() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean SwitchScene( NFGUID self,  int nTargetSceneID,  int nTargetGroupID,  int nType,  float fX,  float fY,  float fZ,  float fOrient,  NFDataList arg)
	{
		NFIObject pObject = m_pKernelModule.GetObject(self);
		if (pObject != null)
		{
			int nOldSceneID = pObject.GetPropertyInt(NFFrame.Scene.SceneID());
			int nOldGroupID = pObject.GetPropertyInt(NFFrame.Scene.GroupID());

			NFSceneInfo pOldSceneInfo = this.GetElement(nOldSceneID);
					NFSceneInfo pNewSceneInfo = this.GetElement(nTargetSceneID);
			if (null==pOldSceneInfo)
			{
				//m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "no this container", nOldSceneID);
				return false;
			}

			if (null==pNewSceneInfo)
			{
				//m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "no this container", nTargetSceneID);
				return false;
			}

			if (null==pNewSceneInfo.GetElement(nTargetGroupID))
			{
				//m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "no this group", nTargetGroupID);
				return false;
			}
			/////////
			BeforeLeaveSceneGroup(self, nOldSceneID, nOldGroupID, nType, arg);

			pOldSceneInfo.RemoveObjectFromGroup(nOldGroupID, self, true);

			//if (nTargetSceneID != nOldSceneID)
			{
				pObject.SetPropertyInt(NFFrame.Scene.GroupID(), 0);
				/////////
				AfterLeaveSceneGroup(self, nOldSceneID, nOldGroupID, nType, arg);

				pObject.SetPropertyInt(NFFrame.Scene.SceneID(), nTargetSceneID);

				OnSwapSceneEvent(self, nTargetSceneID, nTargetGroupID, nType, arg);
			}

			pObject.SetPropertyFloat(NFFrame.IObject.X(), fX);
			pObject.SetPropertyFloat(NFFrame.IObject.Y(), fY);
			pObject.SetPropertyFloat(NFFrame.IObject.Z(), fZ);

			////////
			BeforeEnterSceneGroup(self, nTargetSceneID, nTargetGroupID, nType, arg);

			pNewSceneInfo.AddObjectToGroup(nTargetGroupID, self, true);
			pObject.SetPropertyInt(NFFrame.Scene.GroupID(), nTargetGroupID);

			/////////
			AfterEnterSceneGroup(self, nTargetSceneID, nTargetGroupID, nType, arg);

			return true;
		}

		m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object", "SwitchScene", 0);

		return false;
	}
	
	public int BeforeLeaveSceneGroup( NFGUID self,  int nSceneID,  int nGroupID,  int nType,  NFDataList argList)
	{
		
		for (int i = 0; i < mtBeforeLeaveSceneCallback.size(); i++) {
			SCENE_EVENT_FUNCTOR pFunPtr=mtBeforeLeaveSceneCallback.get(i);
			pFunPtr.operator(self, nSceneID, nGroupID, nType, argList);
		}
		return 0;
	}
	
	public int AfterLeaveSceneGroup( NFGUID self,  int nSceneID,  int nGroupID,  int nType,  NFDataList argList)
	{
		
		for (int i = 0; i < mtAfterLeaveSceneCallback.size(); i++) {
			SCENE_EVENT_FUNCTOR pFunPtr=mtAfterLeaveSceneCallback.get(i);
			pFunPtr.operator(self, nSceneID, nGroupID, nType, argList);
		}
		return 0;
	}
	
	public int OnSwapSceneEvent(NFGUID self,  int nSceneID,  int nGroupID,  int nType,  NFDataList argList)
	{
		
		for (int i = 0; i < mtOnSwapSceneCallback.size(); i++) {
			SCENE_EVENT_FUNCTOR pFunPtr=mtOnSwapSceneCallback.get(i);
			pFunPtr.operator(self, nSceneID, nGroupID, nType, argList);
		}
		
		return 0;
	}
	
	public int BeforeEnterSceneGroup(NFGUID self,  int nSceneID,  int nGroupID,  int nType, NFDataList argList)
	{
		for (int i = 0; i < mtBeforeEnterSceneCallback.size(); i++) {
			SCENE_EVENT_FUNCTOR pFunPtr=mtBeforeEnterSceneCallback.get(i);
			pFunPtr.operator(self, nSceneID, nGroupID, nType, argList);
		}
		return 0;
	}
	
	public int AfterEnterSceneGroup(NFGUID self,  int nSceneID,  int nGroupID,  int nType, NFDataList argList)
	{
		
		for (int i = 0; i < mtAfterEnterSceneCallback.size(); i++) {
			SCENE_EVENT_FUNCTOR pFunPtr=mtAfterEnterSceneCallback.get(i);
			pFunPtr.operator(self, nSceneID, nGroupID, nType, argList);
		}

		return 0;
	}
	
	public int EnterSceneCondition(NFGUID self,  int nSceneID,  int nGroupID,  int nType, NFDataList argList)
	{
		for (int i = 0; i < mtEnterSceneConditionCallback.size(); i++) {
			SCENE_EVENT_FUNCTOR pFunPtr=mtEnterSceneConditionCallback.get(i);
			int nReason=pFunPtr.operator(self, nSceneID, nGroupID, nType, argList);
			if (nReason != 0)
			{
				return nReason;
			}
		}
		return 0;
	}
	
	public int OnPlayerGroupEvent( NFGUID  self,  String  strPropertyName,  NFData  oldVar,  NFData  newVar)
	{
		//this event only happened in the same scene
		int nSceneID = m_pKernelModule.GetPropertyInt(self, NFFrame.IObject.SceneID());
		int nOldGroupID = oldVar.GetInt();
		int nNewGroupID = newVar.GetInt();

		//maybe form 0, maybe not, only three stuation
		//example1: 0 . 1 ==> new_group > 0  old_group <= 0
		//example2: 1 . 2 ==> new_group > 0  old_group > 0
		//example3: 5 . 0 ==> new_group <= 0  old_group > 0
		if (nNewGroupID > 0)
		{
			if (nOldGroupID > 0)
			{
				//example2: 1 . 2 ==> new_group > 0  old_group > 0
				//step1: leave
				NFDataList valueAllOldNPCListNoSelf=new NFDataList();
				NFDataList valueAllOldPlayerListNoSelf=new NFDataList();
				m_pKernelModule.GetGroupObjectList(nSceneID, nOldGroupID, valueAllOldNPCListNoSelf, false, self);
				m_pKernelModule.GetGroupObjectList(nSceneID, nOldGroupID, valueAllOldPlayerListNoSelf, true, self);

				OnObjectListLeave(valueAllOldPlayerListNoSelf, new NFDataList().oper_push(self));
				OnObjectListLeave(new NFDataList().oper_push(self), valueAllOldPlayerListNoSelf);
				OnObjectListLeave(new NFDataList().oper_push(self), valueAllOldNPCListNoSelf);
			}
			else
			{
				//example1: 0 . 1 == > new_group > 0  old_group <= 0
				//only use step2 that enough
			}

			//step2: enter
			NFDataList valueAllNewNPCListNoSelf=new NFDataList();
			NFDataList valueAllNewPlayerListNoSelf=new NFDataList();

			m_pKernelModule.GetGroupObjectList(nSceneID, nNewGroupID, valueAllNewNPCListNoSelf, false, self);
			m_pKernelModule.GetGroupObjectList(nSceneID, nNewGroupID, valueAllNewPlayerListNoSelf, true, self);

			OnObjectListEnter(valueAllNewPlayerListNoSelf, new NFDataList().oper_push(self));
			OnObjectListEnter(new NFDataList().oper_push(self), valueAllNewPlayerListNoSelf);
			OnObjectListEnter(new NFDataList().oper_push(self), valueAllNewNPCListNoSelf);

			//bc others data to u
			for (int i = 0; i < valueAllNewNPCListNoSelf.GetCount(); i++)
			{
				NFGUID identOld = valueAllNewNPCListNoSelf.Object(i);

				OnPropertyEnter(new NFDataList().oper_push(self), identOld);
				OnRecordEnter(new NFDataList().oper_push(self), identOld);
			}

			//bc others data to u
			for (int i = 0; i < valueAllNewPlayerListNoSelf.GetCount(); i++)
			{
				NFGUID identOld = valueAllNewPlayerListNoSelf.Object(i);

				OnPropertyEnter(new NFDataList().oper_push(self), identOld);
				OnRecordEnter(new NFDataList().oper_push(self), identOld);
			}

			//bc u data to others
			OnPropertyEnter(valueAllNewPlayerListNoSelf, self);
			OnRecordEnter(valueAllNewPlayerListNoSelf, self);
		}
		else
		{
			if (nOldGroupID > 0)
			{
				//example3: 5 . 0 ==> new_group <= 0  old_group > 0
				//step1: leave
				NFDataList valueAllOldNPCListNoSelf=new NFDataList();
				NFDataList valueAllOldPlayerListNoSelf=new NFDataList();
				m_pKernelModule.GetGroupObjectList(nSceneID, nOldGroupID, valueAllOldNPCListNoSelf, false, self);
				m_pKernelModule.GetGroupObjectList(nSceneID, nOldGroupID, valueAllOldPlayerListNoSelf, true, self);

				OnObjectListLeave(valueAllOldPlayerListNoSelf,new NFDataList().oper_push(self));
				OnObjectListLeave(new NFDataList().oper_push(self), valueAllOldPlayerListNoSelf);
				OnObjectListLeave(new NFDataList().oper_push(self), valueAllOldNPCListNoSelf);
			}
		}

		return 0;
	}
	
	public int OnObjectListLeave(NFDataList self, NFDataList argVar)
	{
		
		for (int i = 0; i < mtObjectLeaveCallback.size(); i++) {
			OBJECT_LEAVE_EVENT_FUNCTOR pFunc=mtObjectLeaveCallback.get(i);
			pFunc.operator(self, argVar);
		}
		return 0;
	}
	
	public int OnObjectListEnter(NFDataList self, NFDataList argVar)
	{
		for (int i = 0; i < mtObjectEnterCallback.size(); i++) {
			OBJECT_ENTER_EVENT_FUNCTOR pFunc=mtObjectEnterCallback.get(i);
			pFunc.operator(self, argVar);
		}
		return 0;
	}
	
	public int OnPropertyEnter(NFDataList argVar, NFGUID  self)
	{
		
		for (int i = 0; i < mtPropertyEnterCallback.size(); i++) {
			PROPERTY_ENTER_EVENT_FUNCTOR pFunc=mtPropertyEnterCallback.get(i);
			pFunc.operator(argVar, self);
		}
		return 0;
	}
	
	public int OnPlayerSceneEvent(NFGUID self, String strPropertyName, NFData  oldVar, NFData newVar)
	{
		//no more player in this group of this scene at the same time
		//so now only one player(that you) in this group of this scene
		//BTW, most of time, we dont create monsters in the group 0
		//so no one at this group but u

		return 0;
	}
	
	public int OnRecordEnter(NFDataList argVar, NFGUID self)
	{
		
		for (int i = 0; i < mtRecordEnterCallback.size(); i++) {
			RECORD_ENTER_EVENT_FUNCTOR pFunc=mtRecordEnterCallback.get(i);
			pFunc.operator(argVar, self);
		}
		return 0;
	}
	
	public int OnPropertyEvent(NFGUID self, String strProperty, NFData oldVar, NFData newVar, NFDataList argVar)
	{
		
		for (int i = 0; i < mtPropertySingleCallback.size(); i++) {
			PROPERTY_SINGLE_EVENT_FUNCTOR pFunc=mtPropertySingleCallback.get(i);
			pFunc.operator(self, strProperty, oldVar, newVar, argVar);
		}
		return 0;
	}
	
	
	public int OnRecordEvent(NFGUID self, String strProperty, RECORD_EVENT_DATA  xEventData, NFData oldVar, NFData newVar, NFDataList argVar)
	{
		
		for (int i = 0; i < mtRecordSingleCallback.size(); i++) {
			RECORD_SINGLE_EVENT_FUNCTOR pFunc=mtRecordSingleCallback.get(i);
			pFunc.operator(self, strProperty, xEventData, oldVar, newVar, argVar);
		}
		return 0;
	}
	
	
	public int GetBroadCastObject(NFGUID self,  String  strPropertyName,  boolean bTable, NFDataList  valueObject)
	{
		int nObjectContainerID = m_pKernelModule.GetPropertyInt(self, NFFrame.IObject.SceneID());
		int nObjectGroupID = m_pKernelModule.GetPropertyInt(self, NFFrame.IObject.GroupID());

		 String strClassName = m_pKernelModule.GetPropertyString(self, NFFrame.IObject.ClassName());
		NFIRecordManager pClassRecordManager = m_pClassModule.GetClassRecordManager(strClassName);
		NFIPropertyManager pClassPropertyManager = m_pClassModule.GetClassPropertyManager(strClassName);

		NFIRecord pRecord=null;
		NFIProperty pProperty=null;
		if (bTable)
		{
			if (null == pClassRecordManager)
			{
				return -1;
			}

			pRecord = pClassRecordManager.GetElement(strPropertyName);
			if (null == pRecord)
			{
				return -1;
			}
		}
		else
		{
			if (null == pClassPropertyManager)
			{
				return -1;
			}
			pProperty = pClassPropertyManager.GetElement(strPropertyName);
			if (null == pProperty)
			{
				return -1;
			}
		}

		if (bTable)
		{
			if (pRecord.GetPublic())
			{
				m_pKernelModule.GetGroupObjectList(nObjectContainerID, nObjectGroupID, valueObject, true, self);
			}
			else if (pRecord.GetPrivate() && !pRecord.GetUpload())
			{//upload property can not board to itself
				valueObject.Add(self);
			}
		}
		else
		{
			if (pProperty.GetPublic())
			{
				m_pKernelModule.GetGroupObjectList(nObjectContainerID, nObjectGroupID, valueObject, true, self);
			}
			else if (pProperty.GetPrivate() && !pProperty.GetUpload())
			{
				//upload property can not board to itself
				valueObject.Add(self);
			}
		}

		return valueObject.GetCount();
	}
	
	
	
}
