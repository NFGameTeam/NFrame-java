/**   
* @Title: NFEventDefine
* @Package ${package_name} 
* @Description: 事件定义类型
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface;

public enum NFEventDefine {

			NFED_UNKNOW,
			
			NFED_ON_GENERAL_MESSAGE,


			NFED_ON_CLIENT_REQUIRE_MOVE,
			NFED_ON_CLIENT_MOVE_RESULT,


			NFED_ON_CLIENT_REQUIRE_USE_SKILL,
			NFED_ON_CLIENT_USE_SKILL_RESULT,

			NFED_ON_CLIENT_REQUIRE_USE_SKILL_POS,
			NFED_ON_CLIENT_USE_SKILL_POS_RESULT,

			NFED_ON_CLIENT_REQUIRE_USE_ITEM,

			NFED_ON_CLIENT_REQUIRE_USE_ITEM_POS,

			NFED_ON_OBJECT_BE_KILLED,


			NFED_ON_NOTICE_ECTYPE_AWARD,
}
