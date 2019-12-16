package com.noahframe.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.iface.NFIPluginManager;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.module.NFILogModule;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public abstract class BaseFilter implements Filter {

  protected static NFIPluginManager pPluginManager= NFPluginManager.GetSingletonPtr();
  protected static NFILogModule m_pLogModule=pPluginManager.FindModule(NFILogModule.class);

  /** 忽略资源文件正则 */
  protected static Pattern pattern = Pattern.compile("^.*\\..+$");

  /** 不拦截的请求列表 */
  protected List<String> ignoreUris = new ArrayList<String>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // 初始化忽略列表
    String settings = filterConfig.getInitParameter("ignoreUris");
    initList(settings, ignoreUris);
  }
  
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    doFilter(request, response, chain);
  }
  
  public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

  @Override
  public void destroy() {
    // do nothing...
  }

  /**
   * 判断请求是否忽略拦截
   * @param request
   * @return
   */
  protected boolean isIgnore(HttpServletRequest request) {
    return isIgnore(request.getServletPath(), ignoreUris);
  }
  
  /**
   * 判断uri是否忽略拦截
   * @return true-忽略,false-不忽略
   */
  protected boolean isIgnore(String uri, List<String> ignoreList) {
    // 忽略资源文件拦截
    if (pattern.matcher(uri).matches()) {
      return true;
    }

    // 判断请求是否在忽略列表
    if (ignoreUris == null || ignoreUris.size() == 0) {
      return false;
    }

    for (String igUri : ignoreUris) {
      if (igUri.equals(uri)) {
        return true;
      }
      
      if (igUri.endsWith("/*")) {
        igUri = igUri.replace("/*", "");
        if (uri.startsWith(igUri)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 初始化配置列表
   * @param settings 连接字符窜，逗号隔开
   * @param ignoreUris 需要初始化的list
   */
  protected void initList(String settings, List<String> ignoreUris) {
    if (StringUtils.isEmpty(settings)) {
      return;
    }
    /* split分割，没有分隔符，返回一个元素的数组，元素为他本身 */
    String[] arr = settings.split(",");
    if (arr == null || arr.length == 0) {
      return;
    }
    ignoreUris.addAll(Arrays.asList(arr));
  }
  
  /**
   * 字符串编码
   * @param str
   * @return
   */
  public static String encode(String str) {
    String result = str;
    try {
      result = URLEncoder.encode(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {

      m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "An exception has ocurred."+e.getMessage(), m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
    }
    return result;
  }
  
  /**
   * 返回初始化的Service对象
   * @param filterConfig
   * @param clazz Service类型
   * @return
   */
  protected <T> T initService(FilterConfig filterConfig, Class<T> clazz) {
    // filter不能注解，所以需要初始化cmCustomerService
    ServletContext sc = filterConfig.getServletContext();
    WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);
    return wac.getBean(clazz);
  }

  public static void main(String[] args) {
    String s = "abc";
    System.out.println(Arrays.asList(s.split(",")));
  }

}
