package cn.yeegro.nframe.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 * @author 刘斌（改）
 * @create 2017年7月2日
 * recordRequestParam:true需要配置log数据源


 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

	/**
	 * 模块
	 * @return
	 */
	String module();

	/**
	 * 记录执行参数
	 * @return
	 */
	boolean recordRequestParam() default false;
}
