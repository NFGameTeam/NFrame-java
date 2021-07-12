package cn.yeegro.nframe.comm.code.util;

import java.math.BigInteger;


public class CRC_MODBUS16 {

	/**
     * 计算CRC16校验码
     *
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static short getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        short re=(short)CRC;
        byte[] sbytes=shortToLBytes(re);
        return re;
    }
    
    
	
	private short bytesToLShort(byte[] src, int offset) {
		short value;
		value = (short) ((src[offset] & 0xFF) | ((src[offset+1] & 0xFF) << 8));
		return value;
	}


	public static byte[] shortToLBytes(short value) {
		byte[] src = new byte[2];
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}
    /**
     * 将16进制单精度浮点型转换为10进制浮点型
     *
     * @return float
     * @since 1.0
     */
    private float parseHex2Float(String hexStr) {
        BigInteger bigInteger = new BigInteger(hexStr, 16);
        return Float.intBitsToFloat(bigInteger.intValue());
    }

    /**
     * 将十进制浮点型转换为十六进制浮点型
     *
     * @return String
     * @since 1.0
     */
    private String parseFloat2Hex(float data) {
        return Integer.toHexString(Float.floatToIntBits(data));
    }
	
	
	public static void main(String[] args) {
		
		String ss="1234567890qwertyuiopasdfghjklzxcvbnm";
		
		short aaString= CRC_MODBUS16.getCRC(ss.getBytes());
		System.out.println(aaString);
	}
	
//	
//	void InvertUint8(unsigned char *dBuf, unsigned char *srcBuf)
//	{
//		int i;
//		unsigned char tmp[4];
//		tmp[0] = 0;
//		for (i = 0; i<8; i++) {
//			if (srcBuf[0] & (1 << i))
//				tmp[0] |= 1 << (7 - i);
//		}
//		dBuf[0] = tmp[0];
//	}
//
//	void InvertUint16(unsigned short *dBuf, unsigned short *srcBuf)
//	{
//		int i;
//		unsigned short tmp[4];
//		tmp[0] = 0;
//		for (i = 0; i<16; i++) {
//			if (srcBuf[0] & (1 << i))
//				tmp[0] |= 1 << (15 - i);
//		}
//		dBuf[0] = tmp[0];
//	}
//
//	unsigned short CRC16MODBUS(unsigned char *puchMsg, unsigned int usDataLen)
//	{
//		unsigned short wCRCin = 0xFFFF;
//		unsigned short wCPoly = 0x8005;
//		unsigned char wChar = 0;
//		int i = 0;
//
//		while (usDataLen--) {
//			wChar = *(puchMsg++);
//			InvertUint8(&wChar, &wChar);
//			wCRCin ^= (wChar << 8);
//			for (i = 0; i<8; i++) {
//				if (wCRCin & 0x8000)
//					wCRCin = (wCRCin << 1) ^ wCPoly;
//				else
//					wCRCin = wCRCin << 1;
//			}
//		}
//		InvertUint16(&wCRCin, &wCRCin);
//		return (wCRCin);
//	}
	
	
	
	

//	/**
//	 * 将16进制单精度浮点型转换为10进制浮点型
//	 *
//	 * @return float
//	 * @since 1.0
//	 */
//	private float parseHex2Float(String hexStr) {
//		BigInteger bigInteger = new BigInteger(hexStr, 16);
//		return Float.intBitsToFloat(bigInteger.intValue());
//	}
//
//	/**
//	 * 将十进制浮点型转换为十六进制浮点型
//	 *
//	 * @return String
//	 * @since 1.0
//	 */
//	private String parseFloat2Hex(float data) {
//		return Integer.toHexString(Float.floatToIntBits(data));
//	}
}
