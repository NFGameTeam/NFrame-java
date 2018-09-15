package com.noahframe.message.protocos;

import java.nio.ByteBuffer;

import com.noahframe.api.utils.CRC_MODBUS16;
import  com.noahframe.message.protocos.NF_MSG_EMUN.*;
import com.noahframe.nfcore.iface.module.NFIProtoco;

public class NF_Default_Protoco implements NFIProtoco {

	private int nhead = 0;
	private short nlength = 0;
	private byte bmtype = 0;
	private byte bstype = 0;
	private short ncmd = 0;
	private short ndlength = 0;
	private int nkeyid = 0;
	private byte[] datas;
	private short ncrc = 0;
	private boolean isComplete = false;
	private int currLength = 0;

	private int isBreak = 0;

	public NF_Default_Protoco() {
		nhead = NF_MESSAGE.NF_HEAD_VALUE.value();
	}

	public NF_Default_Protoco(byte[] buff, int start) {
		int nStart = start;

		// 赋值头
		if (buff.length - nStart >= NF_MESSAGE.NF_HEAD_LENGTH.value()) {
			byte[] bHead = new byte[NF_MESSAGE.NF_HEAD_LENGTH.value()];
			System.arraycopy(buff, nStart, bHead, 0,
					NF_MESSAGE.NF_HEAD_LENGTH.value());
			nhead = bytesToInt(bHead, 0);
			currLength += NF_MESSAGE.NF_HEAD_LENGTH.value();
			nStart += NF_MESSAGE.NF_HEAD_LENGTH.value();
		}
		// 赋值长度
		if (buff.length - nStart >= NF_MESSAGE.NF_ALL_LENGTH.value()) {
			byte[] bLength = new byte[NF_MESSAGE.NF_ALL_LENGTH.value()];
			System.arraycopy(buff, nStart, bLength, 0,
					NF_MESSAGE.NF_ALL_LENGTH.value());
			nlength = bytesToShort(bLength, 0);
			currLength += NF_MESSAGE.NF_ALL_LENGTH.value();
			nStart += NF_MESSAGE.NF_ALL_LENGTH.value();
		}

		if (buff.length >= nlength) {
			// msg Type
			bmtype = buff[nStart];
			currLength += NF_MESSAGE.NF_MTYPE_LENGTH.value();
			nStart += NF_MESSAGE.NF_MTYPE_LENGTH.value();
			// socket Type
			bstype = buff[nStart];
			currLength += NF_MESSAGE.NF_STYPE_LENGTH.value();
			nStart += NF_MESSAGE.NF_STYPE_LENGTH.value();

			// CMD
			byte[] bCmd = new byte[NF_MESSAGE.NF_CMD_LENGTH.value()];
			System.arraycopy(buff, nStart, bCmd, 0,
					NF_MESSAGE.NF_CMD_LENGTH.value());
			ncmd = bytesToShort(bCmd, 0);
			currLength += NF_MESSAGE.NF_CMD_LENGTH.value();
			nStart += NF_MESSAGE.NF_CMD_LENGTH.value();

			byte[] bDataLength = new byte[NF_MESSAGE.NF_DLENGTH_LENGTH
					.value()];
			System.arraycopy(buff, nStart, bDataLength, 0,
					NF_MESSAGE.NF_DLENGTH_LENGTH.value());
			ndlength = bytesToShort(bDataLength, 0);
			currLength += NF_MESSAGE.NF_DLENGTH_LENGTH.value();
			nStart += NF_MESSAGE.NF_DLENGTH_LENGTH.value();
			// 钥匙ID
			byte[] bkeyid = new byte[NF_MESSAGE.NF_KEY_LENGYH.value()];
			System.arraycopy(buff, nStart, bkeyid, 0,
					NF_MESSAGE.NF_KEY_LENGYH.value());
			nkeyid = bytesToLInt(bkeyid, 0);
			currLength += NF_MESSAGE.NF_KEY_LENGYH.value();
			nStart += NF_MESSAGE.NF_KEY_LENGYH.value();
			// data
			if (ndlength > 0) {
				datas = new byte[ndlength];
				System.arraycopy(buff, nStart, datas, 0, ndlength);
				currLength += (int) ndlength;
				nStart += (int) ndlength;
			}
			byte[] crc = new byte[NF_MESSAGE.NF_CRC_LENGTH.value()];
			System.arraycopy(buff, nStart, crc, 0,
					NF_MESSAGE.NF_CRC_LENGTH.value());

			byte[] packbytes = new byte[nlength
					- NF_MESSAGE.NF_CRC_LENGTH.value()];
			System.arraycopy(buff, 0, packbytes, 0, nlength
					- NF_MESSAGE.NF_CRC_LENGTH.value());

			short verCrc = CRC_MODBUS16.getCRC(packbytes);

			short pacCrc = bytesToLShort(crc, 0);
			if (verCrc == pacCrc) {
				System.out.println("数据包验证码通过验证crc:" + verCrc + ",包crc:"
						+ pacCrc);
				ncrc = pacCrc;
				isComplete = true;
			} else {
				System.err.println("数据包验证码不通过验证crc:" + verCrc + ",包crc:"
						+ pacCrc);
				ncrc = 0;
				isBreak = 3;
			}

		}

	}

