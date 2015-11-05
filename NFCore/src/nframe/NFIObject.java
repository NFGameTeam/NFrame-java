/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 基础对象抽象类
 */
public abstract class NFIObject extends NFBehaviour {
	/**
	 * 查看name的属性是否存在
	 * @param name
	 * @return 如果name的属性存在返回true；反之返回false
	 */
	public abstract boolean hasProperty(String name);
	
	/**
	 * 设置指定name的属性值，如果旧的值和新值不同，则触发属性回调
	 * @param name
	 * @param var
	 * @return 如果name的属性存在，返回true；反之返回false
	 */
	public abstract boolean setProperty(String name, long var);
	public abstract boolean setProperty(String name, double var);
	public abstract boolean setProperty(String name, String var);
	public abstract boolean setProperty(String name, NFIdent var);
	
	/**
	 * 置空指定的属性，如果旧的值非空，则触发属性回调
	 * @param name
	 * @return 如果name的属性存在，返回true；反之返回false
	 */
	public abstract boolean setProperty(String name);
	
	/**
	 * 设置指定name的属性值，不会触发属性回调
	 * @param name
	 * @param var
	 * @return 如果name的属性存在，返回true；反之返回false
	 */
	public abstract boolean setProperty(String name, NFIData var);
	
	/**
	 * 获取属性
	 * @param name
	 * @return 如果name不存在返回null；否则返回获取的属性
	 */
	public abstract NFIProperty getProperty(String name);
	
	/**
	 * 获取属性管理器
	 * @return
	 */
	public abstract NFIPropertyManager getPropertyManager();
}
