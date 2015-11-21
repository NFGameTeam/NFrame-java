/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 记录（属性表）接口
 */
public interface NFIRecord {
	/** 操作类型 */
	public enum Optype {
		ADD,
		DEL,
		SWAP,
		CREATE,
		UPDATE
	}
	
	/**
	 * 返回当前行的数目
	 * @return
	 */
	public int getRowNum();
	
	/**
	 * 返回列的数量
	 * @return
	 */
	public int getColumnNum();
	
	/**
	 * 返回指定索引的列的类型
	 * @param column
	 * @return
	 */
	public NFIData.Type getColumnType(int column);
	
	/**
	 * 获取指定列的tag
	 * @param column 指定的列索引
	 * @return 如果不存在返回NFIData.STRING_NIL
	 */
	public String getColumnTag(int column);
	
	/**
	 * 设置索引（类似sql）
	 * @param uniqueIndexColumn 唯一索引的列
	 * @param indexColumn 不唯一索引的列
	 * @return
	 */
	public boolean setIndex(int uniqueIndexColumn, int indexColumn);
	
	/**
	 * 添加新的一行，如果add成功会触发ADD回调
	 * @param row 指定的行索引，可以不连续
	 * @return 如果指定的行索引已经存在返回-1，添加失败
	 */
	public int addRow(int row);
	
	/**
	 * 添加新的一行，如果add成功会触发ADD回调
	 * @param row 指定的行索引，可以不连续
	 * @param var 行数据
	 * @return 如果指定的行索引已经存在返回-1，添加失败；成功则返回row索引
	 */
	public int addRow(int row, NFIDataList var);
	
	/**
	 * 设置行数据，如果不是add则不会触发回调
	 * @param row 如果不存在，则会add新一行
	 * @param var 行数据
	 * @return 如果失败返回-1，成功则返回row索引
	 */
	public int setRow(int row, NFIDataList var);
	
	/** 
	 * 设置数据
	 * @param row 指定行，如果不存在，则会add新一行，会触发add回调
	 * @param column 指定列
	 * @param var 数据，如果值和旧的不同，则会触发UPDATE回调
	 * @return
	 */
	public boolean set(int row, int column, long var);
	public boolean set(int row, int column, double var);
	public boolean set(int row, int column, String var);
	public boolean set(int row, int column, NFGUID var);
	
	/**
	 * 设置数据
	 * @param row 指定行，如果不存在，则会add新一行，会触发add回调
	 * @param colTag 列标识（列名），每列会有自己的唯一的tag
	 * @param var 数据，如果值和旧的不同，则会触发UPDATE回调
	 * @return
	 */
	public boolean set(int row, String colTag, long var);
	public boolean set(int row, String colTag, double var);
	public boolean set(int row, String colTag, String var);
	public boolean set(int row, String colTag, NFGUID var);
	
	/**
	 * 取得行数据
	 * @param row 指定的行索引
	 * @return 如果行索引不存在，返回null
	 */
	public NFIDataList getRow(int row);
	
	/**
	 * 交换2个指定的行，会触发SWAP回调
	 * @param originRow
	 * @param targetRow
	 * @return 如果指定的行索引有任一不存在，则返回false
	 */
	public boolean swapRow(int originRow, int targetRow);
	
	/**
	 * 获取数据
	 * @param row 指定的行索引
	 * @param column 指定的列索引
	 * @return 如不存在，返回NFIData.XXX_NIL
	 */
	public long getInt(int row, int column);
	public float getFloat(int row, int column);
	public String getString(int row, int column);
	public NFGUID getObject(int row, int column);
	
	/**
	 * 获取数据
	 * @param row 指定的行索引
	 * @param colTag 列标识（列名）
	 * @return 如不存在，返回NFIData.XXX_NIL
	 */
	public long getInt(int row, String colTag);
	public float getFloat(int row, String colTag);
	public String getString(int row, String colTag);
	public NFGUID getObject(int row, String colTag);
	
	/**
	 * 查找指定的值
	 * @param column 指定的列索引
	 * @param value 要查找的值
	 * @param result 不能为null；存放查找到的行索引的列表；如果发生错误，不会有任何改动
	 * @return 如果指定的列索引不存在返回-1；如果正常，返回找到的行的数量
	 */
	public int find(int column, long value, NFIDataList result);
	public int find(int column, float value, NFIDataList result);
	public int find(int column, String value, NFIDataList result);
	public int find(int column, NFGUID value, NFIDataList result);

	/**
	 * 查找指定的值
	 * @param colTag 指定的列tag
	 * @param value 要查找的值
	 * @param result 不能为null；存放查找到的行索引的列表；如果发生错误，不会有任何改动
	 * @return 如果指定的列tag不存在返回-1；如果正常，返回找到的行的数量
	 */
	public int find(String colTag, long value, NFIDataList result);
	public int find(String colTag, float value, NFIDataList result);
	public int find(String colTag, String value, NFIDataList result);
	public int find(String colTag, NFGUID value, NFIDataList result);
	
	/**
	 * 删除指定索引的行，如果行索引存在会触发DEL回调
	 * @param row
	 * @return 如果行索引不存在返回false
	 */
	public boolean delRow(int row);
	
	/**
	 * 清除所有的行，会触发DEL回调
	 */
	public void clear();
	
	/**
	 * 添加属性回调
	 * @param cb
	 */
	public void addCallback(NFIRecordHandler cb);
}
