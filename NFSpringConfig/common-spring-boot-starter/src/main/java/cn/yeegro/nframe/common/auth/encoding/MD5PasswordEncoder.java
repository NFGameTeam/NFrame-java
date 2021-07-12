package cn.yeegro.nframe.common.auth.encoding;

import cn.yeegro.nframe.common.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 作者 someday 
 * md5盐值比对
 */

public class MD5PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return MD5Util.encode(String.valueOf(rawPassword));
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return MD5Util.verify(String.valueOf(rawPassword), encodedPassword) ;
	}

//	public static void main(String[] args) {
//		final MD5PasswordEncoder encode = new MD5PasswordEncoder();
//		 
//		for (int i = 0; i <= 10000; i++) {
//			new Thread(() -> {
//				String str = "admin";
//				System.out.println(encode.encode("admin"));
//				if (encode.matches("admin", "d8680305082a28f12f849e6f249e45d5126da36f68a72262")) {
//					System.out.println("11111");
//				}else{
//					System.out.println("00000");
//				}
//
//			}).start();
//		}
//		
//	}
}
