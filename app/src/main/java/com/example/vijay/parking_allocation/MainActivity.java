package com.example.vijay.parking_allocation;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vijay.parking_allocation.car_details.CarDetails;
import com.example.vijay.parking_allocation.user.Login;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.ACTION_SENDTO;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
import static android.text.TextUtils.isEmpty;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String URL = "https://shayongupta.000webhostapp.com/booking/user_control.php";
    private static final String URL_CAR_LIST = "https://shayongupta.000webhostapp.com/user_info/user_show_car.php";
    private static final String URL_SHOW_CAR = "https://shayongupta.000webhostapp.com/user_info/user_show_car.php";
    SupportMapFragment sMapFragment;
    GoogleMap gMap;
    Marker currLocationMarker;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    GoogleApiClient mGoogleApiClient;
    double longitude;
    double latitude;
    private boolean mPermissionDenied = false;
    private static GoogleMap mMap;
    private View b_get;
    private TrackGps gps;
    int ids = 0;
    ArrayList<LatLng> markerPoints;
    static LatLng srclocation;
    static LatLng destination;
    Button btn1, show_cars;
    private RequestQueue requestQueue, dropdownQueue;
    private StringRequest request, dropdownRequest;
    Spinner spinner;
    String message;
    FloatingActionButton fab;
    static boolean flag = false, flags = false;
    static boolean location_enabled = false;

    ArrayList<String> options = new ArrayList<String>();
    ArrayAdapter<String> adapters;
    PlaceAutocompleteFragment autocompleteFragment;

    StringBuilder start_time_park,end_time_park;

    double parklat = 0.0;
    double parklong = 0.0;
    LatLng parkloc;

    Marker myMarkersrc = null, myMarkerDest = null, myparking = null;
    DrawerLayout drawer;
    SessionHandel session;
    String name;
    TextView t,time_message;
    FloatingActionButton imgMyLocation;
    View b1, b2, b3, b4,b5,b6;
    ArrayAdapter<String> adapter;

    private TextView start_time, end_time;
    private Button start_btn, end_btn,proceed;

    String car_selected;

    private int sHour, eHour;
    private int sMinute, eMinute;
    /**
     * This integer will uniquely define the dialog to be used for displaying time picker.
     */
    static final int TIME_DIALOG_START = 0;
    static final int TIME_DIALOG_END = 1;
    /**
     * Callback received when the user "picks" a time in the dialog
     */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    sHour = hourOfDay;
                    sMinute = minute;
                    updateDisplay();

                    displayToast();
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener_end =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    eHour = hourOfDay;
                    eMinute = minute;
                    updateDisplayEnd();

                    displayToast();
                }
            };

    /**
     * Updates the time in the TextView
     */
    private void updateDisplay() {
        start_time_park = new StringBuilder()
                .append(pad(sHour)).append(":")
                .append(pad(sMinute));
        start_time.setText(start_time_park);

    }

    private void updateDisplayEnd() {
        end_time_park = new StringBuilder()
                .append(pad(eHour)).append(":")
                .append(pad(eMinute));
        end_time.setText(end_time_park);

        time_message.setText("TIME SLOT ALLOTED "+start_time.getText()+" - "+end_time.getText());

    }

    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(start_time.getText()), Toast.LENGTH_SHORT).show();

    }

    /**
     * Add padding to numbers less than ten
     */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





      /*  View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        Intent intent = getIntent();
        message = intent.getStringExtra(Login.EXTRA_MESSAGE);
        sMapFragment = SupportMapFragment.newInstance();
        setContentView(R.layout.activity_main);

        session = new SessionHandel(this);



        start_time = (TextView) findViewById(R.id.Start_time);
        start_btn = (Button) findViewById(R.id.Start_btn);
        end_time = (TextView) findViewById(R.id.End_time);
        end_btn = (Button) findViewById(R.id.End_btn);


        b1 = findViewById(R.id.Start_btn);
        b1.setVisibility(View.GONE);

        b2 = findViewById(R.id.Start_time);
        b2.setVisibility(View.GONE);

        b3 = findViewById(R.id.End_time);
        b3.setVisibility(View.GONE);

        b4 = findViewById(R.id.End_btn);
        b4.setVisibility(View.GONE);

        b5 = findViewById(R.id.reset);
        b5.setVisibility(View.GONE);

        b6 = findViewById(R.id.cancel_trip);
        b6.setVisibility(View.GONE);

        proceed = (Button)findViewById(R.id.proceed);
        proceed.setVisibility(View.INVISIBLE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        spinner = (Spinner) findViewById(R.id.car_spinner);
        spinner.setVisibility(View.GONE);

        time_message = (TextView)findViewById(R.id.time_message);
        time_message.setVisibility(View.INVISIBLE);


        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        ImageView imgv = (ImageView) findViewById(R.id.imageView1);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);





        name = session.getusername();

        //t = (TextView)findViewById(R.id.user_name);
        if (session.getusername().isEmpty()) {
            Intent it = new Intent(MainActivity.this, Login.class);
            startActivity(it);
            finish();
        }



        imgv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                t = (TextView) findViewById(R.id.user_name);
                if (name.isEmpty())
                    name = "Welcome Guest";
                t.setText(name);
                drawer.openDrawer(Gravity.START);


            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView t1 = (TextView)header.findViewById(R.id.user_name);
        t1.setText(session.getusername());


        sMapFragment.getMapAsync(this);


        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        if (!sMapFragment.isAdded()) {
            sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
        } else
            sFm.beginTransaction().show(sMapFragment).commit();

         autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);

        autocompleteFragment.setMenuVisibility(false);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //  Log.i(TAG, "Place: " + place.getName());
                mMap.clear();

                myMarkersrc.setDraggable(false);
                LatLng destlocation = place.getLatLng();

                if (location_enabled == true) {


                        fab.setEnabled(true);

                    setLocationMarker(srclocation.latitude, srclocation.longitude, 1);
                    destination = destlocation;

                    myMarkersrc.setDraggable(false);


                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker arg0) {
                            // TODO Auto-generated method stub


                            Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void onMarkerDragEnd(Marker arg0) {
                            // TODO Auto-generated method stub
                            Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                            double dest_lattitude = arg0.getPosition().latitude;
                            double dest_longitude = arg0.getPosition().longitude;
                            destination = new LatLng(dest_lattitude, dest_longitude);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                        }

                        @Override
                        public void onMarkerDrag(Marker arg0) {
                            // TODO Auto-generated method stub
                            Log.i("System out", "onMarkerDrag...");
                        }
                    });


                    setLocationMarker(destination.latitude, destination.longitude, 2);
                    b1.setVisibility(View.VISIBLE);
                    b2.setVisibility(View.VISIBLE);
                    b3.setVisibility(View.VISIBLE);
                    b4.setVisibility(View.VISIBLE);
                    b5.setVisibility(View.VISIBLE);
                    time_message.setVisibility(View.VISIBLE);
                    proceed.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(), "Please Navigate to Current Location", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }


              /* String url = getDirectionsUrl(destloc , destlocation);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);*/


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                //  Log.i(TAG, "An error occurred: " + status);
            }
        });


        /** Listener for click event of the button */
        start_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_START);
            }
        });


        end_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_END);



            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));



            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               time_message.setText("");
                end_time.setText("");
                start_time.setText("");



            }
        });



        proceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                b1.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
                b4.setVisibility(View.INVISIBLE);
                b5.setVisibility(View.INVISIBLE);
                show_cars.setVisibility(View.VISIBLE);
                proceed.setVisibility(View.INVISIBLE);

            }
        });

        adapters = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        spinner.setAdapter(adapters);

        show_cars = (Button) findViewById(R.id.show_cars);
        show_cars.setVisibility(View.INVISIBLE);


        dropdownQueue = Volley.newRequestQueue(this);

        show_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);


                // Add new Items to List
                dropdownRequest = new StringRequest(Request.Method.POST, URL_SHOW_CAR, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                int i = 0;
                                while (!jsonObject.getString(String.valueOf(i)).equals("end")) {

                                    options.add(jsonObject.getString(String.valueOf(i)));
                                    //Toast.makeText(getApplicationContext(), jsonObject.getString(String.valueOf(i)), Toast.LENGTH_SHORT).show();
                                    i++;


                                }
                                adapters.notifyDataSetChanged();
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
                        hashMap.put("id", session.getuserId());
                        //  hashMap.put("id", session.getuserId());
                        //  Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                        //fab.setEnabled(false);


                        return hashMap;
                    }
                };

                dropdownQueue.add(dropdownRequest);

                adapters.notifyDataSetChanged();
                show_cars.setVisibility(View.INVISIBLE);

            }


        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                car_selected = parent.getItemAtPosition(position).toString();
                fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });


        /** Get the current time */
        final Calendar cal = Calendar.getInstance();
        sHour = cal.get(Calendar.HOUR_OF_DAY);
        sMinute = cal.get(Calendar.MINUTE);
        //updateDisplay();
        eHour = cal.get(Calendar.HOUR_OF_DAY);
        eMinute = cal.get(Calendar.MINUTE);
       // updateDisplayEnd();

        start_time_park = new StringBuilder()
                .append(pad(sHour)).append(":")
                .append(pad(sMinute));
        end_time_park = new StringBuilder()
                .append(pad(eHour)).append(":")
                .append(pad(eMinute));

        /** Display the current time in the TextView */


        requestQueue = Volley.newRequestQueue(this);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

                //   Toast.makeText(getApplicationContext(),"gugugaga"+ name, Toast.LENGTH_SHORT).show();

                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                parklat = Double.parseDouble(jsonObject.getString("lat"));
                                parklong = Double.parseDouble(jsonObject.getString("long"));
                                setLocationMarker(parklat, parklong, 3);
                                parkloc = new LatLng(parklat, parklong);

                                String url = getDirectionsUrl(destination, parkloc);

                                DownloadTask downloadTask = new DownloadTask();

                                // Start downloading json data from Google Directions API
                                downloadTask.execute(url);

                                /* destlat =  Double.parseDouble(jsonObject.getString("lat"));
                                destlong = Double.parseDouble(jsonObject.getString("long"));
                                setLocationMarker(destlat,destlong,3);
                                destloc = new LatLng(destlat,destlong);*/
                                String urlnew = getDirectionsUrl(srclocation, parkloc);
                                DownloadTask downloadTask1 = new DownloadTask();
                                downloadTask1.execute(urlnew);
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                b1.setVisibility(View.GONE);
                                b2.setVisibility(View.GONE);
                                b3.setVisibility(View.GONE);
                                b4.setVisibility(View.GONE);
                                spinner.setVisibility(View.GONE);
                                b5.setVisibility(View.INVISIBLE);
                                b6.setVisibility(View.VISIBLE);

                               /* setLocationMarker(22.5145,88.4033,3);
                                destloc = new LatLng(22.5145,88.4033);
                                String url = getDirectionsUrl(srclocation , destloc);
                                DownloadTask downloadTask = new DownloadTask();
                                // Start downloading json data from Google Directions API
                                downloadTask.execute(url);*/
                                Toast.makeText(getApplicationContext(), "BOOKING SUCCESSFULL", Toast.LENGTH_LONG).show();

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
                        hashMap.put("user_lat", String.valueOf(srclocation.latitude));
                        hashMap.put("user_long", String.valueOf(srclocation.longitude));
                        hashMap.put("dest_lat", String.valueOf(destination.latitude));
                        hashMap.put("dest_long", String.valueOf(destination.longitude));
                        hashMap.put("id", session.getuserId());
                        hashMap.put("car_no", car_selected);
                        hashMap.put("start_time",start_time_park.toString());
                        hashMap.put("end_time",end_time_park.toString());



                        //fab.setEnabled(false);

                        return hashMap;
                    }
                };

                requestQueue.add(request);

                fab.setEnabled(false);
            }
        });


    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_START:
                return new TimePickerDialog(this,
                        mTimeSetListener, sHour, sMinute, false);
            case TIME_DIALOG_END:
                return new TimePickerDialog(this,
                        mTimeSetListener_end, eHour, eMinute, false);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        } else if (!session.getusername().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Parking Allocation")
                    .setMessage("Are you sure you want to Exit")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Login) {
            if (session.getusername().isEmpty()) {
                Intent it = new Intent(MainActivity.this, Login.class);
                startActivity(it);
            } else
                Toast.makeText(getApplicationContext(), "You are already LOGGED IN", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_Rate) {
            startActivity(new Intent(getApplicationContext(), RateCard.class));

        } else if (id == R.id.car_details) {
            Intent i = new Intent(getApplicationContext(), CarDetails.class);
            startActivity(i);


        } else if (id == R.id.nav_Payments) {

        } else if (id == R.id.nav_Logout) {


            session.destroySession();
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_share) {
           /* Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Parking Allocation");
            intent.putExtra(Intent.EXTRA_TEXT, "link to parking allocation app");
            Intent mailer = Intent.createChooser(intent, null);
            startActivity(mailer);*/

            Intent intent = new Intent(ACTION_SENDTO);
            // intent.setType("text/plain");
            intent.setType("message/rfc822");
            String mailTo = "kumarvijay2510@gmail.com";
            String mailCC = "";
            String subject = "Parking allocation app";
            String body = "Link to parking allocation";
            if (mailTo == null) {
                mailTo = "";
            }
            intent.setData(Uri.parse("mailto:" + mailTo));
            if (!isEmpty(mailCC)) {
                intent.putExtra(Intent.EXTRA_CC, new String[]{mailCC});
            }
            if (!isEmpty(subject)) {
                intent.putExtra(EXTRA_SUBJECT, subject);
            }
            if (isEmpty(body)) {
                intent.putExtra(EXTRA_TEXT, body);
            }
            startActivity(Intent.createChooser(intent, "Sending"));


        } else if (id == R.id.nav_Support) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //map.clear();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(true);
        // mMap.setOnMyLocationButtonClickListener(this);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.imgMyLocation);
        fab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                onMyLocationButtonClick();
                enableMyLocation();
                location_enabled = true;
                autocompleteFragment.setMenuVisibility(true);

            }
        });


