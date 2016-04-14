package nframe.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nframe.candy.struct.Pair;

/**
 * @author zhiyu.zhao
 * @Description:
 * 
 */
public class NFList<V>
{
	private ArrayList<V> list = new ArrayList<>();
	private Iterator<V> iter = null;

	public boolean add(V value)
	{
		list.add(value);
		return true;
	}

	public boolean remove(V v)
	{
		return list.remove(v);
	}

	public boolean find(V value)
	{
		return list.contains(value);
	}

	public V get(int index)
	{
		return list.get(index);
	}

	public V first()
	{
		if (list.size() > 0)
		{
			iter = list.iterator();
			if (iter.hasNext())
			{
				return iter.next();
			}
		}
		return null;
	}

	public V next()
	{
		if (iter == null)
		{
			return null;
		}
		if (iter.hasNext())
		{
			return iter.next();
		}
		return null;
	}

	public int size()
	{
		return list.size();
	}

	public boolean clearAll()
	{
		iter = null;
		list.clear();
		return true;
	}

}
