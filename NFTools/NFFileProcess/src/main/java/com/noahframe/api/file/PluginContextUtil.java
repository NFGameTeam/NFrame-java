package com.noahframe.api.file;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

public class PluginContextUtil {

	private ApplicationContext ctx;

    @SuppressWarnings("unchecked")
    public synchronized <T> T getBean(String beanId) {

        return (T) ctx.getBean(beanId);
    }

    public void initContext(String xml) {
        String[] xmls = new String[] { xml };//"classpath:applicationContext.xml"
        ctx = null;
        ctx = ContextLoader.getCurrentWebApplicationContext();
        if (ctx == null) {
            ctx = new ClassPathXmlApplicationContext(xmls);
        }
    }
	
}
