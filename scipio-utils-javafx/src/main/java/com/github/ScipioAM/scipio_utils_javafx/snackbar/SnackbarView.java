package com.github.ScipioAM.scipio_utils_javafx.snackbar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class: SnackbarController
 * Description:JFXSnackbar的view
 *      JavaFX开发配合JFoenix这一UI框架
 *      JFoenix的项目地址：https://github.com/jfoenixadmin/JFoenix
 * Author: Alan Min
 * Create Date: 2019/11/23
 */
//尚可进一步完善，现在只有一个label
public class SnackbarView extends HBox {

    private final double HEIGHT_DEFAULT;//默认高度,是父容器的8分之1
    private static final String TEXT_COLOR_DEFAULT="#ffffff";//文本颜色默认为白色

    private final Pane parentPane;//父容器
    private final Label label_text;//文本

    //=================================================================================

    private SnackbarView(Pane parentPane){
        this.parentPane=parentPane;
        this.label_text=new Label();
        this.getChildren().add(this.label_text);

        HEIGHT_DEFAULT=this.parentPane.getHeight()*0.125;
//        setBarAlignment(Pos.CENTER_LEFT);//默认文本靠左居中
        setBarAlignment(Pos.CENTER);//默认文本居中
    }

    private SnackbarView(Pane parentPane, String text, String bgRgb){
        this(parentPane);
        this.label_text.setText(text);//设置文本
        this.label_text.setTextFill(Color.web(TEXT_COLOR_DEFAULT));//设置文本颜色

        this.prefWidthProperty().bind(this.parentPane.widthProperty());//设置宽度为父控件宽度
        this.prefHeightProperty().setValue(HEIGHT_DEFAULT);//设置高度为默认值
        this.setBackground(new Background(new BackgroundFill(Color.web(bgRgb),null,null)));//设置背景颜色
    }

    //=================================================================================

    public static SnackbarView buildSuccess(Pane parentPane, String text){
        return new SnackbarView(parentPane,text,"#8CD790");
    }

    public static SnackbarView buildError(Pane parentPane, String text){
        return new SnackbarView(parentPane,text,"#E53A40");
    }

    public static SnackbarView buildWarn(Pane parentPane, String text){
        return new SnackbarView(parentPane,text,"#FFBC42");
    }

    public static SnackbarView buildInfo(Pane parentPane, String text){
        return new SnackbarView(parentPane,text,"#30A9D1");
    }

    //=================================================================================

    /**
     * 设置snackbar的Alignment
     */
    public void setBarAlignment(Pos value){
        this.setAlignment(value);
        //如果是靠左居中，则设置靠左缩进
        if(this.getAlignment()==Pos.CENTER_LEFT){
            double paddingLeft=HEIGHT_DEFAULT*0.5;//缩进值为snackbar高度的一半
            this.setPadding(new Insets(0.0,0.0,0.0,paddingLeft));
        }
    }

    /**
     * 设置snackbar的背景颜色
     * @param rgb 16进制rgb代码，例如#353866
     */
    public void setBackgroundColor(String rgb){
        this.setBackground(new Background(new BackgroundFill(Color.web(rgb),null,null)));
    }

    /**
     * 设置snackbar的文本大小
     * @param textSize 文本大小
     */
    public void setBarTextSize(double textSize){
        this.label_text.setFont(Font.font(textSize));
    }

}
