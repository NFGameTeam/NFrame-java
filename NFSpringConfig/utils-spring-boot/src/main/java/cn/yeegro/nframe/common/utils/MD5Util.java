package cn.yeegro.nframe.common.utils;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author 作者 someday 
 * md5加密解密工具
 */
public class MD5Util {
	/**
	 * md5 加密
	 * @param password明文
	 */
	public static String encode(String password) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder(16);
		String saltPassword = "" ;
		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
		int len = sb.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
				sb.append("0");
			}
		}
		String salt = sb.toString();
		saltPassword = md5Hex(password + salt);
		char[] md5salt = new char[48];
		for (int i = 0; i < 48; i += 3) {
			md5salt[i] = saltPassword.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			md5salt[i + 1] = c;
			md5salt[i + 2] = saltPassword.charAt(i / 3 * 2 + 1);
		}
		return new String(md5salt);

	}

	/**
	 * 校验密码是否正确
	 * 	/**
	 * @param password明文
	 * @param md5加密值
	 */
	public static boolean verify(String password, String md5) {
		try {
			char[] md5char = new char[32];
			char[] saltChar = new char[16];
			for (int i = 0; i < 48; i += 3) {
				md5char[i / 3 * 2] = md5.charAt(i);
				md5char[i / 3 * 2 + 1] = md5.charAt(i + 2);
				saltChar[i / 3] = md5.charAt(i + 1);
			}
			String salt = new String(saltChar);
			return  md5Hex(password + salt).equals(new String(md5char)) ;
		} catch (Exception e) {
			return false ;
		}
		
	}

	/**
	 * 获取十六进制字符串形式的MD5摘要
	 */
	public static String md5Hex(String src) {
		return DigestUtils.md5Hex(src);
	}

//	// 测试
//	public static void main(String[] args) throws Exception {
//		
//		
//		for (int i = 0; i <= 10000; i++) {
//			new Thread(() -> {
//				String str = "admin";
//				
//				if (MD5Util.verify("admin", "940f2795750e775461f1d887a79485e3ac32d7a814d1440a")) {
//					System.out.println("11111");
//				}else{
//					System.out.println("00000");
//				}
//
//			}).start();
//		}
//	}
}
