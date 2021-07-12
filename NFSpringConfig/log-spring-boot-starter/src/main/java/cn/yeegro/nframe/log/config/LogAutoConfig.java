package cn.yeegro.nframe.log.config;

import javax.annotation.PostConstruct;
import cn.hutool.core.date.SystemClock;
import cn.yeegro.nframe.common.utils.IPUtils;
import com.github.structlog4j.StructLog4J;
import com.github.structlog4j.json.JsonFormatter;
import cn.yeegro.nframe.log.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 刘斌（改）
 * @create 2017年7月2日
 * 日志拦截器，排除对spring cloud gateway的影响 (WebMvcConfigurer)
 * 配合 EnableLogging 使用


 */
@ConditionalOnClass(WebMvcConfigurer.class)
public class LogAutoConfig implements WebMvcConfigurer {
 
	@Value("${spring.application.name:NA}")
    private String appName;
	
	@PostConstruct
	/*
	 * 初始化StructLog4J配置
	 */
	public void init(){
		
		StructLog4J.setFormatter(JsonFormatter.getInstance());
		
		StructLog4J.setMandatoryContextSupplier(()-> new Object[]{
			"host",	IPUtils.getHostIp(),
			"appName",appName ,
			"logTime",SystemClock.nowDate()
		});
		
	}
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
 
        /**
         * 自定义拦截器，添加拦截路径和排除拦截路径
         * addPathPatterns():添加需要拦截的路径
         * excludePathPatterns():添加不需要拦截的路径
         * 在括号中还可以使用集合的形式，如注释部分代码所示
         */
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**") ;
 
 
    }
}
