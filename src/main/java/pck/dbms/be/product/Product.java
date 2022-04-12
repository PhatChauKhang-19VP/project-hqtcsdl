package pck.dbms.be.product;

import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.utils.Pair;

import java.util.HashMap;

public class Product {
    public HashMap<String, Pair<PartnerBranch, Integer>> inBranches;
    private String PID, productType, imgSrc, name, description;
    private double price;
    private boolean isDeleted;
    private Partner partner;

    public Product() {
        this.PID = PID;
        this.productType = productType;
        this.imgSrc = imgSrc;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isDeleted = isDeleted;
        inBranches = new HashMap<>();
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void addInBranch(PartnerBranch pb, int selfStock) {
        if (!inBranches.containsKey(pb.getPBID())) {
            inBranches.put(pb.getPBID(), new Pair<>(pb, selfStock));
        }
    }
}
