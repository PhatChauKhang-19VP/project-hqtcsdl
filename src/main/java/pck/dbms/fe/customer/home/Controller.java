package pck.dbms.fe.customer.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import pck.dbms.fe.App;

public class Controller {

    public Button btnPlaceOrder;
    public Button btnTrackOrder;
    public ImageView imgVLogout;

    public Button getBtnPlaceOrder() {
        return btnPlaceOrder;
    }

    public void setBtnPlaceOrder(Button btnPlaceOrder) {
        this.btnPlaceOrder = btnPlaceOrder;
    }

    public Button getBtnTrackOrder() {
        return btnTrackOrder;
    }

    public void setBtnTrackOrder(Button btnTrackOrder) {
        this.btnTrackOrder = btnTrackOrder;
    }

    public ImageView getImgVLogout() {
        return imgVLogout;
    }

    public void setImgVLogout(ImageView imgVLogout) {
        this.imgVLogout = imgVLogout;
    }

    public void onBtnPlaceOrderClick(ActionEvent ae){
        if (ae.getSource() == btnPlaceOrder){
            System.out.println("BtnPlaceOrder clicked at" + getClass());

            App.customer.gotoListPartner();
        }
    }

    public void onBtnTrackOrderClick(ActionEvent ae){
        if (ae.getSource() == btnTrackOrder){
            System.out.println("BtnPlaceOrder clicked at" + getClass());

            // todo:
        }
    }
}
