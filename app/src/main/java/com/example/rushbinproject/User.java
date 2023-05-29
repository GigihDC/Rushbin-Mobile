package com.example.rushbinproject;

public class User {
    private String username, nama_lengkap, id_pengguna;
    private int point;

    public User(String username, String nama_lengkap, String id_pengguna, int point){
        this.username = username;
        this.nama_lengkap = nama_lengkap;
        this.id_pengguna = id_pengguna;
        this.point = point;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama_lengkap;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public int getPoint() {
        return point;
    }
    public void setPoint(int point) {
        this.point = point;
    }
}
