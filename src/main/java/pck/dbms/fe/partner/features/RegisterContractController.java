package pck.dbms.fe.partner.features;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import pck.dbms.be.administrativeDivision.Address;
import pck.dbms.be.administrativeDivision.AdministrativeDivision;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.user.LoginInfo;
import pck.dbms.be.user.ROLE;

public class RegisterContractController {
    public ImageView imgVBack;
    public ImageView imgVLogout;
    public Label lblPnName;
    public Label lblPnAddr;
    public Label lblPhone;
    public Label lblMail;
    public Label lblBranchNum;
    public Label lblOrdNum;
    public Label lblProdType;
    public Button btnRegister;
    public TextField tbTIN;
    public DatePicker dpStart;
    public TextField dbCtrTime;
    public TextField tbComm;

    public Partner partner = new Partner(
            new LoginInfo("partner_food", ROLE.PARTNER),
            "Thực phẩm sạch",
            "Ngô Minh Phát",
            "FOOD",
            "0704921213",
            "partner@food.com",
            new Address(AdministrativeDivision.getInstance().getProvinceList().get("01"), AdministrativeDivision.getInstance().getDistrictList().get("001"), AdministrativeDivision.getInstance().getWardList().get("00001"), "123 Đồng Khởi"),
            2,
            100
    );
}
