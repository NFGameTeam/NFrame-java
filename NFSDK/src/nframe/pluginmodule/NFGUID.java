package nframe.pluginmodule;

public class NFGUID
{
	private long head;
	private long data;

	public NFGUID()
	{
	}

	public NFGUID(long head, long data)
	{
		this.setHead(head);
		this.setData(data);
	}

	public NFGUID clone()
	{
		return new NFGUID(head, data);
	}

	public  boolean fromString(String strData)
	{
		String[] strList = strData.split("-", 0);
		if (strList.length != 2)
		{
			return false;
		}

		long head = 0;
		long data = 0;
		try
		{
			head = Long.parseLong(strList[0]);
			data = Long.parseLong(strList[1]);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	public boolean isNull()
	{
		return 0 == data && 0 == head;
	}

	@Override
	public String toString()
	{
		return head + "-" + data;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}

		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		NFGUID rhs = (NFGUID)o;
		return this.getHead() == rhs.getHead() && this.getData() == rhs.getData();
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (int)(head ^ (head >>> 32));
		result = 37 * result + (int)(data ^ (data >>> 32));
		return result;
	}

	public long getHead()
	{
		return head;
	}

	public void setHead(long head)
	{
		this.head = head;
	}

	public long getData()
	{
		return data;
	}

	public void setData(long data)
	{
		this.data = data;
	}
}
