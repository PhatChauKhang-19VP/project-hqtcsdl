package pck.dbms.fe.driver;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import pck.dbms.be.driver.Driver;
import pck.dbms.be.order.Order;

import java.util.HashMap;

public class GetOrdersController {

    public Driver driver = new Driver();
    public HashMap<String, Order> orderHashMap = new HashMap<>();

    public TableView tableView;
    public TableColumn colNO;
    public TableColumn colID;
    public TableColumn colCusName;
    public TableColumn colAddr;
    public TableColumn colPrice;
    public TableColumn colPaymentMethod;
    public TableColumn colPaidSta;
    public TableColumn colBtn;
    public ImageView imgVBack;
    public ImageView imgVLogout;
}
