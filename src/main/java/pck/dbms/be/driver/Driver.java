package pck.dbms.be.driver;

import pck.dbms.be.administrativeDivision.Address;
import pck.dbms.be.user.LoginInfo;

public class Driver {
    private LoginInfo loginInfo;
    private String name, NIN, phone, mail;
    private Address address;
    private String activeAreaCode;
    private Bank bank;

    public Driver() {
        loginInfo = new LoginInfo();
    }

    public Driver(LoginInfo loginInfo, String name, String NIN, String phone, String mail, Address address, String activeAreaCode, Bank bank) {
        this.loginInfo = loginInfo;
        this.name = name;
        this.NIN = NIN;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.activeAreaCode = activeAreaCode;
        this.bank = bank;
    }

    public LoginInfo getLogin() {
        return loginInfo;
    }

    public void setLogin(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
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

    public String getActiveAreaCode() {
        return activeAreaCode;
    }

    public void setActiveAreaCode(String activeAreaCode) {
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
