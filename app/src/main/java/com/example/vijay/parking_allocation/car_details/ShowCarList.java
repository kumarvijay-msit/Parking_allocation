package com.example.vijay.parking_allocation.car_details;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vijay.parking_allocation.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowCarList extends AppCompatActivity {
    ListView lv;
    ArrayAdapter<String> adapter;
    List<String> arr;
    String[] language = { "Car 1","Car 2","Car 3","Car 4","Car 5","Car 6","Car 7","Car 8" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_car_list);
        lv = (ListView) findViewById(R.id.list_item);
        arr = new ArrayList<String>(Arrays.asList(language));
        adapter = new ArrayAdapter<String>(this, R.layout.list_row, arr);
        lv.setAdapter(adapter);
    }
}
