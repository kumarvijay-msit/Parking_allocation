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

public class Login extends AppCompatActivity {
    TextView bytes,sup,lin;
    EditText usr,pswd;
    private RequestQueue requestQueue;
    private static final String URL = "https://shayongupta.000webhostapp.com/user_info/user_login.php";
    private StringRequest request;
    public static final String EXTRA_MESSAGE = "MESSAGE";
    String message = "";
    private SessionHandel session;//global variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usr = (EditText) findViewById(R.id.usrusr);
        pswd = (EditText)findViewById(R.id.passwrd);
        lin = (TextView)findViewById(R.id.logiin);
        sup = (TextView)findViewById(R.id.sup);
        bytes = (TextView)findViewById(R.id.bytes);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/Lato-Light.ttf");
        bytes.setTypeface(custom_font);
        pswd.setTypeface(custom_font);
        sup.setTypeface(custom_font);
        lin.setTypeface(custom_font);
        usr.setTypeface(custom_font);
        requestQueue = Volley.newRequestQueue(this);

        session = new SessionHandel(getApplicationContext());


        lin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.names().get(0).equals("success")){
                                Toast.makeText(getApplicationContext(),jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                                message = jsonObject.getString("user_id");

                                session.setusername(message);
                               // Toast.makeText(getApplicationContext(),session.getusername(), Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra(EXTRA_MESSAGE, message));

                            }
                            else {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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
                        hashMap.put("email",usr.getText().toString());
                        hashMap.put("password",pswd.getText().toString());

                        return hashMap;
                    }
                };

                requestQueue.add(request);
                lin.setEnabled(false);
            }
        });


        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Login.this, Signup.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
