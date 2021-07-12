package cn.yeegro.nframe.common.utils;

import java.security.SecureRandom;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * 三重数据加密算法加密Api工具类
 * 互联网抓包可以清楚的知道这个请求获取到的数据，利用三重数据加密算法可以提升数据安全
 * 用3DES对称加密，加密解密公用一个密钥 我们根据请求参数的appId从数据库获取对应的密钥来解密
 * { "data": "3DES加密数据","sign": "签名", "appId": "发起方标识", "timestamp": "时间戳-毫秒" }
 */
@Slf4j
public class ApiEncryptUtil {
	public static final String KEYCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String APPID = "appId";
	private static final String TIMESTAMP = "timestamp";
	private static final String DATA = "data";
	private static final long TSDIFF = 12000; // 时间戳12秒误差

	private static final String CODE ="code" ;
	private static final String MESSAGE ="msg" ;
	
	/**
	 * 生成签名
	 * @param appId		账号
	 * @param signKey	密钥
	 * @param timestam	时间戳
	 * @param data		密文数据
	 * @return
	 */
	public static String generateSign(String appId, String timestamp, String data, String signKey) throws Exception {
		if (StringUtils.isEmpty(appId)) {
			throw new Exception("参数[appId]为空");
		}
		if (StringUtils.isEmpty(timestamp)) {
			throw new Exception("参数[timestamp]为空");
		}
		if (StringUtils.isEmpty(data)) {
			throw new Exception("参数[data]为空");
		}

		// 参数转成map
		Map<String, Object> params = new HashMap<>();
		params.put(APPID, appId);
		params.put(TIMESTAMP, timestamp);
		params.put(DATA, data);
		params.put("signKey", signKey);
		// 自然排序后拼接参数值
		String signString = map2SignString(params);
		// 生成签名
		return DigestUtils.md5Hex(signString);
	}

