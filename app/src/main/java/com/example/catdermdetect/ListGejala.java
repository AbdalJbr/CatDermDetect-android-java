package com.example.catdermdetect;

public class ListGejala {
    private String nm_gejala, kd_gejala;
    private boolean isChecked;

    public ListGejala(){

    }
    public String getNm_gejala() {
        return nm_gejala;
    }

    public void setNm_gejala(String nm_gejala) {
        this.nm_gejala = nm_gejala;
    }

    public String getKd_gejala() {
        return kd_gejala;
    }

    public void setKd_gejala(String kd_gejala) {
        this.kd_gejala = kd_gejala;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
