package com.github.ScipioAM.scipio_utils_common.os;

import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceResult;
import com.github.ScipioAM.scipio_utils_common.os.constants.SCType;

import java.util.List;

/**
 * 执行了sc命令后的处理(覆盖原有处理)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/6
 */
@FunctionalInterface
public interface WinServiceResultHandler {

    /**
     * 执行了sc命令后的处理
     * @param scType sc命令的类型
     * @param cmd 执行的命令
     * @param resultList 输出的结果list
     * @return 分析过的执行结果
     */
    WinServiceResult handle(SCType scType, String cmd, List<String> resultList);

}
