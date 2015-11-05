/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 属性管理器抽象类
 */
public interface NFIPropertyManager {
	/**
	 * 添加属性
	 * @param name
	 * @param var
	 * @return 如果name已经存在，则返回null；否则返回新添加的属性
	 */
	public NFIProperty addProperty(String name, long var);
	public NFIProperty addProperty(String name, double var);
	public NFIProperty addProperty(String name, String var);
	public NFIProperty addProperty(String name, NFIdent var);
	public NFIProperty addProperty(String name, NFIData var);
	
	/**
	 * 添加一个空的属性
	 * @param name
	 * @return 如果name已经存在，则返回null；否则返回新添加的属性
	 */
	public NFIProperty addProperty(String name);
	
	/**
	 * 获取属性
	 * @param name
	 * @return 如果name不存在返回null；否则返回获取的属性
	 */
	public NFIProperty getProperty(String name);
	
	/**
	 * 是否有指定name的属性
	 * @param name
	 * @return 如果name存在返回true；反之false
	 */
	public boolean hasProperty(String name);
	
	public NFIProperty[] getPropertyList();
	public boolean addCallback(String name, NFIPropertyHandler cb);
}
