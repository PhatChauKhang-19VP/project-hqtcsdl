package pck.dbms.fe.partner.features;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import pck.dbms.be.partner.Contract;
import pck.dbms.be.partner.Partner;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ContractsManagementController {

    public ImageView imgVBack;
    public ImageView imgVLogout;

    public Partner partner = new Partner();
    public HashMap<String, Contract> contractHashMap = new HashMap<>();
    public TableView<Contract> tableViewContractList;
    public TableColumn<Contract, String> colNO;
    public TableColumn<Contract, String> colCID;
    public TableColumn<Contract, String> colRepName;
    public TableColumn<Contract, LocalDateTime> colCA;
    public TableColumn<Contract, LocalDateTime> colEA;
    public TableColumn<Contract, Double> colComm;
    public TableColumn<Contract, String> colStat;
}
