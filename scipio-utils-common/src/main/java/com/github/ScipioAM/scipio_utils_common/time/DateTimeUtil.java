package com.github.ScipioAM.scipio_utils_common.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 时间工具类
 * @author Alan Min
 * @since 2020/9/29
 */
public class DateTimeUtil {

    /** 默认时间日期格式 */
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转日期
     * @param pattern 字符串日期格式
     * @param dateStr 待转换的字符串数据
     * @param isOnlyDate 是否只有日期（而没有时分秒），为true代表是
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static LocalDateTime str2Time(String dateStr, String pattern, boolean isOnlyDate) {
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
     * 字符串转日期（默认时间格式）
     * @param dateStr 待转换的字符串数据
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static LocalDateTime str2Time(String dateStr) {
        return str2Time(DEFAULT_TIME_FORMAT,dateStr,false);
    }

    /**
     * 日期转字符串
     * @param pattern 字符串日期格式
     * @param date 待转换的日期对象
     * @return 转换好的字符串（如果转换失败则为null）
     */
    public static String time2Str(LocalDateTime date, String pattern)
    {
        if(date == null) {
            return null;
        }
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date);
    }

    /**
     * 日期转字符串（默认时间格式）
     * @param date 待转换的日期对象
     * @return 转换好的字符串（如果转换失败则为null）
     */
    public static String time2Str(LocalDateTime date)
    {
        return time2Str(date, DEFAULT_TIME_FORMAT);
    }

    /**
     * 获取当前时间的字符串
     * @param pattern 日期格式
     */
    public static String getNowStr(String pattern) {
        return time2Str(LocalDateTime.now(), pattern);
    }

    /**
     * 获取当前时间的字符串（默认时间格式）
     */
    public static String getNowStr() {
        return time2Str(LocalDateTime.now());
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
