/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;


import com.noahframe.api.utils.Converter;
import com.noahframe.api.utils.MemoryUtil;

public interface NFINet {
	
	public abstract class NFIMsgHead{
		public enum NF_Head
		 {
		        NF_HEAD_LENGTH(4);
		        
		        private int _value;

				private NF_Head(int value) {
					_value = value;
				}

				public int value() {
					return _value;
				}
				public static NF_Head get(int ntype)
				{
					for (int i = 0; i < NF_Head.values().length; i++) {
						NF_Head val=NF_Head.values()[i];
						if (val.value()==ntype) {
							return val;
						}
					}
					return null;
				}
		 };
		 
		 public abstract int EnCode(byte[] strData);
		 public abstract int DeCode(byte[] strData);

		 public abstract int GetMsgID();
		 public abstract void SetMsgID(int nMsgID);

		 public abstract int GetBodyLength();
		 public abstract void SetBodyLength(int nLength);

		 public int NF_HTONLL(int nData)
		    {
//		#if NF_PLATFORM == NF_PLATFORM_WIN
//		//#ifdef _MSC_VER
//		        return htonll(nData);
//		#elif NF_PLATFORM == NF_PLATFORM_APPLE || NF_PLATFORM == NF_PLATFORM_APPLE_IOS
//		//#elseifdef __APPLE_CC__
//		        return OSSwapHostToBigInt64(nData);
//		#else
//		        return htobe64(nData);
//		#endif
			 return nData;
		    }

		 public int NF_NTOHLL(int nData)
		    {
//		#if NF_PLATFORM == NF_PLATFORM_WIN
//		//#ifdef _MSC_VER
//		        return ntohll(nData);
//		#elif NF_PLATFORM == NF_PLATFORM_APPLE || NF_PLATFORM == NF_PLATFORM_APPLE_IOS
//		//#elseifdef __APPLE__
//		        return OSSwapBigToHostInt64(nData);
//		#else
//		        return be64toh(nData);
//		#endif
			 return nData;
		    }

		 public int NF_HTONL(int nData)
		    {
//		#if NF_PLATFORM == NF_PLATFORM_WIN
//		//#ifdef _MSC_VER
//		        return htonl(nData);
//		#elif NF_PLATFORM == NF_PLATFORM_APPLE || NF_PLATFORM == NF_PLATFORM_APPLE_IOS
//		//#elseifdef __APPLE__
//		        return OSSwapHostToBigInt32(nData);
//		#else
//		        return htobe32(nData);
//		#endif
			 return nData;
		    }

		 public int NF_NTOHL(int nData)
		    {
//		#if NF_PLATFORM == NF_PLATFORM_WIN
//		//#ifdef _MSC_VER
//		        return ntohl(nData);
//		#elif NF_PLATFORM == NF_PLATFORM_APPLE || NF_PLATFORM == NF_PLATFORM_APPLE_IOS
//		//#elseifdef __APPLE__
//		        return OSSwapBigToHostInt32(nData);
//		#else
//		        return be32toh(nData);
//		#endif
			 return nData;
		    }

		 public int NF_HTONS(int nData)
		    {
//		#if NF_PLATFORM == NF_PLATFORM_WIN
//		//#ifdef _MSC_VER
//		        return htons(nData);
//		#elif NF_PLATFORM == NF_PLATFORM_APPLE || NF_PLATFORM == NF_PLATFORM_APPLE_IOS
//		//#elseifdef __APPLE__
//		        return OSSwapHostToBigInt16(nData);
//		#else
//		        return htobe16(nData);
//		#endif
			 return nData;
		    }

		 public int NF_NTOHS(int nData)
		    {
//		#if NF_PLATFORM == NF_PLATFORM_WIN
//		//#ifdef _MSC_VER
//		        return ntohs(nData);
//		#elif NF_PLATFORM == NF_PLATFORM_APPLE || NF_PLATFORM == NF_PLATFORM_APPLE_IOS
//		//#elseifdef __APPLE__
//		        return OSSwapBigToHostInt16(nData);
//		#else
//		        return be16toh(nData);
//		#endif
			 return nData;
		    }
		 
	}
	
