package com.yenti.pencarianjarak.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yenti on 5/7/2018.
 */

public class Event implements Parcelable{
    String id;
    String nama_event;
    String waktu;
    String tempat;

    public Event() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_event() {
        return nama_event;
    }

    public void setNama_event(String nama_event) {
        this.nama_event = nama_event;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String lokasi) {
        this.tempat = tempat;
    }

    protected Event(Parcel in) {
        id = in.readString();
        nama_event = in.readString();
        waktu = in.readString();
        tempat = in.readString();

    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama_event);
        dest.writeString(waktu);
        dest.writeString(tempat);

    }
}
