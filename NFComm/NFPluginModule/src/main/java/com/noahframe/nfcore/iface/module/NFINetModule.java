/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;


import com.noahframe.nfcore.iface.functor.NET_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.functor.NET_RECEIVE_FUNCTOR;

import java.util.List;
import java.util.Map;



public abstract class NFINetModule implements NFIModule {

	public enum NF_PROTOCOL_TYPES
	{
		NF_SOCKET_NONE((byte)0),    // NONE
	    NF_SOCKET_TCP((byte)1),    // TCP
	    NF_SOCKET_WEBSOCKET((byte)2);

		 private byte _value;

		private NF_PROTOCOL_TYPES(byte value) {
			_value = value;
		}

		public byte value() {
			return _value;
		}
		public static NF_PROTOCOL_TYPES get(byte ntype)
		{
			for (int i = 0; i < NF_PROTOCOL_TYPES.values().length; i++) {
				NF_PROTOCOL_TYPES val=NF_PROTOCOL_TYPES.values()[i];
				if (val.value()==ntype) {
					return val;
				}
			}
			return null;
		}
		
	};
	
	
	public enum NF_SERVER_TYPES
	{
	    NF_ST_NONE(0),    // NONE
	    NF_ST_REDIS(1),    //
	    NF_ST_MYSQL(2),    //
	    NF_ST_MASTER(3),    //
	    NF_ST_LOGIN(4),    //
	    NF_ST_PROXY(5),    //
	    NF_ST_GAME(6),    //
	    NF_ST_WORLD(7),    //
		NF_ST_MAX(8),
		NF_ST_TCP(9);    //

		 private int _value;

		private NF_SERVER_TYPES(int value) {
			_value = value;
		}

		public int value() {
			return _value;
		}
		public static NF_SERVER_TYPES get(int ntype)
		{
			for (int i = 0; i < NF_SERVER_TYPES.values().length; i++) {
				NF_SERVER_TYPES val=NF_SERVER_TYPES.values()[i];
				if (val.value()==ntype) {
					return val;
				}
			}
			return null;
		}
		
	};
//
//
//    public class ServerData
//    {
//    	public ServerData()
//        {
//            pData =NFMsg.NFMsgPreGame.ServerInfoReport.getDefaultInstance();
//            nFD = 0;
//        }
//
//    	public int nFD;
//    	public NFMsg.NFMsgPreGame.ServerInfoReport pData;
//    };
//
//
//	static int MAX_PATH =260;
//
//	public static NFGUID PBToNF(NFMsg.NFMsgBase.Ident xID)
//	{
//		NFGUID  xIdent=new NFGUID();
//		xIdent.nHead64 = xID.getSvrid();
//		xIdent.nData64 = xID.getIndex();
//
//		return xIdent;
//	}
//
//	public static NFVector2 PBToNF(NFMsg.NFMsgBase.Vector2 value)
//	{
//		NFVector2  vector=new NFVector2();
//		vector.SetX(value.getX());
//		vector.SetY(value.getY());
//		return vector;
//	}
//
//	public static NFVector3 PBToNF(NFMsg.NFMsgBase.Vector3 value)
//	{
//		NFVector3 vector=new NFVector3();
//		vector.SetX(value.getX());
//		vector.SetY(value.getY());
//		vector.SetZ(value.getZ());
//		return vector;
//	}
//
//	public static NFMsg.NFMsgBase.Ident NFToPB(NFGUID xID)
//	{
//		NFMsg.NFMsgBase.Ident.Builder  xIdent=NFMsg.NFMsgBase.Ident.newBuilder();
//		xIdent.setSvrid(xID.nHead64);
//		xIdent.setIndex(xID.nData64);
//
//		return xIdent.build();
//	}
//
//	public static NFMsg.NFMsgBase.Vector2 NFToPB(NFVector2 value)
//	{
//		NFMsg.NFMsgBase.Vector2.Builder  vector=NFMsg.NFMsgBase.Vector2.newBuilder();
//		vector.setX(value.X());
//		vector.setY(value.Y());
//		return vector.build();
//	}
//
//	public static NFMsg.NFMsgBase.Vector3 NFToPB(NFVector3 value)
//	{
//		NFMsg.NFMsgBase.Vector3.Builder  vector=NFMsg.NFMsgBase.Vector3.newBuilder();
//		vector.setX(value.X());
//		vector.setY(value.Y());
//		vector.setZ(value.Z());
//		return vector.build();
//	}
//
//	public static boolean ReceivePB(int nSockIndex,  int nMsgID,  byte[] msg,  int nLen, byte[] strMsg, NFGUID nPlayer)
//	{
//		NFMsg.NFMsgBase.MsgBase xMsg=NFMsg.NFMsgBase.MsgBase.getDefaultInstance();
//		try {
//			if (xMsg.parseFrom(msg)==null)
//			{
//				byte[] szData=new byte[MAX_PATH];
//				//NFSPRINTF(szData, MAX_PATH, "Parse Message Failed from Packet to MsgBase, MessageID: %d\n", nMsgID);
//				//LogRecive(szData);
//
//				return false;
//			}
//		} catch (InvalidProtocolBufferException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		strMsg=xMsg.getMsgData().toByteArray();
//
//		nPlayer = PBToNF(xMsg.getPlayerId());
//
//		return true;
//	}
//
//	public static boolean ReceivePB( int nSockIndex,  int nMsgID,  byte[] msg,  int nLen, Message xData, NFGUID nPlayer)
//	{
//		NFMsg.NFMsgBase.MsgBase xMsg=NFMsg.NFMsgBase.MsgBase.getDefaultInstance();
//		try {
//			if (xMsg.parseFrom(msg)==null)
//			{
//				byte[] szData=new byte[MAX_PATH];
//				//NFSPRINTF(szData, MAX_PATH, "Parse Message Failed from Packet to MsgBase, MessageID: %d\n", nMsgID);
//				//LogRecive(szData);
//
//				return false;
//			}
//		} catch (InvalidProtocolBufferException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
////		if (!xData.ParseFromString(xMsg.getMsgData()))
////		{
////			byte[] szData=new byte[MAX_PATH];
////			//NFSPRINTF(szData, MAX_PATH, "Parse Message Failed from MsgData to ProtocolData, MessageID: %d\n", nMsgID);
////			//LogRecive(szData);
////
////			return false;
////		}
//
//		nPlayer = PBToNF(xMsg.getPlayerId());
//
//		return true;
//	}
	
	
/////////////////
//as client
public abstract void Initialization( String strIP,   short nPort);

//as server
public abstract int Initialization(  int nMaxClient, String strIP,  int nPort,  int nCpuCount);
public abstract int ExpandBufferSize(  int size);

public abstract void RemoveReceiveCallBack( int nMsgID);

public abstract boolean AddReceiveCallBack( int nMsgID,  NET_RECEIVE_FUNCTOR cb);

public abstract boolean AddReceiveCallBack( NET_RECEIVE_FUNCTOR cb);

public abstract boolean AddEventCallBack( NET_EVENT_FUNCTOR cb);

public abstract boolean Execute();



public abstract <T> boolean SendMsgToSocker(long nSockIndex,T msg);
public abstract <T> boolean SendMsgToAll(T msg);

public abstract NFINet GetNet();
	
}
