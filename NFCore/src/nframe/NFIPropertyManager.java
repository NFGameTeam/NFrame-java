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
	public NFIProperty addProperty(String name, NFGUID var);
	public NFIProperty addProperty(String name, NFIData var);
	
	/**
	 * 返回属性值
	 * @param name
	 * @return 如果不存在，则返回默认值
	 */
	public long getPropertyInt(String name);
	public double getPropertyFloat(String name);
	public String getPropertyString(String name);
	public NFGUID getPropertyObject(String name);
	
	/**
	 * 设置属性
	 * @param name
	 * @param var
	 * @return 如果属性不存在，则返回false
	 */
	public boolean setProperty(String name, long var);
	public boolean setProperty(String name, double var);
	public boolean setProperty(String name, String var);
	public boolean setProperty(String name, NFGUID var);
	
	/**
	 * 直接设置，不会触发属性回调
	 * @param name
	 * @param var
	 * @return
	 */
	public boolean setProperty(String name, NFIData var);
	
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