//
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        gps = new TrackGps(MainActivity.this);
        mMap.clear();


        if (gps.canGetLocation()) {


            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            setLocationMarker(latitude, longitude, 1);
            srclocation = new LatLng(latitude, longitude);

            //final LatLng current = new LatLng(latitude, longitude);
            //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
            //mMap.addMarker(new MarkerOptions().position(current));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(srclocation, 16.0f));
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker arg0) {
                    // TODO Auto-generated method stub


                    Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                }

                @SuppressWarnings("unchecked")
                @Override
                public void onMarkerDragEnd(Marker arg0) {
                    // TODO Auto-generated method stub
                    Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                    double src_lattitude = arg0.getPosition().latitude;
                    double src_longitude = arg0.getPosition().longitude;
                    srclocation = new LatLng(src_lattitude, src_longitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                }

                @Override
                public void onMarkerDrag(Marker arg0) {
                    // TODO Auto-generated method stub
                    Log.i("System out", "onMarkerDrag...");
                }
            });
            location_enabled = true;


            // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            gps.showSettingsAlert();
        }

        return false;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void setLocationMarker(Double latitude, Double longitude, int ids) {
        byte cnt1 = 0, cnt2 = 1;
        final LatLng current = new LatLng(latitude, longitude);
        //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        if (ids == 1) {
            // for source

            if (myMarkersrc != null) {
                myMarkersrc.remove();
                myMarkersrc = null;
            }
            myMarkersrc = mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.src_marker)).draggable(true));
        }


        if (ids == 2) {
            // for destination

            if (myMarkerDest != null) {
                myMarkerDest.remove();
                myMarkerDest = null;
            }


            myMarkerDest = mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.dest_marker)).draggable(true));

        }
        if (ids == 3) {


            if (myparking != null) {
                myparking.remove();
                myparking = null;
            }
            myparking = mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            // Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // /Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }

            //tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }
}