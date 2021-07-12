package cn.yeegro.nframe.log.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author 刘斌（改）
 * log-spring-boot-starter 自动装配 


 */


public class LogImportSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		
		return new String[] { 
				"cn.yeegro.nframe.log.aop.LogAnnotationAOP",
//				"cn.yeegro.nframe.log.config.SentryAutoConfig",
				"cn.yeegro.nframe.log.service.impl.LogServiceImpl",
				"cn.yeegro.nframe.log.config.LogAutoConfig"
				
		};
	}

}