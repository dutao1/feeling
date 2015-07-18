package com.feeling.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;


public class DateUtil {
	
	static String dateYYYY_MM_dd = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	static Pattern pDate = Pattern.compile(dateYYYY_MM_dd);
	public static enum Format {
		YYYY("yyyy"),
		YYYYMM("yyyyMM"),
		MM("MM"),
		HHMM("HH:mm"),
		MMDD("MMdd"),
		DD("dd"),
		YYMMdd("yyMMdd"),
		YYYYMMEEE("yyyyMMEEE"),
		YYYYMMDD("yyyyMMdd"),
		YYMMDDHH("yyMMddHH"),
		YYYYMMDDHH("yyyyMMddHH"),
		YYYYMMDDHHMM("yyyyMMddHHmm"),
		YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
		HYPHEN_YYYYMM("yyyy-MM"),
		HYPHEN_YYYYMMEEE("yyyy-MM-EEE"),
		HYPHEN_YYYYMMDD("yyyy-MM-dd"),
		HYPHEN_YYYYMMDDHH("yyyy-MM-dd HH"),
		HYPHEN_YYYYMMDDHHMM("yyyy-MM-dd HH:mm"),
		HYPHEN_YYYYMMDDHHMMSS("yyyy-MM-dd HH:mm:ss"),
		SLANT_YYYYMM("yyyy/MM"),
		SLANT_YYYYMMEEE("yyyy/MM/EEE"),
		SLANT_YYYYMMDD("yyyy/MM/dd"),
		SLANT_YYYYMMDDHH("yyyy/MM/dd HH"),
		SLANT_YYYYMMDDHHMM("yyyy/MM/dd HH:mm"),
		SLANT_YYYYMMDDHHMMSS("yyyy/MM/dd hh:mm:ss");
		
		private String format;
		
		private Format(String format) {
		
			this.format = format;
		}
		
		public String getFormat() {
		
			return format;
		}
		
		public int getType() {
		
			String[] names = name().split("_");
			String name = null;
			if (names.length > 1) {
				name = names[1];
			} else {
				name = names[0];
			}
			String pref = ".*";
			if (name.matches(pref + "Y+$")) {
				return Calendar.YEAR;
			} else if (name.matches(pref + "M+$")) {
				return Calendar.MONTH;
			} else if (name.matches(pref + "E+$")) {
				return Calendar.WEEK_OF_YEAR;
			} else if (name.matches(pref + "D+$")) {
				return Calendar.DATE;
			} else if (name.matches(pref + "H+$")) {
				return Calendar.HOUR;
			} else if (name.matches(pref + "H+M+$")) {
				return Calendar.MINUTE;
			} else if (name.matches(pref + "S+$")) {
				return Calendar.SECOND;
			} else {
				throw new IllegalArgumentException("Illegal Date Type");
			}
		}
	}
	
	public static int getFormatType(String pattern) {
	
		for (Format format : Format.values()) {
			if (format.getFormat().equals(pattern)) {
				return format.getType();
			}
		}
		return -1;
	}
	public static String getCurrentDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	/**
	 * return current date
	 * 
	 * @return current date
	 */
	public static Date getCurrentDate() {
	
		return new Date(System.currentTimeMillis());
	}
	
