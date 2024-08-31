package com.example.catdermdetect;

public class ListRiwayat {


    private String id_diagnosa,username,email,kd_penyakit,kd_gejala,solusi,date;

    public ListRiwayat() {
        // No-argument constructor required for Firestore
    }

    public String getId_diagnosa() {
        return id_diagnosa;
    }

    public void setId_diagnosa(String id_diagnosa) {
        this.id_diagnosa = id_diagnosa;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKd_penyakit() {
        return kd_penyakit;
    }

    public void setKd_penyakit(String kd_penyakit) {
        this.kd_penyakit = kd_penyakit;
    }

    public String getKd_gejala() {
        return kd_gejala;
    }

    public void setKd_gejala(String kd_gejala) {
        this.kd_gejala = kd_gejala;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
