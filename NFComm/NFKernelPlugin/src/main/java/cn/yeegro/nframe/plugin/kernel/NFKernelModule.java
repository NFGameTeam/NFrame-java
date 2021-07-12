package cn.yeegro.nframe.plugin.kernel;




import cn.yeegro.nframe.comm.code.NFrame;
import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.api.NFObject;
import cn.yeegro.nframe.comm.code.api.NFSceneGroupInfo;
import cn.yeegro.nframe.comm.code.api.NFSceneInfo;
import org.pf4j.Extension;
import cn.yeegro.nframe.comm.code.functor.*;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.code.iface.NFIProperty;
import cn.yeegro.nframe.comm.code.iface.NFIRecord;
import cn.yeegro.nframe.comm.code.iface.NFPlatform;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.module.*;
import cn.yeegro.nframe.comm.code.util.NFDATA_TYPE;
import cn.yeegro.nframe.comm.code.util.NFData;
import cn.yeegro.nframe.comm.code.util.NFDataList;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.comm.module.NFIKernelModule;
import cn.yeegro.nframe.comm.module.NFILogModule;
import cn.yeegro.nframe.comm.module.NFISceneAOIModule;

import java.util.ArrayList;
import java.util.List;


@Extension
public class NFKernelModule extends NFIKernelModule {

	private NFIPluginManager pPluginManager;

	private static NFKernelModule SingletonPtr=null;
	
	public static NFKernelModule GetSingletonPtr()
	{
		if (null==SingletonPtr) {
			 SingletonPtr=new NFKernelModule();
			 return SingletonPtr;
		}
		else {
			return SingletonPtr;
		}
	}
	
	
	public NFKernelModule() {
		
		nGUIDIndex = 0;
		mnRandomPos = 0;
		nLastTime = 0;

		pPluginManager = NFPluginManager.GetSingletonPtr();

		nLastTime = pPluginManager.GetNowTime();
		InitRandom();
	}

	@Override
	public boolean Awake() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Init() {
		mtDeleteSelfList.clear();

		m_pSceneModule = pPluginManager.FindModule(
				NFISceneAOIModule.class);
		m_pClassModule = pPluginManager
				.FindModule( NFIClassModule.class);
		m_pElementModule = pPluginManager.FindModule(
				NFIElementModule.class);
		m_pLogModule = pPluginManager.FindModule( NFILogModule.class)
				;
		m_pScheduleModule = pPluginManager.FindModule(
				NFIScheduleModule.class);
		m_pEventModule = pPluginManager
				.FindModule( NFIEventModule.class);

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
	    ProcessMemFree();

	    mnCurExeObject = new NFGUID();

	    if (mtDeleteSelfList.size() > 0)
	    {
	    	
	    	for (int i = 0; i < mtDeleteSelfList.size(); i++) {
	    		NFGUID it=mtDeleteSelfList.get(i);
	    		DestroyObject(it);
			}
	        mtDeleteSelfList.clear();
	    }

	    m_pSceneModule.Execute();

	    NFIObject pObject = First();
	    while (pObject != null)
	    {
	        mnCurExeObject = pObject.Self();
	        pObject.Execute();
	        mnCurExeObject = new NFGUID();

	        pObject = Next();
	    }

	    return true;
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

	@Override
	public NFIObject CreateObject(NFGUID self, int nSceneID, int nGroupID,
								  String strClassName, String strConfigIndex, NFDataList arg) {
		
		String __FUNCTION__ = Thread.currentThread() .getStackTrace()[1].getMethodName();
		int __LINE__=0;
		
		NFIObject pObject=null;
		NFGUID ident=self;
		NFSceneInfo pContainerInfo=m_pSceneModule.GetElement(nSceneID);
		if (null!=pContainerInfo) {
			m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(0, nSceneID), "There is no scene", nSceneID, __FUNCTION__, __LINE__);
	        return pObject;
		}
		if (null==pContainerInfo.GetElement(nGroupID)) {
			m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(0, nSceneID), "There is no group", nGroupID, __FUNCTION__, __LINE__);
	        return pObject;
		}
		
		if (ident.IsNull()) {
			ident=CreateGUID();
		}
		
