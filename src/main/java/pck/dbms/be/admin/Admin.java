package pck.dbms.be.admin;

import pck.dbms.be.user.LoginInfo;

public class Admin {
    private LoginInfo loginInfo;
    private String name;

    public Admin() {
    }

    public Admin(LoginInfo loginInfo, String name) {
        this.loginInfo = loginInfo;
        this.name = name;
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
}
