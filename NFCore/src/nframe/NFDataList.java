/**
 * 
 */
package nframe;

import java.util.ArrayList;
import java.util.List;

import nframe.NFIData.Type;

/**
 * @author Xiong
 * 数据列表实现类
 */
public class NFDataList implements NFIDataList {
	
	private List<Object> vars;
	
	public NFDataList(){
		this.vars = new ArrayList<Object>();
	}
	
	public NFDataList(NFIDataList other){
		this.vars = new ArrayList<Object>(other.size());
		this.set(other);
	}
	
	public NFDataList(long var){
		this.vars = new ArrayList<Object>(1);
		this.add(var);
	}
	
	public NFDataList(double var){
		this.vars = new ArrayList<Object>(1);
		this.add(var);
	}
	
	public NFDataList(String var){
		this.vars = new ArrayList<Object>(1);
		this.add(var);
	}
	
	public NFDataList(NFGUID var){
		this.vars = new ArrayList<Object>(1);
		this.add(var);
	}
	
	public NFDataList(Object... vars){
		this.vars = new ArrayList<Object>(vars.length);
		this.append(vars);
	}

	@Override
	public int add(long var) {
		return addVar(var);
	}

	@Override
	public int add(double var) {
		return addVar(var);
	}

	@Override
	public int add(String var) {
		assert var != null;
		return addVar(var);
	}

	@Override
	public int add(NFGUID var) {
		assert var != null;
		return addVar(var);
	}
	
	@Override
	public int add(NFIData var) {
		assert var != null;
		switch(var.getType())
		{
		case INT:
			return add(var.getInt());
		case FLOAT:
			return add(var.getFloat());
		case OBJECT:
			return add(var.getObject());
		case STRING:
			return add(var.getString());
		default:
				break;
				
		}
		
		return -1;
	}
	
	@Override
	public void append(Object... vars){
		for (Object o : vars){
			assert o != null;
			this.vars.add(o);
		}
	}

	@Override
	public boolean set(int index, long var) {
		return setVar(index, var);
	}

	@Override
	public boolean set(int index, double var) {
		return setVar(index, var);
	}

	@Override
	public boolean set(int index, String var) {
		assert var != null;
		return setVar(index, var);
	}

	@Override
	public boolean set(int index, NFGUID var) {
		assert null != var;
		return setVar(index, var);
	}

	@Override
	public void set(NFIDataList other){
		assert other != null;
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
		Object o = getVar(index);
		if (o == null){
			return NFIData.INT_NIL;
		}
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
		Object o = getVar(index);
		if (o == null){
			return NFIData.FLOAT_NIL;
		}
		if (o instanceof Float){
			return Float.class.cast(o);
		}else{
			return Double.class.cast(o);
		}
	}

	@Override
	public String getString(int index) {
		Object o = getVar(index);
		if (o == null){
			return NFIData.STRING_NIL;
		}
		return String.class.cast(o);
	}

	@Override
	public NFGUID getObject(int index) {
		Object o = getVar(index);
		if (o == null){
			return NFIData.OBJECT_NIL;
		}
		return NFGUID.class.cast(o);
	}

	@Override
	public int size() {
		return vars.size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public void clear() {
		vars.clear();
	}

	@Override
	public NFIData.Type getType(int index) {
		Object o = getVar(index);
		if (o == null){
			return Type.UNKNOW;
		}
		
		if (o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long){
			return Type.INT;
		}else if (o instanceof Float || o instanceof Double){
			return Type.FLOAT;
		}else if (o instanceof String){
			return Type.STRING;
		}else if (o instanceof NFGUID){
			return Type.OBJECT;
		}else{
			return Type.UNKNOW;
		}
	}
	
	private int addVar(Object o){
		int index = vars.size();
		vars.add(o);
		return index;
	}
	
	private boolean setVar(int index, Object o){
		if (index < 0 || index >= vars.size()){
			return false;
		}
		vars.set(index, o);
		return true;
	}

	private Object getVar(int index) {
		if (index >= 0 && index < vars.size()){
			return vars.get(index);
		}
		return null;
	}
}
