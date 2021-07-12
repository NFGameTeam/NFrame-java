package cn.yeegro.nframe.plugin.oauthcenter.logic.feign;

import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.TraceConstant;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.UaaConstant;
import cn.yeegro.nframe.plugin.oauthcenter.logic.util.TokenUtil;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.yeegro.nframe.common.utils.StringUtil;

import feign.RequestInterceptor;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2017年11月12日 上午22:57:51
 * feign拦截器


 */
@Configuration
public class FeignInterceptorConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        RequestInterceptor requestInterceptor = template -> {
            //传递token
            //使用feign client访问别的微服务时，将accessToken header
            //config.anyRequest().permitAll() 非强制校验token
            if (StringUtil.isNotBlank(TokenUtil.getToken())) {
            	template.header(UaaConstant.TOKEN_HEADER, TokenUtil.getToken());
//            	template.header(UaaConstant.AUTHORIZATION,  OAuth2AccessToken.BEARER_TYPE  +  " "  +  TokenUtil.getToken() );
            }
            //传递traceId
            String traceId = StringUtil.isNotBlank(MDC.get(TraceConstant.LOG_TRACE_ID)) ? MDC.get(TraceConstant.LOG_TRACE_ID) : MDC.get(TraceConstant.LOG_B3_TRACEID);
            if (StringUtil.isNotBlank(traceId)) {
                template.header(TraceConstant.HTTP_HEADER_TRACE_ID, traceId);
            }


        };

        return requestInterceptor;
    }
}
