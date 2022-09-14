package com.example.onlinearamlejelentes;

public class Szamla {
    private String email;
    private String meroOraGyariSzama;
    private double osszeg;

    public Szamla(String email,String meroOraGyariSzama,double osszeg){
        this.email=email;
        this.meroOraGyariSzama=meroOraGyariSzama;
        this.osszeg=osszeg;

    }
    public Szamla(){

    }


    public String getEmail() {
        return email;
    }

    public String getMeroOraGyariSzama() {
        return meroOraGyariSzama;
    }

    public double getOsszeg() {
        return osszeg;
    }
}
