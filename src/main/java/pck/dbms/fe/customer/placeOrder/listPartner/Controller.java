package pck.dbms.fe.customer.placeOrder.listPartner;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;

import java.util.HashMap;

public class Controller {
    public ImageView imgVBack;
    public ImageView imgVLogout;
    public TableView<Partner> tableViewPartner;
    public TableColumn colNO;
    public TableColumn colName;
    public TableColumn colType;
    public TableColumn colRepName;
    public TableColumn colPhone;
    public TableColumn colAddr;
    public TableColumn colMail;
    public TableColumn colBtn;

    public HashMap<String, Partner> partners = new HashMap<>();
    public HashMap<String, PartnerBranch> partnerBranches = new HashMap<>();
    public HashMap<String, Product> products = new HashMap<>();
}
