package pck.dbms.fe.partner.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pck.dbms.fe.App;

public class Controller {
    public Button btnManageProduct;
    public Button btnManageOrder;
    public Button btnContract;

    public void onBtnContractClick(ActionEvent e) {
        if (e.getSource() == btnContract) {
            System.out.println("btn contract clicked");
            App.getInstance().gotoPartnerContract();
        }
    }

    public void onBtnManageOrderClick(ActionEvent ae) {
        if (ae.getSource() == btnManageOrder) {
            System.out.println("btn order clicked");
        }
    }

    public void onBtnManageProductClick(ActionEvent ae) {
        if (ae.getSource() == btnManageProduct) {
            System.out.println("Btn product clicked");
        }
    }
}
