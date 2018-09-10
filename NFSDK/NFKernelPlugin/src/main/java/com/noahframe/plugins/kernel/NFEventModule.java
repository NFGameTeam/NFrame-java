package com.noahframe.plugins.kernel;



import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.api.plugin.Extension;
import com.noahframe.nfcore.iface.NFEventDefine;
import com.noahframe.nfcore.iface.functor.MODULE_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.functor.OBJECT_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.module.NFIEventModule;
import com.noahframe.nfcore.iface.module.NFIKernelModule;
import com.noahframe.nfcore.iface.util.NFDataList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Extension
public class NFEventModule implements NFIEventModule {
	
	
	private static NFEventModule SingletonPtr=null;
	
	public static NFEventModule GetSingletonPtr()
	{
		if (null==SingletonPtr) {
			 SingletonPtr=new NFEventModule();
			 return SingletonPtr;
		}
		else {
			return SingletonPtr;
		}
	}
	

	public boolean DoEvent(NFEventDefine nEventID, NFDataList valueList) {
		boolean bRet = false;
		List<MODULE_EVENT_FUNCTOR> xEventListPtr = mModuleEventInfoMapEx
				.get(nEventID);

		if (null != xEventListPtr) {

			for (int i = 0; i < xEventListPtr.size(); i++) {
				MODULE_EVENT_FUNCTOR pFunPtr = xEventListPtr.get(i);
				bRet = null == pFunPtr ? false : true;
				if (bRet) {
					pFunPtr.operator(nEventID, valueList);
				}
			}

			bRet = true;
		}

		return bRet;
	}

	public boolean ExistEventCallBack(NFEventDefine nEventID) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean RemoveEventCallBack(NFEventDefine nEventID) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean DoEvent(NFGUID self, NFEventDefine nEventID,
						   NFDataList valueList) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean ExistEventCallBack(NFGUID self, NFEventDefine nEventID) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean RemoveEventCallBack(NFGUID self, NFEventDefine nEventID) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean RemoveEventCallBack(NFGUID self) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean AddEventCallBack(NFEventDefine nEventID,
			MODULE_EVENT_FUNCTOR cb) {

		List<MODULE_EVENT_FUNCTOR> xEventListPtr = mModuleEventInfoMapEx
				.get(nEventID);
		if (null == xEventListPtr) {
			xEventListPtr = new ArrayList<MODULE_EVENT_FUNCTOR>();
			mModuleEventInfoMapEx.put(nEventID, xEventListPtr);
		}
		xEventListPtr.add(cb);

		return false;
	}

	public boolean AddEventCallBack(NFGUID self, NFEventDefine nEventID,
			OBJECT_EVENT_FUNCTOR cb) {

		return false;
	}

	private NFIKernelModule m_pKernelodule;

	private List<NFEventDefine> mModuleRemoveListEx;
	private Map<NFEventDefine, List<MODULE_EVENT_FUNCTOR>> mModuleEventInfoMapEx;

	private List<NFGUID> mObjectRemoveListEx;
	private Map<NFEventDefine, List<OBJECT_EVENT_FUNCTOR>> mObjectEventInfoMapEx;

	@Override
	public boolean Awake() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Init() {
		// TODO Auto-generated method stub

		m_pKernelodule = NFPluginManager.GetSingletonPtr().FindModules(null, NFIKernelModule.class).get(0);

		return true;
	}

	@Override
	public boolean AfterInit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CheckConfig() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ReadyExecute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Execute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean BeforeShut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Shut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Finalize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean OnReloadPlugin() {
		// TODO Auto-generated method stub
		return false;
	}

}
