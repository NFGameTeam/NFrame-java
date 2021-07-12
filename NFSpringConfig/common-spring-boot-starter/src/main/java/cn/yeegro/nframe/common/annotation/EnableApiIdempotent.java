package cn.yeegro.nframe.common.annotation;

import cn.yeegro.nframe.common.selector.ApiIdempotentImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动幂等拦截器
 * @author gitgeek
 * @create 2019年9月5日
 * 自动装配starter
 * 选择器


 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Import(ApiIdempotentImportSelector.class)
public @interface EnableApiIdempotent {
}
