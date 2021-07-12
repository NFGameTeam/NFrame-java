package cn.yeegro.nframe.common.async;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author 刘斌（改）


 *
 */
// https://stackoverflow.com/questions/23732089/how-to-enable-request-scope-in-async-task-executor
// 传递RequestAttributes and MDC and SecurityContext
public class ContextCopyingDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        try {
			RequestAttributes context = RequestContextHolder.currentRequestAttributes();  //1
			Map<String,String> previous = MDC.getCopyOfContextMap(); 					  //2
			SecurityContext securityContext = SecurityContextHolder.getContext();	      //3
			return () -> {
			    try {
			    	RequestContextHolder.setRequestAttributes(context);	 //1
			    	MDC.setContextMap(previous);					     //2				
			        SecurityContextHolder.setContext(securityContext);   //3
			        runnable.run();
			    } finally {
			        RequestContextHolder.resetRequestAttributes();		// 1
			        MDC.clear(); 										// 2
			        SecurityContextHolder.clearContext();				// 3
			    }
			};
		} catch (IllegalStateException e) {
			return runnable;
		}
    }
}
