package pck.dbms.fe;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.application.Application;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;

public class FormIndex extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FormIndex.class.getResource("Form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Bán hàng online");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.setFullScreen(false);

        stage.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
        stage.setTitle("Bán hàng Online");

        stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        // get controller to get components
        FormCtrl ctrl = fxmlLoader.getController();

        // get grid pane
        GridPane gridPane = ctrl.getFormIdxGridPane();

        // create panel 1
        Panel panel1 = new Panel("Panel 1"); // create panel
        panel1.setMinSize(200, 200);
        panel1.getStyleClass().add("panel-info");// set boostrap class

        // create content for panel1's header
        Label lb1 = new Label("Label panel 1");
        panel1.setHeading(lb1);

        // create content for panel1's body
        Label lbbd1 = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean finibus nulla sed elementum fermentum. Phasellus a euismod purus. Aliquam vel est arcu. Donec porttitor sapien quis dui euismod ultrices. Nam at hendrerit odio, nec viverra dolor. Donec convallis ");
        lbbd1.setPrefSize(400, 200);
        lbbd1.setWrapText(true);
        lbbd1.setTextAlignment(TextAlignment.JUSTIFY);
        panel1.setBody(lbbd1);

        Panel panel2 = new Panel("Panel 2");
        panel2.setMinSize(200, 200);
        panel2.getStyleClass().add("panel-info");
        Label lb2 = new Label("Label panel 2");
        panel2.setHeading(lb2);
        Label lbbd2 = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean finibus nulla sed elementum fermentum. Phasellus a euismod purus. Aliquam vel est arcu. Donec porttitor sapien quis dui euismod ultrices. Nam at hendrerit odio, nec viverra dolor. Donec convallis accumsan placerat. Nam lobortis augue eu nunc efficitur vestibulum. Mauris eget dolor ante. Pellentesque sed ante ligula. Praesent mollis lectus sem, fringilla accumsan metus ");
        lbbd2.setPrefSize(400, 200);
        lbbd2.setWrapText(true);
        lbbd2.setTextAlignment(TextAlignment.JUSTIFY);
        panel2.setBody(lbbd2);

        Panel panel3 = new Panel("Panel 3");
        panel3.setMinSize(200, 200);
        panel3.getStyleClass().add("panel-info");
        Label lb3 = new Label("Label panel 3");
        panel3.setHeading(lb3);
        Label lbbd3 = new Label("Lorem igilla accumsan metus dignissim eget. Nam eu rhoncus arcu, sit amet viverra augue. Sed mauris lorem, malesuada vitae est posuere, eleifend facilisis arcu.");
        lbbd3.setPrefSize(400, 200);
        lbbd3.setWrapText(true);
        lbbd3.setTextAlignment(TextAlignment.JUSTIFY);
        panel3.setBody(lbbd3);

        Panel panel4 = new Panel("Panel 4");
        panel4.setMinSize(200, 200);
        panel4.getStyleClass().add("panel-info");
        Label lb4 = new Label("Label panel 4");
        panel4.setHeading(lb4);
        Label lbbd4 = new Label("Lorem igilla accumsan metus dignissim eget. Nam eu rhoncus arcu, sit amet viverra augue. Sed mauris lorem, malesuada vitae est posuere, eleifend facilisis arcu.");
        lbbd4.setPrefSize(400, 200);
        lbbd4.setWrapText(true);
        lbbd4.setTextAlignment(TextAlignment.JUSTIFY);
        panel4.setBody(lbbd4);

        Panel panel5 = new Panel("Panel 5");
        panel5.setMinSize(200, 200);
        panel5.getStyleClass().add("panel-info");
        Label lb5 = new Label("Label panel 5");
        panel5.setHeading(lb5);
        Label lbbd5 = new Label("Lorem igilla accumsan metus dignissim eget. Nam eu rhoncus arcu, sit amet viverra augue. Sed mauris lorem, malesuada vitae est posuere, eleifend facilisis arcu.");
        lbbd5.setPrefSize(400, 200);
        lbbd5.setWrapText(true);
        lbbd5.setTextAlignment(TextAlignment.JUSTIFY);
        panel5.setBody(lbbd5);

        Panel panel6 = new Panel("Panel 6");
        panel6.setMinSize(200, 200);
        panel6.getStyleClass().add("panel-info");
        Label lb6 = new Label("Label panel 6");
        panel6.setHeading(lb6);
        Label lbbd6 = new Label("Lorem igilla accumsan metus dignissim eget. Nam eu rhoncus arcu, sit amet viverra augue. Sed mauris lorem, malesuada vitae est posuere, eleifend facilisis arcu.");
        lbbd6.setPrefSize(400, 400);
        lbbd6.setWrapText(true);
        lbbd6.setTextAlignment(TextAlignment.JUSTIFY);
        panel6.setBody(lbbd6);

        // Chú ý GridPane < G viết hoa > address unique
        // define contraint for col and row, [0,...] <=> setConstraints(Component/Node, colIdx, rowIdx);
        GridPane.setConstraints(panel1, 0, 0);
        GridPane.setConstraints(panel2, 1, 1);
        GridPane.setConstraints(panel3, 2, 2);
        GridPane.setConstraints(panel4, 1, 3);
        GridPane.setConstraints(panel5, 2, 4);
        GridPane.setConstraints(panel6, 0, 5);

        gridPane.getChildren().add(panel1); // 1 col 1 row
        gridPane.getChildren().add(panel2); // 2 col 1 row
        gridPane.getChildren().add(panel3); // 2 col 1 row
        gridPane.getChildren().add(panel4); // 1 col 1 row
        gridPane.getChildren().add(panel5); // 2 col 1 row
        gridPane.getChildren().add(panel6); // 2 col 1 row

//        Form form = Form.of(
//                        Group.of(
//                                Field.ofStringType("")
//                                        .placeholder("Hãy nhập mật khẩu")
//                                        .label("Username")
//                                        .required("This field can't be empty"),
//                                Field.ofPasswordType("")
//                                        .label("Password")
//                                        .required("This field can’t be empty")
//                        )
//                )
//                .title("Login");
//        FormRenderer formRenderer = new FormRenderer(form);
//        formRenderer.setMinSize(400, 400);
//
//
//        ctrl.getFormContainer().getChildren().add(formRenderer);

        stage.show();
    }
}
