package cn.yeegro.nframe.log.aop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.model.SysLog;
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.TraceConstant;
import cn.yeegro.nframe.log.annotation.LogAnnotation;
import cn.yeegro.nframe.log.service.LogService;
import cn.yeegro.nframe.log.util.TraceUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 刘斌（改）
 * @create 2017年7月2日
 * 日志AOP,标准日志格式logback-spring.xml
 * 如果开启日志记录，需要多数据配置


 */
@Slf4j 
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
public class LogAnnotationAOP {

	
	@Autowired(required=false)
	private LogService logService ;
	
	@Autowired
	private TaskExecutor taskExecutor;

	private NFIPluginManager pPluginManager;
	private NFIUserModule m_pSysUserModule;

	@Around("@annotation(ds)")
	public Object logSave(ProceedingJoinPoint joinPoint, LogAnnotation ds) throws Throwable {

		pPluginManager= NFPluginManager.GetSingletonPtr();
		m_pSysUserModule=pPluginManager.FindModule(NFIUserModule.class);

		// 请求流水号
		String traceId = StringUtils.defaultString(TraceUtil.getTrace(), MDC.get(TraceConstant.LOG_TRACE_ID));
		// 记录开始时间
		long start = System.currentTimeMillis();
		// 获取方法参数
		String url = null;
		String httpMethod = null;
		Object result = null;
		List<Object> httpReqArgs = new ArrayList<Object>();
		SysLog sysLog = new SysLog();
		sysLog.setCreateTime(new Date());
		LoginAppUser loginAppUser = m_pSysUserModule.getLoginAppUser();
		if (loginAppUser != null) {
			sysLog.setUsername(loginAppUser.getUsername());
		}
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
		sysLog.setModule(logAnnotation.module() + ":" + methodSignature.getDeclaringTypeName() + "/"
				+ methodSignature.getName());

		Object[] args = joinPoint.getArgs();// 参数值
		url =  methodSignature.getDeclaringTypeName() + "/"+ methodSignature.getName();
		String params = null ;
		for (Object object : args) {
			if (object instanceof HttpServletRequest) {
				HttpServletRequest request = (HttpServletRequest) object;
				url = request.getRequestURI();
				httpMethod = request.getMethod();
			} else if (object instanceof HttpServletResponse) {
			} else {
				
				httpReqArgs.add(object);
			}
		}

		try {
			params = JSONObject.toJSONString(httpReqArgs);
			sysLog.setParams(params);
			// 打印请求参数参数
			log.info("开始请求，traceId={},  url={} , httpMethod={}, reqData={} ", traceId, url, httpMethod, params);
		} catch (Exception e) {
			log.error("记录参数失败：{}", e.getMessage());
		}

		try {
			// 调用原来的方法
			result = joinPoint.proceed();
			sysLog.setFlag(Boolean.TRUE);
		} catch (Exception e) {
			sysLog.setFlag(Boolean.FALSE);
			sysLog.setRemark(e.getMessage());
			log.error("请求报错，traceId={},  url={} , httpMethod={}, reqData={} ,error ={} ", traceId, url, httpMethod, params,e.getMessage());
			throw e;
		} finally {
			
//			log.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal()+"");
			
			//如果需要记录数据库开启异步操作
			if (logAnnotation.recordRequestParam()) {
				CompletableFuture.runAsync(() -> {
					try {
						
//							log.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal()+"");
							log.trace("日志落库开始：{}", sysLog);
							if(logService!=null){
								logService.save(sysLog);
							}
							log.trace("开始落库结束：{}", sysLog);
						
						
					} catch (Exception e) {
						log.error("落库失败：{}", e.getMessage());
					}
	
				}, taskExecutor);
			}
			 
			// 获取回执报文及耗时
			log.info("请求完成, traceId={}, 耗时={}, resp={}:", traceId, (System.currentTimeMillis() - start),
					result == null ? null : JSON.toJSONString(result));

		}
		return result;
	}

	
	
	
	/**
	 * 生成日志随机数
	 * 
	 * @return
	 */
	public String getRandom() {
		int i = 0;
		StringBuilder st = new StringBuilder();
		while (i < 5) {
			i++;
			st.append(ThreadLocalRandom.current().nextInt(10));
		}
		return st.toString() + System.currentTimeMillis();
	}

}