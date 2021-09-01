package com.github.ScipioAM.scipio_utils_common.os;

import com.github.ScipioAM.scipio_utils_common.os.bean.WinExecMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Class: WindowsCmd
 * Description:
 * Author: Alan Min
 * Create Date: 2019/11/23
 */
public class WindowsCmd {

    /**
     * 执行cmd命令
     * @param cmd cmd命令
     * @return Windows操作系统反馈的信息
     */
    public static String execute(String cmd) {
        List<String> normalList = execute_list(cmd);
        if(normalList == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String msgLine : normalList) {
            sb.append(msgLine).append("\n");
        }
        return sb.deleteCharAt(sb.length() - 1)
                .toString();
    }

    /**
     * 执行cmd命令，并返回常规信息
     * @param cmd cmd命令
     * @return 常规信息（但很多命令的无论什么返回信息都是走这个常规的管道）
     */
    public static List<String> execute_list(String cmd) {
        return execute_list(cmd,false);
    }

    /**
     * 执行cmd命令
     * @param cmd cmd命令
     * @param getErrorResult 是否返回错误管道的信息，为false则返回常规管道的信息
     * @return 指定管道的信息
     */
    public static List<String> execute_list(String cmd, boolean getErrorResult) {
        WinExecMessage result = execute_result(cmd);
        if(result == null) {
            return null;
        }
        return getErrorResult ? result.getErrorResultList() : result.getNormalResultList();
    }

    /**
     * 执行cmd命令,将反馈信息的每行装进javaBean并返回
     * @param cmd cmd命令
     * @return Windows操作系统反馈的信息
     */
    public static WinExecMessage execute_result(String cmd) {
        WinExecMessage execResult = null;
        Process process;
        String cmdProgram = "cmd.exe /c ";
        BufferedReader bufferedReader_normal = null;
        BufferedReader bufferedReader_error = null;
        try {
            //执行
            process = Runtime.getRuntime().exec(cmdProgram + cmd);

            //处理响应
            execResult = new WinExecMessage();
            String line;
            //常规输入流
            bufferedReader_normal = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            while ((line = bufferedReader_normal.readLine()) != null) {
                if("".equals(line)) { //去除空行
                    continue;
                }
                execResult.addNormalResultList(line);
            }
            //错误输入流
            bufferedReader_error = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            while ((line = bufferedReader_error.readLine()) != null) {
                if("".equals(line)) { //去除空行
                    continue;
                }
                execResult.addErrorResultList(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//最终关闭IO流
            if (bufferedReader_normal != null) {
                try {
                    bufferedReader_normal.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader_error != null) {
                try {
                    bufferedReader_error.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }//end of finally
        return execResult == null ? null : execResult.check();
    }

}
