package com.example.vijay.parking_allocation;


import android.Manifest;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    Button btn1;
    private RequestQueue requestQueue;
    private StringRequest request;

    String message;
    FloatingActionButton fab;
    static boolean flag = false;
    static boolean location_enabled = false;


    double parklat = 0.0;
    double parklong = 0.0;
    LatLng parkloc;

    Marker myMarkersrc = null, myMarkerDest = null, myparking = null;
    DrawerLayout drawer;
    SessionHandel session;
    String name = null;
    TextView t;
    FloatingActionButton imgMyLocation;

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
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        ImageView imgv = (ImageView) findViewById(R.id.imageView1);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        session = new SessionHandel(getApplicationContext());
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
        sMapFragment.getMapAsync(this);


        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        if (!sMapFragment.isAdded()) {
            sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
        } else
            sFm.beginTransaction().show(sMapFragment).commit();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);





                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        //  Log.i(TAG, "Place: " + place.getName());

                        if(location_enabled == true) {

                            final LatLng destlocation = place.getLatLng();
                            fab.setEnabled(true);
                            mMap.clear();


                            destination = destlocation;
                            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker arg0) {
                                    // TODO Auto-generated method stub


                                    Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                                }

                                @SuppressWarnings("unchecked")
                                @Override
                                public void onMarkerDragEnd(Marker arg0) {
                                    // TODO Auto-generated method stub
                                    Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
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

                            setLocationMarker(srclocation.latitude, srclocation.longitude, 1);
                            setLocationMarker(destination.latitude, destination.longitude, 2);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please Navigate to Current Location", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

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




        requestQueue = Volley.newRequestQueue(this);


        fab = (FloatingActionButton) findViewById(R.id.fab);


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
                               /* setLocationMarker(22.5145,88.4033,3);
                                destloc = new LatLng(22.5145,88.4033);
                                String url = getDirectionsUrl(srclocation , destloc);
                                DownloadTask downloadTask = new DownloadTask();
                                // Start downloading json data from Google Directions API
                                downloadTask.execute(url);*/

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
                        hashMap.put("user_id", session.getusername());
                        //fab.setEnabled(false);


                        return hashMap;
                    }
                };

                requestQueue.add(request);

                fab.setEnabled(false);
            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        } else if(!session.getusername().isEmpty()) {
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
            startActivity(Intent.createChooser(intent,"Sending"));




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
                location_enabled=true;

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


                    Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                }

                @SuppressWarnings("unchecked")
                @Override
                public void onMarkerDragEnd(Marker arg0) {
                    // TODO Auto-generated method stub
                    Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
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
            location_enabled= true;


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
            myMarkersrc = mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true));
        }


        if (ids == 2) {
            // for destination

            if (myMarkerDest != null) {
                myMarkerDest.remove();
                myMarkerDest = null;
            }


            myMarkerDest = mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true));

        }
        if (ids == 3) {


            if (myparking != null) {
                myparking.remove();
                myparking = null;
            }
            myparking = mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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