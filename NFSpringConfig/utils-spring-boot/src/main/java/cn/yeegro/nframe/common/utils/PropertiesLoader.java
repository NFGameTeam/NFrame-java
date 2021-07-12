package cn.yeegro.nframe.common.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Properties文件载入工具类. 可载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先.<br/>
 * 为了实现该类的方法是 static 的. 在工程启动时就应该对它进行初始化.<br/>
 * 要使用该类, 必须进行初始化:<br/>
 * 在工程中实现: ServletContextListener 接口的 contextInitialized() 和 contextDestroyed() 方法.<br/>
 * ServletContextListener 接口在 Spring MVC 由Web容器启动时进行调用.<br/>
 * 要使用该接口, 还必须在 web.xml中进行配置:
  <listener><br/>
    <listener-class>com.tianhai.saas.system.config.ProcessorContextListener</listener-class><br/>  
  </listener><br/>
 * 该配置需要早于 ContextLoaderListener 的配置.
 */
public class PropertiesLoader {
  private Properties properties;
  private static PropertiesLoader instance;
  
  /**
   * 可载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先<br/>
   * 初始化后, 可以使用该类的 static 方法直接访问.
   * @param resourcesPaths
   * @return 初始化成功返回 true; 失败返回 false;
   */
  public boolean init(String... resourcesPaths){
    if( null != instance )
      instance = null;
    this.properties = loadProperties(resourcesPaths);
    if( null == this.properties ){
      Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, "load properties fail, please check!",resourcesPaths);
      return false;
    }
    instance = this;
    instance.properties = this.properties;
    return true;
  }

  private static PropertiesLoader getInstance(){
    return instance;
  }

  /**
   * 返回已经装载的 Properties 对象
   * @return
   */
  public static Properties getProperties() {
    return PropertiesLoader.getInstance().properties;
  }

  /**
   * 取出Property。
   */
  private static String getValue(String key) {
    String systemProperty = System.getProperty(key);
    if (systemProperty != null) {
      return systemProperty;
    }
    if( null == instance || null == PropertiesLoader.getProperties() ){
      Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, "you are trying to access an uninitialized PropertiesLoader object.");
      return null;
    }
    return PropertiesLoader.getProperties().getProperty(key);
  }

  /**
   * 取出String类型的Property,如果都為Null则抛出异常.
   */
  public static String getProperty(String key) {
    String value = getValue(key);
    if (value == null) {
      throw new NoSuchElementException();
    }
    return value;
  }

  /**
   * 取出String类型的Property.如果都為Null則返回Default值.
   */
  public static String getProperty(String key, String defaultValue) {
    String value = getValue(key);
    return value != null ? value : defaultValue;
  }

  /**
   * 取出Integer类型的Property.如果都為Null或内容错误则抛出异常.
   */
  public static Integer getInteger(String key) {
    String value = getValue(key);
    if (value == null) {
      throw new NoSuchElementException();
    }
    return Integer.valueOf(value);
  }

  /**
   * 取出Integer类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
   */
  public static Integer getInteger(String key, Integer defaultValue) {
    String value = getValue(key);
    return value != null ? Integer.valueOf(value) : defaultValue;
  }

  /**
   * 取出Double类型的Property.如果都為Null或内容错误则抛出异常.
   */
  public static Double getDouble(String key) {
    String value = getValue(key);
    if (value == null) {
      throw new NoSuchElementException();
    }
    return Double.valueOf(value);
  }

  /**
   * 取出Double类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
   */
  public static Double getDouble(String key, Integer defaultValue) {
    String value = getValue(key);
    return value != null ? Double.valueOf(value) : defaultValue;
  }

  /**
   * 取出Boolean类型的Property.如果都為Null抛出异常,如果内容不是true/false则返回false.
   */
  public static Boolean getBoolean(String key) {
    String value = getValue(key);
    if (value == null) {
      throw new NoSuchElementException();
    }
    return Boolean.valueOf(value);
  }

  /**
   * 取出Boolean类型的Propert.如果都為Null則返回Default值,如果内容不为true/false则返回false.
   */
  public static Boolean getBoolean(String key, boolean defaultValue) {
    String value = getValue(key);
    return value != null ? Boolean.valueOf(value) : defaultValue;
  }

  /**
   * 载入多个文件, 文件路径使用Spring Resource格式
   * @param resourcesPaths 带路径的文件名称, 可变参数.
   */
  private static Properties loadProperties(String... resourcesPaths) {
    Properties props = new Properties();
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    for (String location : resourcesPaths) {

      Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, "Loading properties file from path:{}",location);

      InputStream is = null;
      try {
        Resource resource = resourceLoader.getResource(location);
        is = resource.getInputStream();
        props.load(is);
      } catch (IOException ex) {
        Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, "Could not load properties from path:{}, {} ",ex);
      } finally {
        IOUtils.closeQuietly(is);
      }
    }
    return props;
  }
}
