package com.github.ScipioAM.scipio_utils_net.catcher;

import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;

/**
 * IO操作监听器（数据保存）
 * @author alan scipio
 * @since 2021/6/9
 */
public interface IOListener {

    /**
     * IO操作的回调
     * @param webInfo 目标网页信息
     * @param params 附加参数
     */
    void onProcess(WebInfo webInfo, Object... params);

}
