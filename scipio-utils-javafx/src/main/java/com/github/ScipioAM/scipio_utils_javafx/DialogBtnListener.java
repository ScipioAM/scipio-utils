package com.github.ScipioAM.scipio_utils_javafx;

import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;

/**
 * Class: JFXDialogListener
 * Description:
 * Author: Alan Min
 * Create Date: 2020/2/23
 */
public interface DialogBtnListener {

    void onButtonAction(ActionEvent actionEvent, JFXDialog dialog);//按钮点击事件

}
