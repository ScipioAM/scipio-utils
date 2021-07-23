package com.github.ScipioAM.scipio_utils_common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class: DateTimeUtil
 * Description: 日期工具类（原始的Date类）
 * Author: Alan Min
 * Create Date: 2020/4/14
 */
public class DateUtil {

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
        sdf=null;//明确手动销毁日期转换工具对象
        return date;
    }

    /**
     * 字符串转日期（默认yyyy/MM/dd HH:mm:ss格式）
     * @param dateStr 待转换的字符串数据
     * @return 转换好的日期对象（如果转换失败则为null）
     */
    public static Date string2Date(String dateStr)
    {
        return string2Date("yyyy/MM/dd HH:mm:ss",dateStr);
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
        String dateStr=sdf.format(date);
        sdf=null;//明确手动销毁日期转换工具对象
        return dateStr;
    }

    /**
     * 日期转字符串（默认yyyy/MM/dd HH:mm:ss格式）
     * @param date 待转换的日期对象
     * @return 转换好的字符串（如果转换失败则为null）
     */
    public static String date2String(Date date)
    {
        return date2String("yyyy/MM/dd HH:mm:ss",date);
    }

}
