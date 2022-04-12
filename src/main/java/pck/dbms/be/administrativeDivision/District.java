package pck.dbms.be.administrativeDivision;

public class District {
    String code, name, fullName;
    Province belong_to;

    public District(String code, String name, String fullName, Province belong_to) {
        this.code = code;
        this.name = name;
        this.fullName = fullName;
        this.belong_to = belong_to;
    }

    public District() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Province getProvinceBelongTo() {
        return belong_to;
    }

    public void setBelong_to(Province belong_to) {
        this.belong_to = belong_to;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
