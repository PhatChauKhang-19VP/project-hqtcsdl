package pck.dbms.be.driver;

public class Bank {
    private String BID, name, branch;

    public Bank() {
    }

    public Bank(String BID, String name, String branch) {
        this.BID = BID;
        this.name = name;
        this.branch = branch;
    }

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
