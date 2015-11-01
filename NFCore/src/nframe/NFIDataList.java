/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 数据列表
 */
public abstract class NFIDataList {
	/** 数据类型 */
	public enum ValueType {
		UNKNOWN,
		INT, // int64
		FLOAT,
		DOUBLE,
		STRING,
		OBJECT, // 对象id
	}
	
	/**
	 * 添加新数据
	 * @param value
	 * @return 返回新值得索引
	 */
	public abstract int add(long value);
	public abstract int add(float value);
	public abstract int add(double value);
	public abstract int add(String value);
	public abstract int add(NFIdent value);
	
	/**
	 * 添加一组新数据
	 * @param vars
	 */
	public abstract void add(Object... vars);
	
	/**
	 * 设置数据
	 * @param index
	 * @param value
	 * @return 先前的值
	 */
	public abstract long set(int index, long value);
	public abstract float set(int index, float value);
	public abstract double set(int index, double value);
	public abstract String set(int index, String value);
	public abstract NFIdent set(int index, NFIdent value);
	
	/**
	 * 设置一组数据，从0开始，如果vars的长度超过本身长度，剩余参数忽略
	 * @param vars
	 */
	public abstract void set(Object... vars);
	
	/**
	 * 获取数据
	 * @param index
	 * @return
	 */
	public abstract long getInt(int index);
	public abstract float getFloat(int index);
	public abstract double getDouble(int index);
	public abstract String getString(int index);
	public abstract NFIdent getObject(int index);
	
	public abstract int size();
	public abstract boolean isEmpty();
	public abstract void clear();
	public abstract ValueType getType(int index);
}
