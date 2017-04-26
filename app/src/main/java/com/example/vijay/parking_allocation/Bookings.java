package com.example.vijay.parking_allocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bookings extends AppCompatActivity {

    private static final String URL_BOOKING = "https://shayongupta.000webhostapp.com/booking/past_booking.php";
    ListView lv;
    ArrayAdapter<String> adapter;
    List<String> arr;
    String[] language = { };
    Button past_book_btn;
    private RequestQueue requestQueue;
    private StringRequest request;
    SessionHandel session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        past_book_btn = (Button) findViewById(R.id.past_book_btn);
        lv = (ListView) findViewById(R.id.list_item);
        arr = new ArrayList<String>(Arrays.asList(language));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(adapter);
        session = new SessionHandel(getApplicationContext());
        requestQueue = Volley.newRequestQueue(this);

        past_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new Items to List
                request = new StringRequest(Request.Method.POST, URL_BOOKING, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                int i =0;

                                Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();



                                while(!jsonObject.getString(String.valueOf(i)).equals("end")){

                                    arr.add(jsonObject.getString(String.valueOf(i)));
                                    //Toast.makeText(getApplicationContext(), jsonObject.getString(String.valueOf(i)), Toast.LENGTH_SHORT).show();
                                    i++;


                                }
                                adapter.notifyDataSetChanged();
                                // Toast.makeText(getApplicationContext(), jsonObject.getString("0"), Toast.LENGTH_SHORT).show();

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
                        hashMap.put("id",session.getuserId());
                        //  hashMap.put("id", session.getuserId());
                        //  Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                        //fab.setEnabled(false);


                        return hashMap;
                    }
                };

                requestQueue.add(request);

                adapter.notifyDataSetChanged();
                past_book_btn.setEnabled(false);

            }


        });






    }
}
