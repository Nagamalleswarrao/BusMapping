package com.creative.busmapping.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.creative.busmapping.BusList;
import com.creative.busmapping.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ListView listView;
    ArrayList<String> busInfoList;
    List<BusList> mBusList;
    ArrayList<BusList> busesList;
    String mSource = "none", mDestination = "none";
    double mLatS = 10, mLngS = 10;
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

        Bundle args = getIntent().getExtras();
        if(args != null){
            mSource = args.getString("SOURCE");
            mDestination = args.getString("DESTINATION");
            Bundle args1 = args.getBundle("bundle");
            mLatS = args1.getDouble("lat");
            mLngS = args1.getDouble("lng");
//            mLngS = 78.409816699999960000;
//            mLatS = 17.455570500000000000;
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
                        busInfoList = new ArrayList<String>();
                        for (int i=position;i<=position;i++)
                        {
                            BusList temp = busesList.get(position);
                            busInfoList.add(temp.name);
                            busInfoList.add(temp.busNumber);
                        }
                        System.out.println("source"+mSource+"destination"+mDestination+"lat"+mLatS+"lng"+mLngS);
                        Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
                        intent.putExtra("BUS_INFO",busInfoList);
                        intent.putExtra("SOURCE", mSource);
                        intent.putExtra("DESTINATION",mDestination);
                        intent.putExtra("SOURCE_LAT",mLatS);
                        intent.putExtra("DESTINATION_LNG",mLngS);
                        intent.putExtra("bundle", getIntent().getExtras());
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
                String key = "&destination=";
                String url = strings[0];
                int index = url.indexOf(key) + key.length();
                String sub1 = url.substring(0, index - key.length());
                String sub2 = url.substring(index, url.length());
                String sub3 = sub2.substring(sub2.indexOf('&'), sub2.length());

                String subUrl = url.substring(index, index + sub2.indexOf('&'));

                String newUrl = sub1 + key + URLEncoder.encode(subUrl, "utf-8") + sub3;
                Log.d("Dest", newUrl);
                URL webUrl = new URL(newUrl);

                String response = null;
                HttpURLConnection con = (HttpURLConnection) webUrl.openConnection();
                int sc = con.getResponseCode();
                Log.d("Code", sc + " " + con.getResponseMessage() + " ");
                if(sc == 400){
                    InputStream er = con.getErrorStream();
                    String res = readResponse(er);
                    Log.d("Body", res);
                }
                else if (sc == 200)
                {
                    InputStream is = con.getInputStream();
                    response = readResponse(is);
                }
                return response;

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
            if (s != null && !s.isEmpty()) {
                try {
                    mBusList = getBuses(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<BusList> getBuses(String response) throws JSONException {


        Log.d("GetBuses", "I am here!!");
        busesList = new ArrayList<BusList>();

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
                        BusList busList = new BusList(name,bus_number);
                        busList.name = name;
                        busList.busNumber = bus_number;
                        busesList.add(busList);
                        listView.setAdapter(new BusesListViewAdapter(this));
                    }

                }
            }

        }

        return busesList;
    }

    public class BusesListViewAdapter extends BaseAdapter {

        Context context;
        public BusesListViewAdapter(Context context) {
            this.context=context;
        }

        @Override
        public int getCount() {
            return busesList.size();
        }

        @Override
        public Object getItem(int i) {
            return busesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.single_row,viewGroup,false);
            TextView busName = (TextView) row.findViewById(R.id.textView2);
            TextView busNumber = (TextView) row.findViewById(R.id.textView);

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

            BusList temp = busesList.get(i);
            busName.setText(temp.name);
            busNumber.setText(temp.busNumber);
            return row;
        }
    }


}

