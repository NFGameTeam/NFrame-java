/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;


import com.noahframe.nfcore.iface.functor.MODULE_SCHEDULE_FUNCTOR;
import com.noahframe.nfcore.iface.functor.OBJECT_SCHEDULE_FUNCTOR;
import com.noahframe.nfcore.iface.util.NFDateTime;

public abstract class NFIScheduleModule implements NFIModule {

		///for module
		public abstract boolean AddSchedule(String strScheduleName, MODULE_SCHEDULE_FUNCTOR cb, float fTime, int nCount) ;
		public abstract boolean AddSchedule(String strScheduleName, MODULE_SCHEDULE_FUNCTOR cb, int nCount, NFDateTime date) ;
		public abstract boolean RemoveSchedule(String strScheduleName) ;
		public abstract boolean ExistSchedule(String strScheduleName) ;

		public<BaseType> boolean AddSchedule(String strScheduleName, BaseType pBase, MODULE_SCHEDULE_FUNCTOR functor,  float fIntervalTime,  int nCount)
		{
			return AddSchedule(strScheduleName, functor, fIntervalTime, nCount);
		}
		
		///for object
		public abstract boolean AddSchedule(NFGUID self, String strScheduleName, OBJECT_SCHEDULE_FUNCTOR cb, float fTime, int nCount) ;
		public abstract boolean AddSchedule(NFGUID self, String strScheduleName, OBJECT_SCHEDULE_FUNCTOR cb, int nCount, NFDateTime date) ;
		public abstract boolean RemoveSchedule( NFGUID self) ;
		public abstract boolean RemoveSchedule( NFGUID self, String strScheduleName) ;
		public abstract boolean ExistSchedule( NFGUID self, String strScheduleName) ;

		public<BaseType> boolean AddSchedule( NFGUID self, String strScheduleName, MODULE_SCHEDULE_FUNCTOR functor,  float fIntervalTime,  int nCount)
		{

			return AddSchedule(self, strScheduleName, functor, fIntervalTime, nCount);
		}
}
