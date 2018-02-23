package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static final String GET_ALL = "SELECT * FROM notes";
    public static final String FXML_EDIT = "/fxml/note.fxml";
    public static final String FXML_MAIN = "/fxml/main.fxml";
    @Override
    public void start(Stage stage) throws Exception{
        String fxmlFile = FXML_MAIN;
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Note editor");
        stage.setScene(new Scene(root));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
