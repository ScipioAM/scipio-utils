package com.github.ScipioAM.scipio_utils_javafx.launch;

import com.jfoenix.controls.JFXProgressBar;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class: SplashScreen
 * Description:
 * Author: Alan Min
 * Create Date: 2020/10/8
 */
public class SplashScreen {

    private String imagePath = "/img/splash.gif";//启动画面的图片

    private boolean visible = true;//启动画面是否显示

    private boolean progressBarVisible = true;//进度条是否显示

    private Stage stage;

    public SplashScreen() { }

    public SplashScreen(boolean visible) {
        this.visible = visible;
    }

    public SplashScreen(String imagePath, boolean visible, boolean progressBarVisible) {
        this.imagePath = imagePath;
        this.visible = visible;
        this.progressBarVisible = progressBarVisible;
    }

    /**
     * 构建启动画面
     */
    public Parent getViews() {
        final VBox vbox = new VBox();
        final ImageView imageView = new ImageView(getClass().getResource(getImagePath()).toExternalForm());
        if(progressBarVisible) {
            final JFXProgressBar splashProgressBar = new JFXProgressBar();
            splashProgressBar.setPrefHeight(7.0);
            splashProgressBar.setPrefWidth(imageView.getImage().getWidth());
            vbox.getChildren().addAll(imageView, splashProgressBar);
        }
        else {
            vbox.getChildren().add(imageView);
        }
        return vbox;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setProgressBarVisible(boolean progressBarVisible) {
        this.progressBarVisible = progressBarVisible;
    }

}
