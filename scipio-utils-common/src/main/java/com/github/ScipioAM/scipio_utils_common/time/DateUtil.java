package com.github.ScipioAM.scipio_utils_common.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具类
 * @author Alan Scipio
 * @since 1.0.0
 * @date 2020/4/14
 */
public class DateUtil {

    /** 默认日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 字符串转日期
     * @param pattern 字符串日期格式
     * @param dateStr 待转换的字符串数据
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static Date string2Date(String pattern,String dateStr)
    {
        Date date=null;
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        try {
            date=sdf.parse(dateStr);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转日期（默认日期格式）
     * @param dateStr 待转换的字符串数据
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static Date string2Date(String dateStr)
    {
        return string2Date(DEFAULT_DATE_FORMAT,dateStr);
    }

    /**
     * 日期转字符串
     * @param pattern 字符串日期格式
     * @param date 待转换的日期对象
     * @return 转换好的字符串（如果转换失败则为null）
     */
    public static String date2String(String pattern,Date date)
    {
        if(date==null)
        {
            return null;
        }
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 日期转字符串（默认日期格式）
     * @param date 待转换的日期对象
     * @return 转换好的字符串（如果转换失败则为null）
     */
    public static String date2String(Date date)
    {
        return date2String(DEFAULT_DATE_FORMAT,date);
    }


    /**
     * 字符串转{@link LocalDate}
     * @param str 字符串
     * @param formatPattern 日期格式
     * @return {@link LocalDate}对象
     */
    public static LocalDate str2Date(String str, String formatPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        return LocalDate.parse(str,formatter);
    }

    /**
     * 字符串转{@link LocalDate}，采用默认日期格式
     * @param str 字符串
     * @return {@link LocalDate}对象
     */
    public static LocalDate str2Date(String str) {
        return str2Date(str, DEFAULT_DATE_FORMAT);
    }

    /**
     * {@link LocalDate}转字符串
     * @param date {@link LocalDate}对象
     * @param formatPattern 日期格式
     * @return 转换后的字符串
     */
    public static String date2Str(LocalDate date, String formatPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        return date.format(formatter);
    }

    /**
     * {@link LocalDate}转字符串，采用默认日期格式
     * @param date 字符串
     * @return 转换后的字符串
     */
    public static String date2Str(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return date.format(formatter);
    }

    /**
     * 智能检测并构成日期格式，目前支持以下格式：
     *      <ul>
     *          <li> yyyy-MM-dd </li>
     *          <li> yyyy/MM/dd </li>
     *          <li> yyyy.MM.dd </li>
     *      </ul>
     * @param str 待检测字符串
     * @throws IllegalArgumentException 检测的字符串不在支持范围内
     * @return 日期格式字符串
     */
    public static String smartPatternGet(String str) throws IllegalArgumentException {
        String pattern0 = getDatePattern(str,"-");
        if(pattern0 != null) {
            return pattern0;
        }
        else {
            String pattern1 = getDatePattern(str,"/");
            if(pattern1 != null) {
                return pattern1;
            }
            else {
                String pattern2 = getDatePattern(str,"\\.");
                if(pattern2 != null) {
                    return pattern2;
                }
            }
        }
        throw new IllegalArgumentException("Illegal date string: \"" + str + "\"");
    }

    private static String getDatePattern(String str, String split) {
        Pattern pattern = Pattern.compile("(\\d{4})" + split + "(\\d{1,2})" + split + "(\\d{1,2})");
        Matcher matcher = pattern.matcher(str);
        if(matcher.matches()) {
            String yearPattern = "yyyy", monthPattern, dayPattern;
            //月份格式
            String month = matcher.group(2);
            monthPattern = (month.length() == 1 ? "M" : "MM");
            //日格式
            String day = matcher.group(3);
            dayPattern = (day.length() == 1 ? "d" : "dd");
            //合成最终的格式
            return yearPattern + split + monthPattern + split + dayPattern;
        }
        else {
            return null;
        }
    }

}
