package com.creative.busmapping.Activities;

import android.app.Activity;
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
    ArrayList<Double> coordinatesList;
    ArrayList<String> busInfoList;

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
                        Intent intent = new Intent(MainActivity.this,TimingsActivity.class);
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
}

