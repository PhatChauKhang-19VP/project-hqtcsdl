package pck.dbms.be.user;

public class LoginInfo {
    private String username;
    private String password;
    private ROLE role;

    public LoginInfo() {
        username = "";
        password = "";
        role = ROLE.APP;
    }

    public LoginInfo(String username, String password, ROLE role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
