/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 数据类
 */
public class NFData implements NFIData {
	
	private Type type;
	private long value;
	private Object obj;
	
	public NFData(){
		this.type = Type.UNKNOW;
	}
	
	public NFData(long var){
		this.type = Type.INT;
		set(var);
	}
	
	public NFData(double var){
		this.type = Type.FLOAT;
		set(var);
	}
	
	public NFData(String var){
		this.type = Type.STRING;
		set(var);
	}
	
	public NFData(NFGUID var){
		this.type = Type.OBJECT;
		set(var);
	}
	
	public NFData(NFIData other){
		set(other);
	}

	@Override
	public void set(long var){
		assert this.type == Type.INT;
		this.type = Type.INT;
		this.value = var;
		this.obj = null;
	}

	@Override
	public void set(double var){
		assert this.type == Type.FLOAT;
		this.type = Type.FLOAT;
		this.value = Double.doubleToLongBits(var);
		this.obj = null;
	}

	@Override
	public void set(String var){
		assert this.type == Type.STRING;
		assert var != null;
		this.type = Type.STRING;
		this.value = 0;
		this.obj = var;
	}

	@Override
	public void set(NFGUID var){
		assert var != null;
		assert this.type == Type.OBJECT;
		this.type = Type.OBJECT;
		this.value = 0;
		this.obj = var;
	}

	@Override
	public void set(NFIData other){
		assert other != null;
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
	public NFGUID getObject(){
		assert type == Type.OBJECT;
		return (NFGUID) obj;
	}

	@Override
	public boolean isNull(){
		return type == Type.UNKNOW;
	}

	@Override
	public void dispose(){
		type = Type.UNKNOW;
		value = 0;
		obj = null;
	}

	@Override
	public Type getType(){
		return type;
	}

}
