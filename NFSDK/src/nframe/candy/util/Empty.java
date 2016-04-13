package nframe.candy.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author zhiyu.zhao
 * @Description: 
 *
 */
public class Empty
{

	public static boolean is(Object obj)
	{
		return obj == null;
	}

	public static boolean is(String str)
	{
		if (str == null || "".equals(str) || str.trim().length() == 0)
		{
			return true;
		}
		return false;
	}

	public static boolean is(Integer value)
	{
		if (value == null || value == 0)
		{
			return true;
		}
		return false;
	}

	public static boolean is(Long value)
	{
		if (value == null || value == 0)
		{
			return true;
		}
		return false;
	}

	public static boolean is(Float value)
	{
		if (value == null || value == 0)
		{
			return true;
		}
		return false;
	}

	public static boolean is(Double value)
	{
		if (value == null || value == 0)
		{
			return true;
		}
		return false;
	}

	public static <T> boolean is(List<T> list)
	{
		return list == null || list.size() == 0;
	}

	public static <K, V> boolean is(Map<K, V> map)
	{
		return map == null || map.size() == 0;
	}

	public static <T> boolean is(Set<T> set)
	{
		return set == null || set.size() == 0;
	}
}
