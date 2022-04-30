package pck.dbms.be.partner;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Contract {
    private Partner partner;
    private String CID, TIN, status;
    private int extension;
    private LocalDateTime createdAt, expiredAt;
    private int contractTime;
    private double commission;
    private HashMap<String, PartnerBranch> partnerBranchList;
    private boolean isExpired;

    public Contract() {
    }

    public Contract(Partner partner, String CID, int extension, String TIN, LocalDateTime createdAt, LocalDateTime expiredAt, double commission, boolean isExpired, HashMap<String, PartnerBranch> partnerBranchHashMap) {
        this.partner = partner;
        this.CID = CID;
        this.TIN = TIN;
        this.extension = extension;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.commission = commission;
        this.isExpired = isExpired;
        this.partnerBranchList = partnerBranchHashMap;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public int getExtension() {
        return extension;
    }

    public void setExtension(int extension) {
        this.extension = extension;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public int getContractTime() {
        return contractTime;
    }

    public void setContractTime(int contractTime) {
        this.contractTime = contractTime;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public HashMap<String, PartnerBranch> getPartnerBranchList() {
        return partnerBranchList;
    }

    public void setPartnerBranchList(HashMap<String, PartnerBranch> partnerBranchList) {
        this.partnerBranchList = partnerBranchList;
    }

    public void addPartnerBranch(PartnerBranch partnerBranch) {
        if (partnerBranchList == null) {
            partnerBranchList = new HashMap<>();
        }
        if (!partnerBranchList.containsKey(partnerBranch.getPBID())) {
            partnerBranchList.put(partnerBranch.getPBID(), partnerBranch);
        }
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getPartnerName() {
        return partner.getName();
    }

    public String getRepName() {
        return partner.getRepresentativeName();
    }

    public String getPartnerAddress(){
        return partner.getAddressAsString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