	public static boolean isToday(Date date) {
	
		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)//
				&& cal.get(Calendar.MONTH) == today.get(Calendar.MONTH)//
				&& cal.get(Calendar.DATE) == today.get(Calendar.DATE);
	}
	
	public static Date add(Date date, int amount, int field) {
	
		if (field == Calendar.YEAR) {
			return DateUtils.addYears(date, amount);
		} else if (field == Calendar.MONTH) {
			return DateUtils.addMonths(date, amount);
		} else if (field == Calendar.DATE || field == Calendar.DAY_OF_MONTH) {
			return DateUtils.addDays(date, amount);
		} else if (field == Calendar.WEEK_OF_MONTH || field == Calendar.WEEK_OF_YEAR) {
			return DateUtils.addWeeks(date, amount);
		} else if (field == Calendar.HOUR || field == Calendar.HOUR_OF_DAY) {
			return DateUtils.addHours(date, amount);
		} else if (field == Calendar.SECOND) {
			return DateUtils.addSeconds(date, amount);
		} else if (field == Calendar.MINUTE) {
			return DateUtils.addMinutes(date, amount);
		} else {
			throw new IllegalArgumentException("Un support date type : " + field);
		}
	}
	
	public static long diff(Date left, Date right, TimeUnit unit) {
	
		return unit.convert(left.getTime() - right.getTime(), TimeUnit.MILLISECONDS);
	}
	
	public static long diff(Date left, long delay, Date right, TimeUnit unit) {
	
		return unit.convert(left.getTime() + TimeUnit.MILLISECONDS.convert(delay, unit) - right.getTime(), TimeUnit.MILLISECONDS);
	}
	
	public static Date getStarting(Date date, int field) {
	
		if (field == Calendar.YEAR) {
			return DateUtils.truncate(date, Calendar.YEAR);
		} else if (field == Calendar.MONTH) {
			return DateUtils.truncate(date, Calendar.MONTH);
		} else if (field == Calendar.DATE || field == Calendar.DAY_OF_MONTH) {
			return DateUtils.truncate(date, Calendar.DATE);
		} else if (field == Calendar.WEEK_OF_MONTH || field == Calendar.WEEK_OF_YEAR) {
			Calendar cal = Calendar.getInstance(Locale.CHINA);
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			cal.setTime(DateUtils.truncate(date, Calendar.DATE));
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			return cal.getTime();
		} else if (field == Calendar.HOUR || field == Calendar.HOUR_OF_DAY) {
			return DateUtils.truncate(date, Calendar.HOUR);
		} else {
			throw new IllegalArgumentException("Un support date type : " + field);
		}
	}
	
	public static Date getEnding(Date date, int field) {
	
		if (field == Calendar.YEAR) {
			return DateUtils.addYears(DateUtils.truncate(date, Calendar.YEAR), 1);
		} else if (field == Calendar.MONTH) {
			return DateUtils.addMonths(DateUtils.truncate(date, Calendar.MONTH), 1);
		} else if (field == Calendar.DATE || field == Calendar.DAY_OF_MONTH) {
			return DateUtils.addDays(DateUtils.truncate(date, Calendar.DATE), 1);
		} else if (field == Calendar.WEEK_OF_MONTH || field == Calendar.WEEK_OF_YEAR) {
			Calendar cal = Calendar.getInstance(Locale.CHINA);
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			cal.setTime(DateUtils.truncate(date, Calendar.DATE));
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			return DateUtils.addWeeks(cal.getTime(), 1);
		} else if (field == Calendar.HOUR || field == Calendar.HOUR_OF_DAY) {
			return DateUtils.addHours(DateUtils.truncate(date, Calendar.HOUR), 1);
		} else {
			throw new IllegalArgumentException("Un support date type : " + field);
		}
	}
	
	public static boolean isEqual(Date left, Date right, int... fields) {
	
		Calendar cLeft = Calendar.getInstance();
		cLeft.setTime(left);
		Calendar cRight = Calendar.getInstance();
		cRight.setTime(right);
		for (int field : fields) {
			if (cLeft.get(field) != cRight.get(field)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * format the date in given pattern
	 * 
	 * @param d
	 *            date
	 * @param pattern
	 *            time pattern
	 * @return the formatted string
	 */
	public static String formatDate(Date d) {
	
		if (d == null) {
			throw new IllegalArgumentException("given date is invalid " + d);
		}
		return formatDate(d, Format.HYPHEN_YYYYMMDD);
	}
	
	/**
	 * format the date in given pattern
	 * 
	 * @param d
	 *            date
	 * @param pattern
	 *            time pattern
	 * @return the formatted string
	 */
	public static String formatDate(Date d, String pattern) {
	
		if (null == d || StringUtils.isBlank(pattern)) {
			throw new IllegalArgumentException("given parameters is invalid " + d + " - " + pattern);
		}
		SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance();
		formatter.applyPattern(pattern);
		return formatter.format(d);
	}
	
	/**
	 * format the date in given pattern
	 * 
	 * @param d
	 *            date
	 * @param pattern
	 *            time pattern
	 * @return the formatted string
	 */
	public static String formatDate(Date d, Format pattern) {
	
		if (null == d || pattern == null) {
			throw new IllegalArgumentException("given date or pattern is invalid " + d + " - " + pattern);
		}
		SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance();
		formatter.applyPattern(pattern.getFormat());
		return formatter.format(d);
	}
	
	/**
	 * change string to date
	 * 
	 * @param sDate
	 *            the date string
	 * @param sFmt
	 *            the date format
	 * @return Date object
	 */
	public static Date formatDate(String sDate) {
	
		Date dt = null;
		if (StringUtils.isNotBlank(sDate)) {
			for (Format format : Format.values()) {
				try {
					dt = DateUtils.parseDateStrictly(sDate, format.getFormat());
				} catch (ParseException e) {
					dt = null;
					continue;
				}
				break;
			}
		}
		if (dt == null) {
			throw new IllegalArgumentException("given pattern is invalid " + sDate);
		}
		return dt;
	}
	
	/**
	 * change string to date
	 * 
	 * @param sDate
	 *            the date string
	 * @param sFmt
	 *            the date format
	 * @return Date object
	 */
	public static Date formatDate(String sDate, Format format) {
	
		Date dt = null;
		try {
			dt = DateUtils.parseDateStrictly(sDate, format.getFormat());
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return dt;
	}
	
	public static Date truncate(Date date, Format format) {
	
		return DateUtils.truncate(date, format.getType());
	}
	
	public static String getWeekOfDate(Date date) {
	
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) w = 0;
		return weekDays[w];
	}
	
	public static String getWeekOfDate2(Date date) {
	
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) w = 0;
		return weekDays[w];
	}
	/**
	 * 获得周的数字，比如周日返回7 周一返回1
	 * @param date
	 * @return int
	 */
	public static int getWeekNumOfDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK)-1;
		if(w==0)w=7;
		return w;
	}
	public static Date  formatWeekDate(Date d,int weekdate){
		 if(d==null){
			 d = new Date();
		 }
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(d);
		 if(d.compareTo(new Date())>0){
			 cal.add(Calendar.DATE, -7);
		 }
		 cal.set(Calendar.DAY_OF_WEEK,weekdate);
		 return cal.getTime();
	} 
	
	/**
	 * 获取某日期在该年的 ios(国际标准)周的几周</br>
	 * 说明：ios标准计算法：若1月1日大于等于周五 则算做去年的最后一周。否则是今年第一周
	 * @param date  日期字符串
	 * @param pattern 格式化
	 * @return  String  201405 /201452
	 */
	public static String getIOSWeekOfYear(String date,String pattern){
		if(pattern==null||pattern.equals("")){
			pattern = "yyyy-MM-dd";
		}
		Date d = null;
		int year = 0;
		if(date==null ||date.equals("")){
			d = new Date();
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			try {
				d = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	   Calendar calendar = Calendar.getInstance();
	   calendar.setTime(d);
	   //获得日期是那年的第几周
	   int num =getWeekOfYear(d);
	   //获得日期的一月一日是周几
	   int yearfirstweek = getWeekOfFirstdayByYear(d);
	   if(yearfirstweek>=5){
		   num=num-1;
	   }
	   //若是0 说明是去年的周后一周，计算最后一周是那年的第几周
	   if(num==0){
		  
		   calendar.add(Calendar.YEAR, -1);
		   calendar.set(Calendar.MONTH, 11);
		   calendar.set(Calendar.DAY_OF_MONTH, 31);
		   int lastYearWeekNum =  getWeekOfYear(calendar.getTime());
		   int lastyearfirstweek = getWeekOfFirstdayByYear(calendar.getTime());
		   //等于一说明 最后一天属于下一年的第一周（非Ios标准），则向前推一周计算
		   if(lastYearWeekNum==1){
			   calendar.add(Calendar.DAY_OF_MONTH, -7);
			   lastYearWeekNum = getWeekOfYear(calendar.getTime())+1;
			    
		   } 
		   if(lastyearfirstweek>=5){
			   num=lastYearWeekNum-1;
		   } else{
			   num=lastYearWeekNum;
		   }
		   year = calendar.get(Calendar.YEAR);
	   } 
	   year = calendar.get(Calendar.YEAR);
	   if(num<10){
		   return  year+"0"+num;
	   }
	   return year+""+num;
	}
	
	/**
	 * 获得当期日期是今年第几周
	 * @param d 日期
	 * @return int
	 */
	public static int getWeekOfYear(Date d){
		Calendar calendar = Calendar.getInstance();
	    calendar.setFirstDayOfWeek(Calendar.MONDAY);
	    calendar.setTime(d);
	    return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	/**
	 * 获得某年一月一日是周几
	 * @param d  日期
	 * @return int  1-7
	 */
	public static int getWeekOfFirstdayByYear(Date d){
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return  getWeekNumOfDate(cal.getTime());
	}
	
	
	/**
	 * 按照周，格式化日期范围内第一天和最后一天<br>（比如2014-01-17（周五）到2014-09-17（周三） 返回 2014-01-13（周一）,2014-09-14（周日））
	 * </br> 如果日期非法（比如【2014-12-17到2012-09-17】或 返回 2012-09-10（周一）,2012-09-16（周日））
	 * </br> 如果日期超过当期日期（比如当期日期是10月17日）【2014-08-17到2014-12-17】 返回 2014-08-18（周一）,2014-10-12（周日）
	 * </br> 如果日期超过当期日期（比如当期日期是10月17日）【2014-12-17到2014-12-17】 返回 2014-10-06（周一）,2014-10-12（周日）
	 * @param beginDate 开始日期  
	 * @param endDate 结束日期
	 * @param pattern  
	 * @throws Exception
	 * @return String[] 开始，结束 日期
	 */
	public static String[] formatDateToPassedWeek(String beginDate,String endDate,String pattern) throws Exception{
		if(pattern==null||pattern.equals("")){
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date bdate = sdf.parse(beginDate);
		Date edate = sdf.parse(endDate);
		Date now = new Date();
		if(bdate.compareTo(now)>0){
			bdate = now;
		}
		if(edate.compareTo(now)>0){
			edate = now;
		}
		if(bdate.compareTo(edate)>0){
			bdate = edate;
		}
		bdate =formatWeekDate(bdate,Calendar.MONDAY);
		edate = formatWeekDate(edate,Calendar.SUNDAY);
		if(bdate.compareTo(edate)>0){
			bdate = add(bdate, -7, Calendar.DAY_OF_MONTH);
		}
		String lastday = sdf.format(edate);
		String firstday = sdf.format(bdate);
		return  new String[]{firstday,lastday};
		
	}
	/**
	 * 按照月份，格式化日期范围内第一天和最后一天<br>（比如2014-01-17到2014-09-17 返回 2014-01-01,2014-09-30）
	 * </br> 如果日期非法（比如【2014-12-17到2012-09-17】或 返回 2014-01-01,2014-09-30）
	 * </br> 如果日期超过当期日期（比如当期日期是10月17日）【2014-08-17到2014-12-17】 返回 2014-08-01,2014-09-30
	 * </br> 如果日期超过当期日期（比如当期日期是10月17日）【2014-12-17到2014-12-17】 返回 2014-08-01,2014-09-30
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param pattern  
	 * @throws Exception
	 * @return String[] 开始，结束 日期
	 */
	public static String[] formatDateToPassedMonth(String beginDate,String endDate,String pattern) throws Exception{
		if(pattern==null||pattern.equals("")){
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date bdate = sdf.parse(beginDate);
		Date edate = sdf.parse(endDate);
		bdate = getMonthFirstday(bdate);
		edate = getMonthLastday(edate);
		if(new Date().compareTo(edate)<0){
			edate = getMonthLastday(null);
		}
		if(edate.compareTo(bdate)<0){
			edate = getMonthLastday(null);
			if(edate.compareTo(bdate)<0){
				bdate =  getMonthFirstday(null);
			}
		}
		String lastday = sdf.format(edate);
		String firstday = sdf.format(bdate);
		return  new String[]{firstday,lastday};
	}
	/**
	 * 获得某月的第一天</br>
	 * 如果传空或者日期是当月，则返回当前月上个月的第一天
	 * @return Date
	 */
	public static Date getMonthFirstday(Date  d){
		boolean islastMonth = false;
		if(d==null){
			d = new Date();
		//	islastMonth=true;
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			if(sdf.format(d).equals(sdf.format(new Date()))){
				islastMonth=true;
			}
		}
		Calendar cale = Calendar.getInstance();  
		cale.setTime(d);
		if(islastMonth){
			cale.add(Calendar.MONTH,-1);
		}
		cale.set(Calendar.DAY_OF_MONTH,1);
		cale.set(Calendar.HOUR_OF_DAY, 0);  
		//将分钟至0  
		cale.set(Calendar.MINUTE, 0);  
		//将秒至0  
		cale.set(Calendar.SECOND,0);  
		//将毫秒至0  
		cale.set(Calendar.MILLISECOND, 0);  
		return cale.getTime();
	}
	/**
	 * 获得某月的最后一天</br>
	 * 如果传空或日期是当月，则返回当前月上个月的最后一天
	 * @return Date
	 */
	public static Date getMonthLastday(Date  d){
		boolean islastMonth = false;
		if(d==null){
			d = new Date();
			//islastMonth=true;
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			if(sdf.format(d).equals(sdf.format(new Date()))){
				islastMonth=true;
			}
		}
		Calendar cale = Calendar.getInstance();  
		cale.setTime(d);
		if(!islastMonth){
			cale.add(Calendar.MONTH, 1);
		}
		cale.set(Calendar.DAY_OF_MONTH,0);
		cale.set(Calendar.HOUR_OF_DAY, 23);  
		//将分钟至0  
		cale.set(Calendar.MINUTE, 59);  
		//将秒至0  
		cale.set(Calendar.SECOND,59);  
		//将毫秒至0  
		cale.set(Calendar.MILLISECOND, 999);  
		return cale.getTime();
	}
	
	public static String getLastMonth(String parrent){
		Calendar cale = Calendar.getInstance();  
		cale.add(Calendar.MONTH, -1);
		return formatDate(cale.getTime(), parrent);
	}
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, week);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//设置周一
		c.setFirstDayOfWeek(Calendar.MONDAY);
		return c.getTime();
	} 
	
	/**
	 * 取日期
	 * @param date 日期
	 * @param datetype  计算方式  0 以天计算，1以周计算，2以月计算，3以年计算
	 * @return String yyyy-MM-dd
	 */
	public static String getFormatPassdeDateByStat(int date,int datetype){
		//计算方式  0 以天计算，1以周计算，2以月计算，3以年计算
		Date  d= null;
		String date_m = String.valueOf(date);
		String date_str = "" ;
		switch(datetype){
			case 0:
				date_str =date_m;
				break;
			case 1:
				String date_w = String.valueOf(date);
				int year = 0;
				int week = 0;
				if(date_w!=null&&date_w.length()>=8){
					year = Integer.valueOf(date_w.substring(0,4));
					week = Integer.valueOf(date_w.substring(6));
					d = getFirstDayOfWeek(year,week);
					String week_ios = getIOSWeekOfYear(formatDate(d), "yyyy-MM-dd");
					if(!week_ios.equals(year+""+week)){
						d = add(d, 7, Calendar.DAY_OF_MONTH);
					}
					return formatDate(d);
				}
				break;
			case 2:
				date_str = date_m.substring(0,6)+"01";
				break;
			case 3:
				date_str = date_m.substring(0,4)+"0101";
				break;
			default:
				return formatDate(new Date());
		}
		d= formatDate(String.valueOf(date_str),Format.YYYYMMDD);
		return formatDate(d);
	}
	public static String formatDateByDatePattern(int dateType,String cdate){
		if(cdate==null){
			return null;
		}
		if(dateType==0){
			return cdate;
		}else if(dateType==1){
			cdate = DateUtil.getIOSWeekOfYear(cdate, "yyyyMMdd");
		}else if(dateType ==2){
			if(cdate!=null&&cdate.length()>=8){
				cdate = cdate.substring(0,6);
			}
		}else{
			if(cdate!=null&&cdate.length()>=8){
				cdate = cdate.substring(0,4);
			}
		}
		return formatDateStr(cdate,dateType);
		 
	}
	/**
	 * 将日期格式化成8位
	 * @param cdate 日期 20141001 201410 201433
	 * @param dateType   0 日期，1周 2月  3 年
	 * @return String
	 */
	public static String formatDateStr(String cdate,int dateType){
		if(cdate!=null){
			switch(dateType){
				case 1:
					if(cdate.length()>=6){
						//处理周，如果是201552 将其转成去年的最后一周，而不是当前年最后一周
						String year = cdate.substring(0,4);
						String week = cdate.substring(4);
						if(Integer.valueOf(week)>50){
							SimpleDateFormat sdf = new SimpleDateFormat("MM");
							if(sdf.format(new Date()).equals("01")){
								year = String.valueOf(Integer.valueOf(year)-1);
							}
						}
						cdate = year+"00"+week;
					}
					break;
				case 2:
					cdate=cdate+"00";//补全日
					break;
				case 3:
					cdate=cdate+"0000";//补全月日
					break;
				default:
					break;	
			}
		}
		return cdate;
		
	}
	
	public static long diffDays(String endDate,String beginDate){
		 Date bdate = null;
		 Date edate = null;
		 if(endDate==null||endDate.equals("")){
			 edate = new Date();
		 }else{
			 edate =  formatDate(endDate);
		 }
		 if(beginDate==null||beginDate.equals("")){
			 bdate = new Date();
		 }else{
			 bdate = formatDate(beginDate);
		 }
		long days =  diff(edate,bdate,TimeUnit.DAYS);
		return days;
	}
	/**
	 * 获得日期所在周的周N
	 * @param date
	 * @param weekDate  1代表周日  7 代表周六
	 * @return String YYYY-MM-DD格式日期
	 */
	public static String getWeekDate(Date date,int weekDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK,weekDate);
		Date dateLastMonday = cal.getTime();
		if(diff(date, cal.getTime(), TimeUnit.DAYS)<0){
			dateLastMonday = add(dateLastMonday, -7, Calendar.DAY_OF_MONTH);
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(dateLastMonday);
	}
	public static List<String> getDateSpaceList(String begin,String end){
		
     List<String> listTime = new ArrayList<String>();//获取所有时间  
     SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");  
     Date dBegin;  
     Date dEnd;  
     try {  
         dBegin = f.parse(begin);  
         dEnd = f.parse(end);  
         for(long i=dBegin.getTime();i<=dEnd.getTime();i+=86400000){  
             Date d=new Date(i);  
             String date=f.format(d);  
             listTime.add(date);  
         }  
     } catch (ParseException e) {  
         e.printStackTrace();  
     } 
     return listTime;
	}
    /**
     * 是否日期格式【YYYY-MM-DD】
     * @param date
     * @return
     */
    public static boolean isCommonDateStr(String date){
    	if(StringUtils.isEmpty(date)){
    		return false;
    	}
		return  pDate.matcher(date).matches();
    	
    }
    /**
     * 时间转换，默认【若为null】会将第一个转换成本月第一天，最后一个本月最后一天
     * @param times
     * @return Date[]
     */
    public static Date[] covertMonthsDate(Long... times){
    	 if(times==null){
    		 return null;
    	 }
    	 Date dates[] = new Date[times.length];
    	for(int i=0;i<times.length;i++){
    		Long time = times[i];
    		if(time==null){
    			if(i==0){
    				dates[i]=DateUtil.getMonthFirstday(null);
    			}else if(i==times.length-1){
    				dates[i]=DateUtil.getMonthLastday(null);
    			}else{
    				dates[i]=new Date();
    			}
    		}else{
    			//转换秒
    			int length = String.valueOf(time.longValue()).length();
    			if(length==10){
    				time =time.longValue()*1000;
    			}
    			dates[i]=new Date(time);
    		}
    	}
    	return dates;
    }
    public static int getAge(String birthday) throws Exception{
    	SimpleDateFormat sdf = new SimpleDateFormat(Format.YYMMdd.getFormat());
		return getAge(sdf.parse(birthday));
	}
	
	public static int getAge(Date birthday){
		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();
		
		cal.setTime(birthday);
		
		int yearOff = cal1.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		
		cal.add(Calendar.YEAR, yearOff);
		
		if(!cal1.after(cal)){
			yearOff --;
		}
		
		return yearOff;
	}
	public static Date parse(String str, String pattern, Locale locale) {
        if(str == null || pattern == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern, locale).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
	public static void main(String[] args) {
		System.out.println(isCommonDateStr("2531455"));
		SimpleDateFormat sdf = new SimpleDateFormat();
		System.out.println(formatDate(getMonthLastday(null), "yyyyMMdd HHmmss"));
		//System.out.println(getWeekDate(formatDate("2015-01-01"),Calendar.MONDAY));
		//List<String>  list =  getDateSpaceList("2014-02-01","2014-03-01");
		//System.out.println(list.toString());
		//System.out.println(diffDays("2014-11-28","2014-12-28"));
		/*String num = DateUtil.formatDateByDatePattern(3,"20141006");
		System.out.println(num);*/
		/*String s = getFormatPassdeDateByStat(20050000,5);
		System.out.println(s);
		System.out.println(getLastMonth("yyyyMM00"));
		
		String sdate = "2006-01-11" ;
		String edate = "2014-12-17" ;
		System.out.println( DateUtil.add(DateUtil.getCurrentDate(), 1, Calendar.HOUR));
		*/
		/*long days=DateUtil.diff(DateUtil.formatDate(edate), DateUtil.formatDate(sdate), TimeUnit.DAYS);
		if(days>31){
			edate = DateUtil.formatDate(DateUtil.add(DateUtil.formatDate(sdate), 30, Calendar.DAY_OF_MONTH));
		}
		System.out.println(edate);
		System.out.println(DateUtil.getWeekOfDate(getCurrentDate()));
		*/
		/*try {
			String num = DateUtil.getIOSWeekOfYear(sdate,"yyyy-MM-dd");
			System.out.println("num=========="+num);
			String s[] = DateUtil.formatDateToPassedMonth(sdate,edate,"yyyy-MM-dd");
			System.out.println(s[0]+","+s[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
}
