package com.github.ScipioAM.scipio_utils_javafx;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class: ProgressDialog
 * Description:
 * Author: Alan Min
 * Create Date: 2020/7/21
 */
public class ProgressDialog extends AbstractDialog{

    private JFXSpinner spinner;

    @Override
    public ProgressDialog build(final StackPane dialogContainer){
        return build(dialogContainer,null);
    }

    public ProgressDialog build(final StackPane dialogContainer, String content){
        spinner=new JFXSpinner();
        spinner.setPrefSize(80.0,80.0);
        spinner.setRadius(100.0);
        spinner.setStartingAngle(-40.0);

        label_content=new Label(content);
        label_content.setStyle("-fx-font-size: 20");

        VBox vBox=new VBox(30.0,spinner,label_content);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefSize(400,250);

        dialogObj=new JFXDialog();
        dialogObj.setContent(vBox);
        dialogObj.setDialogContainer(dialogContainer);
        dialogObj.setOverlayClose(false);//默认不能在对话框外点击关闭
        return this;
    }

    //=================================================================================================

    /**
     * 延迟关闭进度条对话框
     * @param delay 延迟时长，单位毫秒
     */
    public void scheduleForClose(long delay){
        check(dialogObj);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dismiss();
                System.gc();//否则需要一直等到jvm自行gc才会彻底结束此子线程，此间隔较长
            }
        }, delay);
    }

    //=================================================================================================

    public ProgressDialog setProgress(double value){
        check(dialogObj,spinner,"spinner");
        spinner.setProgress(value);
        return this;
    }

    public ProgressDialog setRadius(double value){
        check(dialogObj,spinner,"spinner");
        spinner.setRadius(value);
        return this;
    }

    public ProgressDialog setStartingAngle(double value){
        check(dialogObj,spinner,"spinner");
        spinner.setStartingAngle(value);
        return this;
    }

    public JFXSpinner getSpinner() {
        return spinner;
    }
    
}
