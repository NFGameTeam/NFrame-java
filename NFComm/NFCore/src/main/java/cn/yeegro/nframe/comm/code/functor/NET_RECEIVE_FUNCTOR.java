/**   
* @Title: NET_RECEIVE_FUNCTOR 
* @Package ${package_name} 
* @Description: 网络接收事件接口 
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.functor;

public interface NET_RECEIVE_FUNCTOR<T> {
	
	void operator(long nSockIndex, int nMsgType, int keyid, short cmd, byte[] msg, int nLen);
	void operator(long nSockIndex, int nMsgType, String msg);
	
}
