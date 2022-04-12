package pck.dbms.fe;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Test {
    public Pane pane;

    public void showModal(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("ModalTest.fxml"), null, new JavaFXBuilderFactory());
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Stuff");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }
}
