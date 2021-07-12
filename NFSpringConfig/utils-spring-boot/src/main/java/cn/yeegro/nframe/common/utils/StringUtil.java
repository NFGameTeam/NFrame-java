
package cn.yeegro.nframe.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * StringUtils包装字符串处理类 
 */
@SuppressWarnings("all") 
public class StringUtil extends org.apache.commons.lang3.StringUtils{

    /**
     * //mybatis use
     */
    public static final String ITEM_PREFIX = "__frch_";

    private static final String pingYin = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
    private static Map<String, String> pinYinMap = new HashMap<String, String>();

    /**
     * // 后台系统 // 访问设置的合法ip
     */
    private static final String[] IPLegalList = { "127.0.0.1", "127.0.0.2" };


    static {
        String[] pinYins = pingYin.split(",");
        for (int i = 0; i < pinYins.length; i++) {
            pinYinMap.put(pinYins[i], pinYins[i]);
        }
    }

    /**
     * 字符串值 “1”
     */
    public final static String COMMON_VALUE_YES = "1";
    /**
     * 字符串值 “0”
     */
    public final static String COMMON_VALUE_NO = "0";
    /**
     * 字符串默认值 0
     */
    public final static String COMMON_VALUE_0 = "0";
    public final static String COMMON_VALUE_1 = "1";
    public final static String COMMON_VALUE_2 = "2";
    public final static String COMMON_VALUE_3 = "3";
    public final static String COMMON_VALUE_4 = "4";
    public final static String COMMON_VALUE_5 = "5";
    public final static String COMMON_VALUE_6 = "6";
    public final static String COMMON_VALUE_7 = "7";
    public final static String COMMON_VALUE_8 = "8";
    public final static String COMMON_VALUE_9 = "9";
    public final static String COMMON_VALUE_10 = "10";
    public final static String COMMON_VALUE_11 = "11";
    public final static String COMMON_VALUE_12 = "12";
    public final static String COMMON_VALUE_13 = "13";
    public final static String COMMON_VALUE_14 = "14";
    public final static String COMMON_VALUE_15 = "15";
    public final static String COMMON_VALUE_16 = "16";
    public final static String COMMON_VALUE_17 = "17";
    public final static String COMMON_VALUE_18 = "18";
    public final static String COMMON_VALUE_19 = "19";
    public final static String COMMON_VALUE_20 = "20";

    /**
     * 字符串常量 -1
     */
    public final static String COMMON_VALUE_M1 = "-1";
    /**
     * 字符串常量 -2
     */
    public final static String COMMON_VALUE_M2 = "-2";

    /**
     * 字符串常量 -3
     */
    public final static String COMMON_VALUE_M3 = "-3";

    /**
     * 空字符串 " "
     */
    public final static String BLANK = " ";

    /**
     * 字符串常量 默认id
     */
    public final static String COMMON_VALUE_DEFAULTID = "defaultId";

    /**
     * 字符串常量 默认系统用户id：10000
     */
    public final static String COMMON_VALUE_DEFAULT_USER = "10000";

    /**
     * 字符串常量
     */
    public final static String COMMON_VALUE_DEFAULT_DATA = "DEFAULT_DATA";

    /**
     * 字符串常量
     */
    public final static String COMMON_VALUE_VERSION_DEFAULT_DATA = "DEFAULT_DATA_FREE_VERSION";

    /**
     * 字符串常量
     */
    public final static String COMMON_VALUE_VERSION_DEFAULT_FREE = "DEFAULT_FREE_VERSION";

    /**
     * 服务类型
     */
    public final static String SYS_TYPE_SERVICE = "SYS_TYPE_SERVICE";

    /**
     * 流程设置动态业务id
     */
    public final static String SYS_FLOW_SETTING_DYNAMIC_BIZID = "SYS_FLOW_SETTING_DYNAMIC_BIZID";

    /**
     * 文本框
     */
    public final static String COMMON_VALUE_100 = "100";

    /**
     * 附件框
     */
    public final static String COMMON_VALUE_101 = "101";

    /**
     * 下拉框
     */
    public final static String COMMON_VALUE_102 = "102";

    /**
     * 单选框
     */
    public final static String COMMON_VALUE_103 = "103";

