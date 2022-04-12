package pck.dbms.be.partner;

import java.time.LocalDateTime;

public class PartnerRegistration {
    private Partner partner;
    private LocalDateTime atDatetime;
    private String status;

    public PartnerRegistration() {
    }

    public PartnerRegistration(Partner partner, LocalDateTime atDatetime, String status) {
        this.partner = partner;
        this.atDatetime = atDatetime;
        this.status = status;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public LocalDateTime getAtDatetime() {
        return atDatetime;
    }

    public void setAtDatetime(LocalDateTime atDatetime) {
        this.atDatetime = atDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
