package pck.dbms.fe.customer.home;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Controller {

    public Button btnPlaceOrder;
    public Button btnTrackOrder;
    public ImageView imgVLogout;
    public ImageView imgVBack;

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
    }

    public void onBtnTrackOrderClick(ActionEvent ae){
    }
}
