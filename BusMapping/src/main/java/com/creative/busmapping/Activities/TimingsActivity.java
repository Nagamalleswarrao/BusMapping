package com.creative.busmapping.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.creative.busmapping.GridViewAdapter;
import com.creative.busmapping.R;

/**
 * Created by Eshwar on 12/29/13.
 */
public class TimingsActivity extends Activity {

    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timings_info);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(this));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
