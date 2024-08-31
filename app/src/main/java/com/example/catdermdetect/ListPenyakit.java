package com.example.catdermdetect;

public class ListPenyakit {
    private String nm_penyakit;
    private String solusi;
    private String kd_penyakit;
    private String deskripsi;

    public ListPenyakit() {
        // No-argument constructor required for Firestore
    }

    public String getNm_penyakit() {
        return nm_penyakit;
    }

    public void setNm_penyakit(String nm_penyakit) {
        this.nm_penyakit = nm_penyakit;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public String getKd_penyakit() {
        return kd_penyakit;
    }

    public void setKd_penyakit(String kd_penyakit) {
        this.kd_penyakit = kd_penyakit;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
