package nframe.core;

import nframe.candy.util.Empty;
import nframe.pluginmodule.NFGUID;

/**
 * 
 * @author zhiyu.zhao
 * @Description:
 * method in this Class never return null!
 *
 */
public class NFData
{
	public enum Type
	{
		UNKNOW, LONG, DOUBLE, STRING, OBJECT,
	}

	private Type type = Type.UNKNOW;;
	private long value = 0;
	private Object obj = null;

	public NFData()
	{
	}

	public NFData(long var)
	{
		this.type = Type.LONG;
		setLong(var);
	}

	public NFData(double var)
	{
		this.type = Type.DOUBLE;
		setDouble(var);
	}

	public NFData(String var)
	{
		this.type = Type.STRING;
		setString(var);
	}

	public NFData(NFGUID var)
	{
		this.type = Type.OBJECT;
		setObject(var);
	}

	public NFData(NFData other)
	{
		clone(other);
	}

	public void setLong(long var)
	{
		if (this.type == Type.LONG || this.type == Type.UNKNOW)
		{
			this.type = Type.LONG;
			this.value = var;
			this.obj = null;
		}
	}

	public void setDouble(double var)
	{
		if (this.type == Type.DOUBLE || this.type == Type.UNKNOW)
		{
			this.type = Type.DOUBLE;
			this.value = Double.doubleToLongBits(var);
			this.obj = null;
		}
	}

	public void setString(String var)
	{
		if (var == null)
		{
			return;
		}
		if (this.type == Type.STRING || this.type == Type.UNKNOW)
		{

			this.type = Type.STRING;
			this.value = 0;
			this.obj = var;
		}
	}

	public void setObject(NFGUID var)
	{
		if (var == null)
		{
			return;
		}
		if (this.type == Type.OBJECT || this.type == Type.UNKNOW)
		{

			this.type = Type.OBJECT;
			this.value = 0;
			this.obj = var;
		}
	}

	public long getLong()
	{
		if (type == Type.LONG)
		{
			return value;
		}
		return 0;
	}

	public double getDouble()
	{
		if (type == Type.DOUBLE)
		{
			return Double.longBitsToDouble(value);
		}
		return 0;
	}

	public String getString()
	{
		if (type == Type.STRING)
		{
			return (String)obj;
		}
		return "";
	}

	public NFGUID getObject()
	{
		if (type == Type.OBJECT)
		{
			return (NFGUID)obj;
		}
		return new NFGUID();
	}

	public boolean isNullValue()
	{
		switch (type)
		{
			case LONG:
				return Empty.is(value);
			case DOUBLE:
				return Empty.is(this.getDouble());
			case STRING:
				return Empty.is(this.getString());
			case OBJECT:
				return this.getObject().isNull();
			default:
				return true;
		}
	}

	public void reset()
	{
		type = Type.UNKNOW;
		value = 0;
		obj = null;
	}

	public Type getType()
	{
		return type;
	}

	public NFData clone(NFData other)
	{
		if (other == null)
		{
			return this;
		}
		this.type = other.getType();
		switch (this.type)
		{
			case LONG:
			{
				this.value = other.getLong();
				this.obj = null;
				break;
			}
			case DOUBLE:
			{
				this.value = Double.doubleToLongBits(other.getDouble());
				this.obj = null;
				break;
			}
			case STRING:
			{
				this.value = 0;
				this.obj = other.getString();
				break;
			}
			case OBJECT:
			{
				this.value = 0;
				this.obj = other.getObject().clone();
				break;
			}
			default:
				break;
		}
		return this;
	}

}
