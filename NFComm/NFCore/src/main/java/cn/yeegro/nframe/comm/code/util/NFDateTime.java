/**   
* @Title: NFDateTime
* @Package ${package_name} 
* @Description: 事件数据对象
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.util;

import cn.yeegro.nframe.comm.code.math.NFTimeSpan;

import java.util.Calendar;
import java.util.Date;


public class NFDateTime {
	
	
	///////////////////////////////
	//for datetime
	private static int SINCE_YEAR=               1900;

	// Summertime
	private static int SUMMERTIME_BEGIN_MONTH=      3;
	private static int SUMMERTIME_END_MONTH=       10;

	// Min and Max values
	private static int MIN_MONTH=                   1;
	private static int MAX_MONTH=                  12;

	private static int MIN_DAY=                     1;
	private static int MAX_DAY=                    30;

	private static int MIN_WEEKDAY=                 0;
	private static int MAX_WEEKDAY=                 7;

	private static int MIN_HOUR =                   0;
	private static int MAX_HOUR =                  24;

	private static int MIN_MINUTE =                0;
	private static int MAX_MINUTE =                60;

	private static int MIN_SECOND =                 0;
	private static int MAX_SECOND =                60;

	private static int MIN_MILLISECOND  =           0;
	private static int MAX_MILLISECOND  =        1000;
	

	int[] daysOfMonth=new int[13];
	String[] monthNames=new String[13];
	String[] dayNames=new String[8];

	int year;
	int month;
	int day;
	int hour;
	int minute;
	int second;
	int millisecond;

	boolean summertime;
	boolean leapyear;
	
	
	public enum Day
	{
		Monday(1),
		Thuesday(2),
		Wednesday(3),
		Thursday(4),
		Friday(5),
		Saturday(6),
		Sunday(7);
		private int _value;

		private Day(int value) {
			_value = value;
		}

		public int value() {
			return _value;
		}
		public static Day get(int ntype)
		{
			for (int i = 0; i < Day.values().length; i++) {
				Day val=Day.values()[i];
				if (val.value()==ntype) {
					return val;
				}
			}
			return null;
		}
	};
	public enum Month
	{
		January(1),
		February(2),
		March(3),
		April(4),
		May(5),
		June(6),
		July(7),
		August(8),
		September(9),
		October(10),
		November(11),
		December(12);
		private int _value;

		private Month(int value) {
			_value = value;
		}

		public int value() {
			return _value;
		}
		public static Month get(int ntype)
		{
			for (int i = 0; i < Month.values().length; i++) {
				Month val=Month.values()[i];
				if (val.value()==ntype) {
					return val;
				}
			}
			return null;
		}
	};
	
	public NFDateTime(int day, int month, int year)
	{
		Init(day, month, year, 0, 0, 0, 0);
	}

	public NFDateTime(int day, int month, int year, int hour, int minute, int second)
	{
		Init(day, month, year, hour, minute, second, 0);
	}

	public NFDateTime(int day, int month, int year, int hour, int minute, int second, int millisecond)
	{
		Init(day, month, year, hour, minute, second, millisecond);
	}

	public NFDateTime(Date timestamp)
	{
		SetWithTimestamp(timestamp);
	}
	
	public long GetTimestamp()
	{
		Date time = new Date();
		time.setYear(year - SINCE_YEAR);
		time.setMonth(month - 1);
		time.setDate(day);
		time.setHours(hour);
		time.setMinutes(minute);
		time.setSeconds(second);
		return time.getTime();
	}

	public long GetTMStruct()
	{
		Date time = new Date();
		time.setYear(year - SINCE_YEAR);
		time.setMonth(month - 1);
		time.setDate(day);
		time.setHours(hour);
		time.setMinutes(minute);
		time.setSeconds(second);
		return time.getTime();
	}
	
	public static NFDateTime Now()
	{
		NFDateTime dt=new NFDateTime();
		dt.SetNow();
		return dt;
	}
	
	public void SetNow()
	{
		SetWithTimestamp(new Date());
	}
	
	public int GetYear()  { return year; }
	public int GetMonth()  { return month; }
	public int GetDay()  { return day; }

	public int GetHour()  { return hour; }
	public int GetMinute()  { return minute; }
	public int GetSecond()  { return second; }
	public int GetMillisecond()  { return millisecond; }
	
	public void SetWithTimestamp(Date timestamp)
	{
		Init(timestamp.getDay(), timestamp.getMonth() + 1, timestamp.getYear() + SINCE_YEAR, timestamp.getHours(), timestamp.getMinutes(), timestamp.getSeconds(), 0);
	}
	
	public void Add(NFTimeSpan ts)
	{
		AddMilliseconds(ts.GetTotalMilliseconds());
	}

	public void AddYears(int years)
	{
		year += years;
	}

	public void AddMonths(int months)
	{
		AddYears(months / MAX_MONTH);
		month += months % MAX_MONTH;
	}

	public void AddDays(int days)
	{
		AddMonths(days / MAX_DAY);
		day += days % MAX_DAY;
	}

	public void AddHours(int hours)
	{
		AddDays(hours / MAX_HOUR);
		hour += hours % MAX_HOUR;
	}

	public void AddMinutes(int minutes)
	{
		AddHours(minutes / MAX_MINUTE);
		minute += minutes % MAX_MINUTE;
	}

	public void AddSeconds(int seconds)
	{
		AddMinutes(seconds / MAX_SECOND);
		second += seconds % MAX_SECOND;
	}

	public void AddMilliseconds(Long milliseconds)
	{
		AddSeconds((int)(milliseconds / MAX_MILLISECOND));
		millisecond += milliseconds % MAX_MILLISECOND;
	}

	public static boolean IsYearLeapYear(int year)
	{
		return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
	}

	public boolean IsSummertime() { return IsYearLeapYear(year); }
	public boolean IsLeapYear() { return IsDateSummertime(day, month); }

	public static boolean IsDateSummertime(int day, int month)
	{
		// FIXME: include day in calculation
		if (month >= SUMMERTIME_BEGIN_MONTH && month <= SUMMERTIME_END_MONTH)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String GetNameOfDay(int day)
	{
		if (IsValidWeekday(day))
		{
			return dayNames[day - 1];
		}
		else
		{
			try {
				throw new Exception("Day "+day+" is not in valid weekday range ( "+MIN_WEEKDAY+" - "+MAX_WEEKDAY+" )");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			//throw NFException("Day %d is not in valid weekday range ( %d - %d )", day, MIN_WEEKDAY, MAX_WEEKDAY);
		}
	}

	public String GetNameOfMonth(int month)
	{
		if (IsValidMonth(month))
		{
			return monthNames[month - 1];
		}
		else
		{
			
			try {
				throw new Exception("Month "+month+" is not in valid range ( "+MIN_MONTH+" - "+MAX_MONTH+" )");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		}
	}

	public Day GetDayOfWeek()
	{
		Calendar c=Calendar.getInstance();
		c.setTime(new Date(GetTMStruct()));
		
		return Day.get(c.DAY_OF_WEEK);
				//static_cast<NFDateTime.Day>(c.DAY_OF_WEEK);
	}

	public int GetDayOfYear()
	{
		Calendar c=Calendar.getInstance();
		c.setTime(new Date(GetTMStruct()));
		return c.DAY_OF_YEAR;
				//GetTMStruct().tm_yday;
	}

	public int GetDaysOfMonth(int month)
	{
		return IsValidMonth(month) ? daysOfMonth[month] : -1;
	}
	
	boolean oper_lt( NFDateTime dt)
	{
		return GetTimestamp() < dt.GetTimestamp();
	}

	boolean oper_gt( NFDateTime dt)
	{
		return GetTimestamp() > dt.GetTimestamp();
	}

	boolean oper_lthq( NFDateTime dt)
	{
		return GetTimestamp() <= dt.GetTimestamp();
	}

	boolean oper_gthq( NFDateTime dt)
	{
		return GetTimestamp() >= dt.GetTimestamp();
	}

	boolean oper_heq( NFDateTime dt)
	{
		return GetTimestamp() == dt.GetTimestamp();
	}

	NFDateTime oper_add( NFTimeSpan ts)
	{
		NFDateTime tmp = this;
		tmp.Add(ts);
		return tmp;
	}

	void oper_addeq( NFTimeSpan ts)
	{
		Add(ts);
	}


	String GetAsString()
	{
		return GetShortDateString() + new String(" - ") + GetShortTimeString();
	}

	String GetShortTimeString()
	{
		StringBuffer ss=new StringBuffer();
		ss.append(hour);
		ss.append(":");
		ss.append(minute);
		ss.append(":");
		ss.append(second);
		
		return ss.toString();
	}

	String GetLongTimeString()
	{
		
		StringBuffer ss=new StringBuffer();
		ss.append(hour);
		ss.append(":");
		ss.append(minute);
		ss.append(":");
		ss.append(second);
		ss.append(":");
		ss.append(millisecond);
		
		return ss.toString();
		
//		Stringstream ss(Stringstream.in | Stringstream.out);
//		ss << std.setfill('0') << std.setw(2) << hour << ":" << std.setw(2) << minute << ":" << std.setw(2) << second << ":" << std.setw(2) << millisecond;
//		return ss.str();
	}

	String GetShortDateString()
	{
		
		StringBuffer ss=new StringBuffer();
		ss.append(day);
		ss.append(".");
		ss.append(month);
		ss.append(".");
		ss.append(year);
		
		return ss.toString();
		
//		Stringstream ss(Stringstream.in | Stringstream.out);
//		ss << std.setfill('0') << std.setw(2) << day << "." << std.setw(2) << month << "." << year;
//		return ss.str();
	}

	String GetLongDateString()
	{
		
		StringBuffer ss=new StringBuffer();
		ss.append(GetNameOfDay(GetDayOfWeek().value()));
		ss.append(",");
		ss.append(GetNameOfMonth(month));
		ss.append(" ");
		ss.append(day);
		ss.append(",");
		ss.append(year);
		
		return ss.toString();
		
//		Stringstream ss(Stringstream.in | Stringstream.out);
//		ss << GetNameOfDay(GetDayOfWeek()) << ", " << GetNameOfMonth(month) << " " << day << ", " << year;
//		return ss.str();
	}
	
	public NFDateTime()
	{
		this.day = 0;
		this.month = 0;
		this.year = 0;

		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.millisecond = millisecond;

		InitMonths();
		InitMonthNames();
		InitDayNames();
	}

	void Init(int day, int month, int year, int hour, int minute, int second, int millisecond)
	{
		this.year = year;

		InitMonths();
		InitMonthNames();
		InitDayNames();

		if (!IsValidMonth(month))
		{
			
			try {
				throw new Exception("Month "+month+" is not in range");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//throw NFException("Month %d is not in range", month);
		}

		if (!IsValidDayOfMonth(day, month))
		{
			try {
				throw new Exception("Day "+day+" is not in month "+month+"'s range");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//throw NFException("Day %d is not in month %d's range", day, month);
		}

		if (!IsValidHour(hour))
		{
			try {
				throw new Exception("Hour "+hour+" is not in valid range ( "+MIN_HOUR+" - "+MAX_HOUR+" )");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//throw NFException("Hour %d is not in valid range ( %d - %d )", hour, MIN_HOUR, MAX_HOUR);
		}

		if (!IsValidMinute(minute))
		{
			
			try {
				throw new Exception("Minute "+minute+" is not in valid range ( "+MIN_MINUTE+" - "+MAX_MINUTE+" )");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//throw NFException("Minute %d is not in valid range ( %d - %d )", minute, MIN_MINUTE, MAX_MINUTE);
		}

		if (!IsValidSecond(second))
		{
			try {
				throw new Exception("Second "+second+" is not in valid range ( "+MIN_SECOND+" - "+MAX_SECOND+" )");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//throw NFException("Second %d is not in valid range ( %d - %d )", second, MIN_SECOND, MAX_SECOND);
		}

		if (!IsValidMillisecond(millisecond))
		{
			try {
				throw new Exception("Millisecond "+millisecond+" is not in valid range ( "+MIN_MILLISECOND+" - "+MAX_MILLISECOND+" )");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//throw NFException("Millisecond %d is not in valid range ( %d - %d )", millisecond, MIN_MILLISECOND, MAX_MILLISECOND);
		}

		this.day = day;
		this.month = month;

		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.millisecond = millisecond;
	}

	void InitMonths()
	{
		// Perhaps an algorithm would be more efficient
		daysOfMonth[Month.January.value()] = 31;
		daysOfMonth[Month.February.value()] = IsLeapYear() ? 29 : 28; // In a leapyear 29 else 28
		daysOfMonth[Month.March.value()] = 31;
		daysOfMonth[Month.April.value()] = 30;
		daysOfMonth[Month.May.value()] = 31;
		daysOfMonth[Month.June.value()] = 30;
		daysOfMonth[Month.July.value()] = 31;
		daysOfMonth[Month.August.value()] = 31;
		daysOfMonth[Month.September.value()] = 30;
		daysOfMonth[Month.October.value()] = 31;
		daysOfMonth[Month.November.value()] = 30;
		daysOfMonth[Month.December.value()] = 31;
	}

	void InitMonthNames()
	{
		monthNames[Month.January.value()] = "January";
		monthNames[Month.February.value()] = "February";
		monthNames[Month.March.value()] = "March";
		monthNames[Month.April.value()] = "April";
		monthNames[Month.May.value()] = "May";
		monthNames[Month.June.value()] = "June";
		monthNames[Month.July.value()] = "July";
		monthNames[Month.August.value()] = "August";
		monthNames[Month.September.value()] = "September";
		monthNames[Month.October.value()] = "October";
		monthNames[Month.November.value()] = "November";
		monthNames[Month.December.value()] = "December";
	}

	void InitDayNames()
	{
		dayNames[Day.Monday.value()] = "Monday";
		dayNames[Day.Thuesday.value()] = "Thuesday";
		dayNames[Day.Wednesday.value()] = "Wednesday";
		dayNames[Day.Thursday.value()] = "Thursday";
		dayNames[Day.Friday.value()] = "Friday";
		dayNames[Day.Saturday.value()] = "Saturday";
		dayNames[Day.Sunday.value()] = "Sunday";
	}

	boolean IsValidWeekday(int day)
	{
		return day >= MIN_WEEKDAY && day <= MAX_WEEKDAY;
	}

	boolean IsValidDayOfMonth(int day, int month)
	{
		if (IsValidMonth(month))
		{
			return day >= 1 && day <= GetDaysOfMonth(month);
		}
		else
		{
			return false;
		}
	}

	boolean IsValidMonth(int month)
	{
		return month >= MIN_MONTH && month <= MAX_MONTH;
	}

	boolean IsValidYear(int year)
	{
		return year >= 0;
	}

	boolean IsValidHour(int hour)
	{
		return hour >= MIN_HOUR && hour <= MAX_HOUR;
	}

	boolean IsValidMinute(int minute)
	{
		return minute >= MIN_MINUTE && minute <= MAX_MINUTE;
	}

	boolean IsValidSecond(int second)
	{
		return second >= MIN_SECOND && second <= MAX_SECOND;
	}

	boolean IsValidMillisecond(int millisecond)
	{
		return millisecond >= MIN_MILLISECOND && millisecond <= MAX_MILLISECOND;
	}
	
}
