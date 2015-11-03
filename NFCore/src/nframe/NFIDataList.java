/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 数据列表
 */
public abstract class NFIDataList {
	/**
	 * 添加新数据
	 * @param value
	 * @return 返回新值得索引
	 */
	public abstract int add(long value);
	public abstract int add(double value);
	public abstract int add(String value);
	public abstract int add(NFIdent value);
	
	/**
	 * 添加一组新数据
	 * @param vars
	 */
	public abstract void append(Object... vars);
	
	/**
	 * 设置数据
	 * @param index
	 * @param value
	 */
	public abstract void set(int index, long value);
	public abstract void set(int index, double value);
	public abstract void set(int index, String value);
	public abstract void set(int index, NFIdent value);
	
	/**
	 * 从其他对象拷贝数据
	 * @param other
	 */
	public abstract void set(NFIDataList other);
	
	/**
	 * 获取数据
	 * @param index
	 * @return
	 */
	public abstract long getInt(int index);
	public abstract double getFloat(int index);
	public abstract String getString(int index);
	public abstract NFIdent getObject(int index);
	
	public abstract int size();
	public abstract boolean isEmpty();
	public abstract void clear();
	public abstract NFIData.Type getType(int index);
}
