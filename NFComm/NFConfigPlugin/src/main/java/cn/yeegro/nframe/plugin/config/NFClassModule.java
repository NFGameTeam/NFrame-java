package cn.yeegro.nframe.plugin.config;


import cn.yeegro.nframe.comm.code.api.NFGUID;
import org.pf4j.Extension;
import cn.yeegro.nframe.comm.code.functor.CLASS_EVENT_FUNCTOR;
import cn.yeegro.nframe.comm.code.functor.CLASS_OBJECT_EVENT;
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
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.tools.file.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;


@Extension
public class NFClassModule extends NFIClassModule {

	private NFIPluginManager pPluginManager;

	protected NFIElementModule m_pElementModule;

	protected String msConfigFileName;

	private XMLUtil xmlUtil;
	private String __FILE__ = "NFClassModule";

	private static NFClassModule SingletonPtr = null;

	public static NFClassModule GetSingletonPtr() {
		if (null == SingletonPtr) {
			SingletonPtr = new NFClassModule();
			return SingletonPtr;
		} else {
			return SingletonPtr;
		}
	}

	public NFClassModule() {
		pPluginManager = NFPluginManager.GetSingletonPtr();
		msConfigFileName = "DataCfg" + File.separator + "Struct"
				+ File.separator + "LogicClass.xml";
		Load();
	}

