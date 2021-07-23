package com.github.ScipioAM.scipio_utils_javafx.launch;

import com.github.ScipioAM.scipio_utils_javafx.fxml.FxmlView;

import java.io.IOException;

/**
 * Interface: LaunchListener
 * Description: 程序启动的监听器
 * Author: Alan Min
 * Create Date: 2020/10/8
 */
public interface LaunchListener {

    /**
     * 子线程初始化完成时
     * @param mainView 主画面
     * @param splashScreen 启动画面
     */
    void onFinishInit(final FxmlView mainView, final SplashScreen splashScreen);

    /**
     * 创建主画面对象
     */
    FxmlView buildMainView() throws IOException;

    /**
     * 启动出错时
     * @param throwable 抛出的异常
     */
    void onError(final Throwable throwable);

}
