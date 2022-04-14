package pck.dbms.be.customer;

import pck.dbms.be.administrativeDivision.Address;
import pck.dbms.be.user.LoginInfo;

public class Customer {
    private LoginInfo loginInfo;
    private String name, phone, mail;
    private Address address;

    public Customer() {
        loginInfo = new LoginInfo();
    }

    public Customer(LoginInfo loginInfo, String name, String phone, String mail, Address address) {
        this.loginInfo = loginInfo;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
