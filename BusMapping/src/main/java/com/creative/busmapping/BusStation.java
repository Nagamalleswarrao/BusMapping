package com.creative.busmapping;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eshwar on 1/5/14.
 */
public class BusStation {
    public LatLng latLng;
    public String name;

    public  BusStation(LatLng latLng,String name){
        this.latLng = latLng;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " lat: " + latLng.latitude + " lng: " + latLng.longitude;
    }
}
