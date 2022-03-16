package com.github.ScipioAM.scipio_utils_common.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 时间段
 *
 * @author WuFeee
 * @since 2022/3/16
 */
public class TimeBucket {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    private final Long startTime;

    private final Long endTime;

    public TimeBucket(long start, long end) {
        this.startTime = start;
        this.endTime = end;
    }

    public TimeBucket(String format, String startStr, String endStr) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(format);
        Date start = formatter.parse(startStr);
        Date end = formatter.parse(endStr);
        this.startTime = start.getTime();
        this.endTime = end.getTime();
    }

    public TimeBucket(String start, String end) throws ParseException {
        this(DEFAULT_FORMAT, start, end);
    }

    public TimeBucket(Date start, Date end) {
        this.startTime = start.getTime();
        this.endTime = end.getTime();
    }

    public TimeBucket(LocalDateTime start, LocalDateTime end) {
        //毫秒级
        this.startTime = start.toInstant(ZoneOffset.of(ZoneId.systemDefault().getId())).toEpochMilli();
        this.endTime = end.toInstant(ZoneOffset.of(ZoneId.systemDefault().getId())).toEpochMilli();
    }

    public TimeBucket(LocalDate start, LocalDate end) {
        //毫秒级
        this.startTime = start.atStartOfDay().toInstant(ZoneOffset.of(ZoneId.systemDefault().getId())).toEpochMilli();
        this.endTime = end.atStartOfDay().toInstant(ZoneOffset.of(ZoneId.systemDefault().getId())).toEpochMilli();
    }

    /**
     * 检测是否有时间段重叠
     *
     * @param buckets n个时间段
     * @return 返回null说明没有重叠的时间段,否则返回第一个发现重叠的时间段
     */
    public static TimeBucket checkOverlap(TimeBucket... buckets) {
        //长度为1无需判断
        if (buckets == null || buckets.length <= 1) {
            return null;
        }
        for (int i = 0; i < buckets.length - 1; i++) {
            long start = buckets[i].getStartTime();
            long end = buckets[i].getEndTime();
            for (int j = i + 1; j < buckets.length; j++) {
                if (buckets[j].getStartTime() > start) {
                    start = buckets[j].getStartTime();
                }
                if (buckets[j].getEndTime() < end) {
                    end = buckets[j].getEndTime();
                }
                //有重叠的，返回此时间段（第一个发现的）
                if (start < end) {
                    return new TimeBucket(start, end);
                }
            }
        }
        //没有重叠的，返回null
        return null;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "TimeBucket{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
