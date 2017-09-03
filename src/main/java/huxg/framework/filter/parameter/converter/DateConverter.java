/*
 * @(#)DateConverter.java 2008-5-29
 * Copyright 2008 Thunisoft,Inc. All rights reserved.
 * Company: 
 */
package huxg.framework.filter.parameter.converter;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <br>
 * <br>
 * <br>
 * <ul>
 * <li></li>
 * <li><br>
 * </li>
 * <li></li>
 * </ul>
 * 
 * @author zhangjy
 * @version 1.0, 2008-5-29
 */
public class DateConverter implements Converter {

    private static final Log logger = LogFactory.getLog(DateConverter.class);

    /**
     * 
     */
    public static final String DEF_DATE_STYLE_REGEX = "^((?:\\d{2})?\\d{2})-(0?[1-9]|1[012])-(0?[1-9]|[12]\\d|3[01])(?!\\d)";
    /**
     * 
     */
    public static final String DEF_TIME_STYLE_REGEX = "(?<!\\d)(2[0-3]|[01]?\\d):([0-5]?\\d):([0-5]?\\d)$";

    // ----------------------------------------------------------- Constructors

    /**
     * Create a {@link Converter} that will throw a {@link ConversionException}
     * if a conversion error occurs.
     */
    public DateConverter() {
        initPattern(DEF_DATE_STYLE_REGEX, DEF_TIME_STYLE_REGEX);

        this.defaultValue = null;
        this.useDefault = false;

    }

    /**
     * Create a {@link Converter} that will return the specified default value
     * if a conversion error occurs.
     * 
     * @param defaultValue The default value to be returned
     */
    public DateConverter(Object defaultValue) {
        initPattern(DEF_DATE_STYLE_REGEX, DEF_TIME_STYLE_REGEX);

        this.defaultValue = defaultValue;
        this.useDefault = true;

    }

    /**
     * @param dateRegex 
     * @param timeRegex 
     */
    public DateConverter(String dateRegex, String timeRegex) {
        initPattern(dateRegex, timeRegex);

        this.defaultValue = null;
        this.useDefault = false;
    }

    /**
     * @param dateRegex 
     * @param timeRegex 
     * @param defaultValue The default value to be returned
     */
    public DateConverter(String dateRegex, String timeRegex, Object defaultValue) {
        initPattern(dateRegex, timeRegex);

        this.defaultValue = defaultValue;
        this.useDefault = true;
    }

    /**
     * 
     */
    protected void initPattern(String dateRegex, String timeRegex) {
        if (dateRegex != null) {
            datePat = Pattern.compile(dateRegex);
        }
        if (timeRegex != null) {
            timePat = Pattern.compile(timeRegex);
        }
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * 
     */
    protected Pattern datePat = null;

   /**
    * 
    */
    protected Pattern timePat = null;

    /**
     * The default value specified to our Constructor, if any.
     */
    protected Object defaultValue = null;

    /**
     * Should we return the default value on conversion errors?
     */
    protected boolean useDefault = true;

    // --------------------------------------------------------- Public Methods

    /**
     * Convert the specified input object into an output object of the specified
     * type.
     * 
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     * @exception ConversionException if conversion cannot be performed
     *                successfully
     */
    public Object convert(Class type, Object value) {
        if (logger.isDebugEnabled() == true) {
            logger.debug("input: " + value);
        }
        if (value == null) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException("No value specified");
            }
        }

        if (value instanceof Calendar) {
            Calendar cal = (Calendar) value;
            return cal.getTime();
        } else if (value instanceof Date) {
            return (Date) value;
        }

        //
        String sValue = value.toString().trim();
        if (sValue.length() == 0) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException("No value specified");
            }
        }

        // null
        if (datePat == null && timePat == null) {
            return null;
        }
        //
        try {
            Calendar cal = convertDateField(sValue, null);
            cal = convertTimeField(sValue, cal);

            Date dateTime = (cal == null ? null : cal.getTime());
            if (logger.isDebugEnabled() == true) {
                logger.debug("output: " + dateTime);
            }

            return dateTime;
        } catch (Exception e) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException(e);
            }
        }
    }

    /**
     * 
     * @param sValue 
     * @param cal Calendar
     * @return (Calendar)
     */
    protected Calendar convertDateField(String sValue, Calendar cal) {
        if (datePat == null) {
            return cal;
        }
        Matcher matcher = datePat.matcher(sValue);
        if (!matcher.find()) {
            return cal;
        }
        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int day = Integer.parseInt(matcher.group(3));

        if (cal == null) {
            cal = Calendar.getInstance();
        }
        // 
        if (year < 100) {
           
            int shortYear = cal.get(Calendar.YEAR) % 100;
            if (shortYear >= year) {
                year = (cal.get(Calendar.YEAR) - shortYear) + year;
            } else {
                //
                year = (cal.get(Calendar.YEAR) - shortYear - 100) + year;
            }
        }
        cal.set(Calendar.YEAR, year);
        //
        setMonthField(cal, month);
        //
        cal.set(Calendar.DAY_OF_MONTH, day);

        return cal;
    }

    /**
     * 
     * 
     * @param sValue
     * @param cal Calendar
     * @return (Calendar)
     */
    protected Calendar convertTimeField(String sValue, Calendar cal) {
        if (timePat == null) {
            return cal;
        }
        Matcher matcher = timePat.matcher(sValue);
        if (!matcher.find()) {
            return cal;
        }
        int hour = Integer.parseInt(matcher.group(1));
        int min = Integer.parseInt(matcher.group(2));
        int sec = Integer.parseInt(matcher.group(3));

        if (cal == null) {
            cal = Calendar.getInstance();
        }
        // 
        cal.set(Calendar.HOUR_OF_DAY, hour);
        // 
        cal.set(Calendar.MINUTE, min);
        // 
        cal.set(Calendar.SECOND, sec);

        return cal;
    }

    /**
     * 
     * 
     * @param cal Calendar
     * @param month
     */
    protected static void setMonthField(Calendar cal, int month) {
        switch (month) {
        case 1:
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            break;
        case 2:
            cal.set(Calendar.MONTH, Calendar.FEBRUARY);
            break;
        case 3:
            cal.set(Calendar.MONTH, Calendar.MARCH);
            break;
        case 4:
            cal.set(Calendar.MONTH, Calendar.APRIL);
            break;
        case 5:
            cal.set(Calendar.MONTH, Calendar.MAY);
            break;
        case 6:
            cal.set(Calendar.MONTH, Calendar.JUNE);
            break;
        case 7:
            cal.set(Calendar.MONTH, Calendar.JULY);
            break;
        case 8:
            cal.set(Calendar.MONTH, Calendar.AUGUST);
            break;
        case 9:
            cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
            break;
        case 10:
            cal.set(Calendar.MONTH, Calendar.OCTOBER);
            break;
        case 11:
            cal.set(Calendar.MONTH, Calendar.NOVEMBER);
            break;
        case 12:
            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            break;
        default:
            throw new ConversionException("�·ݵ�ֵ���Ϸ�!");
        }
    }

}
