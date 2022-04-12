package pck.dbms.be.partner;

import pck.dbms.be.administrativeDivision.Address;
import pck.dbms.be.product.Product;
import pck.dbms.be.utils.Pair;

import java.util.HashMap;

public class PartnerBranch {
    private Partner partner;
    private String PBID, name;
    private Address address;
    private boolean isDeleted;

    // products in branches
    private HashMap<String, Pair<Product, Integer>> productList;

    public PartnerBranch() {
    }

    public PartnerBranch(Partner partner, String PBID, String name, Address address, boolean isDeleted) {
        this.partner = partner;
        this.PBID = PBID;
        this.name = name;
        this.address = address;
        this.isDeleted = isDeleted;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getPBID() {
        return PBID;
    }

    public void setPBID(String PBID) {
        this.PBID = PBID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public HashMap<String, Pair<Product, Integer>> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<String, Pair<Product, Integer>> productList) {
        this.productList = productList;
    }

    public void addProductToBranch(Product product, Integer quantity){
        if (productList == null){
            productList = new HashMap<>();
        }

        if (!productList.containsKey(product.getPID())){
            productList.put(product.getPID(), new Pair<>(product, quantity));
        }
    }
}
