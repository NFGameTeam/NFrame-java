/**   
 * @Title: ${name} 
 * @Package ${package_name} 
 * @Description: 略
 * @author zoecee yideal_formula@126.com  
 * @date 2017.7.6 
 * @version V1.0   
 */
package com.noahframe.plugins.net;

import com.noahframe.api.utils.Converter;
import com.noahframe.message.protocos.NF_Default_Protoco;
import com.noahframe.nfcore.iface.functor.NET_EVENT_FUNCTOR;
import com.noahframe.nfcore.iface.functor.NET_RECEIVE_FUNCTOR;
import com.noahframe.nfcore.iface.module.NFINet;
import com.noahframe.nfcore.iface.module.NFIProtoco;
import com.noahframe.nfcore.iface.module.NetObject;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.WriteCompletionEvent;

import  com.noahframe.message.protocos.NF_MSG_EMUN.*;

public class NFNet extends SimpleChannelHandler implements NFINet {

	public Map<Integer, Long> mmSocketID;
	private Map<Long, NetObject> mmObject;
	private List<Long> mvRemoveObject;

	private int mnMaxConnect;
	private String mstrIP;
	private int mnPort;
	private int mnCpuCount;
	private boolean mbServer;

	private int mnBufferSize;

	private boolean mbWorking;
	
	private boolean mbLocking;

	long mnSendMsgTotal;
	long mnReceiveMsgTotal;

	private NFNet pNet = null;

	private org.jboss.netty.channel.Channel channel;
	private ServerBootstrap bootstrap;

	// struct event_base base;
	// struct evconnlistener listener;
	// ////////////////////////////////////////////////////////////////////////

	NET_RECEIVE_FUNCTOR mRecvCB;
	NET_EVENT_FUNCTOR mEventCB;

	public NFNet() {
		// base = NULL;
		// listener = NULL;
		mstrIP = "";
		mnPort = 0;
		mnCpuCount = 0;
		mbServer = false;
		mbWorking = false;

		mnSendMsgTotal = 0;
		mnReceiveMsgTotal = 0;

		mnBufferSize = 0;

		pNet = this;

		mmSocketID = new HashMap<Integer, Long>();
		mmObject = new HashMap<Long, NetObject>();
		mvRemoveObject = new ArrayList<Long>();

	}

	public <BaseType> NFNet(BaseType pBaseType,
							NET_RECEIVE_FUNCTOR handleRecieve, NET_EVENT_FUNCTOR handleEvent) {
		// base = NULL;
		// listener = NULL;

		// mRecvCB = std.bind(handleRecieve, pBaseType, std.placeholders._1,
		// std.placeholders._2, std.placeholders._3, std.placeholders._4);
		// mEventCB = std.bind(handleEvent, pBaseType, std.placeholders._1,
		// std.placeholders._2, std.placeholders._3);
		mstrIP = "";
		mnPort = 0;
		mnCpuCount = 0;
		mbServer = false;
		mbWorking = false;

		mnSendMsgTotal = 0;
		mnReceiveMsgTotal = 0;

		mnBufferSize = 0;
		pNet = this;
		mRecvCB = handleRecieve;
		mEventCB = handleEvent;

		mmSocketID = new HashMap<Integer, Long>();
		mmObject = new HashMap<Long, NetObject>();
		mvRemoveObject = new ArrayList<Long>();

	}

	@Override
	public boolean Execute() {
		ExecuteClose();

		return true;
	}

	@Override
	public void Initialization(String strIP, int nPort) {
		// TODO Auto-generated method stub

	}

	@Override
	public int Initialization(int nMaxClient, String strIP, int nPort,
			int nCpuCount) {
		mnMaxConnect = nMaxClient;
		mnPort = nPort;
		mnCpuCount = nCpuCount;
		mstrIP = strIP;

		return InitServerNet();
	}

