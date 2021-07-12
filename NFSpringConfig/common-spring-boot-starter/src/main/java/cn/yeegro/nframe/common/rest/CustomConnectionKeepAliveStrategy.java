package cn.yeegro.nframe.common.rest;

import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import cn.yeegro.nframe.common.utils.StringUtil;

import cn.hutool.core.convert.Convert;


/**
 *	连接复用
 */
public class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
	private final long DEFAULT_SECONDS = 30;
	private final String DEFAULT_HEAD = "timeout";

	/**
	 * 最大keep alive的时间（秒钟）
	 * 这里默认为30秒，可以根据实际情况设置。可以观察客户端机器状态为TIME_WAIT的TCP连接数，如果太多，可以增大此值。
	 */
	@Override
	public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		return Arrays.asList(response.getHeaders(HTTP.CONN_KEEP_ALIVE)).stream()
				.filter(h -> StringUtil.equalsIgnoreCase(h.getName(), DEFAULT_HEAD)
						&& StringUtil.isNumeric(h.getValue()))
				.findFirst().map(h -> Convert.toLong(h.getValue(), DEFAULT_SECONDS)).orElse(DEFAULT_SECONDS) * 1000;
	}
}
