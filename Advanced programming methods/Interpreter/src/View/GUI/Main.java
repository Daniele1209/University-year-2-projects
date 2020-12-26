package View.GUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            StackPane root = FXMLLoader.load(getClass().getResource("Programs.fxml"));
            Scene scene = new Scene(root, 800, 500);
            Stage primary = new Stage();
            primary.setScene(scene);
            primary.setTitle("Program Selector");
            primary.show();
        } catch(Exception err) {
            err.printStackTrace();
        }
    }

}
