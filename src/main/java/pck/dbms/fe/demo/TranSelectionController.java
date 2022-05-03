package pck.dbms.fe.demo;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import pck.dbms.fe.App;

public class TranSelectionController {

    public String demoType = ""; // ERROR or FIXED
    public String errType = "";
    public int errNO = 0;

    public ImageView imgVBack;
    public ImageView imgVLogout;
    public Button btnTran1;
    public Button btnTran2;
    public Label liablePrim;

    public void onBtnTran1Click(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnTran1) {
            System.out.println(getClass() + ": btn tran1 clicked");

            System.out.println(errNO + " " + errType + " - Tran 1");
            if (demoType.equals("ERROR")) {
                switch (errType) {
                    case "Dirty read": {
                        System.out.println("DR");
                        if (errNO == 1) {
                            App.Demo.DirtyRead1.Error.gotoTran1();
                        } else {
                            App.Demo.DirtyRead2.Error.gotoTran1();
                        }

                        return;
                    }

                    case "Lost update": {
                        if (errNO == 1) {
                            App.Demo.LostUpdate1.Error.gotoTran1();
                        } else {
                            //App.Demo.LostUpdate2.Error.gotoTran1();
                        }

                        return;
                    }

                    case "Non-repeatable read": {
                        if (errNO == 1) {
                            //App.Demo.NonRepeatableRead1.Error.gotoTran1();
                        } else {
                            App.Demo.NonRepeatableRead2.Error.gotoTran1();
                        }
                        return;
                    }

                    case "Phantom": {
                        if (errNO == 1) {
                            //App.Demo.Phantom2.Error.gotoTran1();
                        } else {
                            App.Demo.Phantom2.Error.gotoTran1();
                        }
                        return;
                    }

                    case "Deadlock": {
                        return;
                    }

                    default:
                        return;
                }
            } else {
                switch (errType) {
                    case "Dirty read": {
                        System.out.println("DR");
                        if (errNO == 1) {
                            App.Demo.DirtyRead1.Fixed.gotoTran1();
                        } else {
                            App.Demo.DirtyRead2.Fixed.gotoTran1();
                        }

                        return;
                    }

                    case "Lost update": {
                        if (errNO == 1) {
                            App.Demo.LostUpdate1.Fixed.gotoTran1();
                        } else {
                            //App.Demo.LostUpdate2.Fixed.gotoTran1();
                        }
                        return;
                    }

                    case "Non-repeatable read": {
                        if (errNO == 1) {
                            //App.Demo.NonRepeatableRead1.Fixed.gotoTran1();
                        } else {
                            App.Demo.NonRepeatableRead2.Fixed.gotoTran1();
                        }
                        return;
                    }

                    case "Phantom": {
                        if (errNO == 1) {
                            //App.Demo.Phantom1.Fixed.gotoTran1();
                        } else {
                            App.Demo.Phantom2.Fixed.gotoTran1();
                        }
                        return;
                    }

                    case "Deadlock": {
                        return;
                    }

                    default:
                        return;
                }
            }
        }
    }

    public void onBtnTran2Click(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnTran2) {
            System.out.println(getClass() + ": btn tran2 clicked");
            System.out.println(errNO + " " + errType + " - Tran 2");
            if (demoType.equals("ERROR")) {
                switch (errType) {
                    case "Dirty read": {
                        System.out.println("DR");
                        if (errNO == 1) {
                            App.Demo.DirtyRead1.Error.gotoTran2();
                        } else {
                            App.Demo.DirtyRead2.Error.gotoTran2();
                        }

                        return;
                    }

                    case "Lost update": {
                        if (errNO == 1) {
                            App.Demo.LostUpdate1.Error.gotoTran2();
                        } else {
                            //App.Demo.LostUpdate2.Error.gotoTran2();
                        }

                        return;
                    }

                    case "Non-repeatable read": {
                        if (errNO == 1) {
                        } else {
                            App.Demo.NonRepeatableRead2.Error.gotoTran2();
                        }
                        return;
                    }

                    case "Phantom": {
                        if (errNO == 1) {
                        } else {
                            App.Demo.Phantom2.Fixed.gotoTran2();
                        }
                        return;
                    }

                    case "Deadlock": {
                        return;
                    }

                    default:
                        return;
                }
            } else {
                switch (errType) {
                    case "Dirty read": {
                        System.out.println("DR");
                        if (errNO == 1) {
                            App.Demo.DirtyRead1.Fixed.gotoTran2();
                        } else {
                            App.Demo.DirtyRead2.Fixed.gotoTran2();
                        }

                        return;
                    }

                    case "Lost update": {
                        if (errNO == 1) {
                            App.Demo.LostUpdate1.Fixed.gotoTran2();
                        } else {
                            //App.Demo.LostUpdate2.Error.gotoTran2();
                        }
                        return;
                    }

                    case "Non-repeatable read": {
                        if (errNO == 1) {
                        } else {
                            App.Demo.NonRepeatableRead2.Fixed.gotoTran2();
                        }
                        return;
                    }

                    case "Phantom": {
                        if (errNO == 1) {
                            //App.Demo.Phantom1.Fixed.gotoTran2();
                        } else {
                            App.Demo.Phantom2.Fixed.gotoTran2();;
                        }
                        return;
                    }

                    case "Deadlock": {
                        return;
                    }

                    default:
                        return;
                }
            }
        }
    }
}
