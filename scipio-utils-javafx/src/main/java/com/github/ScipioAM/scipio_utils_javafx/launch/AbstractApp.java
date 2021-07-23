package com.github.ScipioAM.scipio_utils_javafx.launch;

import com.github.ScipioAM.scipio_utils_javafx.fxml.FXMLLoadHelper;
import com.github.ScipioAM.scipio_utils_javafx.fxml.FxmlView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class: AbstractApp
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/21
 */
public abstract class AbstractApp extends Application implements LaunchListener {

    protected final ExecutorService threadPool = Executors.newCachedThreadPool();//线程池

    protected FxmlView mainView;//主画面
    protected Stage primaryStage;//主窗口
    protected static SplashScreen splashScreen;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //设置图标
        String iconPath = getIconPath();
        if(iconPath!=null && !"".equals(iconPath)) {
            primaryStage.getIcons().addAll(new Image(getClass().getResource(iconPath).toExternalForm()));
        }
        //设置标题
        String title = getTitle();
        if(title!=null && !"".equals(title)) {
            primaryStage.setTitle(title);
        }
        initPrimaryStage(primaryStage);
        //显示启动画面
        if(splashScreen.isVisible()) {
            Stage splashStage = new Stage(StageStyle.TRANSPARENT);
            if(iconPath!=null && !"".equals(iconPath)) {
                splashStage.getIcons().addAll(new Image(getClass().getResource(iconPath).toExternalForm()));
            }
            Scene splashScene = new Scene(splashScreen.getViews(),Color.TRANSPARENT);
            splashStage.setScene(splashScene);
            splashStage.setResizable(false);
            splashScreen.setStage(splashStage);
            splashStage.show();
        }
        //不显示启动画面，直接显示主画面
        else {
            try {
                this.mainView = buildMainView();
                showMainView(splashScreen);
            }catch (Exception e) {
                onError(e);
                e.printStackTrace();
            }
        }

        AppInitThread initThread = getInitThread(splashScreen);
        if(initThread != null) {
            //启动初始化子线程
            threadPool.submit(initThread);
        }
    }//end start()

    /**
     * 显示主画面
     * @param splashScreen 启动画面
     */
    private void showMainView(final SplashScreen splashScreen) {
        primaryStage.setScene(new Scene(mainView.getView()));
        if (splashScreen.isVisible()) {
            Stage splashStage = splashScreen.getStage();
            splashStage.hide();
            splashStage.setScene(null);
        }
        beforeShowMainView(mainView);
        primaryStage.show();
        afterShowMainView(primaryStage,mainView);
    }

    /**
     * 子线程初始化完成时
     * @param mainView 主画面
     * @param splashScreen 启动画面
     */
    @Override
    public void onFinishInit(final FxmlView mainView, final SplashScreen splashScreen) {
        if(splashScreen.isVisible()) {
            this.mainView = mainView;
            Platform.runLater(()->{
                showMainView(splashScreen);
                onLaunched(primaryStage);
            });
        }
        else {
            onLaunched(primaryStage);
        }
    }//end onFinishInit()

    /**
     * 创建主画面对象
     */
    @Override
    public FxmlView buildMainView() throws IOException {
        FXMLLoadHelper fxmlLoadHelper = new FXMLLoadHelper();
        return fxmlLoadHelper.load(getMainViewPath());
    }

    /**
     * 启动出错时
     * @param throwable 抛出的异常
     */
    @Override
    public void onError(final Throwable throwable) {
        Platform.runLater(()->showErrorAlert(throwable));
    }

    /**
     * 程序结束时
     */
    @Override
    public void stop() {
        threadPool.shutdownNow();
    }

    //========================================================================================

    /**
     * 启动app
     * @param appClass 实现类
     * @param args 程序启动参数
     * @param splashScreen 启动画面
     */
    public static void launchApp(final Class<? extends Application> appClass, String[] args, SplashScreen splashScreen) {
//        System.out.println("====== Application start ======");
        AbstractApp.splashScreen = splashScreen;
        launch(appClass,args);
//        System.out.println("====== Application exit ======");
    }

    /**
     * 启动app（默认加载启动画面）
     */
    public static void launchApp(final Class<? extends Application> appClass, String[] args) {
        launchApp(appClass,args,new SplashScreen());
    }

    /**
     * 启动app（默认加载启动画面）
     * @param visible 启动画面是否显示，为true代表显示
     */
    public static void launchApp(final Class<? extends Application> appClass, String[] args, boolean visible) {
        launchApp(appClass,args,new SplashScreen(visible));
    }

    //========================================================================================

    /**
     * 获取初始化子线程的实例
     */
    public abstract AppInitThread getInitThread(SplashScreen splashScreen);

    /**
     * 获取主画面的路径
     */
    public abstract String getMainViewPath();

    /**
     * 获取程序图标的路径（需要重写，否则默认默认没有图标）
     */
    public String getIconPath() {
        return null;
    }

    /**
     * 获取程序标题（需要重写，否则默认默认没有标题）
     */
    public String getTitle() {
        return null;
    }

    /**
     * 在显示mainView之前的回调
     */
    public void beforeShowMainView(FxmlView mainView) {}

    /**
     * 在显示了mainView之后的回调
     * @param primaryStage 主画面
     * @param mainView 主画面对象
     */
    public void afterShowMainView(Stage primaryStage, FxmlView mainView) {
        mainView.getController().afterShowStage();
    }

    /**
     * 程序启动完成后（初始化子线程也完成）
     * @param primaryStage 主画面
     */
    public void onLaunched(Stage primaryStage) {}

    /**
     * 最开始时，初始化主画面的一些基本属性
     * @param primaryStage 主画面
     */
    public void initPrimaryStage(Stage primaryStage) {}

    //========================================================================================

    /**
     * 启动失败时显示的错误信息框
     * @param throwable 抛出的异常对象
     */
    private static void showErrorAlert(Throwable throwable) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "An unrecoverable error occurred.\n" +
                "The application will stop now.\n\n" +
                "Error: " + throwable.getMessage());
        alert.showAndWait().ifPresent(response -> Platform.exit());
    }

}
