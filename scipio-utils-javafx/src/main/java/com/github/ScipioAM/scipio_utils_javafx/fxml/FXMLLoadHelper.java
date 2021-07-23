package com.github.ScipioAM.scipio_utils_javafx.fxml;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * Class: FXMLLoadHelper
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/14
 */
public class FXMLLoadHelper {

    private final FXMLLoader fxmlLoader;

    public FXMLLoadHelper() {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
    }

    public FxmlView load(String fxmlPath) throws IOException {
        URL location = getClass().getResource(fxmlPath);
        fxmlLoader.setLocation(location);
        Parent rootNode = fxmlLoader.load();
        BaseController controller = fxmlLoader.getController();
        return new FxmlView(rootNode,controller);
    }

}
