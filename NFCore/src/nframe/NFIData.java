/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 数据抽象类
 */
public abstract class NFIData {
	/** 数据类型 */
	public enum Type {
		NULL,
		INT, // byte,short,int,long
		FLOAT, // float,double
		STRING,
		OBJECT, // NFIdent
	}
	
	/** 数据默认值 */
	public static final long INT_NIL = 0;
	public static final double FLOAT_NIL = 0.0d;
	public static final String STRING_NIL = "";
	public static final NFGUID OBJECT_NIL = new NFGUID();
	
	/** 设置值 */
	public abstract void set(long var);
	public abstract void set(double var);
	public abstract void set(String var);
	public abstract void set(NFGUID var);
	
	/**
	 * 从other拷贝数据
	 * @param other
	 */
	public abstract void set(NFIData other);
	
	public abstract long getInt();
	public abstract double getFloat();
	public abstract String getString();
	public abstract NFGUID getObject();
	
	public abstract boolean isNull();
	public abstract void dispose();
	public abstract Type getType();
}
