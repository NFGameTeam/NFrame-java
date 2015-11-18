/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 属性抽象类
 */
public interface NFIProperty {
	public String getName();
	public NFIData.Type getType();

	/**
	 * 直接设置数据，不会触发属性回调
	 * @param value
	 */
	public void set(NFIData other);
	public NFIData get();
	
	public long getInt();
	public double getFloat();
	public String getString();
	public NFGUID getObject();

	/**
	 * 设置数据，如果旧的值和新值不同，则触发属性回调
	 * @param var
	 */
	public void set(long var);
	public void set(double var);
	public void set(String var);
	public void set(NFGUID var);
	
	/**
	 * 设置为空，如果旧的值非空，则触发属性回调
	 */
	public void set();
	
	/**
	 * 是否为空
	 * @return 返回true如果是空；反之false
	 */
	public boolean isEmpty();
	
	/**
	 * 添加属性回掉，当属性值改变的时候，自动调用回掉，可以添加多个（按顺序调用）
	 * @param cb
	 */
	public void addCallback(NFIPropertyHandler cb);
}
