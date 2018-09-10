/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.api.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MemoryUtil {
	public static void memcpy(byte[] dst, int dst_pos, short src) {
		byte[] val = Converter.toLH(src);
		memcpy(dst, dst_pos, val, 0, val.length);
	}

	public static void memcpy(byte[] dst, int dst_pos, int src) {
		byte[] val = Converter.toLH(src);
		memcpy(dst, dst_pos, val, 0, val.length);
	}

	public static void memcpy(byte[] dst, int dst_pos, byte src) {
		byte[] val = new byte[] { src };
		memcpy(dst, dst_pos, val, 0, val.length);
	}

	public static void memcpy(byte[] dst, int dst_pos, byte[] src) {
		memcpy(dst, dst_pos, src, 0, src.length);
	}

	public static void memcpy(byte[] dst, int dst_pos, double src) {
		byte[] val = Converter.toLH(src);
		memcpy(dst, dst_pos, val, 0, val.length);
	}

	public static void memcpy(byte[] dst, int dst_pos, String src) {
		if (null == src) {
			return;
		}
		byte[] val = src.getBytes(Charset.forName("utf-8"));
		memcpy(dst, dst_pos, val, 0, val.length);
	}

	public static void memcpy(byte[] dst, int dst_pos, byte[] src, int src_pos,
			int length) {
		if (dst == null || dst_pos < 0 || src == null || src_pos < 0
				|| length <= 0) {
			return;
		}

		if (src.length < src_pos + length) {
			return;
		}

		if (dst.length < dst_pos + length) {
			return;
		}

		int i = 0;
		while (length-- > 0) {
			dst[i + dst_pos] = src[src_pos + i];
			i++;
		}
	}

	public static void memset(byte[] dst, int length) {
		if (null == dst || length < 0) {
			return;
		}

		int i = 0;
		while (length-- > 0) {
			dst[i++] = 0;
		}
	}
	
	public static void memset(byte[] dst, int pos, int length) {
		if (null == dst || length < 0) {
			return;
		}

		int i = pos;
		while (length-- > 0) {
			dst[i++] = 0;
		}
	}
	
	public static void memmove(byte []dst, int dst_pos, byte []src, int src_pos, int size){
	
		if(null == dst || null == src){
			return;
		}
		
		byte []tmp = new byte[size];
		memcpy(tmp, 0, src, src_pos, size);
		memcpy(dst, dst_pos, tmp, 0, size);	
	}

	public static boolean memcmp(byte[] src, int offset_src, byte[] dst,
			int offset_dst, int len) {
		try {
			int index = 0;
			while (index < len) {
				if (src[offset_src + index] != dst[offset_dst + index]) {
					return false;
				}

				index++;
			}
			if (index == len) {
				return true;
			}
		} catch (Exception e) {

		}

		return false;
	}

	public static int memsearch(byte[] src, int offset, byte target) {
		if (null == src || offset < 0) {
			return -1;
		}

		try {
			int length = src.length;
			while (offset < length) {
				if (src[offset] == target) {
					return offset;
				}
			}
		} catch (Exception e) {

		}

		return -1;
	}

	public static boolean isZero(byte[] data) {
		if (null == data) {
			return true;
		}

		if (data.length == 0) {
			return true;
		}

		for (int i = 0; i < data.length; i++) {
			if (data[i] != 0) {
				return false;
			}
		}

		return true;
	}

	public static void zero(byte[] data) {
		if (null == data) {
			return;
		}

		Arrays.fill(data, (byte) 0x00);
	}

	/*
	 * byte[]
	 */
	public static int getInt(byte[] buf, int offset, int length) {
		try {
			byte []ibuf = new byte[4];
			memcpy(ibuf, 0, buf, offset, 4);
			
			return Converter.lBytesToInt(ibuf);
		} catch (Exception e) {

		}

		return 0;
	}

	/*
	 * byte[]
	 */
	public static long getLong(byte[] buf, int offset, int length) {
		try {
			ByteBuffer bb = ByteBuffer.wrap(buf, offset, length);
			bb.order(ByteOrder.LITTLE_ENDIAN);

			return bb.getLong();
		} catch (Exception e) {

		}

		return 0L;
	}

	/*
	 * byte[]
	 */
	public static short getShort(byte[] buf, int offset, int length) {
		try {
			ByteBuffer bb = ByteBuffer.wrap(buf, offset, length);
			bb.order(ByteOrder.LITTLE_ENDIAN);

			return bb.getShort();
		} catch (Exception e) {

		}

		return 0;
	}

	/*
	 * byte[]
	 */
	public static byte getByte(byte[] buf, int offset, int length) {
		try {
			ByteBuffer bb = ByteBuffer.wrap(buf, offset, length);
			bb.order(ByteOrder.LITTLE_ENDIAN);

			return bb.get();
		} catch (Exception e) {
		}

		return 0x00;
	}

	/*
	 * byte[]
	 */
	public static double getDouble(byte[] buf, int offset, int length) {
		try {
			ByteBuffer bb = ByteBuffer.wrap(buf, offset, length);
			bb.order(ByteOrder.LITTLE_ENDIAN);

			return bb.getDouble();
		} catch (Exception e) {
		}

		return 0.0;
	}

	public static String getString(byte[] buf, int offset, int length) {
		try {
			String s = new String(buf, offset, length, Charset.forName("utf-8"));
			return s;
		} catch (Exception e) {

		}
		return "";
	}
}
