package com.tanlong.exercise.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具
 *
 * @author zhangpu
 */
public class DateUtil {
    /**
     * 缺省的日期显示格式： yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期时间显示格式：MM-dd HH:mm
     */
    public static final String NO_YEAR_DATETIME = "MM-dd HH:mm";

    /**
     * 日期时间显示格式：MM-dd HH:mm
     */
    public static final String NO_SECOND_DATETIME = "yyyy-MM-dd HH:mm";

    /**
     * 日期时间显示格式：MM-dd
     */
    public static final String MONTH_AND_DAY_DATETIME = "MM-dd";

    public static final String HOUR_MINUTE = "HH:mm";

    /**
     * 获取当前时间戳
     * @return
     */
    public static long getMillis(){
        return System.currentTimeMillis();
    }

    /**
     * 获得当前日期
     *
     * @return 当前日期 形式为yyyy-MM-dd
     */
    public static String getCurrentDate() {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return simpleFormat.format(new Date());
    }

    /**
     * 获得当前日期时间
     *
     * @return 当前日期时间 形式为yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT,
                Locale.getDefault());
        return simpleFormat.format(new Date());
    }

    /**
     * 得到当前时间，Date形式
     *
     * @return -- 当前时间，Date形式
     */
    public static Date getNowDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获得传入日期的当天开始时间，单位为毫秒
     * @param date -- 传入日期
     * @return 传入日期的当天开始时间，单位为毫秒
     * @throws ParseException
     */
    public static long getTodayBegin(Date date) throws ParseException {
        String today = getDateTime(date, DEFAULT_DATE_FORMAT);
        Date todayDate = changeStringToDate(today, DEFAULT_DATE_FORMAT);
        return todayDate.getTime();
    }


    /**
     * 将传入的Date转化为 yyyy-MM-dd HH:mm:ss 形式
     *
     * @param date -- 欲格式化时间
     * @return 格式化后的时间字符串，形式为yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT,
                Locale.getDefault());
        return simpleFormat.format(date);
    }

    /**
     * 将传入的时间（单位为秒）转化为 yyyy-MM-dd HH:mm:ss 形式
     *
     * @param time -- 欲格式化时间，单位为s
     * @return 格式化后的时间字符串，形式为yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(int time) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT,
                Locale.getDefault());
        return simpleFormat.format(new Date(time * 1000L));
    }

    /**
     * 将传入的时间字符串按照格式转换成Date
     *
     * @param time     -- 时间字符串
     * @param template -- 格式化的时间格式,如yyyy-MM-dd HH:mm:ss, 要和time匹配
     * @return 转换后的Date
     * @throws ParseException
     */
    public static Date changeStringToDate(String time, String template) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(template, Locale.getDefault());
        return simpleDateFormat.parse(time);
    }

    /**
     * 将传入的时间字符串转换成期望格式的时间字符串
     * @param time -- 输入时间字符串
     * @param srcTemplate -- 输入时间字符串对应的时间格式，要和time匹配
     * @param tarTemplate -- 输出时间字符串对应的时间格式
     * @return 格式化后的时间字符串
     * @throws ParseException
     */
    public static String changeStringToString(String time, String srcTemplate, String tarTemplate)
            throws ParseException {
        Date temp = changeStringToDate(time, srcTemplate);
        return getDateTime(temp, tarTemplate);
    }

    /**
     * 计算两个日期的时间差，dateEnd - dateStart, 单位为秒
     *
     * @param dateStart -- 第一个时间
     * @param dateEnd   -- 第二个时间
     * @return 时间差，单位为秒
     */
    public static long getTimeInterval(Date dateStart, Date dateEnd) {
        return (dateEnd.getTime() - dateStart.getTime()) / 1000;
    }

    /**
     * 得到用指定方式格式化的日期
     *
     * @param date    需要进行格式化的日期
     * @param pattern 显示格式
     * @return 日期时间字符串
     */
    public static String getDateTime(Date date, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 得到周几
     * @param time 时间字符串, 必须是yyyy-MM-dd格式
     * @return 返回周几
     */
    public static String getWeek(String time) {
        String Week = "周";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//也可将此值当参数传进来
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week += "日";
                break;
            case 2:
                Week += "一";
                break;
            case 3:
                Week += "二";
                break;
            case 4:
                Week += "三";
                break;
            case 5:
                Week += "四";
                break;
            case 6:
                Week += "五";
                break;
            case 7:
                Week += "六";
                break;
            default:
                break;
        }
        return Week;
    }

    /**
     * 获得传入时间字符串是周几
     * @param time -- 传入时间字符串
     * @param template -- 时间字符串的格式
     * @return 周X，如周一
     * @throws ParseException
     */
    public static String getWeek(String time, String template) throws ParseException {
        String target = changeStringToString(time, template, DEFAULT_DATE_FORMAT);
        return getWeek(target);
    }


    /**
     * 对传入Number补零
     * @param number -- 传入数字
     * @return 补零后的字符串
     */
    private static String fillZero(int number) {
        StringBuilder result = new StringBuilder();
        if (number > 9) {
            result.append(number);
        } else if (number > 0 && number < 10) {
            result.append(0).append(number);
        } else {
            result.append("00");
        }
        return result.toString();
    }

    /**
     * 将传入时间转换为HH:MM:SS显示格式
     *
     * @param time -- 传入时间，单位为秒
     * @return time > 0 时，返回HH:MM:SS显示格式的时间字符串，否则返回空字符串
     */
    public static String convertTime(long time) {
        if (time <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int h;
        if ((h = (int) time / (60 * 60)) > 0) {// 小时
            sb.append(fillZero(h));
            sb.append(":");
        } else {
            sb.append("00:");
        }
        if ((h = (int) time % (60 * 60) / 60) > 0) {// 分钟
            sb.append(fillZero(h));
            sb.append(":");
        } else {
            sb.append("00:");
        }
        if ((h = (int) time % (60)) >= 0) {// 秒
            sb.append(fillZero(h));
        }
        return sb.toString();
    }
}
