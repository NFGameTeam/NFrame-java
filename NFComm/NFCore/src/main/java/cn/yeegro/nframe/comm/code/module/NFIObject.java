/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.module;


import cn.yeegro.nframe.comm.code.functor.CLASS_OBJECT_EVENT;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.functor.PROPERTY_EVENT_FUNCTOR;
import cn.yeegro.nframe.comm.code.functor.RECORD_EVENT_FUNCTOR;

public abstract class NFIObject {

	public NFIObject()
	{
		
	}
	public NFIObject(NFGUID self){
		
	}
	
	public abstract boolean Execute();
	public abstract NFGUID Self();
	
	public <BaseType> boolean AddPropertyCallBack(String strPropertyName, BaseType pBase, PROPERTY_EVENT_FUNCTOR functor)
    {
		  return AddPropertyCallBack(strPropertyName, functor);
    }
	public <BaseType> boolean AddRecordCallBack(String strRecordName, BaseType pBase, RECORD_EVENT_FUNCTOR functor)
	{
		return AddRecordCallBack(strRecordName, functor);
	}
	
	public <T> boolean AddComponent()
	{
		return GetComponentManager().AddComponent();
	}
	
	public <T> T FindComponent(String strComponentName)
	{
		return (T)GetComponentManager().FindComponent(strComponentName);
	}
	
	 /////////////////////////////////////////////////////////////////
		public abstract CLASS_OBJECT_EVENT GetState() ;
		public abstract boolean SetState( CLASS_OBJECT_EVENT eState) ;

	    public abstract boolean FindProperty( String strPropertyName) ;

	    public abstract boolean SetPropertyInt(String strPropertyName, int nValue) ;
	    public abstract boolean SetPropertyFloat(String strPropertyName, double dwValue) ;
	    public abstract boolean SetPropertyString(String strPropertyName, String strValue) ;
	    public abstract boolean SetPropertyObject(String strPropertyName, NFGUID obj) ;
		public abstract boolean SetPropertyVector2(String strPropertyName, NFVector2 value) ;
		public abstract boolean SetPropertyVector3(String strPropertyName, NFVector3 value) ;

	    public abstract int GetPropertyInt( String strPropertyName) ;
	    public abstract double GetPropertyFloat( String strPropertyName) ;
	    public abstract String GetPropertyString(String strPropertyName) ;
	    public abstract  NFGUID GetPropertyObject( String strPropertyName) ;
		public abstract  NFVector2 GetPropertyVector2( String strPropertyName) ;
		public abstract  NFVector3 GetPropertyVector3( String strPropertyName) ;

	    public abstract boolean FindRecord( String strRecordName) ;

	    public abstract boolean SetRecordInt(String strRecordName, int nRow, int nCol, int nValue) ;
	    public abstract boolean SetRecordFloat(String strRecordName, int nRow, int nCol, double dwValue) ;
	    public abstract boolean SetRecordString(String strRecordName, int nRow, int nCol, String strValue) ;
	    public abstract boolean SetRecordObject(String strRecordName, int nRow, int nCol, NFGUID obj) ;
		public abstract boolean SetRecordVector2(String strRecordName, int nRow, int nCol, NFVector2 value) ;
		public abstract boolean SetRecordVector3(String strRecordName, int nRow, int nCol, NFVector3 value) ;

	    public abstract boolean SetRecordInt(String strRecordName, int nRow, String strColTag, int value) ;
	    public abstract boolean SetRecordFloat(String strRecordName, int nRow, String strColTag, double value) ;
	    public abstract boolean SetRecordString(String strRecordName, int nRow, String strColTag, String value) ;
	    public abstract boolean SetRecordObject(String strRecordName, int nRow, String strColTag, NFGUID value) ;
		public abstract boolean SetRecordVector2(String strRecordName, int nRow, String strColTag, NFVector2 value) ;
		public abstract boolean SetRecordVector3(String strRecordName, int nRow, String strColTag, NFVector3 value) ;

	    public abstract int GetRecordInt(String strRecordName, int nRow, int nCol) ;
	    public abstract double GetRecordFloat(String strRecordName, int nRow, int nCol) ;
	    public abstract String GetRecordString(String strRecordName, int nRow, int nCol) ;
	    public abstract  NFGUID GetRecordObject(String strRecordName, int nRow, int nCol) ;
		public abstract  NFVector2 GetRecordVector2(String strRecordName, int nRow, int nCol) ;
		public abstract  NFVector3 GetRecordVector3(String strRecordName, int nRow, int nCol) ;

	    public abstract int GetRecordInt(String strRecordName, int nRow, String strColTag) ;
	    public abstract double GetRecordFloat(String strRecordName, int nRow, String strColTag) ;
	    public abstract String GetRecordString(String strRecordName, int nRow, String strColTag) ;
	    public abstract  NFGUID GetRecordObject(String strRecordName, int nRow, String strColTag) ;
		public abstract  NFVector2 GetRecordVector2(String strRecordName, int nRow, String strColTag) ;
		public abstract  NFVector3 GetRecordVector3(String strRecordName, int nRow, String strColTag) ;

	    //  public abstract NFIComponent> AddComponent( String strComponentName,  String strLanguageName) ;
	    //  public abstract NFIComponent> FindComponent( String strComponentName) ;

	    public abstract NFIRecordManager GetRecordManager() ;
	    public abstract NFIPropertyManager GetPropertyManager() ;
	    public abstract NFIComponentManager GetComponentManager() ;
	    
	    
	    protected abstract boolean AddRecordCallBack(String strRecordName, RECORD_EVENT_FUNCTOR cb);

	    protected abstract boolean AddPropertyCallBack(String strPropertyName, PROPERTY_EVENT_FUNCTOR cb);

}
