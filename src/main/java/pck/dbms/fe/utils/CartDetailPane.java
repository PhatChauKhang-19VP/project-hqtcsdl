package pck.dbms.fe.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import pck.dbms.be.cart.CartDetail;

public class CartDetailPane {
    private static final String iconMinusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646897/HQTCSDL/Icon/minus-button_ukufql.png";
    private static final String iconPlusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646899/HQTCSDL/Icon/plus_arj0go.png";
    public CartDetail cartDetail;
    public Pane pane;

    public CartDetailPane(CartDetail cartDetail) {
        this.cartDetail = cartDetail;

        pane = new Pane();
        ImageView imgProduct = new ImageView(cartDetail.product.getImgSrc());
        Label labelProductName = new Label(String.format("Tên chi nhánh %s", cartDetail.product.getName()));
        Label labelPBName = new Label(cartDetail.partnerBranch.getName());
        Label labelProductPrice = new Label(String.format("Đon giá: %.2f VNĐ", cartDetail.product.getPrice()));
        Label labelProductQuantity = new Label(String.format("Số lượng: %d", cartDetail.quantity));
        Label labelSubTotal = new Label(String.format("Tổng cộng: %.2f VNĐ", cartDetail.subTotal));
        pane.getChildren().addAll(imgProduct, labelProductName, labelPBName, labelProductPrice, labelProductQuantity, labelSubTotal);

        // pane
        pane.setMinSize(380, 150);
        pane.setPrefSize(380, 150);

        // pane > imgProduct
        imgProduct.setFitWidth(200);
        imgProduct.setFitHeight(150);
        imgProduct.setLayoutX(0);
        imgProduct.setLayoutY(0);

        // pane > labelProductName
        labelProductName.setTextAlignment(TextAlignment.LEFT);
        labelProductName.setAlignment(Pos.CENTER_LEFT);
        labelProductName.setStyle("-fx-font-family: Arial;-fx-font-size:14;-fx-text-fill: #132ac1;");
        labelProductName.setWrapText(true);
        labelProductName.setPrefSize(160, 50);
        labelProductName.setLayoutX(210);
        labelProductName.setLayoutY(5);

        // pane > labelProductName
        labelPBName.setTextAlignment(TextAlignment.LEFT);
        labelPBName.setAlignment(Pos.CENTER_LEFT);
        labelPBName.setStyle("-fx-font-family: Arial;-fx-font-size:12;-fx-text-fill: #000; -fx-font-style: italic");
        labelPBName.setWrapText(true);
        labelPBName.setPrefSize(160, 20);
        labelPBName.setLayoutX(210);
        labelPBName.setLayoutY(60);


        // pane > labelProductPrice
        labelProductPrice.setTextAlignment(TextAlignment.LEFT);
        labelProductPrice.setAlignment(Pos.CENTER_LEFT);
        labelProductPrice.setStyle("-fx-font-family: Arial;-fx-font-weight: 900");
        labelProductPrice.setPrefSize(160, 20);
        labelProductPrice.setLayoutX(210);
        labelProductPrice.setLayoutY(85);

        // pane > labelProductQuantity
        labelProductQuantity.setTextAlignment(TextAlignment.LEFT);
        labelProductQuantity.setAlignment(Pos.CENTER_LEFT);
        labelProductQuantity.setStyle("-fx-font-family: Arial;-fx-font-weight: 900");
        labelProductQuantity.setPrefSize(160, 20);
        labelProductQuantity.setLayoutX(210);
        labelProductQuantity.setLayoutY(110);

        // pane > labelSubTotal
        labelSubTotal.setTextAlignment(TextAlignment.LEFT);
        labelSubTotal.setAlignment(Pos.CENTER_LEFT);
        labelSubTotal.setStyle("-fx-font-family: Arial;-fx-font-weight: 900;-fx-text-fill: #b0280c;");
        labelSubTotal.setPrefSize(160, 20);
        labelSubTotal.setLayoutX(210);
        labelSubTotal.setLayoutY(130);
    }

    public Pane get() {
        return pane;
    }
}
