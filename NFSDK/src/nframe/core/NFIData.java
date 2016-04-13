package nframe.core;

import nframe.pluginmodule.NFGUID;

/**
 * 
 * @author zhiyu.zhao
 * @Description:
 *
 */
public interface NFIData
{

	public enum Type
	{
		UNKNOW, LONG, // byte,short,int,long
		DOUBLE, // float,double
		STRING, OBJECT, // NFIdent
	}

	public void setLong(long var);

	public void setDouble(double var);

	public void setString(String var);

	public void setObject(NFGUID var);

	public long getLong();

	public double getDouble();

	public String getString();

	public NFGUID getObject();

	public boolean isNullValue();

	public void reset();

	public Type getType();

	public void copyFrom(NFIData other);

}
