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
	
	public abstract void set(long var);
	public abstract void set(double var);
	public abstract void set(String var);
	public abstract void set(NFIdent var);
	
	/**
	 * 从other拷贝数据
	 * @param other
	 */
	public abstract void set(NFIData other);
	
	public abstract long getInt();
	public abstract double getFloat();
	public abstract String getString();
	public abstract NFIdent getObject();
	
	public abstract boolean isNull();
	public abstract void dispose();
	public abstract Type getType();
}
