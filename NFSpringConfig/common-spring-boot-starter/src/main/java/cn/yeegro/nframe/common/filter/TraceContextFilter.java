package cn.yeegro.nframe.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.TraceConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 参考brave.servlet.TracingFilter
 * 优先级
 * TracingFilter > TraceContextFilter
 * 此处理方式可以不用aop强行设置traceid，摆脱日志traceid强制使用log注解


 */
@Order(Ordered.HIGHEST_PRECEDENCE + 8)
@ConditionalOnClass(WebMvcConfigurer.class)
public class TraceContextFilter  extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    	//请求头传入存在以请求头传入的为准，不然以X-B3-TraceId为
    	String appTraceId = StringUtils.defaultString( request.getHeader(TraceConstant.HTTP_HEADER_TRACE_ID), MDC.get(TraceConstant.LOG_B3_TRACEID));
		//未经过HandlerInterceptor的设置，但是有请求头，重新设置
		if (StringUtils.isNotEmpty(appTraceId)) {
			MDC.put(TraceConstant.LOG_TRACE_ID, appTraceId);
		}
		filterChain.doFilter(request, response);
    }
}