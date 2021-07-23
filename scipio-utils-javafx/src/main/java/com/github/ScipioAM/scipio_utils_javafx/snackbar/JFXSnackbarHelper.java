package com.github.ScipioAM.scipio_utils_javafx.snackbar;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.Pane;

/**
 * Class: JFXSnackbarHelper
 * Description:FXSnackbar帮助类
 *      JavaFX开发配合JFoenix这一UI框架
 *      JFoenix的项目地址：https://github.com/jfoenixadmin/JFoenix
 * Author: Alan Min
 * Create Date: 2019/11/23
 */
public class JFXSnackbarHelper {

    public static void showSuccess(Pane parentPane,String text){
        SnackbarView view= SnackbarView.buildSuccess(parentPane,text);
        JFXSnackbar snackbar=new JFXSnackbar(parentPane);
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(view));
    }

    public static void showError(Pane parentPane,String text){
        SnackbarView view= SnackbarView.buildError(parentPane,text);
        JFXSnackbar snackbar=new JFXSnackbar(parentPane);
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(view));
    }

    public static void showWarn(Pane parentPane,String text){
        SnackbarView view= SnackbarView.buildWarn(parentPane,text);
        JFXSnackbar snackbar=new JFXSnackbar(parentPane);
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(view));
    }

    public static void showInfo(Pane parentPane,String text){
        SnackbarView view= SnackbarView.buildInfo(parentPane,text);
        JFXSnackbar snackbar=new JFXSnackbar(parentPane);
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(view));
    }

}
