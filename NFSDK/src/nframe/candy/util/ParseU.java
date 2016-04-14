package nframe.candy.util;

/**
 * @author zhiyu.zhao
 * @Description:
 * 
 */
public class ParseU
{
	public static boolean tInt(String str)
	{
		try
		{
			if (!Empty.is(str))
			{
				Integer.parseInt(str);
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	public static boolean tLong(String str)
	{
		try
		{
			if (!Empty.is(str))
			{
				Long.parseLong(str);
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static int pInt(String str)
	{
		if (!Empty.is(str))
		{
			return Integer.parseInt(str);
		}
		return 0;
	}

	public static long pLong(String str)
	{
		if (!Empty.is(str))
		{
			return Long.parseLong(str);
		}
		return 0;
	}
	
	public static double pDouble(String str)
	{
		if (!Empty.is(str))
		{
			return Double.parseDouble(str);
		}
		return 0;
	}

	public static String pStr(Object str)
	{
		if (!Empty.is(str))
		{
			return String.valueOf(str);
		}
		return "";
	}

	public static boolean pBool(String str)
	{
		if (!Empty.is(str))
		{
			if (str.equals("1"))
			{
				return true;
			}
			return Boolean.parseBoolean(str);
		}
		return false;
	}

}
