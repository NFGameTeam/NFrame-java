package cn.yeegro.nframe.plugin.config;

import cn.yeegro.nframe.comm.code.api.NFGUID;
import org.pf4j.Extension;
import cn.yeegro.nframe.comm.code.iface.NFIProperty;
import cn.yeegro.nframe.comm.code.iface.NFIRecord;
import cn.yeegro.nframe.comm.code.iface.NFPlatform;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.module.*;
import cn.yeegro.nframe.comm.code.util.NFDATA_TYPE;
import cn.yeegro.nframe.comm.code.util.NFData;
import cn.yeegro.nframe.comm.code.util.NFMapEx;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.tools.file.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Extension
public class NFElementModule extends NFMapEx<String, ElementConfigInfo>
		implements NFIElementModule {

	private NFIPluginManager pPluginManager;

	private NFIClassModule m_pClassModule;
	private boolean mbLoaded;

	private XMLUtil xmlUtil;
	private String __FILE__ = "NFElementModule";

	private static NFElementModule SingletonPtr = null;

	public static NFElementModule GetSingletonPtr() {
		if (null == SingletonPtr) {
			SingletonPtr = new NFElementModule();
			return SingletonPtr;
		} else {
			return SingletonPtr;
		}
	}

	public NFElementModule() {
		pPluginManager = NFPluginManager.GetSingletonPtr();
		xmlUtil = new XMLUtil();
		mbLoaded = false;

	}

	@Override
	public boolean Awake() {
		return false;
	}

	@Override
	public boolean Init() {
		m_pClassModule = pPluginManager.FindModule(NFIClassModule.class);
		Load();

		return true;
	}

	public boolean LegalNumber(String str) {
		int nLen = str.length();
		if (nLen <= 0) {
			return false;
		}

		int nStart = 0;
		if ('-' == str.charAt(0)) {
			nStart = 1;
		}

		for (int i = nStart; i < nLen; ++i) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean AfterInit() {
		CheckRef();
		return true;
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
		return true;
	}

	@Override
	public boolean BeforeShut() {
		return true;
	}

	@Override
	public boolean Shut() {
		Clear();
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
	public boolean Load() {

		if (mbLoaded) {
			return false;
		}

		NFIClass pLogicClass = m_pClassModule.First();
		while (pLogicClass != null) {
			String strInstancePath = pLogicClass.GetInstancePath();
			if (strInstancePath.isEmpty()) {
				pLogicClass = m_pClassModule.Next();
				continue;
			}
			// ////////////////////////////////////////////////////////////////////////
			String strFile = pPluginManager.GetConfigPath() + strInstancePath;

			Document xDoc = null;
			try {
				xDoc = xmlUtil.parseFile(new File(strFile));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Node root = xDoc.getFirstChild();

			for (int i = 0; i < root.getChildNodes().getLength(); i++) {
				Node attrNode = root.getChildNodes().item(i);
				if (attrNode instanceof Element) {
					Load(attrNode, pLogicClass);
				}
			}

			mbLoaded = true;
			pLogicClass = m_pClassModule.Next();
		}

		return true;
	}

	public boolean CheckRef() {
		String __FUNCTION__ = "CheckRef";
		NFIClass pLogicClass = m_pClassModule.First();
		while (pLogicClass != null) {
			NFIPropertyManager pClassPropertyManager = pLogicClass
					.GetPropertyManager();
			if (pClassPropertyManager != null) {
				NFIProperty pProperty = pClassPropertyManager.First();
				while (pProperty != null) {
					// if one property is ref,check every config
					if (pProperty.GetRef()) {
						List<String> strIdList = pLogicClass.GetIDList();
						for (int i = 0; i < strIdList.size(); ++i) {
							String strId = strIdList.get(i);

							String strRefValue = this.GetPropertyString(strId,
									pProperty.GetKey());
							if (this.GetElement(strRefValue) == null) {
								StringBuffer msg = new StringBuffer();
								msg.append("check ref failed id: ")
										.append(strRefValue).append(" in ")
										.append(pLogicClass.GetClassName());
								NFPlatform.NFASSERT(0, msg.toString(),
										__FILE__, __FUNCTION__);
							}
						}
					}
					pProperty = pClassPropertyManager.Next();
				}
			}
			// ////////////////////////////////////////////////////////////////////////
			pLogicClass = m_pClassModule.Next();
		}

		return false;
	}

	boolean Load(Node attrNode, NFIClass pLogicClass) {
		String __FUNCTION__ = "Load";
		// attrNode is the node of a object
		String strConfigID = xmlUtil.getAttrVal(attrNode, "Id");
		if (strConfigID.isEmpty()) {
			NFPlatform.NFASSERT(0, strConfigID, __FILE__, __FUNCTION__);
			return false;
		}

		if (ExistElement(strConfigID)) {
			NFPlatform.NFASSERT(0, strConfigID, __FILE__, __FUNCTION__);
			return false;
		}

		ElementConfigInfo pElementInfo = new ElementConfigInfo();
		AddElement(strConfigID, pElementInfo);

		// can find all configid by class name
		pLogicClass.AddId(strConfigID);

		// ElementConfigInfo* pElementInfo = CreateElement( strConfigID,
		// pElementInfo );
		NFIPropertyManager pElementPropertyManager = pElementInfo
				.GetPropertyManager();
		NFIRecordManager pElementRecordManager = pElementInfo
				.GetRecordManager();

		// 1.add property
		// 2.set the default value of them
		NFIPropertyManager pClassPropertyManager = pLogicClass
				.GetPropertyManager();
		NFIRecordManager pClassRecordManager = pLogicClass.GetRecordManager();
		if (pClassPropertyManager != null && pClassRecordManager != null) {
			NFIProperty pProperty = pClassPropertyManager.First();
			while (pProperty != null) {

				pElementPropertyManager.AddProperty(new NFGUID(), pProperty);

				pProperty = pClassPropertyManager.Next();
			}

			NFIRecord pRecord = pClassRecordManager.First();
			while (pRecord != null) {
				NFIRecord xRecord = pElementRecordManager.AddRecord(
						new NFGUID(), pRecord.GetName(),
						pRecord.GetInitData(), pRecord.GetTag(),
						pRecord.GetRows());

				xRecord.SetPublic(pRecord.GetPublic());
				xRecord.SetPrivate(pRecord.GetPrivate());
				xRecord.SetSave(pRecord.GetSave());
				xRecord.SetCache(pRecord.GetCache());
				xRecord.SetUpload(pRecord.GetUpload());

				pRecord = pClassRecordManager.Next();
			}

		}

		// 3.set the config value to them

		// char* pstrConfigID = attrNode.first_attribute( "ID" );

		for (int i = 0; i < attrNode.getAttributes().getLength(); i++) {
			Node pAttribute = attrNode.getAttributes().item(i);
			String pstrConfigName = pAttribute.getNodeName();
			String pstrConfigValue = pAttribute.getNodeValue();
			NFIProperty temProperty = pElementPropertyManager
					.GetElement(pstrConfigName);
			if (temProperty == null) {
				continue;
			}

			NFData var = new NFData();
			NFDATA_TYPE eType = temProperty.GetType();
			switch (eType) {
			case TDATA_INT: {
				if (!LegalNumber(pstrConfigValue)) {
					NFPlatform.NFASSERT(0, temProperty.GetKey(), __FILE__,
							__FUNCTION__);
				}
				var.SetInt(Integer.valueOf(pstrConfigValue));
			}
				break;
			case TDATA_FLOAT: {
				if (pstrConfigValue.length() <= 0) {
					NFPlatform.NFASSERT(0, temProperty.GetKey(), __FILE__,
							__FUNCTION__);
				}
				var.SetFloat(Double.valueOf(pstrConfigValue));
			}
				break;
			case TDATA_STRING: {
				var.SetString(pstrConfigValue);
			}
				break;
			case TDATA_OBJECT: {
				if (pstrConfigValue.length() <= 0) {
					NFPlatform.NFASSERT(0, temProperty.GetKey(), __FILE__,
							__FUNCTION__);
				}
				var.SetObject(new NFGUID());
			}
				break;
			case TDATA_VECTOR2: {
				if (pstrConfigValue.length() != 0) {
					NFPlatform.NFASSERT(0, temProperty.GetKey(), __FILE__,
							__FUNCTION__);
				}
				NFVector2 tmp = new NFVector2();
				tmp.FromString(pstrConfigValue);
				var.SetVector2(tmp);
			}
				break;
			case TDATA_VECTOR3: {
				if (pstrConfigValue.length() != 0) {
					NFPlatform.NFASSERT(0, temProperty.GetKey(), __FILE__,
							__FUNCTION__);
				}
				NFVector3 tmp = new NFVector3();
				tmp.FromString(pstrConfigValue);
				var.SetVector3(tmp);
			}
				break;
			default:
				NFPlatform.NFASSERT(0, temProperty.GetKey(), __FILE__,
						__FUNCTION__);
				break;
			}

			temProperty.SetValue(var);
			if (eType == NFDATA_TYPE.TDATA_STRING) {
				temProperty.DeSerialization();
			}

		}

		NFData xData = new NFData();
		xData.SetString(pLogicClass.GetClassName());
		pElementPropertyManager.SetProperty("ClassName", xData);

		return true;
	}

	@Override
	public boolean Save() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Clear() {
		ClearAll();

		mbLoaded = false;
		return true;
	}

	@Override
	public boolean LoadSceneInfo(String strFileName, String strClassName) {

		Document xDoc = null;
		try {
			xDoc = xmlUtil.parseFile(new File(strFileName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		NFIClass pLogicClass = m_pClassModule.GetElement(strClassName);

		if (pLogicClass != null) {
			// support for unlimited layer class inherits
			Node root = xDoc.getFirstChild();

			for (int i = 0; i < root.getChildNodes().getLength(); i++) {
				Node attrNode = root.getChildNodes().item(i);

				Load(attrNode, pLogicClass);
			}
		} else {
			System.out.println("error load scene info failed, name is:"
					+ strClassName + " file name is :");
		}

		return true;
	}

	public boolean ExistElement(String strConfigName) {
		ElementConfigInfo pElementInfo = GetElement(strConfigName);
		if (pElementInfo != null) {
			return true;
		}

		return false;
	}

	@Override
	public boolean ExistElement(String strClassName, String strConfigName) {
		ElementConfigInfo pElementInfo = GetElement(strConfigName);
		if (pElementInfo == null) {
			return false;
		}

		String strClass = pElementInfo.GetPropertyManager().GetPropertyString(
				"ClassName");
		if (strClass != strClassName) {
			return false;
		}

		return true;
	}

	@Override
	public NFIPropertyManager GetPropertyManager(String strConfigName) {
		ElementConfigInfo pElementInfo = GetElement(strConfigName);
		if (pElementInfo != null) {
			return pElementInfo.GetPropertyManager();
		}

		return null;
	}

	@Override
	public NFIRecordManager GetRecordManager(String strConfigName) {
		ElementConfigInfo pElementInfo = GetElement(strConfigName);
		if (pElementInfo != null) {
			return pElementInfo.GetRecordManager();
		}
		return null;
	}

	@Override
	public NFIComponentManager GetComponentManager(String strConfigName) {
		ElementConfigInfo pElementInfo = GetElement(strConfigName);
		if (pElementInfo != null) {
			return pElementInfo.GetComponentManager();
		}

		return null;
	}

	@Override
	public int GetPropertyInt(String strConfigName, String strPropertyName) {
		NFIProperty pProperty = GetProperty(strConfigName, strPropertyName);
		if (pProperty != null) {
			return pProperty.GetInt();
		}

		return 0;
	}

	@Override
	public double GetPropertyFloat(String strConfigName, String strPropertyName) {
		NFIProperty pProperty = GetProperty(strConfigName, strPropertyName);
		if (pProperty != null) {
			return pProperty.GetFloat();
		}

		return 0.0;
	}

	@Override
	public String GetPropertyString(String strConfigName, String strPropertyName) {
		NFIProperty pProperty = GetProperty(strConfigName, strPropertyName);
		if (pProperty != null) {
			return pProperty.GetString();
		}
		return null;
	}

	@Override
	public List<String> GetListByProperty(String strClassName,
                                          String strPropertyName, int nValue) {
		List<String> xList = new ArrayList<String>();

		NFIClass xClass = m_pClassModule.GetElement(strClassName);
		if (null != xClass) {
			List<String> xElementList = xClass.GetIDList();
			for (int i = 0; i < xElementList.size(); ++i) {
				String strConfigID = xElementList.get(i);
				int nElementValue = GetPropertyInt(strConfigID, strPropertyName);
				if (nValue == nElementValue) {
					xList.add(strConfigID);
				}
			}
		}
		return xList;
	}

	@Override
	public List<String> GetListByProperty(String strClassName,
                                          String strPropertyName, String nValue) {
		List<String> xList = new ArrayList<String>();

		NFIClass xClass = m_pClassModule.GetElement(strClassName);
		if (null != xClass) {
			List<String> xElementList = xClass.GetIDList();
			for (int i = 0; i < xElementList.size(); ++i) {
				String strConfigID = xElementList.get(i);
				String strElementValue = GetPropertyString(strConfigID,
						strPropertyName);
				if (nValue == strElementValue) {
					xList.add(strConfigID);
				}
			}
		}

		return xList;
	}

	NFIProperty GetProperty(String strConfigName, String strPropertyName) {
		ElementConfigInfo pElementInfo = GetElement(strConfigName);
		if (pElementInfo != null) {
			return pElementInfo.GetPropertyManager()
					.GetElement(strPropertyName);
		}

		return null;
	}

}