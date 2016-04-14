package nframe.core;

import java.util.List;
import java.util.function.Function;

import nframe.candy.util.ParseU;
import nframe.pluginmodule.NFGUID;

public class NFCProperty implements NFIProperty
{

	private NFGUID self;
	private String name;
	private NFData.Type eType = NFData.Type.UNKNOW;
	private NFData data = new NFData();
	private boolean bPublic;
	private boolean bPrivate;
	private boolean save;
	private boolean view;
	private int index;

	private List<Function<FunctionParam, Integer>> propertyCallback;

	public NFCProperty()
	{
		bPublic = false;
		bPrivate = false;
		save = false;
		self = new NFGUID();
		name = "";
	}

	public NFCProperty(NFGUID self, String strPropertyName, NFData.Type varType, boolean bPublic, boolean bPrivate, boolean bSave, boolean bView, int nIndex,
		String strScriptFunction)
	{

		this.bPublic = bPublic;
		this.bPrivate = bPrivate;
		save = bSave;
		view = bView;
		index = nIndex;
		this.self = self;

		name = strPropertyName;
		eType = varType;
	}
	
	public String getName()
	{
		return name;
	}

	public NFData getValue()
	{
		return data;
	}

	public void setValue(NFData data)
	{
		if (data == null)
		{
			return;
		}
		if (eType != data.getType())
		{
			return;
		}
		NFData oldValue = new NFData().clone(data);
		this.data = data;

		OnEventHandler(oldValue, data);
	}

	public void setValue(NFIProperty pProperty)
	{
		setValue(pProperty.getValue());
	}

	

	public void setScriptFunction(String strScriptFunction)
	{
		// msScriptFunction = strScriptFunction;
	}

	public long getLong()
	{
		if (data != null)
		{
			return data.getLong();
		}
		return 0L;
	}

	public double getDouble()
	{
		if (data != null)
		{
			return data.getDouble();
		}
		return 0.0;
	}

	public String getString()
	{
		if (data != null)
		{
			return data.getString();
		}
		return "";
	}

	public NFGUID getObject()
	{
		if (data != null)
		{
			return data.getObject();
		}
		return null;
	}

	public void registerCallback(Function<FunctionParam, Integer> function)
	{
		propertyCallback.add(function);
	}

	public int OnEventHandler(NFData oldVar, NFData newVar)
	{
		if (propertyCallback.size() > 0)
		{
			for (Function<FunctionParam, Integer> function : propertyCallback)
			{
				function.apply(new FunctionParam(self, name, oldVar, newVar));
			}
		}
		return 0;
	}

	public boolean setLong(long value)
	{
		if (eType != NFData.Type.LONG)
		{
			return false;
		}
		if (data == null && value == 0)
		{
			return false;
		}
		if (value == data.getLong())
		{
			return false;
		}

		NFData oldValue = new NFData(data);
		data.setLong(value);

		OnEventHandler(oldValue, data);

		return true;
	}

	public boolean setDouble(double value)
	{
		if (eType != NFData.Type.DOUBLE)
		{
			return false;
		}

		if (data == null && value == 0)
		{
			return false;
		}
		if (value == data.getLong())
		{
			return false;
		}

		NFData oldValue = new NFData(data);
		data.setDouble(value);

		OnEventHandler(oldValue, data);

		return true;
	}

	public boolean setString(String value)
	{
		if (eType != NFData.Type.STRING)
		{
			return false;
		}
		if (value == null)
		{
			return false;
		}
		if (data.getString().equals(value))
		{
			return false;
		}

		NFData oldValue = new NFData(data);
		data.setString(value);

		OnEventHandler(oldValue, data);

		return true;
	}

	public boolean setObject(NFGUID value)
	{
		if (eType != NFData.Type.OBJECT)
		{
			return false;
		}
		if (value == null)
		{
			return false;
		}
		if (data.getObject().equals(value))
		{
			return false;
		}

		NFData oldValue = new NFData(data);
		data.setObject(value);

		OnEventHandler(oldValue, data);

		return true;
	}

	public boolean changed()
	{
		return this.getValue().isNullValue();
	}

	public boolean geUsed()
	{
		return this.geteType() != NFData.Type.UNKNOW;
	}

	public String toString()
	{
		String strData = "";
		NFData.Type eType = this.geteType();
		switch (eType)
		{
			case LONG:
				strData = String.valueOf(this.getLong());
				break;

			case DOUBLE:
				strData = String.valueOf(this.getDouble());
				break;

			case STRING:
				strData = this.getString();
				break;
			case OBJECT:
				strData = this.getObject().toString();
				break;
			default:
				break;
		}

		return strData;
	}

	public boolean fromString(String strData)
	{
		NFData.Type eType = this.geteType();
		boolean bRet = false;
		switch (eType)
		{
			case LONG:
			{
				this.setLong(ParseU.pLong(strData));
				bRet = true;
				break;
			}

			case DOUBLE:
			{
				this.setDouble(ParseU.pDouble(strData));
				bRet = true;
				break;
			}

			case STRING:
			{
				this.setString(strData);
				bRet = true;
				break;
			}

			case OBJECT:
			{
				NFGUID xID = new NFGUID();
				bRet = xID.fromString(strData);
				this.setObject(xID);
				bRet = true;
			}
			default:
				break;
		}

		return bRet;
	}

	public NFGUID getSelf()
	{
		return self;
	}

	public void setSelf(NFGUID self)
	{
		this.self = self;
	}

	public NFData.Type geteType()
	{
		return eType;
	}

	public void seteType(NFData.Type eType)
	{
		this.eType = eType;
	}

	public NFData getData()
	{
		return data;
	}

	public void setData(NFData data)
	{
		this.data = data;
	}

	public boolean isbPublic()
	{
		return bPublic;
	}

	public void setbPublic(boolean bPublic)
	{
		this.bPublic = bPublic;
	}

	public boolean isbPrivate()
	{
		return bPrivate;
	}

	public void setbPrivate(boolean bPrivate)
	{
		this.bPrivate = bPrivate;
	}

	public boolean isSave()
	{
		return save;
	}

	public void setSave(boolean save)
	{
		this.save = save;
	}

	public boolean isView()
	{
		return view;
	}

	public void setView(boolean view)
	{
		this.view = view;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public List<Function<FunctionParam, Integer>> getPropertyCallback()
	{
		return propertyCallback;
	}

	public void setPropertyCallback(List<Function<FunctionParam, Integer>> propertyCallback)
	{
		this.propertyCallback = propertyCallback;
	}

	public void setName(String name)
	{
		this.name = name;
	}


}
