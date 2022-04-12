package pck.dbms.be.partner;

import pck.dbms.be.administrativeDivision.Address;
import pck.dbms.be.user.Login;

public class Partner {
    private Login login;
    private String name, representativeName, productType, phone, mail;
    private Address address;
    private int branchNumber, orderNumber;

    public Partner(Login login, String name, String representativeName, String productType, String phone, String mail, Address address, int branchNumber, int orderNumber) {
        this.login = login;
        this.name = name;
        this.representativeName = representativeName;
        this.productType = productType;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.branchNumber = branchNumber;
        this.orderNumber = orderNumber;
    }

    public Partner() {
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

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public String getAddressAsString() {
        return address.getAddressLine().toString() + "\n"
                + address.getWard().getFullName() + "\n"
                + address.getDistrict().getFullName() + "\n"
                + address.getProvince().getFullName() + "\n";
    }

    public int getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(int branchNumber) {
        this.branchNumber = branchNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
