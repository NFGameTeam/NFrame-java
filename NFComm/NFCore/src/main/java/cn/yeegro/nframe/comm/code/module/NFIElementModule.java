/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.module;


import java.util.List;


public interface NFIElementModule extends NFIModule {

	
	public  boolean Load() ;
    public  boolean Save() ;
    public  boolean Clear() ;

    //special
    public  boolean LoadSceneInfo(String strFileName, String strClassName) ;

    public  boolean ExistElement(String strConfigName) ;
    public  boolean ExistElement(String strClassName, String strConfigName) ;

    public  NFIPropertyManager GetPropertyManager(String strConfigName) ;
    public  NFIRecordManager GetRecordManager(String strConfigName) ;
    public  NFIComponentManager GetComponentManager(String strConfigName) ;

    public  int GetPropertyInt(String strConfigName, String strPropertyName) ;
    public  double GetPropertyFloat(String strConfigName, String strPropertyName) ;
    public String GetPropertyString(String strConfigName, String strPropertyName) ;
	
	public List<String> GetListByProperty(String strClassName, String strPropertyName, int nValue) ;
	public List<String> GetListByProperty(String strClassName, String strPropertyName, String nValue) ;
	
}
