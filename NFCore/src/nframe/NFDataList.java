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
		this.values = new ArrayList<Object>();
	}
	
	public NFDataList(NFIDataList other){
		this.values = new ArrayList<Object>(other.size());
		this.set(other);
	}
	
	public NFDataList(long value){
		this.values = new ArrayList<Object>(1);
		this.add(value);
	}
	
	public NFDataList(double value){
		this.values = new ArrayList<Object>(1);
		this.add(value);
	}
	
	public NFDataList(String value){
		this.values = new ArrayList<Object>(1);
		this.add(value);
	}
	
	public NFDataList(NFIdent value){
		this.values = new ArrayList<Object>(1);
		this.add(value);
	}
	
	public NFDataList(Object... vars){
		this.values = new ArrayList<Object>(vars.length);
		this.append(vars);
	}

	@Override
	public int add(long value) {
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
	public void append(Object... vars){
		for (Object o : vars){
			this.values.add(o);
		}
	}

	@Override
	public void set(int index, long value) {
		setValue(index, value);
	}

	@Override
	public void set(int index, double value) {
		setValue(index, value);
	}

	@Override
	public void set(int index, String value) {
		assert null != value;
		setValue(index, value);
	}

	@Override
	public void set(int index, NFIdent value) {
		assert null != value;
		setValue(index, value);
	}

	@Override
	public void set(NFIDataList other){
		if (this == other){
			return;
		}
		
		this.clear();
		for (int i=0, size=other.size(); i<size; ++i){
			NFIData.Type type = other.getType(i);
			switch (type){
			case INT:{
				this.add(other.getInt(i));
				break;
			}case FLOAT:{
				this.add(other.getFloat(i));
				break;
			}case STRING:{
				this.add(other.getString(i));
				break;
			}case OBJECT:{
				this.add(other.getObject(i));
				break;
			}default:
				assert false;
			}
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
	public double getFloat(int index) {
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
	public NFIData.Type getType(int index) {
		Object o = getValue(index);
		
		if (o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long){
			return NFIData.Type.INT;
		}else if (o instanceof Float || o instanceof Double){
			return NFIData.Type.FLOAT;
		}else if (o instanceof String){
			return NFIData.Type.STRING;
		}else if (o instanceof NFIdent){
			return NFIData.Type.OBJECT;
		}else{
			return NFIData.Type.UNKNOWN;
		}
	}
	
	private int addValue(Object o){
		int index = values.size();
		values.add(o);
		return index;
	}
	
	private void setValue(int index, Object o){
		values.set(index, o);
	}

	private Object getValue(int index) {
		return values.get(index);
	}
}
