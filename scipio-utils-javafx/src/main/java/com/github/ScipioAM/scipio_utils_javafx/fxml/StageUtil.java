package com.github.ScipioAM.scipio_utils_javafx.fxml;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * javaFX Stage相关工具方法
 * @author Alan Min
 * @since 2020/12/30
 */
public class StageUtil {

    public static Stage showStage(Window owner, FxmlView view) {
        return showStage(StageStyle.DECORATED,Modality.NONE,owner,view,null);
    }

    public static Stage showStage(Window owner, FxmlView view, String title) {
        return showStage(StageStyle.DECORATED,Modality.NONE,owner,view,title);
    }

    public static Stage showStage(Modality modality, Window owner, FxmlView view, String title) {
        return showStage(StageStyle.DECORATED,modality,owner,view,title);
    }

    public static Stage showStage(StageStyle stageStyle, Window owner, FxmlView view, String title) {
        return showStage(stageStyle,Modality.NONE,owner,view,title);
    }

    /**
     * 显示新界面（new Stage）
     * @param stageStyle 界面样式
     * @param modality 模组样式（是否阻塞父界面等）
     * @param owner 父界面
     * @param view 新界面本身
     * @param title 新界面的标题
     * @return 新界面对象
     */
    public static Stage showStage(StageStyle stageStyle, Modality modality, Window owner, FxmlView view, String title) {
        if(view==null)
            return null;
        Stage stage = new Stage(stageStyle);
        stage.initModality(modality);
        stage.initOwner(owner);
        stage.setScene(new Scene(view.getView()));
        stage.setTitle(title);
        stage.show();
        return stage;
    }

}
