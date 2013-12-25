package com.creative.busmapping.Activities;

/**
 * Created by Eshwar on 12/12/13.
 */

import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.widget.SimpleAdapter;

import com.creative.busmapping.R;

public class BusInfoActivity extends ListActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_row);

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.bus_info,
                new String[] {"busNumber","labelBusNumber"},
                new int[] {R.id.text1,R.id.text2}
        );
        populateList();
        setListAdapter(adapter);
    }

    static final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    private void populateList() {
        HashMap<String,String> temp = new HashMap<String,String>();
        temp.put("busNumber","10H");
        temp.put("labelBusNumber", "Bus Number :");
        list.add(temp);

    }

}
