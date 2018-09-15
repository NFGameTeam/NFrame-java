package com.noahframe.plugins.net;

import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.api.plugin.Extension;
import com.noahframe.nfcore.iface.functor.NET_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.functor.NET_RECEIVE_FUNCTOR;
import com.noahframe.nfcore.iface.module.NFINet;
import com.noahframe.nfcore.iface.module.NFISocketModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Extension
public class NFNetModule extends NFISocketModule {

	public int mnBufferSize;
	public NFINet m_pNet;
	public long nLastTime;
	public Map<Integer, NET_RECEIVE_FUNCTOR> mxReceiveCallBack;
	public List<NET_EVENT_FUNCTOR> mxEventCallBackList;
	public List<NET_RECEIVE_FUNCTOR> mxCallBackList;
	public NFPluginManager pPluginManager = null;
	
	
	private static NFNetModule SingletonPtr=null;
	
	public static NFNetModule GetSingletonPtr()
	{
		if (null==SingletonPtr) {
			 SingletonPtr=new NFNetModule();
			 return SingletonPtr;
		}
		else {
			return SingletonPtr;
		}
	}
	
	
	public NFNetModule()
	{
		pPluginManager = NFPluginManager.GetSingletonPtr();

		mnBufferSize = 0;
		nLastTime = GetPluginManager().GetNowTime();
		m_pNet = null;
		mxReceiveCallBack=new HashMap<Integer, NET_RECEIVE_FUNCTOR>();
		mxEventCallBackList=new ArrayList<NET_EVENT_FUNCTOR>();
		mxCallBackList=new ArrayList<NET_RECEIVE_FUNCTOR>();
	}
	
	
	private NFPluginManager GetPluginManager() {
		// TODO Auto-generated method stub
		return pPluginManager;
	}


	public boolean Awake() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean Init() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean AfterInit() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean CheckConfig() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean ReadyExecute() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean BeforeShut() {

		return false;
	}

	public boolean Shut() {
		boolean isClose=false;
		if (m_pNet!=null) {
			isClose=m_pNet.CloseChannel();
		}
		return isClose;
	}

	public boolean Finalize() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean OnReloadPlugin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void Initialization(String strIP, short nPort) {
		m_pNet = new NFNet(this, OnReceiveNetPack, OnSocketNetEvent);
		m_pNet.ExpandBufferSize(mnBufferSize);
		m_pNet.Initialization(strIP, nPort);
		
	}

	@Override
	public int Initialization(int nMaxClient,String strIP, int nPort, int nCpuCount) {
		m_pNet =new NFNet(this,OnReceiveNetPack,OnSocketNetEvent);
		m_pNet.ExpandBufferSize(mnBufferSize);
		return m_pNet.Initialization(nMaxClient,strIP, nPort, nCpuCount);
	}

	@Override
	public int ExpandBufferSize(int size) {
		if (size > 0)
		{
			mnBufferSize = size;
			if (m_pNet != null)
			{
				m_pNet.ExpandBufferSize(mnBufferSize);
			}
		}

		return mnBufferSize;
	}

	@Override
	public void RemoveReceiveCallBack(int nMsgID) {
		
		NET_RECEIVE_FUNCTOR it=mxReceiveCallBack.get(nMsgID);
		if (it!=null) {
			mxReceiveCallBack.remove(nMsgID);
		}
	}

	@Override
	public boolean AddReceiveCallBack(int nMsgID, NET_RECEIVE_FUNCTOR cb) {
		if (mxReceiveCallBack.get(nMsgID) != null)
		{
			return false;
		}

		mxReceiveCallBack.put(nMsgID, cb);

		return true;
	}

	@Override
	public boolean AddReceiveCallBack(NET_RECEIVE_FUNCTOR cb) {
		mxCallBackList.add(cb);

		return true;
	}

	@Override
	public boolean AddEventCallBack(NET_EVENT_FUNCTOR cb) {
		mxEventCallBackList.add(cb);

		return true;
	}

