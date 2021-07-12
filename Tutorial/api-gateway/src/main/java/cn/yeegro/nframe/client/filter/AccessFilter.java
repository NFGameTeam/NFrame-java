package cn.yeegro.nframe.client.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.UaaConstant;
import cn.yeegro.nframe.plugin.oauthcenter.logic.util.TokenUtil;

/**
 * Created by owen on 2017/9/10.
 */
@Component
@SuppressWarnings("all")
public class AccessFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();
            // 一定要get一下,下面这行代码才能取到值... [注1]
            request.getParameterMap();

            Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();
            Map<String, String> requestHeaders = ctx.getZuulRequestHeaders();


            if (requestQueryParams == null) {
                requestQueryParams = new HashMap<>();
            }
            ctx.setRequestQueryParams(requestQueryParams);

            if (StringUtils.isNotBlank(TokenUtil.getToken())) {

                ctx.addZuulRequestHeader(UaaConstant.TOKEN_HEADER, TokenUtil.getToken());
            }

        } catch (Exception e) {

        }
        return null;
    }


}
