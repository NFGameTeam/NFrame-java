/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类模块
 */

public class NFLogicClassModule extends NFILogicClassModule{
	
	private NFIElementInfoModule mxElementInfoModule;
	private String msConfigFileName;
	
	@Override
	public void init()
	{
		msConfigFileName = "./DataCfg/Struct/LogicClass.xml";
		
		//mxElementInfoModule 从pluginmng获取
		Load();
	}
	@Override
	public void afterInit()
	{
		
	}
	@Override
	public void beforeShut()
	{
		Clear();
	}
	@Override
	public void shut()
	{
		
	}
	@Override
	public void execute()
	{
		
	}
	
	@Override
	public boolean Load()
	{
/*		rapidxml::file<> fdoc(msConfigFileName.c_str());
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
	public boolean Clear()
	{
		return false;
	}
	
	@Override
	public boolean ReLoad(String strClassName)
	{
		return false;
	}
	
	@Override
	public NFIPropertyManager GetClassPropertyManager(String strClassName)
	{
/*		NF_SHARED_PTR<NFILogicClass> pClass = GetElement(strClassName);
		if (pClass.get())
	    {
	        return pClass->GetPropertyManager();
	    }*/

	    
		return null;
	}

	
	
    private NFIData.Type ComputerType(String strTypeName, NFIData var)
    {
    	if ( strTypeName == "int")
        {
            var.set(NFIData.INT_NIL);
            return NFIData.Type.INT;
        }
        else if ( strTypeName == "float")
        {
        	var.set(NFIData.FLOAT_NIL);
            return NFIData.Type.FLOAT;
        }
        else if ( strTypeName == "string")
        {
        	var.set(NFIData.STRING_NIL);
            return NFIData.Type.STRING;
        }
        else if ( strTypeName ==  "object")
        {
        	var.set(NFIData.OBJECT_NIL);
            return NFIData.Type.OBJECT;
        }

        return NFIData.Type.UNKNOW;
    }
    
/*//    private boolean AddPropertys(rapidxml::xml_node<>* pPropertyRootNode, NFILogicClass pClass)
//    {
//    	return false;
//    }
//    
//    private boolean AddRecords(rapidxml::xml_node<>* pRecordRootNode, NFILogicClass pClass)
//    {
//    	return false;
//    }
*/    
    private boolean AddClassInclude(String strClassFilePath, NFILogicClass pClass)
    {
    	return false;
    }
    
    private boolean AddClass(String strClassFilePath, NFILogicClass pClass)
    {
    	return false;
    }
	
}
