package pck.dbms.be.administrativeDivision;

public class Address {
    private Province province;
    private District district;
    private Ward ward;
    private String addressLine;

    public Address(Province province, District district, Ward ward, String addressLine) {
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.addressLine = addressLine;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    @Override
    public String toString() {
        return addressLine + ", " + ward + ", " + district + ", " + province;
    }
}
