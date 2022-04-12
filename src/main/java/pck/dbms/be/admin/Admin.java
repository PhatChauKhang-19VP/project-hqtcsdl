package pck.dbms.be.admin;

import pck.dbms.be.user.Login;

public class Admin {
    private Login login;
    private String name;

    public Admin() {
    }

    public Admin(Login login, String name) {
        this.login = login;
        this.name = name;
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
}
