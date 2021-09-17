package com.projectbelajar.yuukbelajar.chat.model;

public class Chater {
    public String id;
    public String username;
    public String imgUrl;
    public String status;
    public String nama;
    public String level;
    public String kodeSekolah;
//    private String password;
    public String nis;
    public String nip;
    public String klinik;
    public String namaSekolah;
    public String kelas;
    public String time;

    public Chater() {

    }

    public Chater(String id, String username, String imgUrl, String status, String nama, String level, String kodeSekolah, String password, String nis, String nip, String klinik, String namaSekolah, String kelas, String time) {
        this.id = id;
        this.username = username;
        this.imgUrl = imgUrl;
        this.status = status;
        this.nama = nama;
        this.level = level;
        this.kodeSekolah = kodeSekolah;
//        this.password = password;
        this.nis = nis;
        this.nip = nip;
        this.klinik = klinik;
        this.namaSekolah = namaSekolah;
        this.kelas = kelas;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getKodeSekolah() { return kodeSekolah; }

    public void setKodeSekolah(String kodeSekolah) {
        this.kodeSekolah = kodeSekolah;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getKlinik() {
        return klinik;
    }

    public void setKlinik(String klinik) {
        this.klinik = klinik;
    }

    public String getNamaSekolah() {
        return namaSekolah;
    }

    public void setNamaSekolah(String namaSekolah) {
        this.namaSekolah = namaSekolah;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
