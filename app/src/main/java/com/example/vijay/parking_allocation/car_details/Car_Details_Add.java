package com.example.vijay.parking_allocation.car_details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vijay.parking_allocation.R;
import com.example.vijay.parking_allocation.SessionHandel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Car_Details_Add extends AppCompatActivity {

    ListView listView;
    EditText editText;
    Button add_btn;
    private Handler mHandler = new Handler();
    private RequestQueue requestQueue;
    private StringRequest request;
    private static final String URL = "https://shayongupta.000webhostapp.com/user_info/user_add_car.php";

    SessionHandel session;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__details__add);

        editText = (EditText)findViewById(R.id.edit1);
        add_btn = (Button)findViewById(R.id.addBtn);
        session = new SessionHandel(getApplicationContext());

        adapter=new ArrayAdapter<String>(this,
                R.layout.list_row,
                listItems);
        listView = (ListView)findViewById(R.id.list_item);
        listView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        add_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("car_no",editText.getText().toString());
                        hashMap.put("id", session.getuserId());
                      //  Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                        //fab.setEnabled(false);


                        return hashMap;
                    }
                };

                requestQueue.add(request);




                listItems.add("Successfully added Car ->"+editText.getText().toString());
                adapter.notifyDataSetChanged();




                v.setEnabled(false);
                // wait(200);new Handler().postDelayed(new Runnable() {
               mHandler.postDelayed(mUpdateTimeTask,5000);


            }
        });




    }

   /* public void addItems(View v) {
        listItems.add("Successfully added Car ->"+editText.getText().toString());
        adapter.notifyDataSetChanged();




        v.setEnabled(false);
       // wait(200);new Handler().postDelayed(new Runnable() {
        mHandler.postDelayed(mUpdateTimeTask,2000);


    }*/
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            // do what you need to do here after the delay
            startActivity(new Intent(getApplicationContext(),CarDetails.class));
            finish();

        }
    };
}
