package com.github.ScipioAM.scipio_utils_javafx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Class: AlertDialog
 * Description:
 * Author: Alan Min
 * Create Date: 2020/7/21
 */
public class AlertDialog extends AbstractDialog {

    private AnchorPane dialogPane;

    private VBox contentPane;

    private Label label_title;

    private HBox btnPane;

    private JFXButton btn_positive;

    private JFXButton btn_negative;

    @Override
    public AlertDialog build(final StackPane dialogContainer){
        return build(dialogContainer,null,null);
    }

    /**
     * 创建AlertDialog对象
     * @param dialogContainer dialog的父容器
     * @param title 标题
     * @param content 内容
     */
    public AlertDialog build(final StackPane dialogContainer, String title, String content){
        if(dialogContainer==null){
            throw new IllegalArgumentException("dialogContainer can not be null");
        }
        label_title=new Label(title);
        label_title.getStyleClass().add("dialog-title");
//        AnchorPane.setLeftAnchor(label_title,40.0);
//        AnchorPane.setTopAnchor(label_title,40.0);

        label_content=new Label(content);
        label_content.setWrapText(true);
        label_content.getStyleClass().add("dialog-content");
//        AnchorPane.setLeftAnchor(label_content,40.0);
//        AnchorPane.setRightAnchor(label_content,40.0);
//        AnchorPane.setTopAnchor(label_content,100.0);

        contentPane = new VBox(label_title,label_content);
        contentPane.setSpacing(20.0);
        AnchorPane.setLeftAnchor(contentPane,30.0);
        AnchorPane.setRightAnchor(contentPane,30.0);
        AnchorPane.setTopAnchor(contentPane,30.0);
        AnchorPane.setBottomAnchor(contentPane,30.0);

        btn_positive=new JFXButton("确定");
//        btn_positive.setPrefSize(80.0,45.0);
        btn_positive.setOnAction((actionEvent)->dismiss());
        btn_positive.getStyleClass().add("dialog-jfx-button");
        btn_negative=new JFXButton("取消");
//        btn_negative.setPrefSize(80.0,45.0);
        btn_negative.setOnAction((actionEvent)->dismiss());
        btn_negative.getStyleClass().add("dialog-jfx-button");

        btnPane=new HBox(50.0,btn_positive,btn_negative);
        btnPane.setPrefHeight(100.0);
        btnPane.setAlignment(Pos.CENTER_RIGHT);
        AnchorPane.setRightAnchor(btnPane,20.0);
        AnchorPane.setLeftAnchor(btnPane,20.0);
        AnchorPane.setBottomAnchor(btnPane,0.0);

        dialogPane=new AnchorPane(contentPane,btnPane);
        dialogPane.setPrefSize(400.0,250.0);
//        dialogPane.setPrefWidth(400.0);
        dialogPane.getStylesheets().add("dialog.css");

        dialogObj=new JFXDialog();
        dialogObj.setDialogContainer(dialogContainer);
        dialogObj.setContent(dialogPane);
        return this;
    }

    //=================================================================================================

    public AlertDialog setTitle(String title){
        check(dialogObj,label_title,"title label");
        label_title.setText(title);
        return this;
    }

    public AlertDialog setPositiveBtnText(String text){
        check(dialogObj,btn_positive,"positive button");
        btn_positive.setText(text);
        return this;
    }

    public AlertDialog setNegativeBtnText(String text){
        check(dialogObj,btn_negative,"negative button");
        btn_negative.setText(text);
        return this;
    }

    /**
     * 设置确定按钮的显示和隐藏（隐藏仅仅是原地不可见）
     * @param visible 为true显示
     */
    public AlertDialog setPositiveBtnVisible(boolean visible){
        check(dialogObj,btn_positive,"positive button");
        btn_positive.setVisible(visible);
        return this;
    }

    /**
     * 设置取消按钮的显示和隐藏（隐藏是从画面中去除）
     * @param visible 为true显示
     */
    public AlertDialog setNegativeBtnVisible(boolean visible){
        check(dialogObj,btn_negative,"negative button");
        if(visible){
            btnPane.getChildren().add(btn_negative);
            btnPane.setPadding(new Insets(0,0,0,0));
        }
        else{
            btnPane.getChildren().remove(btn_negative);
            btnPane.setPadding(new Insets(0,20.0,0,0));
        }
        return this;
    }

    /**
     * 设置确定按钮的动作
     * @param listener 如果为null是清除动作
     */
    public AlertDialog setPositiveBtnAction(DialogBtnListener listener){
        check(dialogObj,btn_positive,"positive button");
        if(listener!=null){
            btn_positive.setOnAction((actionEvent)->listener.onButtonAction(actionEvent,dialogObj));
        }
        else{
            btn_positive.setOnAction(null);
        }
        return this;
    }

    /**
     * 设置取消按钮的动作
     * @param listener 如果为null是清除动作
     */
    public AlertDialog setNegativeBtnAction(DialogBtnListener listener){
        check(dialogObj,btn_negative,"negative button");
        if(listener!=null){
            btn_negative.setOnAction((actionEvent)->listener.onButtonAction(actionEvent,dialogObj));
        }
        else{
            btn_negative.setOnAction(null);
        }
        return this;
    }

    public AnchorPane getDialogPane() {
        return dialogPane;
    }

    public VBox getContentPane() {
        return contentPane;
    }
}
