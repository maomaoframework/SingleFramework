package huxg.framework.util;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import huxg.framework.util.encrypt.TypeConvertUtils;

/**
 * <p>
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class DateUtils {
	public final static int MAX_YEAR = 3000;

	public final static int MIN_YEAR = 1900;

	public final static String DATE_SEPERATOR = "-";

	public final static String DF_EN_DATE = "yyyy-MM-dd";

	public final static String DF_EN_DATETIME = "yyyy-MM-dd HH:mm:ss";

	public final static String DF_CN_DATE = "yyyy年MM月dd日";

	public final static String DF_CN_DATETIME = "yyyy年MM月dd日 HH点mm分ss秒";

	/**
	 * 
	 * 
	 * @return String
	 */
	public static final String getCurDate() {
		Calendar today = Calendar.getInstance();

		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd");

		return formatDateTime.format(today.getTime());
	}

	/**
	 * 
	 * 
	 * @return Date
	 * @throws ParseException
	 */
	public static final Date getDateFromString(String sDate) throws ParseException {
		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd");
		return formatDateTime.parse(sDate);
	}

	/**
	 * 
	 * @return Date
	 * @throws ParseException
	 */
	public static final Date getDateFromCHString(String sDate) throws ParseException {
		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy��MM��dd��");
		return formatDateTime.parse(sDate);
	}

	/**
	 * 
	 * 
	 * @return String
	 */
	public static final String getCurDateString() {
		StringBuffer result = new StringBuffer();

		int iYear, iMonth, iDate;

		Calendar today = Calendar.getInstance();

		iYear = today.get(Calendar.YEAR);
		result.append(iYear);

		iMonth = today.get(Calendar.MONTH) + 1;
		if (iMonth < 10)
			result.append("0");
		result.append(iMonth);

		if ((iDate = today.get(Calendar.DATE)) < 10)
			result.append("0");
		result.append(iDate);

		return result.toString();
	}

	/**
	 * 
	 * 
	 * @param sDateTime1
	 * @param sDateTime2
	 * 
	 * @return int
	 */
	static public final int equalDateTime(final String sDateTime1, final String sDateTime2) {
		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date dateTime1 = formatDateTime.parse(sDateTime1);
			Date dateTime2 = formatDateTime.parse(sDateTime2);

			if (dateTime1.equals(dateTime2))
				return 0;
			else if (dateTime1.after(dateTime2))
				return 1;
			else
				return -1;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 
	 * 
	 * @param sDate1
	 * @param sDate2
	 * 
	 * @return int
	 */
	static public final boolean after(final String sDate1, final String sDate2) {
		int iYear1, iMonth1, iDay1;
		int iYear2, iMonth2, iDay2;

		iYear1 = getYear(sDate1);
		iMonth1 = getMonth(sDate1);
		iDay1 = getDay(sDate1);

		iYear2 = getYear(sDate2);
		iMonth2 = getMonth(sDate2);
		iDay2 = getDay(sDate2);

		Calendar dtDate1 = Calendar.getInstance();
		dtDate1.set(iYear1, iMonth1 - 1, iDay1);
		Calendar dtDate2 = Calendar.getInstance();
		dtDate2.set(iYear2, iMonth2 - 1, iDay2);

		if (dtDate1.after(dtDate2))
			return true;
		else
			return false;
	}

	/**
	 * 
	 * 
	 * @param sDate1
	 * @param sDate2
	 * 
	 * @return int
	 */
	static public final boolean equals(final String sDate1, final String sDate2) {
		int iYear1, iMonth1, iDay1;
		int iYear2, iMonth2, iDay2;

		iYear1 = getYear(sDate1);
		iMonth1 = getMonth(sDate1);
		iDay1 = getDay(sDate1);

		iYear2 = getYear(sDate2);
		iMonth2 = getMonth(sDate2);
		iDay2 = getDay(sDate2);

		Calendar dtDate1 = Calendar.getInstance();
		dtDate1.set(iYear1, iMonth1 - 1, iDay1);
		Calendar dtDate2 = Calendar.getInstance();
		dtDate2.set(iYear2, iMonth2 - 1, iDay2);

		if (dtDate1.equals(dtDate2))
			return true;
		else
			return false;
	}

	/**
	 * 
	 * 
	 * @param iYear
	 * @param iMonth
	 * 
	 * @return int
	 */
	static public final boolean laterMonth(final int iYear, final int iMonth) {
		Calendar today = Calendar.getInstance();

		int iCurY = today.get(Calendar.YEAR);
		int iCurM = today.get(Calendar.MONTH) + 1;

		if (iYear == iCurY) {
			if (iMonth > iCurM)
				return true;
			else
				return false;
		} else if (iYear < iCurY)
			return false;
		else
			return true;
	}

	/**
	 * 
	 * 
	 * @return int
	 */
	static public final int getMonth() {
		Calendar today = Calendar.getInstance();

		return today.get(Calendar.MONTH) + 1;
	}

	/**
	 * 
	 * 
	 * @return int
	 */
	static public final int getYear() {
		Calendar today = Calendar.getInstance();
		return today.get(Calendar.YEAR);
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return int
	 */
	static public final int getYear(String sDate) {
		sDate = getDate(sDate);

		int iIdx1 = sDate.indexOf('-');

		int iYear = Integer.parseInt(sDate.substring(0, iIdx1));

		return iYear;
	}

	/**
	 * @return
	 */
	public static String getCurYearString() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		return String.valueOf(year);
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return int
	 */
	static public final int getMonth(String sDate) {
		sDate = getDate(sDate);

		int iIdx1 = sDate.indexOf('-');
		int iIdx2 = sDate.lastIndexOf('-');

		int iMonth = Integer.parseInt(sDate.substring(iIdx1 + 1, iIdx2));

		return iMonth;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return int
	 */
	static public final int getDay(String sDate) {
		sDate = getDate(sDate);

		int iIdx2 = sDate.lastIndexOf('-');

		int iDay = Integer.parseInt(sDate.substring(iIdx2 + 1));

		return iDay;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public final String getDateTimeWithoutSecond(final String sDateTime) {
		if (sDateTime.indexOf(':') > 0 && sDateTime.indexOf(':') != sDateTime.lastIndexOf(':'))
			return sDateTime.substring(0, sDateTime.lastIndexOf(':'));
		else
			return sDateTime;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public final String getDateTime(final String sDateTime) {
		if (sDateTime.indexOf('.') > 0)
			return sDateTime.substring(0, sDateTime.lastIndexOf('.'));
		else
			return sDateTime;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public final String getDate(final String sDateTime) {
		int iEndIdx = sDateTime.indexOf(" ");

		if (iEndIdx != -1)
			return sDateTime.substring(0, iEndIdx);
		else
			return sDateTime;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return int
	 */
	static public final int getWeek(final String sDate) {
		int iYear, iMonth, iDate;

		iYear = getYear(sDate);
		iMonth = getMonth(sDate);
		iDate = getDay(sDate);

		Calendar calendar = Calendar.getInstance();
		calendar.set(iYear, iMonth - 1, iDate);

		int iWeekDay = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

		if (iWeekDay == 0) // Sunday
			iWeekDay = 7;

		return iWeekDay;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return int
	 */
	static public final String getWeekName(String sDate, final Locale locale) {
		MessageFormat formatMsg = new MessageFormat("{0,Date,EEE}");

		if (locale != null)
			formatMsg.setLocale(locale);

		Object[] aobjArg = { TypeConvertUtils.convertString2Date(getDate(sDate)) };
		sDate = formatMsg.format(aobjArg);

		return sDate;
	}

	/**
	 * 
	 * 
	 * @param iYear
	 * @param iMonth
	 * 
	 * @return int
	 */
	static public final int getMonthDays(final int iYear, final int iMonth) {
		if (iMonth == 2) {
			if ((iYear % 4) == 0 && (iYear % 100 > 0 || iYear % 400 == 0))
				return 29;
			else
				return 28;
		}

		if (iMonth <= 7) {
			if ((iMonth % 2) == 1)
				return 31;
			else
				return 30;
		} else {
			if ((iMonth % 2) == 1)
				return 30;
			else
				return 31;
		}
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public final String getWeekFirstDate(String sDate) {
		String sRetDate = null;

		if (sDate == null || sDate.length() == 0)
			sDate = getCurDate();

		int iMonth, iYear, iDay;
		int iIdx1 = sDate.indexOf('-');
		int iIdx2 = sDate.lastIndexOf('-');
		int iIdx3 = sDate.length();
		try {
			iYear = Integer.parseInt(sDate.substring(0, iIdx1));
			iMonth = Integer.parseInt(sDate.substring(iIdx1 + 1, iIdx2));
			iDay = Integer.parseInt(sDate.substring(iIdx2 + 1, iIdx3));
			Calendar calendar = Calendar.getInstance();
			calendar.set(iYear, iMonth - 1, iDay);

			if (calendar != null) {
				int iWeekDay = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

				if (iWeekDay != 0) // Not Sunday
					iDay = iDay - iWeekDay + 1;
				else
					// Sunday
					iDay = iDay - 6;

				if (iDay <= 0) {
					if (--iMonth < 1) {
						iMonth = 12;
						iYear--;
					}
					iDay = getMonthDays(iYear, iMonth) + iDay;
				}
			}
			sRetDate = iYear + "-" + iMonth + "-" + iDay;
		} catch (Exception e) {
			sRetDate = null;
		}

		return sRetDate;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public final String getWeekLastDate(String sDate) {
		String sEndDate = null;

		if (sDate == null || sDate.length() == 0)
			sDate = getCurDate();

		int iMonth, iYear, iDay;
		int iIdx1 = sDate.indexOf('-');
		int iIdx2 = sDate.lastIndexOf('-');
		int iIdx3 = sDate.length();
		try {
			iYear = Integer.parseInt(sDate.substring(0, iIdx1));
			iMonth = Integer.parseInt(sDate.substring(iIdx1 + 1, iIdx2));
			iDay = Integer.parseInt(sDate.substring(iIdx2 + 1, iIdx3));
			Calendar calendar = Calendar.getInstance();
			calendar.set(iYear, iMonth - 1, iDay);

			if (calendar != null) {
				int iWeekDay = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
				if (iWeekDay != 0)
					iDay = iDay + (7 - iWeekDay);
				// else
				// iDay = iDay ;

				if (iDay > getMonthDays(iYear, iMonth)) {
					iDay = iDay - getMonthDays(iYear, iMonth);
					if (++iMonth > 12) {
						iMonth = 1;
						iYear++;
					}
				}
			}
			sEndDate = iYear + "-" + iMonth + "-" + iDay;
		} catch (Exception e) {
			sEndDate = null;
		}

		return sEndDate;
	}

	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	public static Date firstDateOfWeek(Date date) {
		Calendar calendar = firstTimeWeek(date);
		return calendar.getTime();
	}

	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	public static Date lastDateOfWeek(Date date) {
		Calendar calendar = lastTimeWeek(date);
		return calendar.getTime();
	}

	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar lastTimeWeek(Date date) {
		Calendar calendar = firstTimeWeek(date);
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar;
	}

	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar firstTimeWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 2);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public final String getPrevWeekDate(String sDate) {
		String sRetDate = null;

		if (sDate == null || sDate.length() == 0)
			sDate = getCurDate();

		int iMonth, iYear, iDay;
		int iIdx1 = sDate.indexOf('-');
		int iIdx2 = sDate.lastIndexOf('-');
		int iIdx3 = sDate.length();
		try {
			iYear = Integer.parseInt(sDate.substring(0, iIdx1));
			iMonth = Integer.parseInt(sDate.substring(iIdx1 + 1, iIdx2));
			iDay = Integer.parseInt(sDate.substring(iIdx2 + 1, iIdx3)) - 7;

			if (iDay <= 0) {
				if (--iMonth < 1) {
					iMonth = 12;
					iYear--;
				}
				iDay = getMonthDays(iYear, iMonth) + iDay;
			}
			if (iYear < 1900)
				sRetDate = null;
			else
				sRetDate = iYear + "-" + iMonth + "-" + iDay;
		} catch (Exception e) {
			sRetDate = null;
		}

		return sRetDate;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public String getNextWeekDate(String sDate) {
		String sRetDate = null;

		if (sDate == null || sDate.length() == 0)
			sDate = getCurDate();

		int iMonth, iYear, iDay;
		int iIdx1 = sDate.indexOf('-');
		int iIdx2 = sDate.lastIndexOf('-');
		int iIdx3 = sDate.length();
		try {
			iYear = Integer.parseInt(sDate.substring(0, iIdx1));
			iMonth = Integer.parseInt(sDate.substring(iIdx1 + 1, iIdx2));
			iDay = Integer.parseInt(sDate.substring(iIdx2 + 1, iIdx3)) + 7;

			int nDays;
			if (iDay > (nDays = getMonthDays(iYear, iMonth))) {
				iDay = iDay - nDays;
				if (++iMonth > 12) {
					iMonth = 1;
					iYear++;
				}
			}

			if (iYear < MAX_YEAR)
				sRetDate = iYear + "-" + iMonth + "-" + iDay;
			else
				return null;
		} catch (Exception e) {
			sRetDate = null;
		}

		return sRetDate;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * @param iBeginNo
	 * 
	 * @return String
	 */
	static public Date getMonthFirstDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		return calendar.getTime();
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * @param iBeginNo
	 * 
	 * @return String
	 */
	static public Date getMonthLastDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.roll(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public String getPrevMonthDate(String sDate) {
		String sRetDate = null;

		if (sDate == null || sDate.length() == 0)
			sDate = getCurDate();

		int iMonth, iYear, iDay;
		try {
			iYear = getYear(sDate);
			iMonth = getMonth(sDate);
			iDay = getDay(sDate);

			iMonth--;

			if (iMonth <= 0) {
				iYear--;
				iMonth = 12;
			}

			if (iYear < 1900)
				sRetDate = null;
			else
				sRetDate = iYear + "-" + iMonth + "-" + iDay;
		} catch (Exception e) {
			sRetDate = null;
		}

		return sRetDate;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public String getNextMonthDate(String sDate) {
		String sRetDate = null;

		if (sDate == null || sDate.length() == 0)
			sDate = getCurDate();

		int iMonth, iYear, iDay;
		try {
			iYear = getYear(sDate);
			iMonth = getMonth(sDate);
			iDay = getDay(sDate);

			iMonth++;
			if (iMonth > 12) {
				iYear++;
				iMonth = 1;
			}

			if (iYear >= MAX_YEAR)
				return null;

			sRetDate = iYear + "-" + iMonth + "-" + iDay;
		} catch (Exception e) {
			sRetDate = null;
		}

		return sRetDate;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public String getPrevDate(String sDate) {
		String sRetDate;

		if (sDate == null || sDate.length() == 0)
			sDate = getCurDate();

		int iYear, iMonth, iDay;
		int iIdx1 = sDate.indexOf('-');
		int iIdx2 = sDate.lastIndexOf('-');
		try {
			iYear = Integer.parseInt(sDate.substring(0, iIdx1));
			iMonth = Integer.parseInt(sDate.substring(iIdx1 + 1, iIdx2));
			iDay = Integer.parseInt(sDate.substring(iIdx2 + 1));
		} catch (Exception e) {
			return null;
		}

		if ((--iDay) < 1) {
			if ((--iMonth) < 1) {
				iMonth = 12;
				iYear--;
			}
			iDay = getMonthDays(iYear, iMonth);
		}

		if (iYear < 1900)
			sRetDate = null;
		else
			sRetDate = iYear + "-" + iMonth + "-" + iDay;

		return sRetDate;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public String getNextDate(String sDate, boolean bExist) {
		String sRetDate;

		if (sDate == null || sDate.length() == 0) {
			if (!bExist)
				sDate = getCurDate();
			else
				return null;
		}

		int iYear, iMonth, iDay;
		int iIdx1 = sDate.indexOf('-');
		int iIdx2 = sDate.lastIndexOf('-');
		try {
			iYear = Integer.parseInt(sDate.substring(0, iIdx1));
			iMonth = Integer.parseInt(sDate.substring(iIdx1 + 1, iIdx2));
			iDay = Integer.parseInt(sDate.substring(iIdx2 + 1));
		} catch (Exception e) {
			return null;
		}

		if (bExist) {
			if (!haveNextDay(iYear, iMonth, iDay))
				return null;
		}

		if (++iDay > getMonthDays(iYear, iMonth)) {
			iDay = 1;
			if (++iMonth > 12) {
				iMonth = 1;
				iYear++;
			}
		}

		if (iYear >= MAX_YEAR)
			return null;

		sRetDate = iYear + "-" + iMonth + "-" + iDay;
		return sRetDate;
	}

	/**
	 * 
	 * 
	 * @param iYear
	 * @param iMonth
	 * @param iDay
	 * 
	 * @return boolean
	 */
	static public boolean haveNextDay(int iYear, int iMonth, int iDay) {
		Calendar today = Calendar.getInstance();

		Calendar tdCompared = Calendar.getInstance();
		tdCompared.set(iYear, iMonth, iDay);

		if (tdCompared.after(today))
			return false;
		else
			return true;
	}

	/**
	 * 
	 * 
	 * @return String
	 */
	static public String getCurTime() {
		Calendar today = Calendar.getInstance();

		SimpleDateFormat formatDateTime = new SimpleDateFormat("HH:mm:ss");

		String sTime = formatDateTime.format(today.getTime());
		sTime = sTime.substring(0, sTime.length() - 2) + "00";

		return sTime;
	}

	/**
	 * 
	 * 
	 * @return String
	 */
	static public String getCurDateTime() {
		Calendar today = Calendar.getInstance();

		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String sTime = formatDateTime.format(today.getTime());
		sTime = sTime.substring(0, sTime.length() - 2) + "00";

		return sTime;
	}

	/**
	 * 
	 * 
	 * @return String
	 */
	static public String getCurDateTimeWithSecond() {
		Calendar today = Calendar.getInstance();

		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String sTime = formatDateTime.format(today.getTime());

		return sTime;
	}

	/**
	 * 
	 * 
	 * @param iYear
	 * @param bExist
	 * 
	 * @return int
	 */
	static public int getNextYear(int iYear, boolean bExist) {
		int iCurYear = getYear();

		if (bExist) {
			if (iYear >= iCurYear)
				return -1;
		}

		iYear++;

		if (iYear >= MAX_YEAR)
			return -1;

		return iYear;
	}

	/**
	 * 
	 * 
	 * @param iYear
	 * 
	 * @return int
	 */
	static public int getPrevYear(int iYear) {
		if (iYear <= 1900)
			return -1;

		return --iYear;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * 
	 * @return String
	 */
	static public String reformDate(String sDate) {
		if (sDate == null || sDate.length() == 0)
			return null;

		return getYear(sDate) + "��" + getMonth(sDate) + "��" + getDay(sDate) + "��";
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * @param nDay
	 * 
	 * @return String
	 */
	static public String addDays(String sDate, int nDay) {
		String sRetDate = sDate;

		for (int i = 0; i < nDay; i++)
			sRetDate = getNextDate(sRetDate, false);

		return sRetDate;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * @return int
	 */
	static public int getAge(String sDate) {
		int i1stMin = sDate.indexOf("-", 0);
		int i2stMin = sDate.indexOf("-", i1stMin + 1);
		int i3stMin = i2stMin + 3;
		if (i2stMin + 3 < sDate.length()) {
			i3stMin = i2stMin + 2;
		} else {
			if (sDate.charAt(i2stMin + 2) == ' ') {
				i3stMin = i2stMin + 2;
			}
		}
		int iYear = Integer.parseInt(sDate.substring(0, i1stMin));
		int iMonth = Integer.parseInt(sDate.substring(i1stMin + 1, i2stMin));
		int iDay = Integer.parseInt(sDate.substring(i2stMin + 1, i3stMin));

		Calendar calendar = Calendar.getInstance();
		int iSysYear = calendar.get(Calendar.YEAR);
		int iSysMonth = calendar.get(Calendar.MONTH) + 1;
		int iSysDay = calendar.get(Calendar.DATE);

		int iAge = iSysYear - iYear;
		if (iSysDay - iDay < 0) {
			iSysMonth--;
		}
		if (iSysMonth - iMonth < 0) {
			iAge--;
		}
		return iAge;
	}

	/**
	 * 
	 * 
	 * @param sDate
	 * @param pattern
	 * @return int
	 */
	static public int getAge(String sDate, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		Date d = sdf.parse(sDate);

		return getAge(d);
	}

	/**
	 * 
	 * 
	 * @param date
	 * @return int
	 */
	static public int getAge(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int iYear = cal.get(Calendar.YEAR);
		int iMonth = cal.get(Calendar.MONTH) + 1;
		int iDay = cal.get(Calendar.DATE);

		Calendar calendar = Calendar.getInstance();
		int iSysYear = calendar.get(Calendar.YEAR);
		int iSysMonth = calendar.get(Calendar.MONTH) + 1;
		int iSysDay = calendar.get(Calendar.DATE);

		int iAge = iSysYear - iYear;
		if (iSysDay - iDay < 0) {
			iSysMonth--;
		}
		if (iSysMonth - iMonth < 0) {
			iAge--;
		}
		return iAge;
	}

	/**
	 * 
	 * 
	 * @param birthday
	 * @param effectiveDate
	 * @return
	 */
	public static int computeAge(Date birthday, Date effectiveDate) {
		return computeAge(birthday.getTime(), effectiveDate.getTime());
	}

	/**
	 * 
	 * 
	 * @param birthday
	 * @return
	 */
	public static int computeAge(Date birthday) {
		return computeAge(birthday.getTime(), System.currentTimeMillis());
	}

	/**
	 * 
	 * 
	 * @param birthday
	 * @param effective
	 * @return
	 */
	public static int computeAge(long birthday, long effective) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(birthday);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(effective);

		int ret = cal2.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		if (cal.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR))
			ret = ret - 1;
		return ret;
	}

	/**
	 * 取得当前日期
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
		Date now = new Date();
		String date = sf.format(now);
		return date;
	}

	/**
	 * 取得指定时间所在的开始时间和结束时间。一天之内。 结束时间为23:59:59 日期格式为yyyy/MM/dd
	 * 
	 * @param queryDate
	 * @return
	 */
	public static Date[] getStartAndEnd(String queryDate) {
		String s1 = queryDate + " 00:00:00";
		String s2 = queryDate + " 23:59:59";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			Date d1 = sf.parse(s1);
			Date d2 = sf.parse(s2);
			return new Date[] { d1, d2 };
		} catch (Exception e) {
		}
		return null;

	}

	/**
	 * 取得当前季度
	 * 
	 * @return
	 */
	public static int getQuarter() {
		int season = 0;

		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4;
			break;
		default:
			break;
		}
		return season;
	}

	/**
	 * 判断一个日期是否在两个日期之间
	 * 
	 * @param d
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int between(Date d, Date d1, Date d2) {
		if (d.compareTo(d1) < 0) {
			return -1;
		}
		if (d.compareTo(d2) > 0)
			return 1;

		return 0;
	}

	public static long getDateInterval(Timestamp min, Timestamp max) {
		long l = (max.getTime() - min.getTime()) / (3600 * 24 * 1000);
		return l;
	}

	/**
	 * 判断两天是否是同一天
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static final boolean isSameDate(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String s1 = sf.format(d1);
		String s2 = sf.format(d2);
		return s1.equals(s2);
	}

	/**
	 * 取得今天起始日期和结束日期
	 * 
	 * @return
	 */
	public static Date[] getToday() {
		Date today = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

		String prefix = sf.format(today);
		String begin = prefix + " 00:0:00";
		String end = prefix + " 23:59:59";

		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return new Date[] { sf2.parse(begin), sf2.parse(end) };
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算间隔天数
	 * 
	 * @param d1
	 * @param d2
	 */
	public static int computeInterval(Date d1, Date d2) {
		int betweenDate = (int) ((d1.getTime() - d2.getTime()) / (24 * 60 * 60 * 1000));
		return betweenDate;
	}

	public static String format(Date date, String parttern) {
		SimpleDateFormat sf = new SimpleDateFormat(parttern);
		return sf.format(date);
	}

	/**
	 * @Description 获取当前中国时区的TIMESTAMP日期
	 * @return
	 */
	public static Timestamp getSysTimestamp() {
		final TimeZone zone = TimeZone.getTimeZone("GMT+8");// 获取中国时区
		TimeZone.setDefault(zone);// 设置时区
		return new Timestamp((new java.util.Date()).getTime());
	}

	/**
	 * 格式日期为字符串内容
	 * <p>
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            日期格式,例： yyyyMMddHHmmss
	 * @return String 格式后的字符串日期
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 格式long类型日期为 Date
	 * <p>
	 * 
	 * @param time
	 *            long类型日期
	 * @return Date
	 */
	public static Date formatDate(long time) {
		return new Date(time);
	}

	/**
	 * 格式化字符串
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date format(String dateStr, String pattern) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(pattern);
			return sf.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断是否在指定日期段内
	 * 
	 * @param end
	 * @param interval
	 * @return
	 */
	public static boolean isIn(Date start, int interval) {
		Calendar c = Calendar.getInstance();
		c.setTime(start);

		c.add(Calendar.DATE, interval);

		Date end = c.getTime();

		Date now = new Date();
		return between(now, start, end) == 0;
	}

	public static Date nextYear(Date time) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(time);
		cal.add(GregorianCalendar.YEAR, 1);// 在年上加1
		return cal.getTime();
	}
	
	public static int getHour (String str){
		SimpleDateFormat sf = new SimpleDateFormat("HH");
		return Integer.parseInt(sf.format(str));
	}
	
	public static int getMinute (String str){
		SimpleDateFormat sf = new SimpleDateFormat("mm");
		return Integer.parseInt(sf.format(str));
	}
}
