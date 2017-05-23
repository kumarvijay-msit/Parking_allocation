package com.example.vijay.parking_allocation;

/**
 * Created by kumar on 23-05-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Belal on 7/22/2015.
 */
public class CustomList extends ArrayAdapter<String> {
    private String[] names;
    private String[] desc;
    private Integer imageid;
    private Activity context;

    public CustomList(Activity context, String[] names, Integer imageid) {
        super(context, R.layout.list_layout, names);
        this.context = context;
        this.names = names;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.list_textView);
        FloatingActionButton fab = (FloatingActionButton) listViewItem.findViewById(R.id.list_btn);

        textViewName.setText(names[position]);
        fab.setImageResource(imageid);
        return  listViewItem;
    }
}
