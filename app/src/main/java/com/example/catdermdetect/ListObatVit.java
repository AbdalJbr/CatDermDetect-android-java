package com.example.catdermdetect;

public class ListObatVit {
    private String nama_obat;
    private String fungsi;
    private String kd_obat;
    public ListObatVit() {
        // No-argument constructor required for Firestore
    }

    public String getNama_obat() {
        return nama_obat;
    }

    public void setNm_obat(String nama_obat) {
        this.nama_obat = nama_obat;
    }

    public String getFungsi() {
        return fungsi;
    }

    public void setFungsi(String fungsi) {
        this.fungsi = fungsi;
    }

    public String getKd_obat() {
        return kd_obat;
    }

    public void setKd_obat(String kd_obat) {
        this.kd_obat = kd_obat;
    }


}
