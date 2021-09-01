package com.github.ScipioAM.scipio_utils_common.os;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceRunState;
import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceStatus;
import com.github.ScipioAM.scipio_utils_common.os.constants.WinServiceType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Windows Service相关(运行需要管理员权限)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public class WindowsService extends AbstractWindowsExecutor {

    private WindowsService() {}

    public static WindowsService newInstance() {
        return new WindowsService();
    }

    //==================================================================================================================

    /**
     * 查询指定service的状态
     * @param serviceName 指定service的名字
     * @return service当前的状态，执行失败则返回null
     */
    public WinServiceStatus query(String serviceName) {
        if(StringUtil.isNull(serviceName)) {
            throw new IllegalArgumentException("argument[serviceName] is empty");
        }

        String cmd = "sc query " + serviceName;
        //执行
        List<String> resultList = WindowsCmd.execute_list(cmd);
        if(resultList == null || resultList.size() <= 0) { //执行失败，返回null
            return null;
        }
        //处理返回消息
        return analyseResultForStatus(resultList);
    }

    //==================================================================================================================

    /**
     * 解析sc相关命令的结果（预期结果是service的状态信息）
     * @param resultList 执行返回的信息
     * @return 解析后的结果对象，解析失败则返回null
     */
    private WinServiceStatus analyseResultForStatus(List<String> resultList) {
        WinServiceStatus status = new WinServiceStatus();
        try {
            //执行成功
            if(resultList.size() > 4) {
                status.setExecSuccess(true);
                //第1行原文参考：[SERVICE_NAME: redis]
                String serviceNameTemp = resultList.get(0).split(":")[1];
                status.setServiceName(serviceNameTemp.replaceFirst(" ",""));
                //第2行原文参考：[        TYPE               : 10  WIN32_OWN_PROCESS]
                status.setServiceType(WinServiceType.analyze(resultList.get(1)));
                //第3行原文参考：[        STATE              : 4  RUNNING]
                status.setState(WinServiceRunState.analyze(resultList.get(2)));
                //(略)第4行原文参考：[                                (STOPPABLE, NOT_PAUSABLE, ACCEPTS_PRESHUTDOWN)]
                //第5行原文参考：[        WIN32_EXIT_CODE    : 0  (0x0)]
                status.setWin32ExitCode(analyzeCode(resultList.get(4)));
                //第6行原文参考：[        SERVICE_EXIT_CODE  : 0  (0x0)]
                status.setServiceExitCode(analyzeCode(resultList.get(5)));
                //第7行原文参考：[        CHECKPOINT         : 0x0]
                status.setCheckPoint(analyzeCode(resultList.get(6)));
                //第8行原文参考：[        WAIT_HINT          : 0x0]
                status.setWaitHint(analyzeCode(resultList.get(7)));
            }
            else { //执行得到失败结果
                status.setExecSuccess(false);
                StringBuilder errMsg = new StringBuilder();
                for(String line : resultList) {
                    errMsg.append(line).append("\n");
                }
                status.setErrorMsg(errMsg.deleteCharAt(errMsg.length() - 1).toString());
            }
        }catch (Exception e) {
            status = null;
            e.printStackTrace();
        }
        return status;
    }

    private Integer analyzeCode(String line) {
        Pattern pattern = Pattern.compile("(\\s+\\S+\\s+:\\s)(\\w{1,5})(.*)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            try {
                return Integer.decode(matcher.group(2));
            }catch (Exception e) {
                System.err.println("Can not convert string to number: [" + line + "]");
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    //==================================================================================================================

}
