package pck.dbms.fe.demo;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import pck.dbms.fe.App;

public class ErrSelectionController {

    public ImageView imgVBack;
    public ImageView imgVLogout;
    public ComboBox<String> cbBox;
    public int tranNO;
    public Label lblPrim;
    public ComboBox<String> cbBoxNO;


    public String currErrTypeSelected = "";
    public int currErrNoSelected = 1;
    public boolean isErrTypeSelected = false, isErrNoSelected = false;
    public Button btnSelect;

    public void onBtnSelectClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnSelect) {
            System.out.println(getClass() + ": btn select clicked");

            App.Demo.gotoTypeSelection(currErrTypeSelected, currErrNoSelected);
        }
    }
}
