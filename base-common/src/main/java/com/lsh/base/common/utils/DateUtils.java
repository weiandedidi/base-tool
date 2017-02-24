package com.lsh.base.common.utils;

import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * yyyy-MM-dd
     */
    public static final SimpleDateFormat FORMAT_DATE_WITH_BAR = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * yyyyMMdd
     */
    public static final SimpleDateFormat FORMAT_DATE_NO_BAR = new SimpleDateFormat("yyyyMMdd");
    /**
     * yyyy年MM月dd日
     */
    public static final SimpleDateFormat FORMAT_DATE_CHINESE = new SimpleDateFormat("yyyy年MM月dd日");
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final SimpleDateFormat FORMAT_TIME_WITH_BAR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final SimpleDateFormat FORMAT_TIME_WITH_MINUTE = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /**
     * yyyyMMddHHmmss
     */
    public static final SimpleDateFormat FORMAT_TIME_NO_BAR = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * yyyyMMddHHmmssS
     */
    public static final SimpleDateFormat FORMAT_MILLISECOND_NO_BAR = new SimpleDateFormat("yyyyMMddHHmmssS");
    /**
     * yyyyMM
     */
    public static final SimpleDateFormat FORMAT_MONTH_1 = new SimpleDateFormat("yyyyMM");

    /**
     * 格式化成：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return FORMAT_TIME_WITH_BAR.format(date);
    }

    /**
     * 毫秒数格式化成：yyyy-MM-dd HH:mm:ss
     *
     * @param timeMillis
     * @return
     */
    public static String formatTimeMillis(long timeMillis) {
        return format(new Date(timeMillis));
    }

    /**
     * 匹配格式：yyyy-MM-dd HH:mm:ss
     *
     * @param str
     * @return
     */
    public static Date parse(String str) {
        try {
            return FORMAT_TIME_WITH_BAR.parse(str);
        } catch (Exception ex) {
            logger.error("Date parse exception: str = {}", str);
        }
        return null;
    }

    /**
     * 格式化成：yyyyMM
     *
     * @param date
     * @return
     */
    public static int getYearMonth(Date date) {
        return ObjUtils.toInteger(FORMAT_MONTH_1.format(date));
    }

    /**
     * 格式化成：yyyyMMdd
     *
     * @param date
     * @return
     */
    public static int getYearMonthDay(Date date) {
        return ObjUtils.toInteger(FORMAT_DATE_NO_BAR.format(date));
    }

    /**
     * 获取年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int daysBetween(Date smallDate,Date bigDate){

        long between_days=(bigDate.getTime()-smallDate.getTime())/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取当前秒数，redis中只精确到秒
     *
     * @return
     */
    public static Long getCurrentSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当天0点的秒数
     *
     * @return
     */
    public static Long getTodayBeginSeconds() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获取指定日期的目录  2015/06/28/
     *
     * @param date
     * @return
     */
    public static String getDateFilePath(Date date) {
        StringBuilder sb = new StringBuilder();
        sb.append("yyyy").append(File.separator).append("MM").append(File.separator).append("dd");
        return new SimpleDateFormat(sb.toString()).format(date);
    }

    public static void main(String[] args) {

    }

}
