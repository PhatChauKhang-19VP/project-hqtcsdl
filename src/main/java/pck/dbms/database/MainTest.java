package pck.dbms.database;

import pck.dbms.be.administrativeDivision.*;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.user.Login;

public class MainTest {
    public static void main(String[] args) {
        DatabaseCommunication dc = DatabaseCommunication.getInstance();
//        AdministrativeDivision ad = AdministrativeDivision.getInstance();
//
//        dc.loadAdministrativeDivision();
//        DatabaseCommunication.customer.getListPartner();

        Login login = new Login();
        login.setUsername("phatnm.partner1");
        Partner partner = new Partner();
        partner.setLogin(login);

        DatabaseCommunication.customer.getListProduct(partner);
//        dc.partnerGetAcceptedContracts();
//
//        Login login = new Login("phatnguqqqqq", "1234", ROLE.PARTNER);
//        Ward w = ad.getWardList().get("26787");
//        District d = w.getDistrictBelongTo();
//        Province p = d.getProvinceBelongTo();
//        Address address = new Address(p, d, w, "di chi chi tiet");
//        Partner partner = new Partner(login, "Ngo minh phat", "Dai dien CC", "FOOD", "0704921111", "phat@gmail.com", address, 10, 100);
//
//        dc.partnerRegistration(login, partner);
//        if (DatabaseCommunication.employee.acceptAllContract()) {
//            System.out.println("done");
//        }

    }
}
