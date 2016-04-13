package nframe.core;

import java.util.ArrayList;
import java.util.List;

import nframe.pluginmodule.NFGUID;

public class NFDataList implements NFIDataList
{

	private List<Object> vars;

	public NFDataList()
	{
		this.vars = new ArrayList<Object>();
	}

	public NFDataList(NFIDataList other)
	{
		this.vars = new ArrayList<Object>(other.size());
		this.set(other);
	}

	public NFDataList(long var)
	{
		this.vars = new ArrayList<Object>(1);
		this.add(var);
	}

	public NFDataList(double var)
	{
		this.vars = new ArrayList<Object>(1);
		this.add(var);
	}

	public NFDataList(String var)
	{
		this.vars = new ArrayList<Object>(1);
		this.add(var);
	}

	public NFDataList(NFGUID var)
	{
		this.vars = new ArrayList<Object>(1);
		this.add(var);
	}

	public NFDataList(Object... vars)
	{
		this.vars = new ArrayList<Object>(vars.length);
		this.append(vars);
	}

	@Override
	public boolean add(long var)
	{
		this.addVar(var);
		return true;
	}

	@Override
	public boolean add(double var)
	{
		this.addVar(var);
		return true;
	}

	@Override
	public boolean add(String var)
	{
		if (var == null)
		{
			return false;
		}
		this.addVar(var);
		return true;
	}

	@Override
	public boolean add(NFGUID var)
	{
		if (var == null)
		{
			return false;
		}
		this.addVar(var);
		return true;
	}

	@Override
	public boolean add(NFIData var)
	{
		if (var == null)
		{
			return false;
		}
		switch (var.getType())
		{
			case LONG:
				add(var.getLong());
				return true;
			case DOUBLE:
				add(var.getDouble());
				return true;
			case OBJECT:
				add(var.getObject());
				return true;
			case STRING:
				add(var.getString());
				return true;
			default:
				return false;
		}

	}

	@Override
	public void append(Object... vars)
	{
		for (Object o : vars)
		{
			assert o != null;
			this.vars.add(o);
		}
	}

	@Override
	public boolean set(int index, long var)
	{
		return setVar(index, var);
	}

	@Override
	public boolean set(int index, double var)
	{
		return setVar(index, var);
	}

	@Override
	public boolean set(int index, String var)
	{
		if (var == null)
		{
			return false;
		}
		return setVar(index, var);
	}

	@Override
	public boolean set(int index, NFGUID var)
	{
		if (var == null)
		{
			return false;
		}
		return setVar(index, var);
	}

	@Override
	public void set(NFIDataList other)
	{
		assert other != null;
		if (other == null)
		{
			return;
		}
		if (this == other)
		{
			return;
		}

		this.clear();
		for (int i = 0, size = other.size(); i < size; ++i)
		{
			NFIData.Type type = other.getType(i);
			switch (type)
			{
				case LONG:
				{
					this.add(other.getInt(i));
					break;
				}
				case DOUBLE:
				{
					this.add(other.getFloat(i));
					break;
				}
				case STRING:
				{
					this.add(other.getString(i));
					break;
				}
				case OBJECT:
				{
					this.add(other.getObject(i));
					break;
				}
				default:
					assert false;
			}
		}
	}

	@Override
	public long getInt(int index)
	{
		Object o = getVar(index);
		if (o == null)
		{
			return 0;
		}
		if (o instanceof Byte)
		{
			return Byte.class.cast(o);
		}
		else if (o instanceof Short)
		{
			return Short.class.cast(o);
		}
		else if (o instanceof Integer)
		{
			return Integer.class.cast(o);
		}
		else
		{
			return Long.class.cast(o);
		}
	}

	@Override
	public double getFloat(int index)
	{
		Object o = getVar(index);
		if (o == null)
		{
			return 0;
		}
		if (o instanceof Float)
		{
			return Float.class.cast(o);
		}
		else
		{
			return Double.class.cast(o);
		}
	}

	@Override
	public String getString(int index)
	{
		Object o = getVar(index);
		if (o == null)
		{
			return "";
		}
		return String.class.cast(o);
	}

	@Override
	public NFGUID getObject(int index)
	{
		Object o = getVar(index);
		if (o == null)
		{
			return new NFGUID();
		}
		return NFGUID.class.cast(o);
	}

	@Override
	public int size()
	{
		return vars.size();
	}

	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}

	@Override
	public void clear()
	{
		vars.clear();
	}

	@Override
	public NFIData.Type getType(int index)
	{
		Object o = getVar(index);
		if (o == null)
		{
			return NFIData.Type.UNKNOW;
		}

		if (o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long)
		{
			return NFIData.Type.LONG;
		}
		else if (o instanceof Float || o instanceof Double)
		{
			return NFIData.Type.DOUBLE;
		}
		else if (o instanceof String)
		{
			return NFIData.Type.STRING;
		}
		else if (o instanceof NFGUID)
		{
			return NFIData.Type.OBJECT;
		}
		else
		{
			return NFIData.Type.UNKNOW;
		}
	}

	private int addVar(Object o)
	{
		int index = vars.size();
		vars.add(o);
		return index;
	}

	private boolean setVar(int index, Object o)
	{
		if (index < 0 || index >= vars.size())
		{
			return false;
		}
		vars.set(index, o);
		return true;
	}

	private Object getVar(int index)
	{
		if (index >= 0 && index < vars.size())
		{
			return vars.get(index);
		}
		return null;
	}
}