    /**
     * 时间限制 不超过
     */
    public final static String COMMON_VALUE_TIMELIMIT_0 = "不超过";

    /**
     * 时间限制 超过
     */
    public final static String COMMON_VALUE_TIMELIMIT_1 = "超过";

    /**
     * 时间单位(0:时 1:天 2:周 3:月)
     */
    public final static String COMMON_VALUE_DATE_0 = "时";

    /**
     * 时间单位(0:时 1:天 2:周 3:月)
     */
    public final static String COMMON_VALUE_DATE_1 = "天";
    /**
     * 时间单位(0:时 1:天 2:周 3:月)
     */
    public final static String COMMON_VALUE_DATE_2 = "周";
    /**
     * 时间单位(0:时 1:天 2:周 3:月)
     */
    public final static String COMMON_VALUE_DATE_3 = "月";

    /**
     * 当前时间
     */
    public final static String COMMON_VALUE_CURR_TIME = "#CURR_TIME";

    public final static String COMMON_VALUE_STATE_SUCCESS = "SUCCESS";

    public final static String COMMON_VALUE_STATE_FAIL = "FAIL";



    /**
     * 连字符号：“-”
     */
    public static final String HYPHEN = "-";

    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 图片类型
     */
    public static final String imgType = "bmp,jpg,png,jpeg,gif";


    /**
     * 取定长字符串,不足的在前面补指定字符
     * @param str 目标字符串
     * @param length 返回字符串长度
     * @param preChar 不足前补字符
     * @return
     */
    public static String getFixStr(String str, int length, String preChar) {
        if (str == null) {
            str = "";
        }
        int addLen = length - str.length();
        if (addLen > 0) {
            for (int i = 0; i < addLen; i++) {
                str = preChar + str;
            }
        }
        return str;
    }

    /**
     * Convert a String to int.
     *
     * @param intString
     *            A String contains an int value.
     * @return int The int value parsed from the string as parameter, 0 is
     *         returned if cannot parse an int value from the given string.
     */
    public static int toInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    /**
     * 判断图片类型是否正确
     *
     * @param fileEnd
     * @param fileType
     * @return
     */
    public static boolean checkImgFileType(String fileEnd, String fileType) {
        boolean isRealType = false;
        if (StringUtil.isEmpty(fileType)) {
            fileType = StringUtil.imgType;
        }
        if (fileType.indexOf(",") != -1) {
            String[] arrType = fileType.split(",");
            for (String str : arrType) {
                if (fileEnd.equals(str.toLowerCase())) {
                    isRealType = true;
                    break;
                }
            }
        } else {
            if (fileEnd.equals(fileType.toLowerCase())) {
                isRealType = true;
            }
        }
        return isRealType;
    }

    /**
     * 判断集合非空
     *
     * @param collection
     * @return
     * @author:kaiqiang.wu
     * @time:2015年9月9日下午4:34:45
     */
    public static boolean isNotEmpty( Collection collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断集合为空
     *
     * @param collection
     * @return
     * @author:kaiqiang.wu
     * @time:2015年9月9日下午4:35:01
     */
    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }

    /***
     *
     * @describe：判断一个集合中的项是否有空
     * @param str
     * @return
     * @author:kui.he
     * @time:2014年9月12日上午10:05:21
     */
    public static boolean isEmpty(String[] str) {
        boolean temp = true;
        for (String s : str) {
            temp = StringUtils.isEmpty(s);
            if (temp) {
                break;
            } else {
                continue;
            }
        }
        return temp;
    }


    /**
     *
     * @describe：获取指定Map指定Key的值,当没有的时候返回给定的默认值
     * @param map
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     * @author:kui.he
     * @time:2014年9月12日上午11:37:28
     */
    public static String getString(Map map, String key, String defaultValue) {
        if(map==null){
            return defaultValue;
        }
        if (map.containsKey(key)) {
            Object obj = map.get(key);
            if (obj == null) {
                return defaultValue;
            } else {
                return obj.toString();
            }
        }
        return defaultValue;
    }

