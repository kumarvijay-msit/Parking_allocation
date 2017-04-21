package com.example.vijay.parking_allocation.car_details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.vijay.parking_allocation.R;

import java.util.ArrayList;

public class Car_Details_Add extends AppCompatActivity {

    ListView listView;
    EditText editText;
    private Handler mHandler = new Handler();

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__details__add);
        editText = (EditText)findViewById(R.id.edit1);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView = (ListView)findViewById(R.id.list_item);
        listView.setAdapter(adapter);

    }

    public void addItems(View v) {
        listItems.add("Successfully added Car ->"+editText.getText().toString());
        adapter.notifyDataSetChanged();

        v.setEnabled(false);
       // wait(200);new Handler().postDelayed(new Runnable() {
        mHandler.postDelayed(mUpdateTimeTask,2000);


    }
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            // do what you need to do here after the delay
            startActivity(new Intent(getApplicationContext(),CarDetails.class));
            finish();

        }
    };
}
