package com.creative.busmapping.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.creative.busmapping.ListViewAdapter;
import com.creative.busmapping.R;

public class MainActivity extends Activity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(new ListViewAdapter(this));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View view1 = view.findViewById(R.id.parent);

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
            }
        });
    }

    private void openMap(String route){
        Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
        startActivity(intent);
    }

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.buttRoute:
                openMap("Hyderabad");
                break;
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
            openMap("Hyderabad");
        }
        return super.onOptionsItemSelected(item);
    }

    private ListView getListView() {
        return list;
    }
}

