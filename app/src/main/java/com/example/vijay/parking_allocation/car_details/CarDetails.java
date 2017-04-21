package com.example.vijay.parking_allocation.car_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.vijay.parking_allocation.R;

public class CarDetails extends AppCompatActivity {
    Button add_btn,del_btn,show_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        add_btn = (Button)findViewById(R.id.add_car);
        del_btn = (Button)findViewById(R.id.del_car);
        show_btn = (Button)findViewById(R.id.show_car);

        add_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                startActivity(new Intent(getApplicationContext(),Car_Details_Add.class));
                finish();
            }
        });

        del_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                startActivity(new Intent(getApplicationContext(),DeleteCar.class));
                //finish();
            }
        });

        show_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                startActivity(new Intent(getApplicationContext(),ShowCarList.class));
                finish();
            }
        });
    }
}
