package cn.yeegro.nframe.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * spring获取bean工具类
 * 
* @author 作者 zoocee(改)
 * @version 创建时间：2018年3月20日 下午10:13:18 类说明
 *
 */
@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.applicationContext = applicationContext;
	}

	public static <T> T getBean(Class<T> cla) {
		T m_Bean=null;
		try{
			m_Bean=applicationContext.getBean(cla);
		}
		catch (Exception e)
		{

		}
		return m_Bean;
	}

	public static <T> T getBean(String name, Class<T> cal) {
		return applicationContext.getBean(name, cal);
	}

	public static String getProperty(String key) {
		return applicationContext.getBean(Environment.class).getProperty(key);
	}

	public static void setAppContext(ApplicationContext applicationContext){
		SpringUtils.applicationContext = applicationContext;
	}

	public static ApplicationContext getAppContext()
	{
		return SpringUtils.applicationContext;
	}
}