	public class NFMsgHead extends NFIMsgHead{

		protected int munSize;
		protected int munMsgID;
		
		public NFMsgHead()
		    {
		        munSize = 0;
		        munMsgID = 0;
		    }
		
		@Override
		public int EnCode(byte[] strData) {
			 int nOffset = 0;

		        int nMsgID = NF_HTONS(munMsgID);
		        MemoryUtil.memcpy(strData, nOffset, nMsgID);
		        nOffset += 4;

		        int nPackSize = munSize + NF_Head.NF_HEAD_LENGTH.value();
		        int nSize = NF_HTONL(nPackSize);
		        MemoryUtil.memcpy(strData, nOffset, nSize);
		        nOffset += 4;

		        if (nOffset != NF_Head.NF_HEAD_LENGTH.value())
		        {
		            assert false;
		        }

		        return nOffset;
		}

		@Override
		public int DeCode(byte[] strData) {
			  int nOffset = 0;

			  byte[] nMsgID = new byte[2];
			  MemoryUtil.memcpy(nMsgID, 0, strData, nOffset, 2);
			  
		        munMsgID = NF_NTOHS(Converter.hBytesToShort(nMsgID));
		        nOffset += nMsgID.length;

		        byte[] nPackSize = new byte[4];
				MemoryUtil.memcpy(nPackSize, 0, strData, nOffset, 4);
				
		        munSize = NF_NTOHL(Converter.hBytesToInt(nPackSize)) - NF_Head.NF_HEAD_LENGTH.value();
		        nOffset += nPackSize.length;

		        if (nOffset != NF_Head.NF_HEAD_LENGTH.value())
		        {
		        	assert false;
		        }

		        return nOffset;
		}

		@Override
		public int GetMsgID() {
			return munMsgID;
		}

		@Override
		public void SetMsgID(int nMsgID) {
			munMsgID = nMsgID;
		}

		@Override
		public int GetBodyLength() {
			return munSize;
		}

		@Override
		public void SetBodyLength(int nLength) {
			munSize = nLength;
		}
		
	}
	
	public enum NF_NET_EVENT {
	    NF_NET_EVENT_EOF(0x10),
	    NF_NET_EVENT_ERROR(0x20),
	    NF_NET_EVENT_TIMEOUT(0x40),
	    NF_NET_EVENT_CONNECTED(0x80);
	    
	    private int _value;

		private NF_NET_EVENT(int value) {
			_value = value;
		}

		public int value() {
			return _value;
		}
		public static NF_NET_EVENT get(int ntype)
		{
			for (int i = 0; i < NF_NET_EVENT.values().length; i++) {
				NF_NET_EVENT val=NF_NET_EVENT.values()[i];
				if (val.value()==ntype) {
					return val;
				}
			}
			return null;
		}
	}

	

    //need to call this function every frame to drive network library
    public boolean Execute() ;

    public void Initialization(String strIP, int nPort) ;
    public int Initialization(int nMaxClient, String strIP, int nPort, int nCpuCount) ;
	public int ExpandBufferSize(int size) ;

    public boolean Final() ;

    //send a message with out msg-head[auto add msg-head in this function]
    public <T> boolean SendMsgToSocker(long nSockIndex, T msg) ;
    public <T> boolean SendMsgToAll(T msg) ;

    public <T> long GetSocketID(T socketid);
    
    public NetObject getNetObjectByUserID(int nUserID);
    
    public void removeNetObjectByUserID(int nUserID);
    
    public NetObject getNetObjectByModelID(int nModelID);
    
    public void putModelID(long nSockIndex, int nModelID);
    
    public boolean CloseNetObject(long nSockIndex) ;
    public NetObject GetNetObject(long nSockIndex) ;
    public boolean AddNetObject(long nSockIndex, NetObject pObject) ;

    public boolean IsServer() ;
    
    public boolean CloseChannel();

    public boolean Log(int severity, byte[] msg) ;
    
}
