package pck.dbms.fe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.dbms.be.Container;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.product.Product;
import pck.dbms.be.user.Login;
import pck.dbms.be.user.ROLE;
import pck.dbms.database.DatabaseCommunication;
import pck.dbms.fe.utils.LineNumbersCellFactory;
import pck.dbms.fe.utils.ProductAndBranchPane;

import java.io.IOException;

/**
 * Main Application. This class handles navigation and user session.
 */
public class App extends Application {
    public static Login login;
    private static App instance;
    private Stage stage;
    private FXMLLoader loader;

    public App() {
        login = new Login();
        instance = this;
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }

        return instance;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    stage = primaryStage;
                    //stage.setResizable(false);
                    stage.setFullScreen(false);

                    stage.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                    stage.setTitle("Bán hàng Online");

                    //replaceSceneContent("template.fxml");

                    gotoWelCome();

                    stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                    primaryStage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public ROLE getUserRole() {
        return login.getRole();
    }

    public void setUserRole(ROLE role) {
        login.setRole(role);
    }

    private boolean isSignInSuccess(String signInRes) {
        return (!signInRes.equals("ERROR") && !signInRes.equals("LOGIN_FAILED"));
    }

    public String handleSQLSignIn(String username, String password) {
        String signInRes = DatabaseCommunication.getInstance().login(username, password);

        if (isSignInSuccess(signInRes)) {
            // update new user role
            getInstance().login.setUsername(username);
            getInstance().login.setRole(ROLE.valueOf(signInRes));

            System.out.println(login);
        }

        return signInRes;
    }

    public void gotoWelCome() {
        try {
            replaceSceneContent("index.fxml");
//            userRole = "PARTNER";
//            gotoHome();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoSignIn() {
        try {
            replaceSceneContent("entry.sign_in.fxml");

            pck.dbms.fe.entry.sign_in.Controller controller = loader.getController();

            System.out.println("[CONTROLLER] " + controller);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoHome() {
        System.out.println("[USERROLE] " + getUserRole());
        switch (getUserRole()) {
            case ADMIN:
                // todo gotoAdminHome();
                return;
            case PARTNER:
                gotoPartnerHome();
                return;
            case EMPLOYEE:
                // todo gotoEmployeeHome();
                return;
            case DRIVER:
                // todo gotoDriverHome();
                return;
            case CUSTOMER:
                customer.gotoHome();
                return;
            default:
                gotoSignIn();
        }
    }

    public void gotoPartnerContract() {
        try {
            replaceSceneContent("partner.contract.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Parent replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml), null, new JavaFXBuilderFactory());
        Parent page = loader.load();

        getInstance().loader = loader;

        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, 800, 600);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.sizeToScene();
        return page;
    }

    /*========================== APP ==========================*/


    /*========================== PARTNER ==========================*/
    public void gotoPartnerHome() {
        try {
            replaceSceneContent("partner.home.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*========================== ADMIN ==========================*/
    //public void gotoAdminHome

    /*========================== CUSTOMER ==========================*/
    public static class customer {
        public static void gotoHome() {
            try {
                getInstance().replaceSceneContent("customer.home.fxml");

                pck.dbms.fe.customer.home.Controller controller = getInstance().loader.getController();

                controller.getImgVLogout().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Btn logout pressed in " + getInstance().getClass());
                        event.consume();
                    }
                });


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static void gotoListPartner() {
            try {
                getInstance().replaceSceneContent("customer.placeOrder.listPartner.fxml");

                pck.dbms.fe.customer.placeOrder.listPartner.Controller controller = getInstance().loader.getController();

                controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Btn logout pressed in " + getInstance().getClass());
                        event.consume();
                        System.out.println("icon shake");
                        new animatefx.animation.Shake(controller.imgVBack).play();
                        gotoHome();
                    }
                });

                controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Btn logout pressed in " + getInstance().getClass());
                        event.consume();

                        getInstance().gotoSignIn();
                    }
                });

                // get list Partner
                DatabaseCommunication.customer.getListPartner();

                TableView tableView = controller.tableViewPartner;
                tableView.setRowFactory(param -> {
                    return new TableRow() {
                        @Override
                        public void updateIndex(int i) {
                            super.updateIndex(i);
                            setTextAlignment(TextAlignment.JUSTIFY);
                            setMinHeight(70);
                        }
                    };
                });

                controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
                controller.colName.setCellValueFactory(new PropertyValueFactory<Partner, String>("name"));
                controller.colType.setCellValueFactory(new PropertyValueFactory<Partner, String>("productType"));
                controller.colRepName.setCellValueFactory(new PropertyValueFactory<Partner, String>("representativeName"));
                controller.colAddr.setCellValueFactory(new PropertyValueFactory<Partner, String>("addressAsString"));
                controller.colPhone.setCellValueFactory(new PropertyValueFactory<Partner, String>("phone"));
                controller.colMail.setCellValueFactory(new PropertyValueFactory<Partner, String>("mail"));
                Callback<TableColumn<Partner, String>, TableCell<Partner, String>> cellFactory
                        = //
                        new Callback<TableColumn<Partner, String>, TableCell<Partner, String>>() {
                            @Override
                            public TableCell call(final TableColumn<Partner, String> param) {
                                final TableCell<Partner, String> cell = new TableCell<Partner, String>() {

                                    final Button btn = new Button("Chọn");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Partner partner = getTableView().getItems().get(getIndex());
                                                System.out.println("select partner " + partner.getName());
                                                gotoListProduct(partner);
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                                return cell;
                            }
                        };

                controller.colBtn.setCellFactory(cellFactory);

                for (String key : Container.partners.keySet()) {
                    tableView.getItems().add(Container.partners.get(key));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static void gotoListProduct(Partner partner) {
            try {
                getInstance().replaceSceneContent("customer.placeOrder.listProduct.fxml");

                pck.dbms.fe.customer.placeOrder.listProduct.Controller controller = getInstance().loader.getController();

                controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Btn back pressed in " + getInstance().getClass());
                        event.consume();
                        new animatefx.animation.Shake(controller.imgVBack).play();
                        gotoListPartner();
                    }
                });

                controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Btn logout pressed in " + getInstance().getClass());
                        event.consume();

                        getInstance().gotoSignIn();
                    }
                });

                controller.imgVCart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Btn cart pressed in " + getInstance().getClass());
                        event.consume();

                        try {
                            Stage stageCartDetails = new Stage();
                            FXMLLoader loader = new FXMLLoader(App.class.getResource("ModalTest.fxml"), null, new JavaFXBuilderFactory());
                            Parent root = loader.load();
                            stageCartDetails.initOwner(getInstance().stage);
                            stageCartDetails.setScene(new Scene(root));
                            stageCartDetails.setTitle("Đơn hàng của bạn");
                            stageCartDetails.initModality(Modality.APPLICATION_MODAL);
                            stageCartDetails.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

                // load product
                DatabaseCommunication.customer.getListProduct(partner);
                GridPane gp = controller.gridPaneProd;

                int row = 0, col = 0;
                for (String key : Container.products.keySet()) {
                    Product p = Container.products.get(key);

                    if (p.getImgSrc().contains("http")) {
                        ProductAndBranchPane pp = new ProductAndBranchPane(p);

                        Pane pTemp = pp.get();

                        GridPane.setConstraints(pTemp, col, row);
                        System.out.println(row + " " + col);
                        gp.getChildren().add(pTemp);
                        col += 1;
                        if (col == 4) {
                            col = 0;
                            row += 1;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
