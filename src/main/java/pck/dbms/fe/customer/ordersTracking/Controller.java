package pck.dbms.fe.customer.ordersTracking;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import pck.dbms.be.customer.Customer;
import pck.dbms.be.order.Order;

import java.util.HashMap;

public class Controller {
    @FXML
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

    public Customer customer = new Customer();
    public HashMap<String, Order> hm = new HashMap<>();
    public String paymentMethod;
}
