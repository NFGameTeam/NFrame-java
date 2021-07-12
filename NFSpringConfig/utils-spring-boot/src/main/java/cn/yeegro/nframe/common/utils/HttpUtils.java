package cn.yeegro.nframe.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 封装http get post
 */
@Slf4j
@SuppressWarnings("all")
public class HttpUtils {

	/**
	 * get方法
	 * 
	 * @param url
	 * @return
	 */
	public static Map<String, Object> doGet(String url) {

		Map<String, Object> map = new HashMap<>();
		CloseableHttpClient httpClient = HttpClients.createDefault();

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) // 连接超时
				.setConnectionRequestTimeout(5000)// 请求超时
				.setSocketTimeout(5000).setRedirectsEnabled(true) // 允许自动重定向
				.build();

		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);

		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				String jsonResult = EntityUtils.toString(httpResponse.getEntity());
				map = GsonBuilder.build().fromJson(jsonResult, map.getClass());
			}

		} catch (Exception e) {
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			log.error("doGet-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
			}
		}
		return map;
	}

	/**
	 * 封装post
	 * 
	 * @return
	 */
	public static String doPost(String url, String data, int timeout) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 超时设置

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout) // 连接超时
				.setConnectionRequestTimeout(timeout)// 请求超时
				.setSocketTimeout(timeout).setRedirectsEnabled(true) // 允许自动重定向
				.build();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Content-Type", "text/html; chartset=UTF-8");

		if (data != null && data instanceof String) { // 使用字符串传参
			StringEntity stringEntity = new StringEntity(data, "UTF-8");
			httpPost.setEntity(stringEntity);
		}

		try {

			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpEntity);
				return result;
			}

		} catch (Exception e) {
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			log.error("doPost-" + stackTraceElement.getMethodName() + "--" + stackTraceElement.getLineNumber());

		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;

	}

}
