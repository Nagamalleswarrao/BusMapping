package com.creative.busmapping;

/**
 * Created by Eshwar on 12/25/13.
 */
public class SingleRow {
    String busNumber;
    String busRoute;
    String viaRoute;
    Double frmLat;
    Double frmLng;
    Double toLat;
    Double toLng;

    public SingleRow(String busNumbers, String busRoutes, String viaRoutes, Double frmLat, Double frmLng, Double toLat, Double toLng)
    {
        this.busNumber = busNumbers;
        this.busRoute = busRoutes;
        this.viaRoute = viaRoutes;
        this.frmLat = frmLat;
        this.frmLng = frmLng;
        this.toLat = toLat;
        this.toLng = toLng;
    }
}
