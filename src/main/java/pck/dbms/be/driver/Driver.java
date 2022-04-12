package pck.dbms.be.driver;

import pck.dbms.be.administrativeDivision.Address;
import pck.dbms.be.user.Login;

public class Driver {
    private Login login;
    private String name, NIN, phone, mail;
    private Address address;
    private Driver activeAreaCode;
    private Bank bank;

    public Driver() {
    }

    public Driver(Login login, String name, String NIN, String phone, String mail, Address address, Driver activeAreaCode, Bank bank) {
        this.login = login;
        this.name = name;
        this.NIN = NIN;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.activeAreaCode = activeAreaCode;
        this.bank = bank;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNIN() {
        return NIN;
    }

    public void setNIN(String NIN) {
        this.NIN = NIN;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Driver getActiveAreaCode() {
        return activeAreaCode;
    }

    public void setActiveAreaCode(Driver activeAreaCode) {
        this.activeAreaCode = activeAreaCode;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
