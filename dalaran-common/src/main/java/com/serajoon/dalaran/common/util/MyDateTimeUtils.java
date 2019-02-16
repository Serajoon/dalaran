package com.serajoon.dalaran.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public abstract class MyDateTimeUtils {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public static int getCurrentYear(){
        return LocalDateTime.now().getYear();
    }

    public static int getCurrentMonth(){
        return LocalDateTime.now().getMonthValue();
    }

    public static int getCurrentDay(){
        return LocalDateTime.now().getDayOfMonth();
    }

    /**
     * Date类型转时间字符串字符串yyyy-MM-dd
     * @param   date
     * @return  string
     * @author  hanmeng1
     * @since  2019/2/16 10:02
     */
    public static String dateToDateString(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate.format(DATE_FORMATTER);
    }

    /**
     * Date类型转时间字符串字符串 yyyy-MM-dd HH:mm:ss
     * @param   date
     * @return  string
     * @author  hanmeng1
     * @since  2019/2/16 10:02
     */
    public static String dateToTimeString(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime.format(DATETIME_FORMATTER);
    }
}