package com.noahframe.api.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IDCreater {

	private AtomicInteger _id = new AtomicInteger();
	/*
	 * 锁
	 */
	private static Object LOCK = new Object();
	/*
	 * 唯一实例
	 */
	private static IDCreater ID = null;

	public static IDCreater instance() {
		synchronized (LOCK) {
			if (null == ID) {
				ID = new IDCreater();
			}
			return ID;
		}
	}
	
//	public static long ID(long id,int protocol) {
//		// 这里之所以重新定义ID，是因为MINA的架构导致该类只有一个实例，无法在类中定义IYHSession成员变量
//		id += IDCreater.instance().getIPLong(protocol);
//		return id;
//	}
//	
//	public Long getIPLong(int protocol)
//	{
//		String sIP = getIp();
//		
////		System.err.println("Session TCP:当前服务器IP地址:"+sIP);
//
//		byte[] byteID = new byte[8];
//		try {
//			String[] ips = sIP.split("\\.");
//
//			byteID[0] = (byte) (Integer.parseInt(ips[0]) & 0xFF);
//			byteID[1] = (byte) (Integer.parseInt(ips[1]) & 0xFF);
//			byteID[2] = (byte) (Integer.parseInt(ips[2]) & 0xFF);
//			byteID[3] = (byte) (Integer.parseInt(ips[3]) & 0xFF);
//
//			byteID[4] = (byte) (protocol & 0xFF);
//			byteID[5] = 0;
//			byteID[6] = 0;
//			byteID[7] = 0;
//
//			return bytes2long(byteID);
//		} catch (Exception e) {
//			throw new IllegalArgumentException(sIP + " is invalid IP");
//		}
//	}
//	
	public int get()
	{
		return _id.incrementAndGet();
	}

//	public Long get() {
//
//		// InetAddress netAddress = getInetAddress();
//
//		String sIP = getIp();
////		System.err.println("Session:当前服务器IP地址:"+sIP);
//
//		byte[] byteID = new byte[8];
//		long nIdx = _id.incrementAndGet();
////		System.out.println("nIdx：" + nIdx);
//
//		try {
//			String[] ips = sIP.split("\\.");
//
//			byteID[0] = (byte) (Integer.parseInt(ips[0]) & 0xFF);
//			byteID[1] = (byte) (Integer.parseInt(ips[1]) & 0xFF);
//			byteID[2] = (byte) (Integer.parseInt(ips[2]) & 0xFF);
//			byteID[3] = (byte) (Integer.parseInt(ips[3]) & 0xFF);
//
//			byteID[4] = (byte) (nIdx >>> 24);
//			byteID[5] = (byte) (nIdx >>> 16);
//			byteID[6] = (byte) (nIdx >>> 8);
//			byteID[7] = (byte) (nIdx >>> 0);
//
//			return bytes2long(byteID);
//		} catch (Exception e) {
//			throw new IllegalArgumentException(sIP + " is invalid IP");
//		}
//	}
	
	
//	public Long get(int protocol) {
//
//		// InetAddress netAddress = getInetAddress();
//
//		String sIP = getIp();
//
//		byte[] byteID = new byte[8];
//		long nIdx = _id.incrementAndGet();
////		System.out.println("nIdx：" + nIdx);
//
//		try {
//			String[] ips = sIP.split("\\.");
//
//			byteID[0] = (byte) (Integer.parseInt(ips[0]) & 0xFF);
//			byteID[1] = (byte) (Integer.parseInt(ips[1]) & 0xFF);
//			byteID[2] = (byte) (Integer.parseInt(ips[2]) & 0xFF);
//			byteID[3] = (byte) (Integer.parseInt(ips[3]) & 0xFF);
//
//			byteID[4] = (byte) (protocol & 0xFF);
//			byteID[5] = (byte) (nIdx >>> 16);
//			byteID[6] = (byte) (nIdx >>> 8);
//			byteID[7] = (byte) (nIdx >>> 0);
//
//			return bytes2long(byteID);
//		} catch (Exception e) {
//			throw new IllegalArgumentException(sIP + " is invalid IP");
//		}
//	}
//	
//
//	public Long get(long nIdx, int protocol) {
//
//		// InetAddress netAddress = getInetAddress();
//
//		String sIP = getIp();
////		System.err.println("Session:当前服务器IP地址:"+sIP);
//
//		byte[] byteID = new byte[8];
////		System.out.println("nIdx：" + nIdx);
//
//		try {
//			String[] ips = sIP.split("\\.");
//
//			byteID[0] = (byte) (Integer.parseInt(ips[0]) & 0xFF);
//			byteID[1] = (byte) (Integer.parseInt(ips[1]) & 0xFF);
//			byteID[2] = (byte) (Integer.parseInt(ips[2]) & 0xFF);
//			byteID[3] = (byte) (Integer.parseInt(ips[3]) & 0xFF);
//
//			byteID[4] = (byte) (protocol & 0xFF);
//			byteID[5] = (byte) (nIdx >>> 16);
//			byteID[6] = (byte) (nIdx >>> 8);
//			byteID[7] = (byte) (nIdx >>> 0);
//
//			return bytes2long(byteID);
//		} catch (Exception e) {
//			throw new IllegalArgumentException(sIP + " is invalid IP");
//		}
//	}
//	
//	
//	public String long2ip(Long num) {
//		byte[] bytes = long2bytes(num);
//		StringBuffer buff = new StringBuffer();
//		buff.append(bytes[0] & 0xFF);
//		buff.append(".");
//		buff.append(bytes[1] & 0xFF);
//		buff.append(".");
//		buff.append(bytes[2] & 0xFF);
//		buff.append(".");
//		buff.append(bytes[3] & 0xFF);
//
////		long aa = ((((long) bytes[4] & 0xff) << 24)
////				| (((long) bytes[5] & 0xff) << 16)
////				| (((long) bytes[6] & 0xff) << 8) | (((long) bytes[7] & 0xff) << 0));
////		buff.append("=");
////		buff.append(aa);
//		return buff.toString();
//	}

//	/**
//	 * byte[]转 long
//	 * 
//	 * @param b
//	 * @return
//	 */
//	private long bytes2long(byte[] b) {
//		long temp = 0l;
//		long res = 0l;
//		for (int i = 0; i < 8; i++) {
//			res <<= 8;
//			temp = b[i] & 0xff;
//			res |= temp;
//		}
//		return res;
//	}

//	/**
//	 * long转 byte[]
//	 * 
//	 * @param num
//	 * @return
//	 */
//	private byte[] long2bytes(long num) {
//		byte[] b = new byte[8];
//		for (int i = 0; i < 8; i++) {
//			b[i] = (byte) (num >>> (56 - (i * 8)));
//		}
//		return b;
//	}

	/**
	 * 多IP处理，可以得到最终ip
	 * 
	 * @return
	 */
	public String getIp() {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			boolean finded = false;// 是否找到外网IP
			while (netInterfaces.hasMoreElements() && !finded) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {// 外网IP
						netip = ip.getHostAddress();
						finded = true;
						break;
					} else if (ip.isSiteLocalAddress()
							&& !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
						localip = ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}
}
