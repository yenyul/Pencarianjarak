package com.yenti.pencarianjarak.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yenti on 4/29/2018.
 *
 */

public class Wisata implements Parcelable{
    String id;
    String nama_wisata;
    String alamat;
    String no_telp;
    String foto;
    String keterangan;
    String lattitude;
    String longtitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_wisata() {
        return nama_wisata;
    }

    public void setNama_wisata(String nama_wisata) {
        this.nama_wisata = nama_wisata;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    protected Wisata(Parcel in) {
        id = in.readString();
        nama_wisata = in.readString();
        alamat = in.readString();
        no_telp = in.readString();
        foto = in.readString();
        keterangan = in.readString();
        lattitude = in.readString();
        longtitude = in.readString();
    }

    public static final Creator<Wisata> CREATOR = new Creator<Wisata>() {
        @Override
        public Wisata createFromParcel(Parcel in) {
            return new Wisata(in);
        }

        @Override
        public Wisata[] newArray(int size) {
            return new Wisata[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama_wisata);
        dest.writeString(alamat);
        dest.writeString(no_telp);
        dest.writeString(foto);
        dest.writeString(keterangan);
        dest.writeString(lattitude);
        dest.writeString(longtitude);
    }
}