	public int InitServerNet() {

		int nMaxClient = mnMaxConnect;
		int nCpuCount = mnCpuCount;
		int nPort = mnPort;

		// Server服务启动器
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		// 设置一个处理客户端消息和各种消息事件的类(Handler)
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(pNet);
			}
		});
		try {
			channel = bootstrap.bind(new InetSocketAddress(mstrIP, nPort));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mnMaxConnect;
	}

	@Override
	public int ExpandBufferSize(int size) {
		if (size > 0) {
			mnBufferSize = size;
		}
		return mnBufferSize;
	}

	@Override
	public boolean Final() {
		CloseSocketAll();

		return true;
	}

	@Override
	public boolean CloseNetObject(long nSockIndex) {

		System.err.println("删除会话:" + nSockIndex);
		if (mmObject.containsKey(nSockIndex)) {
			NetObject pObject = mmObject.get(nSockIndex);
			pObject.SetNeedRemove(true);
			mvRemoveObject.add(nSockIndex);
			CloseObject(nSockIndex);
			mmObject.remove(nSockIndex);
			int fd = ((ChannelHandlerContext) pObject.GetBuffEvent())
					.getChannel().getId();
			mmSocketID.remove(fd);
			
			return true;
		}
		return false;
	}

	@Override
	public NetObject GetNetObject(long nSockIndex) {
		if (mmObject.containsKey(nSockIndex)) {
			NetObject pObject = mmObject.get(nSockIndex);
			return pObject;
		}
		return null;
	}

	@Override
	public boolean AddNetObject(long nSockIndex, NetObject pObject) {

		if (mmObject.containsKey(nSockIndex)) {
			mmObject.remove(nSockIndex);
		}
		NetObject oldNetObject = getNetObjectByUserID(pObject.GetUserID());
		if (oldNetObject != null) {
			mmObject.remove(oldNetObject.getKey());
		}

		mmObject.put(nSockIndex, pObject);

		return true;
	}

	public void log_cb(int severity, byte[] msg) {

	}

	@Override
	public boolean IsServer() {
		return mbServer;
	}

	@Override
	public boolean Log(int severity, byte[] msg) {
		log_cb(severity, msg);
		return true;
	}

	// Handler实现
	/**
	 * Invoked when a message object (e.g: {@link ChannelBuffer}) was received
	 * from a remote peer.
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		super.messageReceived(ctx, e);

		NetObject pObject = mmObject.get(GetSocketID(ctx.getChannel().getId()));
		if (pObject == null) {
			return;
		}

		NFNet pNet = (NFNet) pObject.GetNet();
		if (pNet == null) {
			return;
		}

		ChannelBuffer input = (ChannelBuffer) e.getMessage();

		int len = input.readableBytes();

		byte[] strMsg = input.array();
		System.out.println("Net 接收原始数据:" + Converter.encodeHexStr(strMsg));
		if (len > 0) {
			int inLen = pObject.AddBuff(strMsg);
		}
		strMsg = null;

		pNet.Dismantle(pObject);

	}

	public boolean Dismantle(NetObject pObject) {
		boolean bNeedDismantle = false;

		for (int i = 0; i < pObject.GetBuff().length
				- NF_MESSAGE.NF_HEAD_LENGTH.value(); i++) {
			byte[] msg = pObject.GetBuff();
			byte[] bHead = new byte[NF_MESSAGE.NF_HEAD_LENGTH
					.value()];
			for (int j = 0; j < bHead.length; j++) {
				bHead[j] = msg[i + j];
			}
			int nHead = Converter.bytesToInt(bHead, 0);
			if (nHead == NF_MESSAGE.NF_HEAD_VALUE
					.value()) {
				NFIProtoco protoco = new NF_Default_Protoco(msg, i);

				if (protoco.getComplete()) {
					pObject.RemoveBuff(i, protoco.getLength());
					i = -1;

					if (mRecvCB != null) {
						mRecvCB.operator(pObject.getKey(), protoco.getMtype(),protoco.getKey(),
								protoco.getCmd(), protoco.getDatas(),
								protoco.getDLength());

						mnReceiveMsgTotal++;
					}
					System.out.println("创建协议对象成功!" + mnReceiveMsgTotal);

				}

			}
		}

		return bNeedDismantle;
	}

	/**
	 * Invoked when an exception was raised by an I/O thread or a
	 * {@link ChannelHandler}.
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		// super.exceptionCaught(ctx, e);
		System.out.println("exceptionCaught:" + e.getCause());
		org.jboss.netty.channel.Channel ch = e.getChannel();
		ch.close();
		ctx.getChannel().close();
		CloseNetObject(this.GetSocketID(ctx.getChannel().getId()));

	}

	/**
	 * Invoked when a {@link Channel} is open, but not bound nor connected.
	 */
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("channelOpen");
		super.channelOpen(ctx, e);
	}

	/**
	 * Invoked when a {@link Channel} is open and bound to a local address, but
	 * not connected.
	 */
	@Override
	public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("channelBound");
		super.channelBound(ctx, e);
	}

	/**
	 * Invoked when a {@link Channel} is open, bound to a local address, and
	 * connected to a remote address.
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("channelConnected");
		super.channelConnected(ctx, e);
		int fd = ctx.getChannel().getId();
		mbWorking = true;

		long SessionID = this.GetSocketID(ctx.getChannel().getId());
		if (SessionID != 0) {
			boolean bClose = this.CloseNetObject(ctx.getChannel().getId());
		}

		if (pNet.mmObject.size() >= pNet.mnMaxConnect) {
			return;
		}
		NetObject<ChannelHandlerContext> pObject = new NetObject<ChannelHandlerContext>(
				pNet, fd, ctx);
		pObject.setMnServerIP();
		pObject.GetNet().AddNetObject(pObject.getKey(), pObject);

		mmSocketID.put(fd, pObject.getKey());

	}

	/**
	 * Invoked when a {@link Channel}'s {@link Channel#getInterestOps()
	 * interestOps} was changed.
	 */
	@Override
	public void channelInterestChanged(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		System.out.println("channelInterestChanged");
		super.channelInterestChanged(ctx, e);
	}

	/**
	 * Invoked when a {@link Channel} was disconnected from its remote peer.
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		System.out.println("channelDisconnected");
		super.channelDisconnected(ctx, e);

	}

	/**
	 * Invoked when a {@link Channel} was unbound from the current local
	 * address.
	 */
	@Override
	public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("channelUnbound");
		super.channelUnbound(ctx, e);
	}

	/**
	 * Invoked when a {@link Channel} was closed and all its related resources
	 * were released.
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("channelClosed");
		super.channelClosed(ctx, e);

		this.CloseNetObject(this.GetSocketID(ctx.getChannel().getId()));
		// CloseObject(ctx.getChannel().getId());
	}

	/**
	 * Invoked when something was written into a {@link Channel}.
	 */
	@Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
			throws Exception {
		System.out.println("writeComplete");
		super.writeComplete(ctx, e);
	}

	/**
	 * Invoked when a child {@link Channel} was open. (e.g. a server channel
	 * accepted a connection)
	 */
	@Override
	public void childChannelOpen(ChannelHandlerContext ctx,
			ChildChannelStateEvent e) throws Exception {
		System.out.println("childChannelOpen");
		super.childChannelOpen(ctx, e);
	}

	/**
	 * Invoked when a child {@link Channel} was closed. (e.g. the accepted
	 * connection was closed)
	 */
	@Override
	public void childChannelClosed(ChannelHandlerContext ctx,
			ChildChannelStateEvent e) throws Exception {
		System.out.println("childChannelClosed");
		super.childChannelClosed(ctx, e);
	}

	/**
	 * Invoked when {@link Channel#write(Object)} is called.
	 */
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		System.out.println("writeRequested");
		super.writeRequested(ctx, e);

		// this.CloseNetObject(this.GetSocketID(ctx.getChannel().getId()));

	}

	/**
	 * Invoked when {@link Channel#bind(SocketAddress)} was called.
	 */
	@Override
	public void bindRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("bindRequested");
		super.bindRequested(ctx, e);

	}

	/**
	 * Invoked when {@link Channel#connect(SocketAddress)} was called.
	 */
	@Override
	public void connectRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("connectRequested");
		super.connectRequested(ctx, e);

	}

	/**
	 * Invoked when {@link Channel#setInterestOps(int)} was called.
	 */
	@Override
	public void setInterestOpsRequested(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		System.out.println("setInterestOpsRequested");
		super.setInterestOpsRequested(ctx, e);
	}

	/**
	 * Invoked when {@link Channel#disconnect()} was called.
	 */
	@Override
	public void disconnectRequested(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		System.out.println("disconnectRequested");
		super.disconnectRequested(ctx, e);

	}

	/**
	 * Invoked when {@link Channel#unbind()} was called.
	 */
	@Override
	public void unbindRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("unbindRequested");
		super.unbindRequested(ctx, e);

	}

	/**
	 * Invoked when {@link Channel#close()} was called.
	 */
	@Override
	public void closeRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("closeRequested");
		if (ctx.getChannel().isConnected()) {
			this.CloseNetObject(this.GetSocketID(ctx.getChannel().getId()));
		}
		
	}

	public int DeCode(byte[] strData, int unAllLen, NFMsgHead xHead) {

		if (unAllLen < NFMsgHead.NF_Head.NF_HEAD_LENGTH.value()) {

			return -1;
		}

		if (NFIMsgHead.NF_Head.NF_HEAD_LENGTH.value() != xHead
				.DeCode(strData)) {

			return -2;
		}

		if (xHead.GetBodyLength() > (unAllLen - NFIMsgHead.NF_Head.NF_HEAD_LENGTH
				.value())) {

			return -3;
		}

		return xHead.GetBodyLength();
	}

	public int EnCode(int unMsgID, byte[] strData, int unDataLen,
			byte[] strOutData) {
		NFMsgHead xHead = new NFMsgHead();
		xHead.SetMsgID(unMsgID);
		xHead.SetBodyLength(unDataLen);

		byte[] szHead = new byte[NFIMsgHead.NF_Head.NF_HEAD_LENGTH.value()];
		xHead.EnCode(szHead);

		StringBuffer outdata = new StringBuffer();
		outdata.append(new String(szHead));
		outdata.append(new String(strData));
		strOutData = outdata.toString().getBytes();

		return xHead.GetBodyLength()
				+ NFIMsgHead.NF_Head.NF_HEAD_LENGTH.value();
	}

	@Override
	public boolean CloseChannel() {
		ChannelFuture future = channel.close();
		future.awaitUninterruptibly();
		bootstrap.releaseExternalResources();

		return true;
	}

	public boolean CloseSocketAll() {

		Iterator<Map.Entry<Long, NetObject>> it = mmObject.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Long, NetObject> entry = it.next();
			Long nFD = entry.getKey();
			mvRemoveObject.add(nFD);

		}
		ExecuteClose();

		mmObject.clear();

		return true;
	}

	public void ExecuteClose() {
		for (int i = 0; i < mvRemoveObject.size(); ++i) {
			Long nSocketIndex = mvRemoveObject.get(i);
			CloseObject(nSocketIndex);
		}

		mvRemoveObject.clear();
	}

	public void CloseObject(Long nSockIndex) {
		if (mmObject.containsKey(nSockIndex)) {
			NetObject<ChannelHandlerContext> pObject = mmObject.get(nSockIndex);
			ChannelHandlerContext bev = pObject.GetBuffEvent();
			bev.getChannel().disconnect();
			bev = null;
			mmObject.remove(nSockIndex);
			pObject = null;
		}
	}

	@Override
	public <T> boolean SendMsgToSocker(long nSockIndex, T msg) {
		// TODO Auto-generated method stub

		byte[] _msg = (byte[]) msg;

		return SendMsg(_msg, _msg.length, nSockIndex);

	}

	public boolean SendMsg(byte[] msg, int nLen, long nSockIndex) {
		if (nLen <= 0) {
			return false;
		}

		if (mmObject.containsKey(nSockIndex)) {
			NetObject<ChannelHandlerContext> pNetObject = mmObject
					.get(nSockIndex);
			if (pNetObject != null) {
				ChannelHandlerContext bev = pNetObject.GetBuffEvent();

				ChannelBuffer buf = ChannelBuffers.buffer(nLen);
				buf.writeBytes(msg);
				System.out.println("TCP发送原始数据:" + Converter.encodeHexStr(msg));
				if (null != bev && mbWorking) {
					bev.getChannel().write(buf);
					mnSendMsgTotal++;
					return true;
				}

			}
		}
		return false;
	}

	@Override
	public <T> long GetSocketID(T socketid) {

		if (mmSocketID.containsKey(socketid)) {
			return mmSocketID.get(socketid);
		}
		return 0;
	}

	@Override
	public NetObject getNetObjectByUserID(int nUserID) {
		Iterator<Map.Entry<Long, NetObject>> it = mmObject.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Long, NetObject> entry = it.next();
			NetObject netObject = entry.getValue();

			if (netObject.GetUserID() == nUserID) {
				return netObject;
			}
		}

		return null;
	}

	@Override
	public NetObject getNetObjectByModelID(int nModelID) {

		Iterator<Map.Entry<Long, NetObject>> it = mmObject.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Long, NetObject> entry = it.next();
			NetObject netObject = entry.getValue();

			List<Integer> moduleids = netObject.getMnSocketModelID();
			if (moduleids.contains(nModelID)) {
				return netObject;
			}
		}

		return null;
	}

	@Override
	public void putModelID(long nSockIndex, int nModelID) {
		if (mmObject.containsKey(nSockIndex)) {
			mmObject.get(nSockIndex).putModelID(nModelID);
		}
	}

	@Override
	public <T> boolean SendMsgToAll(T msg) {
		Iterator<Map.Entry<Long, NetObject>> it = mmObject.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Long, NetObject> entry = it.next();
			Long nFD = entry.getKey();
			System.out.println("向(" + nFD + ")发送数据：");
			SendMsg((byte[]) msg, ((byte[]) msg).length, nFD);
		}
		return true;
	}

	@Override
	public void removeNetObjectByUserID(int nUserID) {
		Iterator<Map.Entry<Long, NetObject>> it = mmObject.entrySet()
				.iterator();

		List<Long> removeKeys = new ArrayList<Long>();

		while (it.hasNext()) {
			Map.Entry<Long, NetObject> entry = it.next();
			NetObject netObject = entry.getValue();

			if (netObject.GetUserID() == nUserID) {
				netObject.GetNet().CloseNetObject(netObject.getKey());
				removeKeys.add(entry.getKey());
			}
		}

		for (int i = 0; i < removeKeys.size(); i++) {

			mmObject.remove(removeKeys.get(i));
		}

	}

	public boolean isMbLocking() {
		return mbLocking;
	}

	public void setMbLocking(boolean mbLocking) {
		this.mbLocking = mbLocking;
	}

}
