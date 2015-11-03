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
	 * 设置属性
	 * @param name
	 * @param var
	 * @return 如果name不存在返回false；否则返回true
	 */
	public boolean setProperty(String name, long var);
	public boolean setProperty(String name, double var);
	public boolean setProperty(String name, String var);
	public boolean setProperty(String name, NFIdent var);
	public boolean setProperty(String name, NFIData var);
	
	/**
	 * 获取属性
	 * @param name
	 * @return 如果name不存在返回null；否则返回获取的属性
	 */
	public NFIProperty getProperty(String name);
	
	public NFIProperty[] getPropertyList();
	public boolean addCallback(String name, NFIPropertyHandler cb);
}
