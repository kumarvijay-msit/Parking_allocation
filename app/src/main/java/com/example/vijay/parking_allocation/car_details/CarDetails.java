package com.example.vijay.parking_allocation.car_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.vijay.parking_allocation.MainActivity;
import com.example.vijay.parking_allocation.R;
import com.example.vijay.parking_allocation.RateCard;

public class CarDetails extends AppCompatActivity {
    Button add_btn,del_btn,show_btn;
    ImageView imgv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        add_btn = (Button)findViewById(R.id.add_car);
        del_btn = (Button)findViewById(R.id.del_car);
        show_btn = (Button)findViewById(R.id.show_car);
        imgv = (ImageView)findViewById(R.id.imageView1);
        imgv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent it = new Intent(CarDetails.this, MainActivity.class);
                startActivity(it);
                finish();


            }
        });




        add_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                startActivity(new Intent(getApplicationContext(),Car_Details_Add.class));

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

            }
        });
    }
}