	@Override
	public boolean Awake() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Init() {
		m_pElementModule = pPluginManager.FindModule(NFIElementModule.class);
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

	public boolean Load(Node attrNode, NFIClass pParentClass) {
		String pstrLogicClassName = xmlUtil.getAttrVal(attrNode, "Id").trim();
		String pstrType = xmlUtil.getAttrVal(attrNode, "Type").trim();
		String pstrPath = xmlUtil.getAttrVal(attrNode, "Path").trim();
		String pstrInstancePath = xmlUtil.getAttrVal(attrNode, "InstancePath")
				.trim();

		// printf( "-----------------------------------------------------\n");
		// printf( "%s:\n", pstrLogicClassName );

		NFIClass pClass = new NFClass(pstrLogicClassName);
		AddElement(pstrLogicClassName, pClass);
		pClass.SetParent(pParentClass);
		pClass.SetTypeName(pstrType);
		pClass.SetInstancePath(pstrInstancePath);

		AddClass(pstrPath, pClass);

		NodeList attrNode_Childs = attrNode.getChildNodes();

		for (int i = 0; i < attrNode_Childs.getLength(); i++) {

			Node pDataNode = attrNode_Childs.item(i);
			if (pDataNode instanceof Element) {
				Load(pDataNode, pClass);
			}
		}

		return true;
	}

	@Override
	public boolean Load() {
		// ////////////////////////////////////////////////////////////////////////
		String strFile = pPluginManager.GetConfigPath() + msConfigFileName;
		xmlUtil = new XMLUtil();
		Document xDoc = null;
		try {
			xDoc = xmlUtil.parseFile(new File(strFile));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Node root = xDoc.getFirstChild();

		NodeList attrNodes = root.getChildNodes();

		for (int i = 0; i < attrNodes.getLength(); i++) {
			Node attrNode = attrNodes.item(i);
			if (attrNode instanceof Element) {
				Load(attrNode, null);
			}
		}

		// String strContent;
		// pPluginManager.GetFileContent(strFile, strContent);
		//
		// rapidxml.xml_document<> xDoc;
		// xDoc.parse<0>((char*)strContent.c_str());
		// //////////////////////////////////////////////////////////////////////////
		// //support for unlimited layer class inherits
		// rapidxml.xml_node<>* root = xDoc.first_node();
		// for (rapidxml.xml_node<>* attrNode = root.first_node(); attrNode;
		// attrNode = attrNode.next_sibling())
		// {
		// Load(attrNode, NULL);
		// }
		return true;
	}

	public boolean AddClass(String pstrClassFilePath, NFIClass pClass) {
		NFIClass pParent = pClass.GetParent();
		while (pParent != null) {
			// inherited some properties form class of parent

			for (int i = 0; i < pParent.size(); i++) {
				String strFileName = pParent.get(i);
				if (AddClassInclude(strFileName, pClass)) {
					pClass.add(strFileName);
				}
				pClass.remove(strFileName);
			}
			pParent = pParent.GetParent();
		}

		// ////////////////////////////////////////////////////////////////////////
		if (AddClassInclude(pstrClassFilePath, pClass)) {
			pClass.add(pstrClassFilePath);
		}

		// file.close();

		return true;
	}

	public boolean AddClassInclude(String pstrClassFilePath, NFIClass pClass) {
		if (pClass.contains(pstrClassFilePath)) {
			return false;
		}
		// ////////////////////////////////////////////////////////////////////////
		String strFile = pPluginManager.GetConfigPath() + pstrClassFilePath;
		Document xDoc;
		try {
			xDoc = xmlUtil.parseFile(new File(strFile));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		Node root = xDoc.getFirstChild();

		Node pRropertyRootNode = null;
		try {
			pRropertyRootNode = xmlUtil.getNode(root, "./Propertys");
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (pRropertyRootNode != null) {
			AddPropertys(pRropertyRootNode, pClass);
		}

		// ////////////////////////////////////////////////////////////////////////
		// and record
		Node pRecordRootNode = null;
		try {
			pRecordRootNode = xmlUtil.getNode(root, "./Records");
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pRecordRootNode != null) {
			AddRecords(pRecordRootNode, pClass);
		}

		Node pComponentRootNode = null;
		try {
			pComponentRootNode = xmlUtil.getNode(root, "./Components");
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pComponentRootNode != null) {
			AddComponents(pComponentRootNode, pClass);
		}

		// pClass.mvIncludeFile.push_back( pstrClassFilePath );
		// and include file
		Node pIncludeRootNode = null;
		try {
			pIncludeRootNode = xmlUtil.getNode(root, "./Includes");
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pIncludeRootNode != null) {
			Element e_pIncludeRootNode = (Element) pIncludeRootNode;
			for (int i = 0; i < e_pIncludeRootNode.getChildNodes().getLength(); i++) {

				Node includeNode = e_pIncludeRootNode.getChildNodes().item(i);
				if (includeNode instanceof Element) {

					String pstrIncludeFile = xmlUtil.getAttrVal(includeNode,
							"Id");

					if (AddClassInclude(pstrIncludeFile, pClass)) {
						pClass.add(pstrIncludeFile);
					}
				}

			}
		}

		return true;
	}

	public boolean AddPropertys(Node pPropertyRootNode, NFIClass pClass) {
		String __FUNCTION__ = "AddPropertys";

		for (int i = 0; i < pPropertyRootNode.getChildNodes().getLength(); i++) {
			Node pPropertyNode = pPropertyRootNode.getChildNodes().item(i);

			if (pPropertyNode instanceof Element) {

				String strPropertyName = xmlUtil
						.getAttrVal(pPropertyNode, "Id");

				if (pClass.GetPropertyManager().GetElement(strPropertyName) != null) {
					// error
					NFPlatform.NFASSERT(0, strPropertyName, __FILE__,
							__FUNCTION__);
					continue;
				}

				String pstrType = xmlUtil.getAttrVal(pPropertyNode, "Type");
				String pstrPublic = xmlUtil.getAttrVal(pPropertyNode, "Public");
				String pstrPrivate = xmlUtil.getAttrVal(pPropertyNode,
						"Private");
				String pstrSave = xmlUtil.getAttrVal(pPropertyNode, "Save");
				String pstrCache = xmlUtil.getAttrVal(pPropertyNode, "Cache");
				String pstrRef = xmlUtil.getAttrVal(pPropertyNode, "Ref");
				String pstrUpload = xmlUtil.getAttrVal(pPropertyNode, "Upload");

				boolean bPublic = pstrPublic.equals("1") ? true : false;
				boolean bPrivate = pstrPrivate.equals("1") ? true : false;
				boolean bSave = pstrSave.equals("1") ? true : false;
				boolean bCache = pstrCache.equals("1") ? true : false;
				boolean bRef = pstrRef.equals("1") ? true : false;
				boolean bUpload = pstrUpload.equals("1") ? true : false;

				NFData varProperty = new NFData();
				if (NFDATA_TYPE.TDATA_UNKNOWN == ComputerType(pstrType,
						varProperty)) {
					NFPlatform.NFASSERT(0, strPropertyName, __FILE__,
							__FUNCTION__);
				}

				NFIProperty xProperty = pClass.GetPropertyManager()
						.AddProperty(new NFGUID(), strPropertyName,
								varProperty.GetType());
				xProperty.SetPublic(bPublic);
				xProperty.SetPrivate(bPrivate);
				xProperty.SetSave(bSave);
				xProperty.SetCache(bCache);
				xProperty.SetRef(bRef);
				xProperty.SetUpload(bUpload);
			}

		}
		return true;
	}

	public boolean AddRecords(Node pRecordRootNode, NFIClass pClass) {
		String __FUNCTION__ = "AddRecords";
		for (int i = 0; i < pRecordRootNode.getChildNodes().getLength(); i++) {
			Node pRecordNode = pRecordRootNode.getChildNodes().item(i);
			String pstrRecordName = xmlUtil.getAttrVal(pRecordNode, "Id");
			if (pClass.GetRecordManager().GetElement(pstrRecordName) != null) {
				// error
				// file << pClass.mstrType << ":" << pstrRecordName << std.endl;
				// assert(0);
				NFPlatform
						.NFASSERT(0, pstrRecordName, __FILE__, __FUNCTION__);
				continue;
			}

			String pstrRow = xmlUtil.getAttrVal(pRecordNode, "Row");
			String pstrCol = xmlUtil.getAttrVal(pRecordNode, "Col");

			String pstrPublic = xmlUtil.getAttrVal(pRecordNode, "Public");
			String pstrPrivate = xmlUtil.getAttrVal(pRecordNode, "Private");
			String pstrSave = xmlUtil.getAttrVal(pRecordNode, "Save");
			String pstrCache = xmlUtil.getAttrVal(pRecordNode, "Cache");
			String pstrUpload = xmlUtil.getAttrVal(pRecordNode, "Upload");

			String strView = xmlUtil.getAttrVal(pRecordNode, "View");

			boolean bPublic = pstrPublic.equals("1") ? true : false;
			boolean bPrivate = pstrPrivate.equals("1") ? true : false;
			boolean bSave = pstrSave.equals("1") ? true : false;
			boolean bCache = pstrCache.equals("1") ? true : false;
			boolean bUpload = pstrUpload.equals("1") ? true : false;

			NFDataList recordVar = new NFDataList();
			NFDataList recordTag = new NFDataList();

			for (int j = 0; j < pRecordNode.getChildNodes().getLength(); j++) {
				Node recordColNode = pRecordNode.getChildNodes().item(j);
				NFData TData = new NFData();
				String pstrColType = xmlUtil.getAttrVal(recordColNode, "Type");
				if (NFDATA_TYPE.TDATA_UNKNOWN == ComputerType(pstrColType,
						TData)) {
					// assert(0);
					NFPlatform.NFASSERT(0, pstrRecordName, __FILE__,
							__FUNCTION__);
				}

				recordVar.Append(TData);
				// ////////////////////////////////////////////////////////////////////////
				if (xmlUtil.getAttrVal(recordColNode, "Tag") != null) {
					String pstrTag = xmlUtil.getAttrVal(recordColNode, "Tag");
					recordTag.Add(pstrTag);
				} else {
					recordTag.Add("");
				}
			}

			NFIRecord xRecord = pClass.GetRecordManager().AddRecord(
					new NFGUID(), pstrRecordName, recordVar, recordTag,
					Integer.valueOf(pstrRow));

			xRecord.SetPublic(bPublic);
			xRecord.SetPrivate(bPrivate);
			xRecord.SetSave(bSave);
			xRecord.SetCache(bCache);
			xRecord.SetUpload(bUpload);

		}

		return true;
	}

	public boolean AddComponents(Node pComponentRootNode, NFIClass pClass) {

		String __FUNCTION__ = "AddComponents";

		for (int i = 0; i < pComponentRootNode.getChildNodes().getLength(); i++) {
			Node pComponentNode = pComponentRootNode.getChildNodes().item(i);

			if (pComponentNode != null) {
				String strComponentName = xmlUtil.getAttrVal(pComponentNode,
						"Name");
				String strLanguage = xmlUtil.getAttrVal(pComponentNode,
						"Language");
				String strEnable = xmlUtil.getAttrVal(pComponentNode, "Enable");

				boolean bEnable = strEnable.equals("1") ? true : false;
				if (bEnable) {
					if (pClass.GetComponentManager().GetElement(
							strComponentName) != null) {
						// error
						NFPlatform.NFASSERT(0, strComponentName, __FILE__,
								__FUNCTION__);
						continue;
					}
					NFIComponent xComponent = new NFIComponent(new NFGUID(),
							strComponentName);
					pClass.GetComponentManager().AddComponent(strComponentName,
							xComponent);
				}
			}

		}

		return true;
	}

	public NFDATA_TYPE ComputerType(String pstrTypeName, NFData var) {

		if (pstrTypeName.equals("int")) {
			var.SetInt(0);
			return var.GetType();
		}

		if (pstrTypeName.equals("string")) {
			var.SetString("");
			return var.GetType();
		}
		if (pstrTypeName.equals("float")) {
			var.SetFloat(0.0);
			return var.GetType();
		}
		if (pstrTypeName.equals("object")) {
			var.SetObject(new NFGUID());
			return var.GetType();
		}
		if (pstrTypeName.equals("vector2")) {
			var.SetVector2(new NFVector2());
			return var.GetType();
		}
		if (pstrTypeName.equals("vector3")) {
			var.SetVector3(new NFVector3());
			return var.GetType();
		}

		return NFDATA_TYPE.TDATA_UNKNOWN;
	}

	@Override
	public boolean Save() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Clear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean DoEvent(NFGUID objectID, String strClassName,
						   CLASS_OBJECT_EVENT eClassEvent, NFDataList valueList) {
		NFIClass pClass = GetElement(strClassName);
		if (null == pClass) {
			return false;
		}

		return pClass.DoEvent(objectID, eClassEvent, valueList);
	}

	@Override
	public boolean AddClassCallBack(String strClassName, CLASS_EVENT_FUNCTOR cb) {
		NFIClass pClass = GetElement(strClassName);
		if (null == pClass) {
			return false;
		}

		return pClass.AddClassCallBack(cb);
	}

	@Override
	public NFIPropertyManager GetClassPropertyManager(String strClassName) {
		NFIClass pClass = GetElement(strClassName);
		if (pClass != null) {
			return pClass.GetPropertyManager();
		}

		return null;
	}

	@Override
	public NFIRecordManager GetClassRecordManager(String strClassName) {
		NFIClass pClass = GetElement(strClassName);
		if (pClass != null) {
			return pClass.GetRecordManager();
		}

		return null;
	}

	@Override
	public NFIComponentManager GetClassComponentManager(String strClassName) {
		NFIClass pClass = GetElement(strClassName);
		if (pClass != null) {
			return pClass.GetComponentManager();
		}

		return null;
	}

}
