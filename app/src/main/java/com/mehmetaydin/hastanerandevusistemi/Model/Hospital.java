package com.mehmetaydin.hastanerandevusistemi.Model;

public class Hospital {
    private String hastaneAdi;
    private String adres;
    public static String hastaneID;

    public String getHastaneAdi() {
        return hastaneAdi;
    }

    public void setHastaneAdi(String hastaneAdi) {
        this.hastaneAdi = hastaneAdi;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
}
