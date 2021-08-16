package com.github.ScipioAM.scipio_utils_common.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 日期工具类（Java8添加的LocalDateTime类）
 * @author Alan Min
 * @since 2020/9/29
 */
public class DateTimeUtil {

    /**
     * 字符串转日期
     * @param pattern 字符串日期格式
     * @param dateStr 待转换的字符串数据
     * @param isOnlyDate 是否只有日期（而没有时分秒），为true代表是
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static LocalDateTime timeString2Obj(String pattern, String dateStr, boolean isOnlyDate)
    {
        if(isOnlyDate) {
            pattern += "[['T'HH][:mm][:ss]]";
        }
        final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
                .toFormatter();
        return LocalDateTime.parse(dateStr,formatter);
    }

    /**
     * 字符串转日期（确定只有日期而没有时分秒）
     * @param dateStr 待转换的字符串数据
     * @param pattern 字符串日期格式
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static LocalDateTime dateString2Obj(String pattern, String dateStr)
    {
        return timeString2Obj(pattern,dateStr,true);
    }

    /**
     * 字符串转日期（确定只有日期而没有时分秒）
     *      默认格式：yyyy/MM/dd
     * @param dateStr 待转换的字符串数据
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static LocalDateTime dateString2Obj(String dateStr)
    {
        return timeString2Obj("yyyy/MM/dd",dateStr,true);
    }

    /**
     * 字符串转日期（默认yyyy/MM/dd HH:mm:ss格式）
     * @param dateStr 待转换的字符串数据
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static LocalDateTime timeString2Obj(String dateStr)
    {
        return timeString2Obj("yyyy/MM/dd HH:mm:ss",dateStr,false);
    }

    /**
     * 日期转字符串
     * @param pattern 字符串日期格式
     * @param date 待转换的日期对象
     * @return 转换好的字符串（如果转换失败则为null）
     */
    public static String date2String(String pattern,LocalDateTime date)
    {
        if(date==null)
        {
            return null;
        }
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date);
    }

    /**
     * 日期转字符串（默认yyyy/MM/dd HH:mm:ss格式）
     * @param date 待转换的日期对象
     * @return 转换好的字符串（如果转换失败则为null）
     */
    public static String date2String(LocalDateTime date)
    {
        return date2String("yyyy/MM/dd HH:mm:ss",date);
    }

    /**
     * 获取当前时间的字符串
     * @param pattern 日期格式
     */
    public static String getNowStr(String pattern) {
        return date2String(pattern,LocalDateTime.now());
    }

    /**
     * 获取当前时间的字符串（默认yyyy/MM/dd HH:mm:ss格式）
     */
    public static String getNowStr() {
        return date2String(LocalDateTime.now());
    }

    /**
     * 时间戳转LocalDateTime对象
     * @param timestamp 时间戳（毫秒级）
     * @param zoneOffset 时区
     * @return LocalDateTime对象
     */
    public static LocalDateTime getObjFromTimestamp(long timestamp, ZoneOffset zoneOffset) {
        return Instant.ofEpochMilli(timestamp).atZone(zoneOffset).toLocalDateTime();
    }

    /**
     * 时间戳转LocalDateTime对象，默认时区为东八区
     * @param timestamp 时间戳（毫秒级）
     * @return LocalDateTime对象
     */
    public static LocalDateTime getObjFromTimestamp(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    /**
     * LocalDateTime对象转时间戳
     * @param obj LocalDateTime对象
     * @param zoneOffset 时区
     * @return 时间戳（毫秒级）
     */
    public static long getTimestampFromObj(LocalDateTime obj, ZoneOffset zoneOffset) {
        return obj.toInstant(zoneOffset).toEpochMilli();
    }

    /**
     * LocalDateTime对象转时间戳，默认时区为东八区
     * @param obj LocalDateTime对象
     * @return 时间戳（毫秒级）
     */
    public static long getTimestampFromObj(LocalDateTime obj) {
        return obj.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

}
