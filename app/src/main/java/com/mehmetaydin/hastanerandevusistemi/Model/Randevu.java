package com.mehmetaydin.hastanerandevusistemi.Model;

public class Randevu {
    private String randevuID;
    private String doktorID;
    private String hastaID;
    private String saatID;
    private String tarih;

    public String getRandevuID() {
        return randevuID;
    }

    public void setRandevuID(String randevuID) {
        this.randevuID = randevuID;
    }

    public String getDoktorID() {
        return doktorID;
    }

    public void setDoktorID(String doktorID) {
        this.doktorID = doktorID;
    }

    public String getHastaID() {
        return hastaID;
    }

    public void setHastaID(String hastaID) {
        this.hastaID = hastaID;
    }

    public String getSaatID() {
        return saatID;
    }

    public void setSaatID(String saatID) {
        this.saatID = saatID;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
