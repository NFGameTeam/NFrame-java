package cn.yeegro.nframe.sentinel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import cn.yeegro.nframe.common.interceptor.RestTemplateInterceptor;

import cn.hutool.json.JSONUtil;

/**
 * Sentinel配置类
 */
public class SentinelAutoConfig {
	
	@PostConstruct
    public void init() {
        WebCallbackManager.setUrlBlockHandler(new DefaultUrlBlockHandler());
        //若希望对 HTTP ,黑白名单，请求按照来源限流，则可以自己实现  RequestOriginParser  接口从 HTTP 请求中解析 origin 并注册至 
//        WebCallbackManager.setRequestOriginParser(new RequestOriginParser() {
//            @Override
//            public String parseOrigin(HttpServletRequest request) {
//                return request.getRemoteAddr();
//            }
//        });
    }

    /**
     * 新增流控规则，自定义url处理异常
     * 限流、熔断统一处理类
     */
    public class DefaultUrlBlockHandler implements UrlBlockHandler {
        @Override
        public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
        	
        	
        	httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        	httpServletResponse.getWriter().print(JSONUtil.toJsonStr(("{\"code\":403,\"message\":\"sentinel限流了\"}".getBytes(StandardCharsets.UTF_8))));
        }
    }
    
    @Bean
    @Primary
	@LoadBalanced
	@SentinelRestTemplate
	public RestTemplate sentinelRestTemplate() {
//		 长连接
		PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager();
//		 总连接数
		pollingConnectionManager.setMaxTotal(1000);
//		同路由的并发数
		pollingConnectionManager.setDefaultMaxPerRoute(1000);

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setConnectionManager(pollingConnectionManager);
//		  重试次数，默认是3次，没有开启
//		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
		HttpClient httpClient = httpClientBuilder.build();

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);
//		 连接超时
		clientHttpRequestFactory.setConnectTimeout(12000);
//		 数据读取超时时间，即SocketTimeout
		clientHttpRequestFactory.setReadTimeout(12000);
//		连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
		clientHttpRequestFactory.setConnectionRequestTimeout(200);
//		缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
//		clientHttpRequestFactory.setBufferRequestBody(false);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		//传递token traceid
		restTemplate.setInterceptors(
		            Collections.singletonList(
		                new RestTemplateInterceptor()
		            )
		        );
		return restTemplate;
	}
    
}