	@Override
	public boolean Execute() {
		if (m_pNet==null)
		{
			return false;
		}


		KeepAlive();

		return m_pNet.Execute();
	}

//	@Override
//	public boolean SendMsgWithOutHead(int nMsgID, byte[] msg, int nSockIndex) {
//		
//		return m_pNet.SendMsgWithOutHead(nMsgID, msg, msg.length, nSockIndex);
//	}
//
//	@Override
//	public boolean SendMsgToAllClientWithOutHead(int nMsgID, byte[] msg) {
//		return m_pNet.SendMsgToAllClientWithOutHead(nMsgID, msg, msg.length);
//	}
//
//	@Override
//	public boolean SendMsgPB(int nMsgID, Message xData, int nSockIndex) {
//		MsgBase xMsg=MsgBase.getDefaultInstance();
////		if (!xData.SerializeToString(xMsg.mutable_msg_data()))
////		{
////			char szData[MAX_PATH] = { 0 };
////			NFSPRINTF(szData, MAX_PATH, "Send Message to %d Failed For Serialize of MsgData, MessageID: %d\n", nSockIndex, nMsgID);
////
////			return false;
////		}
////
////		Ident pPlayerID = xMsg.mutable_player_id();
////		pPlayerID = NFToPB(new NFGUID());
//
//		byte[] strMsg=null;
////		if (!xMsg.SerializeToString(strMsg))
////		{
////			char szData[MAX_PATH] = { 0 };
////			NFSPRINTF(szData, MAX_PATH, "Send Message to %d Failed For Serialize of MsgBase, MessageID: %d\n", nSockIndex, nMsgID);
////
////			return false;
////		}
//
//		SendMsgWithOutHead(nMsgID, strMsg, nSockIndex);
//
//		return true;
//	}
//
//	@Override
//	public boolean SendMsgPBToAllClient(int nMsgID, Message xData) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean SendMsgPB(int nMsgID, Message xData, int nSockIndex,
//			NFGUID nPlayer, List<NFGUID> pClientIDList) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean SendMsgPB(int nMsgID, byte[] strData, int nSockIndex,
//			NFGUID nPlayer, List<NFGUID> pClientIDList) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public NFINet GetNet() {
		// TODO Auto-generated method stub
		return m_pNet;
	}
	
	public void KeepAlive()
	{
		if (m_pNet==null)
		{
			return;
		}

		if (m_pNet.IsServer())
		{
			return;
		}

		if (nLastTime + 10 > GetPluginManager().GetNowTime())
		{
			return;
		}

		nLastTime = GetPluginManager().GetNowTime();


		//SendMsgPB(EGameMsgID.EGMI_STS_HEART_BEAT.getNumber(), xMsg.build(), 0);

	}
	
	public NET_RECEIVE_FUNCTOR OnReceiveNetPack=new NET_RECEIVE_FUNCTOR() {

		public void operator(long nSockIndex, int nMsgType,int keyid, short cmd,byte[] msg, int nLen) {
			//下面为正式代码根据协议进行处理
			if (mxReceiveCallBack.containsKey(nMsgType)) {
				NET_RECEIVE_FUNCTOR pFunc = mxReceiveCallBack.get(nMsgType);
				pFunc.operator(nSockIndex, nMsgType,keyid,cmd, msg, nLen);
			}
			else {
				for (int i = 0; i < mxCallBackList.size(); i++) {
					NET_RECEIVE_FUNCTOR pFunc = mxCallBackList.get(i);
					pFunc.operator(nSockIndex, nMsgType,keyid,cmd, msg, nLen);
				}
				
			}
		}

		@Override
		public void operator(long nSockIndex, int nMsgID, String msg) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public NET_EVENT_FUNCTOR OnSocketNetEvent=new NET_EVENT_FUNCTOR()
	{
		@Override
		public void operator(int nSockIndex, NFINet.NF_NET_EVENT eEvent, NFINet pNet)
		{
			for (int i = 0; i < mxEventCallBackList.size(); i++) {
				NET_EVENT_FUNCTOR pFunc = mxEventCallBackList.get(i);
				pFunc.operator(nSockIndex, eEvent, pNet);
				pFunc.operator(nSockIndex, eEvent, pNet);
			}
		}
	};

	@Override
	public <T> boolean SendMsgToSocker(long nSockIndex, T msg) {
	
		return m_pNet.SendMsgToSocker(nSockIndex, (byte[])msg);
		
	}


	@Override
	public <T> boolean SendMsgToAll(T msg) {
		// TODO Auto-generated method stub
		return m_pNet.SendMsgToAll(msg);
	}

//	@Override
//	public boolean SendMsg(int nMsgID, byte[] msg) {
//		m_pNet.SendMsgToAllClient(msg, msg.length);
//		return false;
//	}
	

}