	/**
	 * 校验签名
	 * @param appId	账号
	 * @param signKey	密钥
	 * @param timestamp	时间戳
	 * @param data		密文数据
	 * @param sign		签名
	 * @return true/false
	 */
	public static Map<String, String> checkSign(String appId, String timestamp, String data, String sign,String signKey) {
		Map<String, String> resp = new HashMap<>();
		resp.put(CODE, "0000");
		resp.put(MESSAGE, "");
		try {
			if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(data)
					|| StringUtils.isEmpty(sign)) {
				throw new Exception("请求参数不全");
			}
			if (!checkTimestamp(timestamp)) {
				throw new Exception("时间戳无效");
			}
			String newSign = generateSign(appId, timestamp, data, signKey);
			if (!sign.equals(newSign)) {
				throw new Exception("签名无效");
			}

			// todo timestamp+nonce 防重放，在TS_DIFF有效期内如果速度够快实际还是可以重放的，除非TS_DIFF足够短
			// 这里我们可以直接用appId+timestamp+hash（data）做唯一标识，
			// 在签名和TS_DIFF都检验通过之后，增加判断redis里是否有这个唯一标识的key，有就说明是重放的数据,校验失败
			// 否则校验通过，并把这个唯一标识放到redis里,有效期=TS_DIFF
			// 这样即使在TS_DIFF有效期内，短时间内再收到相同报文的请求也不会重复处理了

			// 返回解密的数据
			resp.put(DATA, decryptBy3Des(data, getSignKey(appId)));

		} catch (Exception e) {
			resp.put(CODE, "1000");
			resp.put(MESSAGE, e.getMessage());
		}
		return resp;
	}

	private static String map2SignString(Map map) {
		
		if(MapUtils.isEmpty(map)){
			return null ;
		}
		
		Map<String, Object> paramMap = sortByKey(map);
		StringBuilder content = new StringBuilder();
		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key).toString();
			if (StringUtils.isNotBlank(value)) {
				content.append(value);
			}
		}
		return content.toString();
	}

	private static Map<String, Object> sortByKey(Map map) {
		if(MapUtils.isEmpty(map)){
			return null ;
		}
		Map<String, Object> sortMap = new TreeMap<>(Comparator.naturalOrder());
		sortMap.putAll(map);
		return sortMap;
	}

	/**
	 * 3重DES加密
	 * @param data
	 * @param desKey
	 * 密钥长度不少于24的倍数位
	 * @return
	 */
	private static String encryptBy3Des(String data, String desKey) {
		String result = null;
		try {
			SecureRandom secureRandom = new SecureRandom();
			DESedeKeySpec sedeKeySpec = new DESedeKeySpec(desKey.getBytes());
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = secretKeyFactory.generateSecret(sedeKeySpec);
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, secureRandom);

			byte[] bytesresult = cipher.doFinal(data.getBytes());
			Base64.Encoder encoder = Base64.getEncoder();
			result = encoder.encodeToString(bytesresult);
		} catch (Exception e) {
			log.error("EncryptBy3DES加密失败，data = " + data, e);
		}
		return result;
	}

	/**
	 * 3重DES解密
	 * @param data
	 * @param desKey
	 * @return
	 */
	private static String decryptBy3Des(String data, String desKey) {
		String desResult = null;
		try {
			SecureRandom secureRandom = new SecureRandom();
			DESedeKeySpec sedeKeySpec = new DESedeKeySpec(desKey.getBytes());
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = secretKeyFactory.generateSecret(sedeKeySpec);
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, secureRandom);
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] bytesResult = cipher.doFinal(decoder.decode(data));
			desResult = new String(bytesResult);
		} catch (Exception e) {
			log.error("decryptBy3DES解密失败，data = " + data, e);
		}
		return desResult;
	}

	/**
	 * 判断时间戳是否有效 超过阈值TS_DIFF则请求无效
	 * @param timestamp
	 * @return true/false
	 */
	private static boolean checkTimestamp(String timestamp) {
		long curTime = System.currentTimeMillis();
		long dt = Math.abs(curTime - Long.parseLong(timestamp));
		return dt <= TSDIFF;
	}

	/**
	 * todo 获取appId对应的密钥，数据库可以配置appId对应的密钥，有效期，时间戳允许的误差等等，放redis缓存 3des密钥长度
	 * 3*8=24
	 * @param appId
	 * @return 密钥
	 */
	private static String getSignKey(String appId) {
		  return StringUtils.leftPad(appId, 24,"0");
	}

	public static void main(String[] args) {
		/*
		 * appId: 应用ID timestamp: 发送请求的时间，时间戳-毫秒 System.currentTimeMillis()
		 * sign: 请求参数签名 data: 业务请求参数 jsonStr
		 */
		try {
			String appId = "webApp";
			String signKey = getSignKey(appId);
			String timestamp = System.currentTimeMillis() + "";
			String sign = "";

			// 业务请求参数
			Map<String, Object> data = new HashMap<>();
			data.put("grant_type", "password");
			data.put("username", "admin");
			data.put("password", "admin");
			data.put("scope", "app");
			
			// 业务请求参数使用3des加密
			String dataStr = encryptBy3Des(JSON.toJSONString(data), signKey);
			// 生成请求参数的签名
			sign = generateSign(appId, timestamp, dataStr, signKey);
			// 请求报文
			Map<String, String> reqParams = new HashMap<>();
			reqParams.put(APPID, appId);
			reqParams.put(TIMESTAMP, timestamp);
			reqParams.put(DATA, dataStr);
			reqParams.put("sign", sign);

			// 校验请求报文
			Map desMap = checkSign(reqParams.get(APPID), reqParams.get(TIMESTAMP), reqParams.get(DATA),
					reqParams.get("sign"), signKey);

			String desData = MapUtils.getString(desMap, "data");

			log.info("请求报文：{}\n解密报文：{}\n解密参数:{}", JSON.toJSONString(reqParams), JSON.toJSONString(desMap),
					MapUtils.getString(desMap, "data"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
