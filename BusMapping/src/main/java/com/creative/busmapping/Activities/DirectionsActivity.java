package com.creative.busmapping.Activities;

import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.creative.busmapping.GoogleDirections;
import com.creative.busmapping.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class DirectionsActivity extends FragmentActivity {

    GoogleMap mMap;
    GoogleDirections mDirections;
    LocationClient client;
    ArrayList<Double> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
//        Bundle extras = getIntent().getExtras();
//
//        Double frmLat = extras.getDouble("From Latitude");
//        Double frmLng = extras.getDouble("From Longitude");
//        Double toLat = extras.getDouble("To Latitude");
//        Double toLng = extras.getDouble("To Longitude");



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

        ArrayList<Double> extras = (ArrayList<Double>) getIntent().getSerializableExtra("COORDINATES");

        System.out.println(extras);
        double frmLat = extras.get(0);
        double frmLng = extras.get(1);
        double toLat = extras.get(2);
        double toLng = extras.get(3);
        LatLng fromPosition = new LatLng(frmLat, frmLng);
        LatLng toPosition = new LatLng(toLat, toLng);

        System.out.println("from position"+fromPosition+"to position"+toPosition);

        mMap.addMarker(new MarkerOptions().position(fromPosition).title("Start"));
        mMap.addMarker(new MarkerOptions().position(toPosition).title("End"));

        Document doc = mDirections.getDocument(fromPosition, toPosition, GoogleDirections.MODE_DRIVING);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_directions, container, false);
            return rootView;
        }
    }

}
