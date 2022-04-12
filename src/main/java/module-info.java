module pck.dbms {
    requires java.naming;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires mssql.jdbc;
    requires java.sql;
    requires AnimateFX;

    opens pck.dbms.be.partner to javafx.base;

    opens pck.dbms.fe to javafx.fxml;
    exports pck.dbms.fe;

    opens pck.dbms.fe.partner.home to javafx.fxml;
    exports pck.dbms.fe.partner.home;

    opens pck.dbms.fe.customer.home to javafx.fxml;
    exports pck.dbms.fe.customer.home;

    opens pck.dbms.fe.customer.ordersTracking to javafx.fxml;
    exports pck.dbms.fe.customer.ordersTracking;

    opens pck.dbms.fe.welcome to javafx.fxml;
    exports pck.dbms.fe.welcome;

    opens pck.dbms.fe.entry.sign_in to javafx.fxml;
    exports pck.dbms.fe.entry.sign_in;

    opens pck.dbms.fe.partner.contract;
    exports pck.dbms.fe.partner.contract to javafx.fxml;

    opens pck.dbms.fe.customer.placeOrder.listPartner;
    exports pck.dbms.fe.customer.placeOrder.listPartner to javafx.fxml;

    opens pck.dbms.fe.customer.placeOrder.listProduct;
    exports pck.dbms.fe.customer.placeOrder.listProduct to javafx.fxml;
}