		if (GetElement(ident) != null) {
			m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, ident, "The object has Exists", __FUNCTION__, __LINE__);
			return pObject;
		}
		
		NFIPropertyManager pStaticClassPropertyManager=m_pClassModule.GetClassPropertyManager(strClassName);
		NFIRecordManager pStaticClassRecordManager=m_pClassModule.GetClassRecordManager(strClassName);
		NFIComponentManager pStaticClasComponentManager = m_pClassModule.GetClassComponentManager(strClassName);
		
		if (pStaticClassPropertyManager!=null && pStaticClassRecordManager!=null&& pStaticClasComponentManager!=null) {
			
			pObject=new NFObject(self);
			AddElement(self, pObject);
			 pContainerInfo.AddObjectToGroup(nGroupID, ident, (strClassName == NFrame.Player.ThisName()? true : false));

		        NFIPropertyManager pPropertyManager = pObject.GetPropertyManager();
		        NFIRecordManager pRecordManager = pObject.GetRecordManager();
		        NFIComponentManager pComponentManager = pObject.GetComponentManager();

		        
		        NFIProperty pStaticConfigPropertyInfo = pStaticClassPropertyManager.First();
		        while (pStaticConfigPropertyInfo != null)
		        {
		        	NFIProperty xProperty = pPropertyManager.AddProperty(ident, pStaticConfigPropertyInfo.GetKey(), pStaticConfigPropertyInfo.GetType());

		            xProperty.SetPublic(pStaticConfigPropertyInfo.GetPublic());
		            xProperty.SetPrivate(pStaticConfigPropertyInfo.GetPrivate());
		            xProperty.SetSave(pStaticConfigPropertyInfo.GetSave());
		            xProperty.SetCache(pStaticConfigPropertyInfo.GetCache());
		            xProperty.SetRef(pStaticConfigPropertyInfo.GetRef());
					xProperty.SetUpload(pStaticConfigPropertyInfo.GetUpload());

		            
		            pObject.AddPropertyCallBack(pStaticConfigPropertyInfo.GetKey(), this,OnPropertyCommonEvent);

		            pStaticConfigPropertyInfo = pStaticClassPropertyManager.Next();
		        }

		        NFIRecord pConfigRecordInfo = pStaticClassRecordManager.First();
		        while (pConfigRecordInfo != null)
		        {
		        	NFIRecord xRecord =  pRecordManager.AddRecord(ident,
		                                      pConfigRecordInfo.GetName(),
		                                      pConfigRecordInfo.GetInitData(),
		                                      pConfigRecordInfo.GetTag(),
		                                      pConfigRecordInfo.GetRows());

		             xRecord.SetPublic(pConfigRecordInfo.GetPublic());
		             xRecord.SetPrivate(pConfigRecordInfo.GetPrivate());
		             xRecord.SetSave(pConfigRecordInfo.GetSave());
		             xRecord.SetCache(pConfigRecordInfo.GetCache());
					 xRecord.SetUpload(pConfigRecordInfo.GetUpload());
		            
		            pObject.AddRecordCallBack(pConfigRecordInfo.GetName(), this, OnRecordCommonEvent);

		            pConfigRecordInfo = pStaticClassRecordManager.Next();
		        }

		        NFVector3 vRelivePos = m_pSceneModule.GetRelivePosition(nSceneID, 0);

				pObject.SetPropertyObject(NFrame.IObject.ID(), self);
				pObject.SetPropertyString(NFrame.IObject.ConfigID(), strConfigIndex);
				pObject.SetPropertyString(NFrame.IObject.ClassName(), strClassName);
				pObject.SetPropertyInt(NFrame.IObject.SceneID(), nSceneID);
				pObject.SetPropertyInt(NFrame.IObject.GroupID(), nGroupID);
				pObject.SetPropertyFloat(NFrame.IObject.X(), vRelivePos.X());
				pObject.SetPropertyFloat(NFrame.IObject.Y(), vRelivePos.Y());
				pObject.SetPropertyFloat(NFrame.IObject.Z(), vRelivePos.Z());

				//no data
				DoEvent(ident, strClassName, pObject.GetState(), arg);

		        //////////////////////////////////////////////////////////////////////////
		        
		        NFIPropertyManager pConfigPropertyManager = m_pElementModule.GetPropertyManager(strConfigIndex);
		        NFIRecordManager pConfigRecordManager = m_pElementModule.GetRecordManager(strConfigIndex);

		        if (pConfigPropertyManager!=null && pConfigRecordManager!=null)
		        {
		            NFIProperty pConfigPropertyInfo = pConfigPropertyManager.First();
		            while (null != pConfigPropertyInfo)
		            {
		                if (pConfigPropertyInfo.Changed())
		                {
		                    pPropertyManager.SetProperty(pConfigPropertyInfo.GetKey(), pConfigPropertyInfo.GetValue());
		                }

		                pConfigPropertyInfo = pConfigPropertyManager.Next();
		            }
		        }

		        for (int i = 0; i < arg.GetCount() - 1; i += 2)
		        {
		            String strPropertyName = arg.String(i);
		            if (NFrame.IObject.ConfigID() != strPropertyName
		                && NFrame.IObject.ClassName() != strPropertyName
		                && NFrame.IObject.SceneID() != strPropertyName
						&& NFrame.IObject.ID() != strPropertyName
		                && NFrame.IObject.GroupID() != strPropertyName)
		            {
		                NFIProperty pArgProperty = pStaticClassPropertyManager.GetElement(strPropertyName);
		                if (pArgProperty != null)
		                {
		                    switch (pArgProperty.GetType())
		                    {
		                        case TDATA_INT:
		                            pObject.SetPropertyInt(strPropertyName, arg.Int(i + 1));
		                            break;
		                        case TDATA_FLOAT:
		                            pObject.SetPropertyFloat(strPropertyName, arg.Float(i + 1));
		                            break;
		                        case TDATA_STRING:
		                            pObject.SetPropertyString(strPropertyName, arg.String(i + 1));
		                            break;
		                        case TDATA_OBJECT:
		                            pObject.SetPropertyObject(strPropertyName, arg.Object(i + 1));
		                            break;
		                        default:
		                            break;
		                    }
		                }
		            }
		        }


				pObject.SetState(CLASS_OBJECT_EVENT.COE_CREATE_LOADDATA);
				DoEvent(ident, strClassName, pObject.GetState(), arg);

				pObject.SetState(CLASS_OBJECT_EVENT.COE_CREATE_BEFORE_EFFECT);
				DoEvent(ident, strClassName, pObject.GetState(), arg);

				pObject.SetState(CLASS_OBJECT_EVENT.COE_CREATE_EFFECTDATA);
				DoEvent(ident, strClassName, pObject.GetState(), arg);

				pObject.SetState(CLASS_OBJECT_EVENT.COE_CREATE_AFTER_EFFECT);
				DoEvent(ident, strClassName, pObject.GetState(), arg);

				pObject.SetState(CLASS_OBJECT_EVENT.COE_CREATE_HASDATA);
				DoEvent(ident, strClassName, pObject.GetState(), arg);

				pObject.SetState(CLASS_OBJECT_EVENT.COE_CREATE_FINISH);
				DoEvent(ident, strClassName, pObject.GetState(), arg);
		    }

		    return pObject;
	}

	@Override
	public boolean ExistScene(int nSceneID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ExistObject(NFGUID ident) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ExistObject(NFGUID ident, int nSceneID, int nGroupID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NFIObject GetObject(NFGUID ident) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean DestroyAll() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DestroySelf(NFGUID self) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DestroyObject(NFGUID self) {
		if (self == mnCurExeObject
		        && !self.IsNull())
		    {
		        
		        return DestroySelf(self);
		    }

		    
		    int nGroupID = GetPropertyInt(self, NFrame.IObject.GroupID());
		    int nSceneID = GetPropertyInt(self, NFrame.IObject.SceneID());

		    NFSceneInfo pContainerInfo = m_pSceneModule.GetElement(nSceneID);
		    if (pContainerInfo != null)
		    {
		        String strClassName = GetPropertyString(self, NFrame.IObject.ClassName());

		        pContainerInfo.RemoveObjectFromGroup(nGroupID, self, strClassName == NFrame.Player.ThisName() ? true : false);

		        DoEvent(self, strClassName, CLASS_OBJECT_EVENT.COE_BEFOREDESTROY, new NFDataList());
		        DoEvent(self, strClassName, CLASS_OBJECT_EVENT.COE_DESTROY, new NFDataList());

		        RemoveElement(self);

				m_pEventModule.RemoveEventCallBack(self);
				m_pScheduleModule.RemoveSchedule(self);

		        return true;

		    }

		    m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no scene", nSceneID, "DestroyObject", 0);

		    return false;
	}

	@Override
	public boolean FindProperty(NFGUID self, String strPropertyName) {
		 NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        return pObject.FindProperty(strPropertyName);
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "FindProperty", 0);

		    return false;
	}

	@Override
	public boolean SetPropertyInt(NFGUID self, String strPropertyName,
			int nValue) {
		NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        return pObject.SetPropertyInt(strPropertyName, nValue);
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "SetPropertyInt", 0);

		    return false;
	}

	@Override
	public boolean SetPropertyFloat(NFGUID self, String strPropertyName,
			double dValue) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.SetPropertyFloat(strPropertyName, dValue);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "SetPropertyFloat", 0);

	    return false;
	}

	@Override
	public boolean SetPropertyString(NFGUID self, String strPropertyName,
			String strValue) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.SetPropertyString(strPropertyName, strValue);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "SetPropertyString", 0);

	    return false;
	}

	@Override
	public boolean SetPropertyObject(NFGUID self, String strPropertyName,
			NFGUID objectValue) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.SetPropertyObject(strPropertyName, objectValue);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "SetPropertyObject", 0);

	    return false;
	}

	@Override
	public boolean SetPropertyVector2(NFGUID self, String strPropertyName,
			NFVector2 value) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.SetPropertyVector2(strPropertyName, value);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "SetPropertyVector2", 0);

	    return false;
	}

	@Override
	public boolean SetPropertyVector3(NFGUID self, String strPropertyName,
			NFVector3 value) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.SetPropertyVector3(strPropertyName, value);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "SetPropertyVector3", 0);

	    return false;
	}

	@Override
	public int GetPropertyInt(NFGUID self, String strPropertyName) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetPropertyInt(strPropertyName);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "GetPropertyInt", 0);

	    return 0;
	}

	@Override
	public double GetPropertyFloat(NFGUID self, String strPropertyName) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetPropertyFloat(strPropertyName);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "GetPropertyFloat", 0);

	    return 0.0;
	}

	@Override
	public String GetPropertyString(NFGUID self, String strPropertyName) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetPropertyString(strPropertyName);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "GetPropertyString", 0);

	    return null;
	}

	@Override
	public NFGUID GetPropertyObject(NFGUID self, String strPropertyName) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetPropertyObject(strPropertyName);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "GetPropertyObject", 0);

	    return null;
	}

	@Override
	public NFVector2 GetPropertyVector2(NFGUID self, String strPropertyName) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetPropertyVector2(strPropertyName);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "GetPropertyVector2", 0);

	    return null;
	}

	@Override
	public NFVector3 GetPropertyVector3(NFGUID self, String strPropertyName) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetPropertyVector3(strPropertyName);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strPropertyName + "| There is no object", "GetPropertyVector3", 0);

	    return null;
	}

	@Override
	public NFIRecord FindRecord(NFGUID self, String strRecordName) {
		NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        return pObject.GetRecordManager().GetElement(strRecordName);
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object", "FindRecord", 0);

		    return null;
	}

	@Override
	public boolean ClearRecord(NFGUID self, String strRecordName) {
		NFIRecord pRecord =  FindRecord(self, strRecordName);
		    if (pRecord != null)
		    {
		        return pRecord.Clear();
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no record", "ClearRecord", 0);

		    return false;
	}

	@Override
	public boolean SetRecordInt(NFGUID self, String strRecordName, int nRow,
                                int nCol, int nValue) {
		 NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        if (!pObject.SetRecordInt(strRecordName, nRow, nCol, nValue))
		        {
		            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordInt", 0);
		        }
		        else
		        {
		            return true;
		        }
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object", "SetRecordInt", 0);

		    return false;
	}

	@Override
	public boolean SetRecordFloat(NFGUID self, String strRecordName, int nRow,
                                  int nCol, double dwValue) {
		 NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        if (!pObject.SetRecordFloat(strRecordName, nRow, nCol, dwValue))
		        {
		            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordFloat", 0);
		        }
		        else
		        {
		            return true;
		        }
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object", "SetRecordFloat", 0);

		    return false;
	}

	@Override
	public boolean SetRecordString(NFGUID self, String strRecordName,
			int nRow, int nCol, String strValue) {
		 NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        if (!pObject.SetRecordString(strRecordName, nRow, nCol, strValue))
		        {
		            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordString", 0);
		        }
		        else
		        {
		            return true;
		        }
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object", "SetRecordString", 0);

		    return false;
	}

	@Override
	public boolean SetRecordObject(NFGUID self, String strRecordName,
			int nRow, int nCol, NFGUID objectValue) {
		 NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        if (!pObject.SetRecordObject(strRecordName, nRow, nCol, objectValue))
		        {
		            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordObject", 0);
		        }
		        else
		        {
		            return true;
		        }
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object", "SetRecordObject", 0);

		    return false;
	}

	@Override
	public boolean SetRecordVector2(NFGUID self, String strRecordName,
			int nRow, int nCol, NFVector2 value) {
		 NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        if (!pObject.SetRecordVector2(strRecordName, nRow, nCol, value))
		        {
		            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordVector2", 0);
		        }
		        else
		        {
		            return true;
		        }
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object", "SetRecordVector2", 0);

		    return false;
	}

	@Override
	public boolean SetRecordVector3(NFGUID self, String strRecordName,
			int nRow, int nCol, NFVector3 value) {
		 NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        if (!pObject.SetRecordVector3(strRecordName, nRow, nCol, value))
		        {
		            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordVector3", 0);
		        }
		        else
		        {
		            return true;
		        }
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object", "SetRecordVector3", 0);

		    return false;
	}

	@Override
	public boolean SetRecordInt(NFGUID self, String strRecordName, int nRow,
                                String strColTag, int value) {
		NFIObject pObject = GetElement(self);
		    if (pObject != null)
		    {
		        if (!pObject.SetRecordInt(strRecordName, nRow, strColTag, value))
		        {
		            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordInt", 0);
		        }
		        else
		        {
		            return true;
		        }
		    }

		    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object",  "SetRecordInt", 0);

		    return false;
	}

	@Override
	public boolean SetRecordFloat(NFGUID self, String strRecordName, int nRow,
                                  String strColTag, double value) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        if (!pObject.SetRecordFloat(strRecordName, nRow, strColTag, value))
	        {
	            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordFloat", 0);
	        }
	        else
	        {
	            return true;
	        }
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object",  "SetRecordFloat", 0);

	    return false;
	}

	@Override
	public boolean SetRecordString(NFGUID self, String strRecordName,
                                   int nRow, String strColTag, String value) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        if (!pObject.SetRecordString(strRecordName, nRow, strColTag, value))
	        {
	            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordString", 0);
	        }
	        else
	        {
	            return true;
	        }
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object",  "SetRecordString", 0);

	    return false;
	}

	@Override
	public boolean SetRecordObject(NFGUID self, String strRecordName,
                                   int nRow, String strColTag, NFGUID value) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        if (!pObject.SetRecordObject(strRecordName, nRow, strColTag, value))
	        {
	            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordObject", 0);
	        }
	        else
	        {
	            return true;
	        }
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object",  "SetRecordObject", 0);

	    return false;
	}

	@Override
	public boolean SetRecordVector2(NFGUID self, String strRecordName,
                                    int nRow, String strColTag, NFVector2 value) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        if (!pObject.SetRecordVector2(strRecordName, nRow, strColTag, value))
	        {
	            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordVector2", 0);
	        }
	        else
	        {
	            return true;
	        }
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object",  "SetRecordVector2", 0);

	    return false;
	}

	@Override
	public boolean SetRecordVector3(NFGUID self, String strRecordName,
                                    int nRow, String strColTag, NFVector3 value) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        if (!pObject.SetRecordVector3(strRecordName, nRow, strColTag, value))
	        {
	            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName, "error for row or col", "SetRecordVector3", 0);
	        }
	        else
	        {
	            return true;
	        }
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, strRecordName + "| There is no object",  "SetRecordVector3", 0);

	    return false;
	}

	@Override
	public int GetRecordInt(NFGUID self, String strRecordName, int nRow,
                            int nCol) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordInt(strRecordName, nRow, nCol);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object",  "GetRecordInt", 0);

	    return 0;
	}

	@Override
	public double GetRecordFloat(NFGUID self, String strRecordName, int nRow,
                                 int nCol) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordFloat(strRecordName, nRow, nCol);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object",  "GetRecordFloat", 0);

	    return 0.0;
	}

	@Override
	public String GetRecordString(NFGUID self, String strRecordName, int nRow,
                                  int nCol) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordString(strRecordName, nRow, nCol);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object",  "GetRecordString", 0);

	    return null;
	}

	@Override
	public NFGUID GetRecordObject(NFGUID self, String strRecordName,
			int nRow, int nCol) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordObject(strRecordName, nRow, nCol);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object",  "GetRecordObject", 0);

	    return null;
	}

	@Override
	public NFVector2 GetRecordVector2(NFGUID self, String strRecordName,
			int nRow, int nCol) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordVector2(strRecordName, nRow, nCol);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object",  "GetRecordVector2", 0);

	    return null;
	}

	@Override
	public NFVector3 GetRecordVector3(NFGUID self, String strRecordName,
			int nRow, int nCol) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordVector3(strRecordName, nRow, nCol);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object",  "GetRecordVector3", 0);

	    return null;
	}

	@Override
	public int GetRecordInt(NFGUID self, String strRecordName, int nRow,
                            String strColTag) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordInt(strRecordName, nRow, strColTag);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object","GetRecordInt", 0);

	    return 0;
	}

	@Override
	public double GetRecordFloat(NFGUID self, String strRecordName, int nRow,
                                 String strColTag) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordFloat(strRecordName, nRow, strColTag);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object","GetRecordFloat", 0);

	    return 0.0;
	}

	@Override
	public String GetRecordString(NFGUID self, String strRecordName, int nRow,
                                  String strColTag) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordString(strRecordName, nRow, strColTag);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object","GetRecordString", 0);

	    return null;
	}

	@Override
	public NFGUID GetRecordObject(NFGUID self, String strRecordName,
			int nRow, String strColTag) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordObject(strRecordName, nRow, strColTag);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object","GetRecordObject", 0);

	    return null;
	}

	@Override
	public NFVector2 GetRecordVector2(NFGUID self, String strRecordName,
			int nRow, String strColTag) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordVector2(strRecordName, nRow, strColTag);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object","GetRecordVector2", 0);

	    return null;
	}

	@Override
	public NFVector3 GetRecordVector3(NFGUID self, String strRecordName,
			int nRow, String strColTag) {
		NFIObject pObject = GetElement(self);
	    if (pObject != null)
	    {
	        return pObject.GetRecordVector3(strRecordName, nRow, strColTag);
	    }

	    m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, self, "There is no object","GetRecordVector3", 0);

	    return null;
	}

	@Override
	public NFGUID CreateGUID() {
		 long value = 0;   
		    long time = NFPlatform.NFGetTime();

		    
		    //value = time << 16;
		    value = time * 1000000;

		    
		    //value |= nGUIDIndex++;
		    value += nGUIDIndex++;

		    //if (sequence_ == 0x7FFF)
		    if (nGUIDIndex == 999999)
		    {
		        nGUIDIndex = 0;
		    }

		    NFGUID xID=new NFGUID();
		    xID.nHead64 = pPluginManager.GetAppID();
		    xID.nData64 = value;

		    return xID;
	}

	@Override
	public boolean CreateScene(int nSceneID) {
		 NFSceneInfo pSceneInfo = m_pSceneModule.GetElement(nSceneID);
		    if (pSceneInfo != null)
		    {
		        return false;
		    }

		    pSceneInfo = new NFSceneInfo(nSceneID);
		    if (pSceneInfo != null)
		    {
		        m_pSceneModule.AddElement(nSceneID, pSceneInfo);
		        
		        NFSceneGroupInfo pGroupInfo = new NFSceneGroupInfo(nSceneID, 0);
		        if (null != pGroupInfo)
		        {
		            pSceneInfo.AddElement(0, pGroupInfo);

		            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, new NFGUID(), "Create scene success, groupId:0, scene id:", nSceneID, "CreateScene", 0);

		            return true;
		        }
		    }

		    return false;
	}

	@Override
	public boolean DestroyScene(int nSceneID) {
		 m_pSceneModule.RemoveElement(nSceneID);

		 return true;
	}

	@Override
	public int GetOnLineCount() {
		 int nCount = 0;
		 NFSceneInfo pSceneInfo = m_pSceneModule.First();
		    while (pSceneInfo != null)
		    {
		        NFSceneGroupInfo pGroupInfo = pSceneInfo.First();
		        while (pGroupInfo != null)
		        {
		            nCount += pGroupInfo.mxPlayerList.Count();
		            pGroupInfo = pSceneInfo.Next();
		        }

		        pSceneInfo = m_pSceneModule.Next();
		    }

		    return nCount;
	}

	@Override
	public int GetMaxOnLineCount() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	public int RequestGroupScene(int nSceneID) {


		    return -1;
	}

	@Override
	public boolean ReleaseGroupScene(int nSceneID, int nGroupID) {
		NFSceneInfo pSceneInfo = m_pSceneModule.GetElement(nSceneID);
		if (pSceneInfo != null)
		{
			if (nGroupID > 0)
			{
				m_pSceneModule.DestroySceneNPC(nSceneID, nGroupID);

				pSceneInfo.RemoveElement(nGroupID);
			}
		}

	    return false;
	}

	@Override
	public boolean ExitGroupScene(int nSceneID, int nGroupID) {
		NFSceneInfo pSceneInfo = m_pSceneModule.GetElement(nSceneID);
		    if (pSceneInfo != null)
		    {
		    	NFSceneGroupInfo pGroupInfo = pSceneInfo.GetElement(nGroupID);
		        if (pGroupInfo != null)
		        {
		            return true;
		        }
		    }

		    return false;
	}

	@Override
	public boolean GetGroupObjectList(int nSceneID, int nGroupID,
			NFDataList list) {
		NFSceneInfo pSceneInfo = m_pSceneModule.GetElement(nSceneID);
		    if (pSceneInfo != null)
		    {

		    	NFSceneGroupInfo pGroupInfo = pSceneInfo.GetElement(nGroupID);
		        if (pGroupInfo != null)
		        {
		        	NFGUID ident = new NFGUID();
		            int pRet = pGroupInfo.mxPlayerList.First(ident);
		            while (!ident.IsNull())
		            {
		                list.Add(ident);

		                ident = new NFGUID();
		                pRet = pGroupInfo.mxPlayerList.Next(ident);
		            }

		            pRet = pGroupInfo.mxOtherList.First(ident);
		            while (!ident.IsNull())
		            {
		                list.Add(ident);

		                ident =new NFGUID();
		                pRet = pGroupInfo.mxOtherList.Next(ident);
		            }

		            return true;
		        }
		    }

		    return false;
	}

	@Override
	public boolean GetGroupObjectList(int nSceneID, int nGroupID,
			NFDataList list, NFGUID noSelf) {
		NFSceneInfo pSceneInfo = m_pSceneModule.GetElement(nSceneID);
		if (pSceneInfo != null)
		{

			NFSceneGroupInfo pGroupInfo = pSceneInfo.GetElement(nGroupID);
			if (pGroupInfo != null)
			{
				NFGUID ident = new NFGUID();
				int pRet = pGroupInfo.mxPlayerList.First(ident);
				while (!ident.IsNull() && noSelf != ident)
				{
					list.Add(ident);

					ident = new NFGUID();
					pRet = pGroupInfo.mxPlayerList.Next(ident);
				}

				pRet = pGroupInfo.mxOtherList.First(ident);
				while (!ident.IsNull() && noSelf != ident)
				{
					list.Add(ident);

					ident = new NFGUID();
					pRet = pGroupInfo.mxOtherList.Next(ident);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean GetGroupObjectList(int nSceneID, int nGroupID,
			NFDataList list, boolean bPlayer) {
		NFSceneInfo pSceneInfo = m_pSceneModule.GetElement(nSceneID);
		if (pSceneInfo != null)
		{
			NFSceneGroupInfo pGroupInfo = pSceneInfo.GetElement(nGroupID);
			if (pGroupInfo != null)
			{
				if (bPlayer)
				{
					NFGUID ident = new NFGUID();
					int pRet = pGroupInfo.mxPlayerList.First(ident);
					while (!ident.IsNull())
					{
						list.Add(ident);

						ident = new NFGUID();
						pRet = pGroupInfo.mxPlayerList.Next(ident);
					}
				}
				else
				{
					NFGUID ident = new NFGUID();
					int pRet = pGroupInfo.mxOtherList.First(ident);
					while (!ident.IsNull())
					{
						list.Add(ident);

						ident = new NFGUID();
						pRet = pGroupInfo.mxOtherList.Next(ident);
					}
				}
				
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean GetGroupObjectList(int nSceneID, int nGroupID,
			NFDataList list, boolean bPlayer, NFGUID noSelf) {
		NFSceneInfo pSceneInfo = m_pSceneModule.GetElement(nSceneID);
		if (pSceneInfo != null)
		{

			NFSceneGroupInfo pGroupInfo = pSceneInfo.GetElement(nGroupID);
			if (pGroupInfo != null)
			{
				if (bPlayer)
				{
					NFGUID ident = new NFGUID();
					int pRet = pGroupInfo.mxPlayerList.First(ident);
					while (!ident.IsNull() && ident != noSelf)
					{
						list.Add(ident);

						ident = new NFGUID();
						pRet = pGroupInfo.mxPlayerList.Next(ident);
					}
				}
				else
				{
					NFGUID ident = new NFGUID();
					int pRet = pGroupInfo.mxOtherList.First(ident);
					while (!ident.IsNull() && ident != noSelf)
					{
						list.Add(ident);

						ident = new NFGUID();
						pRet = pGroupInfo.mxOtherList.Next(ident);
					}
				}

				return true;
			}
		}
		return false;
	}

	@Override
	public boolean GetGroupObjectList(int nSceneID, int nGroupID,
                                      String strClassName, NFDataList list) {
		NFDataList xDataList=new NFDataList();
		if (GetGroupObjectList(nSceneID, nGroupID, xDataList))
		{
			for (int i = 0; i < xDataList.GetCount(); i++)
			{
				NFGUID xID = xDataList.Object(i);
				if (xID.IsNull())
				{
					continue;
				}

				if (this.GetPropertyString(xID, NFrame.IObject.ClassName()) == strClassName)
				{
					list.AddObject(xID);
				}
			}
			
			return true;
		}

		return false;
	}

	@Override
	public boolean GetGroupObjectList(int nSceneID, int nGroupID,
                                      String strClassName, NFGUID noSelf, NFDataList list) {
		NFDataList xDataList=new NFDataList();
		if (GetGroupObjectList(nSceneID, nGroupID, xDataList))
		{
			for (int i = 0; i < xDataList.GetCount(); i++)
			{
				NFGUID xID = xDataList.Object(i);
				if (xID.IsNull())
				{
					continue;
				}

				if (this.GetPropertyString(xID, NFrame.IObject.ClassName()) == strClassName
					&& xID != noSelf)
				{
					list.AddObject(xID);
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public int GetObjectByProperty(int nSceneID, int nGroupID,
                                   String strPropertyName, NFDataList valueArg, NFDataList list) {
		NFDataList varObjectList=new NFDataList();
		GetGroupObjectList(nSceneID, nGroupID, varObjectList);

	    int nWorldCount = varObjectList.GetCount();
	    for (int i = 0; i < nWorldCount; i++)
	    {
	    	NFGUID ident = varObjectList.Object(i);
	        if (this.FindProperty(ident, strPropertyName))
	        {
	            NFDATA_TYPE eType = valueArg.Type(0);
	            switch (eType)
	            {
	                case TDATA_INT:
	                {
	                    int nValue = GetPropertyInt(ident, strPropertyName);
	                    if (valueArg.Int(0) == nValue)
	                    {
	                        list.Add(ident);
	                    }
	                }
	                break;
	                case TDATA_STRING:
	                {
	                    String strValue = GetPropertyString(ident, strPropertyName);
	                    String strCompareValue = valueArg.String(0);
	                    if (strValue == strCompareValue)
	                    {
	                        list.Add(ident);
	                    }
	                }
	                break;
	                case TDATA_OBJECT:
	                {
	                	NFGUID identObject = GetPropertyObject(ident, strPropertyName);
	                    if (valueArg.Object(0) == identObject)
	                    {
	                        list.Add(ident);
	                    }
	                }
	                break;
	                default:
	                    break;
	            }
	        }
	    }

	    return list.GetCount();
	}

	@Override
	public void Random(int nStart, int nEnd, int nCount, NFDataList valueList) {
		 if (mnRandomPos + nCount >= mvRandom.size())
		    {
		        mnRandomPos = 0;
		    }

		    for (int i = mnRandomPos; i < mnRandomPos + nCount; i++)
		    {
		        float fRanValue = mvRandom.get(i);
		        int nValue =(int) ((nEnd - nStart) * fRanValue + nStart);
		        valueList.Add((int)nValue);
		    }

		    mnRandomPos += nCount;

	}

	@Override
	public int Random(int nStart, int nEnd) {
		if (mnRandomPos + 1 >= mvRandom.size())
		{
			mnRandomPos = 0;
		}

		float fRanValue = mvRandom.get(mnRandomPos);
		mnRandomPos++;

		int nValue =(int) ( (nEnd - nStart) * fRanValue + nStart);
		return nValue;
	}

	@Override
	public boolean LogStack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean LogInfo(NFGUID ident) {
	    
	    NFIObject pObject = GetObject(ident);
	    if (pObject != null)
	    {
			int nSceneID = GetPropertyInt(ident, NFrame.IObject.SceneID());
			int nGroupID = GetPropertyInt(ident, NFrame.IObject.GroupID());

	       // m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, ident, "//----------child object list-------- SceneID = ", nSceneID);

	        NFDataList valObjectList=new NFDataList();
			GetGroupObjectList(nSceneID, nGroupID, valObjectList);
	        for (int i  = 0; i < valObjectList.GetCount(); i++)
	        {
	           NFGUID targetIdent = valObjectList.Object(i);
	           LogInfo(targetIdent);
	       }
	    }
	    else
	    {
	       // m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, ident, "", __FUNCTION__, __LINE__);
	    }

	    return true;
	}

	@Override
	public boolean LogSelfInfo(NFGUID ident) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DoEvent(NFGUID self, String strClassName,
			CLASS_OBJECT_EVENT eEvent, NFDataList valueList) {
		// TODO Auto-generated method stub
		return m_pClassModule.DoEvent(self, strClassName, eEvent, valueList);
	}

	@Override
	public boolean RegisterCommonClassEvent(CLASS_EVENT_FUNCTOR cb) {
		 mtCommonClassCallBackList.add(cb);
		    return true;
	}

	@Override
	public boolean RegisterCommonPropertyEvent(PROPERTY_EVENT_FUNCTOR cb) {
		 mtCommonPropertyCallBackList.add(cb);
		    return true;
	}

	@Override
	public boolean RegisterCommonRecordEvent(RECORD_EVENT_FUNCTOR cb) {
		mtCommonRecordCallBackList.add(cb);
	    return true;
	}

	@Override
	public boolean AddClassCallBack(String strClassName, CLASS_EVENT_FUNCTOR cb) {
		// TODO Auto-generated method stub
		 return m_pClassModule.AddClassCallBack(strClassName, cb);
	}

	@Override
	public void InitRandom() {
		// TODO Auto-generated method stub
		 mvRandom.clear();

		    int nRandomMax = 100;
		    mnRandomPos = 0;
		    java.util.Random random=new java.util.Random();
		    List<Float> list=new ArrayList<Float>();

		    for (int i = 0; i < nRandomMax; i++)
		    {
		    	float val=-1;
		    	while (!list.contains(val)||val==-1) {
		    		val=random.nextFloat();
		    		list.add(val);
					mvRandom.add(val);
				}
		    }
	}

	@Override
	public int OnClassCommonEvent(NFGUID self, String strClassName,
			CLASS_OBJECT_EVENT eClassEvent, NFDataList var) {
		for (int i = 0; i < mtCommonClassCallBackList.size(); i++) {
			CLASS_EVENT_FUNCTOR pFun=mtCommonClassCallBackList.get(i);
			pFun.operator(self, strClassName, eClassEvent, var);
		}
		return 0;
	}
	
	private PROPERTY_EVENT_FUNCTOR OnPropertyCommonEvent=new PROPERTY_EVENT_FUNCTOR() {

		@Override
		public int operator(NFGUID guid, String str, NFData pData,
                            NFData zData) {
			NFIObject xObject = GetElement(guid);
			if (xObject != null)
			{
				if (xObject.GetState() == CLASS_OBJECT_EVENT.COE_CREATE_HASDATA
					|| xObject.GetState() == CLASS_OBJECT_EVENT.COE_CREATE_FINISH)
				{
					
					for (int i = 0; i < mtCommonPropertyCallBackList.size(); i++) {
						PROPERTY_EVENT_FUNCTOR pFun=mtCommonPropertyCallBackList.get(i);
						pFun.operator(guid, str, pData, zData);
					}
				}
			}
			return 0;
		}
	};

	
		
//		NFIObject xObject = GetElement(self);
//		if (xObject != null)
//		{
//			if (xObject.GetState() == CLASS_OBJECT_EVENT.COE_CREATE_HASDATA
//				|| xObject.GetState() == CLASS_OBJECT_EVENT.COE_CREATE_FINISH)
//			{
//				
//				for (int i = 0; i < mtCommonPropertyCallBackList.size(); i++) {
//					PROPERTY_EVENT_FUNCTOR pFun=mtCommonPropertyCallBackList.get(i);
//					pFun._handler.operator(self, strPropertyName, oldVar, newVar);
//				}
//			}
//		}
		
	   // return 0;

	private RECORD_EVENT_FUNCTOR OnRecordCommonEvent=new RECORD_EVENT_FUNCTOR() {

		@Override
		public int operator(NFGUID guid, RECORD_EVENT_DATA red,
				NFData data1, NFData data2) {
			NFIObject xObject = GetElement(guid);
			if (xObject != null)
			{
				if (xObject.GetState() == CLASS_OBJECT_EVENT.COE_CREATE_HASDATA
					|| xObject.GetState() == CLASS_OBJECT_EVENT.COE_CREATE_FINISH)
				{
					
					for (int i = 0; i < mtCommonRecordCallBackList.size(); i++) {
						RECORD_EVENT_FUNCTOR pFun=mtCommonRecordCallBackList.get(i);
						pFun.operator(guid, red, data1, data2);
					}
				}
			}

		    return 0;
		}
	};
	

	@Override
	public void ProcessMemFree() {
		if (nLastTime + 30 > pPluginManager.GetNowTime())
	    {
	        return;
	    }

	    nLastTime = pPluginManager.GetNowTime();

	    //NFCMemManager.GetSingletonPtr()->FreeMem();

	}

}
