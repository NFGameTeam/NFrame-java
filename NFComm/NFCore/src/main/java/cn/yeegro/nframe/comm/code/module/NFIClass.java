/**   
* @Title: NFIClass
* @Package ${package_name} 
* @Description: 基础类接口
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.module;




import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.functor.CLASS_EVENT_FUNCTOR;
import cn.yeegro.nframe.comm.code.functor.CLASS_OBJECT_EVENT;
import cn.yeegro.nframe.comm.code.util.NFDataList;

import java.util.List;


public interface NFIClass extends List<String> {

    public NFIPropertyManager GetPropertyManager();

    public NFIRecordManager GetRecordManager();

    public NFIComponentManager GetComponentManager();

    public void SetParent(NFIClass pClass);
    public NFIClass GetParent();
    public void SetTypeName(String strType);
    public String GetTypeName();
    public String GetClassName();
    public  boolean AddId(String strConfigName);
    public List<String> GetIDList();
    public String GetInstancePath();
	public void SetInstancePath(String strPath);

    public boolean AddClassCallBack(CLASS_EVENT_FUNCTOR cb);
    public boolean DoEvent(NFGUID objectID, CLASS_OBJECT_EVENT eClassEvent, NFDataList valueList);
	
}
