package com.example.vijay.parking_allocation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    EditText passwordd,mobphone,mail,usrusr;
    TextView login,signup;


    private static final String URL = "https://shayongupta.000webhostapp.com/user_info/user_signup.php";
    private RequestQueue requestQueue;
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usrusr = (EditText) findViewById(R.id.usrusr);
        passwordd = (EditText)findViewById(R.id.passwrd);
        mail = (EditText) findViewById(R.id.mail);
        mobphone = (EditText) findViewById(R.id.mobphone);
        login = (TextView)findViewById(R.id.logiin);
        signup = (TextView)findViewById(R.id.sup);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/Lato-Light.ttf");
        signup.setTypeface(custom_font);
        mail.setTypeface(custom_font);
        mobphone.setTypeface(custom_font);
        passwordd.setTypeface(custom_font);
        usrusr.setTypeface(custom_font);
        login.setTypeface(custom_font);
        requestQueue = Volley.newRequestQueue(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.names().get(0).equals("success")){
                                Toast.makeText(getApplicationContext(),"SUCCESS "+jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),login.class));
                            }else {
                                Toast.makeText(getApplicationContext(), "Error" +jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("email",mail.getText().toString() );
                        hashMap.put("username",usrusr.getText().toString());
                        hashMap.put("password",passwordd.getText().toString());
                        hashMap.put("mob_no",mobphone.getText().toString() );
                        return hashMap;
                    }
                };

                requestQueue.add(request);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(signup.this,login.class);
                startActivity(it);
            }
        });
    }
}