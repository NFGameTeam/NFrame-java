/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 数据类
 */
public class NFData extends NFIData {
	
	private Type type = Type.UNKNOWN;
	private long value;
	private Object obj;
	
	public NFData(long var){
		set(var);
	}
	
	public NFData(double var){
		set(var);
	}
	
	public NFData(String var){
		set(var);
	}
	
	public NFData(NFIdent var){
		set(var);
	}
	
	public NFData(NFIData other){
		set(other);
	}

	@Override
	public void set(long var){
		this.type = Type.INT;
		this.value = var;
		this.obj = null;
	}

	@Override
	public void set(double var){
		this.type = Type.FLOAT;
		this.value = Double.doubleToLongBits(var);
		this.obj = null;
	}

	@Override
	public void set(String var){
		this.type = Type.STRING;
		this.value = 0;
		this.obj = var;
	}

	@Override
	public void set(NFIdent var){
		this.type = Type.OBJECT;
		this.value = 0;
		this.obj = var;
	}

	@Override
	public void set(NFIData other){
		this.type = other.getType();
		switch (this.type){
		case INT:{
			this.value = other.getInt();
			this.obj = null;
			break;
		}case FLOAT:{
			this.value = Double.doubleToLongBits(other.getFloat());
			this.obj = null;
			break;
		}case STRING:{
			this.value = 0;
			this.obj = other.getString();
			break;
		}case OBJECT:{
			this.value = 0;
			this.obj = other.getObject();
			break;
		}default:
			break;
		}
	}

	@Override
	public long getInt(){
		assert type == Type.INT;
		return value;
	}

	@Override
	public double getFloat(){
		assert type == Type.FLOAT;
		return Double.longBitsToDouble(value);
	}

	@Override
	public String getString(){
		assert type == Type.STRING;
		return (String) obj;
	}

	@Override
	public NFIdent getObject(){
		assert type == Type.OBJECT;
		return (NFIdent) obj;
	}

	@Override
	public boolean isEmpty(){
		return type == Type.UNKNOWN;
	}

	@Override
	public void clear(){
		type = Type.UNKNOWN;
		value = 0;
		obj = null;
	}

	@Override
	public Type getType(){
		return type;
	}

}
