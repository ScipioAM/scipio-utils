package com.github.ScipioAM.scipio_utils_javafx;

import com.jfoenix.controls.JFXDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Class: AbstractDialog
 * Description:
 * Author: Alan Min
 * Create Date: 2020/7/21
 */
public abstract class AbstractDialog {

    protected JFXDialog dialogObj;

    protected Label label_content;

    public void show(){
        check(dialogObj);
        dialogObj.show();
    }

    public void dismiss(){
        check(dialogObj);
        dialogObj.close();
    }

    public AbstractDialog setTransition(JFXDialog.DialogTransition transitionType){
        check(dialogObj);
        dialogObj.setTransitionType(transitionType);
        return this;
    }

    public AbstractDialog setContent(String content){
        check(dialogObj,label_content,"content label");
        label_content.setText(content);
        return this;
    }

    public AbstractDialog setContentVisible(boolean visible){
        check(dialogObj,label_content,"content label");
        label_content.setVisible(visible);
        return this;
    }

    /**
     * 设置在dialog之外的区域点击后，是否能关闭dialog，默认为true
     * @param overlayClose 为true代表可以在dialog之外的区域点击后，关闭dialog
     */
    public AbstractDialog setOverlayClose(boolean overlayClose){
        check(dialogObj);
        dialogObj.setOverlayClose(overlayClose);
        return this;
    }

    public JFXDialog getDialogObj() {
        return dialogObj;
    }

    public Label getContentLabel() {
        return label_content;
    }

    //=================================================================================================

    public abstract AbstractDialog build(final StackPane dialogContainer);

    //=================================================================================================

    /**
     * 检查参数(检查不通过则抛出异常)
     * @param dialog JFXDialog对象
     * @param ui ui控件对象
     * @param uiName ui控件在异常信息中的名称
     */
    protected void check(JFXDialog dialog, Object ui, String uiName){
        if(dialog==null){
            throw new RuntimeException("dialog object is null, should build first");
        }
        if(ui==null){
            throw new IllegalArgumentException("["+uiName+"] can not be null, should build first");
        }
    }

    protected void check(JFXDialog dialog){
        if(dialog==null){
            throw new RuntimeException("dialog object is null, should build it first");
        }
    }

}
