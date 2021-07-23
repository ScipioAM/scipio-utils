package com.github.ScipioAM.scipio_utils_javafx.register;

import java.io.*;
import java.util.Scanner;

/**
 * Class: MachineCodeUtil
 * Description: 获取机器码（cpu序列号、主板序列号）的工具类
 * Author: Alan Min
 * Create Date: 2020/3/28
 */
public class MachineCodeUtil {

    /**
     * 获取CPU序列号
     * @return CPU序列号,为null则说明获取失败
     */
    public static String getCpuSerial(){
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
            String serial = sc.next();
            return serial;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取主板序列号
     * @return 主板序列号,为null则说明获取失败
     */
    public static String getMotherboardSN() {
        try {
            File file = File.createTempFile("getMBSN", ".vbs");//创建临时文件
            file.deleteOnExit();//关闭文件时删除
            FileWriter fw = new FileWriter(file);
            //读取主板序列号的vbs代码
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_BaseBoard\") \n"
                    + "For Each objItem in colItems \n"
                    + "   Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";
            //写入文件
            fw.write(vbs);
            fw.close();
            //执行vbs脚本
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + file.getPath());
            //开始读取脚本执行内容
            BufferedReader input = new BufferedReader( new InputStreamReader(p.getInputStream()) );
            String line;
            StringBuilder result=new StringBuilder();
            while ((line = input.readLine()) != null) {
                result.append(line);
            }
            input.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
