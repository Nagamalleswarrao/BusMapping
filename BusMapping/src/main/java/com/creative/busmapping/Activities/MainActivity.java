package com.creative.busmapping.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.creative.busmapping.ListViewAdapter;
import com.creative.busmapping.R;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView listView;
    Context context;
    ArrayList<Double> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ListViewAdapter(this));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                View view1 = view.findViewById(R.id.parent);
                Button buttRoute = (Button)view.findViewById(R.id.buttRoute);

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
                        arrayList = new ArrayList<Double>();
                        String[] frmLat = getResources().getStringArray(R.array.fromLatitude);
                        String[] frmLng = getResources().getStringArray(R.array.fromLongitude);
                        String[] toLat = getResources().getStringArray(R.array.toLatitude);
                        String[] toLng = getResources().getStringArray(R.array.toLongitude);

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

                              arrayList.add(frmDoubleLat[i]);
                              arrayList.add(frmDoubleLng[i]);
                              arrayList.add(toDoubleLat[i]);
                              arrayList.add(toDoubleLng[i]);
                        }

                        System.out.println(arrayList);

                        Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
                        intent.putExtra("COORDINATES",arrayList);

                        startActivity(intent);
                    }
                });

            }
        });
    }

    public void openMap(double frmLat, double frmLng, double toLat, double toLng){
        Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
        intent.putExtra("From Latitude",frmLat);
        intent.putExtra("From Longitude",frmLng);
        intent.putExtra("To Latitude",toLat);
        intent.putExtra("To Longitude",toLng);
        startActivity(intent);
    }

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.buttTimings:
                break;
        }
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
            openMap(17.451619700000000000,78.416924999999990000,17.432614500000000000,78.502896299999970000);
        }
        return super.onOptionsItemSelected(item);
    }

    private ListView getListView() {
        return listView;
    }
}

