package pck.dbms.fe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.dbms.be.Container;
import pck.dbms.be.product.Product;
import pck.dbms.database.DatabaseCommunication;
import pck.dbms.fe.utils.ProductPane;

public class MainTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {


        DatabaseCommunication.getInstance();
        DatabaseCommunication.getInstance().loadAdministrativeDivision();
        DatabaseCommunication.customer.getListPartner();
        DatabaseCommunication.customer.getListProduct(Container.partners.get("phatnm.partner1"));

        FXMLLoader loader = new FXMLLoader(App.class.getResource("customer.placeOrder.listProduct.fxml"), null, new JavaFXBuilderFactory());
        Parent root = loader.load();

        pck.dbms.fe.customer.placeOrder.listProduct.Controller ctrl = loader.getController();

        ProductPane pp = new ProductPane(Container.products.get("product1"));
        ctrl.gridPaneProd.getChildren().add(pp.get());

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("FXML Welcome");
        stage.setScene(scene);

        stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.show();
    }
}