    /**
     * 将一个对象转换为字符串，如果字符串为空直接设定的返回默认字符串
     *
     * @describe：TODO
     * @param obj
     * @param defaultValue
     * @return
     * @author:kui.he
     * @time:2014年9月13日下午12:31:19
     */
    public static String getString(Object obj, String defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (StringUtils.isEmpty(obj.toString())) {
            return defaultValue;
        } else {
            return obj.toString();
        }
    }



    /**
     * 取List中不为空的指定索引对象
     *
     * @param list
     * @param index
     * @return
     * @author:kaiqiang.wu
     * @time:2015年9月25日上午11:33:27
     */
    public static <T> T getInList(List list,int index){
    	
    	if(CollectionUtils.isNotEmpty(list) && list.size() > index ){
    		 return (T)list.get(index);
    		
    	}else{
            return null;
        }
    	
    }



    /**
     * 邮箱正则表达式true匹配
     *
     * @param yx
     * @return
     */
    public static boolean matchYx(String yx) {
        boolean temp = false;
        if (StringUtil.isNotEmpty(yx)) {
            temp = yx
                    .matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        }
        return temp;
    }

    

    /**
     *
     * @describe：比较两个字符串是否相同(为空返回false)
     * @param str
     *            第一个字符串
     * @param str2
     *            第二个字符串
     * @return 返回 true|false
     * @author:he.kui
     * @time:2014年5月29日下午7:31:54
     */
    public static boolean equals(String str, String str2) {
        if (StringUtils.isEmpty(str2) || StringUtils.isEmpty(str)) {
            return false;
        }
        return str.equals(str2);
    }



    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     * @param s
     * @return
     */
    public static int getStringlength(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }


    /**
     * 获取异常的String
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        }catch (Exception e){
            return "getStackTrace error:"+e.getMessage();
        }finally{
            pw.close();
        }
    }

    /**
     * 判断某值是否在列表中
     * @param inValues
     * @param value
     * @return
     */
    public static boolean isInStringValue(String[] inValues,String value){
        if(value == null){
            return false;
        }
        boolean isIn = false;
        for (String v : inValues) {
            if(value.equals(v)){
                isIn = true;
                break;
            }
        }
        return isIn;
    }


    /**
     * 根据字节类型截取字符串
     * Java语言中，采用ISO8859-1编码方式时，一个中文字符与一个英文字符一样只占1个字节；
     * 采用GB2312或GBK编码方式时，一个中文字符占2个字节；
     * 而采用UTF-8编码方式时，一个中文字符会占3个字节。
     * @param targetString
     * @param byteCode (ISO8859-1,GB2312或GBK,UTF-8)
     * @param byteIndex
     * @return
     * @throws Exception
     * @auth luyi
     */
    public static String getSubString(String targetString,String byteCode, int byteIndex)
            throws Exception {
        if (targetString.getBytes(byteCode).length < byteIndex) {
            return targetString;
        }
        String temp = targetString;
        for (int i = 0; i < targetString.length(); i++) {
            if (temp.getBytes(byteCode).length <= byteIndex) {
                break;
            }
            temp = temp.substring(0, temp.length() - 1);
        }
        return temp;
    }

    /**
     * 截取字符串
     * @param str 待截取的字符串
     * @param start 截取起始位置 （ 1 表示第一位 -1表示倒数第1位）
     * @param end 截取结束位置 （如上index）
     * @return
     */
    public static String sub(String str,int start,int end){
		String result = null;

        if(str == null || str.equals(""))
            return "";

        int len=str.length();
        start = start < 0 ? len+start : start-1;
        end= end < 0 ? len+end+1 :end;

        return str.substring(start, end);
    }

    /**
     * 随机产生一个4位数的数字密码
     *
     * @return
     */
    public static String generateRamdomNum() {
        String[] beforeShuffle = new String[] { "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9" };
        List<String> list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(3, 7);
        return result;
    }


    /**
     * 正则判断手机号
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }
     
    // 手机号码前三后四脱敏
    public static String mobileEncrypt(String mobile) {
        if (StringUtil.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    //身份证前三后四脱敏
    public static String idEncrypt(String id) {
        if (StringUtil.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    //护照前2后3位脱敏，护照一般为8或9位
    public static String idPassport(String id) {
        if (StringUtil.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }

}
