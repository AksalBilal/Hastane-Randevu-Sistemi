package com.mehmetaydin.hastanerandevusistemi.Model;

public class Bolum {
    private String bolumID;
    private String bolumAdi;
    private String hastaneID;

    public String getBolumID() {
        return bolumID;
    }

    public void setBolumID(String bolumID) {
        this.bolumID = bolumID;
    }

    public String getBolumAdi() {
        return bolumAdi;
    }

    public void setBolumAdi(String bolumAdi) {
        this.bolumAdi = bolumAdi;
    }

    public String getHastaneID() {
        return hastaneID;
    }

    public void setHastaneID(String hastaneID) {
        this.hastaneID = hastaneID;
    }
}
