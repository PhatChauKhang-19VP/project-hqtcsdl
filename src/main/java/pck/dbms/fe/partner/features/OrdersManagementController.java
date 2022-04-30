package pck.dbms.fe.partner.features;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import pck.dbms.be.order.Order;
import pck.dbms.be.partner.Partner;

import java.util.HashMap;

public class OrdersManagementController {
    public Partner partner = new Partner();
    public HashMap<String, Order> orderHashMap = new HashMap<>();

    public TableView tableView;
    public TableColumn colNO;
    public TableColumn colID;
    public TableColumn colCusName;
    public TableColumn colAddr;
    public TableColumn colPrice;
    public TableColumn colDiSta;
    public TableColumn colPaidSta;
    public TableColumn colBtn;
    public ImageView imgVBack;
    public ImageView imgVLogout;
}
