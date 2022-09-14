package com.example.onlinearamlejelentes;

public class FelhasznaloiAdat {
    private String email;
    private String veznev;
    private String kernev;
    private String cim;



    public FelhasznaloiAdat(String email, String veznev,String kernev, String cim){
        this.email=email;
        this.veznev=veznev;
        this.kernev=kernev;
        this.cim=cim;


    }
    public FelhasznaloiAdat(){

    }

    public String getEmail(){
        return email;
    }


    public String getVeznev() {
        return veznev;
    }

    public String getKernev() {
        return kernev;
    }

    public String getCim() {
        return cim;
    }


}
