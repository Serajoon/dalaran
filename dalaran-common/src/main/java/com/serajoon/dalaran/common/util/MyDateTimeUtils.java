package com.serajoon.dalaran.common.util;

import com.google.common.collect.Maps;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Map;

public abstract class MyDateTimeUtils {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 开始时间
     */
    public static final String START_TIME = "startTime";

    /**
     * 结束时间
     */
    public static final String END_TIME = "endTime";

    /**
     * 返回当前日期
     * @return Date date
     */
    public Date getCurrentDate(){
        return new Date();
    }


    /**
     * 返回当前日期 格式 yyyy-MM-dd
     *
     * @return LocalDate
     * @author hm 2019/1/7 17:51
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    /**
     * 返回当前日期 格式 yyyy-MM-dd HH:mm:ss
     *
     * @return LocalDateTime
     * @author hm 2019/1/7 17:51
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 返回当前日期 格式 yyyy-MM-dd
     *
     * @return string
     * @author hm 2019/1/7 17:51
     */
    public static String getCurrentDateStr() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * 返回当前时间 格式 yyyy-MM-dd HH:mm:ss
     *
     * @return string
     * @author hm 2019/1/7 17:51
     */
    public static String getCurrentDateTimeStr() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    /**
     * 返回当前的年份
     */
    public static int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }


    /**
     * 返回当前的月份
     */
    public static int getCurrentMonth() {
        return LocalDateTime.now().getMonthValue();
    }

    /**
     * 返回当前的日期
     */
    public static int getCurrentDay() {
        return LocalDateTime.now().getDayOfMonth();
    }

    /**
     * <p>
     *     例如
     *     返回本周周一对应的日期
     *     MyDateTimeUtils.getDayOfWeek(DayOfWeek.MONDAY)
     * </p>
     * @param   dayOfWeek 返回本周中星期对应的日期
     * @return  LocalDate 日期
     * @author  hanmeng1
     * @since  2019/2/25 10:37
     */
    public static LocalDate getDayOfWeek(DayOfWeek dayOfWeek){
        LocalDate today = LocalDate.now();
        return today.with(dayOfWeek);
    }

    /**
     * <p>
     *     例如:
     *     获得本年的第一天的日期:MyDateTimeUtils.getDayOfTime(TemporalAdjusters.firstDayOfYear())
     *     获得本月的第一天的日期:MyDateTimeUtils.getDayOfTime(TemporalAdjusters.firstDayOfMonth())
     * </p>
     * @param temporalAdjuster
     * @return
     */
    public static LocalDate getDayOfTime(TemporalAdjuster temporalAdjuster){
        return LocalDate.now().with(temporalAdjuster);
    }

    /**
     * 获得本周周一(开始时间)和当前时间(结束时间)的Map
     */
    public static Map<String, LocalDate> getCurrentLocalDateMapByWeek() {
        LocalDate startDayOfWeek = MyDateTimeUtils.getDayOfWeek(DayOfWeek.MONDAY);
        LocalDate currentLocalDate = MyDateTimeUtils.getCurrentLocalDate();
        Map<String, LocalDate> mapByWeek = Maps.newHashMap();
        mapByWeek.put(START_TIME, startDayOfWeek);
        mapByWeek.put(END_TIME, currentLocalDate);
        return mapByWeek;
    }

    /**
     * 获得当月的第一天(开始时间)和当前时间(结束时间)Map
     */
    public static Map<String, LocalDate> getCurrentLocalDateMapByMonth() {
        LocalDate startDayOfMonth = MyDateTimeUtils.getDayOfTime(TemporalAdjusters.firstDayOfMonth());
        LocalDate currentLocalDate = MyDateTimeUtils.getCurrentLocalDate();
        Map<String, LocalDate> mapByMonth = Maps.newHashMap();
        mapByMonth.put(START_TIME, startDayOfMonth);
        mapByMonth.put(END_TIME, currentLocalDate);
        return mapByMonth;
    }

    /**
     * 获得本年第一天(开始时间)和当前时间(结束时间)
     */
    public static Map<String, LocalDate> getCurrentLocalDateMapByYear() {
        LocalDate startDayOfYear = MyDateTimeUtils.getDayOfTime(TemporalAdjusters.firstDayOfYear());
        LocalDate currentLocalDate = MyDateTimeUtils.getCurrentLocalDate();
        Map<String, LocalDate> mapByYear = Maps.newHashMap();
        mapByYear.put(START_TIME, startDayOfYear);
        mapByYear.put(END_TIME, currentLocalDate);
        return mapByYear;
    }


    /**
     * Date类型转时间字符串字符串yyyy-MM-dd
     *
     * @param date
     * @return string
     * @author hanmeng1
     * @since 2019/2/16 10:02
     */
    public static String transformDateToStr(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate.format(DATE_FORMATTER);
    }

    /**
     * Date类型转时间字符串字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return string
     * @author hanmeng1
     * @since 2019/2/16 10:02
     */
    public static String transformDateToTimeStr(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime.format(DATETIME_FORMATTER);
    }
}