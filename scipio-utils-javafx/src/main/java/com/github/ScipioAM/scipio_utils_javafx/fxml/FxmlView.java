package com.github.ScipioAM.scipio_utils_javafx.fxml;

import com.github.ScipioAM.scipio_utils_javafx.AlertHelper;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Class: FxmlView
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/14
 */
public class FxmlView {

    private Parent view;

    private BaseController controller;

    private Stage stage;

    public FxmlView() {}

    public FxmlView(Parent view, BaseController controller) {
        this.view = view;
        this.controller = controller;
    }

    //=================================================================

    /**
     * 显示新界面（模态界面）
     * @param view 界面对象 (为null则代表第一次显示，会在方法中自动创建并返回)
     * @param fxmlPath fxml文件路径
     * @param title 新界面的窗体标题
     * @param parentWindow 从属的父窗体
     * @param stageStyle 窗体样式
     * @param modality 模态样式
     * @param param 新界面启动时的参数对象（用于调用controller对象里的onInit方法）
     * @return 界面对象
     */
    public static FxmlView showView(FxmlView view, String fxmlPath, String title, Window parentWindow, StageStyle stageStyle, Modality modality, Object param) {
        Stage stage;
        if(view == null) {
            FXMLLoadHelper loadHelper = new FXMLLoadHelper();
            try {
                view = loadHelper.load(fxmlPath);
            }catch (Exception e) {
                AlertHelper.showError("程序错误","打开界面失败","请联系管理员");
                e.printStackTrace();
                return null;
            }

            BaseController controller = view.getController();
            stage = StageUtil.showStage(stageStyle, modality, parentWindow,view,title);
            view.setStage(stage);
            controller.setThisStage(stage);
            //初始化
            controller.onInit(param);
        }
        else {
            stage = view.getStage();
        }
        stage.show();
        return view;
    }

    public static FxmlView showView(FxmlView view, String fxmlPath, String title, Window window, Object param) {
        return showView(view, fxmlPath, title, window,StageStyle.UTILITY, Modality.APPLICATION_MODAL,param);
    }

    public static FxmlView showView(FxmlView view, String fxmlPath, String title, Window window) {
        return showView(view, fxmlPath, title, window,StageStyle.UTILITY, Modality.APPLICATION_MODAL,null);
    }

    /**
     * 关闭界面
     */
    public static void closeView(FxmlView view) {
        if(view!=null) {
            Stage stage = view.getStage();
            if(stage!=null) {
                stage.close();
            }
        }
    }

    //=================================================================

    public Parent getView() {
        return view;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    public BaseController getController() {
        return controller;
    }

    public void setController(BaseController controller) {
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
