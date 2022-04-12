package pck.dbms.be.employee;

import pck.dbms.be.user.Login;

public class Employee {
    private Login login;
    private String name, mail;

    public Employee() {
    }

    public Employee(Login login, String name, String mail) {
        this.login = login;
        this.name = name;
        this.mail = mail;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
