package com.creative.busmapping.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.creative.busmapping.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

public class MapActivity extends ActionBarActivity {

    GoogleMap map;

    private class MyGeoPoint {
        String location;
        double latitude;
        double longitude;

        public MyGeoPoint(String loc, double lat, double longi) {
            this.location = loc;
            this.latitude = lat;
            this.longitude = longi;
        }

        public LatLng getLatLng() {
            return new LatLng(latitude, longitude);
        }
    }
    Vector<MyGeoPoint> myGeoPoints = new Vector<MyGeoPoint>();

    LocationClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_activity);
        addLatLong();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);

        client = new LocationClient(MapActivity.this,
                new GooglePlayServicesClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                        updateMap();
                    }

                    @Override
                    public void onDisconnected() {

                    }
                }, new GooglePlayServicesClient.OnConnectionFailedListener() {

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {

            }
        }
        );
        client.connect();
//        map.addMarker(new MarkerOptions().position(myGeoPoints.get(0).getLatLng()).title("Hyderabad"));

     }

    public Location updateMap(){
        Location loc = null;
        Log.d("TAG", client.getLastLocation() + "");
        boolean isLocationAvailable = checkLocationAvailability();
        if(isLocationAvailable){
            loc = client.getLastLocation();
            if(loc != null)
            {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()),12);
                map.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())));
                map.animateCamera(update);
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Location access not enabled");
            builder.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_SETTINGS );
                    startActivity(myIntent);
                    //get gps
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            builder.create().show();
        }
        return loc;



    }

    public boolean checkLocationAvailability(){

        LocationManager lm = null;

        boolean gps_enabled = false,
                network_enabled = false;
        if(lm==null)
            lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex){}

        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex){}

        if(!gps_enabled && !network_enabled){
            return false;
        }
        else
            return true;
    }

    private void addLatLong() {
        myGeoPoints.add(new MyGeoPoint("Hyderabad", 17.38504, 78.48667));
        myGeoPoints.add(new MyGeoPoint("Srinagar", 34.08366, 74.79737));
        myGeoPoints.add(new MyGeoPoint("Delhi", 28.63531, 77.22496));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
