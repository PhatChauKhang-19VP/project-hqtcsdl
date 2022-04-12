package pck.dbms.fe.welcome;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pck.dbms.fe.App;

public class Controller {
    public Button btnSignIn,
            btnSignUp;

    public void onBtnSignInClick(ActionEvent ae) {
        if (ae.getSource() == btnSignIn) {
            System.out.println("Btn Sign-In Clicked");
            App.getInstance().gotoSignIn();
        }
    }

    public void onBtnSignUpClick(ActionEvent ae) {
        if (ae.getSource() == btnSignUp) {
            System.out.println("Btn Sign-up click");
            // todo App.getInstance().gotoSignUp();
            new animatefx.animation.ZoomOut(btnSignUp).play();
        }
    }
}
