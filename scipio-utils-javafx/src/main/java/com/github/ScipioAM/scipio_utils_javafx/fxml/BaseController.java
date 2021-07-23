package com.github.ScipioAM.scipio_utils_javafx.fxml;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * Class: BaseController
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/14
 */
public abstract class BaseController implements Initializable {

    protected Stage thisStage;

    /**
     * 停止的回调，可在Application类的onStop方法里调用
     */
    public void onStop() {}

    /**
     * 初始化时的回调，避免initialize方法过早被调用的问题
     */
    public void onInit() {}

    /**
     * 初始化时的回调（带参数版本），避免initialize方法过早被调用的问题
     */
    public void onInit(Object param) {}

    /**
     * 在显示了主画面之后的回调
     */
    public void afterShowStage() {}

    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }
}
