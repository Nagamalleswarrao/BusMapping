package com.creative.busmapping;

/**
 * Created by Eshwar on 12/25/13.
 */
public class SingleRow {
    String busNumber;
    String busRoute;
    String viaRoute;

    SingleRow(String busNumbers, String busRoutes, String viaRoutes)
    {
        this.busNumber = busNumbers;
        this.busRoute = busRoutes;
        this.viaRoute = viaRoutes;
    }
}
