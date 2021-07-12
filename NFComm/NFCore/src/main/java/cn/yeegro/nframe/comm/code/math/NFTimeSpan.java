/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.math;

public class NFTimeSpan {

	//for timespan
	public static int FACTOR_SEC_TO_MILLI=                     1000;
	public static int FACTOR_MIN_TO_MILLI=                60 * 1000;
	public static int FACTOR_MIN_TO_SEC=                         60;
	public static int FACTOR_HOUR_TO_MILLI=          60 * 60 * 1000;
	public static int FACTOR_HOUR_TO_MIN =                       60;
	public static int FACTOR_DAY_TO_MILLI=      24 * 60 * 60 * 1000;
	public static int FACTOR_DAY_TO_HOUR=                        24;
	
	
	int milliseconds;
	int seconds;
	int minutes;
	int hours;
	int days;

	NFTimeSpan(int seconds)
	{
		Init(0, 0, 0, seconds, 0);
	}

	NFTimeSpan(int hours, int minutes, int seconds)
	{
		Init(0, hours, minutes, seconds, 0);
	}

	NFTimeSpan(int days, int hours, int minutes, int seconds)
	{
		Init(days, hours, minutes, seconds, 0);
	}

	NFTimeSpan(int days, int hours, int minutes, int seconds, int milliseconds)
	{
		Init(days, hours, minutes, seconds, milliseconds);
	}
	
	static NFTimeSpan FromMilliseconds(int milliseconds)
	{
		return new NFTimeSpan(0, 0, 0, 0, milliseconds);
	}

	static NFTimeSpan FromSeconds(int seconds)
	{
		return new NFTimeSpan(0, 0, 0, seconds, 0);
	}

	static NFTimeSpan FromMinutes(int minutes)
	{
		return new NFTimeSpan(0, 0, minutes, 0, 0);
	}

	static NFTimeSpan FromHours(int hours)
	{
		return new NFTimeSpan(0, hours, 0, 0, 0);
	}

	static NFTimeSpan FromDays(int days)
	{
		return new NFTimeSpan(days, 0, 0, 0, 0);
	}

	int GetMilliseconds()  { return milliseconds; }
	int GetSeconds()  { return seconds; }
	int GetMinutes()  { return minutes; }
	int GetHours()  { return hours; }
	int GetDays()  { return days; }

	public long GetTotalMilliseconds()
	{
		return milliseconds + seconds * FACTOR_SEC_TO_MILLI + minutes * FACTOR_MIN_TO_MILLI + hours * FACTOR_HOUR_TO_MILLI + days * FACTOR_DAY_TO_MILLI;
	}

	double GetTotalSeconds() 
	{
		return GetTotalMilliseconds() / FACTOR_SEC_TO_MILLI;
	}

	double GetTotalMinutes() 
	{
		return GetTotalSeconds() / FACTOR_MIN_TO_SEC;
	}

	double GetTotalHours() 
	{
		return GetTotalMinutes() / FACTOR_HOUR_TO_MIN;
	}

	double GetTotalDays() 
	{
		return GetTotalHours() / FACTOR_DAY_TO_HOUR;
	}

	boolean oper_lt( NFTimeSpan ts)
	{
		return GetTotalMilliseconds() < ts.GetTotalMilliseconds();
	}

	boolean oper_gt( NFTimeSpan ts)
	{
		return GetTotalMilliseconds() > ts.GetTotalMilliseconds();
	}

	boolean oper_lteq( NFTimeSpan ts)
	{
		return GetTotalMilliseconds() <= ts.GetTotalMilliseconds();
	}

	boolean oper_gteq( NFTimeSpan ts)
	{
		return GetTotalMilliseconds() >= ts.GetTotalMilliseconds();
	}

	boolean oper_heq( NFTimeSpan ts)
	{
		return GetTotalMilliseconds() == ts.GetTotalMilliseconds();
	}

	void Init(int days, int hours, int minutes, int seconds, int milliseconds)
	{
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliseconds = milliseconds;
	}
}
