package com.staj.eray.doit;

public class Liste {

    public int id;
    public String yapilacak;
    public int yapildi_mi;

    public Liste (int id, String yapilacak, int yapildi_mi){
        this.id = id;
        this.yapilacak = yapilacak;
        this.yapildi_mi = yapildi_mi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYapilacak() {
        return yapilacak;
    }

    public void setYapilacak(String yapilacak) {
        this.yapilacak = yapilacak;
    }

    public int getYapildi_mi() {
        return yapildi_mi;
    }

    public void setYapildi_mi(int yapildi_mi) {
        this.yapildi_mi = yapildi_mi;
    }

}
