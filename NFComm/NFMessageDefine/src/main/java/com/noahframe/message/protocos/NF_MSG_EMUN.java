package com.noahframe.message.protocos;


public class NF_MSG_EMUN {
	public enum  NF_CMD{
		//握手命令组
		NF_CMD_HANDSHAKE((short)1),
		//心跳命令
		NF_CMD_HEARTBEAT((short)2),
		//更新命令
		NF_CMD_UPDATE((short)3),
		//鸿泰车控命令
		HT_CMD_CONTROL((short)4),
		;
		private short _value;

		private NF_CMD(short value) {
			_value = value;
		}

		public short value() {
			return _value;
		}

		public static NF_CMD get(short ntype) {
			for (int i = 0; i < NF_CMD.values().length; i++) {
				NF_CMD val = NF_CMD.values()[i];
				if (val.value() == ntype) {
					return val;
				}
			}
			return null;
		}
	}
	
	
	// 系统协议支持
	public enum NF_MSGTYPE {
		NF_CARCONTROL_DEFAULT((byte) 1), NF_WXPHONE_DEFAULT((byte) 2), NF_TSPSOCKET_DEFAULT(
				(byte) 3);

		private byte _value;

		private NF_MSGTYPE(byte value) {
			_value = value;
		}

		public byte value() {
			return _value;
		}

		public static NF_MSGTYPE get(byte ntype) {
			for (int i = 0; i < NF_MSGTYPE.values().length; i++) {
				NF_MSGTYPE val = NF_MSGTYPE.values()[i];
				if (val.value() == ntype) {
					return val;
				}
			}
			return null;
		}
	}

	public enum NF_MESSAGE {
		NF_HEAD_LENGTH(4), NF_ALL_LENGTH(2), NF_MTYPE_LENGTH(1), NF_STYPE_LENGTH(
				1), NF_CMD_LENGTH(2), NF_DLENGTH_LENGTH(2), NF_KEY_LENGYH(4), NF_CRC_LENGTH(
				2),
		// MSH<
		NF_HEAD_VALUE(1011372877);

		;
		private int _value;

		private NF_MESSAGE(int value) {
			_value = value;
		}

		public int value() {
			return _value;
		}

		public static NF_MESSAGE get(int ntype) {
			for (int i = 0; i < NF_MESSAGE.values().length; i++) {
				NF_MESSAGE val = NF_MESSAGE.values()[i];
				if (val.value() == ntype) {
					return val;
				}
			}
			return null;
		}
	};
}
