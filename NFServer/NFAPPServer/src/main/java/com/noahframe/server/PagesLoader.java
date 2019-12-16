package com.noahframe.server;

import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.module.NFILogModule;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author:zoocee
 * @Date:2019/9/13 9:41
 */
public class PagesLoader extends BaseFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "加载页面loader", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

        ApplicationContext Context=(ApplicationContext) pPluginManager.getWebContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping=Context.getBean(RequestMappingHandlerMapping.class);

        Map mappings= requestMappingHandlerMapping.getHandlerMethods();

        String[] ss= Context.getBeanDefinitionNames();


        chain.doFilter(request, response);
    }
}
