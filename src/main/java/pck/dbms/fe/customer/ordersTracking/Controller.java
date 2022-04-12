package pck.dbms.fe.customer.ordersTracking;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class Controller {
    @FXML
    public TableView tableviewOrders;

    public TableView getTableviewOrders() {
        return tableviewOrders;
    }
}
