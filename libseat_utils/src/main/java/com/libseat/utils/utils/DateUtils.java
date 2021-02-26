package com.libseat.utils.utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

 
 
 
 
public class DateUtils {
 
 
    public final static String YYYY_MM_DD_HH_MM    = "yyyy-MM-dd HH:mm";
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYY_MM_DD_HH       = "yyyy-MM-dd HH";
    public final static String YYYY_MM_DD          = "yyyy-MM-dd";
    public final static String YYYY_MM             = "yyyy-MM";
    public final static String YYYYMMDD            = "yyyyMMdd";
    public final static String YYYYMMDDHH          = "yyyyMMddHH";
    public final static String YYYYMMDDHHMMSS      = "yyyyMMddHHmmss";
    public final static String YYYYMMDDHHMM        = "yyyyMMddHHmm";

    public static final long SECONDS = 1000;
    public static final long MINUTE = SECONDS * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;
 
 
    /**
     * 通过制定的格式，将日期字符串解析为java.util.Date对象
     * 
     * @param dateStr 日期字符串
     * @param formatStr 解析的格式
     * @return 转换后的结果：Date对象
     * @throws ParseException
     */
    public static Date strToDate(String dateStr, String formatStr) {
        Date date = null;
        if (StringUtils.isNotBlank(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 通过制定的格式，将日期字符串解析为时间戳
     *
     * @param dateStr 日期字符串
     * @param formatStr 解析的格式
     * @return 转换后的结果：Date对象
     * @throws ParseException
     */
    public static long strToTime(String dateStr, String formatStr) {
        Date date = strToDate(dateStr, formatStr);
        return date==null?0L:date.getTime();
    }

    /**
     * 通过制定的格式，将日期字符串解析为Timestamp对象
     *
     * @param dateStr 日期字符串
     * @param formatStr 解析的格式
     * @return 转换后的结果：Date对象
     * @throws ParseException
     */
    public static Timestamp strToTimestamp(String dateStr, String formatStr) {
        long time = strToTime(dateStr, formatStr);
        return time==0L?null:new Timestamp(time);
    }
 
 
    /**
     * 根据一个完整的日期，格式化成一个只到分钟的时间.yyyy-MM-dd HH:mm
     * 
     * @param date
     * @param formatFormat
     * @return
     */
    public static Date getMinuteDate(String date, String formatFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(strToDate(date, formatFormat));
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
 
 
    /**
     * 根据一个完整的日期，格式化成一个只到天的时间.yyyy-MM-dd
     * 
     * @param date
     * @param formatFormat
     * @return
     */
    public static Date getDayDate(String date, String formatFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(strToDate(date, formatFormat));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
 
 
    /**
     * 通过制定的格式，将Date对象格式化为字符串
     * 
     * @param date 需要转换的Date对象
     * @param formatStr 转换的格式
     * @return 转换之后的字符串
     */
    public static String dateToStr(Date date, String formatStr) {
        String result = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            result = sdf.format(date);
        }
        return result;
    }
 
 
    /**
     * 将字符串,格式化.
     * 
     * @param date
     * @param format
     * @return
     */
    public static String parseDateStr(String date, String format) {
        if (StringUtils.isEmpty(date) || StringUtils.isEmpty(format)) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(format);
 
 
        return f.format(strToDate(date, format));
    }
 
 
    /**
     * 将一个字符串日期转换成另一个字符串日期.
     * 
     * @param date 日期对象
     * @param fromFormat 原来的日期格式
     * @param toFormat 转换后的日期格式
     * @return
     */
    public static String parseDateStr(String date, String fromFormat, String toFormat) {
        return dateToStr(strToDate(date, fromFormat), toFormat);
    }
 
 
    //
    /**
     * 获取当前系统时间
     * 
     * @return
     */
    public static Date getCurrDate() {
        return new Date();
    }
 
 
    /**
     * 获取当前指定格式 的系统时间，可以改变分钟的数值 ，加或者减，例如：-1，10.
     * 
     * @param formatStr 日期的格式化.
     * @param changeMinute 正数或者负数。
     * @return
     */
    public static String getCurrCustomMinuteDate(String formatStr, int changeMinute) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getCurrDate());
        cal.add(Calendar.MINUTE, changeMinute);
        return dateToStr(cal.getTime(), formatStr);
    }
 
 
    /**
     * 获取当前指定格式的系统时间
     * 
     * @param formatStr
     * @return
     */
    public static String getCurrDate(String formatStr) {
        return dateToStr(getCurrDate(), formatStr);
    }
 
 
    /**
     * 获取指定日期的小时 如果date为nul则返回-1.
     * 
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
 
 
    /**
     * 获取指定日期的天,如果date为null则返回-1;
     * 
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
 
 
    /**
     * 获取当月最大的天数.
     * 
     * @param date
     * @return
     */
    public static int getMonthMaxDay(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DATE);
    }
 
 
    /**
     * 获取当前日期对应的上一个月的日期.
     * 
     * @return
     */
    public static Date getCurrUpMonthDate() {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }
 
 
    /**
     * 获取当前日期对应的上一天的日期.
     * 
     * @return
     */
    public static Date getCurrUpDayDate() {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }

    /**
     * 获取今天的开始时间
     * @param time
     * @return
     */
    public static long getDayStartTime(long time) {
        return time / DAY * DAY;
    }

    /**
     * 获取当天已经过了多长时间。
     *
     * @param time
     * @return
     */
    public static long getDayGoneTime(long time) {
        return time - getDayStartTime(time);
    }


    public static void main(String[] args) {
 
 
        String s = dateToStr(getCurrUpMonthDate(), YYYY_MM_DD_HH_MM_SS);
        System.out.println(s);
    }
}