package com.creative.busmapping;

/**
 * Created by Eshwar on 1/11/14.
 */
public class BusList {
    public String name;
    public String busNumber;

    public  BusList(String name,String busNumber){
        this.name = name;
        this.busNumber = busNumber;
    }

    @Override
    public String toString() {
        return name + " Bus Detail " + busNumber + "Bus number ";
    }
}
