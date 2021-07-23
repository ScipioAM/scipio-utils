import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class: TestRun
 * Description:
 * Author: Alan Min
 * Create Date: 2020/7/21
 */
public class TestRun extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent= FXMLLoader.load(getClass().getResource("/test.fxml"));
        Scene scene=new Scene(parent);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Test dialog");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(TestRun.class,args);
    }

}
