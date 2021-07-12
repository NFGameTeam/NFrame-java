package cn.yeegro.nframe.client.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIClientModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import cn.yeegro.nframe.client.utils.RedisLimiterUtils;
import cn.yeegro.nframe.common.web.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by owen on 2017/9/10. 根据应用 url 限流 oauth_client_details if_limit 限流开关
 * limit_count 阈值
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class RateLimitFilter extends ZuulFilter {


    private NFIPluginManager pPluginManager;

    private ThreadLocal<Result> error_info = new ThreadLocal<Result>();
    @Autowired
    private RedisLimiterUtils redisLimiterUtils;
    @Autowired
    private ObjectMapper objectMapper;

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
            if (!checkLimit(request)) {

                log.error("too many requests!");
                error_info.set(Result.failedWith(null, 429, "too many requests!"));

                serverResponse(ctx, 429);
                return null;

            }

        } catch (Exception e) {
            log.error("RateLimitFilter->run:{}", e.getMessage());
        }
        return null;
    }

    /***
     * 统一禁用输出
     *
     * @param ctx
     * @param ret_message
     *            输出消息
     * @param http_code
     *            返回码
     */
    public void serverResponse(RequestContext ctx, int http_code) {


        try {
            ctx.setSendZuulResponse(false);
            outputChineseByOutputStream(ctx.getResponse(), error_info);
            ctx.setResponseStatusCode(http_code);
        } catch (IOException e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            log.error("serverResponse:" + "---|Exception:" + stackTraceElement.getLineNumber() + "----" + e.getMessage());

        }

    }

    /**
     * 使用OutputStream流输出中文
     *
     * @param request
     * @param response
     * @throws IOException
     */

    public void outputChineseByOutputStream(HttpServletResponse response, ThreadLocal<Result> data) throws IOException {
        /**
         * 使用OutputStream输出中文注意问题： 在服务器端，数据是以哪个码表输出的，那么就要控制客户端浏览器以相应的码表打开，
         * 比如：outputStream.write("中国".getBytes("UTF-8"));//使用OutputStream流向客户端浏览器输出中文，以UTF-8的编码进行输出
         * 此时就要控制客户端浏览器以UTF-8的编码打开，否则显示的时候就会出现中文乱码，那么在服务器端如何控制客户端浏览器以以UTF-8的编码显示数据呢？
         * 可以通过设置响应头控制浏览器的行为，例如： response.setHeader("content-type",
         * "text/html;charset=UTF-8");//通过设置响应头控制浏览器以UTF-8的编码显示数据
         */

        OutputStream outputStream = response.getOutputStream();// 获取OutputStream输出流
        response.setHeader("content-type", "application/json;charset=UTF-8");// 通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
        /**
         * data.getBytes()是一个将字符转换成字节数组的过程，这个过程中一定会去查码表，
         * 如果是中文的操作系统环境，默认就是查找查GB2312的码表， 将字符转换成字节数组的过程就是将中文字符转换成GB2312的码表上对应的数字
         * 比如： "中"在GB2312的码表上对应的数字是98 "国"在GB2312的码表上对应的数字是99
         */
        /**
         * getBytes()方法如果不带参数，那么就会根据操作系统的语言环境来选择转换码表，如果是中文操作系统，那么就使用GB2312的码表
         */

        String msg = objectMapper.writeValueAsString(data.get());

        byte[] dataByteArr = msg.getBytes("UTF-8");// 将字符转换成字节数组，指定以UTF-8编码进行转换
        outputStream.write(dataByteArr);// 使用OutputStream流向客户端输出字节数组
    }

    public boolean checkLimit(HttpServletRequest request) {

        pPluginManager = NFPluginManager.GetSingletonPtr();
        NFIClientModule m_pClientServiceModule = pPluginManager.FindModule(NFIClientModule.class);
        // 解决zuul token传递问题
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if (user != null) {

            if (user instanceof OAuth2Authentication) {

                try {
                    OAuth2Authentication athentication = (OAuth2Authentication) user;
                    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails();

                    String clientId = athentication.getOAuth2Request().getClientId();

                    Map client = m_pClientServiceModule.getClient(clientId);

                    if (client != null) {
                        String flag = String.valueOf(client.get("ifLimit"));

                        if ("1".equals(flag)) {
                            String accessLimitCount = String.valueOf(client.get("limitCount"));
                            if (!accessLimitCount.isEmpty()) {
                                Result result = redisLimiterUtils.rateLimitOfDay(clientId, request.getRequestURI(),
                                        Long.parseLong(accessLimitCount));
                                if (-1 == result.getCode()) {
                                    log.error("token:" + details.getTokenValue() + result.getMsg());
                                    // ((ResultMsg)
                                    // this.error_info.get()).setMsg("clientid:" +
                                    // client_id + ":token:" + accessToken + ":" +
                                    // result.getMsg());
                                    // ((ResultMsg) this.error_info.get()).setCode(401);
                                    return false;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    StackTraceElement stackTraceElement = e.getStackTrace()[0];
                    log.error("checkLimit:" + "---|Exception:" + stackTraceElement.getLineNumber() + "----" + e.getMessage());
                }


            }

        }
        return true;
    }
}
