package com.creative.busmapping.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.creative.busmapping.R;

/**
 * Created by Eshwar on 1/7/14.
 */
public class BussesListActivity extends Activity {
    ListView busListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busses_list);
        busListView = (ListView) findViewById(R.id.busListView);

    }
}
