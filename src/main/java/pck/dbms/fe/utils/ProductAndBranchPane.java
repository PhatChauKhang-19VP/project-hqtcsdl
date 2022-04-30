package pck.dbms.fe.utils;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import pck.dbms.be.Container;
import pck.dbms.be.cart.Cart;
import pck.dbms.be.cart.CartDetail;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;
import pck.dbms.database.DatabaseCommunication;

public class ProductAndBranchPane {
    private static final String iconMinusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646897/HQTCSDL/Icon/minus-button_ukufql.png";
    private static final String iconPlusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646899/HQTCSDL/Icon/plus_arj0go.png";
    Pane pane;
    Product product;
    private PartnerBranch branchSelected;

    public ProductAndBranchPane(Product p) {
        this.product = p;

        pane = new Pane();
        pane.setMinSize(195, 315);
        pane.setPrefSize(195, 315);

        TextField tq = new TextField();

        //img view
        ImageView productImg = new ImageView(new Image(product.getImgSrc()));
        productImg.setFitWidth(180);
        productImg.setFitHeight(135);
        productImg.setLayoutX(10);
        productImg.setLayoutY(10);
        pane.getChildren().add(productImg);


        // label pb addr
        Label pbAddr = new Label("");
        pbAddr.setTextAlignment(TextAlignment.CENTER);
        pbAddr.setStyle("-fx-font-family: Arial;-fx-font-size:12;-fx-text-fill: #000;-fx-font-style: italic");
        pbAddr.setPrefSize(200, 20);
        pbAddr.setLayoutY(170);
        pbAddr.setAlignment(Pos.CENTER);
        pane.getChildren().add(pbAddr);


        // label stock
        Label stock = new Label("");
        stock.setTextAlignment(TextAlignment.CENTER);
        stock.setStyle("-fx-font-family: Arial;-fx-font-size:12;-fx-text-fill: #000;-fx-font-weight: 900");
        stock.setPrefSize(200, 20);
        stock.setLayoutY(190);
        stock.setAlignment(Pos.CENTER);
        pane.getChildren().add(stock);


        // label pro name
        Label productName = new Label(product.getName());
        productName.setTextAlignment(TextAlignment.CENTER);
        productName.setStyle("-fx-font-family: Arial;-fx-font-size:14;-fx-text-fill: #132ac1;");
        productName.setPrefSize(200, 20);
        productName.setLayoutY(210);
        productName.setAlignment(Pos.CENTER);
        pane.getChildren().add(productName);


        // label price
        Label productPrice = new Label(product.getPrice() + " VNĐ");
        productPrice.setTextAlignment(TextAlignment.CENTER);
        productPrice.setAlignment(Pos.CENTER);
        productPrice.setStyle("-fx-font-family: Arial;");
        productPrice.setPrefSize(200, 20);
        productPrice.setLayoutY(230);
        pane.getChildren().add(productPrice);


        // pane > btnAddProd
        Button btnAddProd = new Button("Thêm sản phẩm");
        btnAddProd.setAlignment(Pos.CENTER);
        btnAddProd.setPrefSize(120, 25);
        btnAddProd.setLayoutX(40);
        btnAddProd.setLayoutY(280);
        btnAddProd.setDisable(true);
        pane.getChildren().add(btnAddProd);


        btnAddProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + " btn add prod clicked");

                Cart cart = new Cart();
                cart.partner = branchSelected.getPartner();
                cart.customer = Container.customer;

                CartDetail cartDetail = new CartDetail();
                cartDetail.cart = cart;
                cartDetail.product = product;
                cartDetail.partnerBranch = branchSelected;
                cartDetail.quantity = Integer.parseInt(tq.getText());

                DatabaseCommunication
                        .customer
                        .addProductToCart(cartDetail);
            }
        });

        // pane quantity = pq
        Pane paneQuantity = new Pane();
        paneQuantity.setPrefSize(200, 30);
        paneQuantity.setLayoutY(250);
        pane.getChildren().add(paneQuantity);

        // pq > text quantity = tq
        tq.setPrefSize(50, 25);
        tq.setAlignment(Pos.CENTER);
        tq.setLayoutX(75);
        tq.setLayoutY(2);
        tq.setText("0");
        paneQuantity.getChildren().add(tq);
        tq.textProperty().addListener((observable, oldValue, newValue) -> {
            if (btnAddProd.isDisable()) {
                tq.setText("0");
                return;
            }
            if (newValue.equals("")) {
                tq.setText("0");
                return;
            }
            if (!newValue.matches("\\d*")) {
                tq.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (Integer.parseInt(newValue) > Integer.parseInt(stock.getText().replaceAll("\\D+", ""))) {
                tq.setText(stock.getText().replaceAll("\\D+", ""));
            }
        });

        // pq > iMinus
        ImageView iMinus = new ImageView(new Image(iconMinusURL));
        iMinus.setFitWidth(20);
        iMinus.setFitHeight(20);
        iMinus.setLayoutX(40);
        iMinus.setLayoutY(5);
        paneQuantity.getChildren().add(iMinus);

        iMinus.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (btnAddProd.isDisable()) {
                return;
            }

            // decrease quantity by 1
            String preQuantityStr = tq.getText();
            try {
                int quantity = Integer.parseInt(preQuantityStr);
                quantity -= 1;

                if (quantity >= 0) {
                    tq.setText(String.valueOf(quantity));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                tq.setText("0");
            }
        });

        // pq > iAdd
        ImageView iAdd = new ImageView(new Image(iconPlusURL));
        iAdd.setFitWidth(20);
        iAdd.setFitHeight(20);
        iAdd.setLayoutX(140);
        iAdd.setLayoutY(5);
        paneQuantity.getChildren().add(iAdd);

        iAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (btnAddProd.isDisable()) {
                return;
            }

            // increase quantity by 1
            String preQuantityStr = tq.getText();
            try {
                int quantity = Integer.parseInt(preQuantityStr);
                quantity += 1;

                if (quantity <= Integer.parseInt(stock.getText().replaceAll("\\D+", ""))) {
                    tq.setText(String.valueOf(quantity));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                tq.setText("0");
            }
        });

        ComboBox<String> comboBox = new ComboBox<>();

        for (String key : product.inBranches.keySet()) {
            comboBox.getItems().add(product.inBranches.get(key).getFirst().getName());
        }

        comboBox.setOnAction((event) -> {
            Object selectedItem = comboBox.getSelectionModel().getSelectedItem();

            for (String key : product.inBranches.keySet()) {
                if (selectedItem.toString().contains(product.inBranches.get(key).getFirst().getName())) {


                    pbAddr.setText(product.inBranches.get(key).getFirst().getAddress().getProvince().toString());
                    stock.setText("Số lượng: " + product.inBranches.get(key).getSecond().toString());
                    btnAddProd.setDisable(false);

                    branchSelected = product.inBranches.get(key).getFirst();

                    return;
                }
            }
        });

        comboBox.setPromptText("Hãy chọn chi nhánh");

        comboBox.setPrefSize(180, 20);
        comboBox.setLayoutX(10);
        comboBox.setLayoutY(150);
        pane.getChildren().add(comboBox);

    }

    public Pane get() {
        return pane;
    }
}
