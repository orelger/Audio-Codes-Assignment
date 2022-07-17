import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static utils.Utils.MAIN_PAGE;

/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */
public class AudioCode extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource(MAIN_PAGE));
        primaryStage.setTitle("Audio Codes");
        primaryStage.setScene(new Scene(root, 525, 350));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
