/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 属性抽象类
 */
public abstract class NFIProperty {
	public abstract String getName();
	public abstract NFIData.Type getType();

	/**
	 * 直接设置数据，不会触发属性回调
	 * @param value
	 */
	public abstract void set(NFIData other);
	public abstract NFIData get();
	
	public abstract long getInt();
	public abstract double getFloat();
	public abstract String getString();
	public abstract NFIdent getObject();

	/**
	 * 设置数据，如果旧的值和新值不同，则触发属性回调
	 * @param var
	 */
	public abstract void set(long var);
	public abstract void set(double var);
	public abstract void set(String var);
	public abstract void set(NFIdent var);
	
	/**
	 * 设置为空，如果旧的值非空，则触发属性回调
	 */
	public abstract void set();
	
	/**
	 * 是否为空
	 * @return 返回true如果是空；反之false
	 */
	public abstract boolean isEmpty();
	
	/**
	 * 添加属性回掉，当属性值改变的时候，自动调用回掉，可以添加多个（按顺序调用）
	 * @param cb
	 */
	public abstract void addCallback(NFIPropertyHandler cb);
}
