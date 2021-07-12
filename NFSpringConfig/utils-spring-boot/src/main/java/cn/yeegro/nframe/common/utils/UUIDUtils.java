package cn.yeegro.nframe.common.utils;

import java.util.UUID;

/**
 * Created by zhangzhiguang
 */
public class UUIDUtils {
    /**
     * 生产GUID 36位
     * @return
     */
    public static String getGUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * 产生32位GUID
     * @return
     */
    public static String getGUID32() {
        return UUID.randomUUID().toString().toUpperCase().replace("-","");
    }

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    /**
     * @Param digit 生成的位数
     */
    public static String generateShortUuid(int digit ) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i <digit; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

}
