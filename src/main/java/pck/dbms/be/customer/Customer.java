package pck.dbms.be.customer;

import pck.dbms.be.administrativeDivision.Address;
import pck.dbms.be.user.Login;

public class Customer {
    private Login login;
    private String name, phone, mail;
    private Address address;

    public Customer() {
    }

    public Customer(Login login, String name, String phone, String mail, Address address) {
        this.login = login;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
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
