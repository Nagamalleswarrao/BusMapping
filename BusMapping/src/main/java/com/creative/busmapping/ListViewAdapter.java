package com.creative.busmapping;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eshwar on 12/25/13.
 */
public class ListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<SingleRow> list;
    public ListViewAdapter(Context c)
    {
        context = c;
        list = new ArrayList<SingleRow>();
        Resources res = c.getResources();
        String[] busNumbers = res.getStringArray(R.array.bus_numbers);
        String[] busRoutes = res.getStringArray(R.array.bus_routes);
        String[] viaRoutes = res.getStringArray(R.array.via_routes);

        for(int i=0;i<busNumbers.length;i++)
        {
            list.add(new SingleRow(busNumbers[i],busRoutes[i],viaRoutes[i]));
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row,viewGroup,false);
        TextView busNumber = (TextView) row.findViewById(R.id.textView);
        TextView busRoute = (TextView) row.findViewById(R.id.textView2);
        TextView viaRoute = (TextView) row.findViewById(R.id.textView3);

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

        SingleRow temp = list.get(i);
        busNumber.setText(temp.busNumber);
        busRoute.setText(temp.busRoute);
        viaRoute.setText(temp.viaRoute);
        return row;
    }
}
