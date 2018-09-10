/**   
* @Title: NET_EVENT_FUNCTOR 
* @Package ${package_name} 
* @Description: 网络事件接口 
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.functor;


import com.noahframe.nfcore.iface.module.NFINet;

public interface NET_EVENT_FUNCTOR <T> {
	void operator(int nSockIndex, NFINet.NF_NET_EVENT nEvent, NFINet pNet);
}
