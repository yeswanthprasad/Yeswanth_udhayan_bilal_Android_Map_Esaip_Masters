package com.example.yeswanth.mapapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.maps.CameraUpdateFactory.*;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mMap;
    GoogleApiClient mGoogleapiClient;
    private static final String TAG = "MapsActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            /*  Showing the Perfect when the app is opend or performed  */
            Toast.makeText(this, "Perfect", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_maps);
            /*   Here initMap is to layout or fragment and executing here  */
            initMap();
            editText = (EditText) findViewById(R.id.editText);
            btnAdd = (Button) findViewById(R.id.btnAdd);
            btnView = (Button) findViewById(R.id.btnView);
            mDatabaseHelper = new DatabaseHelper(this);

            btnAdd.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                String newEntry = editText.getText().toString();
                if (editText.length() != 0){
                    AddData(newEntry);
                    editText.setText("");
                }else {
                    toastMessage("You must put something in the text field");
                }

                }
            });


            btnView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(MapsActivity.this,ListDataActivity.class);
                    startActivity(intent);

                }
            });




        } else {
            //no Google Map Layout
        }
    }
    public void AddData(String newEntry){
    boolean insertData = mDatabaseHelper.addData(newEntry);
    if (insertData){
        toastMessage("DATA SUCCESSFULLY INSEARTED");
    }else {
        toastMessage("Something wrong");
    }
    }




    private void toastMessage(String message){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void initMap() {
        /*  Fragement is calling by the id in the Design activity_maps.xml  */
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        /*  Creatig the API object and cheaking the connection HERE */
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        /* If the connection was NOT SUCCESS and there is any error then show the ERROR dialog  */
        else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            /* If Connection is NOT there making the Text CANT CONNECT THE PLAY SERVICES  */
            Toast.makeText(this, "CANT CONNECT THE PLAY SERVICES", Toast.LENGTH_LONG).show();
        }
        return false;


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*  Going to the Location that is having the langitude and longitude and the zoom  */
          gotolocationzoom(47.464054,-0.4973334, 15);


        //   if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //     // TODO: Consider calling
        //   //    ActivityCompat#requestPermissions
        // // here to request the missing permissions, and then overriding
        //     //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //   //                                          int[] grantResults)
        // // to handle the case where the user grants the permission. See the documentation
        // // for ActivityCompat#requestPermissions for more details.
        //  return;
        // }
        //mMap.setMyLocationEnabled(true);

 //       mGoogleapiClient = new GoogleApiClient.Builder(this)
   //             .addApi(LocationServices.API)
     //           .addConnectionCallbacks(this)
       //         .addOnConnectionFailedListener(this)
         //       .build();
//
       // mGoogleapiClient.connect();
    }

    private void gotolocation(double lat, double lon) {
        LatLng ll = new LatLng(lat, lon);
        CameraUpdate update = newLatLng(ll);
        mMap.moveCamera(update);
    }

    /*  Taking the Location and Giving the latitude and the Longitude and ZOOM of the required Location Updating the Map */
    private void gotolocationzoom(double lat, double lon, float zoom) {
        LatLng ll = new LatLng(lat, lon);
        CameraUpdate update = newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    /*  getting the address from the all the text and taking the latitude and logitude and giving the results */
    public void geoLocate(View view) throws IOException {
        EditText et = (EditText) findViewById(R.id.editText);
        String location = et.getText().toString();
        Geocoder ge = new Geocoder(this);
        List<Address> list = ge.getFromLocationName(location, 1);
        Address address = list.get(0);
        String Locality = address.getLocality();

        Toast.makeText(this, Locality, Toast.LENGTH_LONG).show();

        Double lat = address.getLatitude();
        Double lon = address.getLongitude();
        gotolocationzoom(lat, lon, 15);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleapiClient, mLocationRequest, (LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null){
            Toast.makeText(this, "Cant get current Location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mMap.animateCamera(update);
        }

    }
}
