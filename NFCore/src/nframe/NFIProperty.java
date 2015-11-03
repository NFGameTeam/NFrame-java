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
	 * 直接设置数据，不会触发回调
	 * @param value
	 */
	public abstract void set(NFIData value);
	public abstract NFIData get();
	
	public abstract long getInt();
	public abstract double getFloat();
	public abstract String getString();
	public abstract NFIdent getObject();

	public abstract void set(long value);
	public abstract void set(double value);
	public abstract void set(String value);
	public abstract void set(NFIdent value);
	
	/**
	 * 添加属性回掉，当属性值改变的时候，自动调用回掉，可以添加多个（按顺序调用）
	 * @param cb
	 */
	public abstract void addCallback(NFIPropertyHandler cb);
}
