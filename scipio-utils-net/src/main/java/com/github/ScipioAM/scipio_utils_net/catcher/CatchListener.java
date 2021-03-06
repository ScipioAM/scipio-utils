package com.github.ScipioAM.scipio_utils_net.catcher;

import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;

/**
 * 具体抓取逻辑的接口
 * @author Alan Scipio
 * @since 1.0.0
 * @date  2021/6/9
 */
@FunctionalInterface
public interface CatchListener {

    /**
     * 抓取的回调
     * @param webInfo 目标网页信息
     * @param params 附加参数
     */
    void onCatch(WebInfo webInfo, Object... params);

}
