package pck.dbms.be.employee;

import pck.dbms.be.user.LoginInfo;

public class Employee {
    private LoginInfo loginInfo;
    private String name, mail;

    public Employee() {
    }

    public Employee(LoginInfo loginInfo, String name, String mail) {
        this.loginInfo = loginInfo;
        this.name = name;
        this.mail = mail;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
