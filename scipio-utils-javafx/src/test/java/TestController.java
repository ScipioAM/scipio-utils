import com.github.ScipioAM.scipio_utils_javafx.AlertDialog;
import com.github.ScipioAM.scipio_utils_javafx.ProgressDialog;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

/**
 * Class: TestController
 * Description:
 * Author: Alan Min
 * Create Date: 2020/7/21
 */
public class TestController {

    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    @FXML
    private StackPane rootPane;

    @FXML
    private void click_showAlert(){
        if(alertDialog==null){
            String title = "title";
            String content = "This is content";
//            String content = "错误信息:\nadb: error: failed to get feature set: device '192.168.3.65:5555' not found";
//            String content = "请检查是否已用usb连接了android设备和电脑,并在Android设置中开启了usb调试\n\n错误信息:\ncannot connect to 192.168.3.60:5555: 由于连接方在一段时间后没有正确答复或连接的主机没有反应，连接尝试失败。 (10060)";
            alertDialog=new AlertDialog();
            alertDialog.build(rootPane,title,content)
                    .setTransition(JFXDialog.DialogTransition.CENTER)
                    .setOverlayClose(false);
//            alertDialog.getDialogPane().setPrefHeight(350.0);
//            alertDialog.getDialogPane().setPrefWidth(450.0);
        }
        alertDialog.show();
    }

    @FXML
    private void click_invisibleNegativeBtn(){
        alertDialog.setNegativeBtnVisible(false);
    }

    @FXML
    private void click_showNegativeBtn(){
        alertDialog.setNegativeBtnVisible(true);
    }

    @FXML
    private void click_clearNegativeBtnAct() {
        alertDialog.setNegativeBtnAction(null);
    }

    @FXML
    private void click_showProgress() {
        if(progressDialog==null){
            progressDialog=new ProgressDialog();
            progressDialog.build(rootPane,"This is a content");
        }

        progressDialog.scheduleForClose(3000);
        progressDialog.show();
    }


}