	@Override
	public boolean getComplete() {
		return isComplete;
	}

	@Override
	public boolean fillProtoco(byte[] buff) {

		return isComplete;
	}
	
	
	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
	 * 
	 * @param src
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return int数值
	 */
	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
		return value;
	}


	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
	 * 
	 * @param src
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return int数值
	 */
	private int bytesToLInt(byte[] src, int offset) {
		int value;
		value = (int) ((src[offset+3] & 0xFF) | ((src[offset + 2] & 0xFF) << 8)
				| ((src[offset + 1] & 0xFF) << 16) | ((src[offset] & 0xFF) << 24));
		return value;
	}

	private short bytesToShort(byte[] src, int offset) {
		short value;
		value = (short) ((src[offset+1] & 0xFF) | ((src[offset] & 0xFF) << 8));
		return value;
	}
	
	private short bytesToLShort(byte[] src, int offset) {
		short value;
		value = (short) ((src[offset] & 0xFF) | ((src[offset+1] & 0xFF) << 8));
		return value;
	}

	public static byte[] shortToBytes(short value) {
		byte[] src = new byte[2];
		src[0] = (byte) ((value >> 8) & 0xFF);
		src[1] = (byte) (value & 0xFF);
		return src;
	}

	public static byte[] shortToLBytes(short value) {
		byte[] src = new byte[2];
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}
	
	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt（）配套使用
	 * 
	 * @param value
	 *            要转换的int值
	 * @return byte数组
	 */
	public static byte[] intToLBytes(int value) {
		byte[] src = new byte[4];
		src[0] = (byte) ((value >> 24) & 0xFF);
		src[1] = (byte) ((value >> 16) & 0xFF);
		src[2] = (byte) ((value >> 8) & 0xFF);
		src[3] = (byte) (value & 0xFF);
		return src;
	}
	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt（）配套使用
	 * 
	 * @param value
	 *            要转换的int值
	 * @return byte数组
	 */
	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	@Override
	public byte[] CreateProtocolBuff() {
		short ProtocoLength = (short) (NF_MESSAGE.NF_HEAD_LENGTH.value()
				+ NF_MESSAGE.NF_ALL_LENGTH.value()
				+ NF_MESSAGE.NF_MTYPE_LENGTH.value()
				+ NF_MESSAGE.NF_STYPE_LENGTH.value()
				+ NF_MESSAGE.NF_CMD_LENGTH.value()
				+ NF_MESSAGE.NF_DLENGTH_LENGTH.value()
				+ NF_MESSAGE.NF_KEY_LENGYH.value() + datas.length + NF_MESSAGE.NF_CRC_LENGTH
				.value());

		ByteBuffer buffer = ByteBuffer.allocate(ProtocoLength);
		buffer.put(intToBytes(NF_MESSAGE.NF_HEAD_VALUE.value()));
		byte[] bLength = shortToBytes(ProtocoLength);
		buffer.put(bLength);
		buffer.put(bmtype);
		buffer.put(bstype);
		buffer.put(shortToBytes(ncmd));
		ndlength = (short) datas.length;
		buffer.put(shortToBytes(ndlength));
		buffer.put(intToLBytes(nkeyid));
		buffer.put(datas);

		ByteBuffer crcBuffer = buffer;

		byte[] verDatas = new byte[ProtocoLength - 2];

		System.arraycopy(crcBuffer.array(), 0, verDatas, 0, ProtocoLength - 2);

		ncrc = CRC_MODBUS16.getCRC(verDatas);

		buffer.put(shortToLBytes(ncrc));

		return buffer.array();

	}

	@Override
	public byte getMtype() {
		return bmtype;
	}

	@Override
	public void setMtype(byte mtype) {
		this.bmtype = mtype;
	}

	@Override
	public byte getStype() {
		return bstype;
	}

	public void setStype(byte stype) {
		this.bstype = stype;
	}

	@Override
	public int getKey() {
		return nkeyid;
	}

	@Override
	public void setKey(int key) {
		this.nkeyid = key;
	}

	@Override
	public short getCmd() {
		return ncmd;
	}

	@Override
	public void setCmd(short cmd) {
		this.ncmd = cmd;
	}

	@Override
	public byte[] getDatas() {
		return datas;
	}

	@Override
	public void setDatas(byte[] datas) {
		this.datas = datas;
	}

	@Override
	public int getLength() {
		return nlength;
	}

	@Override
	public int getDLength() {
		return ndlength;
	}

}
