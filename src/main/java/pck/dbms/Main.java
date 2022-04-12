package pck.dbms;

import pck.dbms.database.DatabaseCommunication;
import pck.dbms.fe.App;

public class Main {
    public static void main(String[] args) {
        DatabaseCommunication.getInstance().loadAdministrativeDivision();
        System.out.println("hello");

        App.main(args);
    }
}
