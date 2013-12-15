package com.creative.busmapping;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(new ListViewAdapter(this));

        Resources res = getResources();
        Drawable divider = res.getDrawable(R.drawable.line);
        getListView().setDivider(divider);
        getListView().setDividerHeight(1);
        registerForContextMenu(getListView());

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View view1 = view.findViewById(R.id.parent);

                final View view2 = view.findViewById(R.id.child);
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

    private ListView getListView() {
        return list;
    }

}

class SingleRow{
    String busNumber;
    String busRoute;
    String viaRoute;

    SingleRow(String busNumbers,String busRoutes,String viaRoutes)
    {
        this.busNumber = busNumbers;
        this.busRoute = busRoutes;
        this.viaRoute = viaRoutes;
    }
}

class ListViewAdapter extends BaseAdapter{

    Context context;
    ArrayList<SingleRow> list;
    ListViewAdapter(Context c)
    {
        context = c;
        list = new ArrayList<SingleRow>();
        Resources res = c.getResources();
        String[] busNumbers = res.getStringArray(R.array.busNumbers);
        String[] busRoutes = res.getStringArray(R.array.busRoutes);
        String[] viaRoutes = res.getStringArray(R.array.viaRoutes);
        for(int i=0;i<5;i++)
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

        SingleRow temp = list.get(i);
        busNumber.setText(temp.busNumber);
        busRoute.setText(temp.busRoute);
        viaRoute.setText(temp.viaRoute);
        return row;
    }
}
