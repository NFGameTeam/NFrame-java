/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类模块
 */

public class NFLogicClassModule extends NFILogicClassModule{
	
	private NFIElementInfoModule elementInfoModule;
	private String configFileName;
	
	@Override
	public void init(){
		configFileName = "./DataCfg/Struct/LogicClass.xml";
		
		//elementInfoModule 从pluginmng获取
		load();
	}
	
	@Override
	public void afterInit(){
	}
	
	@Override
	public void beforeShut(){
		clear();
	}
	
	@Override
	public void shut(){
	}
	
	@Override
	public void execute(){
	}
	
	@Override
	public boolean load(){
/*		rapidxml::file<> fdoc(configFileName.c_str());
		//std::cout << fdoc.data() << std::endl;
		rapidxml::xml_document<>  doc;
		doc.parse<0>(fdoc.data());
		
		//support for unlimited layer class inherits
		rapidxml::xml_node<>* root = doc.first_node();
		for (rapidxml::xml_node<>* attrNode = root->first_node(); attrNode; attrNode = attrNode->next_sibling())
		{
			Load(attrNode, NF_SHARED_PTR<NFILogicClass>());
		}
		
		m_pElementInfoModule->Load();*/

		return false;
	}
	
	@Override
	public boolean clear(){
		return false;
	}
	
	@Override
	public boolean reload(String strClassName){
		return false;
	}
	
	@Override
	public NFIPropertyManager getPropertyManager(String className){
/*		NF_SHARED_PTR<NFILogicClass> pClass = GetElement(strClassName);
		if (pClass.get())
		{
			return pClass->GetPropertyManager();
		}*/

		return null;
	}

	
	
	private NFIData.Type computerType(String typeName, NFIData var){
		NFIData.Type type = NFIData.Type.UNKNOW;
		if ("int".equals(typeName)){
			var.set(NFIData.INT_NIL);
			type = NFIData.Type.INT;
		}else if ("float".equals(typeName)){
			var.set(NFIData.FLOAT_NIL);
			type = NFIData.Type.FLOAT;
		}else if ("string".equals(typeName)){
			var.set(NFIData.STRING_NIL);
			type = NFIData.Type.STRING;
		}else if ("object".equals(typeName)){
			var.set(NFIData.OBJECT_NIL);
			type = NFIData.Type.OBJECT;
		}
		return type;
	}

/*//    private boolean addPropertys(rapidxml::xml_node<>* pPropertyRootNode, NFILogicClass pClass)
//    {
//    	return false;
//    }
//    
//    private boolean addRecords(rapidxml::xml_node<>* pRecordRootNode, NFILogicClass pClass)
//    {
//    	return false;
//    }
*/    
	private boolean addClassInclude(String classFilePath, NFILogicClass parentClass){
		return false;
	}
	
	private boolean addClass(String classFilePath, NFILogicClass parentClass){
		return false;
	}
}
