package com.github.ScipioAM.scipio_utils_common.os;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: WindowsCmd
 * Description:
 * Author: Alan Min
 * Create Date: 2019/11/23
 */
public class WindowsCmd {

    /**
     * 执行一个cmd命令
     *
     * @param cmd cmd命令
     * @return Windows操作系统反馈的信息
     */
    public static String execute(String cmd) {
        List<String> msgList = execute_list(cmd);
        if (msgList != null) {
            StringBuilder sb = new StringBuilder();
            for (String msgLine : msgList) {
                sb.append(msgLine).append("\n");
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * 执行一个cmd命令,将反馈信息的每行装进List数组返回
     *
     * @param cmd cmd命令
     * @return Windows操作系统反馈的信息
     */
    public static List<String> execute_list(String cmd) {
        List<String> msgList = null;
        Process process;
        String cmdProgram = "cmd.exe /c ";
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec(cmdProgram + cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            msgList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                msgList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//最终关闭缓冲资源
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//end of if
        }//end of finally
        return msgList;
    }

}
