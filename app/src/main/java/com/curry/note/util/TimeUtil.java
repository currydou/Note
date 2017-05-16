package com.curry.note.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具--ldz
 */
public class TimeUtil {
    public final static String FORMAT_MONTH_DAY = "MM-dd";
    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_DATE2 = "yyyyMMdd";
    public final static String FORMAT_TIME = "hh:mm";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 HH:mm";
    public final static String FORMAT_PICTURE = "yyyyMMdd_hhmm_ss";
    public final static String FORMAT_YEAR_MONTH_DAY = "yyyy年MM月dd日";
    public final static String FORMAT_YEAR_MONTH = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_Y_M_D_H_M_2 = "yyyy年MM月dd日 HH:mm";
    public final static String FORMAT_H_M_S = " HH:mm:ss";
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    private static final int THREEDAYS = DAY * 3;

    /**
     * 根据时间戳获取描述性时间，     如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为秒----(因为服务端返回的时间戳是秒)
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        try {
            long currentTime = System.currentTimeMillis();
            long timeGap = (currentTime - timestamp * 1000) / 1000;// 与现在时间相差秒数
            String timeStr = null;
            if (timeGap > YEAR) {// 去年及以前：显示x年X月X日 X:X
                // timeStr = timeGap / YEAR + "年前";
                timeStr = getFormatTimeFromTimestamp(timestamp * 1000, FORMAT_DATE_TIME);
            } else if (timeGap > THREEDAYS && timeGap < YEAR) {// 3天以上显示X月X日 X:X
                // timeStr = timeGap
                // / MONTH + "个月前";
                timeStr = getFormatTimeFromTimestamp(timestamp * 1000, FORMAT_MONTH_DAY_TIME);
            } else if (timeGap > DAY && timeGap < THREEDAYS) {// 1天以上3天以下
                timeStr = timeGap / DAY + "天前";
            } else if (timeGap > HOUR) {// 1小时-24小时
                timeStr = timeGap / HOUR + "小时前";
            } else if (timeGap > MINUTE) {// 1分钟-59分钟
                timeStr = timeGap / MINUTE + "分钟前";
            } else {// 1秒钟-59秒钟
                timeStr = "刚刚";
            }
            return timeStr;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据时间戳获取描述性时间，如1天前, 5天前，06-11, 2年前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getFormatTimeFromTimestamp(long timestamp) {
        try {
            long currentTime = System.currentTimeMillis();
            long timeGap = (currentTime - timestamp ) / 1000;// 与现在时间相差秒数
            String timeStr = null;
            if (timeGap > YEAR) {// 1年以前：显示 X年前
                timeStr = timeGap / YEAR + "年前";
            } else if (timeGap > MONTH) {// 30天以上1年以下：显示 月份-日期
                timeStr = getFormatTimeFromTimestamp(timestamp * 1000, FORMAT_MONTH_DAY);
            } else if (timeGap > DAY) {// 1天以上
                timeStr = timeGap / DAY + "天前";
            } else if (timeGap > HOUR) {// 1小时-24小时
                timeStr = timeGap / HOUR + "小时前";
            } else if (timeGap > MINUTE) {// 1分钟-59分钟
                timeStr = timeGap / MINUTE + "分钟前";
            } else {// 1秒钟-59秒钟
                timeStr = "刚刚";
            }
            return timeStr;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据时间戳获取指定格式的时间，如2011-11-30 08:40
     *
     * @param timestamp 时间戳 单位为毫秒
     * @param format    指定格式 如果为null或空串则使用默认格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getFormatTimeFromTimestamp(long timestamp, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int year = Integer.valueOf(sdf.format(new Date(timestamp)).substring(0, 4));
            if (currentYear == year) {// 如果为今年则不显示年份
                sdf.applyPattern(FORMAT_MONTH_DAY_TIME);
            } else {
                sdf.applyPattern(FORMAT_DATE_TIME);
            }
        } else {
            sdf.applyPattern(format);
        }
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    /**
     * 根据时间戳获取时间字符串，并根据指定的时间分割数partionSeconds来自动判断返回描述性时间还是指定格式的时间
     *
     * @param timestamp      时间戳 单位是毫秒
     * @param partionSeconds 时间分割线，当现在时间与指定的时间戳的秒数差大于这个分割线时则返回指定格式时间，否则返回描述性时间
     * @param format
     * @return
     */
    public static String getMixTimeFromTimestamp(long timestamp, long partionSeconds, String format) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        if (timeGap <= partionSeconds) {
            return getDescriptionTimeFromTimestamp(timestamp);
        } else {
            return getFormatTimeFromTimestamp(timestamp, format);
        }
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    /**
     * 将日期字符串以指定格式转换为Date
     *
     * @param timeStr 日期字符串
     * @param format  指定的日期格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static Date getTimeFromString(String timeStr, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        try {
            return sdf.parse(timeStr);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 将Date以指定格式转换为日期时间字符串
     *
     * @param time   日期
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getStringFromTime(Date time, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(time);
    }

    /**
     * 将时间戳转化成日期时间字符串
     *
     * @param cc_time 日期
     * @param format  指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getStrTime(String cc_time, String format) {
        String re_StrTime = null;
        SimpleDateFormat sdf = null;
        if (TextUtils.isEmpty(format)) {
            sdf = new SimpleDateFormat(FORMAT_DATE_TIME);
        } else {
            try {
                sdf = new SimpleDateFormat(format);
            } catch (Exception e) {
                sdf = new SimpleDateFormat(FORMAT_DATE_TIME);
            }
        }
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 根据秒数转换时间格式
     *
     * @param pattern  格式
     * @param dateTime 秒
     * @return
     */
    public static String getFormatedDateTime(String pattern, long dateTime) {

        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }

    public static String getStrTime2(int cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH);
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    public static String getStrDate(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    public static void main(String[] main) {
        System.out.print(getFormatTimeFromTimestamp(5656, "mm:ss"));
    }
}
