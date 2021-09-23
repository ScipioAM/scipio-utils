package com.github.ScipioAM.scipio_utils_net.catcher;

import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;

/**
 * IO操作监听器（数据保存）
 * @author Alan Scipio
 * @since 1.0.0
 * @date 2021/6/9
 */
@FunctionalInterface
public interface IOListener {

    /**
     * IO操作的回调
     * @param webInfo 目标网页信息
     * @param params 附加参数
     */
    void process(WebInfo webInfo, Object... params);

}
