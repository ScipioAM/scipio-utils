package old_dialogs;

import com.github.ScipioAM.scipio_utils_javafx.DialogBtnListener;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class: DialogController
 * Description:
 * Author: Alan Min
 * Create Date: 2020/2/23
 */
public class AlertDialogController extends AbstractDialogController implements Initializable {

    @FXML
    private Label label_title;
    @FXML
    protected JFXButton btn_positive;//确定按钮
    @FXML
    protected JFXButton btn_negative;//取消按钮

    /**
     * 显示对话框
     * @param title 标题
     * @param content 内容
     * @param location 对话框在父容器中的位置
     */
    public void show(String title, String content , JFXDialog.DialogTransition location)
    {
        if(title!=null && !title.equals(""))
        {
            label_title.setText(title);
        }
        label_content.setText(content);
        setNegativeBtn2Close();//默认取消按钮是关闭对话框
        dialog.setTransitionType(location);
        dialog.show();
    }

    /**
     * 显示对话框（在父容器中间位置）
     * @param title 标题
     * @param content 内容
     */
    public void show(String title,String content)
    {
        show(title,content, JFXDialog.DialogTransition.CENTER);
    }

    /**
     * 显示对话框（在父容器中间位置，没有标题）
     * @param content 内容
     */
    public void show(String content)
    {
        show(null,content, JFXDialog.DialogTransition.CENTER);
    }

    /**
     * 对话框与fxml绑定初始化时
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        label_title.setText("");
    }

    /**
     * 设置对话框标题
     */
    public void setTitle(String title)
    {
        label_title.setText(title);
    }

    /**
     * 设置对确定按钮是否可见
     */
    public void setPositiveBtnVisible(boolean visible)
    {
        btn_positive.setVisible(visible);
        btn_positive.setDisable(visible);
    }

    /**
     * 设置取消按钮是否可见
     */
    public void setNegativeBtnVisible(boolean visible)
    {
        btn_negative.setVisible(visible);
        btn_negative.setDisable(visible);
    }

    /**
     * 设置确定按钮点击事件
     */
    public void setPositiveBtnListener(DialogBtnListener dialogListener)
    {
        btn_positive.setOnAction( (actionEvent)->dialogListener.onButtonAction(actionEvent,dialog) );
    }

    /**
     * 设置取消按钮点击事件
     */
    public void setNegativeBtnListener(DialogBtnListener dialogListener)
    {
        btn_negative.setOnAction( (actionEvent)->dialogListener.onButtonAction(actionEvent,dialog) );
    }

    /**
     * 设置确定按钮的默认点击事件 - 点击后关闭对话框
     */
    public void setPositiveBtn2Close()
    {
        btn_positive.setOnAction( (actionEvent)->dialog.close() );
    }

    /**
     * 设置取消按钮的默认点击事件 - 点击后关闭对话框
     */
    public void setNegativeBtn2Close()
    {
        btn_negative.setOnAction( (actionEvent)->dialog.close() );
    }

}
