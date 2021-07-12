package cn.yeegro.nframe.common.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
* @author 作者 zoocee(改)
* @version 创建时间：2017年11月12日 上午22:57:51
* 非网关应用限流

*
*/
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
	int seconds();
	int maxCount();
	boolean needLogin() default true;
}
