package com.github.ScipioAM.scipio_utils_javafx.launch;

import com.github.ScipioAM.scipio_utils_javafx.fxml.FxmlView;

/**
 * Class: InitThread
 * Description: 程序后台初始化子线程
 * Author: Alan Min
 * Create Date: 2020/10/4
 */
public abstract class AppInitThread implements Runnable{

    protected final AbstractApp app;
    protected final LaunchListener launchListener;
    protected final SplashScreen splashScreen;

    public AppInitThread(AbstractApp app, LaunchListener launchListener, SplashScreen splashScreen) {
        this.app = app;
        this.launchListener = launchListener;
        this.splashScreen = splashScreen;
    }

    @Override
    public void run() {
        try {
            //初始化
            init(app);

            FxmlView mainView = null;
            if(splashScreen.isVisible()) {
                mainView = launchListener.buildMainView();
            }
            //初始化完成
            launchListener.onFinishInit(mainView,splashScreen);
        }catch (Exception e){
            launchListener.onError(e);
            e.printStackTrace();
        }
    }

    /**
     * 初始化的具体实现
     */
    public abstract void init(AbstractApp app);

}
