package pck.dbms.fe.partner.home;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import pck.dbms.be.partner.Partner;

public class Controller {
    public ImageView imgVBack;
    public ImageView imgVLogout;

    public Partner partner;

    public Button btnProd;
    public Button btnOrd;
    public Button btnCnt;

    public void onBtnCntClick(ActionEvent ae) {
        if (ae.getSource() == btnCnt) {
            System.out.println(getClass() + "btnCnt clicked");
        }
    }

    public void onBtnProdClick(ActionEvent ae) {
        if (ae.getSource() == btnProd) {
            System.out.println(getClass() + "btnProd clicked");
        }
    }

    public void onBtnOrdClick(ActionEvent ae) {
        if (ae.getSource() == btnOrd) {
            System.out.println(getClass() + "btnOrd clicked");
        }
    }
}
