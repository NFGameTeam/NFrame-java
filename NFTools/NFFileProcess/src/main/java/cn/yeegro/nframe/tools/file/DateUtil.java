package cn.yeegro.nframe.tools.file;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className:DateUitl.java
 * @classDescription:
 * @author:weironglang
 * @createTime:2014-8-5 上午9:25:46
 */
public class DateUtil {

	/**
	 * 日期格式化yyyy-MM-dd HH:mm:ss
	 */
	public static final DateFormat DATE_TIME_FORMATER = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 日期格式化yyyy-MM-dd
	 */
	public static final DateFormat DATE_FORMATER = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 日期格式化yyyyMMdd
	 */
	public static final DateFormat DATE_FORMATER1 = new SimpleDateFormat(
			"yyyyMMdd");

	public static DateFormat DATE_FORMATER2 = new SimpleDateFormat("MM月dd日");

	/**
	 * 时间格式化HH:mm:ss
	 */
	public static final DateFormat TIME_FORMATER = new SimpleDateFormat(
			"HH:mm:ss");

	/**
	 * 月份格式化yyyyMM
	 */
	public static final DateFormat MONTH_FORMATER = new SimpleDateFormat(
			"yyyyMM");

	public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final SimpleDateFormat fmt1 = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public static final SimpleDateFormat fmt2 = new SimpleDateFormat(
			"yyMMddHHmmss");

	public static final SimpleDateFormat fmt3 = new SimpleDateFormat(
			"yyMMddHHmmssSSS");

	public static final SimpleDateFormat fmt4 = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss");

