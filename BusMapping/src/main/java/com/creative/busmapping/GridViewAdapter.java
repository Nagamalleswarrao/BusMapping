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
 * Created by Eshwar on 12/29/13.
 */
public class GridViewAdapter extends BaseAdapter{

    Context context;
    ArrayList<String> arrayList;
    public GridViewAdapter(Context context)
    {
        this.context=context;
        arrayList = new ArrayList<String>();
        Resources res = context.getResources();
        String[] timings = res.getStringArray(R.array.timings);

        for(int i=0;i<timings.length;i++)
        {
            arrayList.add(timings[i]);
        }
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View grid = inflater.inflate(R.layout.single_grid,viewGroup,false);
        TextView timings = (TextView) grid.findViewById(R.id.timing_text);

        timings.setText(arrayList.get(i));
        return grid;
    }
}
