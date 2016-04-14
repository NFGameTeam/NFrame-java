package nframe.core;

import java.util.HashMap;
import java.util.Iterator;

import nframe.candy.struct.Pair;

/**
 * @author zhiyu.zhao
 * @Description:
 * 
 */
public class NFMap<K, V>
{
	private HashMap<K, V> map = new HashMap<>();
	private Iterator<K> iter = null;

	public boolean addElement(K key, V value)
	{
		if (map.containsKey(key))
		{
			map.put(key, value);
			return true;
		}
		return false;
	}

	public V removeElement(K key)
	{
		return map.remove(key);
	}

	public V getElement(K key)
	{
		return map.get(key);
	}

	public V first()
	{
		if (map.size() > 0)
		{
			iter = map.keySet().iterator();
			if (iter.hasNext())
			{
				K key = iter.next();
				return map.get(key);
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
			K key = iter.next();
			return map.get(key);
		}
		return null;
	}

	public Pair<K, V> firstPair()
	{
		if (map.size() > 0)
		{
			iter = map.keySet().iterator();
			if (iter.hasNext())
			{
				K key = iter.next();
				V value = map.get(key);
				return Pair.makePair(key, value);
			}
		}
		return null;
	}

	public Pair<K, V> nextPair()
	{
		if (iter == null)
		{
			return null;
		}
		if (iter.hasNext())
		{
			K key = iter.next();
			V value = map.get(key);
			return Pair.makePair(key, value);
		}
		return null;
	}

	public int size()
	{
		return map.size();
	}

	public boolean clearAll()
	{
		iter = null;
		map.clear();
		return true;
	}

	public HashMap<K, V> getMap()
	{
		return map;
	}

	public void setMap(HashMap<K, V> map)
	{
		this.map = map;
	}

}
