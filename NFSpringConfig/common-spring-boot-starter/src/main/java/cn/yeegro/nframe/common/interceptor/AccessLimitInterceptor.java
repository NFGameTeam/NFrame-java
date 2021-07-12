package cn.yeegro.nframe.common.interceptor;

import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.annotation.AccessLimit;
import cn.yeegro.nframe.common.web.Result;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;

/**
 * 非网关部分应用次数限制
 * blog: https://blog.51cto.com/13005375
 * code: https://gitee.com/owenwangwen/open-capacity-platform
 */

@AllArgsConstructor
@SuppressWarnings("all")
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private NFIRedisModule redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        NFIPluginManager pPluginManager= NFPluginManager.GetSingletonPtr();
        NFIUserModule m_pSysUserModule=pPluginManager.FindModule(NFIUserModule.class);

        if (handler instanceof HandlerMethod) {

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {
                LoginAppUser user = m_pSysUserModule.getLoginAppUser();
                if (user == null) {
                    render(response, Result.failed("用户鉴权异常！"));
                    return false;
                }
                key += ":" + user.getId();
            } else {
                // do nothing
            }

            if (!redisUtil.hasKey(key) || redisUtil.getExpire(key) <= 0) {
                redisUtil.set(key, 0, seconds);
            }
            if (redisUtil.incr(key, 1) > maxCount) {
                render(response, Result.failed("访问太频繁！"));
                return false;
            }

        }
        return true;
    }

    private void render(HttpServletResponse response, Result result) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(result);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

}
