package com.noahframe.api.file;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

public class PluginContextUtil {

	private ApplicationContext ctx;

    @SuppressWarnings("unchecked")
    public synchronized <T> T getBean(String beanId) {

        if (ctx.containsBean(beanId))
        {
            return  (T)ctx.getBean(beanId);
        }
        else {
            return null;
        }
    }

    public void initContext(String xml) {
        String[] xmls = new String[] { xml };//"classpath:applicationContext.xml"
        ctx = null;
        if (ctx == null) {
            ctx = new ClassPathXmlApplicationContext(xmls);
        }

        int a= ctx.getBeanDefinitionCount();
        String[] s=ctx.getBeanDefinitionNames();

    }
	
}
