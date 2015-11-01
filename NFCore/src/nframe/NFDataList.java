/**
 * 
 */
package nframe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiong
 * 数据列表实现类
 */
public class NFDataList extends NFIDataList {
	
	private List<Object> values;
	
	public NFDataList(){
		this(0);
	}
	
	public NFDataList(int reserve){
		this.values = new ArrayList<Object>(reserve);
	}
	
	public NFDataList(Object... vars){
		this(vars.length);
		this.add(vars);
	}

	@Override
	public int add(long value) {
		return addValue(value);
	}

	@Override
	public int add(float value) {
		return addValue(value);
	}

	@Override
	public int add(double value) {
		return addValue(value);
	}

	@Override
	public int add(String value) {
		return addValue(value);
	}

	@Override
	public int add(NFIdent value) {
		return addValue(value);
	}
	
	@Override
	public void add(Object... vars){
		for (Object o : vars){
			this.values.add(o);
		}
	}

	@Override
	public long set(int index, long value) {
		return Long.class.cast(setValue(index, value));
	}

	@Override
	public float set(int index, float value) {
		return Float.class.cast(setValue(index, value));
	}

	@Override
	public double set(int index, double value) {
		return Double.class.cast(setValue(index, value));
	}

	@Override
	public String set(int index, String value) {
		return String.class.cast(setValue(index, value));
	}

	@Override
	public NFIdent set(int index, NFIdent value) {
		return NFIdent.class.cast(setValue(index, value));
	}

	@Override
	public void set(Object... vars){
		int size = vars.length >= this.size() ? this.size() : vars.length;
		for (int i=0; i<size; ++i){
			values.set(i, vars[i]);
		}
	}

	@Override
	public long getInt(int index) {
		Object o = getValue(index);
		if (o instanceof Byte){
			return Byte.class.cast(o);
		}else if (o instanceof Short){
			return Short.class.cast(o);
		}else if (o instanceof Integer){
			return Integer.class.cast(o);
		}else{
			return Long.class.cast(o);
		}
	}

	@Override
	public float getFloat(int index) {
		Object o = getValue(index);
		return Float.class.cast(o);
	}

	@Override
	public double getDouble(int index) {
		Object o = getValue(index);
		if (o instanceof Float){
			return Float.class.cast(o);
		}else{
			return Double.class.cast(o);
		}
	}

	@Override
	public String getString(int index) {
		Object o = getValue(index);
		return String.class.cast(o);
	}

	@Override
	public NFIdent getObject(int index) {
		Object o = getValue(index);
		return NFIdent.class.cast(o);
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public void clear() {
		values.clear();
	}

	@Override
	public ValueType getType(int index) {
		Object o = getValue(index);
		if (null == o){
			return ValueType.UNKNOWN;
		}
		
		if (o instanceof Long){
			return ValueType.INT;
		}else if (o instanceof Float){
			return ValueType.FLOAT;
		}else if (o instanceof Double){
			return ValueType.DOUBLE;
		}else if (o instanceof String){
			return ValueType.STRING;
		}else if (o instanceof NFIdent){
			return ValueType.OBJECT;
		}else{
			return ValueType.UNKNOWN;
		}
	}
	
	private int addValue(Object o){
		int index = values.size();
		values.add(o);
		return index;
	}
	
	private Object setValue(int index, Object o){
		return values.set(index, o);
	}

	private Object getValue(int index) {
		return values.get(index);
	}
}
