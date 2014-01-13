package com.creative.busmapping.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.creative.busmapping.BusStation;
import com.creative.busmapping.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BusStationListActivity extends Activity {

    LocationClient client;

    ListView busListView;
    List<BusStation> mBusStations;
    ArrayList<BusStation> busStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_station_list);
        busListView = (ListView) findViewById(R.id.busStationListView);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                System.out.println(busStations);
                Intent intent = new Intent(BusStationListActivity.this, InputActivity.class);
                intent.putExtra("BUS_STOP", busStations.get(position).name);
                intent.putExtra("lat", busStations.get(position).latLng.latitude);
                intent.putExtra("lng", busStations.get(position).latLng.longitude);
                startActivity(intent);
            }
        });
        getLocation();
    }

    public void getLocation(){
        client = new LocationClient(getApplicationContext(),
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
    }

    public Location updateMap(){
        Location loc = null;
        Log.d("TAG", client.getLastLocation() + "");
        boolean isLocationAvailable = checkLocationAvailability();
        if(isLocationAvailable){
            loc = client.getLastLocation();
            if(loc != null)
            {
                LatLng  coordinates =new LatLng(loc.getLatitude(), loc.getLongitude());
                final String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                        + "location=" + coordinates.latitude + "," + coordinates.longitude
                        + "&radius=1000&types=bus_station&sensor=false&key=AIzaSyAA7S81XKyj4zbp6-ZkYk_zPWkUHRgb2oo";
                System.out.println(url);
                new BusStationTask().execute(url);
            }
        }
        else{
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bus_station_list, menu);
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

    public AdapterView getListView() {
        return busListView;
    }

    private class BusStationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL webUrl = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) webUrl.openConnection();
                int sc = con.getResponseCode();
                if (sc == 200) {
                    InputStream is = con.getInputStream();
                    String response = readResponse(is);
                    return response;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String readResponse(InputStream is) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] data = new byte[2048];
            int len = 0;
            while ((len = is.read(data, 0, data.length)) >= 0) {
                bos.write(data, 0, len);
            }
            return new String(bos.toByteArray(), "UTF-8");
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    mBusStations = getBusStations(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public List<BusStation> getBusStations(String response) throws JSONException {

        busStations = new ArrayList<BusStation>();

        JSONObject object = new JSONObject(response);
        JSONArray results = object.getJSONArray("results");


        for (int i = 0; i < results.length(); i++) {
            JSONObject obj = results.getJSONObject(i);
            JSONObject geometry = obj.getJSONObject("geometry");

            JSONArray types = obj.getJSONArray("types");
            for (int j = 0; j < types.length(); j++) {
                if (types.getString(j).equals("bus_station")) {
                    double lat = geometry.getJSONObject("location").getDouble("lat");
                    double lng = geometry.getJSONObject("location").getDouble("lng");
                    BusStation busStation = new BusStation(new LatLng(lat, lng),obj.getString("name"));
                    busStation.name = obj.getString("name");

                    Log.d("Bus", busStation.toString());
                    busStations.add(busStation);
                    busListView.setAdapter(new BusListViewAdapter(this));
                }
            }
        }

        return busStations;
    }

    public class BusListViewAdapter extends BaseAdapter {

        Context context;
        public BusListViewAdapter(Context context) {
            this.context=context;
        }

        @Override
        public int getCount() {
            return busStations.size();
        }

        @Override
        public Object getItem(int i) {
            return busStations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.bus_list_row,viewGroup,false);
            TextView busStop = (TextView) row.findViewById(R.id.busStopTextView);

            if (i%3 == 0)
            {
                row.setBackgroundResource(R.drawable.violet);
            }
            else if (i%2 == 0)
            {
                row.setBackgroundResource(R.drawable.blue);
            }
            else if (i%5 == 0)
            {
                row.setBackgroundResource(R.drawable.red);
            }
            else {
                row.setBackgroundResource((i%2 == 0)? R.drawable.violet : R.drawable.glowblue);
            }

            BusStation temp = busStations.get(i);
            busStop.setText(temp.name);
            return row;
        }
    }
}
