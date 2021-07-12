package cn.yeegro.nframe.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@SuppressWarnings("all") 
public class RequestUtil {
	 

	private HttpHeaders headers;
	private HttpEntity<String> requestEntity;
	private ObjectMapper objectMapper;
	@Autowired
	private RestTemplate restTemplate;
	
 
	
	public RequestUtil(){
		if(objectMapper ==null){
			objectMapper = new ObjectMapper();
		}
		if(headers ==null){
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		}
		if(restTemplate == null){
			restTemplate = new RestTemplate();
		}
		
	}
	
	/**
	 * 发送请求
	 * @param jcString
	 * @param url
	 * @param requstMap
	 * @return
	 */
	public Map doPostMapStr(String url,Map<String,String> requstMap){
		String json = "";
		try {
			json = objectMapper.writeValueAsString(requstMap);
		} catch (JsonProcessingException e) {
			log.error("doPost2|Exception:" +e.getMessage());
			e.printStackTrace();
		}
		
		log.trace(json);
		requestEntity = new HttpEntity<String>(json, headers);
		String resultStr= restTemplate.postForObject(url, requestEntity, String.class);
		log.trace(resultStr);
		Map retrunMap = JSON.parseObject(resultStr,HashMap.class);
		return retrunMap;
	}
	
	

	/**
	 * 发送请求
	 * @param jcString
	 * @param url
	 * @param requstMap
	 * @return
	 */
	public String doPostMapStr2(String url,Map<String,String> requstMap){
		String json = "";
		try {
			json = objectMapper.writeValueAsString(requstMap);
		} catch (JsonProcessingException e) {
			log.error("doPost2|Exception:" +e.getMessage());
			e.printStackTrace();
		}
		
		log.trace(json);
		requestEntity = new HttpEntity<String>(json, headers);
		String resultStr= restTemplate.postForObject(url, requestEntity, String.class);
		
		return resultStr;
	}
	
	/**
	 * 发送请求
	 * @param jcString
	 * @param url
	 * @param requstMap
	 * @return
	 */
	public Map doPostMap(String url,Map<String,Object> requstMap){
		String json = "";
		try {
			json = objectMapper.writeValueAsString(requstMap);
		} catch (JsonProcessingException e) {
			log.error("doPost2|Exception:" +e.getMessage());
			e.printStackTrace();
		}
		log.trace("请求参数为："+json);
		requestEntity = new HttpEntity<String>(json, headers);
		String resultStr= restTemplate.postForObject(url, requestEntity, String.class);
		Map retrunMap = JSON.parseObject(resultStr,HashMap.class);
		return retrunMap;
	}
	/**
	 * 发送请求 post
	 * @param jcString
	 * @param url
	 * @param requstMap
	 * @return
	 */
	public Map<String,String> doPostJson(String url,String json){
		requestEntity = new HttpEntity<String>(json, headers);
		String resultStr= restTemplate.postForObject(url, requestEntity, String.class);
		Map retrunMap = JSON.parseObject(resultStr,HashMap.class);
		return retrunMap;
	}
	/**
	 * 发送请求get
	 * @param jcString
	 * @param url
	 * @param requstMap
	 * @return
	 */
	public Map<String,String> doGetRequest(String url){
		String resultStr= restTemplate.getForObject(url, String.class);
		Map retrunMap = JSON.parseObject(resultStr,HashMap.class);
		return retrunMap;
	}
	
	 
}

