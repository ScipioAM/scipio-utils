package com.github.ScipioAM.scipio_utils_common.os;

import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceRunState;
import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceResult;
import com.github.ScipioAM.scipio_utils_common.os.constants.SCType;
import com.github.ScipioAM.scipio_utils_common.os.constants.WinServiceType;

import java.util.List;

/**
 * 执行了sc命令后，执行成功时的处理
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/6
 */
@FunctionalInterface
public interface WinServiceSuccessHandler {

    /**
     * 执行了sc命令后，执行成功时的处理
     * @param scType sc命令类型
     * @param resultList 输出的结果list
     * @param result 先期构建的result对象（确定了execSuccess，originalMsg）,通常把它补完后返回即可
     * @return 最终的解析结果
     */
    WinServiceResult handle(SCType scType, List<String> resultList, WinServiceResult result);

    //==================================================================================================================

    /**
     * [sc query]命令的处理
     */
    WinServiceSuccessHandler QUERY = (scType, resultList, result) -> {
        //第1行原文参考：[SERVICE_NAME: redis]
        String serviceNameTemp = resultList.get(0).split(":")[1];
        result.setServiceName(serviceNameTemp.replaceFirst(" ",""));
        //第2行原文参考：[        TYPE               : 10  WIN32_OWN_PROCESS]
        result.setServiceType(WinServiceType.analyze(resultList.get(1)));
        //第3行原文参考：[        STATE              : 4  RUNNING]
        result.setState(WinServiceRunState.analyze(resultList.get(2)));
        //(略)第4行原文参考：[                                (STOPPABLE, NOT_PAUSABLE, ACCEPTS_PRESHUTDOWN)]
        //第5行原文参考：[        WIN32_EXIT_CODE    : 0  (0x0)]
        result.setWin32ExitCode(WindowsService.analyzeCode(resultList.get(4)));
        //第6行原文参考：[        SERVICE_EXIT_CODE  : 0  (0x0)]
        result.setServiceExitCode(WindowsService.analyzeCode(resultList.get(5)));
        //第7行原文参考：[        CHECKPOINT         : 0x0]
        result.setCheckPoint(WindowsService.analyzeCode(resultList.get(6)));
        //第8行原文参考：[        WAIT_HINT          : 0x0]
        result.setWaitHint(WindowsService.analyzeCode(resultList.get(7)));
        return result;
    };

    /**
     * [sc start]命令的处理
     */
    WinServiceSuccessHandler START = (scType, resultList, result) -> {
        //第1行原文参考：[SERVICE_NAME: redis]
        String serviceNameTemp = resultList.get(0).split(":")[1];
        result.setServiceName(serviceNameTemp.replaceFirst(" ",""));
        //第2行原文参考：[        TYPE               : 10  WIN32_OWN_PROCESS]
        result.setServiceType(WinServiceType.analyze(resultList.get(1)));
        //第3行原文参考：[        STATE              : 4  RUNNING]
        result.setState(WinServiceRunState.analyze(resultList.get(2)));
        //(略)第4行原文参考：[                                (STOPPABLE, NOT_PAUSABLE, ACCEPTS_PRESHUTDOWN)]
        //第5行原文参考：[        WIN32_EXIT_CODE    : 0  (0x0)]
        result.setWin32ExitCode(WindowsService.analyzeCode(resultList.get(4)));
        //第6行原文参考：[        SERVICE_EXIT_CODE  : 0  (0x0)]
        result.setServiceExitCode(WindowsService.analyzeCode(resultList.get(5)));
        //第7行原文参考：[        CHECKPOINT         : 0x0]
        result.setCheckPoint(WindowsService.analyzeCode(resultList.get(6)));
        //第8行原文参考：[        WAIT_HINT          : 0x0]
        result.setWaitHint(WindowsService.analyzeCode(resultList.get(7)));
        //第9行原文参考：[        PID                : 19916]
        result.setPid(WindowsService.analyzeCode(resultList.get(8)));
        //(略)第10行原文参考：[        FLAGS              : ]
        return result;
    };

    /**
     * [sc stop]命令的处理
     */
    WinServiceSuccessHandler STOP = (scType, resultList, result) -> {
        //第1行原文参考：[SERVICE_NAME: redis]
        String serviceNameTemp = resultList.get(0).split(":")[1];
        result.setServiceName(serviceNameTemp.replaceFirst(" ",""));
        //第2行原文参考：[        TYPE               : 10  WIN32_OWN_PROCESS]
        result.setServiceType(WinServiceType.analyze(resultList.get(1)));
        //第3行原文参考：[        STATE              : 4  RUNNING]
        result.setState(WinServiceRunState.analyze(resultList.get(2)));
        //第4行原文参考：[        WIN32_EXIT_CODE    : 0  (0x0)]
        result.setWin32ExitCode(WindowsService.analyzeCode(resultList.get(3)));
        //第5行原文参考：[        SERVICE_EXIT_CODE  : 0  (0x0)]
        result.setServiceExitCode(WindowsService.analyzeCode(resultList.get(4)));
        //第6行原文参考：[        CHECKPOINT         : 0x0]
        result.setCheckPoint(WindowsService.analyzeCode(resultList.get(5)));
        //第7行原文参考：[        WAIT_HINT          : 0x0]
        result.setWaitHint(WindowsService.analyzeCode(resultList.get(6)));
        return result;
    };

    /**
     * [sc showsid]命令的处理
     */
    WinServiceSuccessHandler SHOW_SID = (scType, resultList, result) -> {
        //第1行原文参考：[名称: redis]
        String serviceNameTemp = resultList.get(0).split(":")[1];
        result.setServiceName(serviceNameTemp.replaceFirst(" ",""));
        //第2行原文参考：[服务 SID: S-1-5-80-3499539596-2560698945-118472934-1916862957-3829011903]
        String sidTemp = resultList.get(1).split(":")[1];
        result.setSid(sidTemp.replaceFirst(" ",""));
        //第3行原文参考：[状态: 非活动]
        result.setRemarks(resultList.get(2));
        return result;
    };

    /**
     * 空处理(仅打印一下)
     */
    WinServiceSuccessHandler EMPTY_PRINT = (scType, resultList, result) -> {
        System.out.println("Empty success handle, scType[" + scType + "]");
        return result;
    };

}
