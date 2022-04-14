package pck.dbms.fe.customer.placeOrder.listProduct.cartDetail;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import pck.dbms.be.cart.Cart;
import pck.dbms.be.order.Order;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;
import pck.dbms.database.DatabaseCommunication;
import pck.dbms.fe.App;

import java.util.HashMap;

public class Controller {
    public GridPane gridPaneCartDetails;
    public ComboBox comboBoxPaymentMethod;
    public Button btnPlaceOrder;
    public String paymentMethod;
    public Cart cart;
    public Label labelTotal;

    public Partner partner = new Partner();
    public HashMap<String, PartnerBranch> partnerBranches = new HashMap<>();
    public HashMap<String, Product> products = new HashMap<>();

    public void onBtnPlaceOrderClick(){

    }
}
