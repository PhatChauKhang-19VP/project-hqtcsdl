package pck.dbms.be;

import pck.dbms.database.DatabaseCommunication;

public class MainTest {
    public static void main(String[] args) {
        DatabaseCommunication dc = DatabaseCommunication.getInstance();

        dc.login("nmphat.admin001", "cus12345");
    }
}
