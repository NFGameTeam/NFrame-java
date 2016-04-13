package nframe.core;

import nframe.pluginmodule.NFGUID;

public interface NFIDataList
{

	public boolean add(long var);

	public boolean add(double var);

	public boolean add(String var);

	public boolean add(NFGUID var);

	public boolean add(NFIData var);

	public void append(Object... vars);

	public boolean set(int index, long var);

	public boolean set(int index, double var);

	public boolean set(int index, String var);

	public boolean set(int index, NFGUID var);

	public void set(NFIDataList other);

	public long getInt(int index);

	public double getFloat(int index);

	public String getString(int index);

	public NFGUID getObject(int index);

	public int size();

	public boolean isEmpty();

	public void clear();

	public NFIData.Type getType(int index);
}
