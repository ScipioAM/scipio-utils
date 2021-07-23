package com.github.ScipioAM.scipio_utils_javafx;

import javafx.scene.layout.StackPane;

/**
 * Class: DialogHelper
 * Description:
 * Author: Alan Min
 * Create Date: 2020/7/21
 */
public class DialogHelper {

    /**
     * （一次性）显示对话框
     * @param dialogContainer 对话框父容器
     * @param title 标题
     * @param content 内容
     */
    public static void showAlert(final StackPane dialogContainer, String title, String content){
        AlertDialog dialog=new AlertDialog();
        dialog.build(dialogContainer,title,content)
                .setNegativeBtnVisible(false)
                .show();
    }

    /**
     * （一次性）显示对话框，设置确定按钮的动作
     * @param dialogContainer 对话框父容器
     * @param title 标题
     * @param content 内容
     * @param positiveBtnListener 确定按钮的动作
     */
    public static void showAlert(final StackPane dialogContainer, String title, String content, DialogBtnListener positiveBtnListener){
        AlertDialog dialog=new AlertDialog();
        dialog.build(dialogContainer,title,content)
                .setPositiveBtnAction(positiveBtnListener)
                .show();
    }

    /**
     * 准备进度条对话框
     * @param dialogContainer 对话框父容器
     * @param content 内容
     * @return 进度条对话框对象
     */
    public static ProgressDialog prepareProgress(final StackPane dialogContainer, String content){
        ProgressDialog dialog=new ProgressDialog();
        dialog.build(dialogContainer,content);
        return dialog;
    }

    /**
     * 准备进度条对话框
     * @param dialogContainer 对话框父容器
     * @param content 内容
     * @param startingAngle 开始的角度，默认-40度
     * @return 进度条对话框对象
     */
    public static ProgressDialog prepareProgress(final StackPane dialogContainer, String content, double startingAngle){
        ProgressDialog dialog=new ProgressDialog();
        dialog.build(dialogContainer,content)
                .setStartingAngle(startingAngle);
        return dialog;
    }

}
