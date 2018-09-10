/**   
* @Title: NetObject 
* @Package ${package_name} 
* @Description: 网络基础对象
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;

import com.noahframe.api.file.SysProperties;
import com.noahframe.api.utils.ByteUtil;
import com.noahframe.api.utils.Converter;
import com.noahframe.api.utils.DateUtil;
import com.noahframe.api.utils.IDCreater;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;




public class NetObject<T> {

	// sockaddr_in sin;
	T bev;

	ByteUtil mstrBuff;
	LinkedBlockingQueue<NFIProtoco> mProtocos;
	String mstrUserData;


	private int mnServerIP;
	public String sIP;
	private byte mbProtocolType;

	int mnLogicState;
	int mnGameID;
	int mnUserID;// player id
	private List<Integer> mnSocketModelID;
	private List<Integer> mnClientID;// temporary client id
	private int mnHashIdentID;// hash ident, special for distributed
	private NFINet m_pNet;
	//
	private int nFD;
	private boolean bNeedRemove;

	private boolean bNetLocking=false;
	private boolean bControlLocked=false;
	private int ExpiredTime=0;
	private Date Expired=null;
	private Date LockExpired=null;

	private Timer timer;
	private LockTask task;

	public boolean isbNetLocking() {
		return bNetLocking;
	}


	public void setbNetLocking(boolean bNetLocking) {
		this.bNetLocking = bNetLocking;

		if (bNetLocking) {
			timer = new Timer();
			task = new LockTask();
			//安排指定的任务在指定的时间开始进行重复的固定延迟执行。
			timer.schedule(task,2000);
		}
	}

	public Date getExpired() {
		return Expired;
	}

	public void setExpired(Date expired) {
		Expired = expired;
	}

	public int getExpiredTime() {
		return ExpiredTime;
	}

	public void setExpiredTime(int expiredTime) {
		ExpiredTime = expiredTime;
	}

	public boolean isbControlLocked() {
		return bControlLocked;
	}

	public void setbControlLocked(boolean bControlLocked) {
		this.bControlLocked = bControlLocked;
	}

	public Date getLockExpired() {
		return LockExpired;
	}

	public void setLockExpired(Date lockExpired) {
		LockExpired = lockExpired;
	}

	public class LockTask extends TimerTask {
		public void run() {
			SysProperties properties=new SysProperties("sys");
			System.out.println("释放连接对象锁定状态:"+bNetLocking);
			if (bNetLocking) {
				bNetLocking=false;
			}
			if (LockExpired!=null && DateUtil.getSecondsBetweenDate(new Date().getTime(),LockExpired.getTime())>Integer.valueOf(properties.getParams("control_timeout")))
			{
				bControlLocked=false;
			}
			task.cancel();
			task=null;
			timer.cancel();
			timer=null;
		}
	}


	public NetObject(NFINet pNet, int fd,T pBev) {
		mnLogicState = 0;
		mnGameID = 0;
		nFD = fd;
		bNeedRemove = false;

		m_pNet = pNet;
		bev=pBev;
		mstrBuff=new ByteUtil(new byte[1024*1024]);
		mProtocos=new LinkedBlockingQueue<NFIProtoco>();
		setMnSocketModelID(new ArrayList<Integer>());
		setMnClientID(new ArrayList<Integer>());
		sIP= IDCreater.instance().getIp();
		SysProperties properties=new SysProperties("sys");
		Expired= DateUtil.addSecond(new Date(),Integer.valueOf(properties.getParams("socket_timeout")));

	}


	public int AddBuff(byte[] str) {

		mstrBuff.append(str, str.length);

		return  mstrBuff.getSize();
	}

	public int AddProtoco(NFIProtoco protoco)
	{
		mProtocos.add(protoco);
		return mProtocos.size();
	}

	public NFIProtoco getProtoco()
	{
		try {
			return mProtocos.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public byte[] GetBuff()
	{
		return mstrBuff.getBuffer(0,mstrBuff.getSize());
	}


//	public int CopyBuffTo(byte[] str, int nStart, int nLen) {
//		if (nStart + nLen > mstrBuff.limit()) {
//			return 0;
//		}
//
//		byte[] byte_mstrBuff = mstrBuff.array();
//
//		for (int i = 0; i < nLen; i++) {
//			str[i] = byte_mstrBuff[i + nStart];
//		}
//		return nLen;
//	}

	public int RemoveBuff(int nStart, int nLen) {

		if (nStart < 0) {
			return 0;
		}

		if (nStart + nLen > mstrBuff.getSize()) {
			return 0;
		}

		mstrBuff.erase(nStart, nLen);

		return mstrBuff.getSize();


	}

	// int RemoveBuff(int nStart, int nLen)
	// {
	// if (nStart < 0)
	// {
	// return 0;
	// }
	//
	// if (nStart + nLen > mstrBuff.limit())
	// {
	// return 0;
	// }
	// byte[] byte_mstrBuff=mstrBuff.array();
	//
	// for (int i = 0; i < mstrBuff.limit()-nStart; i++) {
	//
	// if (i+nStart>mstrBuff.limit()) {
	// byte_mstrBuff[nStart+i]=0;
	// continue;
	// }
	//
	// byte_mstrBuff[nStart+i]=byte_mstrBuff[i+nLen];
	// }
	// return mstrBuff.limit();
	// }

//	public byte[] GetBuff() {
//
//		for (int i = 0; i < mstrBuff.size(); i++) {
//		}
//
//		return mstrBuff.array();
//	}

//	public int GetBuffLen() {
//		return mstrBuff.position();
//	}

	public NFINet GetNet() {
		return m_pNet;
	}

	// ////////////////////////////////////////////////////////////////////////
	public int GetConnectKeyState() {
		return mnLogicState;
	}

	public void SetConnectKeyState(int nState) {
		mnLogicState = nState;
	}

	public boolean NeedRemove() {
		return bNeedRemove;
	}

	public void SetNeedRemove(boolean b) {
		bNeedRemove = b;
	}

	public String GetAccount() {
		return mstrUserData;
	}

	public void SetAccount(String strData) {
		mstrUserData = strData;
	}

	public int GetGameID() {
		return mnGameID;
	}

	public void SetGameID(int nData) {
		mnGameID = nData;
	}

	public int GetUserID() {
		return mnUserID;
	}

	public void SetUserID(int nUserID) {
		mnUserID = nUserID;
	}


	public int GetHashIdentID() {
		return mnHashIdentID;
	}

	public void SetHashIdentID(int xHashIdentID) {
		mnHashIdentID = xHashIdentID;
	}

	public int GetRealFD() {
		return nFD;
	}

	public T GetBuffEvent()
	{
		return bev;
	}


	public int getMnServerIP() {
		return mnServerIP;
	}


	public void setMnServerIP() {

		String[] ips = sIP.split("\\.");
		byte[] ip=new byte[4];

		ip[0] = (byte) (Integer.parseInt(ips[0]) & 0xFF);
		ip[1] = (byte) (Integer.parseInt(ips[1]) & 0xFF);
		ip[2] = (byte) (Integer.parseInt(ips[2]) & 0xFF);
		ip[3] = (byte) (Integer.parseInt(ips[3]) & 0xFF);

		this.mnServerIP = Converter.bytesToInt(ip, 0);
	}


	public byte getMbProtocolType() {
		return mbProtocolType;
	}


	public void setMbProtocolType(byte mbProtocolType) {
		this.mbProtocolType = mbProtocolType;
	}


	public int getMnBevID() {
		return nFD;
	}


	public void setMnBevID(int mnBevID) {
		this.nFD = mnBevID;
	}

	public long getKey()
	{
		byte[] byteID = new byte[8];

		try {
			byteID[0] = (byte) (mnServerIP>>>24);
			byteID[1] = (byte) (mnServerIP>>>16);
			byteID[2] = (byte) (mnServerIP>>>8);
			byteID[3] = (byte) (mnServerIP>>>0);

			byteID[4] = mbProtocolType;
			byteID[5] = (byte) (nFD >>> 16);
			byteID[6] = (byte) (nFD >>> 8);
			byteID[7] = (byte) (nFD >>> 0);

			return Converter.bytes2long(byteID);
		} catch (Exception e) {
			throw new IllegalArgumentException(mnServerIP + " is invalid IP");
		}

	}


	public List<Integer> getMnSocketModelID() {
		return mnSocketModelID;
	}


	public void setMnSocketModelID(List<Integer> mnSocketModelID) {
		this.mnSocketModelID = mnSocketModelID;
	}

	public void putModelID(int nModelID)
	{
		if (!this.mnSocketModelID.contains(nModelID)) {
			this.mnSocketModelID.add(nModelID);
		}

	}


	public List<Integer> getMnClientID() {
		return mnClientID;
	}


	public void setMnClientID(List<Integer> mnClientID) {
		this.mnClientID = mnClientID;
	}

	public void putClientID(int nClientID)
	{
		if (!this.mnClientID.contains(nClientID)) {
			this.mnClientID.add(nClientID);
		}

	}

}
