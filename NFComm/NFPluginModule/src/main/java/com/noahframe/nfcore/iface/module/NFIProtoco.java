package com.noahframe.nfcore.iface.module;

public interface NFIProtoco {

	public boolean getComplete();
	public boolean fillProtoco(byte[] buff);
	public byte[] CreateProtocolBuff();
	
	public byte getMtype();
	public void setMtype(byte mtype);
	public byte getStype();
	public void setStype(byte stype);
	public int getKey();
	public void setKey(int key);
	public short getCmd();
	public void setCmd(short cmd);
	public byte[] getDatas();
	public void setDatas(byte[] datas);
	public int getLength();
	public int getDLength();
}
