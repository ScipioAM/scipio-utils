package old_dialogs;

import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Class: AbstractDialogController
 * Description:
 * Author: Alan Min
 * Create Date: 2020/3/2
 */
public abstract class AbstractDialogController {

    protected JFXDialog dialog;

    @FXML
    protected Label label_content;//内容label

    /**
     * 设置对话框内容
     */
    public void setContent(String content)
    {
        label_content.setText(content);
    }

    /**
     * 关闭dialog显示
     */
    public void dismiss()
    {
        dialog.close();
    }

    public JFXDialog getDialog() {
        return dialog;
    }

    public void setDialog(JFXDialog dialog) {
        this.dialog = dialog;
    }
}
