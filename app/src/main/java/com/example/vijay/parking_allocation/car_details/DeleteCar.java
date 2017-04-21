package com.example.vijay.parking_allocation.car_details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vijay.parking_allocation.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCar extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<String> adapter;
    List<String> arr;
    String[] language = { "Car 1","Car 2","Car 3","Car 4","Car 5","Car 6","Car 7","Car 8" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_car);

        lv = (ListView) findViewById(R.id.list_item);
        arr = new ArrayList<String>(Arrays.asList(language));
        adapter = new ArrayAdapter<String>(this, R.layout.list_row, arr);
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // setting onItemLongClickListener and passing the position to the function
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                removeItemFromList(position);

                return true;
            }
        });
    }

    protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                DeleteCar.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TOD O Auto-generated method stub

                // main code on after clicking yes
                arr.remove(deletePosition);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }
}
