package pck.dbms.be.driver;

public class DriverRegistration {
    private Driver driver;
    private  String VIN, registrationStatus, payFeeStatus;
    private double fee;

    public DriverRegistration() {
    }

    public DriverRegistration(Driver driver, String VIN, String registrationStatus, String payFeeStatus, double fee) {
        this.driver = driver;
        this.VIN = VIN;
        this.registrationStatus = registrationStatus;
        this.payFeeStatus = payFeeStatus;
        this.fee = fee;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getPayFeeStatus() {
        return payFeeStatus;
    }

    public void setPayFeeStatus(String payFeeStatus) {
        this.payFeeStatus = payFeeStatus;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
