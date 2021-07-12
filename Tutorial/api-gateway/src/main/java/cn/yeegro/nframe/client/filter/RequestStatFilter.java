package cn.yeegro.nframe.client.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.TraceConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * zuul 前置过虑器
 * 设置traceid ，由于传递traceid跟踪
 *
 * @author gitgeek
 */
@Slf4j
@Component
public class RequestStatFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private static final String FILTER_TYPE = "pre";

    /*** filter类型，前置过虑器，后置过虑器和路由过虑器 */
    @Override
    public String filterType() {
        return FILTER_TYPE;
    }

    /*** 返回一个整数值，表示filter执行顺序 */
    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    /*** 返回一个boolean，表示该过虑器是否执行 */
    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    /*** 每次filter执行的代码 */
    @Override
    public Object run() {

        String traceId = MDC.get(TraceConstant.LOG_B3_TRACEID);
        MDC.put(TraceConstant.LOG_TRACE_ID, traceId);
        RequestContext requestContext = RequestContext.getCurrentContext();
        String url = requestContext.getRequest().getRequestURL().toString();
        requestContext.addZuulRequestHeader(TraceConstant.HTTP_HEADER_TRACE_ID, traceId);
        log.info("request url = " + url + ", traceId = " + traceId);
//		RibbonFilterContextHolder.getContext().add("hello", "hello");
        return null;
    }
}
