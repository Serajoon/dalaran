package com.serajoon.dalaran.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

}