	public static Date str2date(String str, String format) {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(format)) {
			return null;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(str);
		} catch (ParseException e) {
		}
		return null;
	}

	public static String date2str(Date date, String format) {
		if (null == date || StringUtils.isBlank(format)) {
			return "";
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} catch (Exception e) {
		}
		return "";
	}

	public static String yyyyMMddHHmmss(Date dt) {
		return fmt1.format(dt);
	}

	public static String getCurTime_yyyyMMddHHmmss() {
		return fmt1.format(new Date());
	}

	public static String getCurTime_yyMMddHHmmss() {
		return fmt2.format(new Date());
	}

	public static String getCurTime_yyMMddHHmmssSSS() {
		return fmt3.format(new Date());
	}

	public static long curTime() {
		return System.currentTimeMillis();
	}

	public static long getCurrentTime() {
		Date date = new Date();
		return date.getTime() / 1000;// 得到秒数，Date类型的getTime()返回毫秒数
	}

	// 字符串类型的日期转成时间戳
	public static long strDate2Long(String datetime, String format) {
		SimpleDateFormat sdf1 = null;
		if (StringUtils.isBlank(format)) {
			sdf1 = new SimpleDateFormat(DEFAULT_PATTERN);
		} else {
			sdf1 = new SimpleDateFormat(format);
		}
		Date dt = null;
		try {
			dt = sdf1.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dt.getTime() / 1000;// 得到秒数，Date类型的getTime()返回毫秒数
	}

	// 将时间戳转为字符串
	public static String long2str(Long times) {
		String re_StrTime = null;

		re_StrTime = DATE_TIME_FORMATER.format(new Date(times));

		return re_StrTime;

	}

	public static Date str2Date(String strDate) {
		String formater = null;
		if (strDate == null || StringUtils.isBlank(strDate)) {
			return null;
		}
		if (formater == null) {
			formater = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat df = new SimpleDateFormat(formater);
		Date date = new Date();
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			pe.getStackTrace();
		}
		return date;
	}

	public static Date str2Date(String strDate, String formater) {
		if (strDate == null) {
			return null;
		}
		if (formater == null) {
			formater = "yyyy-MM-dd";
		}
		SimpleDateFormat df = new SimpleDateFormat(formater);
		Date date = new Date();
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			pe.getStackTrace();
		}
		return date;
	}

	// 获取一天的时间：从00:00:00到23:59:59
	public static Map<String, String> getOneDayTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date start = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);

		Date end = calendar.getTime();

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Map<String, String> oneDayTime = new HashMap<String, String>();

		oneDayTime.put("sTime", sdf1.format(start));
		oneDayTime.put("eTime", sdf1.format(end));

		return oneDayTime;
	}

	// 获取指定一天的时间：从00:00:00到23:59:59
	public static Map<String, String> getOneDayTime(String sDate) {

		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formater.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date start = null;
		Date end = null;
		try {
			start = formater2.parse(formater.format(date) + " 00:00:00");
			end = formater2.parse(formater.format(date) + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Map<String, String> oneDayTime = new HashMap<String, String>();

		oneDayTime.put("sTime", formater2.format(start));
		oneDayTime.put("eTime", formater2.format(end));

		return oneDayTime;
	}

	/**
	 * 给指定日期增加days天
	 * 
	 * @param basic
	 * @param days
	 * @return
	 */
	public static Date addDays(Date basic, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(basic);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	/**
	 * 给指定日期增加秒数
	 *
	 * @param basic
	 * @param seconds
	 * @return
	 */
	public static Date addSecond(Date basic, int seconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(basic);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
	}

	/**
	 * 返回当前日期时间
	 * 
	 * @return 例如：2006-06-06 12:12:50
	 */
	public static String getCurDateTime() {
		return getCurDateTime(DEFAULT_PATTERN);
	}

	/**
	 * 根据给定的格式返回当前日期或时间 相当于调用getDateTime(formatStr,Calendar.getInstance()
	 * 
	 * @param formatStr
	 *            日期时间格式 例如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurDateTime(String formatStr) {
		return getDateTime(formatStr, Calendar.getInstance());
	}

	/**
	 * 根据给定的格式、Calendar返回相应字符串
	 * 
	 * @param formatStr
	 *            日期时间格式 例如：yyyy-MM-dd HH:mm:ss
	 * @param c
	 *            Calendar实例
	 * @return
	 */
	public static String getDateTime(String formatStr, Calendar c) {
		SimpleDateFormat nowDate = new SimpleDateFormat(formatStr);
		String curTimeStr = nowDate.format(c.getTime());

		return curTimeStr;
	}

	public static String getDateTime(String formatStr, Date c) {
		SimpleDateFormat nowDate = new SimpleDateFormat(formatStr);
		String curTimeStr = nowDate.format(c.getTime());

		return curTimeStr;
	}

	public static String getDateTime(String formatStr, long time) {
		SimpleDateFormat nowDate = new SimpleDateFormat(formatStr);
		String curTimeStr = nowDate.format(time);

		return curTimeStr;
	}

	/**
	 * 把已经格式好的美国日期转换成指定格式的中国日期
	 * 
	 * @param usFormattedString
	 *            已经格式好的美国日期 例如"02 May 2007"
	 * @param originalPattern
	 *            原始美国日期格式格式 例如"dd MMMMM yyyy"
	 * @param newPattern
	 *            要转换成的中国日期格式 例如"yyyy-MM-dd"
	 * @return 转换后的中国日期格式，如果解析异常即返回原美国日期 此例返回"2007-05-02"
	 */
	public static String usFormat2Ch(String usFormattedString,
                                     String originalPattern, String newPattern) {
		SimpleDateFormat chFormat = new SimpleDateFormat(newPattern,
				Locale.CHINA);
		SimpleDateFormat usFormat = new SimpleDateFormat(originalPattern,
				Locale.US);
		String convertedString;
		try {
			Date d = usFormat.parse(usFormattedString);
			convertedString = chFormat.format(d);
		} catch (ParseException e) {
			convertedString = usFormattedString;
		}
		return convertedString;
	}

	/**
	 * 把给定的日期字符串解析成对应的Calendar对象
	 * 
	 * @param dateTimeString
	 *            "2007-07-25 12:35:01"
	 * @return
	 */
	public static Calendar parse(String dateTimeString) {
		return parse(DEFAULT_PATTERN, dateTimeString);
	}

	/**
	 * 按照指定格式解析对应Calendar对象
	 * 
	 * @param pattern
	 * @param dateTimeString
	 * @return
	 */
	public static Calendar parse(String pattern, String dateTimeString) {
		DateFormat df = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(df.parse(dateTimeString));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return c;
	}

	/**
	 * 返回前天的日期字符串(当前是2007年3月27日，将返回2007-03-25)
	 * 
	 * @return
	 */
	public static String getDayBeforeYesterday() {
		return getBeforeToday(2);
	}

	/**
	 * 返回今天之前几天的日期字符串(当前是2007年3月27日，nDays=5,将返回2007-03-22)
	 * 
	 * @param nDays
	 *            前几天(不大于365,不小于0)
	 * @return
	 */
	public static String getBeforeToday(int nDays) {
		if (nDays < 0) {
			nDays = 0;
		}
		if (nDays > 365) {
			nDays = 365;
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - nDays);
		SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr = nowDate.format(c.getTime());

		return timeStr;
	}

	/**
	 * 获取当月之前nMonths的年月
	 * 
	 * @param nMonths
	 *            2
	 * @return 如果当前年月为2007年12月，则返回2007-10
	 */
	public static String getBeforeMonth(int nMonths) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) - nMonths);
		SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM");
		String timeStr = nowDate.format(c.getTime());

		return timeStr;
	}

	/**
	 * 是否为每月的第一天
	 * 
	 * @return true of false
	 */
	public static boolean isFirstDayOfMonth() {
		Calendar c = Calendar.getInstance();

		return c.get(Calendar.DAY_OF_MONTH) == 1;
	}

	/**
	 * 获取当前日是当前月的第几天
	 * 
	 * @return
	 */
	public static int getCurDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回当前年月的第一天第一秒的Date对象
	 * 
	 * @return
	 */
	public static Date getFirstSecondOfCurMonth() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		return c.getTime();
	}

	/**
	 * 返回明天的第一秒的Date对象
	 * 
	 * @return
	 */
	public static Date getFirstSecondOfTomorrow() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		return c.getTime();
	}

	/**
	 * 返回明天第一秒的毫秒值
	 * 
	 * @return
	 */
	public static long tomorrowFirstSecondMillis() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}

	/**
	 * 返回当天的第一秒的Date对象
	 * 
	 * @return
	 */
	public static Date getFirstSecondOfToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间至指定钟点数0分0秒时的间隔毫秒数 如果当前钟点大于指定钟点数，则结果是当前时间至第二天指定钟点数0分0秒时的间隔毫秒数
	 * 
	 * @param taskHour
	 *            任务钟点数（5点etc.）
	 * @return
	 */
	public static long betweenTaskHourMillis(int taskHour) {
		return betweenTaskHourMillis(taskHour, 0);
	}

	/**
	 * 获取当前时间至指定时的点间隔毫秒数 如果当前钟点大于指定钟点数，则结果是当前时间至第二天指定时的间隔毫秒数
	 * 
	 * @param taskHour
	 * @param taskMiniute
	 * @return
	 */
	public static long betweenTaskHourMillis(int taskHour, int taskMiniute) {
		if (taskHour < 0) {
			taskHour = 0;
		}
		if (taskHour > 23) {
			taskHour = 23;
		}
		if (taskMiniute < 0) {
			taskMiniute = 0;
		}
		if (taskMiniute > 59) {
			taskMiniute = 59;
		}

		Calendar c = Calendar.getInstance();
		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		if (nowHour > taskHour
				|| (nowHour == taskHour && c.get(Calendar.MINUTE) >= taskMiniute)) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		c.set(Calendar.HOUR_OF_DAY, taskHour);
		c.set(Calendar.MINUTE, taskMiniute);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis() - System.currentTimeMillis();
	}

	/**
	 * 格式化日期时间字符串 如果长度不符就直接返回dateTimeString
	 * 
	 * @param dateTimeString
	 *            (070831123800)
	 * @return (07-08-31 12:38:00)
	 */
	public static String formatDateTime(String dateTimeString) {
		StringBuffer buffer = new StringBuffer(dateTimeString);
		if (buffer.length() != 12) {
			return dateTimeString;
		}
		for (int i = 1; i < 3; i++) {
			buffer.insert(12 - i * 2, ":");
		}
		buffer.insert(6, " ");// 设置日期与时间之间的连字符号
		for (int i = 1; i < 3; i++) {
			buffer.insert(6 - i * 2, "-");// 设置年、月、日之间的连字符号
		}
		return buffer.toString();
	}

	/**
	 * 获得上月的第一天日期
	 * 
	 * @return
	 */
	public static String getFrontMonthDate() {
		Calendar c = Calendar.getInstance();
		int nowMonth = c.get(Calendar.MONTH);
		int nowYear = c.get(Calendar.YEAR);
		if (nowMonth == 0) {
			nowYear--;
		} else {
			nowMonth--;
		}
		c.set(Calendar.YEAR, nowYear);
		c.set(Calendar.MONTH, nowMonth);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return getDateTime(DEFAULT_PATTERN, c);
	}

	/**
	 * 获得上周第N天日期
	 * 
	 * @param
	 * @return
	 */
	public static String getFrontWeekDate(int day) {
		Calendar c = Calendar.getInstance();
		int nowWeek = c.get(Calendar.WEEK_OF_YEAR);
		int nowYear = c.get(Calendar.YEAR);
		if (nowWeek == 1) {
			nowYear--;
		} else {
			nowWeek--;
		}

		c.set(Calendar.YEAR, nowYear);
		c.set(Calendar.WEEK_OF_YEAR, nowWeek);
		c.set(Calendar.DAY_OF_WEEK, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return getDateTime(DEFAULT_PATTERN, c);
	}

	/**
	 * 获得本周第N天日期
	 * 
	 * @param
	 * @return
	 */
	public static String getCurWeekDate(int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return getDateTime(DEFAULT_PATTERN, c);
	}

	/**
	 * 获得当月的第一天日期
	 * 
	 * @return
	 */
	public static String getCurMonthDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return getDateTime(DEFAULT_PATTERN, c);
	}

	/**
	 * 获取当天的日期（年月日）
	 * 
	 * @param
	 * @return
	 */
	public static String getCurDate() {
		return getCurDateTime("yyyy-MM-dd");
	}

	/**
	 * 毫秒时间转换为日期(格式：年月日时分秒)
	 * 
	 * @param time
	 * @return
	 */
	public static String getDate(long time) {
		Date d = new Date();
		d.setTime(time);
		SimpleDateFormat nowDate = new SimpleDateFormat(DEFAULT_PATTERN);
		return nowDate.format(d);
	}

	/**
	 * 毫秒时间转换为日期(格式：年月日)
	 * 
	 * @param time
	 * @return
	 */
	public static String getDateToDay(long time) {
		Date d = new Date();
		d.setTime(time);
		SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd");
		return nowDate.format(d);
	}

	/**
	 * 毫秒时间转换为日期(格式：月日时分)
	 * 
	 * @param time
	 * @return
	 */
	public static String getDateToHour(long time) {
		Date d = new Date();
		d.setTime(time);
		SimpleDateFormat nowDate = new SimpleDateFormat("MM-dd HH:mm");
		return nowDate.format(d);
	}

	/**
	 * 毫秒时间转换为日期(格式：时分)
	 * 
	 * @param time
	 * @return
	 */
	public static String getHourToMinute(long time) {
		Date d = new Date();
		d.setTime(time);
		SimpleDateFormat nowDate = new SimpleDateFormat("HH:mm");
		return nowDate.format(d);
	}

	/**
	 * 判断指定日期是否为今天
	 * 
	 * @param day
	 * @return
	 */
	public static boolean isToday(Calendar day) {
		return isSameDay(day, Calendar.getInstance());
	}

	/**
	 * 判断两个日期是否同一天
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean isSameDay(Calendar one, Calendar two) {
		return ((one.get(Calendar.YEAR) == two.get(Calendar.YEAR)) && (one
				.get(Calendar.DAY_OF_YEAR) == two.get(Calendar.DAY_OF_YEAR)));
	}

	/**
	 * 获得某一月份的日期范围 传入月份格式为yyyyMM
	 * 
	 * @return
	 */
	public static String[] getMonthRange(String mr) {
		String[] mrange = null;
		try {
			int year = Integer.parseInt(mr.substring(0, 4));
			int month = Integer.parseInt(mr.substring(4, 6));
			if (year > 1970 && year < 2100 && month > 0 && month < 12) {
				mrange = new String[2];
				mrange[0] = mr + "01";
				switch (month) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					mrange[1] = mr + "31";
					break;
				case 2:
					int my = year & 0x03;
					if (my != 0) {
						mrange[1] = mr + "28";
					} else {
						mrange[1] = mr + "29";
					}
					break;
				default:
					mrange[1] = mr + "30";
					break;
				}
			}
		} catch (Exception e) {
			mrange = null;
		}
		return mrange;
	}

	/**
	 * 获得给定的日期范围
	 * 
	 * @return
	 */
	public static String[] getDateRange(int dis) {
		String[] mrange = new String[2];
		Calendar c = Calendar.getInstance();
		SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd");
		if (dis < 0) {
			mrange[1] = nowDate.format(c.getTime());
			c.add(Calendar.DAY_OF_YEAR, dis);
			mrange[0] = nowDate.format(c.getTime());
		} else {
			mrange[0] = nowDate.format(c.getTime());
			c.add(Calendar.DAY_OF_YEAR, dis);
			mrange[1] = nowDate.format(c.getTime());
		}
		return mrange;
	}

	public static int getHourOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 判定date1，date2个日期是否同属一个月
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameMonth(Date date1, Date date2) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		int year1 = calendar.get(Calendar.YEAR);
		int month1 = calendar.get(Calendar.MONTH);
		calendar.setTime(date2);
		int year2 = calendar.get(Calendar.YEAR);
		int month2 = calendar.get(Calendar.MONTH);
		return (year1 == year2 && month1 == month2);
	}

	/**
	 * 返回2个日期之前相差月份数<br>
	 * 注意：date2应该大于或等于date1<br>
	 * 返回的Date数组中的日期对象为每个月1号0时0分0秒
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Date[] getMonthsBetweenDays(Date date1, Date date2) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date1);
		int startYear = calendar.get(Calendar.YEAR);
		int startMonth = calendar.get(Calendar.MONTH);

		calendar.setTime(date2);
		int endYear = calendar.get(Calendar.YEAR);
		int endMonth = calendar.get(Calendar.MONTH);

		int diffMonths = (endYear - startYear) * 12 + (endMonth - startMonth)
				+ 1;
		Date[] mothns = new Date[diffMonths];
		for (int i = 0; i < diffMonths; i++) {
			calendar.setTime(date1);
			calendar.add(Calendar.MONTH, i);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			mothns[i] = calendar.getTime();
		}
		return mothns;
	}

	/**
	 * 返回指定日期的最后时刻23:59:59.999
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastTimeOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 
	 * getHHmmss(获取指定日期时间的时分秒)
	 * 
	 * @param date
	 * @return String类型
	 * @exception
	 * @since 1.0.0
	 */
	public static String getHHmmss(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		return format.format(date);
	}

	/**
	 * 取得指定日期最初时刻00:00:00.000
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstTimeOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取指定日期所在月份的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 判定两个日期是否为同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
				&& calendar1.get(Calendar.MONTH) == calendar2
						.get(Calendar.MONTH) && calendar1
					.get(Calendar.DAY_OF_MONTH) == calendar2
				.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 取得下一天的这个时候
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 取得昨天
	 * 
	 * @return
	 */
	public static Date getYesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

	/**
	 * 
	 * 功能描述:获取两个日期之间的小时数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getHoursBetweenDate(long d1, long d2) {
		long time = d2 - d1;
		int hours = (int) (time / 1000 / 60 / 60);
		return hours;
	}

	/**
	 * 
	 * 功能描述:获取两个日期之间的秒数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getSecondsBetweenDate(long d1, long d2) {
		long time = d1 - d2;
		int seconds = (int) (time);
		return seconds;
	}

	/**
	 * 
	 * 功能描述:根据当天的小时、分钟获取时间
	 * 
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getTimes(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		long time = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
		if (time < 0) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			time = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
		}
		return time;
	}

	/**
	 * 
	 * 功能描述:获取当前日期是星期几
	 * 
	 * @param dt
	 * @return
	 */
	public static byte getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		byte w = (byte) (cal.get(Calendar.DAY_OF_WEEK) - 1);
		if (w < 0) {
			w = 0;
		}
		return w;
	}

	/**
	 * 获得昨天是星期几
	 * 
	 * @param dt
	 * @return
	 */
	public static int getWeekOfyesterDayDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 2;
		if (w == 0) {
			w = 0;
		} else if (w < 0) {
			w = 6;
		}
		return w;
	}

	/**
	 * 获得2个日期相差的天数
	 * 
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 把long类型的时间转换为天 小时 分钟
	 * 
	 * @param intervalTime
	 * @return
	 */
	public static String getIntervalUpdateTime(long intervalTime) {
		StringBuilder result = new StringBuilder();
		long interval = intervalTime / 1000;
		final long day = 24 * 60 * 60;
		final long hour = 60 * 60;
		final long minute = 60;
		int detailDay = 0;
		int detailHour = 0;
		int detailMinute = 0;
		int detailSecond = 0;
		if (interval >= day) {
			detailDay = (int) (interval / day);
			interval = interval - detailDay * day;
		}
		if (interval >= hour) {
			detailHour = (int) (interval / hour);
			interval = interval - hour * detailHour;
		}
		if (interval >= minute) {
			detailMinute = (int) (interval / minute);
			interval = interval - detailMinute * minute;
		}
		if (interval > 0) {
			detailSecond = (int) interval;
		}
		result.setLength(0);
		if (detailDay > 0) {
			result.append(detailDay);
			result.append("天");
		}
		if (detailHour > 0) {
			result.append(detailHour);
			result.append("小时");
		}
		if (detailMinute > 0) {
			result.append(detailMinute);
			result.append("分");
		}
		if (detailSecond > 0) {
			result.append(detailSecond);
			result.append("秒");
		}
		return result.toString();
	}

	public static String convertDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
		return sdf.format(date);
	}

	/**
	 * 两个时间之间相差距离多少天
	 * 
	 * @param
	 * @param
	 * @return 相差天数
	 */
	public static long getDistanceDays(String str1, String str2)
			throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date one;
		Date two;
		long days = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			days = diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * 
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long[] getDistanceTimes(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long[] times = { day, hour, min, sec };
		return times;
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * 
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}

	public static Date parseDateTime(String dateValue, String format) {
		SimpleDateFormat obj = new SimpleDateFormat(format);
		try {
			return obj.parse(dateValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean lessThan(Date first, Date second, int field,
                                   int amount) {
		if (null == first || null == first) {
			return false;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(first);
		cal.add(field, amount);
		Date nfirst = cal.getTime();
		if (second.getTime() <= nfirst.getTime()) {
			return true;
		}

		return false;
	}
}