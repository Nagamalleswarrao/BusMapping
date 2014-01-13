package com.creative.busmapping.Activities;

import android.app.ActionBar;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.creative.busmapping.GoogleDirections;
import com.creative.busmapping.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class DirectionsActivity extends FragmentActivity {

    GoogleMap mMap;
    GoogleDirections mDirections;
    LocationClient client;
    ArrayList<String> busInfoList;
    TextView busRoute,busNumber;

    String mSource = "none", mDestination = "none";
    double mLat = 10, mLng = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle args = getIntent().getExtras();
        if(args != null){
            mSource = args.getString("SOURCE");
            mDestination = args.getString("DESTINATION");
            busInfoList = args.getStringArrayList("BUS_INFO");
            mLat = args.getDouble("SOURCE_LAT");
            mLng = args.getDouble("DESTINATION_LNG");

            String bus_number = busInfoList.get(1);
            String bus_route = busInfoList.get(0);

            busRoute = (TextView) findViewById(R.id.busRouteTextView);
            busNumber = (TextView) findViewById(R.id.busNumberTextView);
            String[] s = bus_route.split("-");
            System.out.println(s);
            busNumber.setText(bus_number);
            busRoute.setText(bus_route);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mDirections = new GoogleDirections();
        mMap = ((SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        mMap.setMyLocationEnabled(true);

        client = new LocationClient(DirectionsActivity.this,
                new GooglePlayServicesClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                        Location loc = client.getLastLocation();
                        LatLng coordinates = new LatLng(loc.getLatitude(), loc.getLongitude());
                        Marker marker= mMap.addMarker(new MarkerOptions().position(coordinates).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Current Location"));
                        marker.showInfoWindow();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12));
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

        String destination = mDestination;
        String source = mSource;
        System.out.println("Source"+mSource+"Destination"+mDestination);
        LatLng lSource = new LatLng(mLat,mLng);
//        LatLng lDestination = new LatLng(dDestination, dDestination);
        mMap.addMarker(new MarkerOptions().position(lSource).title("Source"));
//        mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));

        Document doc = mDirections.getDocument(source, destination, GoogleDirections.MODE_DRIVING);
        int duration = mDirections.getDurationValue(doc);
        String distance = mDirections.getDistanceText(doc);
        String start_address = mDirections.getStartAddress(doc);
        String copy_right = mDirections.getCopyRights(doc);

        ArrayList<LatLng> directionPoint = mDirections.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.BLUE);

        for(int i = 0 ; i < directionPoint.size() ; i++) {
            rectLine.add(directionPoint.get(i));
        }

        mMap.addPolyline(rectLine);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.directions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(DirectionsActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
