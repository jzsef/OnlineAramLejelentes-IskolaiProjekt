package com.example.onlinearamlejelentes;

public class MeroOra {
    private String email;
    private String meroOraGyariSzama;
    private int meroAllasKWh;

    public MeroOra(String email,String merooragysz,int kWh){
        this.email=email;
        this.meroOraGyariSzama=merooragysz;
        this.meroAllasKWh=kWh;
    }
    public MeroOra(){

    }

    public String getEmail() {
        return email;
    }

    public String getMeroOraGyariSzama() {
        return meroOraGyariSzama;
    }

    public int getMeroAllasKWh() {
        return meroAllasKWh;
    }
}
