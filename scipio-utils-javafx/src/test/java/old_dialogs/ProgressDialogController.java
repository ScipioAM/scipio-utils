package old_dialogs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class: ProgressDialogController
 * Description:
 * Author: Alan Min
 * Create Date: 2020/2/27
 */
public class ProgressDialogController extends AbstractDialogController implements Initializable {

    @FXML
    private JFXSpinner spinner;

    /**
     * 进度框与fxml绑定初始化时
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * 显示进度框
     * @param content 内容
     * @param location 进度框在父容器中的位置
     */
    public void show(String content , JFXDialog.DialogTransition location)
    {
        if(dialog.isOverlayClose())
        {
            dialog.setOverlayClose(false);//不允许在对话框外的区域点击后关闭该对话框
        }
        label_content.setText(content);
        dialog.setTransitionType(location);
        dialog.show();
    }

    /**
     * 显示进度框（在父容器中间位置）
     * @param content 内容
     */
    public void show(String content)
    {
        show(content, JFXDialog.DialogTransition.CENTER);
    }

    public JFXSpinner getSpinner() {
        return spinner;
    }
}
