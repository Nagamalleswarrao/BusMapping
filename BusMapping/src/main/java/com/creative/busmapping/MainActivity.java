package com.creative.busmapping;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {

    boolean isPopulated = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.row_view,
                new String[] {"busNumber","busRoute","viaRoute"},
                new int[] {R.id.text1,R.id.text2, R.id.text3}
        );

        populateList();
        setListAdapter(adapter);

    }


    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    private void populateList() {
        HashMap<String,String> temp = new HashMap<String,String>();
        temp.put("busNumber","10H");
        temp.put("busRoute", "Secendarabad to Kondapur");
        temp.put("viaRoute", "via Hi-tech city, Ammerpet");
        list.add(temp);
        HashMap<String,String> temp1 = new HashMap<String,String>();
        temp1.put("busNumber","158F");
        temp1.put("busRoute", "LB Nagar to Borabanda");
        temp1.put("viaRoute", "via Erragadda, Ammerpet");
        list.add(temp1);
        HashMap<String,String> temp2 = new HashMap<String,String>();
        temp2.put("busNumber","10Y/F");
        temp2.put("busRoute", "Secendarabad to Borabanda");
        temp2.put("viaRoute", "via Yousafguda, Ammerpet");
        list.add(temp2);
        HashMap<String,String> temp3 = new HashMap<String,String>();
        temp3.put("busNumber","10K");
        temp3.put("busRoute", "Secendarabad to Kukatpally");
        temp3.put("viaRoute", "via Erragadda, Ammerpet");
        list.add(temp3);
        HashMap<String,String> temp4 = new HashMap<String,String>();
        temp4.put("busNumber","9F");
        temp4.put("busRoute", "Borabanda to CBS");
        temp4.put("viaRoute", "via Koti, Ammerpet, Erragadda");
        list.add(temp4);

    }
}


