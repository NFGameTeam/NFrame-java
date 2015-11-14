/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 数据列表
 */
public interface NFIDataList {
	/**
	 * 添加新数据
	 * @param value
	 * @return 返回新值得索引
	 */
	public int add(long var);
	public int add(double var);
	public int add(String var);
	public int add(NFGUID var);
	
	/**
	 * 添加一组新数据
	 * @param vars
	 */
	public void append(Object... vars);
	
	/**
	 * 设置数据
	 * @param index
	 * @param value
	 */
	public boolean set(int index, long var);
	public boolean set(int index, double var);
	public boolean set(int index, String var);
	public boolean set(int index, NFGUID var);
	
	/**
	 * 从其他对象拷贝数据
	 * @param other
	 */
	public void set(NFIDataList other);
	
	/**
	 * 获取数据
	 * @param index
	 * @return
	 */
	public long getInt(int index);
	public double getFloat(int index);
	public String getString(int index);
	public NFGUID getObject(int index);
	
	public int size();
	public boolean isEmpty();
	public void clear();
	public NFIData.Type getType(int index);
}
