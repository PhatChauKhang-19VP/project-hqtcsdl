package pck.dbms.fe;

import javafx.application.Application;
import javafx.application.Platform;
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
import pck.dbms.be.cart.Cart;
import pck.dbms.be.cart.CartDetail;
import pck.dbms.be.order.Order;
import pck.dbms.be.partner.Contract;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.product.Product;
import pck.dbms.be.user.LoginInfo;
import pck.dbms.be.user.ROLE;
import pck.dbms.database.DatabaseCommunication;
import pck.dbms.fe.employee.ListContractController;
import pck.dbms.fe.partner.features.RegisterContractController;
import pck.dbms.fe.utils.CartDetailPane;
import pck.dbms.fe.utils.LineNumbersCellFactory;
import pck.dbms.fe.utils.ProductAndBranchPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Random;

/**
 * Main Application. This class handles navigation and user session.
 */
public class App extends Application {
    public static LoginInfo loginInfo;
    private static App instance;
    private Stage stage;
    private FXMLLoader loader;

    public App() {
        loginInfo = new LoginInfo();
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

                    //gotoWelCome();
                    Demo.gotoErrSelection();

                    stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

                    primaryStage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public ROLE getUserRole() {
        return loginInfo.getRole();
    }

    public void setUserRole(ROLE role) {
        loginInfo.setRole(role);
    }

    private boolean isSignInSuccess(String signInRes) {
        return (!signInRes.equals("ERROR") && !signInRes.equals("LOGIN_FAILED"));
    }

    public String handleSQLSignIn(String username, String password) {
        String signInRes = DatabaseCommunication.getInstance().login(username, password);

        if (isSignInSuccess(signInRes)) {
            // update new user role
            App.loginInfo.setUsername(username);
            App.loginInfo.setRole(ROLE.valueOf(signInRes));

            switch (App.loginInfo.getRole()) {
                case APP -> {
                    System.out.println("Case app, do nothing");
                }
                case ADMIN -> {
                    System.out.println("Cass Admin");
                    //todo assign admin
                }
                case CUSTOMER -> {
                    System.out.println("Set customer value to Container");
                    Container.customer.setLogin(App.loginInfo);
                }
                case PARTNER -> {
                    System.out.println("Set partner value to Container");
                    Container.partner.setLogin(App.loginInfo);
                }
                case DRIVER -> {
                    System.out.println("Dri");
                }
                case EMPLOYEE -> {
                    System.out.println("Em");
                }
            }

            System.out.println(loginInfo + " has logged in system");
        }

        return signInRes;
    }

    public void showNotification(Stage owner, String msg, String color) {
        // show notification
        try {
            Stage stageNotification = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("notification.fxml"), null, new JavaFXBuilderFactory());
            Parent root = loader.load();
            stageNotification.initOwner(owner);
            stageNotification.setScene(new Scene(root));
            stageNotification.setTitle("Thông báo");
            stageNotification.initModality(Modality.APPLICATION_MODAL);
            stageNotification.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
            stageNotification.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stageNotification.setResizable(false);
            stageNotification.setFullScreen(false);
            stageNotification.sizeToScene();

            pck.dbms.fe.notification.Controller ctrlNoti = loader.getController();
            ctrlNoti.msg.setText(msg);
            ctrlNoti.msg.getStyleClass().add(color);

            ctrlNoti.btnOK.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> stageNotification.close());

            stageNotification.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            // some database communications and UI component customizations are put down here

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
                partner.gotoHome();
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

    public Parent replaceSceneContent(String fxml, int w, int h) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml), null, new JavaFXBuilderFactory());

        Parent page = loader.load();

        getInstance().loader = loader;

        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, w, h);
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
    public static class partner {

        public static void gotoHome() {
            try {
                getInstance().replaceSceneContent("partner.home.fxml");

                pck.dbms.fe.partner.home.Controller controller = getInstance().loader.getController();

                controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    getInstance().gotoSignIn();
                });

                controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    getInstance().gotoSignIn();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void gotoContractsManagement() {
            try {
                getInstance().replaceSceneContent("partner.contractsManagement.fxml");

                pck.dbms.fe.partner.features.ContractsManagementController ctrl = getInstance().loader.getController();
                ctrl.partner = Container.partner;


                ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    partner.gotoHome();
                });

                ctrl.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    getInstance().gotoSignIn();
                });

                // get contract list from DB


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void gotoOrdersManagement() {
            try {
                getInstance().replaceSceneContent("partner.ordersManagement.fxml");

                pck.dbms.fe.partner.features.OrdersManagementController ctrl = getInstance().loader.getController();

                ctrl.partner = Container.partner;

                ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    partner.gotoHome();
                });

                ctrl.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    getInstance().gotoSignIn();
                });

                // get order list from DB
                DatabaseCommunication.partner.getOrdersDirtyRead1Error(ctrl.partner, ctrl.orderHashMap);

                //setting for value factory
                ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                ctrl.colID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
                ctrl.colCusName.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
                ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Order, String>("customerAddress"));
                ctrl.colPrice.setCellValueFactory(new PropertyValueFactory<Order, String>("total"));
                ctrl.colDiSta.setCellValueFactory(new PropertyValueFactory<Order, String>("deliveryStatus"));
                ctrl.colPaidSta.setCellValueFactory(new PropertyValueFactory<Order, String>("paidStatus"));
                Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactory = new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<Order, String> param) {
                        final TableCell<Order, String> cell = new TableCell<Order, String>() {

                            final Button btn = new Button("Chọn");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Order order = getTableView().getItems().get(getIndex());
                                        System.out.println("btn clicked");
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

                ctrl.colBtn.setCellFactory(cellFactory);

                // add order to table view
                for (String key : ctrl.orderHashMap.keySet()) {
                    ctrl.tableView.getItems().add(ctrl.orderHashMap.get(key));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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
                        getInstance().gotoSignIn();
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
                boolean listPartner = DatabaseCommunication.customer.getListPartner(controller.partners);

                TableView<Partner> tableView = controller.tableViewPartner;
                tableView.setRowFactory(param -> {
                    return new TableRow<>() {
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
                Callback<TableColumn<Partner, String>, TableCell<Partner, String>> cellFactory = new Callback<>() {
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

                for (String key : controller.partners.keySet()) {
                    tableView.getItems().add(controller.partners.get(key));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static void gotoListProduct(Partner partner) {
            try {

                getInstance().replaceSceneContent("customer.placeOrder.listProduct.fxml");

                pck.dbms.fe.customer.placeOrder.listProduct.Controller controller = getInstance().loader.getController();

                controller.partner = partner;

                controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn back pressed in " + getInstance().getClass());
                    event.consume();
                    new animatefx.animation.Shake(controller.imgVBack).play();
                    gotoListPartner();
                });

                controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    getInstance().gotoSignIn();
                });

                controller.imgVCart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Btn cart pressed in " + getInstance().getClass());
                        event.consume();

                        try {
                            Stage stageCartDetails = new Stage();
                            FXMLLoader loader = new FXMLLoader(App.class.getResource("customer.placeOrder.cartDetails.fxml"), null, new JavaFXBuilderFactory());
                            Parent root = loader.load();
                            stageCartDetails.initOwner(getInstance().stage);
                            stageCartDetails.setScene(new Scene(root));
                            stageCartDetails.setTitle("Đơn hàng của bạn");
                            stageCartDetails.initModality(Modality.NONE);

                            stageCartDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                            stageCartDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                            stageCartDetails.setResizable(false);
                            stageCartDetails.setFullScreen(false);
                            stageCartDetails.sizeToScene();

                            //* get cart detail from server
                            Cart cart = new Cart();
                            cart.customer = Container.customer;
                            cart.partner = partner;

                            DatabaseCommunication.customer.getCartDetails(cart);

                            pck.dbms.fe.customer.placeOrder.listProduct.cartDetail.Controller modalCtrl = loader.getController();

                            modalCtrl.cart = cart;
                            modalCtrl.partner = controller.partner;
                            modalCtrl.partnerBranches = controller.partnerBranches;
                            modalCtrl.products = controller.products;

                            int col = 0, row = 0;
                            modalCtrl.gridPaneCartDetails.getChildren().clear();
                            for (CartDetail cd : cart.cartDetails) {
                                CartDetailPane cdp = new CartDetailPane(cd);

                                Pane pTemp = cdp.get();

                                GridPane.setConstraints(pTemp, col, row);
                                row++;

                                modalCtrl.gridPaneCartDetails.getChildren().add(pTemp);
                            }

                            modalCtrl.comboBoxPaymentMethod.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                            modalCtrl.comboBoxPaymentMethod.setOnAction((eCBPM) -> {
                                Object selectedItem = modalCtrl.comboBoxPaymentMethod.getSelectionModel().getSelectedItem();
                                modalCtrl.paymentMethod = String.valueOf(selectedItem);
                                if (modalCtrl.paymentMethod.equals("Tiền mặt")) modalCtrl.paymentMethod = "CASH";
                                modalCtrl.btnPlaceOrder.setDisable(false);
                            });

                            modalCtrl.labelTotal.setText(String.format("%f VNĐ", modalCtrl.cart.total));

                            modalCtrl.btnPlaceOrder.setDisable(true);
                            modalCtrl.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    System.out.println(getClass() + " btnPlaceOrder on click");
                                    if (DatabaseCommunication.customer.createOrderButFailError(new Order(cart, modalCtrl.paymentMethod), cart)) {
                                        System.out.println("Đặt hàng thành công");
                                        stageCartDetails.close();

                                        // show notification
                                        try {
                                            Stage stageNotification = new Stage();
                                            FXMLLoader loader = new FXMLLoader(App.class.getResource("notification.fxml"), null, new JavaFXBuilderFactory());
                                            Parent root = loader.load();
                                            stageNotification.initOwner(getInstance().stage);
                                            stageNotification.setScene(new Scene(root));
                                            stageNotification.setTitle("Thông báo");
                                            stageNotification.initModality(Modality.APPLICATION_MODAL);
                                            stageNotification.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                            stageNotification.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                            stageNotification.setResizable(false);
                                            stageNotification.setFullScreen(false);
                                            stageNotification.sizeToScene();

                                            pck.dbms.fe.notification.Controller ctrlNoti = loader.getController();
                                            ctrlNoti.msg.setText("Đặt hàng thành công");
                                            ctrlNoti.msg.getStyleClass().add("bg-success");

                                            ctrlNoti.btnOK.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> stageNotification.close());

                                            stageNotification.show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                }
                            });

                            stageCartDetails.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

                // load product
                DatabaseCommunication.customer.getListProduct(controller.partner, controller.partnerBranches, controller.products);
                GridPane gp = controller.gridPaneProd;

                int row = 0, col = 0;
                for (String key : controller.products.keySet()) {
                    Product p = controller.products.get(key);

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
                    } else {
                        p.setImgSrc("https://res.cloudinary.com/phatchaukhang/image/upload/v1649956615/HQTCSDL/product-images/product-default-list-350_nejngg.jpg");
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

    public static class Demo {
        private static final LinkedHashMap<ERROR_TYPE, String> errorTypes = new LinkedHashMap<>() {{
            put(ERROR_TYPE.DIRTY_READ, "Dirty read");
            put(ERROR_TYPE.LOST_UPDATE, "Lost update");
            put(ERROR_TYPE.NON_REPEATABLE_READ, "Non-repeatable read");
            put(ERROR_TYPE.PHANTOM, "Phantom");
            put(ERROR_TYPE.DEADLOCK, "Deadlock");
        }};

        public static void gotoErrSelection() {
            try {
                getInstance().replaceSceneContent("demo.errSelection.fxml");

                pck.dbms.fe.demo.ErrSelectionController controller = getInstance().loader.getController();

                controller.imgVBack.setVisible(false);
                controller.imgVLogout.setVisible(false);

                controller.btnSelect.setDisable(true);

                controller.cbBox.setStyle("-fx-font-size: 24");
                controller.cbBox.getItems().addAll(Demo.errorTypes.values());

                controller.cbBox.setOnAction((event) -> {
                    Object selectedItem = controller.cbBox.getSelectionModel().getSelectedItem();

                    controller.currErrTypeSelected = selectedItem.toString();
                    controller.isErrTypeSelected = true;

                    if (controller.isErrNoSelected) {
                        controller.btnSelect.setDisable(false);
                    }
                });

                controller.cbBoxNO.setStyle("-fx-font-size: 24");
                controller.cbBoxNO.getItems().addAll("1", "2");

                controller.cbBoxNO.setOnAction((event) -> {
                    Object selectedItem = controller.cbBoxNO.getSelectionModel().getSelectedItem();

                    controller.currErrNoSelected = Integer.parseInt(selectedItem.toString());
                    controller.isErrNoSelected = true;

                    if (controller.isErrTypeSelected) {
                        controller.btnSelect.setDisable(false);
                    }
                });
                getInstance().stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void gotoTypeSelection(String errType, int errNo) {
            try {
                getInstance().replaceSceneContent("demo.typeSelection.fxml");

                pck.dbms.fe.demo.TypeSelectionController ctrl = getInstance().loader.getController();

                ctrl.errType = errType;
                ctrl.errNO = errNo;

                ctrl.liablePrim.setText(errType + " " + errNo);

                ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    Demo.gotoErrSelection();
                });
                ctrl.imgVLogout.setVisible(false);

                ctrl.btnDemoErr.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn demo err pressed in " + getInstance().getClass());
                    event.consume();

                    gotoTranSelection("ERROR", ctrl.errType, ctrl.errNO);
                });

                ctrl.btnDemoFixed.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn demo fixed pressed in " + getInstance().getClass());
                    event.consume();

                    gotoTranSelection("FIXED", ctrl.errType, ctrl.errNO);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void gotoTranSelection(String demoType, String errType, int errNO) {
            try {
                getInstance().replaceSceneContent("demo.tranSelection.fxml");

                pck.dbms.fe.demo.TranSelectionController ctrl = getInstance().loader.getController();

                ctrl.demoType = demoType;
                ctrl.errType = errType;
                ctrl.errNO = errNO;

                ctrl.liablePrim.setText("DEMO " + demoType + " " + errType + " " + errNO);

                ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Btn logout pressed in " + getInstance().getClass());
                    event.consume();

                    Demo.gotoTypeSelection(ctrl.errType, ctrl.errNO);
                });
                ctrl.imgVLogout.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public enum ERROR_TYPE {
            DIRTY_READ, LOST_UPDATE, NON_REPEATABLE_READ, PHANTOM, DEADLOCK
        }

        // !DONE

        /**
         * T1: user create order but faild
         * T2: partner views its orders
         */
        public static class DirtyRead1 {
            public static String errType = errorTypes.get(ERROR_TYPE.DIRTY_READ);
            public static int errNo = 1;

            public static class Error {
                public static String demoType = "ERROR";

                private static void t1_gotoHome() {

                    try {
                        App.loginInfo.setUsername("customer1");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.customer.setLogin(App.loginInfo);

                        getInstance().replaceSceneContent("customer.home.fxml");

                        pck.dbms.fe.customer.home.Controller controller = getInstance().loader.getController();
                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            Demo.gotoTranSelection(demoType, errType, errNo);
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                            t1_gotoListPartner();

                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t1_gotoListPartner() {
                    try {
                        getInstance().replaceSceneContent("customer.placeOrder.listPartner.fxml");

                        pck.dbms.fe.customer.placeOrder.listPartner.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t1_gotoHome();
                        });

                        controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        controller.imgVLogout.setVisible(false);

                        // get list Partner
                        DatabaseCommunication.customer.getListPartner(controller.partners);

                        TableView<Partner> tableView = controller.tableViewPartner;
                        tableView.setRowFactory(param -> {
                            return new TableRow<Partner>() {
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
                        Callback<TableColumn<Partner, String>, TableCell<Partner, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Partner, String> call(final TableColumn<Partner, String> param) {
                                return new TableCell<>() {

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
                                                t1_gotoListProduct(partner);
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        controller.colBtn.setCellFactory(cellFactory);

                        for (String key : controller.partners.keySet()) {
                            tableView.getItems().add(controller.partners.get(key));
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t1_gotoListProduct(Partner partner) {
                    try {

                        getInstance().replaceSceneContent("customer.placeOrder.listProduct.fxml");

                        pck.dbms.fe.customer.placeOrder.listProduct.Controller controller = getInstance().loader.getController();

                        controller.partner = partner;

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t1_gotoListPartner();
                        });

                        controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.imgVCart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {

                            @Override
                            public void handle(MouseEvent event) {
                                System.out.println("Btn cart pressed in " + getInstance().getClass());
                                event.consume();

                                try {
                                    Stage stageCartDetails = new Stage();
                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("customer.placeOrder.cartDetails.fxml"), null, new JavaFXBuilderFactory());
                                    Parent root = loader.load();
                                    stageCartDetails.initOwner(getInstance().stage);
                                    stageCartDetails.setScene(new Scene(root));
                                    stageCartDetails.setTitle("Đơn hàng của bạn");
                                    stageCartDetails.initModality(Modality.APPLICATION_MODAL);

                                    stageCartDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                    stageCartDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                    stageCartDetails.setResizable(false);
                                    stageCartDetails.setFullScreen(false);
                                    stageCartDetails.sizeToScene();

                                    //* get cart detail from server
                                    Cart cart = new Cart();
                                    cart.customer = Container.customer;
                                    cart.partner = partner;

                                    DatabaseCommunication.customer.getCartDetails(cart);

                                    pck.dbms.fe.customer.placeOrder.listProduct.cartDetail.Controller modalCtrl = loader.getController();

                                    modalCtrl.cart = cart;
                                    modalCtrl.partner = controller.partner;
                                    modalCtrl.partnerBranches = controller.partnerBranches;
                                    modalCtrl.products = controller.products;

                                    int col = 0, row = 0;
                                    modalCtrl.gridPaneCartDetails.getChildren().clear();
                                    for (CartDetail cd : cart.cartDetails) {
                                        CartDetailPane cdp = new CartDetailPane(cd);

                                        Pane pTemp = cdp.get();

                                        GridPane.setConstraints(pTemp, col, row);
                                        row++;

                                        modalCtrl.gridPaneCartDetails.getChildren().add(pTemp);
                                    }

                                    modalCtrl.comboBoxPaymentMethod.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                                    modalCtrl.comboBoxPaymentMethod.setOnAction((eCBPM) -> {
                                        Object selectedItem = modalCtrl.comboBoxPaymentMethod.getSelectionModel().getSelectedItem();
                                        modalCtrl.paymentMethod = String.valueOf(selectedItem);
                                        if (modalCtrl.paymentMethod.equals("Tiền mặt"))
                                            modalCtrl.paymentMethod = "CASH";
                                        modalCtrl.btnPlaceOrder.setDisable(false);
                                    });

                                    modalCtrl.labelTotal.setText(String.format("%f VNĐ", modalCtrl.cart.total));

                                    modalCtrl.btnPlaceOrder.setDisable(true);
                                    modalCtrl.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            System.out.println(getClass() + " btnPlaceOrder on click");
                                            if (DatabaseCommunication.customer.createOrderButFailError(new Order(cart, modalCtrl.paymentMethod), cart)) {

                                                System.out.println("Đặt hàng thành công");
                                                stageCartDetails.close();

                                                App.getInstance().showNotification(getInstance().stage, "Đặt hàng thành công", "bg-success");
                                            } else {
                                                App.getInstance().showNotification(stageCartDetails, "Có lỗi xảy ra, đặt hàng thất bại", "bg-danger");
                                            }
                                        }
                                    });

                                    stageCartDetails.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        // load product
                        DatabaseCommunication.customer.getListProduct(controller.partner, controller.partnerBranches, controller.products);
                        GridPane gp = controller.gridPaneProd;

                        int row = 0, col = 0;
                        for (String key : controller.products.keySet()) {
                            Product p = controller.products.get(key);

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
                            } else {
                                p.setImgSrc("https://res.cloudinary.com/phatchaukhang/image/upload/v1649956615/HQTCSDL/product-images/product-default-list-350_nejngg.jpg");
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

                /**
                 * Action chains to demo: A customer place a order but fail
                 * Chains: customer home -> list partner -> list product -> cart detail <== FAILED HERE
                 */
                public static void gotoTran1() {
                    t1_gotoHome();
                }

                public static void t2_gotoHome() {
                    try {
                        getInstance().replaceSceneContent("partner.home.fxml");
                        App.loginInfo.setUsername("partner_food");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.partner.setLogin(App.loginInfo);

                        pck.dbms.fe.partner.home.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            Demo.gotoTranSelection("ERROR", errorTypes.get(ERROR_TYPE.DIRTY_READ), 1);
                        });

                        controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnOrd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn order pressed in " + getInstance().getClass());
                            event.consume();

                            t2_gotoOrdersManagement();
                        });

                        controller.btnCnt.setDisable(true);
                        controller.btnProd.setDisable(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                private static void t2_gotoOrdersManagement() {
                    try {
                        getInstance().replaceSceneContent("partner.ordersManagement.fxml");

                        pck.dbms.fe.partner.features.OrdersManagementController ctrl = getInstance().loader.getController();

                        ctrl.partner = Container.partner;

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            t2_gotoHome();
                        });

                        ctrl.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        ctrl.imgVLogout.setVisible(false);
                        // get order list from DB
                        DatabaseCommunication.partner.getOrdersDirtyRead1Error(ctrl.partner, ctrl.orderHashMap);

                        //setting for value factory
                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
                        ctrl.colCusName.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Order, String>("customerAddress"));
                        ctrl.colPrice.setCellValueFactory(new PropertyValueFactory<Order, String>("total"));
                        ctrl.colDiSta.setCellValueFactory(new PropertyValueFactory<Order, String>("deliveryStatus"));
                        ctrl.colPaidSta.setCellValueFactory(new PropertyValueFactory<Order, String>("paidStatus"));
                        Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell call(final TableColumn<Order, String> param) {
                                final TableCell<Order, String> cell = new TableCell<Order, String>() {

                                    final Button btn = new Button("Chọn");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Order order = getTableView().getItems().get(getIndex());
                                                System.out.println("btn clicked");
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                                return cell;
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        // add order to table view
                        for (String key : ctrl.orderHashMap.keySet()) {
                            ctrl.tableView.getItems().add(ctrl.orderHashMap.get(key));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /**
                 * Action chain to demo: Partner view its orders
                 */
                public static void gotoTran2() {
                    t2_gotoHome();
                }


            }

            public static class Fixed {
                public static String demoType = "FIXED";

                private static void t1_gotoHome() {

                    try {
                        App.loginInfo.setUsername("customer1");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.customer.setLogin(App.loginInfo);

                        getInstance().replaceSceneContent("customer.home.fxml");

                        pck.dbms.fe.customer.home.Controller controller = getInstance().loader.getController();
                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            Demo.gotoTranSelection(demoType, errType, errNo);
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            t1_gotoListPartner();
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t1_gotoListPartner() {
                    try {
                        getInstance().replaceSceneContent("customer.placeOrder.listPartner.fxml");

                        pck.dbms.fe.customer.placeOrder.listPartner.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t1_gotoHome();
                        });

                        controller.imgVLogout.setVisible(false);

                        // get list Partner
                        DatabaseCommunication.customer.getListPartner(controller.partners);

                        TableView<Partner> tableView = controller.tableViewPartner;
                        tableView.setRowFactory(param -> {
                            return new TableRow<Partner>() {
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
                        Callback<TableColumn<Partner, String>, TableCell<Partner, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Partner, String> call(final TableColumn<Partner, String> param) {
                                return new TableCell<>() {

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
                                                t1_gotoListProduct(partner);
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        controller.colBtn.setCellFactory(cellFactory);

                        for (String key : controller.partners.keySet()) {
                            tableView.getItems().add(controller.partners.get(key));
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t1_gotoListProduct(Partner partner) {
                    try {

                        getInstance().replaceSceneContent("customer.placeOrder.listProduct.fxml");

                        pck.dbms.fe.customer.placeOrder.listProduct.Controller controller = getInstance().loader.getController();

                        controller.partner = partner;

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t1_gotoListPartner();
                        });

                        controller.imgVLogout.setVisible(false);

                        controller.imgVCart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {

                            @Override
                            public void handle(MouseEvent event) {
                                System.out.println("Btn cart pressed in " + getInstance().getClass());
                                event.consume();

                                try {
                                    Stage stageCartDetails = new Stage();
                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("customer.placeOrder.cartDetails.fxml"), null, new JavaFXBuilderFactory());
                                    Parent root = loader.load();
                                    stageCartDetails.initOwner(getInstance().stage);
                                    stageCartDetails.setScene(new Scene(root));
                                    stageCartDetails.setTitle("Đơn hàng của bạn");
                                    stageCartDetails.initModality(Modality.APPLICATION_MODAL);

                                    stageCartDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                    stageCartDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                    stageCartDetails.setResizable(false);
                                    stageCartDetails.setFullScreen(false);
                                    stageCartDetails.sizeToScene();

                                    //* get cart detail from server
                                    Cart cart = new Cart();
                                    cart.customer = Container.customer;
                                    cart.partner = partner;

                                    DatabaseCommunication.customer.getCartDetails(cart);

                                    pck.dbms.fe.customer.placeOrder.listProduct.cartDetail.Controller modalCtrl = loader.getController();

                                    modalCtrl.cart = cart;
                                    modalCtrl.partner = controller.partner;
                                    modalCtrl.partnerBranches = controller.partnerBranches;
                                    modalCtrl.products = controller.products;

                                    int col = 0, row = 0;
                                    modalCtrl.gridPaneCartDetails.getChildren().clear();
                                    for (CartDetail cd : cart.cartDetails) {
                                        CartDetailPane cdp = new CartDetailPane(cd);

                                        Pane pTemp = cdp.get();

                                        GridPane.setConstraints(pTemp, col, row);
                                        row++;

                                        modalCtrl.gridPaneCartDetails.getChildren().add(pTemp);
                                    }

                                    modalCtrl.comboBoxPaymentMethod.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                                    modalCtrl.comboBoxPaymentMethod.setOnAction((eCBPM) -> {
                                        Object selectedItem = modalCtrl.comboBoxPaymentMethod.getSelectionModel().getSelectedItem();
                                        modalCtrl.paymentMethod = String.valueOf(selectedItem);
                                        if (modalCtrl.paymentMethod.equals("Tiền mặt"))
                                            modalCtrl.paymentMethod = "CASH";
                                        modalCtrl.btnPlaceOrder.setDisable(false);
                                    });

                                    modalCtrl.labelTotal.setText(String.format("%f VNĐ", modalCtrl.cart.total));

                                    modalCtrl.btnPlaceOrder.setDisable(true);
                                    modalCtrl.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            System.out.println(getClass() + " btnPlaceOrder on click");
                                            if (DatabaseCommunication.customer.createOrderButFailFixed(new Order(cart, modalCtrl.paymentMethod), cart)) {

                                                System.out.println("Đặt hàng thành công");
                                                stageCartDetails.close();

                                                App.getInstance().showNotification(getInstance().stage, "Đặt hàng thành công", "bg-success");
                                            } else {
                                                App.getInstance().showNotification(stageCartDetails, "Có lỗi xảy ra, đặt hàng thất bại", "bg-danger");
                                            }
                                        }
                                    });

                                    stageCartDetails.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        // load product
                        DatabaseCommunication.customer.getListProduct(controller.partner, controller.partnerBranches, controller.products);
                        GridPane gp = controller.gridPaneProd;

                        int row = 0, col = 0;
                        for (String key : controller.products.keySet()) {
                            Product p = controller.products.get(key);

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
                            } else {
                                p.setImgSrc("https://res.cloudinary.com/phatchaukhang/image/upload/v1649956615/HQTCSDL/product-images/product-default-list-350_nejngg.jpg");
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

                /**
                 * Action chains to demo: A customer place a order but fail
                 * Chains: customer home -> list partner -> list product -> cart detail <== FAILED HERE
                 */
                public static void gotoTran1() {
                    t1_gotoHome();
                }

                public static void t2_gotoHome() {
                    try {
                        getInstance().replaceSceneContent("partner.home.fxml");
                        App.loginInfo.setUsername("partner_food");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.partner.setLogin(App.loginInfo);

                        pck.dbms.fe.partner.home.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            Demo.gotoTranSelection("FIXED", errorTypes.get(ERROR_TYPE.DIRTY_READ), 1);
                        });

                        controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnOrd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn order pressed in " + getInstance().getClass());
                            event.consume();

                            t2_gotoOrdersManagement();
                        });

                        controller.btnCnt.setDisable(true);
                        controller.btnProd.setDisable(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                private static void t2_gotoOrdersManagement() {
                    try {
                        getInstance().replaceSceneContent("partner.ordersManagement.fxml");

                        pck.dbms.fe.partner.features.OrdersManagementController ctrl = getInstance().loader.getController();

                        ctrl.partner = Container.partner;

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            t2_gotoHome();
                        });

                        ctrl.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        ctrl.imgVLogout.setVisible(false);
                        // get order list from DB
                        DatabaseCommunication.partner.getOrdersDirtyRead1Fixed(ctrl.partner, ctrl.orderHashMap);

                        //setting for value factory
                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
                        ctrl.colCusName.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Order, String>("customerAddress"));
                        ctrl.colPrice.setCellValueFactory(new PropertyValueFactory<Order, String>("total"));
                        ctrl.colDiSta.setCellValueFactory(new PropertyValueFactory<Order, String>("deliveryStatus"));
                        ctrl.colPaidSta.setCellValueFactory(new PropertyValueFactory<Order, String>("paidStatus"));
                        Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell call(final TableColumn<Order, String> param) {
                                final TableCell<Order, String> cell = new TableCell<Order, String>() {

                                    final Button btn = new Button("Chọn");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Order order = getTableView().getItems().get(getIndex());
                                                System.out.println("btn clicked");
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                                return cell;
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        // add order to table view
                        for (String key : ctrl.orderHashMap.keySet()) {
                            ctrl.tableView.getItems().add(ctrl.orderHashMap.get(key));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /**
                 * Action chain to demo: Partner view its orders
                 */
                public static void gotoTran2() {
                    t2_gotoHome();
                }
            }

        }

        /**
         * T1: Employee accepts a contract but fail
         * T2: Partner views its contracts
         */
        public static class DirtyRead2 {
            public static String errType = errorTypes.get(ERROR_TYPE.DIRTY_READ);
            public static int errNo = 2;

            public static class Error {

                public static void t1_gotoHome() {
                    try {
                        getInstance().replaceSceneContent("employee.home.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        pck.dbms.fe.employee.HomeController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            Demo.gotoTranSelection("ERROR", errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnPnMng.setDisable(true);

                        ctrl.btnCntrMng.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                            System.out.println("btnCntrMng clickled at " + getInstance().getClass());

                            t1_gotoListContract();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void t1_gotoListContract() {
                    try {
                        getInstance().replaceSceneContent("employee.listContract.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        ListContractController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();

                            t1_gotoHome();
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnAcceptAll.setDisable(true);


                        // set up table view
                        Contract c = new Contract();

                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colCID.setCellValueFactory(new PropertyValueFactory<Contract, String>("CID"));
                        ctrl.colName.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerName"));
                        ctrl.colRepName.setCellValueFactory(new PropertyValueFactory<Contract, String>("repName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerAddress"));
                        ctrl.colStat.setCellValueFactory(new PropertyValueFactory<Contract, String>("status"));
                        Callback<TableColumn<Contract, String>, TableCell<Contract, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Contract, String> call(final TableColumn<Contract, String> param) {
                                return new TableCell<Contract, String>() {

                                    final Button btn = new Button("Chi tiết");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Contract contract = getTableView().getItems().get(getIndex());
                                                System.out.println("btn detail clicked");

                                                try {
                                                    Stage stageCntrDetails = new Stage();
                                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("employee.contractDetail.fxml"), null, new JavaFXBuilderFactory());
                                                    Parent root = loader.load();
                                                    stageCntrDetails.initOwner(getInstance().stage);
                                                    stageCntrDetails.setScene(new Scene(root));
                                                    stageCntrDetails.setTitle("Chi tiết hợp đồng");
                                                    stageCntrDetails.initModality(Modality.NONE);

                                                    stageCntrDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                                    stageCntrDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                                    stageCntrDetails.setResizable(false);
                                                    stageCntrDetails.setFullScreen(false);
                                                    stageCntrDetails.sizeToScene();

                                                    pck.dbms.fe.employee.ContractDetailController ctrl = loader.getController();
                                                    ctrl.contract = contract;

                                                    // set value
                                                    ctrl.lblPnName.setText(ctrl.contract.getPartnerName());
                                                    ctrl.lblPnAddr.setText(ctrl.contract.getPartner().getAddress().toString());
                                                    ctrl.lblPhone.setText(ctrl.contract.getPartner().getPhone());
                                                    ctrl.lblMail.setText(ctrl.contract.getPartner().getMail());
                                                    ctrl.lblBranchNum.setText(String.valueOf(ctrl.contract.getPartner().getBranchNumber()));
                                                    ctrl.lblOrdNum.setText(String.valueOf(ctrl.contract.getPartner().getOrderNumber()));
                                                    ctrl.lblProdType.setText(ctrl.contract.getPartner().getProductType());
                                                    ctrl.lblTIN.setText(ctrl.contract.getTIN());
                                                    ctrl.lblComm.setText(String.format("%.2f", ctrl.contract.getCommission() * 100) + " %");
                                                    ctrl.lblAccStat.setText(contract.getStatus().equals("ACCEPTED") ? "Đã duyệt" : "Chưa duyệt");
                                                    ctrl.lblAccStat.getStyleClass().add(contract.getStatus().equals("ACCEPTED") ? "text-success" : "text-danger");
                                                    ctrl.lblCA.setText(ctrl.contract.getCreatedAt().toString());
                                                    ctrl.lblEA.setText(ctrl.contract.getExpiredAt().toString());

                                                    ctrl.btnAcc.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAccEvent -> {
                                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                        alert.setTitle("Duyệt hợp đồng");
                                                        alert.setHeaderText("Bạn muốn duyệt hợp đồng này ?");

                                                        // option != null.
                                                        Optional<ButtonType> option = alert.showAndWait();

                                                        if (option.isPresent() && option.get() == ButtonType.OK) {
                                                            if (DatabaseCommunication.employee.acceptContractButFailDirtyRead2Error(ctrl.contract)) {
                                                                stageCntrDetails.close();
                                                                getInstance().showNotification(getInstance().stage, "Duyệt thành công hợp đồng " + ctrl.contract.getCID(), "bg-success");
                                                            } else {
                                                                getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                            }
                                                        }

                                                    });
                                                    ctrl.btnRej.setDisable(true);

                                                    stageCntrDetails.show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();

                                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định", "bg-danger");
                                                }

                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        // load contract
                        DatabaseCommunication.employee.getContracts("*", ctrl.contractHashMap);

                        // add contract to table view
                        ctrl.tableView.getItems().addAll(ctrl.contractHashMap.values());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran1() {
                    t1_gotoHome();
                }

                public static void t2_gotoHome() {
                    try {
                        getInstance().replaceSceneContent("partner.home.fxml");
                        App.loginInfo.setUsername("partner_food");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.partner.setLogin(App.loginInfo);

                        pck.dbms.fe.partner.home.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            Demo.gotoTranSelection("ERROR", errType, errNo);
                        });

                        controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnCnt.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn order pressed in " + getInstance().getClass());
                            event.consume();

                            t2_gotoContractManagement();
                        });

                        controller.btnOrd.setDisable(true);
                        controller.btnProd.setDisable(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void t2_gotoContractManagement() {
                    try {
                        getInstance().replaceSceneContent("partner.contractsManagement.fxml");

                        pck.dbms.fe.partner.features.ContractsManagementController ctrl = getInstance().loader.getController();
                        ctrl.partner = Container.partner;


                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            t2_gotoHome();
                        });

                        ctrl.imgVLogout.setVisible(false);

                        // set up tabview
                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colCID.setCellValueFactory(new PropertyValueFactory<Contract, String>("CID"));
                        ctrl.colRepName.setCellValueFactory(new PropertyValueFactory<Contract, String>("repName"));
                        ctrl.colCA.setCellValueFactory(new PropertyValueFactory<Contract, LocalDateTime>("createdAt"));
                        ctrl.colEA.setCellValueFactory(new PropertyValueFactory<Contract, LocalDateTime>("expiredAt"));
                        ctrl.colComm.setCellValueFactory(new PropertyValueFactory<Contract, Double>("commission"));
                        ctrl.colStat.setCellValueFactory(new PropertyValueFactory<Contract, String>("status"));


                        // get contract list from DB
                        DatabaseCommunication.partner.getContractsDirtyRead2Error(ctrl.contractHashMap, "*", ctrl.partner);

                        ctrl.tableViewContractList.getItems().addAll(ctrl.contractHashMap.values());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran2() {
                    t2_gotoHome();
                }


            }

            public static class Fixed {

                public static void t1_gotoHome() {
                    try {
                        getInstance().replaceSceneContent("employee.home.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        pck.dbms.fe.employee.HomeController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            Demo.gotoTranSelection("FIXED", errorTypes.get(ERROR_TYPE.DIRTY_READ), 1);
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnPnMng.setDisable(true);

                        ctrl.btnCntrMng.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                            System.out.println("btnCntrMng clickled at " + getInstance().getClass());

                            t1_gotoListContract();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void t1_gotoListContract() {
                    try {
                        getInstance().replaceSceneContent("employee.listContract.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        ListContractController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();

                            t1_gotoHome();
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnAcceptAll.setDisable(true);


                        // set up table view
                        Contract c = new Contract();

                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colCID.setCellValueFactory(new PropertyValueFactory<Contract, String>("CID"));
                        ctrl.colName.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerName"));
                        ctrl.colRepName.setCellValueFactory(new PropertyValueFactory<Contract, String>("repName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerAddress"));
                        ctrl.colStat.setCellValueFactory(new PropertyValueFactory<Contract, String>("status"));
                        Callback<TableColumn<Contract, String>, TableCell<Contract, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Contract, String> call(final TableColumn<Contract, String> param) {
                                return new TableCell<Contract, String>() {

                                    final Button btn = new Button("Chi tiết");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Contract contract = getTableView().getItems().get(getIndex());
                                                System.out.println("btn detail clicked");

                                                try {
                                                    Stage stageCntrDetails = new Stage();
                                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("employee.contractDetail.fxml"), null, new JavaFXBuilderFactory());
                                                    Parent root = loader.load();
                                                    stageCntrDetails.initOwner(getInstance().stage);
                                                    stageCntrDetails.setScene(new Scene(root));
                                                    stageCntrDetails.setTitle("Chi tiết hợp đồng");
                                                    stageCntrDetails.initModality(Modality.NONE);

                                                    stageCntrDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                                    stageCntrDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                                    stageCntrDetails.setResizable(false);
                                                    stageCntrDetails.setFullScreen(false);
                                                    stageCntrDetails.sizeToScene();

                                                    pck.dbms.fe.employee.ContractDetailController ctrl = loader.getController();
                                                    ctrl.contract = contract;

                                                    // set value
                                                    ctrl.lblPnName.setText(ctrl.contract.getPartnerName());
                                                    ctrl.lblPnAddr.setText(ctrl.contract.getPartner().getAddress().toString());
                                                    ctrl.lblPhone.setText(ctrl.contract.getPartner().getPhone());
                                                    ctrl.lblMail.setText(ctrl.contract.getPartner().getMail());
                                                    ctrl.lblBranchNum.setText(String.valueOf(ctrl.contract.getPartner().getBranchNumber()));
                                                    ctrl.lblOrdNum.setText(String.valueOf(ctrl.contract.getPartner().getOrderNumber()));
                                                    ctrl.lblProdType.setText(ctrl.contract.getPartner().getProductType());
                                                    ctrl.lblTIN.setText(ctrl.contract.getTIN());
                                                    ctrl.lblComm.setText(String.format("%.2f", ctrl.contract.getCommission() * 100) + " %");
                                                    ctrl.lblAccStat.setText(contract.getStatus().equals("ACCEPTED") ? "Đã duyệt" : "Chưa duyệt");
                                                    ctrl.lblAccStat.getStyleClass().add(contract.getStatus().equals("ACCEPTED") ? "text-success" : "text-danger");
                                                    ctrl.lblCA.setText(ctrl.contract.getCreatedAt().toString());
                                                    ctrl.lblEA.setText(ctrl.contract.getExpiredAt().toString());

                                                    ctrl.btnAcc.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                                        @Override
                                                        public void handle(MouseEvent mouseEvent) {

                                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                            alert.setTitle("Duyệt hợp đồng");
                                                            alert.setHeaderText("Bạn muốn duyệt hợp đồng này ?");

                                                            // option != null.
                                                            Optional<ButtonType> option = alert.showAndWait();

                                                            if (option.get() == ButtonType.OK) {
                                                                System.out.println("user chose OK");
                                                                System.out.println("stat" + LocalDateTime.now());
                                                                boolean flag = DatabaseCommunication.employee.acceptContractButFailDirtyRead2Fixed(ctrl.contract);
                                                                System.out.println("end" + LocalDateTime.now());
                                                                if (flag) {
                                                                    stageCntrDetails.close();
                                                                    getInstance().showNotification(getInstance().stage, "Duyệt thành công hợp đồng " + ctrl.contract.getCID(), "bg-success");
                                                                } else {
                                                                    getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                                }
                                                            }

                                                        }
                                                    });
                                                    ctrl.btnRej.setDisable(true);

                                                    stageCntrDetails.show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();

                                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định", "bg-danger");
                                                }
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        // load contract
                        DatabaseCommunication.employee.getContracts("*", ctrl.contractHashMap);

                        // add contract to table view
                        ctrl.tableView.getItems().addAll(ctrl.contractHashMap.values());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran1() {
                    t1_gotoHome();
                }

                public static void t2_gotoHome() {
                    try {
                        getInstance().replaceSceneContent("partner.home.fxml");
                        App.loginInfo.setUsername("partner_food");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.partner.setLogin(App.loginInfo);

                        pck.dbms.fe.partner.home.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            Demo.gotoTranSelection("ERROR", errorTypes.get(ERROR_TYPE.DIRTY_READ), 1);
                        });

                        controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnCnt.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn order pressed in " + getInstance().getClass());
                            event.consume();

                            t2_gotoContractManagement();
                        });

                        controller.btnOrd.setDisable(true);
                        controller.btnProd.setDisable(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void t2_gotoContractManagement() {
                    try {
                        getInstance().replaceSceneContent("partner.contractsManagement.fxml");

                        pck.dbms.fe.partner.features.ContractsManagementController ctrl = getInstance().loader.getController();
                        ctrl.partner = Container.partner;


                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            t2_gotoHome();
                        });

                        ctrl.imgVLogout.setVisible(false);

                        // set up tabview
                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colCID.setCellValueFactory(new PropertyValueFactory<Contract, String>("CID"));
                        ctrl.colRepName.setCellValueFactory(new PropertyValueFactory<Contract, String>("repName"));
                        ctrl.colCA.setCellValueFactory(new PropertyValueFactory<Contract, LocalDateTime>("createdAt"));
                        ctrl.colEA.setCellValueFactory(new PropertyValueFactory<Contract, LocalDateTime>("expiredAt"));
                        ctrl.colComm.setCellValueFactory(new PropertyValueFactory<Contract, Double>("commission"));
                        ctrl.colStat.setCellValueFactory(new PropertyValueFactory<Contract, String>("status"));


                        // get contract list from DB
                        DatabaseCommunication.partner.getContractsDirtyRead2Fixed(ctrl.contractHashMap, "*", ctrl.partner);

                        ctrl.tableViewContractList.getItems().addAll(ctrl.contractHashMap.values());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran2() {
                    t2_gotoHome();
                }


            }
        }

        public static class LostUpdate1 {
            public static String errType = errorTypes.get(ERROR_TYPE.LOST_UPDATE);
            public static int errNo = 1;

            public static class Error {

                public static String demoType = "ERROR";

                private static void t1_gotoHome() {

                    try {
                        App.loginInfo.setUsername("customer1");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.customer.setLogin(App.loginInfo);

                        getInstance().replaceSceneContent("customer.home.fxml");

                        pck.dbms.fe.customer.home.Controller controller = getInstance().loader.getController();
                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            Demo.gotoTranSelection(demoType, errType, errNo);
                        });

                        controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                            t1_gotoListPartner();

                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t1_gotoListPartner() {
                    try {
                        getInstance().replaceSceneContent("customer.placeOrder.listPartner.fxml");

                        pck.dbms.fe.customer.placeOrder.listPartner.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t1_gotoHome();
                        });

                        controller.imgVLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            getInstance().gotoSignIn();
                        });
                        controller.imgVLogout.setVisible(false);

                        // get list Partner
                        DatabaseCommunication.customer.getListPartner(controller.partners);

                        TableView<Partner> tableView = controller.tableViewPartner;
                        tableView.setRowFactory(param -> {
                            return new TableRow<Partner>() {
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
                        Callback<TableColumn<Partner, String>, TableCell<Partner, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Partner, String> call(final TableColumn<Partner, String> param) {
                                return new TableCell<>() {

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
                                                t1_gotoListProduct(partner);
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        controller.colBtn.setCellFactory(cellFactory);

                        for (String key : controller.partners.keySet()) {
                            tableView.getItems().add(controller.partners.get(key));
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t1_gotoListProduct(Partner partner) {
                    try {

                        getInstance().replaceSceneContent("customer.placeOrder.listProduct.fxml");

                        pck.dbms.fe.customer.placeOrder.listProduct.Controller controller = getInstance().loader.getController();

                        controller.partner = partner;

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t1_gotoListPartner();
                        });

                        controller.imgVLogout.setVisible(false);

                        controller.imgVCart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {

                            @Override
                            public void handle(MouseEvent event) {
                                System.out.println("Btn cart pressed in " + getInstance().getClass());
                                event.consume();

                                try {
                                    Stage stageCartDetails = new Stage();
                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("customer.placeOrder.cartDetails.fxml"), null, new JavaFXBuilderFactory());
                                    Parent root = loader.load();
                                    stageCartDetails.initOwner(getInstance().stage);
                                    stageCartDetails.setScene(new Scene(root));
                                    stageCartDetails.setTitle("Đơn hàng của bạn");
                                    stageCartDetails.initModality(Modality.APPLICATION_MODAL);

                                    stageCartDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                    stageCartDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                    stageCartDetails.setResizable(false);
                                    stageCartDetails.setFullScreen(false);
                                    stageCartDetails.sizeToScene();

                                    //* get cart detail from server
                                    Cart cart = new Cart();
                                    cart.customer = Container.customer;
                                    cart.partner = partner;

                                    DatabaseCommunication.customer.getCartDetails(cart);

                                    pck.dbms.fe.customer.placeOrder.listProduct.cartDetail.Controller modalCtrl = loader.getController();

                                    modalCtrl.cart = cart;
                                    modalCtrl.partner = controller.partner;
                                    modalCtrl.partnerBranches = controller.partnerBranches;
                                    modalCtrl.products = controller.products;

                                    int col = 0, row = 0;
                                    modalCtrl.gridPaneCartDetails.getChildren().clear();
                                    for (CartDetail cd : cart.cartDetails) {
                                        CartDetailPane cdp = new CartDetailPane(cd);

                                        Pane pTemp = cdp.get();

                                        GridPane.setConstraints(pTemp, col, row);
                                        row++;

                                        modalCtrl.gridPaneCartDetails.getChildren().add(pTemp);
                                    }

                                    modalCtrl.comboBoxPaymentMethod.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                                    modalCtrl.comboBoxPaymentMethod.setOnAction((eCBPM) -> {
                                        Object selectedItem = modalCtrl.comboBoxPaymentMethod.getSelectionModel().getSelectedItem();
                                        modalCtrl.paymentMethod = String.valueOf(selectedItem);
                                        if (modalCtrl.paymentMethod.equals("Tiền mặt"))
                                            modalCtrl.paymentMethod = "CASH";
                                        modalCtrl.btnPlaceOrder.setDisable(false);
                                    });

                                    modalCtrl.labelTotal.setText(String.format("%f VNĐ", modalCtrl.cart.total));

                                    modalCtrl.btnPlaceOrder.setDisable(true);
                                    modalCtrl.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            System.out.println(getClass() + " btnPlaceOrder on click");
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Xác nhận đặt hàng");
                                            alert.setHeaderText("Bạn có chắc muốn đặt hàng ?");

                                            // option != null.
                                            Optional<ButtonType> option = alert.showAndWait();

                                            if (option.get() == ButtonType.OK) {
                                                if (DatabaseCommunication.customer.createOrderLostUpdate1ErrorT1(new Order(cart, modalCtrl.paymentMethod), cart)) {

                                                    System.out.println("Đặt hàng thành công");
                                                    stageCartDetails.close();

                                                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                                    alert2.setTitle("Thông báo");
                                                    alert2.setHeaderText("Đặt hành thành công");

                                                    stageCartDetails.close();

                                                    alert2.initOwner(getInstance().stage);

                                                    Optional<ButtonType> option2 = alert2.showAndWait();

                                                    if (option2.get() == ButtonType.OK) {
                                                        App.Demo.LostUpdate1.Error.t1_gotoListProduct(modalCtrl.partner);
                                                    }
                                                } else {
                                                    App.getInstance().showNotification(stageCartDetails, "Có lỗi xảy ra, đặt hàng thất bại", "bg-danger");
                                                }
                                            }
                                        }
                                    });

                                    stageCartDetails.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        // load product
                        DatabaseCommunication.customer.getListProduct(controller.partner, controller.partnerBranches, controller.products);
                        GridPane gp = controller.gridPaneProd;

                        int row = 0, col = 0;
                        for (String key : controller.products.keySet()) {
                            Product p = controller.products.get(key);

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
                            } else {
                                p.setImgSrc("https://res.cloudinary.com/phatchaukhang/image/upload/v1649956615/HQTCSDL/product-images/product-default-list-350_nejngg.jpg");
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

                public static void gotoTran1() {
                    t1_gotoHome();
                }

                // -----------------------------------------

                private static void t2_gotoHome() {

                    try {
                        App.loginInfo.setUsername("customer2");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.customer.setLogin(App.loginInfo);

                        getInstance().replaceSceneContent("customer.home.fxml");

                        pck.dbms.fe.customer.home.Controller controller = getInstance().loader.getController();
                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            Demo.gotoTranSelection(demoType, errType, errNo);
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                            t2_gotoListPartner();

                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t2_gotoListPartner() {
                    try {
                        getInstance().replaceSceneContent("customer.placeOrder.listPartner.fxml");

                        pck.dbms.fe.customer.placeOrder.listPartner.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t2_gotoHome();
                        });
                        controller.imgVLogout.setVisible(false);

                        // get list Partner
                        DatabaseCommunication.customer.getListPartner(controller.partners);

                        TableView<Partner> tableView = controller.tableViewPartner;
                        tableView.setRowFactory(param -> {
                            return new TableRow<Partner>() {
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
                        Callback<TableColumn<Partner, String>, TableCell<Partner, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Partner, String> call(final TableColumn<Partner, String> param) {
                                return new TableCell<>() {

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
                                                t2_gotoListProduct(partner);
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        controller.colBtn.setCellFactory(cellFactory);

                        for (String key : controller.partners.keySet()) {
                            tableView.getItems().add(controller.partners.get(key));
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t2_gotoListProduct(Partner partner) {
                    try {

                        getInstance().replaceSceneContent("customer.placeOrder.listProduct.fxml");

                        pck.dbms.fe.customer.placeOrder.listProduct.Controller controller = getInstance().loader.getController();

                        controller.partner = partner;

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t2_gotoListPartner();
                        });

                        controller.imgVLogout.setVisible(false);

                        controller.imgVCart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {

                            @Override
                            public void handle(MouseEvent event) {
                                System.out.println("Btn cart pressed in " + getInstance().getClass());
                                event.consume();

                                try {
                                    Stage stageCartDetails = new Stage();
                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("customer.placeOrder.cartDetails.fxml"), null, new JavaFXBuilderFactory());
                                    Parent root = loader.load();
                                    stageCartDetails.initOwner(getInstance().stage);
                                    stageCartDetails.setScene(new Scene(root));
                                    stageCartDetails.setTitle("Đơn hàng của bạn");
                                    stageCartDetails.initModality(Modality.APPLICATION_MODAL);

                                    stageCartDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                    stageCartDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                    stageCartDetails.setResizable(false);
                                    stageCartDetails.setFullScreen(false);
                                    stageCartDetails.sizeToScene();

                                    //* get cart detail from server
                                    Cart cart = new Cart();
                                    cart.customer = Container.customer;
                                    cart.partner = partner;

                                    DatabaseCommunication.customer.getCartDetails(cart);

                                    pck.dbms.fe.customer.placeOrder.listProduct.cartDetail.Controller modalCtrl = loader.getController();

                                    modalCtrl.cart = cart;
                                    modalCtrl.partner = controller.partner;
                                    modalCtrl.partnerBranches = controller.partnerBranches;
                                    modalCtrl.products = controller.products;

                                    int col = 0, row = 0;
                                    modalCtrl.gridPaneCartDetails.getChildren().clear();
                                    for (CartDetail cd : cart.cartDetails) {
                                        CartDetailPane cdp = new CartDetailPane(cd);

                                        Pane pTemp = cdp.get();

                                        GridPane.setConstraints(pTemp, col, row);
                                        row++;

                                        modalCtrl.gridPaneCartDetails.getChildren().add(pTemp);
                                    }

                                    modalCtrl.comboBoxPaymentMethod.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                                    modalCtrl.comboBoxPaymentMethod.setOnAction((eCBPM) -> {
                                        Object selectedItem = modalCtrl.comboBoxPaymentMethod.getSelectionModel().getSelectedItem();
                                        modalCtrl.paymentMethod = String.valueOf(selectedItem);
                                        if (modalCtrl.paymentMethod.equals("Tiền mặt"))
                                            modalCtrl.paymentMethod = "CASH";
                                        modalCtrl.btnPlaceOrder.setDisable(false);
                                    });

                                    modalCtrl.labelTotal.setText(String.format("%f VNĐ", modalCtrl.cart.total));

                                    modalCtrl.btnPlaceOrder.setDisable(true);
                                    modalCtrl.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            System.out.println(getClass() + " btnPlaceOrder on click");
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Xác nhận đặt hàng");
                                            alert.setHeaderText("Bạn có chắc muốn đặt hàng ?");

                                            // option != null.
                                            Optional<ButtonType> option = alert.showAndWait();

                                            if (option.get() == ButtonType.OK) {
                                                if (DatabaseCommunication.customer.createOrderLostUpdate1ErrorT2(new Order(cart, modalCtrl.paymentMethod), cart)) {

                                                    System.out.println("Đặt hàng thành công");
                                                    stageCartDetails.close();

                                                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                                    alert2.setTitle("Thông báo");
                                                    alert2.setHeaderText("Đặt hành thành công");

                                                    stageCartDetails.close();

                                                    alert2.initOwner(getInstance().stage);

                                                    Optional<ButtonType> option2 = alert2.showAndWait();

                                                    if (option2.get() == ButtonType.OK) {
                                                        App.Demo.LostUpdate1.Error.t2_gotoListProduct(modalCtrl.partner);
                                                    }
                                                } else {
                                                    App.getInstance().showNotification(stageCartDetails, "Có lỗi xảy ra, đặt hàng thất bại", "bg-danger");
                                                }
                                            }
                                        }
                                    });

                                    stageCartDetails.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        // load product
                        DatabaseCommunication.customer.getListProduct(controller.partner, controller.partnerBranches, controller.products);
                        GridPane gp = controller.gridPaneProd;

                        int row = 0, col = 0;
                        for (String key : controller.products.keySet()) {
                            Product p = controller.products.get(key);

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
                            } else {
                                p.setImgSrc("https://res.cloudinary.com/phatchaukhang/image/upload/v1649956615/HQTCSDL/product-images/product-default-list-350_nejngg.jpg");
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

                public static void gotoTran2() {
                    t2_gotoHome();
                }
            }

            // =========================================================

            public static class Fixed {
                public static String demoType = "FIXED";

                private static void t1_gotoHome() {

                    try {
                        App.loginInfo.setUsername("customer1");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.customer.setLogin(App.loginInfo);

                        getInstance().replaceSceneContent("customer.home.fxml");

                        pck.dbms.fe.customer.home.Controller controller = getInstance().loader.getController();
                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            Demo.gotoTranSelection(demoType, errType, errNo);
                        });

                        controller.imgVLogout.setVisible(false);

                        controller.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                            t1_gotoListPartner();

                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t1_gotoListPartner() {
                    try {
                        getInstance().replaceSceneContent("customer.placeOrder.listPartner.fxml");

                        pck.dbms.fe.customer.placeOrder.listPartner.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t1_gotoHome();
                        });

                        controller.imgVLogout.setVisible(false);

                        // get list Partner
                        DatabaseCommunication.customer.getListPartner(controller.partners);

                        TableView<Partner> tableView = controller.tableViewPartner;
                        tableView.setRowFactory(param -> {
                            return new TableRow<Partner>() {
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
                        Callback<TableColumn<Partner, String>, TableCell<Partner, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Partner, String> call(final TableColumn<Partner, String> param) {
                                return new TableCell<>() {

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
                                                t1_gotoListProduct(partner);
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        controller.colBtn.setCellFactory(cellFactory);

                        for (String key : controller.partners.keySet()) {
                            tableView.getItems().add(controller.partners.get(key));
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t1_gotoListProduct(Partner partner) {
                    try {

                        getInstance().replaceSceneContent("customer.placeOrder.listProduct.fxml");

                        pck.dbms.fe.customer.placeOrder.listProduct.Controller controller = getInstance().loader.getController();

                        controller.partner = partner;

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t1_gotoListPartner();
                        });

                        controller.imgVLogout.setVisible(false);

                        controller.imgVCart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {

                            @Override
                            public void handle(MouseEvent event) {
                                System.out.println("Btn cart pressed in " + getInstance().getClass());
                                event.consume();

                                try {
                                    Stage stageCartDetails = new Stage();
                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("customer.placeOrder.cartDetails.fxml"), null, new JavaFXBuilderFactory());
                                    Parent root = loader.load();
                                    stageCartDetails.initOwner(getInstance().stage);
                                    stageCartDetails.setScene(new Scene(root));
                                    stageCartDetails.setTitle("Đơn hàng của bạn");
                                    stageCartDetails.initModality(Modality.APPLICATION_MODAL);

                                    stageCartDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                    stageCartDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                    stageCartDetails.setResizable(false);
                                    stageCartDetails.setFullScreen(false);
                                    stageCartDetails.sizeToScene();

                                    //* get cart detail from server
                                    Cart cart = new Cart();
                                    cart.customer = Container.customer;
                                    cart.partner = partner;

                                    DatabaseCommunication.customer.getCartDetails(cart);

                                    pck.dbms.fe.customer.placeOrder.listProduct.cartDetail.Controller modalCtrl = loader.getController();

                                    modalCtrl.cart = cart;
                                    modalCtrl.partner = controller.partner;
                                    modalCtrl.partnerBranches = controller.partnerBranches;
                                    modalCtrl.products = controller.products;

                                    int col = 0, row = 0;
                                    modalCtrl.gridPaneCartDetails.getChildren().clear();
                                    for (CartDetail cd : cart.cartDetails) {
                                        CartDetailPane cdp = new CartDetailPane(cd);

                                        Pane pTemp = cdp.get();

                                        GridPane.setConstraints(pTemp, col, row);
                                        row++;

                                        modalCtrl.gridPaneCartDetails.getChildren().add(pTemp);
                                    }

                                    modalCtrl.comboBoxPaymentMethod.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                                    modalCtrl.comboBoxPaymentMethod.setOnAction((eCBPM) -> {
                                        Object selectedItem = modalCtrl.comboBoxPaymentMethod.getSelectionModel().getSelectedItem();
                                        modalCtrl.paymentMethod = String.valueOf(selectedItem);
                                        if (modalCtrl.paymentMethod.equals("Tiền mặt"))
                                            modalCtrl.paymentMethod = "CASH";
                                        modalCtrl.btnPlaceOrder.setDisable(false);
                                    });

                                    modalCtrl.labelTotal.setText(String.format("%f VNĐ", modalCtrl.cart.total));

                                    modalCtrl.btnPlaceOrder.setDisable(true);
                                    modalCtrl.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            System.out.println(getClass() + " btnPlaceOrder on click");
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Xác nhận đặt hàng");
                                            alert.setHeaderText("Bạn có chắc muốn đặt hàng ?");

                                            // option != null.
                                            Optional<ButtonType> option = alert.showAndWait();

                                            if (option.get() == ButtonType.OK) {
                                                if (DatabaseCommunication.customer.createOrderLostUpdate1FixedT1(new Order(cart, modalCtrl.paymentMethod), cart)) {

                                                    System.out.println("Đặt hàng thành công");
                                                    stageCartDetails.close();

                                                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                                    alert2.setTitle("Thông báo");
                                                    alert2.setHeaderText("Đặt hành thành công");

                                                    stageCartDetails.close();

                                                    alert2.initOwner(getInstance().stage);

                                                    Optional<ButtonType> option2 = alert2.showAndWait();

                                                    if (option2.get() == ButtonType.OK) {
                                                        App.Demo.LostUpdate1.Error.t1_gotoListProduct(modalCtrl.partner);
                                                    }
                                                } else {
                                                    App.getInstance().showNotification(stageCartDetails, "Có lỗi xảy ra, đặt hàng thất bại", "bg-danger");
                                                }
                                            }
                                        }
                                    });

                                    stageCartDetails.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        // load product
                        DatabaseCommunication.customer.getListProduct(controller.partner, controller.partnerBranches, controller.products);
                        GridPane gp = controller.gridPaneProd;

                        int row = 0, col = 0;
                        for (String key : controller.products.keySet()) {
                            Product p = controller.products.get(key);

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
                            } else {
                                p.setImgSrc("https://res.cloudinary.com/phatchaukhang/image/upload/v1649956615/HQTCSDL/product-images/product-default-list-350_nejngg.jpg");
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

                public static void gotoTran1() {
                    t1_gotoHome();
                }

                // -----------------------------------------

                private static void t2_gotoHome() {

                    try {
                        App.loginInfo.setUsername("customer2");
                        App.loginInfo.setRole(ROLE.CUSTOMER);
                        Container.customer.setLogin(App.loginInfo);

                        getInstance().replaceSceneContent("customer.home.fxml");

                        pck.dbms.fe.customer.home.Controller controller = getInstance().loader.getController();
                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            Demo.gotoTranSelection(demoType, errType, errNo);
                        });
                        controller.imgVLogout.setVisible(false);

                        controller.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                            t2_gotoListPartner();

                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t2_gotoListPartner() {
                    try {
                        getInstance().replaceSceneContent("customer.placeOrder.listPartner.fxml");

                        pck.dbms.fe.customer.placeOrder.listPartner.Controller controller = getInstance().loader.getController();

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();
                            System.out.println("icon shake");
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t2_gotoHome();
                        });
                        controller.imgVLogout.setVisible(false);

                        // get list Partner
                        DatabaseCommunication.customer.getListPartner(controller.partners);

                        TableView<Partner> tableView = controller.tableViewPartner;
                        tableView.setRowFactory(param -> {
                            return new TableRow<Partner>() {
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
                        Callback<TableColumn<Partner, String>, TableCell<Partner, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Partner, String> call(final TableColumn<Partner, String> param) {
                                return new TableCell<>() {

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
                                                t2_gotoListProduct(partner);
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        controller.colBtn.setCellFactory(cellFactory);

                        for (String key : controller.partners.keySet()) {
                            tableView.getItems().add(controller.partners.get(key));
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                private static void t2_gotoListProduct(Partner partner) {
                    try {

                        getInstance().replaceSceneContent("customer.placeOrder.listProduct.fxml");

                        pck.dbms.fe.customer.placeOrder.listProduct.Controller controller = getInstance().loader.getController();

                        controller.partner = partner;

                        controller.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();
                            new animatefx.animation.Shake(controller.imgVBack).play();
                            t2_gotoListPartner();
                        });

                        controller.imgVLogout.setVisible(false);

                        controller.imgVCart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {

                            @Override
                            public void handle(MouseEvent event) {
                                System.out.println("Btn cart pressed in " + getInstance().getClass());
                                event.consume();

                                try {
                                    Stage stageCartDetails = new Stage();
                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("customer.placeOrder.cartDetails.fxml"), null, new JavaFXBuilderFactory());
                                    Parent root = loader.load();
                                    stageCartDetails.initOwner(getInstance().stage);
                                    stageCartDetails.setScene(new Scene(root));
                                    stageCartDetails.setTitle("Đơn hàng của bạn");
                                    stageCartDetails.initModality(Modality.APPLICATION_MODAL);

                                    stageCartDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                    stageCartDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                    stageCartDetails.setResizable(false);
                                    stageCartDetails.setFullScreen(false);
                                    stageCartDetails.sizeToScene();

                                    //* get cart detail from server
                                    Cart cart = new Cart();
                                    cart.customer = Container.customer;
                                    cart.partner = partner;

                                    DatabaseCommunication.customer.getCartDetails(cart);

                                    pck.dbms.fe.customer.placeOrder.listProduct.cartDetail.Controller modalCtrl = loader.getController();

                                    modalCtrl.cart = cart;
                                    modalCtrl.partner = controller.partner;
                                    modalCtrl.partnerBranches = controller.partnerBranches;
                                    modalCtrl.products = controller.products;

                                    int col = 0, row = 0;
                                    modalCtrl.gridPaneCartDetails.getChildren().clear();
                                    for (CartDetail cd : cart.cartDetails) {
                                        CartDetailPane cdp = new CartDetailPane(cd);

                                        Pane pTemp = cdp.get();

                                        GridPane.setConstraints(pTemp, col, row);
                                        row++;

                                        modalCtrl.gridPaneCartDetails.getChildren().add(pTemp);
                                    }

                                    modalCtrl.comboBoxPaymentMethod.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                                    modalCtrl.comboBoxPaymentMethod.setOnAction((eCBPM) -> {
                                        Object selectedItem = modalCtrl.comboBoxPaymentMethod.getSelectionModel().getSelectedItem();
                                        modalCtrl.paymentMethod = String.valueOf(selectedItem);
                                        if (modalCtrl.paymentMethod.equals("Tiền mặt"))
                                            modalCtrl.paymentMethod = "CASH";
                                        modalCtrl.btnPlaceOrder.setDisable(false);
                                    });

                                    modalCtrl.labelTotal.setText(String.format("%f VNĐ", modalCtrl.cart.total));

                                    modalCtrl.btnPlaceOrder.setDisable(true);
                                    modalCtrl.btnPlaceOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            System.out.println(getClass() + " btnPlaceOrder on click");
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Xác nhận đặt hàng");
                                            alert.setHeaderText("Bạn có chắc muốn đặt hàng ?");

                                            // option != null.
                                            Optional<ButtonType> option = alert.showAndWait();

                                            if (option.get() == ButtonType.OK) {
                                                if (DatabaseCommunication.customer.createOrderLostUpdate1FixedT2(new Order(cart, modalCtrl.paymentMethod), cart)) {

                                                    System.out.println("Đặt hàng thành công");
                                                    stageCartDetails.close();

                                                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                                    alert2.setTitle("Thông báo");
                                                    alert2.setHeaderText("Đặt hành thành công");

                                                    stageCartDetails.close();

                                                    alert2.initOwner(getInstance().stage);

                                                    Optional<ButtonType> option2 = alert2.showAndWait();

                                                    if (option2.get() == ButtonType.OK) {
                                                        App.Demo.LostUpdate1.Error.t2_gotoListProduct(modalCtrl.partner);
                                                    }
                                                } else {
                                                    App.getInstance().showNotification(stageCartDetails, "Có lỗi xảy ra, đặt hàng thất bại", "bg-danger");
                                                }
                                            }
                                        }
                                    });

                                    stageCartDetails.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        // load product
                        DatabaseCommunication.customer.getListProduct(controller.partner, controller.partnerBranches, controller.products);
                        GridPane gp = controller.gridPaneProd;

                        int row = 0, col = 0;
                        for (String key : controller.products.keySet()) {
                            Product p = controller.products.get(key);

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
                            } else {
                                p.setImgSrc("https://res.cloudinary.com/phatchaukhang/image/upload/v1649956615/HQTCSDL/product-images/product-default-list-350_nejngg.jpg");
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

                public static void gotoTran2() {
                    t2_gotoHome();
                }
            }
        }

        public static class Phantom2 {
            public static String errType = errorTypes.get(ERROR_TYPE.PHANTOM);
            public static int errNo = 2;

            public static class Error {
                public static String demoType = "ERROR";

                public static void t1_gotoHome() {
                    try {
                        getInstance().replaceSceneContent("employee.home.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        pck.dbms.fe.employee.HomeController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            Demo.gotoTranSelection(demoType, errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnPnMng.setDisable(true);

                        ctrl.btnCntrMng.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                            System.out.println("btnCntrMng clickled at " + getInstance().getClass());

                            t1_gotoListContract();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void t1_gotoListContract() {
                    try {
                        getInstance().replaceSceneContent("employee.listContract.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        ListContractController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();

                            t1_gotoHome();
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnAcceptAll.setDisable(false);
                        ctrl.btnAcceptAll.setOnAction(actionEvent -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                            int ctrNum = ctrl.contractHashMap.size();

                            alert.setTitle("Xác nhận");
                            alert.setHeaderText("Bạn có chắc muốn duyệt " + ctrNum + " hợp đồng");

                            Optional<ButtonType> option2 = alert.showAndWait();

                            if (option2.get() == ButtonType.OK) {
                                String accRes = DatabaseCommunication.employee.acceptAllContractPhantom2Error(ctrl.contractHashMap);
                                if (accRes.equals("success")) {
                                    getInstance().showNotification(getInstance().stage, "Đã duyệt " + ctrl.contractHashMap.size() + " hợp đồng", "bg-success");
                                } else if (accRes.contains("phantom")) {
                                    getInstance().showNotification(getInstance().stage, accRes, "bg-warning");
                                } else {
                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định: " + accRes, "bg-danger");
                                }
                                t1_gotoListContract_noReset();
                            }
                        });


                        // set up table view
                        Contract c = new Contract();

                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colCID.setCellValueFactory(new PropertyValueFactory<Contract, String>("CID"));
                        ctrl.colName.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerName"));
                        ctrl.colRepName.setCellValueFactory(new PropertyValueFactory<Contract, String>("repName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerAddress"));
                        ctrl.colStat.setCellValueFactory(new PropertyValueFactory<Contract, String>("status"));
                        Callback<TableColumn<Contract, String>, TableCell<Contract, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Contract, String> call(final TableColumn<Contract, String> param) {
                                return new TableCell<Contract, String>() {

                                    final Button btn = new Button("Chi tiết");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Contract contract = getTableView().getItems().get(getIndex());
                                                System.out.println("btn detail clicked");

                                                try {
                                                    Stage stageCntrDetails = new Stage();
                                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("employee.contractDetail.fxml"), null, new JavaFXBuilderFactory());
                                                    Parent root = loader.load();
                                                    stageCntrDetails.initOwner(getInstance().stage);
                                                    stageCntrDetails.setScene(new Scene(root));
                                                    stageCntrDetails.setTitle("Chi tiết hợp đồng");
                                                    stageCntrDetails.initModality(Modality.NONE);

                                                    stageCntrDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                                    stageCntrDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                                    stageCntrDetails.setResizable(false);
                                                    stageCntrDetails.setFullScreen(false);
                                                    stageCntrDetails.sizeToScene();

                                                    pck.dbms.fe.employee.ContractDetailController ctrl = loader.getController();
                                                    ctrl.contract = contract;

                                                    // set value
                                                    ctrl.lblPnName.setText(ctrl.contract.getPartnerName());
                                                    ctrl.lblPnAddr.setText(ctrl.contract.getPartner().getAddress().toString());
                                                    ctrl.lblPhone.setText(ctrl.contract.getPartner().getPhone());
                                                    ctrl.lblMail.setText(ctrl.contract.getPartner().getMail());
                                                    ctrl.lblBranchNum.setText(String.valueOf(ctrl.contract.getPartner().getBranchNumber()));
                                                    ctrl.lblOrdNum.setText(String.valueOf(ctrl.contract.getPartner().getOrderNumber()));
                                                    ctrl.lblProdType.setText(ctrl.contract.getPartner().getProductType());
                                                    ctrl.lblTIN.setText(ctrl.contract.getTIN());
                                                    ctrl.lblComm.setText(String.format("%.2f", ctrl.contract.getCommission() * 100) + " %");
                                                    ctrl.lblAccStat.setText(contract.getStatus().equals("ACCEPTED") ? "Đã duyệt" : "Chưa duyệt");
                                                    ctrl.lblAccStat.getStyleClass().add(contract.getStatus().equals("ACCEPTED") ? "text-success" : "text-danger");
                                                    ctrl.lblCA.setText(ctrl.contract.getCreatedAt().toString());
                                                    ctrl.lblEA.setText(ctrl.contract.getExpiredAt().toString());

                                                    ctrl.btnAcc.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAccEvent -> {
                                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                        alert.setTitle("Duyệt hợp đồng");
                                                        alert.setHeaderText("Bạn muốn duyệt hợp đồng này ?");

                                                        // option != null.
                                                        Optional<ButtonType> option = alert.showAndWait();

                                                        if (option.isPresent() && option.get() == ButtonType.OK) {
                                                            if (DatabaseCommunication.employee.acceptContractButFailDirtyRead2Error(ctrl.contract)) {
                                                                stageCntrDetails.close();
                                                                getInstance().showNotification(getInstance().stage, "Duyệt thành công hợp đồng " + ctrl.contract.getCID(), "bg-success");
                                                            } else {
                                                                getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                            }
                                                        }

                                                    });
                                                    ctrl.btnRej.setDisable(true);

                                                    stageCntrDetails.show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();

                                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định", "bg-danger");
                                                }
                                            });
                                            setGraphic(btn);
                                            btn.setDisable(true);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        //reset all contracts to pending
                        DatabaseCommunication.employee.resetToAllContractToPending();

                        // load contract
                        DatabaseCommunication.employee.getContracts("*", ctrl.contractHashMap);

                        // add contract to table view
                        ctrl.tableView.getItems().addAll(ctrl.contractHashMap.values());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void t1_gotoListContract_noReset() {
                    try {
                        getInstance().replaceSceneContent("employee.listContract.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        ListContractController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();

                            t1_gotoHome();
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnAcceptAll.setDisable(false);
                        ctrl.btnAcceptAll.setOnAction(actionEvent -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                            int ctrNum = ctrl.contractHashMap.size();

                            alert.setTitle("Xác nhận");
                            alert.setHeaderText("Bạn có chắc muốn duyệt " + ctrNum + " hợp đồng");

                            Optional<ButtonType> option2 = alert.showAndWait();

                            if (option2.get() == ButtonType.OK) {
                                String accRes = DatabaseCommunication.employee.acceptAllContractPhantom2Fixed(ctrl.contractHashMap);
                                if (accRes.equals("success")) {
                                    getInstance().showNotification(getInstance().stage, "Đã duyệt " + ctrl.contractHashMap.size() + " hợp đồng", "bg-success");
                                } else if (accRes.contains("phantom")) {
                                    getInstance().showNotification(getInstance().stage, accRes, "bg-warning");
                                } else {
                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định: " + accRes, "bg-danger");
                                }
                            }
                        });


                        // set up table view
                        Contract c = new Contract();

                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colCID.setCellValueFactory(new PropertyValueFactory<Contract, String>("CID"));
                        ctrl.colName.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerName"));
                        ctrl.colRepName.setCellValueFactory(new PropertyValueFactory<Contract, String>("repName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerAddress"));
                        ctrl.colStat.setCellValueFactory(new PropertyValueFactory<Contract, String>("status"));
                        Callback<TableColumn<Contract, String>, TableCell<Contract, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Contract, String> call(final TableColumn<Contract, String> param) {
                                return new TableCell<Contract, String>() {

                                    final Button btn = new Button("Chi tiết");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Contract contract = getTableView().getItems().get(getIndex());
                                                System.out.println("btn detail clicked");

                                                try {
                                                    Stage stageCntrDetails = new Stage();
                                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("employee.contractDetail.fxml"), null, new JavaFXBuilderFactory());
                                                    Parent root = loader.load();
                                                    stageCntrDetails.initOwner(getInstance().stage);
                                                    stageCntrDetails.setScene(new Scene(root));
                                                    stageCntrDetails.setTitle("Chi tiết hợp đồng");
                                                    stageCntrDetails.initModality(Modality.NONE);

                                                    stageCntrDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                                    stageCntrDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                                    stageCntrDetails.setResizable(false);
                                                    stageCntrDetails.setFullScreen(false);
                                                    stageCntrDetails.sizeToScene();

                                                    pck.dbms.fe.employee.ContractDetailController ctrl = loader.getController();
                                                    ctrl.contract = contract;

                                                    // set value
                                                    ctrl.lblPnName.setText(ctrl.contract.getPartnerName());
                                                    ctrl.lblPnAddr.setText(ctrl.contract.getPartner().getAddress().toString());
                                                    ctrl.lblPhone.setText(ctrl.contract.getPartner().getPhone());
                                                    ctrl.lblMail.setText(ctrl.contract.getPartner().getMail());
                                                    ctrl.lblBranchNum.setText(String.valueOf(ctrl.contract.getPartner().getBranchNumber()));
                                                    ctrl.lblOrdNum.setText(String.valueOf(ctrl.contract.getPartner().getOrderNumber()));
                                                    ctrl.lblProdType.setText(ctrl.contract.getPartner().getProductType());
                                                    ctrl.lblTIN.setText(ctrl.contract.getTIN());
                                                    ctrl.lblComm.setText(String.format("%.2f", ctrl.contract.getCommission() * 100) + " %");
                                                    ctrl.lblAccStat.setText(contract.getStatus().equals("ACCEPTED") ? "Đã duyệt" : "Chưa duyệt");
                                                    ctrl.lblAccStat.getStyleClass().add(contract.getStatus().equals("ACCEPTED") ? "text-success" : "text-danger");
                                                    ctrl.lblCA.setText(ctrl.contract.getCreatedAt().toString());
                                                    ctrl.lblEA.setText(ctrl.contract.getExpiredAt().toString());

                                                    ctrl.btnAcc.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAccEvent -> {
                                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                        alert.setTitle("Duyệt hợp đồng");
                                                        alert.setHeaderText("Bạn muốn duyệt hợp đồng này ?");

                                                        // option != null.
                                                        Optional<ButtonType> option = alert.showAndWait();

                                                        if (option.isPresent() && option.get() == ButtonType.OK) {
                                                            if (DatabaseCommunication.employee.acceptContractButFailDirtyRead2Error(ctrl.contract)) {
                                                                stageCntrDetails.close();
                                                                getInstance().showNotification(getInstance().stage, "Duyệt thành công hợp đồng " + ctrl.contract.getCID(), "bg-success");
                                                            } else {
                                                                getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                            }
                                                        }

                                                    });
                                                    ctrl.btnRej.setDisable(true);

                                                    stageCntrDetails.show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();

                                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định", "bg-danger");
                                                }
                                            });
                                            setGraphic(btn);
                                            btn.setDisable(true);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        // load contract
                        DatabaseCommunication.employee.getContracts("*", ctrl.contractHashMap);

                        // add contract to table view
                        ctrl.tableView.getItems().addAll(ctrl.contractHashMap.values());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran1() {
                    t1_gotoHome();
                }

                public static void t2_gotoRegister() {
                    try {
                        getInstance().replaceSceneContent("partner.registerContract.fxml", 400, 600);
                        App.loginInfo.setUsername("partner_food");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        RegisterContractController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();

                            App.Demo.gotoTranSelection(demoType, errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);

                        ctrl.lblPnName.setText(ctrl.partner.getName());
                        ctrl.lblPnAddr.setText(ctrl.partner.getAddress().toString());
                        ctrl.lblPhone.setText(ctrl.partner.getPhone());
                        ctrl.lblMail.setText(ctrl.partner.getMail());
                        ctrl.lblBranchNum.setText(String.valueOf(ctrl.partner.getBranchNumber()));
                        ctrl.lblOrdNum.setText(String.valueOf(ctrl.partner.getOrderNumber()));
                        ctrl.lblProdType.setText(ctrl.partner.getProductType());

                        int leftLimit = '0'; // letter 'a'
                        int rightLimit = '9'; // letter 'z'
                        int targetStringLength = 20;
                        Random random = new Random();

                        String TIN = random.ints(leftLimit, rightLimit + 1)
                                .limit(targetStringLength)
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                .toString();
                        ctrl.tbTIN.setText(TIN);
                        ctrl.dpStart.setValue(LocalDate.now());
                        ctrl.dbCtrTime.setText("6");
                        ctrl.tbComm.setText("10");

                        ctrl.btnRegister.setOnAction((ae) -> {
                            Contract c = new Contract();

                            String CID = random.ints('a', 'z' + 1)
                                    .limit(targetStringLength)
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString();

                            c.setCID(CID);
                            c.setPartner(ctrl.partner);
                            c.setTIN(TIN);
                            c.setCreatedAt(ctrl.dpStart.getValue().atTime(0, 0));
                            c.setExpiredAt(ctrl.dpStart.getValue().plusMonths(Integer.parseInt(ctrl.dbCtrTime.getText())).atTime(0, 0));
                            c.setCommission(Double.parseDouble(ctrl.tbComm.getText()) / 100);

                            boolean res = DatabaseCommunication.partner.registerContractPhantom2Error(c, Integer.parseInt(ctrl.dbCtrTime.getText()));

                            if (res) {
                                getInstance().showNotification(getInstance().stage, "Tạo mới hợp đồng thành công với ID là:\n" + c.getCID(), "bg-success");
                            } else {
                                getInstance().showNotification(getInstance().stage, "Lỗi bất định, tạo hợp đồng thất bại", "bg-success");
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran2() {
                    t2_gotoRegister();
                }
            }

            //===================
            public static class Fixed {
                public static String demoType = "FIXED";

                public static void t1_gotoHome() {
                    try {
                        getInstance().replaceSceneContent("employee.home.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        pck.dbms.fe.employee.HomeController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            Demo.gotoTranSelection(demoType, errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnPnMng.setDisable(true);

                        ctrl.btnCntrMng.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                            System.out.println("btnCntrMng clickled at " + getInstance().getClass());

                            t1_gotoListContract();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void t1_gotoListContract() {
                    try {
                        getInstance().replaceSceneContent("employee.listContract.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        ListContractController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();

                            t1_gotoHome();
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnAcceptAll.setDisable(false);
                        ctrl.btnAcceptAll.setOnAction(actionEvent -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                            int ctrNum = ctrl.contractHashMap.size();

                            alert.setTitle("Xác nhận");
                            alert.setHeaderText("Bạn có chắc muốn duyệt " + ctrNum + " hợp đồng");

                            Optional<ButtonType> option2 = alert.showAndWait();

                            if (option2.get() == ButtonType.OK) {
                                String accRes = DatabaseCommunication.employee.acceptAllContractPhantom2Fixed(ctrl.contractHashMap);
                                if (accRes.equals("success")) {
                                    t1_gotoListContract_noReset();
                                } else if (accRes.contains("phantom")) {
                                    getInstance().showNotification(getInstance().stage, accRes, "bg-warning");
                                } else {
                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định: " + accRes, "bg-danger");
                                }
                                //t1_gotoListContract_noReset();
                            }
                        });


                        // set up table view
                        Contract c = new Contract();

                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colCID.setCellValueFactory(new PropertyValueFactory<Contract, String>("CID"));
                        ctrl.colName.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerName"));
                        ctrl.colRepName.setCellValueFactory(new PropertyValueFactory<Contract, String>("repName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerAddress"));
                        ctrl.colStat.setCellValueFactory(new PropertyValueFactory<Contract, String>("status"));
                        Callback<TableColumn<Contract, String>, TableCell<Contract, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Contract, String> call(final TableColumn<Contract, String> param) {
                                return new TableCell<Contract, String>() {

                                    final Button btn = new Button("Chi tiết");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Contract contract = getTableView().getItems().get(getIndex());
                                                System.out.println("btn detail clicked");

                                                try {
                                                    Stage stageCntrDetails = new Stage();
                                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("employee.contractDetail.fxml"), null, new JavaFXBuilderFactory());
                                                    Parent root = loader.load();
                                                    stageCntrDetails.initOwner(getInstance().stage);
                                                    stageCntrDetails.setScene(new Scene(root));
                                                    stageCntrDetails.setTitle("Chi tiết hợp đồng");
                                                    stageCntrDetails.initModality(Modality.NONE);

                                                    stageCntrDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                                    stageCntrDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                                    stageCntrDetails.setResizable(false);
                                                    stageCntrDetails.setFullScreen(false);
                                                    stageCntrDetails.sizeToScene();

                                                    pck.dbms.fe.employee.ContractDetailController ctrl = loader.getController();
                                                    ctrl.contract = contract;

                                                    // set value
                                                    ctrl.lblPnName.setText(ctrl.contract.getPartnerName());
                                                    ctrl.lblPnAddr.setText(ctrl.contract.getPartner().getAddress().toString());
                                                    ctrl.lblPhone.setText(ctrl.contract.getPartner().getPhone());
                                                    ctrl.lblMail.setText(ctrl.contract.getPartner().getMail());
                                                    ctrl.lblBranchNum.setText(String.valueOf(ctrl.contract.getPartner().getBranchNumber()));
                                                    ctrl.lblOrdNum.setText(String.valueOf(ctrl.contract.getPartner().getOrderNumber()));
                                                    ctrl.lblProdType.setText(ctrl.contract.getPartner().getProductType());
                                                    ctrl.lblTIN.setText(ctrl.contract.getTIN());
                                                    ctrl.lblComm.setText(String.format("%.2f", ctrl.contract.getCommission() * 100) + " %");
                                                    ctrl.lblAccStat.setText(contract.getStatus().equals("ACCEPTED") ? "Đã duyệt" : "Chưa duyệt");
                                                    ctrl.lblAccStat.getStyleClass().add(contract.getStatus().equals("ACCEPTED") ? "text-success" : "text-danger");
                                                    ctrl.lblCA.setText(ctrl.contract.getCreatedAt().toString());
                                                    ctrl.lblEA.setText(ctrl.contract.getExpiredAt().toString());

                                                    ctrl.btnAcc.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAccEvent -> {
                                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                        alert.setTitle("Duyệt hợp đồng");
                                                        alert.setHeaderText("Bạn muốn duyệt hợp đồng này ?");

                                                        // option != null.
                                                        Optional<ButtonType> option = alert.showAndWait();

                                                        if (option.isPresent() && option.get() == ButtonType.OK) {
                                                            if (DatabaseCommunication.employee.acceptContractButFailDirtyRead2Error(ctrl.contract)) {
                                                                stageCntrDetails.close();
                                                                getInstance().showNotification(getInstance().stage, "Duyệt thành công hợp đồng " + ctrl.contract.getCID(), "bg-success");
                                                            } else {
                                                                getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                            }
                                                        }

                                                    });
                                                    ctrl.btnRej.setDisable(true);

                                                    stageCntrDetails.show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();

                                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định", "bg-danger");
                                                }
                                            });
                                            setGraphic(btn);
                                            btn.setDisable(true);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        //reset all contracts to pending
                        DatabaseCommunication.employee.resetToAllContractToPending();

                        // load contract
                        DatabaseCommunication.employee.getContracts("*", ctrl.contractHashMap);

                        // add contract to table view
                        ctrl.tableView.getItems().addAll(ctrl.contractHashMap.values());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void t1_gotoListContract_noReset() {
                    try {
                        getInstance().replaceSceneContent("employee.listContract.fxml");
                        App.loginInfo.setUsername("employee1");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        ListContractController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();

                            t1_gotoHome();
                        });
                        ctrl.imgVLogout.setVisible(false);
                        ctrl.btnAcceptAll.setDisable(false);
                        ctrl.btnAcceptAll.setOnAction(actionEvent -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                            int ctrNum = ctrl.contractHashMap.size();

                            alert.setTitle("Xác nhận");
                            alert.setHeaderText("Bạn có chắc muốn duyệt " + ctrNum + " hợp đồng");

                            Optional<ButtonType> option2 = alert.showAndWait();

                            if (option2.get() == ButtonType.OK) {
                                String accRes = DatabaseCommunication.employee.acceptAllContractPhantom2Fixed(ctrl.contractHashMap);
                                if (accRes.equals("success")) {
                                    getInstance().showNotification(getInstance().stage, "Đã duyệt " + ctrl.contractHashMap.size() + " hợp đồng", "bg-success");
                                } else if (accRes.contains("phantom")) {
                                    getInstance().showNotification(getInstance().stage, accRes, "bg-warning");
                                } else {
                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định: " + accRes, "bg-danger");
                                }
                            }
                        });


                        // set up table view
                        Contract c = new Contract();

                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colCID.setCellValueFactory(new PropertyValueFactory<Contract, String>("CID"));
                        ctrl.colName.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerName"));
                        ctrl.colRepName.setCellValueFactory(new PropertyValueFactory<Contract, String>("repName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Contract, String>("partnerAddress"));
                        ctrl.colStat.setCellValueFactory(new PropertyValueFactory<Contract, String>("status"));
                        Callback<TableColumn<Contract, String>, TableCell<Contract, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell<Contract, String> call(final TableColumn<Contract, String> param) {
                                return new TableCell<Contract, String>() {

                                    final Button btn = new Button("Chi tiết");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Contract contract = getTableView().getItems().get(getIndex());
                                                System.out.println("btn detail clicked");

                                                try {
                                                    Stage stageCntrDetails = new Stage();
                                                    FXMLLoader loader = new FXMLLoader(App.class.getResource("employee.contractDetail.fxml"), null, new JavaFXBuilderFactory());
                                                    Parent root = loader.load();
                                                    stageCntrDetails.initOwner(getInstance().stage);
                                                    stageCntrDetails.setScene(new Scene(root));
                                                    stageCntrDetails.setTitle("Chi tiết hợp đồng");
                                                    stageCntrDetails.initModality(Modality.NONE);

                                                    stageCntrDetails.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                                                    stageCntrDetails.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                                                    stageCntrDetails.setResizable(false);
                                                    stageCntrDetails.setFullScreen(false);
                                                    stageCntrDetails.sizeToScene();

                                                    pck.dbms.fe.employee.ContractDetailController ctrl = loader.getController();
                                                    ctrl.contract = contract;

                                                    // set value
                                                    ctrl.lblPnName.setText(ctrl.contract.getPartnerName());
                                                    ctrl.lblPnAddr.setText(ctrl.contract.getPartner().getAddress().toString());
                                                    ctrl.lblPhone.setText(ctrl.contract.getPartner().getPhone());
                                                    ctrl.lblMail.setText(ctrl.contract.getPartner().getMail());
                                                    ctrl.lblBranchNum.setText(String.valueOf(ctrl.contract.getPartner().getBranchNumber()));
                                                    ctrl.lblOrdNum.setText(String.valueOf(ctrl.contract.getPartner().getOrderNumber()));
                                                    ctrl.lblProdType.setText(ctrl.contract.getPartner().getProductType());
                                                    ctrl.lblTIN.setText(ctrl.contract.getTIN());
                                                    ctrl.lblComm.setText(String.format("%.2f", ctrl.contract.getCommission() * 100) + " %");
                                                    ctrl.lblAccStat.setText(contract.getStatus().equals("ACCEPTED") ? "Đã duyệt" : "Chưa duyệt");
                                                    ctrl.lblAccStat.getStyleClass().add(contract.getStatus().equals("ACCEPTED") ? "text-success" : "text-danger");
                                                    ctrl.lblCA.setText(ctrl.contract.getCreatedAt().toString());
                                                    ctrl.lblEA.setText(ctrl.contract.getExpiredAt().toString());

                                                    ctrl.btnAcc.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAccEvent -> {
                                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                        alert.setTitle("Duyệt hợp đồng");
                                                        alert.setHeaderText("Bạn muốn duyệt hợp đồng này ?");

                                                        // option != null.
                                                        Optional<ButtonType> option = alert.showAndWait();

                                                        if (option.isPresent() && option.get() == ButtonType.OK) {
                                                            if (DatabaseCommunication.employee.acceptContractButFailDirtyRead2Error(ctrl.contract)) {
                                                                stageCntrDetails.close();
                                                                getInstance().showNotification(getInstance().stage, "Duyệt thành công hợp đồng " + ctrl.contract.getCID(), "bg-success");
                                                            } else {
                                                                getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                            }
                                                        }

                                                    });
                                                    ctrl.btnRej.setDisable(true);

                                                    stageCntrDetails.show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();

                                                    getInstance().showNotification(getInstance().stage, "Có lỗi bất định", "bg-danger");
                                                }
                                            });
                                            setGraphic(btn);
                                            btn.setDisable(true);
                                            setText(null);
                                        }
                                    }
                                };
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        // load contract
                        DatabaseCommunication.employee.getContracts("*", ctrl.contractHashMap);

                        // add contract to table view
                        ctrl.tableView.getItems().addAll(ctrl.contractHashMap.values());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran1() {
                    t1_gotoHome();
                }

                public static void t2_gotoRegister() {
                    try {
                        getInstance().replaceSceneContent("partner.registerContract.fxml", 400, 600);
                        App.loginInfo.setUsername("partner_food");
                        App.loginInfo.setRole(ROLE.EMPLOYEE);
                        Container.partner.setLogin(App.loginInfo);

                        RegisterContractController ctrl = getInstance().loader.getController();

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn back pressed in " + getInstance().getClass());
                            event.consume();

                            App.Demo.gotoTranSelection(demoType, errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);

                        ctrl.lblPnName.setText(ctrl.partner.getName());
                        ctrl.lblPnAddr.setText(ctrl.partner.getAddress().toString());
                        ctrl.lblPhone.setText(ctrl.partner.getPhone());
                        ctrl.lblMail.setText(ctrl.partner.getMail());
                        ctrl.lblBranchNum.setText(String.valueOf(ctrl.partner.getBranchNumber()));
                        ctrl.lblOrdNum.setText(String.valueOf(ctrl.partner.getOrderNumber()));
                        ctrl.lblProdType.setText(ctrl.partner.getProductType());

                        int leftLimit = '0'; // letter 'a'
                        int rightLimit = '9'; // letter 'z'
                        int targetStringLength = 20;
                        Random random = new Random();

                        String TIN = random.ints(leftLimit, rightLimit + 1)
                                .limit(targetStringLength)
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                .toString();
                        ctrl.tbTIN.setText(TIN);
                        ctrl.dpStart.setValue(LocalDate.now());
                        ctrl.dbCtrTime.setText("6");
                        ctrl.tbComm.setText("10");

                        ctrl.btnRegister.setOnAction((ae) -> {
                            Contract c = new Contract();

                            String CID = random.ints('a', 'z' + 1)
                                    .limit(targetStringLength)
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString();

                            c.setCID(CID);
                            c.setPartner(ctrl.partner);
                            c.setTIN(TIN);
                            c.setCreatedAt(ctrl.dpStart.getValue().atTime(0, 0));
                            c.setExpiredAt(ctrl.dpStart.getValue().plusMonths(Integer.parseInt(ctrl.dbCtrTime.getText())).atTime(0, 0));
                            c.setCommission(Double.parseDouble(ctrl.tbComm.getText()) / 100);

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                            alert.setTitle("Tạo hợp đồng");
                            alert.setHeaderText("Bạn có chắc muốn tạo hợp đồng");

                            Optional<ButtonType> option2 = alert.showAndWait();

                            if (option2.get() == ButtonType.OK) {
                                boolean res = DatabaseCommunication.partner.registerContractPhantom2Fixed(c, Integer.parseInt(ctrl.dbCtrTime.getText()));

                                if (res) {
                                    getInstance().showNotification(getInstance().stage, "Tạo mới hợp đồng thành công với ID là:\n" + c.getCID(), "bg-success");
                                } else {
                                    getInstance().showNotification(getInstance().stage, "Lỗi bất định, tạo hợp đồng thất bại", "bg-success");
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran2() {
                    t2_gotoRegister();
                }
            }
        }

        public static class NonRepeatableRead2 {
            public static String errType = errorTypes.get(ERROR_TYPE.NON_REPEATABLE_READ);
            public static int errNo = 2;

            public static class Error {
                public static String demoType = "ERROR";

                public static void gotoReceiveOrder() {
                    try {
                        getInstance().replaceSceneContent("driver.receiveOrders.fxml");

                        pck.dbms.fe.driver.GetOrdersController ctrl = getInstance().loader.getController();

                        ctrl.driver.getLogin().setUsername("phatnm.driver1");

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            gotoTranSelection(demoType, errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);

                        // get order list from DB
                        DatabaseCommunication.driver.getOrdersInActiveArea(ctrl.driver, ctrl.orderHashMap);

                        //setting for value factory
                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
                        ctrl.colCusName.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Order, String>("customerAddress"));
                        ctrl.colPrice.setCellValueFactory(new PropertyValueFactory<Order, String>("total"));
                        ctrl.colPaymentMethod.setCellValueFactory(new PropertyValueFactory<Order, String>("paymentMethod"));
                        ctrl.colPaidSta.setCellValueFactory(new PropertyValueFactory<Order, String>("paidStatus"));
                        Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell call(final TableColumn<Order, String> param) {
                                final TableCell<Order, String> cell = new TableCell<Order, String>() {

                                    final Button btn = new Button("Tiếp nhận");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Order order = getTableView().getItems().get(getIndex());

                                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                alert.setTitle("Tiếp nhận đơn hàng");
                                                alert.setHeaderText("Bạn muốn tiếp nhận đơn hàng " + order.getOrderID() + " " + order.getPaymentMethod());

                                                // option != null.
                                                Optional<ButtonType> option = alert.showAndWait();

                                                if (option.isPresent() && option.get() == ButtonType.OK) {
                                                    // todo: driver receive order
                                                    String res = DatabaseCommunication.driver.receiveOrderNRR2Error(ctrl.driver, order, new Order());
                                                    if (res.equals("success")) {
                                                        getInstance().showNotification(getInstance().stage, "Thành công", "bg-success");
                                                    } else if (res.contains("nrr")) {
                                                        String[] token = res.split(";");
                                                        String msg = "Có lỗi non-repeatable read\n" + token[1];
                                                        getInstance().showNotification(getInstance().stage, msg, "bg-warning");
                                                    } else {
                                                        getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                    }
                                                }
                                            });
                                            setGraphic(btn);
                                        }
                                        setText(null);
                                    }
                                };
                                return cell;
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        // add order to table view
                        for (String key : ctrl.orderHashMap.keySet()) {
                            ctrl.tableView.getItems().add(ctrl.orderHashMap.get(key));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran1() {
                    gotoReceiveOrder();
                }

                public static void gotoCusOrders() {
                    try {
                        getInstance().replaceSceneContent("customer.ordersTracking.fxml");

                        pck.dbms.fe.customer.ordersTracking.Controller ctrl = getInstance().loader.getController();

                        ctrl.customer.getLogin().setUsername("customer1");

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            gotoTranSelection(demoType, errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);

                        // get order list from DB
                        DatabaseCommunication.customer.getOrders(ctrl.customer, ctrl.hm);


                        //setting for value factory
                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
                        ctrl.colCusName.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Order, String>("customerAddress"));
                        ctrl.colPrice.setCellValueFactory(new PropertyValueFactory<Order, String>("total"));

                        Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactoryPM = new Callback<>() {
                            @Override
                            public TableCell<Order, String> call(final TableColumn<Order, String> param) {

                                return new TableCell<>() {

                                    final ComboBox<String> pmCb = new ComboBox<>();

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);

                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            Order order = getTableView().getItems().get(getIndex());
                                            pmCb.setPromptText(order.getPaymentMethod());
                                            if (item != null) {
                                                setText(order.getPaymentMethod());
                                                //System.out.println(getText());
                                            } else System.out.println("đã có text: " + getText());
                                            pmCb.getItems().clear();
                                            pmCb.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                                            pmCb.setOnAction((eCBPM) -> {
                                                Object selectedItem = pmCb.getSelectionModel().getSelectedItem();
                                                ctrl.paymentMethod = String.valueOf(selectedItem);
                                                //order.setPaymentMethod(ctrl.paymentMethod);
                                                if (ctrl.paymentMethod.equals("Tiền mặt"))
                                                    ctrl.paymentMethod = "CASH";
                                                this.setText(ctrl.paymentMethod);


                                            });

                                            setGraphic(pmCb);
                                        }

                                    }
                                };
                            }
                        };

                        ctrl.colPaymentMethod.setCellValueFactory(new PropertyValueFactory<Order, String>("paymentMethod"));
                        ctrl.colPaymentMethod.setCellFactory(cellFactoryPM);

                        //ctrl.colPaymentMethod.setComparator(Comparator.naturalOrder());
                        ctrl.colPaidSta.setCellValueFactory(new PropertyValueFactory<Order, String>("paidStatus"));
                        Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactoryBtn = new Callback<>() {
                            @Override
                            public TableCell call(final TableColumn<Order, String> param) {
                                final TableCell<Order, String> cell = new TableCell<>() {

                                    final Button btn = new Button("Cập nhật");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Order order = getTableView().getItems().get(getIndex());

                                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                alert.setTitle("Cập nhật đơn hàng");
                                                alert.setHeaderText("Bạn muốn cập nhật phương thức thanh toán\n" + order.getPaymentMethod() + " -> " + ctrl.paymentMethod);

                                                // option != null.
                                                Optional<ButtonType> option = alert.showAndWait();

                                                if (option.isPresent() && option.get() == ButtonType.OK) {
                                                    boolean res = DatabaseCommunication.customer.updateOrderPMNRR2Error(order, ctrl.paymentMethod);

                                                    if (res) {
                                                        getInstance().showNotification(getInstance().stage, "Thành công", "bg-success");
                                                    } else {
                                                        getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                    }
                                                }
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                                return cell;
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactoryBtn);
                        // add order to table view
                        System.out.println(ctrl.hm.size());
                        ctrl.tableView.getItems().addAll(ctrl.hm.values());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran2() {
                    gotoCusOrders();
                }
            }

            public static class Fixed {
                public static String demoType = "FIXED";

                public static void gotoReceiveOrder() {
                    try {
                        getInstance().replaceSceneContent("driver.receiveOrders.fxml");

                        pck.dbms.fe.driver.GetOrdersController ctrl = getInstance().loader.getController();

                        ctrl.driver.getLogin().setUsername("phatnm.driver1");

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            gotoTranSelection(demoType, errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);

                        // get order list from DB
                        DatabaseCommunication.driver.getOrdersInActiveArea(ctrl.driver, ctrl.orderHashMap);

                        //setting for value factory
                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
                        ctrl.colCusName.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Order, String>("customerAddress"));
                        ctrl.colPrice.setCellValueFactory(new PropertyValueFactory<Order, String>("total"));
                        ctrl.colPaymentMethod.setCellValueFactory(new PropertyValueFactory<Order, String>("paymentMethod"));
                        ctrl.colPaidSta.setCellValueFactory(new PropertyValueFactory<Order, String>("paidStatus"));
                        Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactory = new Callback<>() {
                            @Override
                            public TableCell call(final TableColumn<Order, String> param) {
                                final TableCell<Order, String> cell = new TableCell<Order, String>() {

                                    final Button btn = new Button("Tiếp nhận");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Order order = getTableView().getItems().get(getIndex());

                                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                alert.setTitle("Tiếp nhận đơn hàng");
                                                alert.setHeaderText("Bạn muốn tiếp nhận đơn hàng " + order.getOrderID() + " " + order.getPaymentMethod());

                                                // option != null.
                                                Optional<ButtonType> option = alert.showAndWait();

                                                if (option.isPresent() && option.get() == ButtonType.OK) {
                                                    // todo: driver receive order
                                                    String res = DatabaseCommunication.driver.receiveOrderNRR2Fixed(ctrl.driver, order, new Order());
                                                    if (res.equals("success")) {
                                                        getInstance().showNotification(getInstance().stage, "Thành công", "bg-success");
                                                    } else if (res.contains("nrr")) {
                                                        String[] token = res.split(";");
                                                        String msg = "Có lỗi non-repeatable read\n" + token[1];
                                                        getInstance().showNotification(getInstance().stage, msg, "bg-warning");
                                                    } else {
                                                        getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                    }
                                                }
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                                return cell;
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactory);

                        // add order to table view
                        for (String key : ctrl.orderHashMap.keySet()) {
                            ctrl.tableView.getItems().add(ctrl.orderHashMap.get(key));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran1() {
                    gotoReceiveOrder();
                }

                public static void gotoCusOrders() {
                    try {
                        getInstance().replaceSceneContent("customer.ordersTracking.fxml");

                        pck.dbms.fe.customer.ordersTracking.Controller ctrl = getInstance().loader.getController();

                        ctrl.customer.getLogin().setUsername("customer1");

                        ctrl.imgVBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            System.out.println("Btn logout pressed in " + getInstance().getClass());
                            event.consume();

                            gotoTranSelection(demoType, errType, errNo);
                        });
                        ctrl.imgVLogout.setVisible(false);

                        // get order list from DB
                        DatabaseCommunication.customer.getOrders(ctrl.customer, ctrl.hm);


                        //setting for value factory
                        ctrl.colNO.setCellFactory(new LineNumbersCellFactory<>());
                        ctrl.colID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
                        ctrl.colCusName.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
                        ctrl.colAddr.setCellValueFactory(new PropertyValueFactory<Order, String>("customerAddress"));
                        ctrl.colPrice.setCellValueFactory(new PropertyValueFactory<Order, String>("total"));

                        Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactoryPM = new Callback<>() {
                            @Override
                            public TableCell<Order, String> call(final TableColumn<Order, String> param) {

                                return new TableCell<>() {

                                    final ComboBox<String> pmCb = new ComboBox<>();

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);

                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            Order order = getTableView().getItems().get(getIndex());
                                            pmCb.setPromptText(order.getPaymentMethod());
                                            if (item != null) {
                                                setText(order.getPaymentMethod());
                                                //System.out.println(getText());
                                            } else System.out.println("đã có text: " + getText());
                                            pmCb.getItems().clear();
                                            pmCb.getItems().addAll("Tiền mặt", "ZALOPAY", "MOMO");
                                            pmCb.setOnAction((eCBPM) -> {
                                                Object selectedItem = pmCb.getSelectionModel().getSelectedItem();
                                                ctrl.paymentMethod = String.valueOf(selectedItem);
                                                //order.setPaymentMethod(ctrl.paymentMethod);
                                                if (ctrl.paymentMethod.equals("Tiền mặt"))
                                                    ctrl.paymentMethod = "CASH";
                                                this.setText(ctrl.paymentMethod);


                                            });

                                            setGraphic(pmCb);
                                        }

                                    }
                                };
                            }
                        };

                        ctrl.colPaymentMethod.setCellValueFactory(new PropertyValueFactory<Order, String>("paymentMethod"));
                        ctrl.colPaymentMethod.setCellFactory(cellFactoryPM);

                        //ctrl.colPaymentMethod.setComparator(Comparator.naturalOrder());
                        ctrl.colPaidSta.setCellValueFactory(new PropertyValueFactory<Order, String>("paidStatus"));
                        Callback<TableColumn<Order, String>, TableCell<Order, String>> cellFactoryBtn = new Callback<>() {
                            @Override
                            public TableCell call(final TableColumn<Order, String> param) {
                                final TableCell<Order, String> cell = new TableCell<>() {

                                    final Button btn = new Button("Cập nhật");

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            btn.setOnAction(event -> {
                                                Order order = getTableView().getItems().get(getIndex());

                                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                alert.setTitle("Cập nhật đơn hàng");
                                                alert.setHeaderText("Bạn muốn cập nhật phương thức thanh toán\n" + order.getPaymentMethod() + " -> " + ctrl.paymentMethod);

                                                // option != null.
                                                Optional<ButtonType> option = alert.showAndWait();

                                                if (option.isPresent() && option.get() == ButtonType.OK) {
                                                    boolean res = DatabaseCommunication.customer.updateOrderPMNRR2Fixed(order, ctrl.paymentMethod);

                                                    if (res) {
                                                        getInstance().showNotification(getInstance().stage, "Thành công", "bg-success");
                                                    } else {
                                                        getInstance().showNotification(getInstance().stage, "Có lỗi xảy ra, duyệt thất bại", "bg-danger");
                                                    }
                                                }
                                            });
                                            setGraphic(btn);
                                            setText(null);
                                        }
                                    }
                                };
                                return cell;
                            }
                        };

                        ctrl.colBtn.setCellFactory(cellFactoryBtn);
                        // add order to table view
                        System.out.println(ctrl.hm.size());
                        ctrl.tableView.getItems().addAll(ctrl.hm.values());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public static void gotoTran2() {
                    gotoCusOrders();
                }
            }
        }
    }
}