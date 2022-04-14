package pck.dbms.fe.customer.placeOrder.listProduct;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;

import java.util.HashMap;

public class Controller {
    public ImageView imgVBack;
    public ImageView imgVLogout;
    public ImageView imgVCart;
    public GridPane gridPaneProd;
    public Partner partner = new Partner();
    public HashMap<String, PartnerBranch> partnerBranches = new HashMap<>();
    public HashMap<String, Product> products = new HashMap<>();
}
