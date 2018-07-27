package com.yenti.pencarianjarak.algorithm;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Yenti on 5/4/2018.
 *
 */

public class Haversine {
    private static final Haversine ourInstance = new Haversine();

    public static Haversine getInstance() {
        return ourInstance;
    }

    public double getDistanceFromLatLonInKm(LatLng first, LatLng second) {
        double R = 6371; // Radius of the earth in km
        //deg2rad untuk convert bilangan degree ke radian (180 -> 3,14)
        double dLat = deg2rad(second.latitude-first.latitude);
        double dLon = deg2rad(second.longitude-first.longitude);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(first.latitude)) * Math.cos(deg2rad(second.latitude)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    //degree to radian converter
    double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
}
