package com.mehmetaydin.hastanerandevusistemi.Model;

public class User {
    private String TCKN;
    private String sifre;
    private String ad;
    private String soyad;
    private String rol;
    private String bolumID;
    public static String uID;

    public String getTCKN() {
        return TCKN;
    }

    public void setTCKN(String TCKN) {
        this.TCKN = TCKN;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getBolumID() {
        return bolumID;
    }

    public void setBolumID(String bolumID) {
        this.bolumID = bolumID;
    }
}
