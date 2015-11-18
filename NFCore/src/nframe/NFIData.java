/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 数据抽象类
 */
public interface NFIData {
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
	public void set(long var);
	public void set(double var);
	public void set(String var);
	public void set(NFGUID var);
	
	/**
	 * 从other拷贝数据
	 * @param other
	 */
	public void set(NFIData other);
	
	public long getInt();
	public double getFloat();
	public String getString();
	public NFGUID getObject();
	
	public boolean isNull();
	public void dispose();
	public Type getType();
}