package pck.dbms.fe.employee;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import pck.dbms.be.partner.Contract;

import java.util.HashMap;

public class ListContractController {
    public ImageView imgVBack;
    public ImageView imgVLogout;
    public TableView<Contract> tableView;
    public TableColumn colNO;
    public TableColumn colCID;
    public TableColumn colName;
    public TableColumn colRepName;
    public TableColumn colAddr;
    public TableColumn colBtn;
    public TableColumn colStat;
    public Button btnAcceptAll;

    public HashMap<String, Contract> contractHashMap = new HashMap<>();
}
