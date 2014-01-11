package com.creative.busmapping.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.creative.busmapping.BusStation;
import com.creative.busmapping.ListViewAdapter;
import com.creative.busmapping.R;
import com.google.android.gms.location.LocationClient;

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

public class MainActivity extends Activity {

    ListView listView;
    LocationClient client;
    ArrayList<Double> coordinatesList;
    ArrayList<String> busInfoList;
    List<BusStation> mBusList;
    ArrayList<BusStation> busList;
    String mSource = "none", mDestination = "none";
    double mLatS = 10, mLngS = 10;
    double mLatD = 17.4567, mlngD =  78.5669;

    long unixTime = System.currentTimeMillis() / 1000;

    public long getUnixTime() {
        unixTime = System.currentTimeMillis() / 1000;
        return unixTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ListViewAdapter(this));

        Bundle args = getIntent().getExtras();
        if(args != null){
            mSource = args.getString("SOURCE");
            mDestination = args.getString("DESTINATION");
            Bundle args1 = args.getBundle("bundle");
            mLatS = args1.getDouble("lat");
            mLngS = args1.getDouble("lng");
        }
        getBusList();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                View view1 = view.findViewById(R.id.parent);
                Button buttRoute = (Button)view.findViewById(R.id.buttRoute);
                Button buttTimings = (Button)view.findViewById(R.id.buttTimings);

                final View view2 = view.findViewById(R.id.child);
                view2.setVisibility(View.VISIBLE);
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view2.getVisibility() == View.GONE){
                            view2.setVisibility(View.VISIBLE);

                        }else {
                            view2.setVisibility(View.GONE);
                        }
                    }
                });

                buttRoute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        coordinatesList = new ArrayList<Double>();
                        busInfoList = new ArrayList<String>();
                        String[] busNumbers = getResources().getStringArray(R.array.bus_numbers);
                        String[] busRoutes = getResources().getStringArray(R.array.bus_routes);
                        String[] viaRoutes = getResources().getStringArray(R.array.via_routes);
                        String[] frmLat = getResources().getStringArray(R.array.from_latitude);
                        String[] frmLng = getResources().getStringArray(R.array.from_longitude);
                        String[] toLat = getResources().getStringArray(R.array.to_latitude);
                        String[] toLng = getResources().getStringArray(R.array.to_longitude);

                        double[] frmDoubleLat = new double[frmLat.length];
                        double[] frmDoubleLng = new double[frmLat.length];
                        double[] toDoubleLat = new double[frmLat.length];
                        double[] toDoubleLng = new double[frmLat.length];

                        for (int i=position;i<=position;i++)
                        {
                              frmDoubleLat[i] = Double.parseDouble(frmLat[i]);
                              frmDoubleLng[i] = Double.parseDouble(frmLng[i]);
                              toDoubleLat[i] = Double.parseDouble(toLat[i]);
                              toDoubleLng[i] = Double.parseDouble(toLng[i]);

                              coordinatesList.add(frmDoubleLat[i]);
                              coordinatesList.add(frmDoubleLng[i]);
                              coordinatesList.add(toDoubleLat[i]);
                              coordinatesList.add(toDoubleLng[i]);

                              busInfoList.add(busNumbers[i]);
                              busInfoList.add(busRoutes[i]);
                              busInfoList.add(viaRoutes[i]);
                        }

                        Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
                        intent.putExtra("CO_ORDINATES", coordinatesList);
                        intent.putExtra("BUS_INFO",busInfoList);

                        startActivity(intent);
                    }
                });

                buttTimings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this,BusStationListActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void openMap(){
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        }else if(id == R.id.action_myLocation){
            openMap();
        }
        return super.onOptionsItemSelected(item);
    }

    private ListView getListView() {
        return listView;
    }

//    Json parsing
    public void getBusList(){
         final String url = "http://maps.googleapis.com/maps/api/directions/json?"
              + "origin=" + mLatS + "," + mLngS
              + "&destination=" + mDestination
              + "&sensor=false&units=metric&mode=transit&arrival_time="+getUnixTime();
              Log.d("Google Transit List", url);
              new BusListTask().execute(url);
    }

    private class BusListTask extends AsyncTask<String, Void, String> {

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
                    mBusList = getBuses(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<BusStation> getBuses(String response) throws JSONException {

        busList = new ArrayList<BusStation>();

        JSONObject object = new JSONObject(response);
        JSONArray routes = object.getJSONArray("routes");


        for (int i = 0; i < routes.length(); i++) {
            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

            for(int k = 0; k < legs.length(); k++){
                JSONArray steps = legs.getJSONObject(k).getJSONArray("steps");

                for(int j = 0; j < steps.length(); j++){
                    if(!steps.getJSONObject(j).isNull("transit_details")){
                        String name = steps.getJSONObject(j).getJSONObject("transit_details").getJSONObject("line").getString("name");
                        String bus_number = steps.getJSONObject(j).getJSONObject("transit_details").getJSONObject("line").getString("short_name");
                        Log.d("test", name);
                        Log.d("test", bus_number);
                    }

                }
            }

        }

        return busList;
    }


}

