/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;


import com.noahframe.nfcore.iface.util.NFMapEx;

public class NFSceneGroupInfo {

	public NFSceneGroupInfo(int nSceneID, int nGroupID) {
		mnGroupID = nGroupID;
	}

	public NFSceneGroupInfo(int nSceneID, int nGroupID, int nWidth) {
		mnGroupID = nGroupID;
	}

	boolean Execute() {
		return true;
	}

	public NFMapEx<NFGUID, Integer> mxPlayerList;
	public NFMapEx<NFGUID, Integer> mxOtherList;
	int mnGroupID;
}
