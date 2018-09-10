/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;


import com.noahframe.nfcore.iface.functor.RECORD_EVENT_DATA;
import com.noahframe.nfcore.iface.math.NFVector3;
import com.noahframe.nfcore.iface.util.NFData;
import com.noahframe.nfcore.iface.util.NFDataList;
import com.noahframe.nfcore.iface.util.NFMapEx;

import java.util.List;
import java.util.Vector;



public abstract class NFISceneAOIModule extends NFMapEx<Integer, NFSceneInfo> implements NFIModule  {

	public interface OBJECT_ENTER_EVENT_FUNCTOR<T>{
		
		int operator(NFDataList dataList1, NFDataList dataList2);

	}
	
	public interface OBJECT_LEAVE_EVENT_FUNCTOR<T>{

		int operator(NFDataList dataList1, NFDataList dataList2);
		
	}
	
	public interface PROPERTY_ENTER_EVENT_FUNCTOR<T>{
		
		int operator(NFDataList dataList1, NFGUID guid);
	}
	
	public interface RECORD_ENTER_EVENT_FUNCTOR<T>{
		int operator(NFDataList dataList1, NFGUID guid);
		
	}
	
	public interface PROPERTY_SINGLE_EVENT_FUNCTOR<T>{
		int operator(NFGUID guid, String str, NFData data1, NFData data2, NFDataList dataList);
		
	}
	
	public interface RECORD_SINGLE_EVENT_FUNCTOR<T>{
		int operator(NFGUID guid, String str, RECORD_EVENT_DATA red, NFData data1, NFData data2, NFDataList dataList);
		
	}
	
	public interface SCENE_EVENT_FUNCTOR<T>{
		int operator(NFGUID guid, int n1, int n2, int n3, NFDataList dataList);
		
	}

	
	
	
	public <BaseType>  boolean AddObjectEnterCallback(BaseType pBase, OBJECT_ENTER_EVENT_FUNCTOR functor){
		return AddObjectEnterCallback(functor);
	}
	
	public <BaseType> boolean AddObjectLeaveCallBack(BaseType pBase, OBJECT_LEAVE_EVENT_FUNCTOR functor)
	{
		return AddObjectLeaveCallBack(functor);
	}
	
	public <BaseType> boolean AddPropertyEnterCallBack(BaseType pBase, PROPERTY_ENTER_EVENT_FUNCTOR functor)
	{
		return AddPropertyEnterCallBack(functor);
	}
	
	public <BaseType> boolean AddRecordEnterCallBack(BaseType pBase, RECORD_ENTER_EVENT_FUNCTOR functor)
	{
		return AddRecordEnterCallBack(functor);
	}
	
	public <BaseType> boolean AddPropertyEventCallBack(BaseType pBase, PROPERTY_SINGLE_EVENT_FUNCTOR functor)
	{;
		return AddPropertyEventCallBack(functor);
	}
	
	public <BaseType> boolean AddRecordEventCallBack(BaseType pBase, RECORD_SINGLE_EVENT_FUNCTOR functor)
	{
		return AddRecordEventCallBack(functor);
	}
	
	public <BaseType> boolean AddEnterSceneConditionCallBack(BaseType pBase, SCENE_EVENT_FUNCTOR functor)
	{
		return AddEnterSceneConditionCallBack(functor);
	}
	
	public <BaseType> boolean AddBeforeEnterSceneGroupCallBack(BaseType pBase, SCENE_EVENT_FUNCTOR functor)
	{
		return AddBeforeEnterSceneGroupCallBack(functor);
	}
	
	public <BaseType> boolean AddAfterEnterSceneGroupCallBack(BaseType pBase, SCENE_EVENT_FUNCTOR functor)
	{
		return AddAfterEnterSceneGroupCallBack(functor);
	}
	
	public <BaseType> boolean AddSwapSceneEventCallBack(BaseType pBase, SCENE_EVENT_FUNCTOR functor)
	{
		return AddSwapSceneEventCallBack(functor);
	}
	
	public abstract boolean RemoveSwapSceneEventCallBack();
	
	public <BaseType> boolean AddBeforeLeaveSceneGroupCallBack(BaseType pBase, SCENE_EVENT_FUNCTOR functor)
	{
		return AddBeforeLeaveSceneGroupCallBack(functor);
	}
	
	public <BaseType> boolean AddAfterLeaveSceneGroupCallBack(BaseType pBase, SCENE_EVENT_FUNCTOR functor)
	{
		return AddAfterLeaveSceneGroupCallBack(functor);
	}
	
	public abstract boolean RequestEnterScene(NFGUID self, int nSceneID, int nGroupID, int nType, NFDataList argList);
	public abstract boolean AddSeedData(int nSceneID, String strSeedID, String strConfigID, NFVector3 vPos, int nHeight);
	public abstract boolean AddRelivePosition(int nSceneID, int nIndex, NFVector3 vPos);
	public abstract NFVector3 GetRelivePosition(int nSceneID, int nIndex);

	public abstract boolean CreateSceneNPC(int nSceneID, int nGroupID);
	public abstract boolean DestroySceneNPC(int nSceneID, int nGroupID);
	
	
	
	public abstract boolean AddObjectEnterCallback(OBJECT_ENTER_EVENT_FUNCTOR handler);
	
	public abstract boolean AddObjectLeaveCallBack( OBJECT_LEAVE_EVENT_FUNCTOR cb);
	public abstract boolean AddPropertyEnterCallBack( PROPERTY_ENTER_EVENT_FUNCTOR cb);
	public abstract boolean AddRecordEnterCallBack( RECORD_ENTER_EVENT_FUNCTOR cb);
	public abstract boolean AddPropertyEventCallBack( PROPERTY_SINGLE_EVENT_FUNCTOR cb);
	public abstract boolean AddRecordEventCallBack( RECORD_SINGLE_EVENT_FUNCTOR cb);

	public abstract boolean AddEnterSceneConditionCallBack( SCENE_EVENT_FUNCTOR cb);

	public abstract boolean AddBeforeEnterSceneGroupCallBack( SCENE_EVENT_FUNCTOR cb);
	public abstract boolean AddAfterEnterSceneGroupCallBack( SCENE_EVENT_FUNCTOR cb);
	public abstract boolean AddSwapSceneEventCallBack( SCENE_EVENT_FUNCTOR cb);
	public abstract boolean AddBeforeLeaveSceneGroupCallBack( SCENE_EVENT_FUNCTOR cb);
	public abstract boolean AddAfterLeaveSceneGroupCallBack( SCENE_EVENT_FUNCTOR cb);

	
	@SuppressWarnings("rawtypes")
	protected List<OBJECT_ENTER_EVENT_FUNCTOR> mtObjectEnterCallback;
}
