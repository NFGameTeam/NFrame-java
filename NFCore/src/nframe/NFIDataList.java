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
	public abstract int add(long var);
	public abstract int add(double var);
	public abstract int add(String var);
	public abstract int add(NFGUID var);
	
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
	public abstract boolean set(int index, long var);
	public abstract boolean set(int index, double var);
	public abstract boolean set(int index, String var);
	public abstract boolean set(int index, NFGUID var);
	
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
	public abstract NFGUID getObject(int index);
	
	public abstract int size();
	public abstract boolean isEmpty();
	public abstract void clear();
	public abstract NFIData.Type getType(int index);
}
